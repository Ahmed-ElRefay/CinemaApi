package com.example.cinema

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CinemaApplication

fun main(args: Array<String>) {
	runApplication<CinemaApplication>(*args)
}
