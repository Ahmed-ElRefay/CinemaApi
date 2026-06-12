package com.example.cinema.showtime.repository

import com.example.cinema.showtime.entity.ShowtimeSeat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ShowtimeSeatRepository : JpaRepository<ShowtimeSeat, UUID> {


    fun findAllByIdInAndShowtimeId(ids: Collection<UUID>, showtimeId: UUID) : List<ShowtimeSeat>

    @Query("""
        SELECT ss FROM ShowtimeSeat ss
        JOIN FETCH ss.seat WHERE ss.showtime.id = :showtimeId
    """)
    fun findByShowtimeIdWithSeat(showtimeId: UUID): List<ShowtimeSeat>
}