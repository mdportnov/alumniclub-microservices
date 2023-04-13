package ru.mephi.alumniclub.app.service

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.yml"])
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class IStatisticsServiceTest {

    @Autowired
    private lateinit var statisticsService: StatisticsService

    @Test
    fun injections() {
        assertTrue { ::statisticsService.isInitialized }
    }

    @Test
    fun `get statistics`() {
        assertDoesNotThrow { statisticsService.getStatistics() }
    }
}
