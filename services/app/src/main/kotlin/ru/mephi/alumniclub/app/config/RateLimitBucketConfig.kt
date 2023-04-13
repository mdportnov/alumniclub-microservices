package ru.mephi.alumniclub.app.config

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.Refill
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class RateLimitBucketConfig {
    /**
     * Every 15 seconds, 10 tokens are issued
     * to an unauthorized user based on his IP address.
     * Token bucket capacity 30.
     */
    @Bean("UnAuthorizedUserBucketBean")
    fun bucketConfigurationForUnAuthorizedUser(): BucketConfiguration {
        val refill: Refill = Refill.intervally(10, Duration.ofSeconds(10))
        val limit: Bandwidth = Bandwidth.classic(30, refill)
        return BucketConfiguration.builder().addLimit(limit).build()
    }

    /**
     * Every 1 minute, 50 tokens are issued
     * to an authorized user based on his userId.
     * Token bucket capacity 50.
     */
    @Bean("AuthorizedUserBucketBean")
    fun bucketConfigurationForAuthorizedUser(): BucketConfiguration {
        val refill: Refill = Refill.intervally(100, Duration.ofMinutes(1))
        val limit: Bandwidth = Bandwidth.classic(50, refill)
        return BucketConfiguration.builder().addLimit(limit).build()
    }

    /**
     * Every 1 minute, 100 tokens are issued
     * to an admin user based on his userId.
     * Token bucket capacity 100.
     */
    @Bean("AdminBucketBean")
    fun bucketConfigurationForAdmin(): BucketConfiguration {
        val refill: Refill = Refill.intervally(100, Duration.ofMinutes(1))
        val limit: Bandwidth = Bandwidth.classic(100, refill)
        return BucketConfiguration.builder().addLimit(limit).build()
    }

    /**
     * Every 100 millis, 10000 tokens are issued
     * to all users. Used for dev and test profiles.
     */
    @Bean("TestBucketBean")
    fun testBucket(): BucketConfiguration {
        val limit: Bandwidth = Bandwidth.simple(10000, Duration.ofMillis(100))
        return BucketConfiguration.builder().addLimit(limit).build()
    }
}