package ru.mephi.alumniclub.app.service.impl.push

import com.google.firebase.messaging.SendResponse
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import ru.mephi.alumniclub.app.database.entity.fcm.ErroredFcmToken
import ru.mephi.alumniclub.app.database.entity.fcm.FcmToken
import ru.mephi.alumniclub.app.database.entity.fcm.PushNotification
import ru.mephi.alumniclub.app.database.entity.preferences.UserPreferences
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastByPublicationRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.app.model.enumeration.fcm.PushError
import ru.mephi.alumniclub.app.model.enumeration.fcm.PushStatus
import ru.mephi.alumniclub.app.model.enumeration.user.ClientType
import ru.mephi.alumniclub.app.model.mapper.fcm.PushNotificationMapper
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.app.util.extension.asPushError
import ru.mephi.alumniclub.app.util.extension.getInvalidHuaweiTokens
import java.util.*

@Service
class PushSenderServiceImpl(
    private val fcmManager: FcmManager,
    private val huaweiManager: HuaweiPushNotificationSender,
    private val mapper: PushNotificationMapper,
    private val erroredFcmTokenService: ErroredFcmTokenService,
    private val pushService: PushNotificationService,
    private val helper: DataSenderHelper,
    @Lazy
    private val projectService: ProjectService,
) : PushService {
    /**
     * Sends push notification to users.
     *
     * Splits all tokens into valid and non-valid.
     * Valid ones are additionally divided into those related to FCM and those related to HUAWEI.
     * Sends a push to devices. Further, errors are processed from api FCM and HUAWEI, as well as invalid tokens.
     *
     * @param request - Indicates what should be in the push and to whom it should be sent.
     * @param ignorePreferences - If true, then ignores [UserPreferences] with sending push notifications
     * @param extraData - Additional information sent along with the push. Needed to go to the news when you click on the push
     * @param contentId - ID of the content (publication/survey/event/...) to which the push is attached. Optional
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    override fun sendSimpleTextNotification(
        request: BroadcastRequest,
        ignorePreferences: Boolean,
        extraData: Map<String, String>,
        contentId: UUID?
    ) {
        val push = pushService.save(mapper.asEntity(request, contentId))
        val tokens = helper.getReceiversFcmTokens(request.broadcastType, request.receiversIds, ignorePreferences)
        val (valid, invalids) = tokens.partition { it.isValid }
        val (huawei, fcm) = valid.partition { it.fingerprint.type == ClientType.HUAWEI }

        val fcmResponses = fcmManager.sendToUsers(request, mapper.asTokens(fcm), extraData)
        val huaweiResponse = huaweiManager.sendToUsers(request, mapper.asTokens(huawei), extraData)

        val erroredTokens = mutableListOf<ErroredFcmToken>()
        erroredTokens += handleHuaweiResponse(huaweiResponse, huawei, push)
        erroredTokens += handleFcmResponses(fcmResponses, fcm, push)
        erroredTokens += handleInvalidTokens(invalids, push)
        erroredFcmTokenService.saveAll(erroredTokens)

        push.status = if (erroredTokens.isEmpty()) PushStatus.SENDED else PushStatus.ERRORED
    }

    /**
     * Builds extraData by data from request [BroadcastByPublicationRequest].
     * Send push notification to users.
     *
     * @param request - Indicates what should be in the push and to whom it should be sent.
     * @param ignorePreferences - If true, then ignores [UserPreferences] with sending push notifications
     */
    override fun sendSimpleTextNotification(request: BroadcastByPublicationRequest, ignorePreferences: Boolean) {
        val publication = request.publication
        val data = mapper.buildExtraDataMap(publication)
        val push = mapper.asBroadcastForPush(request)
        if (publication.category == NotificationCategory.PROJECTS ||
            publication.category == NotificationCategory.EVENTS
        ) {
            projectService.findAbstractProjectByPublication(publication)?.let {
                push.title = it.name
            }
        }
        sendSimpleTextNotification(push, ignorePreferences, data, request.publication.id)
    }

    /**
     * Handles errors from FCM. It goes through all the answers from the FCM.
     * If an error occurred while sending a push to some token, it saves [ErroredFcmToken] and marks the type of error
     *
     * @param responses - Responses from FCM about status of every sended token
     * @param tokens - List of tokens to handle
     * @param push - [PushNotification] that was sended
     * @return list of [ErroredFcmToken]
     */
    private fun handleFcmResponses(
        responses: List<SendResponse>,
        tokens: List<FcmToken>,
        push: PushNotification
    ): List<ErroredFcmToken> {
        return responses.mapIndexedNotNull { index, response ->
            if (response.isSuccessful) return@mapIndexedNotNull null
            val status = response.asPushError()
            if (status == PushError.INVALID_TOKEN) tokens[index].isValid = false
            ErroredFcmToken(push, tokens[index], status)
        }
    }

    /**
     * Handles errors from HUAWEI. It goes through all the answers from the HUAWEI.
     * If an error occurred while sending a push to some token, it saves [ErroredFcmToken] and marks the type of error.
     *
     * response.code sends from HUAWEI API in response. For example:
     *
     * 80000000 - All is OK.
     *
     * 80100000 - The message is successfully sent to some tokens. Tokens identified by illegal_tokens (Invalid tokens)
     *  are those to which the message failed to be sent.
     *
     * 80300007 - All tokens are invalid.
     * @see <a href="https://developer.huawei.com/consumer/en/doc/development/HMSCore-References/https-send-api-0000001050986197#section13968115715131">all statuses of push responses from HUAWEI API</a>
     *
     *
     * @param responses - Responses from HUAWEI about status of every sended token
     * @param tokens - List of tokens to handle
     * @param push - [PushNotification] that was sended
     * @return list of [ErroredFcmToken]
     */
    private fun handleHuaweiResponse(
        responses: List<ru.mephi.push_sdk.response.SendResponse>,
        tokens: List<FcmToken>,
        push: PushNotification
    ): List<ErroredFcmToken> {
        val batches = tokens.chunked(500)
        return responses.flatMapIndexed { index, response ->
            val batch = batches[index]
            return@flatMapIndexed when (response.code) {
                "80000000" -> emptyList()  // Success
                "80100000" -> {     // The message is successfully sent to some tokens
                    val invalids = getInvalidHuaweiTokens(response)
                    batch.mapNotNull {
                        if (it.token !in invalids) return@mapNotNull null
                        it.isValid = false
                        ErroredFcmToken(push, it, PushError.INVALID_TOKEN)
                    }
                }

                "80300007" -> batch.map {// All tokens are invalid
                    it.isValid = false
                    ErroredFcmToken(push, it, PushError.INVALID_TOKEN)
                }

                else -> batch.map { ErroredFcmToken(push, it, PushError.INTERNAL) }
            }
        }
    }

    /**
     * Handles invalid tokens. Just creates a new [ErroredFcmToken] and marks like INVALID_TOKEN.
     *
     * @param tokens - List of invalid tokens to handle
     * @param push - [PushNotification] that was sended
     * @return list of [ErroredFcmToken]
     */
    private fun handleInvalidTokens(tokens: List<FcmToken>, push: PushNotification): List<ErroredFcmToken> {
        return tokens.map { token ->
            ErroredFcmToken(push, token, PushError.INVALID_TOKEN)
        }
    }
}