package com.example.cinema.booking

import com.example.cinema.booking.entity.BookingStatus
import com.example.cinema.showtime.entity.ShowtimeSeatStatus
import com.example.cinema.booking.repository.BookingRepository
import com.example.cinema.booking.repository.BookingSeatRepository
import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class BookingExpirationService(
    private val bookingRepository: BookingRepository,
    private val bookingSeatRepository: BookingSeatRepository
) {

    @CacheEvict(value = ["showtime-seats"], key = "#showtimeId")
    @Transactional
    fun expireBooking(bookingId: UUID , showtimeId: UUID) {
        val booking = bookingRepository.findByIdOrNull(bookingId) ?: return
        if(booking.status != BookingStatus.HELD) return

        val bookingSeats = bookingSeatRepository.findByBookingId(bookingId)

        bookingSeats.forEach { bookingSeat ->
            if (bookingSeat.showtimeSeat.status == ShowtimeSeatStatus.HELD)
                bookingSeat.showtimeSeat.status = ShowtimeSeatStatus.AVAILABLE
        }

        bookingSeatRepository.deleteAll(bookingSeats)
        booking.status = BookingStatus.EXPIRED
        booking.holdExpiresAt = null
    }
}