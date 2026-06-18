package com.example.cinema.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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

    @Column(nullable = false , length = 255)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    var role: UserRole = UserRole.USER,
)