package ru.mephi.alumniclub.shared.config

import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import ru.mephi.alumniclub.shared.logging.AlumniLogger

@Configuration
class LoggerConfig {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    fun logger(injectionPoint: InjectionPoint): AlumniLogger {
        return AlumniLogger(
            injectionPoint.methodParameter?.containingClass ?: injectionPoint.field?.declaringClass
        )
    }
}