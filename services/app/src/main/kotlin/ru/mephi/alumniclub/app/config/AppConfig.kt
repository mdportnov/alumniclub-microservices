package ru.mephi.alumniclub.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.mephi.alumniclub.app.interceptor.ActivityInterceptor
import ru.mephi.alumniclub.app.interceptor.BannedUserInterceptor
import ru.mephi.alumniclub.app.interceptor.RateLimiterInterceptor

@Configuration
class AppConfig(
    private val activityInterceptor: ActivityInterceptor,
    private val bannedUserInterceptor: BannedUserInterceptor,
    private val rateLimiterInterceptor: RateLimiterInterceptor,
) : WebMvcConfigurer {

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/api/v3/api-docs", config)
        return CorsFilter(source)
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.apply {
            addInterceptor(activityInterceptor)
            addInterceptor(bannedUserInterceptor)
            addInterceptor(rateLimiterInterceptor)
        }
    }
}
