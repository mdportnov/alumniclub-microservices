package ru.mephi.alumniclub.shared.util.extension

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.notExists

fun Path.createIfNotExist(): Path {
    if (this.notExists())
        Files.createDirectory(this)
    return this
}