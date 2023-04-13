package ru.mephi.recommendations.database.mysql.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.mephi.recommendations.database.mysql.entity.WordEmbedding

@Repository
interface WordEmbeddingDao : CrudRepository<WordEmbedding, String>