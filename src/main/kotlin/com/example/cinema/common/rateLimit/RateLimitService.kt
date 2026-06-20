package com.example.cinema.common.rateLimit

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RateLimitService(
    private val redis: StringRedisTemplate
) {
    fun tryAcquire(
        key: String,
        maxRequests: Int,
        window: Duration
    ) : Boolean {
        val redisKey = "ratelimit:$key"
        val current = redis.opsForValue().increment(redisKey) ?: 0
        if (current == 1L){
            redis.expire(redisKey, window)
        }
        return current <= maxRequests
    }
}