package ru.mephi.recommendations.service.impl

import org.springframework.stereotype.Service
import ru.mephi.recommendations.database.milvus.PublicationVectorDao
import ru.mephi.recommendations.database.milvus.publication.Publication
import ru.mephi.recommendations.model.dto.publication_events.PublicationCreatedEventRequest
import ru.mephi.recommendations.model.dto.publication_events.PublicationDeletedEventRequest
import ru.mephi.recommendations.model.dto.publication_events.PublicationUpdatedEventRequest
import ru.mephi.recommendations.model.util.Vector
import ru.mephi.recommendations.service.PublicationService
import ru.mephi.recommendations.utils.TextPreprocessorManager
import ru.mephi.recommendations.utils.parseAllText
import ru.mephi.recommendations.utils.word2vec.TextVectorizationService
import java.util.*

@Service
class PublicationServiceImpl(
    private val dao: PublicationVectorDao,
    private val textPreprocessor: TextPreprocessorManager,
    private val textVectorizationService: TextVectorizationService,
) : PublicationService {

    private fun getVector(text: String): Vector {
        val content = parseAllText(text)
        val words = textPreprocessor.getWordsMap(content)
        return textVectorizationService.getVector(words)
    }

    override fun create(request: PublicationCreatedEventRequest): Publication {
        val vector = getVector(request.content)
        val publication = Publication(request.id, vector)
        return  publication //dao.save(publication)
    }

    override fun update(request: PublicationUpdatedEventRequest) {
        val vector = getVector(request.content)
        val publication = Publication(request.id, vector)
        dao.update(publication)
    }

    override fun delete(request: PublicationDeletedEventRequest) {
        dao.deleteById(request.id)
    }

    override fun findPublicationById(id: UUID): Publication {
        return dao.findById(id)
    }

    override fun getNearestPublicationsIds(id: UUID, n: Int): List<UUID> {
        val publication = findPublicationById(id)
        return dao.getNearestPublicationsIds(publication, n)
    }
}