package com.example.cinema.hall

import com.example.cinema.dto.CreateHallRequest
import com.example.cinema.dto.CreateSeatsRequest
import com.example.cinema.entity.Hall
import com.example.cinema.entity.Seat
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/halls")
class HallController(
    private val hallService: HallService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateHallRequest): Hall {
        return hallService.create(request)
    }

    @GetMapping
    fun getByCinemaId(
       @RequestParam cinemaId: UUID): List<Hall> {
        return hallService.findByCinemaId(cinemaId)
    }

    @PostMapping("/{hallId}/seats")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSeats(
        @PathVariable hallId: UUID,
        @Valid @RequestBody request: CreateSeatsRequest
    ): List<Seat>{
        return hallService.createSeats(hallId, request)
    }
}