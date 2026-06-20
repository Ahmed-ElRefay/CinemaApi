package com.example.cinema.booking

import com.example.cinema.booking.dto.BookingResponse
import com.example.cinema.booking.dto.CreateBookingRequest
import com.example.cinema.booking.entity.Booking
import com.example.cinema.common.idempotency.IdempotencyService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService,
    private val idempotencyService: IdempotencyService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @Valid @RequestBody request: CreateBookingRequest,
        @AuthenticationPrincipal userId: UUID,
        @RequestHeader("Idempotency-Key", required = false) idempotencyKey: String?): BookingResponse {
        if (idempotencyKey == null) {
            return bookingService.create(request , userId)
        }
        return idempotencyService.getOrCompute(
            key = idempotencyKey,
            responseType = BookingResponse::class.java){
            bookingService.create(request , userId)
        }
    }

    @PostMapping("/{bookingId}/confirm")
    fun confirm(@PathVariable bookingId: UUID): ResponseEntity<BookingResponse> {
        return ResponseEntity.ok(bookingService.confirm(bookingId))
    }
}