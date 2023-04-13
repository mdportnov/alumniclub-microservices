package ru.mephi.alumniclub.app.service.impl.notification

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.community.Community
import ru.mephi.alumniclub.app.database.entity.fcm.FcmToken
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.community.UserCommunityDao
import ru.mephi.alumniclub.app.database.repository.fcm.FcmTokenDao
import ru.mephi.alumniclub.app.database.repository.user.UserDao
import ru.mephi.alumniclub.app.model.enumeration.fcm.BroadcastType
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.service.CommunityService
import ru.mephi.alumniclub.app.service.DataSenderHelper
import ru.mephi.alumniclub.app.service.UserPreferencesService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.app.service.impl.auth.JwtManager
import ru.mephi.alumniclub.shared.dto.mail.MailReceiver
import javax.transaction.Transactional

@Service
class DataSenderHelperImpl(
    private val userCommunityDao: UserCommunityDao,
    private val userDao: UserDao,
    private val preferencesService: UserPreferencesService,
    private val fcmTokenDao: FcmTokenDao,
    private val userService: UserService,
    private val communityService: CommunityService,
    private val jwtManager: JwtManager
) : DataSenderHelper {

    /**
     * Returns a list of users based on the given broadcast type and receiver IDs.
     *
     * @param broadcastType the type of broadcast for the list of receivers.
     * @param receiversIds the list of receiver IDs.
     * @return a list of users.
     */
    override fun getReceiversUsers(broadcastType: BroadcastType, receiversIds: List<Long>): List<User> {
        return when (broadcastType) {
            BroadcastType.USERS -> userDao.findAllById(receiversIds).toList()
            BroadcastType.COMMUNITIES -> userCommunityDao.findByCommunityIdIn(receiversIds).map { it.user }
            BroadcastType.ALL -> userDao.findAll().toList()
        }
    }

    /**
     * Returns the ids of users based on the broadcast type and the ids of the receivers.
     *
     * @param broadcastType type of broadcast.
     * @param receiversIds list of ids of receivers.
     * @return a list of user ids.
     */
    override fun getReceiversUsersIds(
        broadcastType: BroadcastType,
        receiversIds: List<Long>,
    ): List<Long> {
        return when (broadcastType) {
            BroadcastType.USERS -> receiversIds
            BroadcastType.COMMUNITIES -> userDao.findAllUsersIdsByBeInCommunities(receiversIds)
            BroadcastType.ALL -> userDao.findAllUsersIds()
        }
    }

    /**
     * Gets the FCM tokens for the receivers of a broadcast.
     *
     * @param broadcastType the type of broadcast (USERS, COMMUNITIES, or ALL)
     * @param receiversIds the IDs of the receivers
     * @param ignorePreferences whether to ignore the push notification preferences of the users
     * @return a list of FCM tokens for the receivers
     */
    override fun getReceiversFcmTokens(
        broadcastType: BroadcastType,
        receiversIds: List<Long>,
        ignorePreferences: Boolean
    ): List<FcmToken> {
        val ids = getReceiversUsersIds(broadcastType, receiversIds)
        val idsWithEnable = if (ignorePreferences) ids else {
            val idsWithEnablePush = preferencesService.findAllUsersIdsWithEnablePush(false)
            ids.filter { it in idsWithEnablePush }
        }
        return fcmTokenDao.getFbTokensByUsersId(idsWithEnable)
    }

    /**
     * Validates the receiver ids based on the broadcast type.
     * Throws a [ResourceNotFoundException] if any of the receiver ids is not found.
     *
     * @param broadcastType The type of broadcast
     * @param receiversIds The list of receiver ids
     * @throws ResourceNotFoundException If any of the receiver ids is not found
     */
    @Transactional
    override fun validateIdsOrThrow(broadcastType: BroadcastType, receiversIds: List<Long>) {
        when (broadcastType) {
            BroadcastType.USERS -> receiversIds.forEach {
                if (!userService.isUserExistById(it)) throw ResourceNotFoundException(User::class.java, it)
            }

            BroadcastType.COMMUNITIES -> receiversIds.forEach {
                if (!communityService.existById(it)) throw ResourceNotFoundException(Community::class.java, it)
            }

            BroadcastType.ALL -> return
        }
    }

    /**
     * Returns a list of [MailReceiver] objects with an unsubscribe email token for each user.
     *
     * @param broadcastType the type of broadcast to be sent
     * @param receiversIds a list of IDs for the recipients of the broadcast
     * @param ignorePreferences a flag indicating whether user email preferences should be ignored
     * @return a list of [MailReceiver] objects with an unsubscribe email token for each user
     */
    @Transactional
    override fun getMailReceiversWithUnsubscribeFromMailToken(
        broadcastType: BroadcastType, receiversIds: List<Long>, ignorePreferences: Boolean
    ): List<MailReceiver> {
        val ids = getReceiversUsersIds(broadcastType, receiversIds)
        return userService.findUserIdUserEmailPairs(ids)
            .map {
                val token = jwtManager.getToken(mapOf("userId" to it.userId))
                MailReceiver(it.email, mutableMapOf("unsubscribeEmailToken" to token))
            }
    }
}