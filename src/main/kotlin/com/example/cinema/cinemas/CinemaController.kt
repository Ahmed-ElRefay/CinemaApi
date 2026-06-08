package com.example.cinema.cinemas

import com.example.cinema.dto.CreateCinemaRequest
import com.example.cinema.entity.Cinema
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/cinemas")
class CinemaController(
    private val cinemaService: CinemaService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateCinemaRequest): Cinema {
        return cinemaService.create(request)
    }

    @GetMapping
    fun findAll(): List<Cinema> = cinemaService.findAll()
}