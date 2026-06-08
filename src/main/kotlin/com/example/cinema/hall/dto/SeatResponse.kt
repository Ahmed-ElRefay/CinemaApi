package com.example.cinema.hall.dto

import com.example.cinema.hall.entity.Seat
import java.util.UUID

data class SeatResponse(
    val id: UUID,
    val rowLabel: String,
    val seatNumber: Int,
    val seatType: String?,
){
    companion object{
        fun from(seat: Seat): SeatResponse{
            return SeatResponse(
                id = seat.id,
                rowLabel = seat.rowLabel,
                seatNumber = seat.seatNumber,
                seatType = seat.seatType
            )
        }
    }
}
