//package ru.mephi.alumniclub.app.service
//
//import org.apache.catalina.connector.Connector
//import org.apache.catalina.connector.Request
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.TestPropertySource
//import org.springframework.transaction.annotation.Transactional
//import ru.mephi.alumniclub.app.database.repository.activity.UserActivityDao
//import ru.mephi.alumniclub.app.database.repository.user.UserDao
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceNotFoundException
//import ru.mephi.alumniclub.app.service.helpers.UserHelper
//import ru.mephi.alumniclub.app.service.impl.AuthServiceImpl
//import ru.mephi.alumniclub.app.service.impl.UserActivityServiceImpl
//import kotlin.random.Random
//
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = ["classpath:application-test.yml"])
//@ActiveProfiles("test")
//class IUserActivityServiceTest {
//    @Autowired
//    lateinit var userHelper: UserHelper
//
//    @Autowired
//    lateinit var userActivityService: UserActivityServiceImpl
//
//    @Autowired
//    lateinit var authService: AuthServiceImpl
//
//    @Autowired
//    lateinit var userDao: UserDao
//
//    @Autowired
//    lateinit var userActivityDao: UserActivityDao
//
//
//    @BeforeEach
//    fun prepare() {
//        userHelper.toggleVerification()
//        cleanData()
//    }
//
//    @AfterEach
//    fun cleanData() {
//        userActivityDao.deleteAll()
//        userHelper.deleteAll()
//    }
//
//    @Test
//    fun `do simple request and check activity, check that time setting in the right order`() {
//        assertThrows<ResourceNotFoundException> {
//            userActivityService.findEntityById(Random.nextLong())
//        }
//        val user = userHelper.registerUser()
//        val request = Request(Connector())
//        request.userPrincipal = UsernamePasswordAuthenticationToken(user.id, user.id)
//        userActivityService.handle(request)
//        val activity1 = userActivityService.findEntityById(user.id)
//        Thread.sleep(500)
//        userActivityService.handle(request)
//        val activity2 = userActivityService.findEntityById(user.id)
//        assertEquals(true, activity2.time!!.isAfter(activity1.time!!) || activity1.time!! == activity2.time!!)
//    }
//
//    @Test
//    fun `delete activity`() {
//        val user = userHelper.registerUser()
//        val request = Request(Connector())
//        request.userPrincipal = UsernamePasswordAuthenticationToken(user.id, user.id)
//        userActivityService.handle(request)
//        userActivityService.findEntityById(user.id as Long)
//        assertDoesNotThrow { userActivityService.deleteById(user.id as Long) }
//    }
//
//    @Test
//    fun `haven't collision between users`() {
//        val user1 = userHelper.registerUser("email1@mail.ru")
//        val user2 = userHelper.registerUser("email2@mail.ru")
//        val request = Request(Connector())
//        request.userPrincipal = UsernamePasswordAuthenticationToken(user1.id, user1.id)
//        userActivityService.handle(request)
//        userActivityService.findEntityById(user1.id as Long)
//        assertDoesNotThrow { userActivityService.findEntityById(user2.id as Long) }
//        request.userPrincipal = UsernamePasswordAuthenticationToken(user2.id, user2.id)
//        userActivityService.handle(request)
//        userActivityService.findEntityById(user1.id as Long)
//        userActivityService.findEntityById(user2.id as Long)
//        userActivityService.deleteById(user1.id as Long)
//        assertDoesNotThrow { userActivityService.findEntityById(user1.id as Long) }
//        userActivityService.findEntityById(user2.id as Long)
//    }
//}
