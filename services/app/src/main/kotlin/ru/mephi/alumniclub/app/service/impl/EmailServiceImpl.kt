package ru.mephi.alumniclub.app.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.config.properties.ServerUrlsProperties
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastByPublicationRequest
import ru.mephi.alumniclub.app.model.mapper.broadcast.BroadcastSenderMapper
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Service
class EmailServiceImpl(
    private val rabbitService: RabbitService,
    private val dataSenderHelper: DataSenderHelper,
    private val fullPublicationService: FullPublicationService,
    private val preferencesService: UserPreferencesService,
    private val linksManager: ServerUrlsProperties,
    private val mailMapper: BroadcastSenderMapper
) : ResponseManager(), EmailService {
    /**
     * Sends publication content mail to the list of receiversIds.
     *
     * @param request The [BroadcastByPublicationRequest] that contains the necessary data for creating the email.
     * @param ignorePreferences A flag that indicates whether to ignore the mail preferences of the recipients.
     */
    override fun sendPublicationContentMail(request: BroadcastByPublicationRequest, ignorePreferences: Boolean) {
        val mails = dataSenderHelper.getMailReceiversWithUnsubscribeFromMailToken(
            broadcastType = request.broadcastType,
            receiversIds = request.receiversIds,
            ignorePreferences = ignorePreferences
        )
        val mail = mailMapper.asPublicationContentMail(request = request, mails = mails)
        rabbitService.sendMessage(mail)
    }

    /**
     * Sends broadcast mail to the list of receiversIds.
     *
     * @param request The [BroadcastByPublicationRequest] that contains the necessary data for creating the email.
     * @param ignorePreferences A flag that indicates whether to ignore the mail preferences of the recipients.
     */
    override fun sendBroadcastMail(request: BroadcastByPublicationRequest, ignorePreferences: Boolean) {
        val mails = dataSenderHelper.getMailReceiversWithUnsubscribeFromMailToken(
            broadcastType = request.broadcastType,
            receiversIds = request.receiversIds,
            ignorePreferences = ignorePreferences
        )
        val mail = mailMapper.asBroadcastMail(request = request, mails = mails, ignorePreferences = ignorePreferences)
        rabbitService.sendMessage(mail)
    }
}
