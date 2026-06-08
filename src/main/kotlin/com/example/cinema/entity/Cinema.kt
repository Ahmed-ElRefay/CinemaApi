package com.example.cinema.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "cinemas")
class Cinema (
    @Id
    var id: UUID = UUID.randomUUID(),

    @Column(nullable = false , length = 50)
    var name: String,

    @Column(length = 50)
    var city: String? = null,
)