package com.example.cinema.showtime.repository

import com.example.cinema.showtime.entity.Showtime
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ShowtimeRepository : JpaRepository<Showtime, UUID> {
}