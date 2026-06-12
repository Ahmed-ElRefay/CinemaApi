package com.example.cinema

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.jdbc.core.JdbcTemplate
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@AutoConfigureRestTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
abstract class AbstractIntegrationTest {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    companion object {

        @Container
        @JvmStatic
        @ServiceConnection
        val postgres = PostgreSQLContainer("postgres:16").apply {
            withDatabaseName("cinema_test")
                .withUsername("test")
                .withPassword("test")
        }
    }

    @BeforeEach
    fun cleanDatabase() {
        jdbcTemplate.execute("""
        TRUNCATE TABLE booking_seats, bookings, showtime_seats, showtimes,
                       seats, halls, movies, cinemas, users 
        RESTART IDENTITY CASCADE
    """.trimIndent())
    }
}


