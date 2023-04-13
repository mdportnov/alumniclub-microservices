package ru.mephi.recommendations.model.util

import kotlin.math.sqrt

class Vector(
    val coordinates: Array<Float>,
) {
    operator fun plus(other: Vector): Vector {
        return Vector(Array(this.size) { this.coordinates[it] + other.coordinates[it] })
    }

    operator fun minus(other: Vector): Vector {
        return Vector(Array(this.size) { this.coordinates[it] - other.coordinates[it] })
    }

    operator fun plusAssign(other: Vector) {
        repeat(this.size) { this.coordinates[it] += other.coordinates[it] }
    }

    operator fun minusAssign(other: Vector) {
        repeat(this.size) { this.coordinates[it] += other.coordinates[it] }
    }

    operator fun times(n: Int): Vector {
        return Vector(Array(this.size) { this.coordinates[it] * n })
    }

    operator fun timesAssign(n: Int) {
        repeat(this.size) { this.coordinates[it] = this.coordinates[it] * n }
    }

    operator fun div(n: Int): Vector {
        return Vector(Array(this.size) { this.coordinates[it] / n })
    }

    operator fun divAssign(n: Int) {
        repeat(this.size) { this.coordinates[it] = this.coordinates[it] / n }
    }

    fun lenght(): Double {
        var len = 0.0
        for (xi in this.coordinates) {
            len += xi * xi
        }
        return sqrt(len)
    }

    fun normalize() {
        val len = lenght()
        for (i in coordinates.indices) {
            coordinates[i] = (coordinates[i] / len).toFloat()
        }
    }

    private val size: Int
        get() = coordinates.size

}