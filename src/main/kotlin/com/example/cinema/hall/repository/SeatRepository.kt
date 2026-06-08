package com.example.cinema.hall.repository

import com.example.cinema.hall.entity.Seat
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface SeatRepository: JpaRepository<Seat, UUID> {
    fun findByHallId(hallId: UUID): List<Seat>
}