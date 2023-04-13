package ru.mephi.alumniclub.shared.util.extension

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.ServerResponse.created
import org.springframework.web.servlet.function.paramOrNull
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.dto.security.AuthenticationToken
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.model.enums.Authority
import ru.mephi.alumniclub.shared.model.exceptions.common.MissingParameterException
import ru.mephi.alumniclub.shared.model.exceptions.common.MissingPathVariableException
import ru.mephi.alumniclub.shared.model.exceptions.common.PrincipalNotFoundException
import ru.mephi.alumniclub.shared.util.Cursor
import ru.mephi.alumniclub.shared.util.constants.DEFAULT_PAGE_FIELD_PARAM
import ru.mephi.alumniclub.shared.util.constants.DEFAULT_PAGE_NUMBER_PARAM
import ru.mephi.alumniclub.shared.util.constants.DEFAULT_PAGE_SIZE_PARAM
import java.net.URI
import java.time.LocalDateTime
import javax.servlet.http.Part

fun Any?.toOkBody(): ServerResponse {
    if (this == null) return ServerResponse.ok().build()
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(this)
}

fun Any.toCreatedResponse(location: String): ServerResponse {
    return created(URI(location)).contentType(MediaType.APPLICATION_JSON).body(this)
}

fun ServerRequest.jsonParam(name: String): String? {
    var value = paramOrNull(name) ?: return null
    if (!value.startsWith("\"")) value = "\"$value"
    if (!value.endsWith("\"")) value = "$value\""
    return value
}

fun ServerRequest.jsonPathVariable(name: String): String {
    var value = try {
        pathVariable(name)
    } catch (e: IllegalArgumentException) {
        throw MissingPathVariableException(name)
    }
    if (!value.startsWith("\"")) value = "\"$value"
    if (!value.endsWith("\"")) value = "$value\""
    return value
}

inline fun <reified T> ServerRequest.paramOrThrow(name: String): T {
    if (param(name).isEmpty) throw MissingParameterException(name)
    val value = jsonParam(name)
    val mapper = jacksonObjectMapper()
    return mapper.readValue(value, T::class.java)
}

inline fun <reified T> ServerRequest.paramOrElse(name: String, default: () -> T): T {
    val value = jsonParam(name) ?: return default()
    val mapper = jacksonObjectMapper().also { it.findAndRegisterModules() }
    return try {
        mapper.readValue(value, T::class.java)
    } catch (e: JsonProcessingException) {
        default()
    }
}

inline fun <reified T : Any> ServerRequest.pathVariableOrThrow(name: String): T {
    val value = jsonPathVariable(name)
    val mapper = jacksonObjectMapper().also { it.findAndRegisterModules() }
    return mapper.readValue(value, T::class.java)
}

fun ServerRequest.getMultiPartPhoto(key: String = "photo"): Part? {
    return multipartData()[key]?.firstOrNull()
}

fun ServerRequest.getCursorRequest(): Cursor {
    val from = paramOrElse("from") { LocalDateTime.now() }
    val page = getExtendedPageRequest()
    val chronology = paramOrElse("chronology") { Cursor.Chronology.BEFORE }
    return Cursor(from, page.pageable, chronology)
}

fun ServerRequest.getExtendedPageRequest(
    defaultPage: Int = DEFAULT_PAGE_NUMBER_PARAM,
    defaultSize: Int = DEFAULT_PAGE_SIZE_PARAM,
    defaultOrder: Sort.Direction = Sort.DEFAULT_DIRECTION,
    defaultField: String = DEFAULT_PAGE_FIELD_PARAM
) = ExtendedPageRequest(
    page = paramOrElse("page") { defaultPage },
    size = paramOrElse("size") { defaultSize },
    order = paramOrElse("order") { defaultOrder },
    field = paramOrElse("field") { defaultField }
)

fun ServerRequest.getAuthenticationToken(): AuthenticationToken {
    return try {
        principal().get() as AuthenticationToken
    } catch (e: NoSuchElementException) {
        throw PrincipalNotFoundException()
    }
}

fun ServerRequest.getPrincipal(): Long {
    return getAuthenticationToken().principal
}

fun ServerRequest.hasOneOfPermission(vararg permissions: ScopePermission) =
    getAuthenticationToken().hasOneOfPermission(*permissions)

fun ServerRequest.assertHasOneOfPermission(vararg permissions: ScopePermission) =
    getAuthenticationToken().assertHasOneOfPermission(*permissions)

fun ServerRequest.assertHasAuthority(authority: Authority) =
    getAuthenticationToken().assertHasAuthority(authority)
