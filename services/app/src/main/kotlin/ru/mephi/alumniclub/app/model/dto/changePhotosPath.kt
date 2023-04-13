package ru.mephi.alumniclub.app.model.dto

import ru.mephi.alumniclub.app.model.dto.user.response.UserPreviewResponse
import ru.mephi.alumniclub.app.model.dto.user.response.UserResponse
import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed

fun getPhotoPath(photoPath: String, compressionType: CompressionLevel): String? {
    val (filePath, fileName) = photoPath.split("/", limit = 2)
    val (name, extension) = fileName.split(".").takeIf { it.size >= 2 } ?: return null
    return "$filePath/${name}.${compressionType.suffix}.$extension"
}

fun <T> changePhotosPath(content: List<T>): List<T> {
    if (content.isEmpty() || content.first() !is PhotoPathed) return content

    val compressionType = when (content[0]!!::class.java) {
        UserResponse::class.java, UserShortResponse::class.java,
        UserPreviewResponse::class.java -> CompressionLevel.SMALL

        else -> CompressionLevel.MEDIUM
    }

    for (i in content.indices) {
        val element = content[i] as PhotoPathed
        if (element.photoPath == null) continue
        element.photoPath = getPhotoPath(element.photoPath!!, compressionType)
    }

    return content
}

enum class CompressionLevel(val suffix: String) {
    NO(""), SMALL("small"), MEDIUM("medium")
}