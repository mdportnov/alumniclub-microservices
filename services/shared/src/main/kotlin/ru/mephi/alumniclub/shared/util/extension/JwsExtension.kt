package ru.mephi.alumniclub.shared.util.extension

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import ru.mephi.alumniclub.shared.model.enums.Authority
import ru.mephi.alumniclub.shared.model.exceptions.common.CorruptedTokenException

val Jws<Claims>.userId: Long
    get() {
        return try {
            (body.get("userId", Integer::class.java)?.toLong()) ?: throw CorruptedTokenException()
        } catch (e: Exception) {
            throw CorruptedTokenException()
        }
    }

val Jws<Claims>.authorities: List<Authority>
    get() = (body["authorities"] as List<*>).map { Authority.valueOf(it as String) }