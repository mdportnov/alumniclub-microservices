package ru.mephi.alumniclub.app.util

fun parseArticle(content: String): String {
    return Regex("<p>.*?</p>")
        .find(content)?.value
        ?.replace(Regex("<.*?>"), "")
        ?.replace("&nbsp;", " ") ?: content
}
