//package ru.mephi.alumniclub.app.service
//
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.*
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.domain.Sort
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.TestPropertySource
//import ru.mephi.alumniclub.app.database.repository.user.UserDao
//import ru.mephi.alumniclub.app.model.dto.ExtendedPageRequest
//import ru.mephi.alumniclub.app.model.dto.user.BioVisibilityDTO
//import ru.mephi.alumniclub.app.model.dto.user.DegreeDto
//import ru.mephi.alumniclub.app.model.dto.user.UserVisibilityDTO
//import ru.mephi.alumniclub.app.model.dto.user.request.BanRequest
//import ru.mephi.alumniclub.app.model.dto.user.request.BioRequest
//import ru.mephi.alumniclub.app.model.dto.user.request.RegistrationRequest
//import ru.mephi.alumniclub.app.model.enums.community.roleCommunities
//import ru.mephi.alumniclub.app.model.enums.user.Degree
//import ru.mephi.alumniclub.app.model.enums.user.Role
//import ru.mephi.alumniclub.app.model.exceptions.common.AdminDeleteException
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceNotFoundException
//import ru.mephi.alumniclub.app.service.helpers.UserHelper
//import ru.mephi.alumniclub.app.service.helpers.UserMock
//import ru.mephi.alumniclub.app.service.helpers.mockPageRequest
//import javax.transaction.Transactional
//
//@SpringBootTest
//@TestPropertySource(locations = ["classpath:application-test.yml"])
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class IUserServiceTest {
//
//    @Autowired
//    private lateinit var userHelper: UserHelper
//
//    @Autowired
//    lateinit var userDao: UserDao
//
//    @Autowired
//    lateinit var userService: UserService
//
//    @BeforeAll
//    fun disableVerification() {
//        userHelper.toggleVerification()
//        cleanData()
//    }
//
//    @AfterEach
//    fun cleanData() = userHelper.deleteAll()
//
//    private fun userRequest(
//        email: String = UserMock.EMAIL.value,
//        roles: Set<Role> = setOf(Role.WORKER),
//        degrees: List<DegreeDto> = userHelper.degreeDto(Degree.WORKER),
//        bio: BioRequest? = null,
//    ) = RegistrationRequest(
//        password = UserMock.PASSWORD.value, email = email,
//        name = "Name", surname = "Surname", gender = true,
//        birthday = userHelper.birthday, roles = roles,
//        degrees = degrees, bio = bio
//    )
//
//    @Test
//    fun injections() {
//        assertTrue { ::userHelper.isInitialized }
//        assertTrue { ::userDao.isInitialized }
//        assertTrue { ::userService.isInitialized }
//    }
//
//    @Test
//    fun `list users`() {
//        val emails = List(5) { "user$it@mail.ru" }
//        emails.forEach { userHelper.registerUser(it) }
//        val savedEmails = userService.export().map { it.email }
//        assertTrue(savedEmails.containsAll(emails))
//    }
//
//    @Test
//    fun `exists user by id`() {
//        val user = userHelper.registerUser()
//        assertTrue(userService.existById(user.id))
//    }
//
//    @Test
//    fun `find user by email`() {
//        val user = userHelper.registerUser()
//        assertDoesNotThrow { userService.findUserEntityByEmail(UserMock.EMAIL.value) }
//    }
//
//    @Test
//    fun `find user by fullName`() {
//        val user = userHelper.registerUser()
//        val request = ExtendedPageRequest(1, 10, Sort.Direction.ASC, "fullName")
//        assertEquals(1, userService.list(user.surname, request).size)
//    }
//
//    @Test
//    fun `update user info`() {
//        val user = userHelper.registerUser(roles = setOf(Role.LYCEUM, Role.ALUMNUS, Role.STUDENT))
//        val request = userRequest(roles = setOf(Role.LYCEUM, Role.ALUMNUS, Role.STUDENT))
//        val response = userService.updateFullUserInfo(user.id, request)
//        assertEquals(response.name, userService.findById(user.id).name)
//    }
//
//    @Test
//    fun `update user bio`() {
//        val user = userHelper.registerUser()
//        val response = userService.updateBio(user.id, BioRequest(city = "Moscow"))
//        assertEquals(response?.city, userService.findBioById(user.id)?.city)
//    }
//
//    @Test
//    fun `update user profile`() {
//        val user = userHelper.registerUser()
//        val request = userRequest().apply { surname = "Doe" }
//        val response = userService.updateProfile(user.id, request)
//        assertEquals(response.surname, userService.findProfileById(user.id).surname)
//    }
//
//    @Test
//    fun `update user degrees`() {
//        val user = userHelper.registerUser()
//        val response = userService.updateDegrees(user.id, userHelper.degreeDto(Degree.SPECIALTY, Degree.MASTER))
//        assertEquals(response.toSet(), userService.findDegreesById(user.id).toSet())
//    }
//
//    @Test
//    fun `update user bio to null`() {
//        val user = userHelper.registerUser()
//        val request = userRequest(bio = BioRequest(city = "Moscow"))
//        userService.updateFullUserInfo(user.id, request)
//        val response = userService.updateFullUserInfo(user.id, userRequest(bio = null))
//        assertNull(response.bio)
//    }
//
//    @Test
//    fun `update default user to user with STUDENT, ALUMNUS and LYCEUM roles`() {
//        val user = userHelper.registerUser()
//        val request = userRequest(roles = setOf(Role.LYCEUM, Role.ALUMNUS, Role.STUDENT))
//        val response = userService.updateFullUserInfo(user.id, request)
//        assertNull(response.bio)
//    }
//
//    @Test
//    fun `update user with STUDENT, ALUMNUS and LYCEUM roles to default user`() {
//        val user = userHelper.registerUser(roles = setOf(Role.LYCEUM, Role.ALUMNUS, Role.STUDENT))
//        val request = userRequest()
//        val response = userService.updateFullUserInfo(user.id, request)
//        assertNull(response.bio)
//    }
//
//    @Test
//    fun `delete self`() {
//        val user = userHelper.registerUser()
//        userService.deleteSelf(user.id)
//        assertThrows<ResourceNotFoundException> { userService.findById(user.id) }
//    }
//
//    @Test
//    fun `delete user by id`() {
//        val user = userHelper.registerUser()
//        userService.delete(user.id, 0)
//        assertThrows<ResourceNotFoundException> { userService.findById(user.id) }
//    }
//
//    @Test
//    @Transactional
//    fun `delete admin self`() {
//        val user = userHelper.registerAdmin()
//        assertThrows<AdminDeleteException> { userService.delete(user.id, user.id) }
//    }
//
//    @Test
//    fun `ban by id`() {
//        val user = userHelper.registerUser()
//        userService.setBanStatus(user.id, BanRequest(banned = true))
//        assertTrue(userService.findById(user.id, true).banned)
//    }
//
//    @Test
//    fun `unban by id`() {
//        val user = userHelper.registerUser()
//        userService.setBanStatus(user.id, BanRequest(banned = false))
//        assertFalse(userService.findById(user.id, true).banned)
//    }
//
//    @Test
//    fun `is not banned`() {
//        val user = userHelper.registerUser()
//        assertTrue(userService.isNotBanned(user.id))
//    }
//
//    @Test
//    fun `get profile visibility`() {
//        val user = userHelper.registerUser(roles = setOf(Role.LYCEUM, Role.ALUMNUS, Role.STUDENT))
//        assertDoesNotThrow { userService.getVisibility(user.id) }
//    }
//
//    @Test
//    fun `test profile visibility`() {
//        val user = userHelper.registerUser(roles = setOf(Role.LYCEUM, Role.ALUMNUS, Role.STUDENT))
//        val response = userService.findById(user.id)
//        assertNull(response.email)
//    }
//
//    @Test
//    @Transactional
//    fun `update profile visibility`() {
//        val user = userHelper.registerUser(bio = BioRequest())
//        val visibility = UserVisibilityDTO(
//            email = true, phone = false, vk = false, tg = false, gender = false,
//            birthday = false, createdAt = false, degrees = false,
//            bioVisibility = BioVisibilityDTO(
//                country = false, city = false, jobArea = false, job = false,
//                company = false, experience = false, hobbies = false
//            )
//        )
//        userService.updateVisibility(user.id, visibility)
//        val response = userService.findById(user.id)
//        assertNotNull(response.email)
//    }
//
//    @Test
//    fun `test visibility cascade`() {
//        val user = userHelper.registerUser()
//        userService.deleteSelf(user.id)
//        assertThrows<ResourceNotFoundException> { userService.getVisibility(user.id) }
//    }
//
//    @Test
//    @Transactional
//    fun `get list user communities`() {
//        val user = userHelper.registerUser(roles = setOf(Role.LYCEUM, Role.ALUMNUS, Role.STUDENT))
//        val communities = userService.listUserCommunities(user.id, PageRequest.ofSize(10))
//            .content.map { it.id }.toSet()
//        val userCommunities = setOf(
//            roleCommunities[Role.LYCEUM]?.id,
//            roleCommunities[Role.ALUMNUS]?.id,
//            roleCommunities[Role.STUDENT]?.id
//        )
//        assertEquals(communities, userCommunities)
//    }
//
//    @Test
//    fun `list moderators`() {
//        val admin = userHelper.registerAdmin()
//        val moderators = userService.listByModeratorOrAdminAndSurnameStartsWith("", mockPageRequest())
//        assertTrue(admin.id in moderators.map { it.id })
//    }
//
//    @Test
//    fun `find profile by id`() {
//        val user = userHelper.registerUser()
//        val profile = userService.findProfileById(user.id)
//        assertEquals(profile.email, user.email)
//    }
//
//    @Test
//    fun `find bio by id`() {
//        val user = userHelper.registerUser()
//        val bio = userService.findBioById(user.id)
//        assertNull(bio)
//    }
//
//    @Test
//    fun `find degrees by id`() {
//        val degrees = userHelper.degreeDto(Degree.WORKER, Degree.LYCEUM)
//        val user = userHelper.registerUser(degrees = degrees)
//        assertEquals(userService.findDegreesById(user.id).toSet(), degrees.toSet())
//    }
//
//    @Test
//    fun `preview by id`() {
//        val user = userHelper.registerUser()
//        val preview = userService.previewById(user.id)
//        assertEquals(preview.email, user.email)
//    }
//
//    @Test
//    fun `find user id and email`() {
//        val user = userHelper.registerUser()
//        val pairs = userService.findUserIdUserEmailPairs(listOf(user.id))
//        assertTrue(user.id in pairs.map { it.userId })
//    }
//}
