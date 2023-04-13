package ru.mephi.alumniclub.app.database.repository

import org.springframework.data.redis.core.StringRedisTemplate
import java.io.*
import java.util.*

abstract class RedisRepository<V : Serializable>(
    private val redis: StringRedisTemplate
) {

    fun save(key: String, value: V): V {
        redis.opsForValue().set(key, value.toByteArrayString())
        return value
    }

    fun findByKey(key: String): Optional<V> {
        val bytes: String? = redis.opsForValue().get(key)
        return if (bytes == null) Optional.ofNullable(null)
        else Optional.of(fromByteArrayString(bytes))
    }

    private fun Serializable.toByteArrayString(): String {
        ByteArrayOutputStream().use { bos ->
            ObjectOutputStream(bos).use { out ->
                out.writeObject(this)
                val byteArray = bos.toByteArray()
                return Base64.getEncoder().encodeToString(byteArray)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun fromByteArrayString(string: String): V {
        val bytes = Base64.getDecoder().decode(string)
        ByteArrayInputStream(bytes).use { bis ->
            ObjectInputStream(bis).use { obs ->
                return obs.readObject() as V
            }
        }
    }
}