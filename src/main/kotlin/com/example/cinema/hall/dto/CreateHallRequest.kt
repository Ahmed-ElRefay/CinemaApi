package com.example.cinema.hall.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateHallRequest(
    val cinemaId: UUID,
    @field:Size(max = 50)
    @field:NotBlank
    val name: String
)