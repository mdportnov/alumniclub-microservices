package ru.mephi.alumniclub.shared.util

import org.springframework.stereotype.Component

@Component
@Deprecated("Change to UUID")
class StringGenerator {
    fun getRandomString(len: Int): String {
        val array = CharArray(len)
        val smbs = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        for (i in 0 until len) {
            array[i] = smbs.random()
        }
        return array.joinToString("", "", "")
    }
}