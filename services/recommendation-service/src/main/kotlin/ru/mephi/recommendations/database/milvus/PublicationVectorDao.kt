package ru.mephi.recommendations.database.milvus

import ru.mephi.recommendations.database.milvus.publication.Publication
import java.util.*

interface PublicationVectorDao {
    fun findById(id: UUID): Publication
    fun getNearestPublicationsIds(publication: Publication, n: Int = 3): List<UUID>
    fun save(entity: Publication): Publication
    fun deleteById(id: UUID)
    fun update(entity: Publication): Publication
}