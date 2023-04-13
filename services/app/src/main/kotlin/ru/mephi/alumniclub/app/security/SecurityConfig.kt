package ru.mephi.alumniclub.app.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import ru.mephi.alumniclub.app.config.properties.ActuatorAuthProperties
import ru.mephi.alumniclub.app.config.properties.SwaggerProperties
import ru.mephi.alumniclub.app.filter.JwtFilter
import ru.mephi.alumniclub.app.filter.VersionFilter
import ru.mephi.alumniclub.app.filter.logging.HttpRequestResponseLoggingFilter
import ru.mephi.alumniclub.app.interceptor.CustomAccessDeniedHandler
import ru.mephi.alumniclub.shared.config.CustomAuthenticationProvider
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtFilter: JwtFilter,
    private val versionFilter: VersionFilter,
    private val accessDeniedException: CustomAccessDeniedHandler,
    private val swaggerProperties: SwaggerProperties,
    private val actuatorAuthProperties: ActuatorAuthProperties,
    private val authProvider: CustomAuthenticationProvider
) : WebSecurityConfigurerAdapter() {
    private val loggingFilter: HttpRequestResponseLoggingFilter = HttpRequestResponseLoggingFilter()

    @Bean
    fun getEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("$API_VERSION_1/public/**")
            .permitAll()
            .antMatchers("$API_VERSION_1/user/**")
            .hasAnyRole("USER")
            .antMatchers("$API_VERSION_1/admin/**")
            .hasAnyRole("ADMIN", "MODERATOR")
            .antMatchers("/actuator/**")
            .hasRole("PROMETHEUS_ADMIN")
            .and().httpBasic()
            .and().authenticationProvider(authProvider)
            .exceptionHandling().accessDeniedHandler(accessDeniedException)
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(loggingFilter, JwtFilter::class.java)
            .addFilterAfter(versionFilter, JwtFilter::class.java)

        http
            .authorizeRequests()
            .antMatchers("/swagger/**", "/swagger-ui/**", "/v3/api-docs/**")
            .hasRole("SWAGGER").and().httpBasic()
            .and().exceptionHandling().accessDeniedHandler(accessDeniedException)

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().withUser(actuatorAuthProperties.login)
            .password(getEncoder().encode(actuatorAuthProperties.password)).roles("PROMETHEUS_ADMIN")
            .and().withUser(swaggerProperties.swaggerLogin)
            .password(getEncoder().encode(swaggerProperties.swaggerPassword)).roles("SWAGGER")
    }
}
