package com.example.cinema.booking

import com.example.cinema.AbstractIntegrationTest
import com.example.cinema.booking.dto.CreateBookingRequest
import com.example.cinema.cinemas.dto.CinemaResponse
import com.example.cinema.cinemas.dto.CreateCinemaRequest
import com.example.cinema.hall.dto.CreateHallRequest
import com.example.cinema.hall.dto.CreateSeatsRequest
import com.example.cinema.hall.dto.HallResponse
import com.example.cinema.hall.dto.SeatRequest
import com.example.cinema.hall.dto.SeatResponse
import com.example.cinema.movie.dto.CreateMovieRequest
import com.example.cinema.movie.dto.MovieResponse
import com.example.cinema.showtime.dto.CreateShowtimeRequest
import com.example.cinema.showtime.dto.ShowtimeResponse
import com.example.cinema.showtime.dto.ShowtimeSeatsResponse
import com.example.cinema.user.dto.CreateUserRequest
import com.example.cinema.user.dto.UserResponse
import com.example.cinema.utils.exchangeAs
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.client.RestTestClient
import tools.jackson.databind.json.JsonMapper
import java.math.BigDecimal
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Callable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class BookingConcurrencyTest @Autowired constructor(
    private val client: RestTestClient,
    private val jsonMapper: JsonMapper,
    @LocalServerPort private val port: Int): AbstractIntegrationTest() {

        @Test
    fun `two concurrent bookings for the same seat - one wins, one gets 409`(){
        val cinema: CinemaResponse = client.post().uri("/api/cinemas")
            .body(CreateCinemaRequest("Cairo Cineplex", "Cairo"))
            .exchangeAs(HttpStatus.CREATED)

        val movie: MovieResponse = client.post().uri("/api/movies")
            .body(CreateMovieRequest("The Nun", 132, "horror"))
            .exchangeAs(HttpStatus.CREATED)

        val hall: HallResponse = client.post().uri("/api/halls")
            .body(CreateHallRequest(cinemaId = cinema.id, name = "Hall 1"))
            .exchangeAs(HttpStatus.CREATED)

        client.post().uri("/api/halls/{hallId}/seats", hall.id)
            .body(CreateSeatsRequest(listOf(SeatRequest("A", 1, "Standard"))))
            .exchangeAs<List<SeatResponse>>(HttpStatus.CREATED)

        val showtime: ShowtimeResponse = client.post().uri("/api/showtimes")
            .body(
                CreateShowtimeRequest(
                    hallId = hall.id,
                    movieId = movie.id,
                    startTime = Instant.now().plus(Duration.ofDays(1)),
                    basePrice = BigDecimal.TEN,
                )
            )
            .exchangeAs(HttpStatus.CREATED)

        val alice: UserResponse = client.post().uri("/api/users")
            .body(CreateUserRequest("Alice", "alice@test.com" , password = "password"))
            .exchangeAs(HttpStatus.CREATED)

        val bob: UserResponse = client.post().uri("/api/users")
            .body(CreateUserRequest("Bob", "bob@test.com", password = "password"))
            .exchangeAs(HttpStatus.CREATED)

        val showtimeSeats: List<ShowtimeSeatsResponse> = client.get()
            .uri("/api/showtimes/{showtimeId}/seats", showtime.id)
            .exchangeAs()

        val targetSeatId = showtimeSeats.single().id

        val aliceRequest = CreateBookingRequest( showtime.id, listOf(targetSeatId))
        val bobRequest   = CreateBookingRequest(  showtime.id, listOf(targetSeatId))

        val httpClient = HttpClient.newHttpClient()
        val baseUrl = "http://localhost:$port"

        val startLatch = CountDownLatch(1)
        val executor = Executors.newFixedThreadPool(2)

        val futures = listOf(aliceRequest, bobRequest).map { request ->
            executor.submit(Callable {
                startLatch.await()                       // wait for the signal
                val httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create("$baseUrl/api/bookings"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonMapper.writeValueAsString(request)))
                    .build()
                httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()).statusCode()
            })
        }

        // Give both threads time to reach the latch (await), then release them together
        Thread.sleep(100)
        startLatch.countDown()

        val statuses = futures.map { it.get(10, TimeUnit.SECONDS) }
        executor.shutdown()
        executor.awaitTermination(5, TimeUnit.SECONDS)

        // ----- The assertion -----
        assertThat(statuses).containsExactlyInAnyOrder(201, 409)
    }
}