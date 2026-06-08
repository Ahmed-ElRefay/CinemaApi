package com.example.cinema.dto

import jakarta.validation.constraints.NotBlank

data class CreateCinemaRequest(
    @field:NotBlank
    val name: String,
    val city: String? = null,
)
