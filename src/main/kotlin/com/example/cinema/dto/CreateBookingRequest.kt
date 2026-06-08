package com.example.cinema.dto

import jakarta.validation.constraints.NotEmpty
import java.util.UUID

data class CreateBookingRequest(
    val userId: UUID,
    val showtimeId: UUID,
    @field:NotEmpty
    val seatIds: List<UUID>,
)
