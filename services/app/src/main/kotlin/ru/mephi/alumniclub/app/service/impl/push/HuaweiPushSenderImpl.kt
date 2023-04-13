package ru.mephi.alumniclub.app.service.impl.push

import com.alibaba.fastjson.JSONObject
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.model.dto.broadcast.request.BroadcastRequest
import ru.mephi.alumniclub.app.model.mapper.fcm.HuaweiPushMapper
import ru.mephi.alumniclub.app.service.HuaweiPushNotificationSender
import ru.mephi.alumniclub.app.util.extension.toSendResponse
import ru.mephi.push_sdk.exception.HuaweiMesssagingException
import ru.mephi.push_sdk.message.Message
import ru.mephi.push_sdk.messaging.HuaweiMessaging
import ru.mephi.push_sdk.response.SendResponse


@Component
class HuaweiPushSenderImpl(
    private val huaweiMessaging: HuaweiMessaging,
    private val mapper: HuaweiPushMapper
) : HuaweiPushNotificationSender {
    override fun sendToUsers(
        push: BroadcastRequest,
        tokens: List<String>,
        extraData: Map<String, String>
    ): List<SendResponse> {
        if (tokens.isEmpty()) return emptyList()
        return tokens.chunked(500).map {
            try {
                sendPush(push, tokens, extraData)
            } catch (e: HuaweiMesssagingException) {
                e.printStackTrace()
                e.toSendResponse()
            } catch (e: Exception) {
                e.printStackTrace()
                SendResponse.fromCode("-1", e.message, "-1")
            }
        }
    }

    private fun sendPush(push: BroadcastRequest, tokens: List<String>, extraData: Map<String, String>): SendResponse {
        val notification = mapper.buildNotification(push)
        val androidConfig = mapper.buildAndroidConfig(push)
        val message = Message.builder().setNotification(notification)
            .setAndroidConfig(androidConfig)
            .addAllToken(tokens)
            .setData(JSONObject(extraData).toString())
            .build()
        return huaweiMessaging.sendMessage(message)
    }
}