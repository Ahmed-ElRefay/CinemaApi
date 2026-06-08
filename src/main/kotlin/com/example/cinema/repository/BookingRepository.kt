package com.example.cinema.repository

import com.example.cinema.entity.Booking
import com.example.cinema.entity.BookingStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import java.util.UUID

interface BookingRepository : JpaRepository<Booking, UUID> {

    fun findByStatusAndHoldExpiresAtBefore(status: BookingStatus , time: Instant): List<Booking>
}