package com.example.cinema.auth

import com.example.cinema.auth.dto.LoginRequest
import com.example.cinema.auth.dto.LoginResponse
import com.example.cinema.common.exception.InvalidCredentialsException
import com.example.cinema.user.repository.UserRepository
import io.jsonwebtoken.Jwts
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    @Transactional(readOnly = true)
    fun login(request: LoginRequest): LoginResponse{
        val user = userRepository.findByEmail(request.email)
            ?: throw InvalidCredentialsException()

        if (!passwordEncoder.matches(request.password, user.password))
            throw InvalidCredentialsException()

        val token = jwtService.generateToken(user.id , user.email , user.role)

        return LoginResponse(
            token = token,
            expiresIn = jwtService.expirationSeconds())
    }
}