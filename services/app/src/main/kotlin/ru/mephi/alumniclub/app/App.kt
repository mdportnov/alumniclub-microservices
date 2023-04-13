package ru.mephi.alumniclub.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import java.util.*

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(
    basePackages = ["ru.mephi.alumniclub.app", "ru.mephi.alumniclub.shared.util", "ru.mephi.alumniclub.shared.config"]
)
class App

fun main(args: Array<String>) {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    runApplication<App>(*args)
}