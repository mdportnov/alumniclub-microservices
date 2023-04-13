package ru.mephi.alumniclub.shared.dto.photo

data class ThumbnailRequest(
    val path: String,
    val target: CompressTarget = CompressTarget.CONTENT
)