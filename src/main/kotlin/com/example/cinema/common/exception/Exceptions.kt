package com.example.cinema.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.UUID


@ResponseStatus(HttpStatus.NOT_FOUND)
open class NotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.CONFLICT)
open class ConflictException(message: String) : RuntimeException(message)


@ResponseStatus(HttpStatus.UNAUTHORIZED)
open class UnauthorizedException(message: String) : RuntimeException(message)

class BookingNotFoundException(id: UUID) :
    NotFoundException("Booking $id not found")


class InvalidCredentialsException() :
    UnauthorizedException("Invalid credentials")