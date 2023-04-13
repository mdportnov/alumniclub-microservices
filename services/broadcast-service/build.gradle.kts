plugins {
    application
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.google.cloud.tools.jib")
}

group = "ru.mephi.alumniclub"

application {
    mainClass.set("ru.mephi.alumniclub.broadcast.ServerBroadcastApplicationKt")
}

tasks {
    bootJar {
        archiveFileName.set("${project.name}.jar")
    }
    jar {
        enabled = false
    }
    dokkaHtmlPartial {
        dokkaSourceSets {
            configureEach {
                includes.from("Module.md")
            }
        }
    }
}

dependencies {
    implementation(project(":shared"))

    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-mail")
}