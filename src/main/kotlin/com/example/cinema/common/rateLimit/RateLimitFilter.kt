package com.example.cinema.common.rateLimit

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Duration

@Component
class RateLimitFilter(
    private val rateLimitService: RateLimitService,
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val principal = SecurityContextHolder.getContext().authentication?.principal
        val userKey = principal?.toString() ?: request.remoteAddr

        val allowed = rateLimitService.tryAcquire(
            key = userKey,
            maxRequests = 60,
            window = Duration.ofMinutes(1),
        )

        if (!allowed) {
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            response.writer.write("""{"error":"Rate limit exceeded"}""")
            return
        }
        filterChain.doFilter(request, response)
    }

}