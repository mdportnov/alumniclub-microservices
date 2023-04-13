package ru.mephi.alumniclub.shared.validators

import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

abstract class AbstractValidator<Annotation : kotlin.Annotation, Target>
    : ConstraintValidator<Annotation, Target>, ResponseManager() {

    protected lateinit var context: HibernateConstraintValidatorContext
    protected lateinit var message: String
    protected var valid: Boolean = true

    protected abstract fun checkViolations(value: Target)

    protected fun addViolation(vararg parameters: Pair<String, Any>, template: String = message) {
        parameters.forEach { (name, value) -> context.addMessageParameter(name, value) }
        context.buildConstraintViolationWithTemplate(i18n(template))
            .addConstraintViolation()
        valid = false
    }

    override fun isValid(value: Target, ctx: ConstraintValidatorContext): Boolean {
        context = ctx.unwrap(HibernateConstraintValidatorContext::class.java)
        context.disableDefaultConstraintViolation()
        valid = true
        checkViolations(value)
        return valid
    }
}
