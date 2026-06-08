package com.example.cinema.hall.dto

import com.example.cinema.hall.entity.Hall
import java.util.UUID

data class HallResponse(
    val id: UUID,
    val name: String,
){
    companion object {
        fun from(hall: Hall): HallResponse {
            return HallResponse(
                id = hall.id,
                name = hall.name,
            )
        }
    }
}
