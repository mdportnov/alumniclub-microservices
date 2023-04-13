package ru.mephi.recommendations.database.milvus.publication

import ru.mephi.recommendations.model.util.Vector
import java.util.*

class Publication(
    val id: UUID,
    var vector: Vector,
) {

    companion object {
        const val ID_FIELD_NAME = "id"
        const val VECTOR_FIELD_NAME = "vector"
        const val COLLECTION_NAME = "Publication"
        const val VECTOR_DIMENSION = 300
        val ALL_NAMES_LIST = listOf(ID_FIELD_NAME, VECTOR_FIELD_NAME)
    }
}