package ru.mephi.alumniclub.app.util.version

const val VERSION_PARTS_NUMBER = 3

class ApiVersion {
    var MAJOR: Int = 0
    var MINOR: Int = 0
    var PATCH: Int = 0
    var EXTRA: String? = null

    constructor(fullVersion: String) {
        parse(fullVersion)
    }

    constructor(major: Int = 0, minor: Int = 0, patch: Int = 0, extra: String? = null) {
        this.MAJOR = major
        this.MINOR = minor
        this.PATCH = patch
        this.EXTRA = extra
    }

    override fun toString(): String {
        if (EXTRA == null) return "$MAJOR.$MINOR.$PATCH"
        return "$MAJOR.$MINOR.$PATCH-$EXTRA"
    }

    operator fun compareTo(version: ApiVersion): Int {
        if (this.MAJOR != version.MAJOR) return this.MAJOR - version.MAJOR
        if (this.MINOR != version.MINOR) return this.MINOR - version.MINOR
        return this.PATCH - version.PATCH
    }

    private fun parse(str: String) {
        try {
            val sub = str.split(".", limit = 3)
            if (sub.size != VERSION_PARTS_NUMBER) throw ApiVersionParseException(str)
            MAJOR = sub[0].toInt()
            MINOR = sub[1].toInt()
            val list = sub[2].split("-", limit = 2)
            PATCH = list[0].toInt()
            EXTRA = if (list.size == 2) list[1] else null
        } catch (e: NumberFormatException) {
            throw ApiVersionParseException(str)
        }
    }
}