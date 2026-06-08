package com.example.cinema.repository

import com.example.cinema.entity.Showtime
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ShowtimeRepository : JpaRepository<Showtime , UUID> {
}