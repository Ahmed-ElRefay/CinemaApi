package com.example.cinema.showtime

import com.example.cinema.showtime.dto.CreateShowtimeRequest
import com.example.cinema.showtime.dto.ShowtimeResponse
import com.example.cinema.showtime.dto.ShowtimeSeatsResponse
import com.example.cinema.showtime.entity.Showtime
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/showtimes")
class ShowtimeController(
    private val showtimeService: ShowtimeService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: CreateShowtimeRequest
    ) : ShowtimeResponse {
        return showtimeService.create(request)
    }

    @GetMapping("/{showtimeId}/seats")
    fun findByShowtime(
        @PathVariable showtimeId: UUID
    ): ResponseEntity<List<ShowtimeSeatsResponse>>{
        val seats = showtimeService.findSeatsForShowtime(showtimeId)
        return ResponseEntity.ok(seats)
    }
}