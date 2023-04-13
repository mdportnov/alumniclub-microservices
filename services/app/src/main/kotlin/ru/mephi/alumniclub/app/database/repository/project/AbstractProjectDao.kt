package ru.mephi.alumniclub.app.database.repository.project

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import ru.mephi.alumniclub.app.database.entity.feed.PublicationFeed
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.project.IdColorProjection
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository
import java.time.LocalDateTime

interface AbstractProjectDao<T : AbstractProject> : AbstractRepository<T>, PagingAndSortingRepository<T, Long> {
    fun existsByName(name: String): Boolean
    fun existsByNameAndIdNot(name: String, id: Long): Boolean
    fun findByNameStartsWith(prefix: String, pageable: Pageable): Page<T>
    fun findByNameStartsWithAndArchive(name: String, archive: Boolean, pageable: Pageable): Page<T>
    fun findByArchiveFalseAndCreatedAtAfter(createdAt: LocalDateTime, pageable: Pageable): Page<T>
    fun findByArchiveFalseAndCreatedAtBefore(createdAt: LocalDateTime, pageable: Pageable): Page<T>
    fun findByCommunityUsersContainingAndArchiveFalse(user: User, pageable: Pageable): Page<T>
    fun findByPublicationFeed(publicationFeed: PublicationFeed): AbstractProject?
    fun findByPublicationFeedId(id: Long): AbstractProject?
    fun findByCommunityId(id: Long): AbstractProject?

    @Query(
        """
        SELECT CASE WHEN COUNT(0) > 0 THEN TRUE ELSE FALSE END
        FROM Project WHERE (publicationFeed.id = :feedId OR eventFeed.id = :feedId) AND id IN (:projectsIds)
    """
    )
    fun existsFeedIdInProjects(feedId: Long, projectsIds: List<Long>): Boolean

    @Query("SELECT name FROM Project WHERE id=:id")
    fun findNameById(id: Long): String?

    @Query("""
        SELECT p.id as id, p.color as color FROM Project p
        WHERE p.publicationFeed.id=:feedId OR p.eventFeed.id=:feedId
    """)
    fun findIdAndColorByFeedId(feedId: Long): IdColorProjection?
}
