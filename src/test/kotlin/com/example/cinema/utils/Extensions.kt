package com.example.cinema.utils

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody

inline fun <reified T : Any> RestTestClient.RequestHeadersSpec<*>.exchangeAs(
    expectedStatus: HttpStatusCode = HttpStatus.OK,
): T = exchange()
    .expectStatus().isEqualTo(expectedStatus)
    .expectBody<T>()
    .returnResult()
    .responseBody!!