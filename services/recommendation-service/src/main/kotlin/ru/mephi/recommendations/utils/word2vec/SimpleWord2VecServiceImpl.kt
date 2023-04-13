package ru.mephi.recommendations.utils.word2vec

import org.springframework.stereotype.Service
import ru.mephi.recommendations.database.mysql.dao.WordEmbeddingDao
import ru.mephi.recommendations.model.util.Vector

@Service
class SimpleWord2VecServiceImpl(
    private val dao: WordEmbeddingDao,
) : TextVectorizationService {

    /**
     * Gets vector from text corpus by BoW-like algorithm with Word2Vec words vectors
     *
     * @param words The map of String - Int. Indicates how many times each word occurs in the text
     * @return [Vector] The result vector of text corpus
     */
    override fun getVector(words: Map<String, Int>): Vector {
        val vector = Vector(Array<Float>(300) { 0.00f })
        try {
            var total = 0
            dao.findAllById(words.keys).forEach {
                total += words[it.word]!!
                vector += Vector(it.vector) * words[it.word]!!
            }
            vector /= total
            vector.normalize()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return vector
    }
}