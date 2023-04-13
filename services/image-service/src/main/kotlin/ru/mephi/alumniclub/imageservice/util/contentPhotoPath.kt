package ru.mephi.alumniclub.imageservice.util

fun String.parseContentPhotoPaths(): MutableList<String> {
    val regex = Regex("img .*?src=\"(.*?)\".*?>")
    return regex.findAll(this).map { it.groupValues[1] }.toMutableList()
}
