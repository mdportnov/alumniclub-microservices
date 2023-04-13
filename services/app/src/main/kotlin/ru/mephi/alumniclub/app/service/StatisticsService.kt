package ru.mephi.alumniclub.app.service

import ru.mephi.alumniclub.app.model.dto.statistics.ParameterResponse

interface StatisticsService {
    fun getStatistics(): List<ParameterResponse>
}
