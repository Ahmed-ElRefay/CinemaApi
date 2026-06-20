package com.example.cinema.booking

import com.example.cinema.booking.entity.BookingStatus
import com.example.cinema.booking.repository.BookingRepository
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
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
    @SchedulerLock(name = "expireBookings" , lockAtLeastFor = "30s" , lockAtMostFor = "2m")
    fun sweep(){

        val expired = bookingRepository
            .findByStatusAndHoldExpiresAtBefore(BookingStatus.HELD, Instant.now())


        if (expired.isEmpty()) {
            return
        }



        log.info("Sweeping ${expired.size} expired bookings")
        for (booking in expired) {
            try {
                bookingExpirationService.expireBooking(booking.id , booking.showtime.id)
            }catch (e: Exception){
                log.warn("Expiring ${booking.id} failed", e)
            }
        }

    }

}