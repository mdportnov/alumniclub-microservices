//package ru.mephi.alumniclub.app.service
//
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.*
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.data.domain.PageRequest
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.TestPropertySource
//import ru.mephi.alumniclub.app.database.repository.community.CommunityDao
//import ru.mephi.alumniclub.app.database.repository.community.UserCommunityDao
//import ru.mephi.alumniclub.app.database.repository.user.UserDao
//import ru.mephi.alumniclub.app.model.dto.ParticipationDto
//import ru.mephi.alumniclub.app.model.dto.community.request.CommunityRequest
//import ru.mephi.alumniclub.app.model.enums.community.roleCommunities
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceAlreadyExistsException
//import ru.mephi.alumniclub.app.model.exceptions.community.UserAlreadyInCommunityException
//import ru.mephi.alumniclub.app.model.exceptions.community.UserNotFoundInCommunityException
//import ru.mephi.alumniclub.app.service.helpers.UserHelper
//import ru.mephi.alumniclub.app.util.Cursor
//import java.time.LocalDateTime
//import javax.transaction.Transactional
//
//@SpringBootTest
//@TestPropertySource(locations = ["classpath:application-test.yml"])
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class ICommunityServiceTest {
//
//    @Autowired
//    private lateinit var userHelper: UserHelper
//
//    @Autowired
//    lateinit var communityService: CommunityService
//
//    @Autowired
//    lateinit var communityDao: CommunityDao
//
//    @Autowired
//    lateinit var userCommunityDao: UserCommunityDao
//
//    @Autowired
//    lateinit var userDao: UserDao
//
//    @BeforeAll
//    fun disableVerification() {
//        userHelper.toggleVerification()
//    }
//
//    @AfterEach
//    fun cleanData() {
//        userDao.deleteAll()
//        val communities = communityDao.findAll().map { it.id }.toSet() - roleCommunities.values.map { it.id }.toSet()
//        communityDao.deleteAllById(communities)
//    }
//
//    @Test
//    fun `create community`() {
//        val request = CommunityRequest("Community")
//        val community = communityService.create(request)
//        assertEquals(request.name, community.name)
//    }
//
//    @Test
//    fun `community name is duplicated`() {
//        val request = CommunityRequest("Community")
//        assertThrows<ResourceAlreadyExistsException> {
//            communityService.create(request)
//            communityService.create(request)
//        }
//    }
//
//    @Test
//    fun `list communities`() {
//        val communities = List(5) { communityService.create(CommunityRequest("Community $it")).id }
//        val page = PageRequest.ofSize(20)
//        val responseCommunities = communityService.list(page = page).content.map { it.id }
//        assertTrue(responseCommunities.containsAll(communities))
//    }
//
//    @Test
//    @Transactional
//    fun `list communities by user`() {
//        val user = userHelper.registerUser()
//        val communities = List(5) {
//            val community = communityService.create(CommunityRequest("Community $it"))
//            communityService.participate(user.id, community.id as Long, ParticipationDto(true), false)
//            community.id
//        }
//        val page = PageRequest.ofSize(20)
//        val responseCommunities = communityService.listByUser(user.id, page).content.map { it.id }
//        assertTrue(responseCommunities.containsAll(communities))
//    }
//
//    @Test
//    fun `list communities by user not BEFORE`() {
//        val user = userHelper.registerUser()
//        val communities = List(5) { communityService.create(CommunityRequest("Community $it")).id }
//        val cursor = Cursor(
//            from = LocalDateTime.now().plusMinutes(1),
//            page = PageRequest.ofSize(20),
//            chronology = Cursor.Chronology.BEFORE
//        )
//        val responseCommunities = communityService.listByUserNot(user.id, cursor).content.map { it.id }
//        assertTrue(responseCommunities.containsAll(communities))
//    }
//
//    @Test
//    fun `list communities by user not AFTER`() {
//        val user = userHelper.registerUser()
//        List(5) { communityService.create(CommunityRequest("Community $it")).id }.toSet()
//        val cursor = Cursor(
//            from = LocalDateTime.now(),
//            page = PageRequest.ofSize(10),
//            chronology = Cursor.Chronology.AFTER
//        )
//        val responseCommunities = communityService.listByUserNot(user.id, cursor).content.map { it.id }.toSet()
//        assertTrue(responseCommunities.isEmpty())
//    }
//
//    @Test
//    fun `find community by id`() {
//        val community = communityService.create(CommunityRequest("Community"))
//        val responseCommunities = communityService.findById(community.id as Long)
//        assertEquals(community.id, responseCommunities.id)
//    }
//
//    @Test
//    fun `find community entity by id`() {
//        val community = communityService.create(CommunityRequest("Community"))
//        val responseCommunities = communityService.findEntityById(community.id as Long)
//        assertEquals(community.id, responseCommunities.id)
//    }
//
//    @Test
//    fun `update community entity`() {
//        val community = communityService.create(CommunityRequest("Community"))
//        val entity = communityService.findEntityById(community.id as Long)
//        val updated = communityService.update(community.id as Long, CommunityRequest("New community"))
//        assertEquals("New community", updated.name)
//    }
//
//    @Test
//    fun `update community`() {
//        val community = communityService.create(CommunityRequest("Community"))
//        val request = CommunityRequest("New community")
//        val updated = communityService.update(community.id as Long, request)
//        assertEquals(request.name, updated.name)
//    }
//
//    @Test
//    fun `update community name is duplicated`() {
//        communityService.create(CommunityRequest("New community"))
//        val community = communityService.create(CommunityRequest("Community"))
//        val request = CommunityRequest("New community")
//        assertThrows<ResourceAlreadyExistsException> { communityService.update(community.id as Long, request) }
//    }
//
//    @Test
//    @Transactional
//    fun `hide community`() {
//        val community = communityService.create(CommunityRequest("Community"))
//        communityService.hide(communityService.findEntityById(community.id as Long))
//        assertTrue(communityService.findEntityById(community.id as Long).hidden)
//    }
//
//    @Test
//    fun `show community`() {
//        val community = communityService.create(CommunityRequest("Community"))
//        communityService.show(communityService.findEntityById(community.id as Long))
//        assertFalse(communityService.findEntityById(community.id as Long).hidden)
//    }
//
//    @Test
//    fun `delete community`() {
//        val community = communityService.create(CommunityRequest("Community"))
//        communityService.delete(community.id as Long)
//        assertFalse(communityService.existById(community.id as Long))
//    }
//
//    @Test
//    fun `list community members for user`() {
//        val user = userHelper.registerUser()
//        val community = communityService.create(CommunityRequest("Community"))
//        communityService.participate(user.id, community.id as Long, ParticipationDto(true), false)
//        val page = PageRequest.ofSize(20)
//        val members = communityService.listMembersForUser(community.id as Long, "", page).content.map { it.user.id }
//        assertTrue(user.id in members)
//    }
//
//    @Test
//    fun `list community members for admin`() {
//        val user = userHelper.registerUser()
//        val community = communityService.create(CommunityRequest("Community"))
//        communityService.participate(user.id, community.id as Long, ParticipationDto(true), false)
//        val page = PageRequest.ofSize(20)
//        val members = communityService.listMembersForAdmin(community.id as Long, "", page).content.map { it.user.id }
//        assertTrue(user.id in members)
//    }
//
//    @Test
//    fun `join community twice`() {
//        val user = userHelper.registerUser()
//        val community = communityService.create(CommunityRequest("Community"))
//        assertThrows<UserAlreadyInCommunityException> {
//            communityService.participate(user.id, community.id as Long, ParticipationDto(true), false)
//            communityService.participate(user.id, community.id as Long, ParticipationDto(true), false)
//        }
//    }
//
//    @Test
//    fun `leave community twice`() {
//        val user = userHelper.registerUser()
//        val community = communityService.create(CommunityRequest("Community"))
//        communityService.participate(user.id, community.id as Long, ParticipationDto(true), false)
//        assertThrows<UserNotFoundInCommunityException> {
//            communityService.participate(user.id, community.id as Long, ParticipationDto(false), false)
//            communityService.participate(user.id, community.id as Long, ParticipationDto(false), false)
//        }
//    }
//}
