package com.example.cinema.repository

import com.example.cinema.entity.Cinema
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CinemaRepository : JpaRepository<Cinema , UUID>