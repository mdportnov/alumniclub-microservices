package ru.mephi.recommendations.database.milvus.impl

import ru.mephi.recommendations.database.milvus.PublicationVectorDao
import ru.mephi.recommendations.database.milvus.publication.Publication
import ru.mephi.recommendations.model.util.Vector
import java.util.*
import kotlin.math.min

@Deprecated("Bad")
//@Service
class PublicationVectorDaoBadImpl : PublicationVectorDao {
    val vectors = Collections.synchronizedList(ArrayList<Publication>())

    private fun cos(x: Vector, y: Vector): Double {
        var cos = 0.0
        for (i in 0 until x.coordinates.size) {
            cos += x.coordinates[i] * y.coordinates[i]
        }
        cos /= x.lenght() * y.lenght()
        return cos
    }

    override fun findById(id: UUID): Publication {
        return vectors.find { it.id == id } ?: throw Exception("Resource not found")
    }

    override fun getNearestPublicationsIds(publication: Publication, n: Int): List<UUID> {
        val list = ArrayList<Pair<UUID, Double>>()
        for (p in vectors) {
            if (p.id == publication.id) continue
            list.add(Pair(p.id, cos(p.vector, publication.vector)))
        }
        list.sortBy { -it.second }
        return list.subList(0, min(list.size, n)).map { it.first }
    }

    override fun save(entity: Publication): Publication {
        if (entity.vector.coordinates[0].isNaN() || (entity.vector.lenght() < 0.05f)) return entity
        deleteById(entity.id)
        vectors.add(entity)
        return entity
    }

    override fun deleteById(id: UUID) {
        vectors.removeIf { it.id == id }
    }

    override fun update(entity: Publication): Publication {
        return save(entity)
    }
}