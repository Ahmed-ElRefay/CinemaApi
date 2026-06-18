package com.example.cinema.auth

import com.example.cinema.user.entity.UserRole
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value($$"${app.jwt.secret}") private val secret: String,
    @Value($$"${app.jwt.expiration-minutes}") private val expirationMinutes: Long,
    ) {

    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(userId: UUID , email: String , role: UserRole): String {
        val now = Instant.now()
        val expiry = now.plus(Duration.ofMinutes(expirationMinutes))

        return Jwts.builder()
            .subject(userId.toString())
            .claim("email" , email)
            .claim("role" , role.name)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiry))
            .signWith(key)
            .compact()
    }

    fun expirationSeconds() : Long = Duration.ofMinutes(expirationMinutes).toSeconds()

}