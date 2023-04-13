package ru.mephi.recommendations.runner

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import ru.mephi.recommendations.database.mysql.dao.WordEmbeddingDao
import ru.mephi.recommendations.database.mysql.entity.WordEmbedding
import java.io.File
import java.util.*

//@Service
class ImportWord2VecModelRunner(
    private val dao: WordEmbeddingDao,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        var num = 0
        println("НАЧАЛАСЬ ЗАГРУЗКА МОДЕЛИ")
        val file = File("C:\\Users\\orang\\PycharmProjects\\test-pr-1\\outmodel.txt")
        val model = Scanner(file)
        while (model.hasNextLine()) {
            val line = model.nextLine()
            val ar = line.split(" ", limit = 2)
            val word = ar[0]
            if (isNotGood(word)) continue
            val floats = ar[1].split(" ").map { it.toFloat() }
            dao.save(WordEmbedding(word, floats.toTypedArray()))
            println(++num)
        }
        println("ЗАГРУЗКА МОДЕЛИ ЗАВЕРШЕНА")
        println("ЗАГРУЗКА МОДЕЛИ ЗАВЕРШЕНА")
        println("ЗАГРУЗКА МОДЕЛИ ЗАВЕРШЕНА")
        println("ЗАГРУЗКА МОДЕЛИ ЗАВЕРШЕНА")
    }

    private fun isNotGood(word: String): Boolean {
        return word.length > 60 || word.length < 3 || word.startsWith(",") ||
                word.startsWith(".") || word.startsWith("-") ||
                word.contains("[0-9]".toRegex())
    }
}