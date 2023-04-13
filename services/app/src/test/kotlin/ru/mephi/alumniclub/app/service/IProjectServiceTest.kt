//package ru.mephi.alumniclub.app.service
//
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.*
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.data.jpa.repository.Modifying
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.TestPropertySource
//import ru.mephi.alumniclub.app.database.repository.project.EndowmentDao
//import ru.mephi.alumniclub.app.database.repository.project.ProjectDao
//import ru.mephi.alumniclub.app.database.repository.user.UserDao
//import ru.mephi.alumniclub.app.model.dto.ParticipationDto
//import ru.mephi.alumniclub.app.model.dto.project.ArchiveDto
//import ru.mephi.alumniclub.app.model.dto.project.request.ProjectCreateRequest
//import ru.mephi.alumniclub.app.model.enums.ProjectStatus
//import ru.mephi.alumniclub.app.model.enums.ProjectType
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceAlreadyExistsException
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceForbiddenException
//import ru.mephi.alumniclub.app.service.helpers.*
//import ru.mephi.alumniclub.app.util.Cursor
//
//@SpringBootTest
//@TestPropertySource(locations = ["classpath:application-test.yml"])
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class IProjectServiceTest {
//    @Autowired
//    private lateinit var projectService: ProjectService
//
//    @Autowired
//    private lateinit var publicationService: PublicationService
//
//    @Autowired
//    private lateinit var userHelper: UserHelper
//
//    @Autowired
//    private lateinit var userDao: UserDao
//
//    @Autowired
//    private lateinit var projectDao: ProjectDao
//
//    @Autowired
//    private lateinit var endowmentDao: EndowmentDao
//
//    private fun projectRequest(name: String = "Project") =
//        ProjectCreateRequest(
//            type = ProjectType.PROJECT,
//            name = name,
//            description = "Description",
//            hiddenCommunity = false
//        )
//
//    private fun endowmentRequest(name: String = "Project") =
//        ProjectCreateRequest(
//            type = ProjectType.ENDOWMENT,
//            name = name,
//            description = "Description",
//            hiddenCommunity = false
//        )
//
//    @BeforeAll
//    fun disableVerification() {
//        userHelper.toggleVerification()
//        cleanData()
//    }
//
//    @AfterEach
//    fun cleanData() {
//        userDao.deleteAll()
//        projectDao.deleteAll()
//        endowmentDao.deleteAll()
//    }
//
//    @Test
//    fun `list projects for admin`() {
//        val user = userHelper.registerUser()
//        val projects = List(5) { projectService.create(projectRequest(name = "Project $it")).id }
//        val response = projectService.listProjects(user.id, "", ProjectStatus.ALL, mockPageRequest()).content.map { it.id }
//        assertTrue(response.containsAll(projects))
//    }
//
//    @Test
//    fun `list endowments for admin`() {
//        val user = userHelper.registerUser()
//        val endowments = List(5) { projectService.create(endowmentRequest(name = "Project $it")).id }
//        val response = projectService.listEndowments(user.id, "", ProjectStatus.ALL, mockPageRequest()).content.map { it.id }
//        assertTrue(response.containsAll(endowments))
//    }
//
//    @Test
//    fun `list abstract projects for admin`() {
//        val user = userHelper.registerUser()
//        val projects = List(5) { projectService.create(projectRequest(name = "Project $it")).id }
//        val endowments = List(5) { projectService.create(endowmentRequest(name = "Endowment $it")).id }
//        val response = projectService.listAbstractProjects(user.id, "", ProjectStatus.ALL, mockPageRequest()).content.map { it.id }
//        assertTrue(response.containsAll(endowments + projects))
//    }
//
//    @Test
//    fun `list projects for user and public before`() {
//        val user = userHelper.registerUser()
//        val projects = List(5) { projectService.create(projectRequest(name = "Project $it")).id }
//        val responseForUser = projectService.listProjects(user.id, mockCursorRequest()).content.map { it.id }
//        val responseForPublic = projectService.listProjects(mockCursorRequest()).content.map { it.id }
//        assertTrue(responseForUser.containsAll(projects))
//        assertTrue(responseForPublic.containsAll(projects))
//    }
//
//    @Test
//    fun `list projects for user and public after`() {
//        val user = userHelper.registerUser()
//        repeat(5) { projectService.create(projectRequest(name = "Project $it")).id }
//        val request = mockCursorRequest().apply { chronology = Cursor.Chronology.AFTER }
//        val responseForUser = projectService.listProjects(user.id, request).content
//        val responseForPublic = projectService.listProjects(request).content
//        assertTrue(responseForUser.isEmpty())
//        assertTrue(responseForPublic.isEmpty())
//    }
//
//    @Test
//    fun `list endowments for user before`() {
//        val user = userHelper.registerUser()
//        val endowments = List(5) { projectService.create(endowmentRequest(name = "Project $it")).id }
//        val responseForUser = projectService.listEndowments(user.id, mockCursorRequest()).content.map { it.id }
//        assertTrue(responseForUser.containsAll(endowments))
//    }
//
//    @Test
//    fun `list endowments for user after`() {
//        val user = userHelper.registerUser()
//        repeat(5) { projectService.create(endowmentRequest(name = "Project $it")).id }
//        val request = mockCursorRequest().apply { chronology = Cursor.Chronology.AFTER }
//        val responseForUser = projectService.listEndowments(user.id, request).content
//        assertTrue(responseForUser.isEmpty())
//    }
//
//    @Test
//    fun `list projects by user`() {
//        val user = userHelper.registerUser()
//        val projects = List(5) { projectService.create(projectRequest("Project $it")).id }
//        projects.forEach { projectService.participate(user.id, it as Long, ParticipationDto(true)) }
//        val response = projectService.listProjectsByUser(user.id, mockPageRequest()).content.map { it.id }
//        assertTrue(response.containsAll(projects))
//    }
//
//    @Test
//    fun `list endowments by user`() {
//        val user = userHelper.registerUser()
//        val endowments = List(5) { projectService.create(endowmentRequest(name = "Project $it")).id }
//        endowments.forEach { projectService.participate(user.id, it as Long, ParticipationDto(true)) }
//        val response = projectService.listEndowmentsByUser(user.id, mockPageRequest()).content.map { it.id }
//        assertTrue(response.containsAll(endowments))
//    }
//
//
//    @Test
//    fun `find project by id`() {
//        val user = userHelper.registerUser()
//        val project = projectService.create(projectRequest())
//        val response = projectService.findProjectById(project.id as Long, user.id)
//        assertEquals(project.id, response.id)
//    }
//
//
//    @Test
//    @Modifying
//    fun `find archived project for user`() {
//        val user = userHelper.registerUser()
//        val project = projectService.create(endowmentRequest())
//        projectService.archive(project.id as Long, ArchiveDto(archive = true))
//        assertThrows<ResourceForbiddenException> { projectService.findProjectById(project.id as Long, user.id) }
//    }
//
//
//    @Test
//    @Modifying
//    fun `find unarchived project for user`() {
//        val user = userHelper.registerUser()
//        val project = projectService.create(projectRequest())
//        projectService.archive(project.id as Long, ArchiveDto(archive = true))
//        projectService.archive(project.id as Long, ArchiveDto(archive = false))
//        val response = projectService.findProjectById(project.id as Long, user.id)
//        assertEquals(project.id, response.id)
//    }
//
//
//    @Test
//    @Modifying
//    fun `find archived project for admin`() {
//        val user = userHelper.registerUser()
//        val project = projectService.create(projectRequest())
//        projectService.archive(project.id as Long, ArchiveDto(archive = true))
//        val response = projectService.findProjectById(project.id as Long, user.id, ignoreArchive = true)
//        assertEquals(project.id, response.id)
//    }
//
//
//    @Test
//    fun `find endowment by id`() {
//        val user = userHelper.registerUser()
//        val endowment = projectService.create(endowmentRequest())
//        val response = projectService.findProjectById(endowment.id as Long, user.id)
//        assertEquals(endowment.id, response.id)
//    }
//
//
////    @Test
////    fun `find abstract project by publication`() {
////        val user = userHelper.registerUser()
////        val project = projectService.create(projectRequest())
////        val publication = publicationService
////            .post(user.id, project.publicationFeed.id as Long, mockPublicationRequest())
////            .run { publicationService.findPublicationEntityById(id as UUID) }
////        val response = projectService.findAbstractProjectByPublication(publication)
////        assertEquals(response?.id, project.id)
////    }
//
//
//    @Test
//    @Modifying
//    fun `find archived endowment for user`() {
//        val user = userHelper.registerUser()
//        val endowment = projectService.create(endowmentRequest())
//        projectService.archive(endowment.id as Long, ArchiveDto(archive = true))
//        assertThrows<ResourceForbiddenException> { projectService.findProjectById(endowment.id as Long, user.id) }
//    }
//
//
//    @Test
//    @Modifying
//    fun `find unarchived endowment for user`() {
//        val user = userHelper.registerUser()
//        val endowment = projectService.create(endowmentRequest())
//        projectService.archive(endowment.id as Long, ArchiveDto(archive = true))
//        projectService.archive(endowment.id as Long, ArchiveDto(archive = false))
//        val response = projectService.findProjectById(endowment.id as Long, user.id)
//        assertEquals(endowment.id, response.id)
//    }
//
//
//    @Test
//    @Modifying
//    fun `find archived endowment for admin`() {
//        val user = userHelper.registerUser()
//        val endowment = projectService.create(endowmentRequest())
//        projectService.archive(endowment.id as Long, ArchiveDto(archive = true))
//        val response = projectService.findProjectById(endowment.id as Long, user.id, ignoreArchive = true)
//        assertEquals(endowment.id, response.id)
//    }
//
//
////    @Test
////    fun `preview project by id`() {
////        val user = userHelper.registerUser()
////        val project = projectService.create(projectRequest())
////        val publications = List(5) {
////            val request = mockPublicationRequest().apply { title = "Publication $it" }
////            publicationService.post(user.id, project.publicationFeed.id as Long, request).id
////        }
////        val events = List(5) {
////            val request = mockEventRequest().apply { title = "Event $it" }
////            publicationService.post(user.id, project.eventFeed!!.id as Long, request).id
////        }
////        val response = projectService.previewProjectById(user.id, project.id as Long)
////        assertTrue(publications.containsAll(response.publications.map { it.id }))
////        assertTrue(events.containsAll(response.events.map { it.id }))
////    }
//
//
////    @Test
////    fun `preview endowment by id`() {
////        val user = userHelper.registerUser()
////        val endowment = projectService.create(endowmentRequest())
////        val publications = List(5) {
////            val request = mockPublicationRequest().apply { title = "Publication $it" }
////            publicationService.post(user.id, endowment.publicationFeed.id as Long, request).id
////        }
////        val response = projectService.previewProjectById(user.id, endowment.id as Long)
////        assertTrue(publications.containsAll(response.publications.map { it.id }))
////    }
//
//    @Test
//    fun `create project duplicate name`() {
//        assertThrows<ResourceAlreadyExistsException> {
//            projectService.create(projectRequest())
//            projectService.create(projectRequest())
//        }
//    }
//
//    @Test
//    fun `create endowment duplicate name`() {
//        assertThrows<ResourceAlreadyExistsException> {
//            projectService.create(endowmentRequest())
//            projectService.create(endowmentRequest())
//        }
//    }
//
//    @Test
//    fun `update project`() {
//        val user = userHelper.registerUser()
//        val project = projectService.create(projectRequest())
//        val updated = projectService.update(project.id as Long, projectRequest("New project"))
//        val response = projectService.findProjectById(project.id as Long, user.id)
//        assertEquals(updated.name, response.name)
//        assertEquals(updated.community.name, response.community.name)
//        assertEquals(updated.publicationFeed.name, response.publicationFeed.name)
//    }
//
//    @Test
//    fun `update project by updating community`() {
//        val user = userHelper.registerUser()
//        val project = projectService.create(projectRequest())
//        projectService.update(project.community.id as Long, "New project")
//        val updated = projectService.update(project.id as Long, projectRequest("New project"))
//        assertEquals("New project", updated.name)
//        assertEquals("New project", updated.community.name)
//        assertEquals("New project", updated.publicationFeed.name)
//    }
//
//    @Test
//    fun `update project name duplicated`() {
//        projectService.create(projectRequest("New project"))
//        val project = projectService.create(projectRequest())
//        assertThrows<ResourceAlreadyExistsException> {
//            projectService.update(project.id as Long, projectRequest("New project"))
//        }
//    }
//
//    @Test
//    fun `update endowment`() {
//        val user = userHelper.registerUser()
//        val endowment = projectService.create(endowmentRequest())
//        val updated = projectService.update(endowment.id as Long, endowmentRequest("New endowment"))
//        val response = projectService.findProjectById(endowment.id as Long, user.id)
//        assertEquals(updated.name, response.name)
//        assertEquals(updated.community.name, response.community.name)
//        assertEquals(updated.publicationFeed.name, response.publicationFeed.name)
//    }
//
//    @Test
//    fun `update endowment name duplicated`() {
//        projectService.create(endowmentRequest("New endowment"))
//        val endowment = projectService.create(endowmentRequest())
//        assertThrows<ResourceAlreadyExistsException> {
//            projectService.update(endowment.id as Long, endowmentRequest("New endowment"))
//        }
//    }
//
//    @Test
//    @Modifying
//    fun `delete project`() {
//        val project = projectService.create(projectRequest())
//        projectService.delete(project.id as Long)
//        assertFalse(projectDao.existsById(project.id as Long))
//    }
//
//    @Test
//    fun `list members for user`() {
//        val project = projectService.create(projectRequest())
//        val users = List(5) { userHelper.registerUser("user$it@mail.ru").id }
//        users.forEach { projectService.participate(it, project.id as Long, ParticipationDto(true)) }
//        val response =
//            projectService.listMembersForUser(project.id as Long, "", mockPageRequest()).content.map { it.user.id }
//        assertTrue(response.containsAll(users))
//    }
//
//    @Test
//    fun `list members for admin`() {
//        val project = projectService.create(projectRequest())
//        val users = List(5) { userHelper.registerUser("user$it@mail.ru").id }
//        users.forEach { projectService.participate(it, project.id as Long, ParticipationDto(true)) }
//        val response =
//            projectService.listMembersForAdmin(project.id as Long, "", mockPageRequest()).content.map { it.user.id }
//        assertTrue(response.containsAll(users))
//    }
//
//    @Test
//    fun `leave project`() {
//        val project = projectService.create(projectRequest())
//        val users = List(5) { userHelper.registerUser("user$it@mail.ru").id }
//        users.forEach { projectService.participate(it, project.id as Long, ParticipationDto(true)) }
//        users.forEach { projectService.participate(it, project.id as Long, ParticipationDto(false)) }
//        val response =
//            projectService.listMembersForAdmin(project.id as Long, "", mockPageRequest()).content.map { it.id }
//        assertTrue(response.isEmpty())
//    }
//}
