package com.example.cinema.repository

import com.example.cinema.entity.ShowtimeSeat
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ShowtimeSeatRepository : JpaRepository<ShowtimeSeat , UUID> {
    fun findAllByIdInAndShowtimeId(ids: Collection<UUID> , showtimeId: UUID) : List<ShowtimeSeat>
}