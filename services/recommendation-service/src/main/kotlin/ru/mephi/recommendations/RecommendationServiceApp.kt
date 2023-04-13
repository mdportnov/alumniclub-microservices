package ru.mephi.recommendations

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(
    basePackages = ["ru.mephi.recommendations", "ru.mephi.alumniclub.shared.util", "ru.mephi.alumniclub.shared.config"]
)
class RecommendationServiceApp

fun main(args: Array<String>) {
    runApplication<RecommendationServiceApp>(*args)
}
