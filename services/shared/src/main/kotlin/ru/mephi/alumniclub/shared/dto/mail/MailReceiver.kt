package ru.mephi.alumniclub.shared.dto.mail

class MailReceiver(
    val email: String,
    val personalData: MutableMap<String, String> = mutableMapOf(),
) {
    override fun toString(): String {
        return "MailReceiver(email='$email', personalData=$personalData)"
    }
}