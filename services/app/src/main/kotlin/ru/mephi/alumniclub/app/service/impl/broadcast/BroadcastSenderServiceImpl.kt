package ru.mephi.alumniclub.app.service.impl.broadcast

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.atom.Atom
import ru.mephi.alumniclub.app.database.entity.broadcast.Broadcast
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastByPublicationRequest
import ru.mephi.alumniclub.app.model.dto.notification.request.NotificationByPublicationRequest
import ru.mephi.alumniclub.app.model.enumeration.NotificationCategory
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import ru.mephi.alumniclub.app.model.mapper.broadcast.BroadcastSenderMapper
import ru.mephi.alumniclub.app.service.BroadcastSenderService
import ru.mephi.alumniclub.app.service.EmailService
import ru.mephi.alumniclub.app.service.NotificationService
import ru.mephi.alumniclub.app.service.PushService
import javax.transaction.Transactional

@Service
@Transactional
class BroadcastSenderServiceImpl(
    private val mapper: BroadcastSenderMapper,
    private val notificationService: NotificationService,
    private val emailService: EmailService,
    private val pushService: PushService,
) : BroadcastSenderService {

    override fun createBroadcast(userId: Long, request: BroadcastByPublicationRequest) {
        if (request.options.sendNotification)
            notificationService.create(mapper.asNotificationByPublicationRequest(request))
        if (request.options.sendPush)
            pushService.sendSimpleTextNotification(request, request.options.ignorePreferences)
        if (request.options.sendEmail)
            if (request.publication is Broadcast)
                emailService.sendBroadcastMail(request, request.options.ignorePreferences)
            else
                emailService.sendPublicationContentMail(request, request.options.ignorePreferences)
    }

    override fun createBroadcast(atom: Atom) {
        val notification = NotificationByPublicationRequest(atom.id, BroadcastType.USERS, listOf(atom.user.id))
        notificationService.create(notification)
        val broadcast = mapper.asBroadcastRequest(atom)
        val extraData = mapOf("category" to NotificationCategory.ATOMS.toString())
        pushService.sendSimpleTextNotification(broadcast, extraData = extraData)
    }
}
