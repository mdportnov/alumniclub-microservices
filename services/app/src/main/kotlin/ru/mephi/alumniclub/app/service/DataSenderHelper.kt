package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.database.entity.fcm.FcmToken
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import ru.mephi.alumniclub.shared.dto.mail.MailReceiver

interface DataSenderHelper {
    fun getReceiversUsers(broadcastType: BroadcastType, receiversIds: List<Long> = listOf()): List<User>
    fun getReceiversUsersIds(
        broadcastType: BroadcastType,
        receiversIds: List<Long> = listOf()
    ): List<Long>

    fun getReceiversFcmTokens(
        broadcastType: BroadcastType,
        receiversIds: List<Long> = listOf(),
        ignorePreferences: Boolean = false
    ): List<FcmToken>

    fun validateIdsOrThrow(
        broadcastType: BroadcastType,
        receiversIds: List<Long> = listOf(),
    )

    fun getMailReceiversWithUnsubscribeFromMailToken(
        broadcastType: BroadcastType,
        receiversIds: List<Long> = listOf(),
        ignorePreferences: Boolean = false
    ): List<MailReceiver>
}
