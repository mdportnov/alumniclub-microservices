plugins {
    application
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("com.google.cloud.tools.jib")
    id("io.gatling.gradle") version "3.9.3"
}

repositories {
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
    gradlePluginPortal()
    maven(url = "https://developer.huawei.com/repo/")
}

group = "ru.mephi.alumniclub"

springBoot {
    buildInfo() // For getting buildProperties.version
}

tasks {
    bootJar {
        layered {
            enabled = true
        }
        archiveFileName.set("${project.name}.jar")
    }

    jar {
        enabled = false
    }

    // configuration specific to this subproject.
    dokkaHtmlPartial {
        dokkaSourceSets {
            configureEach {
                includes.from("Module.md")
            }
        }
    }
}

application {
    mainClass.set("ru.mephi.alumniclub.app.AppKt")
}

gradle.taskGraph.whenReady {
    // Because it's some weird decorated wrapper that I can't cast.
    allTasks.filter { it.hasProperty("duplicatesStrategy") }
        .forEach {
            it.setProperty("duplicatesStrategy", "EXCLUDE")
        }
}

val springCloudVersion = "2021.0.5"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
    }
}

dependencies {
    implementation(project(":shared"))

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-validation:2.7.8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Logging
    implementation("net.logstash.logback:logstash-logback-encoder:7.2")

    // DB
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.0.6")
    runtimeOnly("mysql:mysql-connector-java:8.0.30")

    // Cache
    implementation("com.hazelcast:hazelcast-spring:5.2.1")
    implementation("com.github.vladimir-bukhtoyarov:bucket4j-core:7.6.0")
    implementation("com.github.vladimir-bukhtoyarov:bucket4j-jcache:7.6.0")
    implementation("com.github.vladimir-bukhtoyarov:bucket4j-hazelcast:7.6.0")
    implementation("javax.cache:cache-api:1.1.1")

    // Utils
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("org.apache.poi:poi:5.2.2")
    implementation("org.apache.tika:tika-core:2.6.0")
    implementation("javax.el:javax.el-api:3.0.0")
    implementation("org.glassfish.web:javax.el:2.2.6") // for Validation using javax.el.ExpressionFactory

    // Push Notifications: Firebase and HMS
    implementation("com.google.firebase:firebase-admin:9.1.1")
    implementation("com.huawei.hms:push:6.9.0.300")
    implementation("com.alibaba:fastjson:1.2.62")
    implementation(files("./libs/huawei-1.0-SNAPSHOT.jar"))

    // Swagger UI
    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.14")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.14")
    implementation("org.springdoc:springdoc-openapi-webflux-ui:1.6.14")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.14")

    // Performance Testing
    testImplementation("io.gatling.highcharts:gatling-charts-highcharts:3.9.3")
    testImplementation("io.gatling:gatling-app:3.9.3")
    testImplementation("io.gatling:gatling-recorder:3.9.3")
}
