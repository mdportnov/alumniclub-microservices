plugins {
    application
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.google.cloud.tools.jib")
}

group = "ru.mephi.alumniclub"

application {
    mainClass.set("ru.mephi.recommendations.RecommendationServiceAppKt")
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

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-amqp")

    implementation("io.milvus:milvus-sdk-java:2.2.2")
    implementation("org.slf4j:slf4j-api:2.0.5")

    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.0.6")
    runtimeOnly("mysql:mysql-connector-java:8.0.30")

    implementation("org.jetbrains.kotlinx:dataframe:0.9.1")
}

configurations {
    all {
        exclude(group = "org.apache.logging.log4j", module = "log4j-to-slf4j")
        exclude(module = "spring-boot-starter-logging")
        exclude(module = "logback-classic")
    }
}
