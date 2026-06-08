package com.example.cinema.cinemas.dto

import com.example.cinema.cinemas.entity.Cinema
import java.util.UUID

data class CinemaResponse(
    val id: UUID,
    val name: String,
    val city: String?
){
    companion object {
        fun from(cinema: Cinema): CinemaResponse {
            return CinemaResponse(
                id = cinema.id,
                name = cinema.name,
                city = cinema.city
            )
        }
    }
}
