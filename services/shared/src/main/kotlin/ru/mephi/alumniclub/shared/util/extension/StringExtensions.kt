package ru.mephi.alumniclub.shared.util.extension

fun String.containsAnyPath(vararg paths: String): Boolean {
    return paths.any { Regex("$it(\\W|\$)").containsMatchIn(this) }
}