package ru.mephi.alumniclub.app.service.impl

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.config.properties.ServerUrlsProperties
import ru.mephi.alumniclub.app.database.entity.notification.NotificationHolder
import ru.mephi.alumniclub.app.database.entity.publication.AbstractPublication
import ru.mephi.alumniclub.app.database.entity.publication.Event
import ru.mephi.alumniclub.app.database.entity.publication.Publication
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.service.*
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.util.*

@Service
class FullPublicationServiceImpl(
    @Lazy
    private val publicationService: PublicationService,
    @Lazy
    private val surveyService: SurveyService,
    @Lazy
    private val broadcastService: BroadcastService,
    @Lazy
    private val atomService: AtomService,
    private val serverUrlsProperties: ServerUrlsProperties
) : ResponseManager(), FullPublicationService {

    override fun getAbstractPublication(id: UUID): AbstractPublication {
        return getAbstractPublicationOrNull(id) ?: throw ResourceNotFoundException(AbstractPublication::class.java, id)
    }

    override fun getAbstractPublicationOrNull(id: UUID): AbstractPublication? {
        val publication = publicationService.getCommonPublication(id)
        if (publication != null) return publication
        val survey = surveyService.findEntityOrNullById(id)
        if (survey != null) return survey
        val broadcast = broadcastService.findEntityOrNullById(id)
        if (broadcast != null) return broadcast
        return null
    }

    override fun getNotificationHolder(id: UUID): NotificationHolder {
        return getNotificationHolderOrNull(id) ?: throw ResourceNotFoundException(NotificationHolder::class.java, id)
    }

    override fun getNotificationHolderOrNull(id: UUID): NotificationHolder? {
        val publication = getAbstractPublicationOrNull(id)
        if (publication != null) return publication
        val atom = atomService.findAtomEntityOrNullById(id)
        if (atom != null) return atom
        return null
    }

    override fun getRedirectLinkToPublication(id: UUID): String {
        val publication = publicationService.getCommonPublication(id) ?: return serverUrlsProperties.homePage
        val suffix = if (publication is Publication) "news" else "events"
        val humanUrl = if (publication is Publication) publication.humanUrl else (publication as Event).humanUrl
        return "${serverUrlsProperties.fullBaseUrl}/$suffix/$humanUrl"
    }

    override fun existsNotificationHolder(publicationId: UUID): Boolean {
        return getNotificationHolderOrNull(publicationId) != null
    }

    override fun getPhotoUrl(id: UUID): String {
        return "${serverUrlsProperties.fullBaseUrl}/api/v1/public/publication-photo/${id}"
    }

    override fun getMailLinkToPublication(id: UUID): String {
        return "${serverUrlsProperties.fullBaseUrl}/api/v1/public/feed/publication-broadcast/redirect/$id"
    }
}
