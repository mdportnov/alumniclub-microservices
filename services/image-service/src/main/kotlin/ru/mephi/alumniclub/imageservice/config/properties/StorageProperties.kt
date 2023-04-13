package ru.mephi.alumniclub.imageservice.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.util.unit.DataSize

@ConstructorBinding
@ConfigurationProperties(prefix = "storage.image")
class StorageProperties(
    val criticalSize: DataSize? = null,
    val maxSizeUser: DataSize? = null,
    val maxSizeAdmin: DataSize? = null,
    val maxStoredSize: DataSize? = null,
)