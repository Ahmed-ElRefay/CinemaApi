package com.example.cinema.booking

import com.example.cinema.booking.dto.BookingResponse
import com.example.cinema.booking.dto.CreateBookingRequest
import com.example.cinema.booking.dto.SeatBookingResponse
import com.example.cinema.booking.dto.ShowtimeSummary
import com.example.cinema.booking.entity.Booking
import com.example.cinema.booking.entity.BookingSeat
import com.example.cinema.showtime.entity.ShowtimeSeatStatus
import com.example.cinema.booking.repository.BookingRepository
import com.example.cinema.booking.repository.BookingSeatRepository
import com.example.cinema.showtime.repository.ShowtimeRepository
import com.example.cinema.showtime.repository.ShowtimeSeatRepository
import com.example.cinema.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Duration
import java.time.Instant

@Service
class BookingService(
    private val userRepository: UserRepository,
    private val showtimeRepository: ShowtimeRepository,
    private val showtimeSeatRepository: ShowtimeSeatRepository,
    private val bookingRepository: BookingRepository,
    private val bookingSeatRepository: BookingSeatRepository,
) {

    @Transactional
    fun create(request: CreateBookingRequest): BookingResponse {
        val user = userRepository.findByIdOrNull(request.userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User ${request.userId} not found")
        val showtime = showtimeRepository.findByIdOrNull(request.showtimeId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Showtime ${request.showtimeId} is not found")

        val showtimeSeats = showtimeSeatRepository.findAllByIdInAndShowtimeId(request.seatIds, request.showtimeId)

        //Thread.sleep(5000)

        if (showtimeSeats.size != request.seatIds.size)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "some ids were invalid or didn't belong to this showtime"
            )

        if (showtimeSeats.any { it.status != ShowtimeSeatStatus.AVAILABLE }) {
            throw ResponseStatusException(HttpStatus.CONFLICT, "One or more seats are not available")
        }

        val totalPrice = showtimeSeats.sumOf { it.price }

        showtimeSeats.forEach { it.status = ShowtimeSeatStatus.HELD }

        showtimeSeatRepository.flush()

        val booking = Booking(
            user = user,
            showtime = showtime,
            holdExpiresAt = Instant.now().plus(Duration.ofMinutes(7)),
            totalPrice = totalPrice,
        )

        bookingRepository.save(booking)
        val bookingSeats = showtimeSeats.map {
            BookingSeat(
                booking = booking,
                showtimeSeat = it
            )
        }
        bookingSeatRepository.saveAll(bookingSeats)

        return BookingResponse.from(
            booking,
            ShowtimeSummary.from(showtime),
            showtimeSeats.map {
                SeatBookingResponse.from(it)
            }
            )
    }
}