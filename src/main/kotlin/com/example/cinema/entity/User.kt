package com.example.cinema.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id
    var id: UUID = UUID.randomUUID(),
    @Column(nullable = false , unique = true , length = 150)
    var email: String,
    @Column(nullable = false , length = 100)
    var name: String,
)