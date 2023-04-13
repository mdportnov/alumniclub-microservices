package ru.mephi.alumniclub.app.util.extension

import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.MessagingErrorCode
import com.google.firebase.messaging.SendResponse
import ru.mephi.alumniclub.app.model.enumeration.fcm.PushError

fun FirebaseMessagingException.asPushError(): PushError {
    if (this.messagingErrorCode == MessagingErrorCode.UNREGISTERED
        || (this.messagingErrorCode == MessagingErrorCode.INVALID_ARGUMENT)
        && this.message == "The registration token is not a valid FCM registration token"
    ) {
        return PushError.INVALID_TOKEN
    }
    return PushError.valueOf(this.messagingErrorCode.name)
}

fun SendResponse?.asPushError(): PushError {
    return this?.exception?.asPushError() ?: PushError.INVALID_TOKEN
}

