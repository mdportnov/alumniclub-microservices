package ru.mephi.recommendations.utils.word2vec

import ru.mephi.recommendations.model.util.Vector

interface TextVectorizationService {
    fun getVector(words: Map<String, Int>): Vector
}