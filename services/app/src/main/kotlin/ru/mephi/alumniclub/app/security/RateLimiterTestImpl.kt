package ru.mephi.alumniclub.app.security

import com.hazelcast.core.HazelcastInstance
import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.ConsumptionProbe
import io.github.bucket4j.grid.hazelcast.HazelcastProxyManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier

@Suppress("SpringJavaAutowiredMembersInspection")
class RateLimiterTestImpl(hzInstance: HazelcastInstance) : IRateLimiter {
    private var proxyManager: HazelcastProxyManager<String>

    init {
        proxyManager = HazelcastProxyManager(
            hzInstance.getMap("per-client-bucket-map")
        )
    }

    @Autowired
    @Qualifier("TestBucketBean")
    private lateinit var testBucketConfiguration: BucketConfiguration

    override fun tryAuthorizedAccess(key: String, isAdmin: Boolean): ConsumptionProbe {
        return proxyManager.builder()
            .build(key, testBucketConfiguration)
            .tryConsumeAndReturnRemaining(1)
    }

    override fun tryUnAuthorizedAccess(key: String): ConsumptionProbe {
        return proxyManager.builder().build(key, testBucketConfiguration)
            .tryConsumeAndReturnRemaining(1)
    }

    override fun trySwaggerAccess(): ConsumptionProbe {
        return ConsumptionProbe.consumed(1, 1)
    }
}