package ru.mephi.alumniclub.shared.dto.mail

import ru.mephi.alumniclub.shared.util.enums.RabbitMessageType
import java.io.Serializable

abstract class AbstractMail(
    val subject: String,
    val context: Map<String, Any> = emptyMap(),
    val receivers: List<MailReceiver>
) : Serializable {
    abstract val type: RabbitMessageType

    abstract fun templateLocation(): String

    override fun toString(): String {
        return "AbstractMail(receivers=$receivers, subject='$subject', context=$context)"
    }
}