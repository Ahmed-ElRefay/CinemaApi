package com.example.cinema.movie

import com.example.cinema.movie.dto.CreateMovieRequest
import com.example.cinema.movie.dto.MovieResponse
import com.example.cinema.movie.entity.Movie
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/movies")
class MovieController(
    private val movieService: MovieService
) {

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
       @Valid @RequestBody request: CreateMovieRequest): MovieResponse {
        return movieService.create(request)
    }

    @GetMapping
    fun find(
        @RequestParam(required = false) genre: String?
    ) : List<MovieResponse> {
        return if (genre != null)
            movieService.findByGenre(genre)
        else
            movieService.findAll()
    }
}