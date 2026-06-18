package com.example.cinema.common.config

import com.example.cinema.common.security.JwtAuthenticationFilter
import com.example.cinema.user.repository.UserRepository
import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher

@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val errorMatcher = DispatcherTypeRequestMatcher(DispatcherType.ERROR)
        val asyncMatcher = DispatcherTypeRequestMatcher(DispatcherType.ASYNC)
        http{
            csrf { disable() }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }
            authorizeHttpRequests {
                authorize(errorMatcher, permitAll)
                authorize(asyncMatcher, permitAll)
                authorize("/api/auth/**", permitAll)
                authorize(HttpMethod.POST , "/api/users" , permitAll)
                authorize(anyRequest, authenticated)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
            exceptionHandling {
                authenticationEntryPoint = HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)

            }
            httpBasic { disable() }
            formLogin { disable() }
        }
        return http.build()
    }
}