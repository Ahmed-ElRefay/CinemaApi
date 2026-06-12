package com.example.cinema.booking

import com.example.cinema.AbstractIntegrationTest
import com.example.cinema.booking.dto.BookingResponse
import com.example.cinema.booking.dto.CreateBookingRequest
import com.example.cinema.booking.entity.BookingStatus
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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant

class BookingApiTest @Autowired constructor(
    private val client: RestTestClient,
) : AbstractIntegrationTest() {

    @Test
    fun `create everything for booking and return 201 with body`() {
        val cinema: CinemaResponse = client.post().uri("/api/cinemas")
            .body(CreateCinemaRequest(name = "Ci", city = "Cairo"))
            .exchangeAs(HttpStatus.CREATED)

        val movie: MovieResponse = client.post().uri("/api/movies")
            .body(CreateMovieRequest(title = "The Nun", duration = 132, genre = "horror"))
            .exchangeAs(HttpStatus.CREATED)

        val hall: HallResponse = client.post().uri("/api/halls")
            .body(CreateHallRequest(cinemaId = cinema.id, name = "LOLO"))
            .exchangeAs(HttpStatus.CREATED)

        val seats: List<SeatResponse> = client.post().uri("/api/halls/{hallId}/seats", hall.id)
            .body(CreateSeatsRequest(seats = listOf(SeatRequest("A1", 1, "Standard"))))
            .exchangeAs(HttpStatus.CREATED)

        val showtime: ShowtimeResponse = client.post().uri("/api/showtimes")
            .body(CreateShowtimeRequest(
                hallId = hall.id,
                movieId = movie.id,
                startTime = Instant.now().plus(Duration.ofDays(1)),
                basePrice = BigDecimal.TEN,
            ))
            .exchangeAs(HttpStatus.CREATED)

        val user: UserResponse = client.post().uri("/api/users")
            .body(CreateUserRequest(name = "Ahmed", email = "ahmed.elref94@gmail.com"))
            .exchangeAs(HttpStatus.CREATED)

        val showtimeSeats: List<ShowtimeSeatsResponse> = client.get()
            .uri("/api/showtimes/{showtimeId}/seats", showtime.id)
            .exchangeAs()  // defaults to HttpStatus.OK

// finally, the actual subject of the test:
        client.post().uri("/api/bookings")
            .body(CreateBookingRequest(
                userId = user.id,
                showtimeId = showtime.id,
                seatIds = showtimeSeats.map { it.id },
            ))
            .exchange()
            .expectStatus().isCreated
            .expectBody(BookingResponse::class.java)
            .value {
                assertThat(it!!.seats).hasSize(showtimeSeats.size)
                assertThat(it.totalPrice).isEqualTo(showtimeSeats.sumOf { s -> s.price })
                assertThat(it.status).isEqualTo(BookingStatus.HELD)
            }
       /* val cinemaRequest = CreateCinemaRequest(name = "Ci", city = "Cairo")

        val cinema = client.post()
            .uri("/api/cinemas")
            .body(cinemaRequest)
            //extension func for cleaner usage.
            .exchangeAs<CinemaResponse>(HttpStatus.CREATED)


        val movieRequest = CreateMovieRequest(
            title = "The Nun",
            duration = 132,
            genre = "horror"
        )
        val movie = client.post()
            .uri("/api/movies")
            .body(movieRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(MovieResponse::class.java)
            .returnResult().responseBody

        val hallRequest = CreateHallRequest(
            cinemaId = cinema.id,
            name = "LOLO"
        )

        val hall = client.post()
            .uri("/api/halls")
            .body(hallRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(HallResponse::class.java)
            .returnResult().responseBody

        val seatRequest = SeatRequest(
            rowLabel = "A1",
            seatNumber = 1,
            seatType = "Standard"
        )
        val seatsRequest = CreateSeatsRequest(
            seats = listOf(seatRequest)
        )

        val seats = client.post()
            .uri("/api/halls/{hallId}/seats", hall?.id)
            .body(seatsRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody<List<SeatResponse>>()
            .returnResult().responseBody

        val showtimeRequest = CreateShowtimeRequest(
            hallId = hall?.id!!,
            movieId = movie?.id!!,
            startTime = Instant.now().plus(Duration.ofDays(1)),
            basePrice = BigDecimal.TEN
        )

        val showtime = client.post()
            .uri("/api/showtimes")
            .body(showtimeRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(ShowtimeResponse::class.java)
            .returnResult().responseBody

        val userRequest = CreateUserRequest(
            name = "Ahmed",
            email = "ahmed.elref94@gmail.com"
        )
        val user = client.post()
            .uri("/api/users")
            .body(userRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserResponse::class.java)
            .returnResult().responseBody

        val showtimeSeats =
            client.get()
                .uri("/api/showtimes/{showtimeId}/seats", showtime?.id)
                .exchange()
                .expectStatus().isOk
                .expectBody<List<ShowtimeSeatsResponse>>()
                .returnResult().responseBody


        val bookingRequest = CreateBookingRequest(
            userId = user?.id!!,
            showtimeId = showtime?.id!!,
            seatIds = showtimeSeats?.map { it.id }!!
        )

        client.post()
            .uri("/api/bookings")
            .body(bookingRequest)
            .exchange()
            .expectStatus().isCreated
            .expectBody(BookingResponse::class.java)
            .value {
                assertThat(it?.seats).hasSize(showtimeSeats.size)
                assertThat(it?.totalPrice).isEqualTo(showtimeSeats.sumOf { seat -> seat.price })
                assertThat(it?.status).isEqualTo(BookingStatus.HELD)
            }*/
    }

}