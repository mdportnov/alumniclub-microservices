package ru.mephi.alumniclub.shared.util.extension

fun <T> Collection<T>.allUniqueValues(): Boolean {
    return this.size == this.toSet().size
}

fun <T> Collection<T>.hasDuplicates(): Boolean {
    return !allUniqueValues()
}