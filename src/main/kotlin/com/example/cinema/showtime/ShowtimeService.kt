package com.example.cinema.showtime

import com.example.cinema.common.exception.NotFoundException
import com.example.cinema.hall.entity.Seat
import com.example.cinema.showtime.dto.CreateShowtimeRequest
import com.example.cinema.showtime.entity.Showtime
import com.example.cinema.showtime.entity.ShowtimeSeat
import com.example.cinema.hall.repository.HallRepository
import com.example.cinema.movie.repository.MovieRepository
import com.example.cinema.hall.repository.SeatRepository
import com.example.cinema.showtime.dto.ShowtimeResponse
import com.example.cinema.showtime.dto.ShowtimeSeatsResponse
import com.example.cinema.showtime.repository.ShowtimeRepository
import com.example.cinema.showtime.repository.ShowtimeSeatRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class ShowtimeService(
    private val showtimeRepository: ShowtimeRepository,
    private val showtimeSeatRepository: ShowtimeSeatRepository,
    private val movieRepository: MovieRepository,
    private val hallRepository: HallRepository,
    private val seatRepository: SeatRepository,
) {

    @Transactional
    fun create(request: CreateShowtimeRequest): ShowtimeResponse{
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
        return ShowtimeResponse.from(showtime)
    }

    @Transactional(readOnly =true)
    fun findSeatsForShowtime(showtimeId: UUID): List<ShowtimeSeatsResponse> {

        if (!showtimeRepository.existsById(showtimeId))
            throw NotFoundException("showtime $showtimeId is not found")

        val seats = showtimeSeatRepository.findByShowtimeIdWithSeat(showtimeId)
        return seats.map {
            ShowtimeSeatsResponse(
                id = it.id,
                rowLabel = it.seat.rowLabel,
                seatNumber = it.seat.seatNumber,
                seatType = it.seat.seatType,
                status = it.status,
                price = it.price,
            )
        }

    }


}