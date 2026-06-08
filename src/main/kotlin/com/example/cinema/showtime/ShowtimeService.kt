package com.example.cinema.showtime

import com.example.cinema.dto.CreateShowtimeRequest
import com.example.cinema.entity.Showtime
import com.example.cinema.entity.ShowtimeSeat
import com.example.cinema.repository.HallRepository
import com.example.cinema.repository.MovieRepository
import com.example.cinema.repository.SeatRepository
import com.example.cinema.repository.ShowtimeRepository
import com.example.cinema.repository.ShowtimeSeatRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ShowtimeService(
    private val showtimeRepository: ShowtimeRepository,
    private val showtimeSeatRepository: ShowtimeSeatRepository,
    private val movieRepository: MovieRepository,
    private val hallRepository: HallRepository,
    private val seatRepository: SeatRepository,
) {

    @Transactional
    fun create(request: CreateShowtimeRequest): Showtime{
        val movie = movieRepository.findByIdOrNull(request.movieId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found ${request.movieId}")

        val hall = hallRepository.findByIdOrNull(request.hallId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Hall not found ${request.hallId}")

        val seats = seatRepository.findByHallId(request.hallId)
        if (seats.isEmpty())
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "hall ${hall.id} has no seats configured")

        val showtime = Showtime(
            movie = movie,
            hall = hall,
            basePrice = request.basePrice,
            startTime = request.startTime,
        )

       showtimeRepository.save(showtime)

        val showtimeSeats = seats.map { seat ->
            ShowtimeSeat(
                showtime = showtime,
                seat = seat,
                price = request.basePrice
            )
        }
        showtimeSeatRepository.saveAll(showtimeSeats)
        return showtime
    }


}