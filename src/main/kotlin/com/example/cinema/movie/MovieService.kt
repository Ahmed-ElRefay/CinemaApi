package com.example.cinema.movie

import com.example.cinema.dto.CreateMovieRequest
import com.example.cinema.entity.Movie
import org.springframework.stereotype.Service
import com.example.cinema.repository.MovieRepository

@Service
class MovieService(
    private val movieRepository: MovieRepository
) {

    fun create(
        request: CreateMovieRequest
    ) : Movie {
        val movie = Movie(
            title = request.title,
            duration = request.duration,
            genre = request.genre,
        )

        return movieRepository.save(movie)
    }

    fun findAll(): List<Movie> = movieRepository.findAll()

    fun findByGenre(genre: String): List<Movie> = movieRepository.findByGenre(genre)
}