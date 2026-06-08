package com.example.cinema.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.util.UUID

data class CreateSeatsRequest(
    @field:NotEmpty
    @field:Valid
    val seats: List<SeatRequest>
)

data class SeatRequest(
    @field:NotBlank
    @field:Size(max = 20)
    val rowLabel: String,
    @field:Positive
    val seatNumber: Int,
    val seatType: String? = null,
)