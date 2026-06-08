package com.example.cinema.repository

import com.example.cinema.entity.Hall
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface HallRepository: JpaRepository<Hall, UUID> {
    fun findByCinemaId(cinemaId: UUID): List<Hall>
}