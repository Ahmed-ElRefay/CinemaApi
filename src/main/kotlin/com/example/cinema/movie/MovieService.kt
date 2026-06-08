package com.example.cinema.movie

import com.example.cinema.movie.dto.CreateMovieRequest
import com.example.cinema.movie.dto.MovieResponse
import com.example.cinema.movie.entity.Movie
import org.springframework.stereotype.Service
import com.example.cinema.movie.repository.MovieRepository

@Service
class MovieService(
    private val movieRepository: MovieRepository
) {

    fun create(
        request: CreateMovieRequest
    ) : MovieResponse {
        val movie = Movie(
            title = request.title,
            duration = request.duration,
            genre = request.genre,
        )
        movieRepository.save(movie)
        return MovieResponse.from(movie)
    }

    fun findAll(): List<MovieResponse> = movieRepository.findAll().map {
        MovieResponse.from(it)
    }

    fun findByGenre(genre: String): List<MovieResponse> = movieRepository.findByGenre(genre).map {
        MovieResponse.from(it)
    }
}