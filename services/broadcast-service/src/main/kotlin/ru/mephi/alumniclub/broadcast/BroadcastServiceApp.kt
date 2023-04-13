package ru.mephi.alumniclub.broadcast

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

/**
 * Excluding DataSourceAutoConfiguration because shared module has spring-data-jpa
 * which is want to find jdbc config in application.yml, but this service doesn't have
 */
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
@ConfigurationPropertiesScan
@ComponentScan(basePackages = ["ru.mephi.alumniclub.broadcast.*", "ru.mephi.alumniclub.shared.*"])
class BroadcastServiceApp

fun main(args: Array<String>) {
    runApplication<BroadcastServiceApp>(*args)
}