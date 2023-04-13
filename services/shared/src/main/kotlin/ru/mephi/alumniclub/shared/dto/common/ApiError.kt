package ru.mephi.alumniclub.shared.dto.common

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import java.time.LocalDateTime

@JsonIgnoreProperties("localizedMessage", "cause", "stackTrace", "suppressed")
open class ApiError(
    override var message: String
) : ApiCommonResponse, Exception() {
    var status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    var debugMessage: String = status.name

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var validationErrors: List<ValidationError> = listOf()

    @JsonFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss[.SSS]")
    val timestamp: LocalDateTime = LocalDateTime.now()

    private var logger: AlumniLogger = AlumniLogger(this::class.java)

    constructor(
        status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        message: String,
        debugMessage: String = status.name,
        validationErrors: List<ValidationError> = listOf()
    ) : this(message) {
        this.status = status
        this.debugMessage = debugMessage
        this.validationErrors = validationErrors

        validationErrors.forEach {
            logger.error("Rejected field [${it.field}] with value [${it.rejectedValue}]. Reason: ${it.description}")
        }
    }

    constructor(
        status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        message: String,
        exception: Throwable?,
        debugMessage: String = exception?.message ?: status.name,
        validationErrors: List<ValidationError> = emptyList(),
    ) : this(status, message, debugMessage, validationErrors) {
        logger.error("Error with Status Code \"${status.value()}\":\n ${exception?.stackTraceToString()}")
    }
}