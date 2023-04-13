package ru.mephi.alumniclub.shared.util

import java.io.File

fun File.toMediumFileName() =
    "${parentFile.absolutePath}/${nameWithoutExtension}.medium.${extension}"

fun File.toSmallFileName() =
    "${parentFile.absolutePath}/${nameWithoutExtension}.small.${extension}"

