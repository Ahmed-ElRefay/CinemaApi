package com.example.cinema.showtime.dto

import com.example.cinema.showtime.entity.ShowtimeSeatStatus
import java.math.BigDecimal
import java.util.UUID

data class ShowtimeSeatsResponse(
    val id: UUID,
    val rowLabel: String,
    val seatNumber: Int,
    val seatType: String?,
    val status: ShowtimeSeatStatus,
    val price: BigDecimal,
)
