package ru.mephi.alumniclub.app.util.extension

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import ru.mephi.push_sdk.exception.HuaweiMesssagingException
import ru.mephi.push_sdk.response.SendResponse


/**
 * sample of message :
 *  """error code : 80100000, error message : {"success":1,"failure":2,"illegal_tokens":["######","11111111"]}"""
 */
fun HuaweiMesssagingException.toSendResponse(): SendResponse {
    val index = message?.indexOf(",") ?: -1
    val code = message?.slice(13 until index) ?: "-1"
    return SendResponse.fromCode(code, this.message, "-1")
}


fun getInvalidHuaweiTokens(response: SendResponse): List<String> {
    return try {
        val mapRef = object : TypeReference<HashMap<String, Any>>() {}
        val start = response.msg.indexOf("{")
        val result = ObjectMapper().readValue(response.msg.substring(start), mapRef)!!
        result["illegal_tokens"] as List<String>
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}