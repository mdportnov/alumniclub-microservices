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
    mainClass.set("ru.mephi.alumniclub.imageservice.ImageServiceAppKt")
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

    implementation("net.coobird:thumbnailator:0.4.19")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.0.6")
    runtimeOnly("mysql:mysql-connector-java:8.0.30")
}
