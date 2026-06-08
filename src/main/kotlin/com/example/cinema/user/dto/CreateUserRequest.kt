package com.example.cinema.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class CreateUserRequest(
    @field:Size(min = 3, max = 100, message = "Name must be between 1 and 255")
    val name: String,
    @field:Size(max = 150, message = "email maximum must be 150")
    @field:Email("Invalid Email Format")
    val email: String,
)
