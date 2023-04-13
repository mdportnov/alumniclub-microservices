package ru.mephi.alumniclub.app.security

import com.hazelcast.core.HazelcastInstance
import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.ConsumptionProbe
import io.github.bucket4j.grid.hazelcast.HazelcastProxyManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier


@Suppress("SpringJavaAutowiredMembersInspection")
class RateLimiterProdImpl(hzInstance: HazelcastInstance) : IRateLimiter {
    private var proxyManager: HazelcastProxyManager<String>

    init {
        proxyManager = HazelcastProxyManager(
            hzInstance.getMap("per-client-bucket-map")
        )
    }

    @Autowired
    @Qualifier("UnAuthorizedUserBucketBean")
    private lateinit var unauthorizedBucketConfiguration: BucketConfiguration

    @Autowired
    @Qualifier("AuthorizedUserBucketBean")
    private lateinit var userBucketConfiguration: BucketConfiguration

    @Autowired
    @Qualifier("AdminBucketBean")
    private lateinit var adminBucketConfiguration: BucketConfiguration

    override fun tryAuthorizedAccess(key: String, isAdmin: Boolean): ConsumptionProbe {
        return proxyManager.builder()
            .build(key, if (isAdmin) adminBucketConfiguration else userBucketConfiguration)
            .tryConsumeAndReturnRemaining(1)
    }

    override fun tryUnAuthorizedAccess(key: String): ConsumptionProbe {
        return proxyManager.builder().build(key, unauthorizedBucketConfiguration)
            .tryConsumeAndReturnRemaining(1)
    }

    override fun trySwaggerAccess(): ConsumptionProbe {
        return ConsumptionProbe.consumed(1, 1)
    }
}