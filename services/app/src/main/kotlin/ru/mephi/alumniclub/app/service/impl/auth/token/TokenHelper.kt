package ru.mephi.alumniclub.app.service.impl.auth.token

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.security.AuthenticationToken
import ru.mephi.alumniclub.shared.dto.security.PermissionsDTO
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.model.enums.Authority
import ru.mephi.alumniclub.shared.model.exceptions.common.CorruptedTokenException
import ru.mephi.alumniclub.shared.util.extension.authorities
import ru.mephi.alumniclub.shared.util.extension.userId
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.math.BigInteger
import java.security.MessageDigest
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class TokenHelper(
    @Value("\${spring.security.jwt.secret}") private val secret: String,
    @Value("\${spring.security.jwt.access.lifetime}") private val accessTokenLifetime: Long,
    @Value("\${spring.security.jwt.refresh.lifetime}") private val refreshTokenLifetime: Long,
) : ResponseManager() {
    private val algorithm = SignatureAlgorithm.HS256
    private val key = Keys.hmacShaKeyFor(secret.toByteArray())
    private val prefix = "Bearer "
    private val cipher = MessageDigest.getInstance("SHA-1")

    private fun parseJwsPermissions(claims: Jws<Claims>): PermissionsDTO {
        val body = claims.body["permissions"] as? LinkedHashMap<*, *> ?: throw CorruptedTokenException()
        val scopes = body["scopes"] as? List<*> ?: throw CorruptedTokenException()
        val projects = body["projects"] as? List<*> ?: throw CorruptedTokenException()
        return PermissionsDTO(
            scopes = scopes.map { ScopePermission.valueOf(it as String) },
            projects = projects.map { (it as Int).toLong() }
        )
    }

    fun hash(token: String): String {
        val hash = cipher.digest(token.toByteArray())
        return BigInteger(1, hash).toString(16)
    }

    fun getAccessTokenExpiration(): Date =
        Instant.now().plus(accessTokenLifetime, ChronoUnit.DAYS).let { Date.from(it) }

    fun getRefreshTokenExpiration(): Date =
        Instant.now().plus(refreshTokenLifetime, ChronoUnit.DAYS).let { Date.from(it) }

    fun parseToken(token: String): Jws<Claims> {
        return try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
        } catch (e: ExpiredJwtException) {
            throw ApiError(HttpStatus.UNAUTHORIZED, i18n("exception.auth.accessTokenExpired"), exception = e)
        } catch (e: JwtException) {
            throw CorruptedTokenException()
        }
    }

    fun generateAccessToken(user: User): String {
        val expiration = getAccessTokenExpiration()
        val authorities = mutableListOf(Authority.USER)
        if (user.admin) authorities.add(Authority.ADMIN)
        if (user.moderator) authorities.add(Authority.MODERATOR)
        return Jwts.builder()
            .claim("userId", user.id)
            .claim("permissions", user.permissions)
            .claim("authorities", authorities)
            .setExpiration(expiration).signWith(key, algorithm).compact()
    }

    fun createAuthToken(header: String): AuthenticationToken {
        if (!header.startsWith(prefix)) throw CorruptedTokenException()
        val principal = parseTokenPrincipalFromHeader(header)
        val authorities = principal.authorities.map { it.grantedAuthority }
        return AuthenticationToken(principal.userId, principal.permissions, authorities)
    }

    fun parseTokenPrincipalFromHeader(tokenFromHeader: String): UserPrincipal {
        val token = tokenFromHeader.replace(prefix, "")
        val claims = parseToken(token)
        val permissions = parseJwsPermissions(claims)
        return UserPrincipal(claims.userId, permissions, claims.authorities)
    }
}
