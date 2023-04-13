//package ru.mephi.alumniclub.app.service
//
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.TestPropertySource
//import ru.mephi.alumniclub.app.database.repository.user.UserDao
//import ru.mephi.alumniclub.app.model.dto.mentor.request.CreateMentorRequest
//import ru.mephi.alumniclub.app.model.dto.mentor.request.ToggleMentorAvailabilityRequest
//import ru.mephi.alumniclub.app.model.dto.mentor.request.UpdateMentorRequest
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceAlreadyExistsException
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceNotFoundException
//import ru.mephi.alumniclub.app.service.helpers.UserHelper
//import ru.mephi.alumniclub.app.service.helpers.mockPageRequest
//import ru.mephi.alumniclub.app.service.impl.AuthServiceImpl
//import ru.mephi.alumniclub.app.service.impl.MentorServiceImpl
//import ru.mephi.alumniclub.app.service.impl.UserServiceImpl
//import ru.mephi.alumniclub.shared.util.StringGenerator
//import javax.transaction.Transactional
//import kotlin.random.Random
//
//@SpringBootTest
//@TestPropertySource(locations = ["classpath:application-test.yml"])
//@ActiveProfiles("test")
//class IMentorServiceTest {
//    @Autowired
//    lateinit var userHelper: UserHelper
//
//    @Autowired
//    lateinit var userService: UserServiceImpl
//
//    @Autowired
//    lateinit var userDao: UserDao
//
//    @Autowired
//    lateinit var authService: AuthServiceImpl
//
//    @Autowired
//    lateinit var mentorService: MentorServiceImpl
//
//    @Autowired
//    lateinit var stringGenerator: StringGenerator
//
//    @BeforeEach
//    fun prepareData() {
//        userHelper.toggleVerification()
//        cleanData()
//    }
//
//    @AfterEach
//    fun cleanData() = userHelper.deleteAll()
//
//    fun randomMentorCreateRequest(userId: Long = Random.nextLong()) = CreateMentorRequest(
//        userId, stringGenerator.getRandomString(20),
//        stringGenerator.getRandomString(20), stringGenerator.getRandomString(20),
//        stringGenerator.getRandomString(20), stringGenerator.getRandomString(20),
//        stringGenerator.getRandomString(20), stringGenerator.getRandomString(20),
//        stringGenerator.getRandomString(20)
//    )
//
//    fun randomMentorUpdateRequest(userId: Long = Random.nextLong()) = UpdateMentorRequest(
//        stringGenerator.getRandomString(20),
//        stringGenerator.getRandomString(20), stringGenerator.getRandomString(20),
//        stringGenerator.getRandomString(20), stringGenerator.getRandomString(20),
//        stringGenerator.getRandomString(20), stringGenerator.getRandomString(20),
//        stringGenerator.getRandomString(20)
//    )
//
//    @Test
//    @Transactional
//    fun `save mentor and get it`() {
//        assertThrows<ResourceNotFoundException> { mentorService.getMentorByUserId(Random.nextLong()) }
//        assertThrows<ResourceNotFoundException> { mentorService.saveUserMentorInfo(randomMentorCreateRequest()) }
//        val user = userHelper.registerUser()
//        assertThrows<ResourceNotFoundException> { mentorService.getMentorByUserId(user.id) }
//        assertDoesNotThrow { mentorService.saveUserMentorInfo(randomMentorCreateRequest(user.id)) }
//        assertThrows<ResourceAlreadyExistsException> { mentorService.saveUserMentorInfo(randomMentorCreateRequest(user.id)) }
//        assertDoesNotThrow { mentorService.getMentorByUserId(user.id) }
//        assertThrows<ResourceNotFoundException> { mentorService.getMentorByUserId(user.id + 1) }
//    }
//
//    @Test
//    fun `test cascade (user - mentor)`() {
//        val user = userHelper.registerUser()
//        val request = randomMentorCreateRequest(user.id)
//        mentorService.saveUserMentorInfo(request)
//        assertDoesNotThrow { mentorService.getMentorByUserId(user.id) }
//        userService.deleteSelf(user.id)
//        assertThrows<ResourceNotFoundException> { mentorService.getMentorByUserId(user.id) }
//    }
//
//
//    @Test
//    fun `delete mentor`() {
//        assertThrows<ResourceNotFoundException> { mentorService.deleteUserMentor(Random.nextLong()) }
//        val user = userHelper.registerUser()
//        assertEquals(user.mentor, false)
//        assertThrows<ResourceNotFoundException> { mentorService.deleteUserMentor(user.id) }
//
//        val request = randomMentorCreateRequest(user.id)
//        mentorService.saveUserMentorInfo(request)
//        var entityUser = userService.findUserEntityById(user.id)
//        assertEquals(entityUser.mentor, true)
//
//        assertThrows<ResourceNotFoundException> { mentorService.deleteUserMentor(user.id + 1) }
//        val mentor = mentorService.mentorDao.findByUserId(user.id).get()
//        assertDoesNotThrow { mentorService.deleteUserMentor(mentor.userId!!) }
//        assertThrows<ResourceNotFoundException> { mentorService.getMentorByUserId(user.id) }
//        assertThrows<ResourceNotFoundException> { mentorService.deleteUserMentor(mentor.userId!!) }
//        entityUser = userService.findUserEntityById(user.id)
//        assertEquals(entityUser.mentor, false)
//    }
//
//    @Test
//    fun `check saved info + update info`() {
//        val mentorId = 1L
//        assertThrows<ResourceNotFoundException> {
//            mentorService.updateUserMentorInfo(mentorId, randomMentorUpdateRequest())
//        }
//        val user = userHelper.registerUser()
//        assertThrows<ResourceNotFoundException> {
//            mentorService.updateUserMentorInfo(mentorId, randomMentorUpdateRequest())
//        }
//        val request = randomMentorCreateRequest(user.id)
//        mentorService.saveUserMentorInfo(request)
//        var mentor = mentorService.mentorDao.findByUserId(user.id).get()
//        assertEquals(
//            true,
//            mentor.userId == request.userId &&
//                    mentor.company == request.company && mentor.expertArea == request.expertArea &&
//                    mentor.position == request.position && mentor.whyAreYouMentor == request.whyAreYouMentor &&
//                    mentor.formatsOfInteractions == request.formatsOfInteractions
//        )
//        assertEquals(true, mentor.available)
//        val entityUser = userService.findUserEntityById(user.id)
//        assertEquals(entityUser.mentor, true)
//        val update = randomMentorUpdateRequest(user.id)
//        mentorService.updateUserMentorInfo(user.id, update)
//        mentor = mentorService.mentorDao.findByUserId(user.id).get()
//        assertEquals(
//            true,
//            mentor.company == update.company && mentor.expertArea == update.expertArea &&
//                    mentor.position == update.position && mentor.whyAreYouMentor == update.whyAreYouMentor &&
//                    mentor.formatsOfInteractions == update.formatsOfInteractions
//        )
//    }
//
//    @Test
//    fun `check get all mentors + all available mentors`() {
//        val size = 7
//        val deleteCount = 5
//        val users = List(size) { userHelper.registerUser(email = "user$it@mail.ru") }
//        assertEquals(0, mentorService.listAllAvailableMentors("", mockPageRequest()).size)
//        assertEquals(0, mentorService.listAllMentors("", mockPageRequest()).size)
//        var availableCount = 0
//        users.forEach { user ->
//            val isAvailable = Random.nextBoolean()
//            if (isAvailable) availableCount++
//            assertThrows<ResourceNotFoundException> {
//                mentorService.toggleMentorAvailability(user.id, ToggleMentorAvailabilityRequest(isAvailable))
//            }
//            mentorService.saveUserMentorInfo(randomMentorCreateRequest(user.id))
//            mentorService.toggleMentorAvailability(user.id, ToggleMentorAvailabilityRequest(isAvailable))
//        }
//        assertEquals(availableCount.toLong(), mentorService.listAllAvailableMentors("", mockPageRequest()).size)
//        assertEquals(size.toLong(), mentorService.listAllMentors("", mockPageRequest()).size)
//        val all = mentorService.listAllMentors("", mockPageRequest()).content.toMutableList()
//        repeat(deleteCount) {
//            val mentor = mentorService.mentorDao.findByUserId(all.random().also { all.remove(it) }.userId).get()
//            if (mentor.available) availableCount--
//            mentorService.deleteUserMentor(mentor.userId!!)
//        }
//        assertEquals(availableCount.toLong(), mentorService.listAllAvailableMentors("", mockPageRequest()).size)
//        assertEquals((size - deleteCount).toLong(), mentorService.listAllMentors("", mockPageRequest()).size)
//    }
//
//    @Test
//    fun `check mentor availability`() {
//        val user = userHelper.registerUser()
//        val request = randomMentorCreateRequest(user.id)
//        var mentor = mentorService.mentorDao.findByUserId(mentorService.saveUserMentorInfo(request).userId).get()
//        assertEquals(true, mentor.available)
//        val entityUser = userService.findUserEntityById(user.id)
//        repeat(20) {
//            val isAvailable = Random.nextBoolean()
//            mentorService.toggleMentorAvailability(user.id, ToggleMentorAvailabilityRequest(isAvailable))
//            assertEquals(isAvailable, mentorService.mentorDao.findByUserId(user.id).get().available)
//        }
//    }
//}
