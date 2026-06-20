package com.example.cinema.common.idempotency

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import tools.jackson.databind.json.JsonMapper
import java.time.Duration

@Service
class IdempotencyService(
    private val redis: StringRedisTemplate,
    private val jsonMapper: JsonMapper
) {
    private val ttl = Duration.ofHours(24)

    fun <T: Any> getOrCompute(
        key: String,
        responseType: Class<T>,
        compute: () -> T
    ) : T{
        val stored = redis.opsForValue().get("idempotency:$key")
        if (stored != null) {
            return jsonMapper.readValue(stored, responseType)
        }
        val result = compute()
        redis.opsForValue().set("idempotency:$key" , jsonMapper.writeValueAsString(result) , ttl)
        return result
    }
}