package com.example.cinema.booking.dto

import com.example.cinema.booking.entity.Booking
import com.example.cinema.booking.entity.BookingStatus
import com.example.cinema.showtime.entity.Showtime
import com.example.cinema.showtime.entity.ShowtimeSeat
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class BookingResponse(
    val id: UUID,
    val status: BookingStatus,
    val holdExpiresAt: Instant?,
    val totalPrice: BigDecimal,
    val showtime: ShowtimeSummary,
    val seats: List<SeatBookingResponse>,
){
    companion object {
        fun from (booking: Booking, showtimeSummary: ShowtimeSummary, seats: List<SeatBookingResponse>): BookingResponse {
            return BookingResponse(
                id = booking.id,
                status = booking.status,
                holdExpiresAt = booking.holdExpiresAt,
                totalPrice = booking.totalPrice,
                showtime = showtimeSummary,
                seats = seats
            )
        }
    }
}


data class ShowtimeSummary(
    val id: UUID,
    val hallName: String,
    val movieTitle: String,
    val startTime: Instant,
){
    companion object {
        fun from(showtime: Showtime): ShowtimeSummary{
            return ShowtimeSummary(
                id = showtime.id,
                hallName = showtime.hall.name,
                movieTitle = showtime.movie.title,
                startTime = showtime.startTime,
            )
        }
    }
}

data class SeatBookingResponse(
    val showtimeSeatId: UUID,
    val rowLabel: String,
    val seatNumber: Int,
    val price: BigDecimal
){
    companion object {
        fun from(showtimeSeat: ShowtimeSeat): SeatBookingResponse{
            return SeatBookingResponse(
                showtimeSeatId = showtimeSeat.id,
                rowLabel = showtimeSeat.seat.rowLabel,
                seatNumber = showtimeSeat.seat.seatNumber,
                price = showtimeSeat.price
            )
        }
    }
}

