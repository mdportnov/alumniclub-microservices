package ru.mephi.alumniclub.imageservice.service.impl

import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.name
import kotlin.io.path.notExists

@Service
class StorageFileManager {
    private val root = Path("uploads").also { if (it.notExists()) Files.createDirectory(it) }

    fun getShortImagePath(fullPhotoPath: String): String {
        return try {
            fullPhotoPath.split("uploads/")[1]
        } catch (e: Exception) {
            throw Exception("Invalid photo path [$fullPhotoPath]")
        }
    }

    fun retrieveImage(path: String) = File("${root.name}/$path")
}