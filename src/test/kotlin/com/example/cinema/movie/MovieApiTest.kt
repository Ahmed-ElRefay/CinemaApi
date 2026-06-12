package com.example.cinema.movie

import com.example.cinema.AbstractIntegrationTest
import com.example.cinema.movie.dto.CreateMovieRequest
import com.example.cinema.movie.dto.MovieResponse
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
import kotlin.test.Test

class MovieApiTest @Autowired constructor (
    private val client: RestTestClient
): AbstractIntegrationTest() {

    @Test
    fun `create movie and return 201 with body`(){
        val request = CreateMovieRequest(
            title = "The Nun",
            duration = 132,
            genre = "horror"
        )
        client.post()
        .uri("/api/movies")
            .body(request)
            .exchange()
        .expectStatus().isCreated
            .expectBody(MovieResponse::class.java)
            .value {
                assertThat(it?.id).isNotNull
                assertThat(it?.title).isEqualTo("The Nun")
                assertThat(it?.genre).isEqualTo("horror")
            }
    }

    @Test
    fun `should return all movies that matches the genre`(){
        val request = CreateMovieRequest(
            title = "The Nun",
            duration = 132,
            genre = "horror"
        )
        val requestTwo = CreateMovieRequest(
            title = "John Wick",
            duration = 168,
            genre = "Action"
        )
        client.post()
            .uri("/api/movies")
            .body(request)
            .exchange()

        client.post()
            .uri("/api/movies")
            .body(requestTwo)
            .exchange()

        client.get().uri { builder ->
            builder
                .path("/api/movies")
                .queryParam("genre" , "horror")
                .build()
        }
            .exchange()
            .expectStatus().isOk
            .expectBody<List<MovieResponse>>()
            .value {
                assertThat(it).isNotNull.isNotEmpty
                it?.forEach { movie ->
                    assertThat(movie.genre).isEqualTo("horror")
                }
            }

    }
}