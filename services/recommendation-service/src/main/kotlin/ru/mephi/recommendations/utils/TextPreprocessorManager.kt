package ru.mephi.recommendations.utils

import org.springframework.stereotype.Component

@Component
class TextPreprocessorManager {
    private val filterRegex = "[!?.,<>{}=%:;#№@\"'\$^+*/`~]".toRegex()

    /**
     * Simple text preprocessor. Gets the map of String - Int. Indicates how many times each word occurs in the text
     *
     * @param content The text corpus for handle
     * @return [Map] The result of text handle
     */
    fun getWordsMap(content: String): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        content.replace(filterRegex, " ").split(" ")
            .filter { it.isNotEmpty() }
            .map { it.handle() }
            .forEach { map[it] = map.getOrDefault(it, 0) + 1 }
        return map
    }

    /**
     * Word handler. Removes and replaces stop-symbols
     *
     * @return [String] The result of text handling
     */
    private fun String.handle(): String {
        var str = this.replace(filterRegex, "").trim()
        if (str.startsWith("-")) str = str.removeRange(0..0)
        if (str.endsWith("-")) str = str.removeRange((str.length - 1) until str.length)
        return str.trim().lowercase().replace("ё", "е")
    }
}


