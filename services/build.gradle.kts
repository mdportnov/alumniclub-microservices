import com.google.cloud.tools.jib.gradle.JibExtension
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.7.22"
    java
    id("org.jetbrains.dokka") version "1.7.20" // it's latest version
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    kotlin("jvm") version kotlinVersion apply false
    kotlin("kapt") version kotlinVersion apply false
    kotlin("plugin.spring") version kotlinVersion apply false
    // specifies @Entity, @Embeddable, and @MappedSuperclass no-arg annotations automatically
    kotlin("plugin.jpa") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion apply false
    id("org.springframework.boot") version "2.7.8" apply false
    id("io.spring.dependency-management") version "1.1.0" apply false
    id("com.google.cloud.tools.jib") version "3.3.1" apply false
}

// For working plugins on root project level (e.g. dokka)
repositories {
    mavenCentral()
    gradlePluginPortal()
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.7.20")
    }
}

tasks {
    val copyDocImages by registering(Copy::class) {
        from("./assets")
        include("*.png")
        into("$buildDir/../docs/styles")
    }

    dokkaHtmlMultiModule {
        includes.from("Module.md")
        outputDirectory.set(file("$buildDir/../docs"))
        moduleName.set("AlumniClub MicroServices Documentation")
        dependsOn(copyDocImages)
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            customStyleSheets = listOf(file("./assets/logo-styles.css"))
            footerMessage = "Made with ❤️ in Informatization Department"
        }
    }
}

project(":app") {
    version = "0.7.1"
}

project(":broadcast-service") {
    version = "0.0.2"
}

project(":image-service") {
    version = "0.0.2"
}

project(":recommendation-service") {
    version = "0.0.2"
}

val jibBaseImage =
    if (extra["buildJibForArm64"].toString().toBoolean()) "arm64v8/eclipse-temurin:11-jre" else "eclipse-temurin:11-jre"

val jibImageArch = if (extra["buildJibForArm64"].toString().toBoolean()) "arm64" else "amd64"

val registry = if (gradle.startParameter.taskNames.firstOrNull().toString() == "jib")
    "devops-it.mephi.ru/alumniclub/" else ""

// The versioning plugin should be applied in all submodules
subprojects {
    // applying plugins for all subprojects
    apply {
        plugin("org.gradle.java")
        plugin("io.gitlab.arturbosch.detekt")
        plugin("org.jetbrains.dokka")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.kapt")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.jetbrains.kotlin.plugin.jpa")
        plugin("org.jetbrains.kotlin.plugin.allopen")
        plugin("org.jetbrains.kotlin.plugin.noarg")
        if (project.name != "shared") plugin("com.google.cloud.tools.jib")
    }

    // configuring AllOpen plugin for inheritance in JPA for all submodules
    configure<AllOpenExtension> {
        annotation("javax.persistence.Entity")
        annotation("javax.persistence.Embeddable")
        annotation("javax.persistence.MappedSuperclass")
    }

    if (project.name != "shared")
    // https://github.com/GoogleContainerTools/jib/tree/master/jib-gradle-plugin
        configure<JibExtension> {
            container.creationTime.set("USE_CURRENT_TIMESTAMP")
            from {
                image = jibBaseImage
                platforms {
                    platform {
                        architecture = jibImageArch
                        os = "linux"
                    }
                }
            }

            to {
                image = "${registry}${project.name}:${project.version}"
                tags = setOf("${project.version}", "latest")
            }
        }

    repositories {
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        gradlePluginPortal()
    }

    tasks {
        detekt {
            ignoreFailures = true
            parallel = true
            config = files("$rootDir/config/detekt/detekt.yml")
            buildUponDefaultConfig = true
        }

        withType<Test> {
            useJUnitPlatform()
        }

        // Settings of KotlinCompile for all submodules
        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all-compatibility")
                jvmTarget = "11"
            }
        }

        withType<JavaCompile> {
            sourceCompatibility = JavaVersion.VERSION_11.toString()
            targetCompatibility = JavaVersion.VERSION_11.toString()
        }
    }

    val dokkaPlugin by configurations
    dependencies {
        dokkaPlugin("org.jetbrains.dokka:versioning-plugin:1.7.20")

        // adds support for serialization/deserialization of Kotlin classes
        implementation(kotlin("reflect"))
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        if (project.name != "shared") {
            implementation("org.springframework.metrics:spring-metrics:0.5.1.RELEASE") // for /api/actuator/metrics
            implementation("io.micrometer:micrometer-registry-prometheus:1.10.3") // for /api/actuator/prometheus
        }

        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-web")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
        testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
        testImplementation("org.junit.platform:junit-platform-suite:1.9.2")
    }
}