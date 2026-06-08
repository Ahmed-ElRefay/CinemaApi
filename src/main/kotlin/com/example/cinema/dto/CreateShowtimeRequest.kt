package com.example.cinema.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Positive
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class CreateShowtimeRequest(
    val hallId: UUID,
    val movieId: UUID,
    @field:Future val startTime: Instant,
    @field:Positive val basePrice: BigDecimal
)
