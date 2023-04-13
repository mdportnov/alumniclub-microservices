package ru.mephi.alumniclub.imageservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = ["ru.mephi.alumniclub.imageservice.*", "ru.mephi.alumniclub.shared.*"])
@EnableScheduling
class ImageServiceApp

fun main(args: Array<String>) {
    runApplication<ImageServiceApp>(*args)
}
