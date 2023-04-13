package ru.mephi.alumniclub.app.service

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import ru.mephi.alumniclub.app.database.repository.MerchDao
import ru.mephi.alumniclub.app.model.dto.atom.request.MerchRequest
import ru.mephi.alumniclub.app.model.dto.atom.response.MerchResponse
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.service.helpers.UserHelper
import ru.mephi.alumniclub.app.service.impl.MerchServiceImpl
import ru.mephi.alumniclub.shared.util.StringGenerator
import kotlin.random.Random

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.yml"])
@ActiveProfiles("test")
class IMerchServiceTest {
    @Autowired
    lateinit var userHelper: UserHelper

    @Autowired
    lateinit var merchService: MerchServiceImpl

    @Autowired
    lateinit var stringGenerator: StringGenerator

    @Autowired
    lateinit var merchDao: MerchDao

    @BeforeEach
    fun prepareData() {
        userHelper.toggleVerification()
        cleanData()
    }

    @AfterEach
    fun cleanData() {
        userHelper.deleteAll()
        merchDao.deleteAll()
    }

    fun randomMerchRequest(availability: Boolean? = null) = MerchRequest(
        stringGenerator.getRandomString(20), stringGenerator.getRandomString(30),
        Random.nextInt(1, 99), availability ?: Random.nextBoolean()
    )

    @Test
    fun `create merch and get it `() {
        val request = MerchRequest("1234", "4refd", 418, true)
        assertDoesNotThrow { merchService.createMerch(request) }
        val listOfMerch = merchService.getAllAvailableMerch()
        assertEquals(1, listOfMerch.size)
        assertDoesNotThrow { merchService.findMerchEntityById(listOfMerch.first().id) }
    }

    @Test
    fun `test saved data`() {
        val size = 10
        val responses = mutableListOf<MerchResponse>()
        var availabilities = 0
        repeat(size) {
            val available = Random.nextBoolean()
            if (available) availabilities++
            val request = randomMerchRequest(available)
            assertDoesNotThrow { responses.add(merchService.createMerch(request)) }
            assertEquals(it + 1, merchService.getAllMerch().size)
        }
        val allMerch = merchService.getAllMerch()
        assertEquals(size, allMerch.size)
        val allAvailableMerch = merchService.getAllAvailableMerch()
        assertEquals(availabilities, allAvailableMerch.size)
        responses.forEach { response ->
            val merch = allMerch.find { it.id == response.id }!!
            val entity = merchService.findMerchEntityById(response.id)
            assertEquals(
                true,
                response.cost == entity.cost && response.availability == entity.availability
                        && response.name == entity.name && response.description == entity.description
            )
            assertEquals(
                true,
                response.cost == merch.cost && response.availability == merch.availability
                        && response.name == merch.name && response.description == merch.description
            )
            if (response.availability) {
                assertDoesNotThrow { allAvailableMerch.find { it.id == response.id }!! }
            }
        }
        assertEquals(availabilities, allAvailableMerch.size)
    }

    @Test
    fun `deleting merch`() {
        val createSize = 20
        val deleteSize = 10
        assertThrows<ResourceNotFoundException> {
            merchService.deleteMerch(Random.nextLong())
        }
        val responses = mutableListOf<MerchResponse>()
        repeat(createSize) {
            val request = randomMerchRequest()
            responses.add(merchService.createMerch(request))
        }
        val deleted = mutableListOf<MerchResponse>()
        val copyResponses = mutableListOf(*responses.toTypedArray())
        repeat(deleteSize) {
            deleted.add(copyResponses.random())
            copyResponses.remove(deleted.last())
            assertDoesNotThrow { merchService.deleteMerch(deleted.last().id) }
        }
        val all = merchService.getAllMerch()
        assertEquals(all.size, createSize - deleteSize)
        responses.forEach { response ->
            if (all.find { it.id == response.id } == null) {
                assertThrows<ResourceNotFoundException> {
                    merchService.findMerchEntityById(response.id)
                }
                assertThrows<ResourceNotFoundException> {
                    merchService.deleteMerch(response.id)
                }
            } else {
                assertDoesNotThrow {
                    merchService.findMerchEntityById(response.id)
                    merchService.deleteMerch(response.id)
                }
                assertThrows<ResourceNotFoundException> { merchService.deleteMerch(response.id) }
            }
        }
    }

    @Test
    fun `test update merch`() {
//        repeat(5) { // TODO
//            assertThrows<ResourceNotFoundByIdException> {
//                merchService.updateMerch(Random.nextLong(), randomMerchRequest())
//            }
//        }
        repeat(5) {
            val request = randomMerchRequest()
            val response = merchService.createMerch(request)
            val newRequest = randomMerchRequest()
            val newResponse = merchService.updateMerch(response.id, newRequest)
            assertEquals(
                true,
                newResponse.cost == newRequest.cost && newResponse.availability == newRequest.availability &&
                        newResponse.name == newRequest.name && newResponse.description == newRequest.description
            )
            val entity = merchService.findMerchEntityById(response.id)
            assertEquals(
                true,
                entity.cost == newRequest.cost && entity.availability == newRequest.availability &&
                        entity.name == newRequest.name && entity.description == newRequest.description
            )
        }
    }

    @Test
    fun `test upload photo`() {
        // ???
    }
}
