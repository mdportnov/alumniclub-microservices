package ru.mephi.recommendations.database.milvus.util

import io.milvus.client.MilvusServiceClient
import io.milvus.grpc.DataType
import io.milvus.param.ConnectParam
import io.milvus.param.IndexType
import io.milvus.param.MetricType
import io.milvus.param.collection.*
import io.milvus.param.index.CreateIndexParam
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ru.mephi.recommendations.database.milvus.publication.Publication
import javax.annotation.PreDestroy


@Component
class MilvusManager(
    @Value("\${milvus.host}")
    milvusHost: String,
    @Value("\${milvus.port}")
    milvusPort: Int,
) {
    val milvusClient = MilvusServiceClient(
        ConnectParam.newBuilder()
            .withHost(milvusHost)
            .withPort(milvusPort)
            .build()
    )

    init {
        createPublicationTable()
        loadPublicationTable()
    }

    /**
     * Loads Milvus collection to memory. Needed for doing queries to this collection in future
     */

    private fun loadPublicationTable() {
        milvusClient.loadCollection(
            LoadCollectionParam.newBuilder()
                .withCollectionName(Publication.COLLECTION_NAME)
                .build()
        )
    }

    /**
     * Creates [Publication] collection in Milvus if it not exists
     */

    private fun createPublicationTable() {
        if (milvusClient.hasCollection(
                HasCollectionParam.newBuilder()
                    .withCollectionName(Publication.COLLECTION_NAME).build()
            ).data == true
        ) return // Collection exists

        val idField = FieldType.newBuilder()
            .withName(Publication.ID_FIELD_NAME)
            .withDataType(DataType.VarChar)
            .withMaxLength(36)
            .withPrimaryKey(true)
            .withAutoID(false)
            .build()
        val vectorField = FieldType.newBuilder()
            .withName(Publication.VECTOR_FIELD_NAME)
            .withDataType(DataType.FloatVector)
            .withDimension(Publication.VECTOR_DIMENSION)
            .build()
        val createCollectionReq = CreateCollectionParam.newBuilder()
            .withCollectionName(Publication.COLLECTION_NAME)
            .withShardsNum(2)
            .addFieldType(idField)
            .addFieldType(vectorField)
            .build()

        milvusClient.createCollection(createCollectionReq)

        insertIndexToPublicationTable()
    }

    /**
     * Insert vector index to [Publication] collection
     * @see <a href="https://milvus.io/docs/index.md">more information about vector indexes</a>
     */
    private fun insertIndexToPublicationTable() {
        milvusClient.createIndex(
            CreateIndexParam.newBuilder()
                .withCollectionName(Publication.COLLECTION_NAME)
                .withFieldName(Publication.VECTOR_FIELD_NAME)
                .withIndexType(IndexType.IVF_PQ)
                .withMetricType(MetricType.IP)
                .withExtraParam("""{"nlist": 256, "m": 10, "nbits": 16  }""")
                .withSyncMode(true)
                .build()
        )
    }

    /**
     * Releases [Publication] collection
     */

    @PreDestroy
    fun onDestroy() {
        milvusClient.releaseCollection(
            ReleaseCollectionParam.newBuilder()
                .withCollectionName(Publication.COLLECTION_NAME)
                .build()
        )
    }
}