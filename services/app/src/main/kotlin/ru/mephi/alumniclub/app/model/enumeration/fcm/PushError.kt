package ru.mephi.alumniclub.app.model.enumeration.fcm

enum class PushError {
    THIRD_PARTY_AUTH_ERROR,
    INVALID_ARGUMENT,
    INTERNAL,
    QUOTA_EXCEEDED,
    SENDER_ID_MISMATCH,
    UNAVAILABLE,
    INVALID_TOKEN,
}