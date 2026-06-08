package com.example.cinema.hall

import com.example.cinema.dto.CreateHallRequest
import com.example.cinema.dto.CreateSeatsRequest
import com.example.cinema.entity.Hall
import com.example.cinema.entity.Seat
import com.example.cinema.repository.CinemaRepository
import com.example.cinema.repository.HallRepository
import com.example.cinema.repository.SeatRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class HallService(
    private val hallRepository: HallRepository,
    private val cinemaRepository: CinemaRepository,
    private val seatRepository: SeatRepository
) {

    @Transactional
    fun create(request: CreateHallRequest) : Hall {
        val cinema = cinemaRepository.findByIdOrNull(request.cinemaId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Cinema ${request.cinemaId} not found"
        )

        val hall = Hall(
            cinema = cinema,
            name = request.name,
        )
        hallRepository.save(hall)
        return hall
    }

    fun findByCinemaId(cinemaId: UUID): List<Hall> {
        return hallRepository.findByCinemaId(cinemaId)
    }

    @Transactional
    fun createSeats(hallId: UUID ,  request: CreateSeatsRequest): List<Seat> {
        val hall = hallRepository.findByIdOrNull(hallId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Hall $hallId not found"
        )
        val seats = request.seats.map { seat ->
            Seat(
                hall = hall,
                rowLabel = seat.rowLabel,
                seatNumber = seat.seatNumber,
                seatType = seat.seatType,
            )
        }
        return seatRepository.saveAll(seats)
    }

}