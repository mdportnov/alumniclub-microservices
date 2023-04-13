package ru.mephi.recommendations.utils


/**
 * HTML document parser. Gets from it only text for reading
 *
 * @param content The full HTML document text
 * @return [String] The result of parsing
 */

fun parseAllText(content: String): String {
    return "<p>.*?<\\/p>".toRegex()
        .findAll(content)
        .map { it.value.replace("<.*?>".toRegex(), " ") }
        .toList().joinToString(separator = " ")
}
