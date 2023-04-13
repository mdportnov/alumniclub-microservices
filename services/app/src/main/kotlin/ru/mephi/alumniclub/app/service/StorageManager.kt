package ru.mephi.alumniclub.app.service

import org.springframework.http.MediaType
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import java.io.File
import java.nio.file.Path
import javax.servlet.http.Part

interface StorageManager {
    val root: Path
    fun saveImage(part: Part, dir: StoreDir, oldPath: String? = null, isAdmin: Boolean = false): String
    fun getPhotoFile(path: String): File?
    fun getPhotoPath(path: String): String
    fun removeImage(path: String?, dir: StoreDir): Boolean
    fun checkAllowedExtension(file: File): MediaType
}