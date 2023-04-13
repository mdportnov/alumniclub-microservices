package ru.mephi.alumniclub.shared.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import ru.mephi.alumniclub.shared.util.constants.LOCALES
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Validation

@Configuration
class LocaleConfig {
    @Bean
    fun localeResolver(): LocaleResolver {
        val slr = object : AcceptHeaderLocaleResolver() {
            override fun resolveLocale(request: HttpServletRequest): Locale {
                if (request.getHeader("Accept-Language").isNullOrBlank()) {
                    return Locale("ru")
                }
                val list: List<Locale.LanguageRange> =
                    Locale.LanguageRange.parse(request.getHeader("Accept-Language"))
                return Locale.lookup(list, LOCALES) ?: Locale("ru")
            }
        }
        return slr
    }

    @Bean
    fun messageSource(): ResourceBundleMessageSource {
        val source = ResourceBundleMessageSource()
        source.setBasenames("language/messages") // directory with messages_XX.properties
        source.setUseCodeAsDefaultMessage(true)
        source.setDefaultEncoding("UTF-8")
        return source
    }

    @Bean
    fun validator(): LocalValidatorFactoryBean {
        val defaultInterpolator = Validation.byDefaultProvider().configure().defaultMessageInterpolator
        val bean = LocalValidatorFactoryBean()
        bean.setValidationMessageSource(messageSource())
        bean.messageInterpolator = CustomMessageInterpolator(defaultInterpolator)
        return bean
    }
}