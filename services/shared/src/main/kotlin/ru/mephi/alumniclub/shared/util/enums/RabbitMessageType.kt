package ru.mephi.alumniclub.shared.util.enums


enum class RabbitMessageType(val path: String) {
    MAIL_PUBLICATION_CONTENT("mail.publication-content"),
    MAIL_RESET_PASSWORD("mail.reset-password"),
    MAIL_VERIFY_EMAIL("mail.verify-email"),
    MAIL_BROADCAST("mail.broadcast"),
    PUBLICATIONS_EVENT_QUEUE("publications.events"),
    PUBLICATIONS_USERS_FEEDBACK_QUEUE("publications.users-feedback"),
    CONTENT_PHOTO_QUEUE("content-photo"),
    IMAGE_THUMBNAIL_QUEUE("image.handle.thumbnail");

    fun fromPath(path: String): RabbitMessageType {
        RabbitMessageType.values().forEach {
            if (it.path == path) return it
        }
        throw Exception("Can't find RabbitMessageType with path [$path]")
    }
}