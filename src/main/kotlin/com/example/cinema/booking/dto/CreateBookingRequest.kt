package com.example.cinema.booking.dto

import jakarta.validation.constraints.NotEmpty
import java.util.UUID

data class CreateBookingRequest(
    val showtimeId: UUID,
    @field:NotEmpty
    val seatIds: List<UUID>,
)