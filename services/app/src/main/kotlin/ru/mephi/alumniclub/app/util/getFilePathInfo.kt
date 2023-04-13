package ru.mephi.alumniclub.app.util

fun String.getFilePathInfo(): ImageFilePathInfo {
    val parts = this.split("/")
    val dir = parts[0] // ex: users
    val fileFullName = parts[1] // ex: uuid.medium.ext or uuid.ext
    val fileName = fileFullName.split(".")[0]
    val fileNameParts = fileFullName.split(".")
    val fileExtension = fileNameParts.last()

    val quality = if (fileNameParts.size >= 3 && fileNameParts[1].isNotEmpty()) {
        ".${fileNameParts[1]}"
    } else ""

    return ImageFilePathInfo(
        dir = dir, name = fileName, quality = quality, extension = fileExtension
    )
}