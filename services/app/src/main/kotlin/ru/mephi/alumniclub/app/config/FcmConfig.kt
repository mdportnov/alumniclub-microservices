package ru.mephi.alumniclub.app.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import java.io.InputStream

@Configuration
class FcmConfig(
    private val resourceLoader: ResourceLoader
) {
    @Bean
    fun firebaseMessaging(): FirebaseMessaging {
        val resource: Resource = resourceLoader.getResource("classpath:firebase-service-account.json")
        val inputStream: InputStream = resource.inputStream
        val googleCredentials = GoogleCredentials.fromStream(inputStream)
        val firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build()

        val app = if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(firebaseOptions, "my-app")
        } else FirebaseApp.getApps()[0]

        return FirebaseMessaging.getInstance(app)
    }
}