package com.example.cinema.booking.entity

import com.example.cinema.showtime.entity.ShowtimeSeat
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "booking_seats")
class BookingSeat(
    @Id
    var id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    var booking: Booking,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_seat_id", nullable = false , unique = true)
    var showtimeSeat: ShowtimeSeat
)