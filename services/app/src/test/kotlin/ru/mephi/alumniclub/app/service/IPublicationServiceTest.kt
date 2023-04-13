//package ru.mephi.alumniclub.app.service
//
//import org.junit.jupiter.api.*
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.data.domain.PageRequest
//import org.springframework.data.domain.Sort
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.TestPropertySource
//import ru.mephi.alumniclub.app.database.repository.feed.AbstractFeedDao
//import ru.mephi.alumniclub.app.database.repository.feed.EventDao
//import ru.mephi.alumniclub.app.database.repository.feed.PublicationDao
//import ru.mephi.alumniclub.app.database.repository.user.UserDao
//import ru.mephi.alumniclub.app.model.dto.ExtendedPageRequest
//import ru.mephi.alumniclub.app.model.dto.ParticipationDto
//import ru.mephi.alumniclub.app.model.dto.feed.request.LikeRequest
//import ru.mephi.alumniclub.app.model.enums.NotificationCategory
//import ru.mephi.alumniclub.app.model.enums.feed.DefaultFeed
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceNotFoundException
//import ru.mephi.alumniclub.app.model.exceptions.feed.AlreadyJoinedException
//import ru.mephi.alumniclub.app.model.exceptions.feed.AlreadyLikedException
//import ru.mephi.alumniclub.app.model.exceptions.feed.JoinDoesNotExistException
//import ru.mephi.alumniclub.app.service.helpers.UserHelper
//import ru.mephi.alumniclub.app.service.helpers.mockEventRequest
//import ru.mephi.alumniclub.app.service.helpers.mockPublicationRequest
//import ru.mephi.alumniclub.app.util.Cursor
//import java.time.LocalDateTime
//import java.util.*
//import javax.transaction.Transactional
//
//@SpringBootTest
//@TestPropertySource(locations = ["classpath:application-test.yml"])
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class IPublicationServiceTest {
//
//    @Autowired
//    private lateinit var feedService: FeedService
//
//    @Autowired
//    private lateinit var commonFeedDao: AbstractFeedDao
//
//    @Autowired
//    private lateinit var publicationDao: PublicationDao
//
//    @Autowired
//    private lateinit var eventDao: EventDao
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
//    @BeforeAll
//    fun disableVerification() {
//        userHelper.toggleVerification()
//        cleanData()
//    }
//
//    @AfterEach
//    fun cleanData() {
//        userDao.deleteAll()
//        publicationDao.deleteAll()
//        val feeds = commonFeedDao.findAll().map { it.id }.toSet() - DefaultFeed.values().map { it.id }.toSet()
//        commonFeedDao.deleteAllById(feeds)
//    }
//
//    @Test
//    @Transactional
//    fun `list publications by feed id for admin`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publications = List(5) {
//            publicationService.post(user.id, feed.id, mockPublicationRequest().apply { title = "Publication $it" }).id
//        }
//        val page = ExtendedPageRequest(1, 20, Sort.Direction.ASC, "createdAt")
//        val response = publicationService.listPublicationsByFeedIdForUser(feed.id, user.id, "", page).content.map { it.id }
//        assertTrue(response.containsAll(publications))
//    }
//
//    @Test
//    fun `list events by feed id for admin`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        val events = List(5) {
//            publicationService.post(user.id, feed.id, mockEventRequest().apply { title = "Event $it" }).id
//        }
//        val page = ExtendedPageRequest(1, 20, Sort.Direction.ASC, "createdAt")
//        val response = publicationService.listEventsByFeedIdForUser(feed.id, "", "", page).content.map { it.id }
//        assertTrue(response.containsAll(events))
//    }
//
//    @Test
//    @Transactional
//    fun `list publications by feed id for user before`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publications = List(5) {
//            publicationService.post(user.id, feed.id, mockPublicationRequest().apply { title = "Publication $it" }).id
//        }
//        val cursor = Cursor(LocalDateTime.now().plusMinutes(10), PageRequest.ofSize(20), Cursor.Chronology.BEFORE)
//        val response = publicationService.listPublicationsByFeedIdForUser(feed.id, user.id, cursor).content.map { it.id }
//        assertTrue(response.containsAll(publications))
//    }
//
//    @Test
//    @Transactional
//    fun `list publications by feed id for user after`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        repeat(5) {
//            publicationService.post(user.id, feed.id, mockPublicationRequest().apply { title = "Publication $it" }).id
//        }
//        val cursor = Cursor(LocalDateTime.now(), PageRequest.ofSize(20), Cursor.Chronology.AFTER)
//        val response = publicationService.listPublicationsByFeedIdForUser(feed.id, user.id, cursor).content.map { it.id }
//        assertTrue(response.isEmpty())
//    }
//
//    @Test
//    @Transactional
//    fun `list events by feed id for user before`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        repeat(5) {
//            publicationService.post(user.id, feed.id, mockEventRequest().apply { title = "Event $it" }).id
//        }
//        val cursor = Cursor(LocalDateTime.now().minusMinutes(10), PageRequest.ofSize(20), Cursor.Chronology.BEFORE)
//        val response = publicationService.listEventsByFeedIdForUser(feed.id, user.id, cursor).content.map { it.id }
//        assertTrue(response.isEmpty())
//    }
//
//    @Test
//    @Transactional
//    fun `list events by feed id for user after`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        repeat(5) {
//            publicationService.post(user.id, feed.id, mockEventRequest().apply { title = "Event $it" }).id
//        }
//        val cursor = Cursor(LocalDateTime.now(), PageRequest.ofSize(20), Cursor.Chronology.AFTER)
//        val response = publicationService.listEventsByFeedIdForUser(feed.id, user.id, cursor).content.map { it.id }
//        assertTrue(response.isEmpty())
//    }
//
//    @Test
//    fun `list publications by feed does not exist for admin`() {
//        val page = ExtendedPageRequest(1, 20, Sort.Direction.ASC, "createdAt")
//        assertThrows<ResourceNotFoundException> {
//            publicationService.listPublicationsByFeedIdForUser(-1, 1, "", page)
//        }
//    }
//
//    @Test
//    fun `list events by feed does not exist for admin`() {
//        val page = ExtendedPageRequest(1, 20, Sort.Direction.ASC, "createdAt")
//        assertThrows<ResourceNotFoundException> {
//            publicationService.listEventsByFeedIdForUser(-1, "", "", page)
//        }
//    }
//
//    @Test
//    fun `list publications by feed does not exist for user`() {
//        val user = userHelper.registerUser()
//        val cursor = Cursor(LocalDateTime.now(), PageRequest.ofSize(20), Cursor.Chronology.BEFORE)
//        assertThrows<ResourceNotFoundException> {
//            publicationService.listPublicationsByFeedIdForUser(-1, user.id, cursor)
//        }
//    }
//
//    @Test
//    fun `list events by feed does not exist for user`() {
//        val user = userHelper.registerUser()
//        val cursor = Cursor(LocalDateTime.now(), PageRequest.ofSize(20), Cursor.Chronology.BEFORE)
//        assertThrows<ResourceNotFoundException> {
//            publicationService.listEventsByFeedIdForUser(-1, user.id, cursor)
//        }
//    }
//
//    @Test
//    fun `list all publications`() {
//        val user = userHelper.registerUser()
//        val publications = DefaultFeed.values().map { feed ->
//            publicationService.post(user.id, feed.id, mockPublicationRequest()).id
//        }
//        val page = ExtendedPageRequest(1, 20, Sort.Direction.ASC, "createdAt")
//        val response = publicationService.listAllPublications("", page).content.map { it.id }
//        assertTrue(response.containsAll(publications))
//    }
//
//    @Test
//    fun `list all events for user before`() {
//        val user = userHelper.registerUser()
//        repeat(5) {
//            val feed = feedService.createEventFeedEntity("Events $it")
//            publicationService.post(user.id, feed.id, mockEventRequest())
//        }
//        val cursor = Cursor(LocalDateTime.now().minusMinutes(10), PageRequest.ofSize(20), Cursor.Chronology.BEFORE)
//        val response = publicationService.listAllEventsForUser(user.id, cursor).content.map { it.id }
//        assertTrue(response.isEmpty())
//    }
//
//    @Test
//    fun `list all events for user after`() {
//        val user = userHelper.registerUser()
//        repeat(5) {
//            val feed = feedService.createEventFeedEntity("Events $it")
//            publicationService.post(user.id, feed.id, mockEventRequest())
//        }
//        val cursor = Cursor(LocalDateTime.now(), PageRequest.ofSize(20), Cursor.Chronology.AFTER)
//        val response = publicationService.listAllEventsForUser(user.id, cursor).content.map { it.id }
//        assertTrue(response.isEmpty())
//    }
//
//    @Test
//    fun `preview events`() {
//        val user = userHelper.registerUser()
//        val events = List(10) {
//            val feed = feedService.createEventFeedEntity("Events $it")
//            publicationService.post(user.id, feed.id, mockEventRequest()).id
//        }
//        val response = publicationService.previewEvents().map { it.id }
//        assertTrue(events.containsAll(response))
//    }
//
//    @Test
//    fun `find publication entity by id`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publication = publicationService.post(user.id, feed.id, mockPublicationRequest())
//        assertEquals(publication.id, publicationService.findPublicationEntityById(publication.id as UUID).id)
//    }
//
//    @Test
//    fun `find publication by id`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publication = publicationService.post(user.id, feed.id, mockPublicationRequest())
//        val response = publicationService.findPublicationByIdForUser(user.id, feed.id, publication.id as UUID)
//        assertEquals(publication.id, response.id)
//    }
//
//    @Test
//    fun `find event by id`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        val event = publicationService.post(user.id, feed.id, mockEventRequest())
//        val response = publicationService.findEventByIdForUser(user.id, feed.id, event.id as UUID)
//        assertEquals(event.id, response.id)
//    }
//
//    @Test
//    fun `test views count`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publication = publicationService.post(user.id, feed.id, mockPublicationRequest())
//        val count = 5
//        repeat(count) { publicationService.findPublicationByIdForUser(user.id, feed.id, publication.id as UUID) }
//        val response =
//            publicationService.findPublicationByIdForUser(
//                user.id,
//                feed.id,
//                publication.id as UUID
//            )
//        assertEquals(count.toLong(), response.viewsCount)
//    }
//
//    @Test
//    fun `post publication into non existing feed`() {
//        val user = userHelper.registerUser()
//        assertThrows<ResourceNotFoundException> {
//            publicationService.post(user.id, -1, mockPublicationRequest())
//        }
//    }
//
//    @Test
//    @Transactional
//    fun `post event into non existing feed`() {
//        val user = userHelper.registerUser()
//        assertThrows<ResourceNotFoundException> { publicationService.post(user.id, -1, mockEventRequest()) }
//    }
//
//    @Test
//    @Transactional
//    fun `update publication`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publication = publicationService.post(user.id, feed.id, mockPublicationRequest())
//        val request = mockPublicationRequest().apply { title = "Another" }
//        val response = publicationService.update(feed.id, publication.id as UUID, request)
//        assertEquals(
//            response.title,
//            publicationService.findPublicationByIdForUser(user.id, feed.id, publication.id as UUID).title
//        )
//    }
//
//    @Test
//    fun `update event`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        val event = publicationService.post(user.id, feed.id, mockEventRequest())
//        val request = mockEventRequest().apply { title = "Another" }
//        val response = publicationService.update(feed.id, event.id as UUID, request)
//        assertEquals(response.title, publicationService.findEventByIdForUser(user.id, feed.id, event.id as UUID).title)
//    }
//
//    @Test
//    fun `delete publication`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publication = publicationService.post(user.id, feed.id, mockPublicationRequest())
//        publicationService.deletePublication(feed.id, publication.id as UUID)
//        assertThrows<ResourceNotFoundException> {
//            publicationService.findPublicationByIdForUser(user.id, feed.id, publication.id as UUID)
//        }
//    }
//
//    @Test
//    fun `delete event`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        val event = publicationService.post(user.id, feed.id, mockEventRequest())
//        publicationService.deleteEvent(feed.id, event.id as UUID)
//        assertThrows<ResourceNotFoundException> {
//            publicationService.findEventByIdForUser(
//                user.id,
//                feed.id,
//                event.id as UUID,
//            )
//        }
//    }
//
//    @Test
//    fun `like publication`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publication = publicationService.post(user.id, feed.id, mockPublicationRequest())
//        assertEquals(
//            1,
//            publicationService.like(user.id, feed.id, publication.id as UUID, LikeRequest(like = true)).likes
//        )
//        assertEquals(
//            0,
//            publicationService.like(user.id, feed.id, publication.id as UUID, LikeRequest(like = false)).likes
//        )
//    }
//
//    @Test
//    fun `list likes`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publication = publicationService.post(user.id, feed.id, mockPublicationRequest())
//        publicationService.like(user.id, feed.id, publication.id as UUID, LikeRequest(like = true))
//        val page = ExtendedPageRequest(1, 20, Sort.Direction.ASC, "createdAt")
//        assertTrue(
//            user.id in publicationService.listLikes(
//                feed.id,
//                publication.id as UUID,
//                page
//            ).content.map { it.user.id })
//    }
//
//    @Test
//    fun `like publication twice`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publication = publicationService.post(user.id, feed.id, mockPublicationRequest())
//        assertThrows<AlreadyLikedException> {
//            publicationService.like(user.id, feed.id, publication.id as UUID, LikeRequest(like = true))
//            publicationService.like(user.id, feed.id, publication.id as UUID, LikeRequest(like = true))
//        }
//    }
//
//    @Test
//    fun `dislike publication twice`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createPublicationFeedEntity("News", NotificationCategory.ACHIEVEMENTS)
//        val publication = publicationService.post(user.id, feed.id, mockPublicationRequest())
//        publicationService.like(user.id, feed.id, publication.id as UUID, LikeRequest(like = true))
//        assertThrows<ResourceNotFoundException> {
//            publicationService.like(user.id, feed.id, publication.id as UUID, LikeRequest(like = false))
//            publicationService.like(user.id, feed.id, publication.id as UUID, LikeRequest(like = false))
//        }
//    }
//
//    @Test
//    fun `join event`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        val event = publicationService.post(user.id, feed.id, mockEventRequest())
//        assertEquals(1, publicationService.participate(user.id, feed.id, event.id as UUID, ParticipationDto(participation = true)).joins)
//        assertEquals(0, publicationService.participate(user.id, feed.id, event.id as UUID, ParticipationDto(participation = false)).joins)
//    }
//
//    @Test
//    fun `list joins`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        val event = publicationService.post(user.id, feed.id, mockEventRequest())
//        publicationService.participate(user.id, feed.id, event.id as UUID, ParticipationDto(participation = true))
//        val page = ExtendedPageRequest(1, 20, Sort.Direction.ASC, "createdAt")
//        assertTrue(user.id in publicationService.listJoins(feed.id, event.id as UUID, page).content.map { it.user.id })
//    }
//
//    @Test
//    fun `join event twice`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        val event = publicationService.post(user.id, feed.id, mockEventRequest())
//        assertThrows<AlreadyJoinedException> {
//            publicationService.participate(user.id, feed.id, event.id as UUID, ParticipationDto(participation = true))
//            publicationService.participate(user.id, feed.id, event.id as UUID, ParticipationDto(participation = true))
//        }
//    }
//
//    @Test
//    fun `leave event twice`() {
//        val user = userHelper.registerUser()
//        val feed = feedService.createEventFeedEntity("Events")
//        val event = publicationService.post(user.id, feed.id, mockEventRequest())
//        publicationService.participate(user.id, feed.id, event.id as UUID, ParticipationDto(participation = true))
//        assertThrows<JoinDoesNotExistException> {
//            publicationService.participate(user.id, feed.id, event.id as UUID, ParticipationDto(participation = false))
//            publicationService.participate(user.id, feed.id, event.id as UUID, ParticipationDto(participation = false))
//        }
//    }
//}
