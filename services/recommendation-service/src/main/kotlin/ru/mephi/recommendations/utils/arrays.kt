package ru.mephi.recommendations.utils

import java.lang.Float.floatToIntBits
import java.nio.ByteBuffer

fun Array<Float>.toByteArray(): Array<Byte> {
    val array = Array<Byte>(this.size * 4) { 0 }
    this.forEachIndexed { index, it ->
        val intBits = floatToIntBits(it)
        array[index * 4] = (intBits shr 24).toByte()
        array[index * 4 + 1] = (intBits shr 16).toByte()
        array[index * 4 + 2] = (intBits shr 8).toByte()
        array[index * 4 + 3] = intBits.toByte()
    }
    return array
}

fun Array<Byte>.toFloatArray(): Array<Float> {
    val array = Array<Float>(this.size / 4) { 0f }
    for (i in 0 until (this.size / 4)) {
        array[i] =
            ByteBuffer.wrap(listOf(this[i * 4], this[i * 4 + 1], this[i * 4 + 2], this[i * 4 + 3]).toByteArray()).float
    }
    return array
}