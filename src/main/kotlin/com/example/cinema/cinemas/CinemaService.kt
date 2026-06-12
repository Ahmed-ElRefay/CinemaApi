package com.example.cinema.cinemas

import com.example.cinema.cinemas.dto.CinemaResponse
import com.example.cinema.cinemas.dto.CreateCinemaRequest
import com.example.cinema.cinemas.entity.Cinema
import org.springframework.stereotype.Service
import com.example.cinema.cinemas.repository.CinemaRepository
import org.springframework.transaction.annotation.Transactional

@Service
class CinemaService (
    private val cinemaRepository: CinemaRepository
) {
    @Transactional
    fun create(request: CreateCinemaRequest): CinemaResponse {
        val cinema = Cinema(name = request.name, city = request.city)
        cinemaRepository.save(cinema)
        return CinemaResponse.from(cinema)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<CinemaResponse> {
        val cinemas = cinemaRepository.findAll()
        return cinemas.map { CinemaResponse.from(it) }
    }
}