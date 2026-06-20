package com.example.cinema.common.exception

import com.example.cinema.common.ErrorResponse
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ObjectOptimisticLockingFailureException::class)
    fun handleOptimisticLock(): ProblemDetail {
        val problem = ProblemDetail.forStatus(HttpStatus.CONFLICT)
        problem.title = "Concurrent modification"
        problem.detail = "The resource was modified concurrently. Please retry."
        return problem
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException): ProblemDetail {
        val sqlState = (ex.cause as? java.sql.SQLException)?.sqlState
        return when (sqlState) {
            "23505" -> ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
                title = "Resource already exists"
                detail = "A resource with the provided unique value already exists"
            }
            "23503" -> ProblemDetail.forStatus(HttpStatus.CONFLICT).apply {
                title = "Referenced resource missing"
                detail = "The request references a resource that no longer exists"
            }
            "23502" -> ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
                title = "Missing required field"
            }
            "23514" -> ProblemDetail.forStatus(HttpStatus.BAD_REQUEST).apply {
                title = "Field constraint violated"
            }
            else -> ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR).apply {
                title = "Database error"
            }
        }
    }

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException) : ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ErrorResponse(
                status = HttpStatus.NOT_FOUND.value(),
                error = "Not found",
                message = ex.message
            )
        )
    }

    @ExceptionHandler(UnauthorizedException::class)
    fun handleUnauthorizedException(ex: UnauthorizedException) : ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponse(
                status = HttpStatus.UNAUTHORIZED.value(),
                error = "Unauthorized",
                message = ex.message
            )
        )
    }
}