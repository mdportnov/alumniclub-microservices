//package ru.mephi.alumniclub.app.service
//
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.TestInstance
//import org.junit.jupiter.api.assertThrows
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.ActiveProfiles
//import org.springframework.test.context.TestPropertySource
//import ru.mephi.alumniclub.app.model.enums.NotificationCategory
//import ru.mephi.alumniclub.app.model.exceptions.common.ResourceAlreadyExistsException
//import javax.transaction.Transactional
//
//@SpringBootTest
//@Transactional
//@TestPropertySource(locations = ["classpath:application-test.yml"])
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class IFeedServiceTest {
//    @Autowired
//    lateinit var feedService: FeedService
//
//    @Test
//    fun `create feed twice`() {
//        assertThrows<ResourceAlreadyExistsException> {
//            feedService.createPublicationFeedEntity("News", NotificationCategory.EVENTS)
//            feedService.createPublicationFeedEntity("News", NotificationCategory.EVENTS)
//        }
//    }
//
//    @Test
//    fun `exists publication feed by id`() {
//        val news = feedService.createPublicationFeedEntity("News", NotificationCategory.EVENTS)
//        assertTrue(feedService.existsPublicationFeedById(news.id))
//    }
//
//    @Test
//    fun `exists event feed by id`() {
//        val events = feedService.createEventFeedEntity("News")
//        assertTrue(feedService.existsEventFeedById(events.id))
//    }
//
//    @Test
//    fun `find common feed by id`() {
//        val news = feedService.createPublicationFeedEntity("News", NotificationCategory.EVENTS)
//        val events = feedService.createEventFeedEntity("News")
//        assertEquals(news.id, feedService.findAbstractFeedEntityById(news.id).id)
//        assertEquals(events.id, feedService.findAbstractFeedEntityById(events.id).id)
//    }
//
//    @Test
//    fun `find publication feed by id`() {
//        val news = feedService.createPublicationFeedEntity("News", NotificationCategory.EVENTS)
//        assertEquals(news.id, feedService.findPublicationFeedEntityById(news.id).id)
//    }
//
//    @Test
//    fun `find event feed by id`() {
//        val events = feedService.createEventFeedEntity("News")
//        assertEquals(events.id, feedService.findEventFeedEntityById(events.id).id)
//    }
//
//    @Test
//    fun `update feed name`() {
//        val news = feedService.createPublicationFeedEntity("News", NotificationCategory.EVENTS)
//        val updated = feedService.update(news, "New news")
//        val response = feedService.findPublicationFeedEntityById(news.id)
//        assertEquals(updated.name, response.name)
//    }
//
//    @Test
//    fun `update feed name existed`() {
//        val news = feedService.createPublicationFeedEntity("News", NotificationCategory.EVENTS)
//        feedService.createPublicationFeedEntity("New news", NotificationCategory.EVENTS)
//        assertThrows<ResourceAlreadyExistsException> { feedService.update(news, "New news") }
//    }
//}
