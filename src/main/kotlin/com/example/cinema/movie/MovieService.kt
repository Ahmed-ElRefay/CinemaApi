package com.example.cinema.movie

import com.example.cinema.movie.dto.CreateMovieRequest
import com.example.cinema.movie.dto.MovieResponse
import com.example.cinema.movie.entity.Movie
import org.springframework.stereotype.Service
import com.example.cinema.movie.repository.MovieRepository
import org.springframework.transaction.annotation.Transactional

@Service
class MovieService(
    private val movieRepository: MovieRepository
) {

@Transactional
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

    @Transactional(readOnly = true)
    fun findAll(): List<MovieResponse> = movieRepository.findAll().map {
        MovieResponse.from(it)
    }

    @Transactional(readOnly = true)
    fun findByGenre(genre: String): List<MovieResponse> = movieRepository.findByGenre(genre).map {
        MovieResponse.from(it)
    }
}