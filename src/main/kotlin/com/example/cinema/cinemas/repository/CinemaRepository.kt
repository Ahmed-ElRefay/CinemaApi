package com.example.cinema.cinemas.repository

import com.example.cinema.cinemas.entity.Cinema
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CinemaRepository : JpaRepository<Cinema, UUID>