package com.example.cinema.hall.repository

import com.example.cinema.hall.entity.Hall
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface HallRepository: JpaRepository<Hall, UUID> {
    fun findByCinemaId(cinemaId: UUID): List<Hall>
}