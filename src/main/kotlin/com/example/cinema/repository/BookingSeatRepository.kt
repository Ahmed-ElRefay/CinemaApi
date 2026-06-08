package com.example.cinema.repository

import com.example.cinema.entity.BookingSeat
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BookingSeatRepository: JpaRepository<BookingSeat, UUID> {

    fun findByBookingId(bookingId: UUID): List<BookingSeat>
}