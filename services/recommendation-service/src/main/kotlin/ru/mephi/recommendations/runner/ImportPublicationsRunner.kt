package ru.mephi.recommendations.runner

import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.add
import org.jetbrains.kotlinx.dataframe.api.copy
import org.jetbrains.kotlinx.dataframe.io.readCSV
import org.jetbrains.kotlinx.dataframe.io.writeCSV
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service
import ru.mephi.recommendations.model.dto.publication_events.PublicationCreatedEventRequest
import ru.mephi.recommendations.service.PublicationService
import java.io.File
import java.util.*

//@Service
class ImportPublicationsRunner(
    private val service: PublicationService,
) : ApplicationRunner {

    override fun run(args: ApplicationArguments) {
        val file = File("C:\\Users\\orang\\OneDrive\\Рабочий стол\\news.csv")
        val frame = DataFrame.readCSV(file).add("vector") {
            val content = "<p>" + it["text"].toString() + "</p>"
            val publication = service.create(
                PublicationCreatedEventRequest(
                    UUID.randomUUID(),
                    content.split(" .  ")[0].drop(3),
                    content
                )
            )
            return@add publication.vector.coordinates.toList().toString()
        }
        val outFile = File("C:\\Users\\orang\\OneDrive\\Рабочий стол\\news_full.csv")
        outFile.createNewFile()
        frame.writeCSV(outFile)
    }
}