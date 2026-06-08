package com.example.cinema.showtime

import com.example.cinema.dto.CreateShowtimeRequest
import com.example.cinema.entity.Showtime
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/showtimes")
class ShowtimeController(
    private val showtimeService: ShowtimeService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: CreateShowtimeRequest
    ) : Showtime {
        return showtimeService.create(request)
    }
}