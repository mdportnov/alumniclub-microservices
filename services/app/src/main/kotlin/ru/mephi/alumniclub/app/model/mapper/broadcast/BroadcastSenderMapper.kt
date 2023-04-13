package ru.mephi.alumniclub.app.model.mapper.broadcast

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.config.properties.ServerUrlsProperties
import ru.mephi.alumniclub.app.database.entity.atom.Atom
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.model.dto.broadcast.BroadcastOptionsDTO
import ru.mephi.alumniclub.app.model.dto.broadcast.request.AbstractBroadcastRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastByPublicationRequest
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.alumniclub.app.model.dto.notification.request.NotificationByPublicationRequest
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import ru.mephi.alumniclub.app.service.FullPublicationService
import ru.mephi.alumniclub.app.service.UserPreferencesService
import ru.mephi.alumniclub.app.util.parseArticle
import ru.mephi.alumniclub.shared.dto.mail.BroadcastMail
import ru.mephi.alumniclub.shared.dto.mail.MailReceiver
import ru.mephi.alumniclub.shared.dto.mail.PublicationContentMail

@Component
class BroadcastSenderMapper(
    private val fullPublicationService: FullPublicationService,
    private val preferencesService: UserPreferencesService,
    private val linksManager: ServerUrlsProperties
) {


    fun asNotificationByPublicationRequest(request: BroadcastByPublicationRequest): NotificationByPublicationRequest {
        return NotificationByPublicationRequest(
            publicationId = request.publication.id,
            broadcastType = request.broadcastType,
            receiversIds = request.receiversIds
        )
    }

    fun asBroadcastByPublication(
        publication: AbstractPublication, request: AbstractBroadcastRequest
    ): BroadcastByPublicationRequest {
        return BroadcastByPublicationRequest(
            broadcastType = request.broadcastType,
            receiversIds = request.receiversIds,
            options = request.options,
            publication = publication
        )
    }

    fun asBroadcastRequest(atom: Atom): BroadcastRequest {
        return BroadcastRequest(
            NotificationCategory.ATOMS.title,
            if (atom.sign) "На Ваш счёт начислили атомы!" else "С Вашего счёта списали атомы!",
            BroadcastType.USERS, listOf(atom.user.id),
            BroadcastOptionsDTO(),
        )
    }

    fun asPublicationContentMail(
        request: BroadcastByPublicationRequest,
        mails: List<MailReceiver>
    ): PublicationContentMail {
        return PublicationContentMail(
            photoLink = fullPublicationService.getPhotoUrl(request.publication.id),
            publicationLink = fullPublicationService.getMailLinkToPublication(request.publication.id),
            unsubscribeLink = preferencesService.getUnsubscribeFromMailLink(),
            title = request.publication.title,
            content = parseArticle(request.publication.content),
            date = request.publication.createdAt,
            baseUrl = linksManager.baseUrl,
            receivers = mails
        )
    }

    fun asBroadcastMail(
        request: BroadcastByPublicationRequest,
        mails: List<MailReceiver>,
        ignorePreferences: Boolean
    ): BroadcastMail {
        return BroadcastMail(
            receivers = mails,
            title = request.publication.title,
            content = request.publication.content,
            date = request.publication.createdAt,
            baseUrl = linksManager.baseUrl,
            photoLink = fullPublicationService.getPhotoUrl(request.publication.id),
            unsubscribeLink = preferencesService.getUnsubscribeFromMailLink(),
            ignorePreferences = ignorePreferences
        )
    }
}
