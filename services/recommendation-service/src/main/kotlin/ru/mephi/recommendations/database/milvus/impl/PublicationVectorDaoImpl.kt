package ru.mephi.recommendations.database.milvus.impl

import io.milvus.common.clientenum.ConsistencyLevelEnum
import io.milvus.param.MetricType
import io.milvus.param.dml.DeleteParam
import io.milvus.param.dml.InsertParam
import io.milvus.param.dml.QueryParam
import io.milvus.param.dml.SearchParam
import io.milvus.response.QueryResultsWrapper
import io.milvus.response.SearchResultsWrapper
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import ru.mephi.recommendations.database.milvus.PublicationVectorDao
import ru.mephi.recommendations.database.milvus.publication.Publication
import ru.mephi.recommendations.database.milvus.util.MilvusManager
import ru.mephi.recommendations.model.util.Vector
import java.util.*


@Service
class PublicationVectorDaoImpl(
    private val manager: MilvusManager,
    private val logger: AlumniLogger
) : PublicationVectorDao {

    /**
     * Finds and returns [Publication] by it id
     *
     * @param id The id of publication
     * @return [Publication]
     */

    override fun findById(id: UUID): Publication {
        val queryParam = QueryParam.newBuilder()
            .withCollectionName(Publication.COLLECTION_NAME)
            .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
            .withExpr("${Publication.ID_FIELD_NAME} == \"$id\"")
            .withOutFields(Publication.ALL_NAMES_LIST)
            .build()
        val wrapperQuery = QueryResultsWrapper(manager.milvusClient.query(queryParam).data)
        val listVector = wrapperQuery.getFieldWrapper(Publication.VECTOR_FIELD_NAME).fieldData.first() as List<Float>
        return Publication(id, Vector(listVector.toTypedArray()))
    }

    /**
     * Use Milvus to find ANN vectors by ANN vectors indexes
     *
     * @param publication The publication for which we are looking for other upcoming publications
     * @param n count of publication to search
     * @return [List] ids of found [Publication]
     */

    override fun getNearestPublicationsIds(publication: Publication, n: Int): List<UUID> {
        val searchParam = SearchParam.newBuilder()
            .withCollectionName(Publication.COLLECTION_NAME)
            .withConsistencyLevel(ConsistencyLevelEnum.STRONG)
            .withMetricType(MetricType.IP)
            .withOutFields(listOf(Publication.ID_FIELD_NAME))
            .withTopK(n + 1)
            .withVectors(listOf(publication.vector.coordinates.toList()))
            .withVectorFieldName(Publication.VECTOR_FIELD_NAME)
            .withParams(""" { "nprobe": 32 } """)
            .build()
        val result = SearchResultsWrapper(manager.milvusClient.search(searchParam).data.results)

        val ids = (result.getFieldData(Publication.ID_FIELD_NAME, 0) as List<String>).map { UUID.fromString(it) }
        return ids.filter { it != publication.id }
    }

    /**
     * Save [Publication] to Milvus
     *
     * @param entity The publication to saving
     * @return [Publication] that had been saved
     */

    override fun save(entity: Publication): Publication {
        val fields = listOf(
            InsertParam.Field(Publication.ID_FIELD_NAME, listOf(entity.id.toString())),
            InsertParam.Field(Publication.VECTOR_FIELD_NAME, listOf(entity.vector.coordinates.toList()))
        )

        val insertParam = InsertParam.newBuilder()
            .withCollectionName(Publication.COLLECTION_NAME)
            .withFields(fields)
            .build()
        manager.milvusClient.insert(insertParam)
        return entity
    }

    /**
     * Deletes [Publication] by it id
     *
     * @param id of [Publication] for deleting
     */

    override fun deleteById(id: UUID) {
        manager.milvusClient.delete(
            DeleteParam.newBuilder()
                .withCollectionName(Publication.COLLECTION_NAME)
                .withExpr("${Publication.ID_FIELD_NAME} in [\"$id\"]")
                .build()
        )
    }

    /**
     * Update [Publication] in Milvis DB
     *
     * @param entity for updating
     * @return [Publication] that was wrote to DB
     */

    override fun update(entity: Publication): Publication {
        try {
            deleteById(entity.id)
        } catch (e: Exception) {
            logger.error("PUBLICATION UPDATE ERROR", e)
        }
        return save(entity)
    }
}