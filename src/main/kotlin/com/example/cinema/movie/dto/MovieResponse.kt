package com.example.cinema.movie.dto

import com.example.cinema.movie.entity.Movie
import java.util.UUID

data class MovieResponse(
    val id: UUID,
    val title: String,
    val duration: Int?,
    val genre: String?,
){
    companion object {
        fun from(movie: Movie): MovieResponse {
            return MovieResponse(
                id = movie.id,
                title = movie.title,
                duration = movie.duration,
                genre = movie.genre,
            )
        }
    }
}
