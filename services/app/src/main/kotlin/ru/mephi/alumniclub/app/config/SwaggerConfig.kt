package ru.mephi.alumniclub.app.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@Configuration
class SwaggerConfig {
    @Bean
    fun customizeOpenAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"
        return OpenAPI().addSecurityItem(SecurityRequirement().addList(securitySchemeName))
            .components(
                Components().addSecuritySchemes(
                    securitySchemeName,
                    SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
    }

    @Bean
    fun fullApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("alumni-club-v1")
            .displayName("alumni-club-v1")
            .packagesToScan("ru.mephi.alumniclub.app")
            .build()
    }

    @Bean
    fun adminApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("alumni-club-v1 admin")
            .displayName("alumni-club-v1 admin")
            .packagesToScan("ru.mephi.alumniclub.app")
            .pathsToMatch("$API_VERSION_1/admin/**")
            .build()
    }

    @Bean
    fun userApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("alumni-club-v1 user")
            .displayName("alumni-club-v1 user")
            .packagesToScan("ru.mephi.alumniclub.app")
            .pathsToExclude("$API_VERSION_1/admin/**")
            .build()
    }
}