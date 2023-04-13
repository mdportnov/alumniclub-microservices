package ru.mephi.alumniclub.app.service.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.app.service.VerifyEmailService
import ru.mephi.alumniclub.shared.logging.AlumniLogger

@Service
class EmailVerificationScheduler(
    private val verifyEmailService: VerifyEmailService,
    private val userService: UserService,
    private val logger: AlumniLogger
) {
    /**
     * Checks if any email verification tokens have expired, and if so,
     * deletes the corresponding users.
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000L)
    fun handle() {
        logger.info("Start cleaning up users who have not confirmed their mail")
        verifyEmailService.findAllUnVerified().forEach {
            if (verifyEmailService.tokenIsExpired(it)) {
                logger.info("Deleting user with id [${it.user.id}] since the mail [${it.user.email}] is not confirmed")
                userService.delete(it.id)
            }
        }
        logger.info("Cleaning of users who have not confirmed the mail has been completed")
    }
}
