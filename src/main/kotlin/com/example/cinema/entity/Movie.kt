package com.example.cinema.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "movies")
class Movie(
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false , length = 100)
    var title: String,

    @Column(name = "duration_minutes")
    var duration: Int? = null,

    @Column(length = 15)
    var genre: String? = null
)