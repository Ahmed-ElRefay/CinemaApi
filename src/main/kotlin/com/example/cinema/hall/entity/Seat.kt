package com.example.cinema.hall.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "seats")
class Seat (
    @Id
    var id: UUID = UUID.randomUUID(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hall_id", nullable = false)
    var hall: Hall,
    @Column(name = "row_label", nullable = false , length = 20)
    var rowLabel: String,
    @Column(name = "seat_number" , nullable = false)
    var seatNumber: Int,
    @Column(name= "seat_type" ,length = 20)
    var seatType: String? = null,
)