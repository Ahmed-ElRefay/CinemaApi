package com.example.cinema.common.config

import com.example.cinema.common.rateLimit.RateLimitFilter
import com.example.cinema.common.security.JwtAuthenticationFilter
import com.example.cinema.user.repository.UserRepository
import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.DispatcherTypeRequestMatcher
import tools.jackson.databind.json.JsonMapper

@Configuration
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val rateLimitFilter: RateLimitFilter,
    private val jsonMapper: JsonMapper
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
                authorize("/actuator/health", permitAll)
                authorize("/actuator/**", hasRole("ADMIN"))
                authorize(anyRequest, authenticated)
            }
            addFilterBefore<UsernamePasswordAuthenticationFilter>(jwtAuthenticationFilter)
            addFilterAfter<JwtAuthenticationFilter>(rateLimitFilter)
            exceptionHandling {
                authenticationEntryPoint = problemDetailEntryPoint()
                accessDeniedHandler = problemDetailAccessDeniedHandler()
            }
            httpBasic { disable() }
            formLogin { disable() }
        }
        return http.build()
    }

    private fun problemDetailEntryPoint() = AuthenticationEntryPoint { _, response, _ ->
        val problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED).apply {
            title = "Unauthorized"
            detail = "Authentication required"
        }
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/problem+json"
        response.writer.write(jsonMapper.writeValueAsString(problem))
    }

    private fun problemDetailAccessDeniedHandler() = AccessDeniedHandler { _, response, _ ->
        val problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN).apply {
            title = "Forbidden"
            detail = "You don't have permission to access this resource"
        }
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = "application/problem+json"
        response.writer.write(jsonMapper.writeValueAsString(problem))
    }

}