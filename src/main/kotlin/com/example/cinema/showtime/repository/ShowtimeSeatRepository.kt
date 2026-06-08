package com.example.cinema.showtime.repository

import com.example.cinema.showtime.entity.ShowtimeSeat
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ShowtimeSeatRepository : JpaRepository<ShowtimeSeat, UUID> {
    fun findAllByIdInAndShowtimeId(ids: Collection<UUID>, showtimeId: UUID) : List<ShowtimeSeat>
}