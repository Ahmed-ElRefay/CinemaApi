package com.example.cinema.cinema

import com.example.cinema.AbstractIntegrationTest
import com.example.cinema.cinemas.dto.CinemaResponse
import com.example.cinema.cinemas.dto.CreateCinemaRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody
import org.testcontainers.containers.PostgreSQLContainer

class CinemaApiTest @Autowired constructor(
    private val client: RestTestClient
): AbstractIntegrationTest() {

    @Test
    fun `creates a cinema and returns 201 with the cinema body` (){
        val request = CreateCinemaRequest(name = "Test Cinema 2" , city = "Test City 11")

        client.post()
            .uri("/api/cinemas")
            .body(request)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CinemaResponse::class.java)
            .value { response ->
                assertThat(response?.id).isNotNull
                assertThat(response?.name).isEqualTo("Test Cinema 2")
                assertThat(response?.city).isEqualTo("Test City 11")
            }
    }

    @Test
    fun `rejects creation with a blank name and returns 400`(){
        val request = CreateCinemaRequest(name = "" , city = "Test City")
        client.post()
        .uri("/api/cinemas")
            .body(request)
            .exchange()
            .expectStatus().isBadRequest
    }

  /*  @Test
    fun `find all cinemas` (){
        client.get()
            .uri("/api/cinemas")
            .exchange()
            .expectStatus().isOk
            .expectBody<List<CinemaResponse>>()
            .value { response ->
                println(response)
                //assertThat(response?.size).isEqualTo(1)
            }
    }*/
}