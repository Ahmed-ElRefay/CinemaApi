package com.example.cinema.dto

import jakarta.validation.constraints.NotBlank

data class CreateMovieRequest(
    @field:NotBlank
    val title: String,
    val duration: Int? = null,
    val genre: String? = null,
)

