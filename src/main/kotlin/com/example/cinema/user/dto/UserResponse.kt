package com.example.cinema.user.dto

import com.example.cinema.user.entity.User
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val name: String,
    val email: String,
){
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id,
                user.name,
                user.email,
            )
        }
    }
}
