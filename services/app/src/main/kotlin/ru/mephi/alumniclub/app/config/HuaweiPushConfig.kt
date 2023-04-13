package ru.mephi.alumniclub.app.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.mephi.push_sdk.messaging.HuaweiApp
import ru.mephi.push_sdk.messaging.HuaweiMessaging
import ru.mephi.push_sdk.util.InitAppUtils

@Configuration
class HuaweiPushConfig(
    @Value("\${push.huawei.appId}")
    private val appId: String,
    @Value("\${push.huawei.appSecret}")
    private val appSecret: String
) {
    @Bean
    fun createInitAppUtils(): HuaweiApp {
        return InitAppUtils.initializeApp(appId, appSecret)
    }

    @Bean
    fun createHuaweiMessaging(app: HuaweiApp): HuaweiMessaging {
        return HuaweiMessaging.getInstance(app)
    }
}