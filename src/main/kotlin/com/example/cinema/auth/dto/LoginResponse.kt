package com.example.cinema.auth.dto

data class LoginResponse(
    val token: String,
    val expiresIn : Long
)
