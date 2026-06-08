package com.example.cinema.repository

import com.example.cinema.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SeatRepository: JpaRepository<Seat, UUID> {
    fun findByHallId(hallId: UUID): List<Seat>
}