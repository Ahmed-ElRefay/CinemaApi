package com.example.cinema.cinemas

import com.example.cinema.dto.CreateCinemaRequest
import com.example.cinema.entity.Cinema
import org.springframework.stereotype.Service
import com.example.cinema.repository.CinemaRepository

@Service
class CinemaService (
    private val cinemaRepository: CinemaRepository
) {
    fun create(request: CreateCinemaRequest): Cinema {
        val cinema = Cinema(name = request.name, city = request.city)
        return cinemaRepository.save(cinema)
    }

    fun findAll(): List<Cinema> = cinemaRepository.findAll()
}