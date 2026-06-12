package com.example.cinema.hall

import com.example.cinema.hall.dto.CreateHallRequest
import com.example.cinema.hall.dto.CreateSeatsRequest
import com.example.cinema.hall.entity.Hall
import com.example.cinema.hall.entity.Seat
import com.example.cinema.cinemas.repository.CinemaRepository
import com.example.cinema.hall.dto.HallResponse
import com.example.cinema.hall.dto.SeatRequest
import com.example.cinema.hall.dto.SeatResponse
import com.example.cinema.hall.repository.HallRepository
import com.example.cinema.hall.repository.SeatRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class HallService(
    private val hallRepository: HallRepository,
    private val cinemaRepository: CinemaRepository,
    private val seatRepository: SeatRepository
) {

    @Transactional
    fun create(request: CreateHallRequest) : HallResponse {
        val cinema = cinemaRepository.findByIdOrNull(request.cinemaId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Cinema ${request.cinemaId} not found"
        )

        val hall = Hall(
            cinema = cinema,
            name = request.name,
        )
        hallRepository.save(hall)
        return HallResponse.from(hall)
    }

    @Transactional(readOnly = true)
    fun findByCinemaId(cinemaId: UUID): List<HallResponse> {
        val halls = hallRepository.findByCinemaId(cinemaId)
        if (halls.isEmpty())
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Cinema $cinemaId doesn't have halls")
        return halls.map { HallResponse.from(it) }
    }

    @Transactional
    fun createSeats(hallId: UUID ,  request: CreateSeatsRequest): List<SeatResponse> {
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
        seatRepository.saveAll(seats)
        return seats.map { SeatResponse.from(it) }
    }

}