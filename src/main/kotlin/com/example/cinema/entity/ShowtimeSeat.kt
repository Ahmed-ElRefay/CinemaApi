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
import jakarta.persistence.Version
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "showtime_seats")
class ShowtimeSeat(
    @Id
    var id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id", nullable = false)
    var showtime: Showtime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    var seat: Seat,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    var status: ShowtimeSeatStatus = ShowtimeSeatStatus.AVAILABLE,

    @Column(nullable = false , precision = 10 , scale = 2)
    var price: BigDecimal,

    @Version
    @Column(nullable = false)
    var version: Int = 0
)