package com.example.cinema.showtime.dto

import com.example.cinema.showtime.entity.Showtime
import java.time.Instant
import java.util.UUID

data class ShowtimeResponse(
    val id: UUID,
    val hallName: String,
    val movieTitle: String,
    val startTime: Instant,
){
    companion object {
        fun from(showtime: Showtime): ShowtimeResponse{
            return ShowtimeResponse(
                id = showtime.id,
                hallName = showtime.hall.name,
                movieTitle = showtime.movie.title,
                startTime = showtime.startTime,
            )
        }
    }
}
