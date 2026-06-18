package com.example.cinema.user

import com.example.cinema.user.dto.CreateUserRequest
import com.example.cinema.user.dto.UserResponse
import com.example.cinema.user.entity.User
import com.example.cinema.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun create(request: CreateUserRequest): UserResponse {
        val user = User(
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password)!!,
        )
        userRepository.save(user)
        return UserResponse.from(user)
    }
}