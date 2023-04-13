plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

group = "ru.mephi.alumniclub"
version = "0.0.1"

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
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
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.8")

    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
}
