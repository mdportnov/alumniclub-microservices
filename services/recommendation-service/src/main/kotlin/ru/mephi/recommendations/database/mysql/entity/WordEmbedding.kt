package ru.mephi.recommendations.database.mysql.entity

import ru.mephi.recommendations.database.types.FloatConverter
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class WordEmbedding(
    @Id
    @Column(name = "word", nullable = false, length = 60)
    val word: String,

    @Column(name = "vector", nullable = false)
    @Convert(converter = FloatConverter::class)
    val vector: Array<Float>,
)
