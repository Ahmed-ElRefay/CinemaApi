package com.example.cinema.movie.repository

import com.example.cinema.movie.entity.Movie
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MovieRepository: JpaRepository<Movie, UUID> {
    fun findByGenre(genre: String): List<Movie>
}