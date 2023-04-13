//package ru.mephi.alumniclub.app.service
//
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.assertDoesNotThrow
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.TestPropertySource
//import ru.mephi.alumniclub.app.database.entity.atom.Atom
//import ru.mephi.alumniclub.app.database.repository.AtomDao
//import ru.mephi.alumniclub.app.model.dto.atom.request.SendAtomsRequest
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceNotFoundException
//import ru.mephi.alumniclub.app.service.helpers.UserHelper
//import ru.mephi.alumniclub.app.service.impl.AtomServiceImpl
//import ru.mephi.alumniclub.app.service.impl.UserServiceImpl
//import java.util.*
//import kotlin.random.Random
//
//@SpringBootTest
//@TestPropertySource(locations = ["classpath:application-test.yml"])
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class IAtomServiceTest {
//    @Autowired
//    lateinit var userHelper: UserHelper
//
//    @Autowired
//    lateinit var atomService: AtomServiceImpl
//
//    @Autowired
//    lateinit var atomDao: AtomDao
//
//    @Autowired
//    lateinit var userService: UserServiceImpl
//
//
//    @BeforeEach
//    fun disableVerification() {
//        userHelper.toggleVerification()
//        cleanData()
//    }
//
//    @AfterEach
//    fun cleanData() {
//        userHelper.deleteAll()
//        atomDao.deleteAll()
//    }
//
//    @Test
//    fun `give to user some atoms`() {
//        val user = userHelper.registerUser()
//        assertEquals(atomService.getUserAtomHistory(user.id).size, 0)
//        val history1 = atomService.accrueAtoms(user.id, SendAtomsRequest(user.id as Long, true, 100, "Просто так"))
//        assertEquals(history1.size, 1)
//        val history2 = atomService.accrueAtoms(user.id, SendAtomsRequest(user.id as Long, false, 50, "Просто так №2"))
//        assertEquals(history2.size, 2)
//        assertEquals(atomService.getUserAtomHistory(user.id).size, 2)
//        assertThrows<ResourceNotFoundException> {
//            atomService.accrueAtoms(user.id, SendAtomsRequest(404, true, 100, "NOT FOUND"))
//        }
//    }
//
//    @Test
//    fun `delete not existing atom history`() {
//        val user = userHelper.registerUser()
//        assertThrows<ResourceNotFoundException> {
//            atomService.deleteAtomHistoryById(user.id, UUID.randomUUID())
//        }
//        val (atom) = atomService.accrueAtoms(user.id, SendAtomsRequest(user.id, false, 400, ""))
//        assertDoesNotThrow { atomService.deleteAtomHistoryById(user.id, atom.id as UUID) }
//        assertThrows<ResourceNotFoundException> {
//            atomService.deleteAtomHistoryById(user.id, atom.id as UUID)
//        }
//    }
//
//    @Test
//    fun `test saving data`() {
//        val user = userHelper.registerUser()
//        val requests = mutableListOf<SendAtomsRequest>()
//        repeat(40) { it ->
//            val request = SendAtomsRequest(user.id, Random.nextBoolean(), 100, "$it")
//            requests.add(request)
//            val history1 = atomService.accrueAtoms(user.id, request)
//            val history2 = atomService.getUserAtomHistory(user.id)
//            assertEquals(history1.size, history2.size)
//            history1.forEach { req ->
//                val atom = history2.find { it.id == req.id }!!
//                assertEquals(
//                    true,
//                    (atom.amount == req.amount && atom.sign == req.sign && atom.description == req.description)
//                )
//            }
//        }
//    }
//
//    @Test
//    fun `test cascade with user`() {
//        val user = userHelper.registerUser()
//        repeat(10) {
//            atomService.accrueAtoms(user.id, SendAtomsRequest(user.id, true, 100, "$it"))
//        }
//        val history = atomService.getUserAtomHistory(user.id)
//        assertEquals(10, history.size)
//        assertDoesNotThrow { userService.deleteSelf(user.id) }
//        history.forEach {
//            assertThrows<ResourceNotFoundException> {
//                atomDao.findById(it.id as UUID)
//                    .orElseThrow { ResourceNotFoundException(Atom::class.java, it.id as UUID) }
//            }
//        }
//
//    }
//}
