package ru.mephi.alumniclub.app.service.impl

import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.repository.activity.UserActivityDao
import ru.mephi.alumniclub.app.database.repository.form.*
import ru.mephi.alumniclub.app.database.repository.user.UserDao
import ru.mephi.alumniclub.app.model.dto.statistics.ParameterResponse
import ru.mephi.alumniclub.app.service.StatisticsService
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class StatisticsServiceImpl(
    private val userDao: UserDao,
    private val userActivityDao: UserActivityDao,
    private val formBecomeMentor: FormBecomeMentorDao,
    private val formBuyMerch: FormBuyMerchDao,
    private val formFindMentorDao: FormFindMentorDao,
    private val formOfferCommunityDao: FormOfferCommunityDao,
    private val formOfferPartnershipDao: FormOfferPartnershipDao,
    private val formOfferPollDao: FormOfferPollDao
) : ResponseManager(), StatisticsService {

    private fun countUsersTotal(): ParameterResponse {
        return ParameterResponse(name = "Всего пользователей", userDao.count(), 0)
    }

    private fun countUsersLastWeek(): ParameterResponse {
        val time = LocalDateTime.now()
        val now = userDao.countByCreatedAtAfterAndCreatedAtBefore(time.minusWeeks(1), time)
        val prev = userDao.countByCreatedAtAfterAndCreatedAtBefore(time.minusWeeks(2), time.minusWeeks(1))
        return ParameterResponse("Новых пользователей за неделю", now, prev)
    }

    private fun countActiveUsersLastWeek(): ParameterResponse {
        val time = LocalDateTime.now()
        val now = userActivityDao.countDistinctUserByTimeAfterAndTimeBefore(time.minusWeeks(1), time)
        val prev = userActivityDao.countDistinctUserByTimeAfterAndTimeBefore(time.minusWeeks(2), time.minusWeeks(1))
        return ParameterResponse("Активных пользователей за неделю", now, prev)
    }

    private fun countActiveUsersLastDay(): ParameterResponse {
        val time = LocalDateTime.now()
        val date = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
        val now = userActivityDao.countDistinctUserByTimeAfterAndTimeBefore(date, time)
        val prev = userActivityDao.countDistinctUserByTimeAfterAndTimeBefore(date.minusDays(1), date)
        return ParameterResponse("Активных пользователей за день", now, prev)
    }

    private fun countFormsLastWeek(): ParameterResponse {
        val time = LocalDateTime.now()
        val date = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
        val forms = listOf(
            formBecomeMentor,
            formFindMentorDao,
            formBuyMerch,
            formOfferCommunityDao,
            formOfferPartnershipDao,
            formOfferPollDao
        )
        val now = forms.sumOf { it.countByCreatedAtAfterAndCreatedAtBefore(date, time) }
        val prev = forms.sumOf { it.countByCreatedAtAfterAndCreatedAtBefore(date.minusWeeks(1), date) }
        return ParameterResponse("Отправленных анкет за неделю", now, prev)
    }

    override fun getStatistics(): List<ParameterResponse> {
        return listOf(
            countUsersTotal(),
            countUsersLastWeek(),
            countActiveUsersLastWeek(),
            countActiveUsersLastDay(),
            countFormsLastWeek(),
        )
    }
}
