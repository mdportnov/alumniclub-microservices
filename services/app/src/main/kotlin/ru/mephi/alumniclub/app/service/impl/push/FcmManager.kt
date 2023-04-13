package ru.mephi.alumniclub.app.service.impl.push

import com.google.firebase.messaging.*
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest

@Service
class FcmManager(
    private val fcmClient: FirebaseMessaging
) {
    fun sendToUsers(
        push: BroadcastRequest,
        tokens: List<String>,
        extraData: Map<String, String> = mutableMapOf()
    ): List<SendResponse> {
        if (tokens.isEmpty()) return emptyList()
        return tokens.chunked(500).flatMap { chunk ->
            val message = buildMulticastMessage(push, chunk, extraData)
            fcmClient.sendMulticast(message).responses
        }
    }

    private fun buildMulticastMessage(
        push: BroadcastRequest,
        chunk: List<String>,
        extraData: Map<String, String> = mutableMapOf()
    ): MulticastMessage? {
        return MulticastMessage.builder()
            .addAllTokens(chunk)
            .setNotification(buildPushNotification(push))
            .setAndroidConfig(buildAndroidConfig(push, extraData))
            .setApnsConfig(buildIOSConfig(push, extraData))
            .putAllData(extraData)
            .build()
    }

    private fun buildPushNotification(push: BroadcastRequest): Notification {
        return Notification.builder()
            .setTitle(push.title)
            .setBody(push.content)
            .build()
    }

    private fun buildAndroidConfig(push: BroadcastRequest, extraData: Map<String, String>): AndroidConfig {
        return AndroidConfig.builder()
            .setNotification(
                AndroidNotification.builder()
                    .setTitle(push.title)
                    .setBody(push.content)
                    .setPriority(AndroidNotification.Priority.MAX)
                    .build()
            )
            .putAllData(extraData)
            .build()
    }

    private fun buildIOSConfig(push: BroadcastRequest, extraData: Map<String, String>): ApnsConfig {
        return ApnsConfig.builder().setAps(
            Aps.builder().setAlert(
                ApsAlert.builder()
                    .setTitle(push.title)
                    .setBody(push.content)
                    .build()
            ).putAllCustomData(extraData).build()
        ).setFcmOptions(
            ApnsFcmOptions.builder().build()
        ).putAllCustomData(extraData).build()
    }
}
