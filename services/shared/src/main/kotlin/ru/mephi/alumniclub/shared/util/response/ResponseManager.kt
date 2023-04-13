package ru.mephi.alumniclub.shared.util.response

import org.springframework.beans.factory.annotation.Autowired
import ru.mephi.alumniclub.shared.dto.common.ValidationException
import javax.validation.Validator

abstract class ResponseManager {
    protected lateinit var responseHandler: ResponseHandler
    protected lateinit var validator: Validator

    @Autowired
    fun injectErrorHandler(responseHandler: ResponseHandler) {
        this.responseHandler = responseHandler
    }

    @Autowired
    fun injectValidator(validator: Validator) {
        this.validator = validator
    }

    fun <T> validate(obj: T) {
        val violations = validator.validate(obj)
        if (violations.isNotEmpty()) throw ValidationException(violations)
    }

    fun i18n(label: String, vararg args: String?) = responseHandler.i18n(label, *args)

}