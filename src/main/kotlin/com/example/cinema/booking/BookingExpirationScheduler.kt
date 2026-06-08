package com.example.cinema.booking

import com.example.cinema.entity.BookingStatus
import com.example.cinema.repository.BookingRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class BookingExpirationScheduler(
    private val bookingRepository: BookingRepository,
    private val bookingExpirationService: BookingExpirationService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(fixedDelay = 60_000)
    fun sweep(){

        val expiredIds = bookingRepository
            .findByStatusAndHoldExpiresAtBefore(BookingStatus.HELD, Instant.now())
            .map { it.id }


        if (expiredIds.isEmpty()) {
            return
        }

        log.info("Sweeping ${expiredIds.size} expired bookings")

        for (id in expiredIds) {
            try {
                bookingExpirationService.expireBooking(id)
            }catch (e: Exception){
                log.warn("Expiring $id failed", e)
            }
        }

    }

}