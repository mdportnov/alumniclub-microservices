package ru.mephi.alumniclub.app.model.mapper.fcm

import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.push_sdk.android.AndroidNotification
import ru.mephi.push_sdk.android.ClickAction
import ru.mephi.push_sdk.message.AndroidConfig
import ru.mephi.push_sdk.message.Notification
import ru.mephi.push_sdk.model.Urgency


@Component
class HuaweiPushMapper {

    fun buildAndroidNotification(push: BroadcastRequest): AndroidNotification {
        return AndroidNotification.builder()
            .setTitle(push.title)
            .setBigTitle(push.title)
            .setBody(push.content)
            .setBigBody(push.content)
            .setClickAction(ClickAction.builder().setType(3).build())
            .build()
    }

    fun buildAndroidConfig(push: BroadcastRequest): AndroidConfig {
        val notification = buildAndroidNotification(push)
        return AndroidConfig.builder().setCollapseKey(-1)
            .setUrgency(Urgency.HIGH.value)
            .setTtl("864000s")
            .setBiTag(push.title)
            .setNotification(notification)
            .build()
    }

    fun buildNotification(push: BroadcastRequest): Notification {
        return Notification.builder()
            .setTitle(push.title)
            .setBody(push.content)
            .build()
    }
}