package com.example.cinema.booking.repository

import com.example.cinema.booking.entity.BookingSeat
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BookingSeatRepository: JpaRepository<BookingSeat, UUID> {

    fun findByBookingId(bookingId: UUID): List<BookingSeat>
}