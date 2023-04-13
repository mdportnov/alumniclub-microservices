package ru.mephi.alumniclub.app.database.repository

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.registration_limiter.RegistrationLimiterSettings

@Service
class RegistrationLimiterDao(
    redis: StringRedisTemplate
) : RedisRepository<RegistrationLimiterSettings>(redis)