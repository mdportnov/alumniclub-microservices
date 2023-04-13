package ru.mephi.alumniclub.app.service.scheduler

import com.google.firebase.messaging.SendResponse
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.fcm.ErroredFcmToken
import ru.mephi.alumniclub.app.database.entity.fcm.PushNotification
import ru.mephi.alumniclub.app.database.entity.notification.NotificationHolder
import ru.mephi.alumniclub.app.model.enumeration.user.ClientType
import ru.mephi.alumniclub.app.model.mapper.fcm.PushNotificationMapper
import ru.mephi.alumniclub.app.service.ErroredFcmTokenService
import ru.mephi.alumniclub.app.service.FullPublicationService
import ru.mephi.alumniclub.app.service.HuaweiPushNotificationSender
import ru.mephi.alumniclub.app.service.PushNotificationService
import ru.mephi.alumniclub.app.service.impl.push.FcmManager
import ru.mephi.alumniclub.app.util.extension.getInvalidHuaweiTokens
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import java.time.LocalDateTime
import javax.transaction.Transactional

@Component
class ErroredPushScheduler(
    private val fcmManager: FcmManager,
    private val huaweiManager: HuaweiPushNotificationSender,
    private val pushMapper: PushNotificationMapper,
    private val pushService: PushNotificationService,
    private val logger: AlumniLogger,
    private val erroredTokenService: ErroredFcmTokenService,
    private val fullPublicationService: FullPublicationService
) {
    @Scheduled(fixedDelay = 30 * 60 * 1000L) // 30 минут
    fun timingHandle() {
        logger.info("Start processing unsent push notifications")
        doHandling()
        logger.info("Processing of unsent push notifications has been completed")
    }

    /**
     * Foe each push notification:
     * If the push notification is old enough, delete it.
     * Else processes all tokens that were not sent due to an error sending this push
     */
    @Transactional
    fun doHandling() {
        pushService.findAll().forEach { push ->
            if (push.createdAt.isBefore(LocalDateTime.now().minusDays(2))) {
                logger.info(
                    """
                        Deleting push notification due to the expiration:
                        ${pushNotificationToString(push)}
                    """.trimIndent()
                )
                pushService.delete(push)
                return@forEach
            }
            val content = push.contentId?.let { contentId ->
                val content = fullPublicationService.getNotificationHolderOrNull(contentId)
                if (content == null) {
                    pushService.delete(push)
                    return@forEach
                }
                return@let content
            }
            handleErroredPush(push, content)
        }
    }

    /**
     * Attempts to resend a push notification to tokens that previously failed when pushing.
     * This method is similar and made in the likeness [ru.mephi.alumniclub.app.service.impl.push.PushSenderServiceImpl.sendSimpleTextNotification].
     *
     * @param push - [PushNotification] that will now be resent.
     * @param content - Content (publication/survey/event/...) to which the push is attached. Optional
     */
    private fun handleErroredPush(push: PushNotification, content: NotificationHolder?) {
        val (valid, invalids) = push.erroredTokens.partition { it.token.isValid }
        val (huawei, fcm) = valid.partition { it.token.fingerprint.type == ClientType.HUAWEI }

        val request = pushMapper.asRequest(push)
        val extra = pushMapper.buildExtraDataMap(content)

        val fcmResponses = fcmManager.sendToUsers(request, pushMapper.getTokens(fcm), extraData = extra)
        val huaweiResponse = huaweiManager.sendToUsers(request, pushMapper.getTokens(huawei), extraData = extra)

        handleFcmResponses(push, fcm, fcmResponses)
        handleHuaweiResponses(push, huawei, huaweiResponse)

    }

    /**
     * Processes all responses from FCM.
     * If it was possible to send a push to some token, then it deletes [ErroredFcmToken] associated with this token and this push
     *
     * @param push - [PushNotification] that was sended
     * @param tokens - List of tokens to handle
     * @param responses - Responses from FCM about status of every sended token
     */
    private fun handleFcmResponses(
        push: PushNotification,
        tokens: List<ErroredFcmToken>,
        responses: List<SendResponse>
    ) {
        responses.forEachIndexed { index, response ->
            if (!response.isSuccessful) return@forEachIndexed
            val token = tokens[index]
            erroredTokenService.delete(token)
        }
    }

    /**
     * Processes all responses from HUAWEI API.
     * If it was possible to send a push to some token, then it deletes [ErroredFcmToken] associated with this token and this push
     *
     * response.code sends from HUAWEI API in response. For example:
     *
     * 80000000 - All is OK.
     *
     * 80100000 - The message is successfully sent to some tokens. Tokens identified by illegal_tokens (Invalid tokens)
     *  are those to which the message failed to be sent.
     *
     * @see <a href="https://developer.huawei.com/consumer/en/doc/development/HMSCore-References/https-send-api-0000001050986197#section13968115715131">all statuses of push responses from HUAWEI API</a>
     *
     * @param push - [PushNotification] that was sended
     * @param tokens - List of tokens to handle
     * @param responses - Responses from FCM about status of every sended token
     */
    private fun handleHuaweiResponses(
        push: PushNotification,
        tokens: List<ErroredFcmToken>,
        responses: List<ru.mephi.push_sdk.response.SendResponse>
    ) {
        val batches = tokens.chunked(500)
        responses.forEachIndexed { index, response ->
            val batch = batches[index]
            when (response.code) {
                "80000000" -> {     // Success
                    batch.forEach { erroredTokenService.delete(it) }
                }

                "80100000" -> {     // The message is successfully sent to some tokens
                    val invalid = getInvalidHuaweiTokens(response)
                    batch.forEach { if (it.token.token !in invalid) erroredTokenService.delete(it) }
                }
            }
        }
    }

    private fun pushNotificationToString(push: PushNotification): String {
        return """
        Title: ${push.title}
        Broadcast type: ${push.broadcastType}
        Receivers: ${push.receiversIds}
        Text: ${push.text}
        """.trimIndent()
    }
}