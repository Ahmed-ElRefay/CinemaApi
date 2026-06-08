package com.example.cinema.showtime.entity

import com.example.cinema.hall.entity.Hall
import com.example.cinema.movie.entity.Movie
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "showtimes")
class Showtime(
    @Id
    var id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    var hall: Hall,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    var movie: Movie,

    @Column(nullable = false)
    var startTime: Instant,

    @Column(nullable = false ,precision = 10 ,scale = 2)
    var basePrice: BigDecimal
)