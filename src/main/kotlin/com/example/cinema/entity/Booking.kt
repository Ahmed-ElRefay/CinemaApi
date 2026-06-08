package com.example.cinema.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "bookings")
class Booking (
    @Id
    var id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    var showtime: Showtime,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    var status: BookingStatus = BookingStatus.HELD,
    @Column
    var holdExpiresAt: Instant? = null,
    @Column(nullable = false , precision = 10 , scale = 2)
    var totalPrice: BigDecimal
)