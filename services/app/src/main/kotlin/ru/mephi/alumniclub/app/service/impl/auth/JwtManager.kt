package ru.mephi.alumniclub.app.service.impl.auth

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.shared.model.exceptions.common.CorruptedTokenException

@Service
class JwtManager(
    @Value("\${spring.security.email.unsubscribe-secret}")
    private val secret: String
) {
    private val algorithm = SignatureAlgorithm.HS256
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())

    /**
     * Parses a JWT token and returns its claims.
     *
     * @param token The JWT token to parse.
     * @return The claims contained in the JWT token.
     * @throws CorruptedTokenException if the token is corrupted or invalid.
     */
    fun parseToken(token: String): Jws<Claims> {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
        } catch (throwable: JwtException) {
            throw CorruptedTokenException()
        }
    }

    /**
     * Generates a JSON Web Token (JWT) using a [Map] of claims.
     *
     * @param map A [Map] of claims to include in the JWT.
     * @return A representation of the generated JWT as a [String].
     */
    fun getToken(map: Map<String, Any>): String {
        return Jwts.builder()
            .addClaims(map)
            .signWith(key, algorithm)
            .compact()
    }
}
