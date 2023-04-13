package ru.mephi.alumniclub.app.service.impl.storage

import org.apache.tika.Tika
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.config.properties.ServerUrlsProperties
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.app.service.ThumbnailatorService
import ru.mephi.alumniclub.app.util.getFilePathInfo
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.dto.photo.CompressTarget
import ru.mephi.alumniclub.shared.dto.photo.ThumbnailRequest
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1
import ru.mephi.alumniclub.shared.util.extension.createIfNotExist
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import ru.mephi.alumniclub.shared.util.toMediumFileName
import ru.mephi.alumniclub.shared.util.toSmallFileName
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.util.*
import javax.servlet.http.Part
import javax.transaction.Transactional
import kotlin.io.path.*


@Service
class StorageManagerImpl(
    private val urls: ServerUrlsProperties,
    private val thumbnailService: ThumbnailatorService,
) : ResponseManager(), StorageManager {
    override val root = Path("uploads").also { if (it.notExists()) Files.createDirectory(it) }

    /**
     * Saves an image to the file system.
     * - First checks the MIME type of the Part data.
     * Generates a random filename using a UUID and the extension.
     * - The function creates the store directory if it doesn't exist and
     * saves the image to the directory using Files.copy.
     * - If the oldPath is not null, the function removes the old
     * image using the removeImage method.
     * - The function then sends a request to the thumbnailService to generate
     * a thumbnail for the new image. The compressionTarget is set to [CompressTarget.USER]
     * if the dir is [StoreDir.USER], otherwise it's set to [CompressTarget.CONTENT].
     *
     * @param part the image part
     * @param dir the store directory
     * @param oldPath the old path, can be null
     * @param isAdmin flag indicating whether the user is an admin or not
     * @return the new path of the saved image
     */
    @Transactional
    override fun saveImage(part: Part, dir: StoreDir, oldPath: String?, isAdmin: Boolean): String {
        val extension = checkMime(part.inputStream)
        val filename = "${UUID.randomUUID()}.$extension"

        val path = Path(root.pathString + dir.path).createIfNotExist()
        val newPath: String
        path.resolve(filename).also {
            Files.copy(part.inputStream, it)
            newPath = "${it.parent.name}/${it.name}"
        }

        if (oldPath != null) {
            removeImage(oldPath, dir)
        }

        val compressionTarget = if (dir == StoreDir.USER) CompressTarget.USER else CompressTarget.CONTENT
        thumbnailService.sendToQueue(ThumbnailRequest(newPath, compressionTarget))

        return newPath
    }

    /**
     * Returns the photo file based on the given path.
     * - Checks if the path is correct using the [isPathCorrect] method.
     * If the path is incorrect, it throws an [HttpStatus.BAD_REQUEST].
     * - The function then parses the path using the [getFilePathInfo] method to
     * get the directory name, file name, quality, and extension.
     * - The function checks for the existence of the photo file with the desired quality,
     * and if it doesn't exist, it checks for photo files with other qualities (medium, large, small)
     * in the order specified. If none of the photo files exist, the function returns null.
     *
     * @param path the path of the photo file
     * @return the photo file, or `null` if the file doesn't exist
     * @throws ApiError with a `BAD_REQUEST` status if the path is incorrect
     */
    override fun getPhotoFile(path: String): File? {
        if (!isPathCorrect(path)) throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.common.badRequest"))

        val (dir, name, quality, extension) = path.getFilePathInfo()

        for (fileName in getImageThumbnailsNames(dir, name, extension, quality)) {
            val file = File(fileName)
            if (file.exists()) return file
        }
        return null
    }

    /**
     * Builds all possible qualities of entered file. Returns list with their names
     */

    private fun getImageThumbnailsNames(dir: String, name: String, extension: String, quality: String): List<String> {
        val mainFile = "${root.name}/${dir}/$name${quality}.${extension}"
        val mediumFile = "${root.name}/${dir}/$name.medium.$extension"
        val largeFile = "${root.name}/${dir}/$name.$extension"
        val smallFile = "${root.name}/${dir}/$name.small.$extension"
        return listOf(mainFile, mediumFile, largeFile, smallFile)
    }

    /**
     * Check cases when path contains:
     * - more than 2 dots: users/uuid.medium..jpg
     * - 2 dots in a row users/uuid..jpg
     */
    fun isPathCorrect(path: String): Boolean {
        if (path.count { it == '.' } == 0 || path.count { it == '.' } > 2 ||
            (path.count { it == '.' } == 2 && path.contains(".."))) return false
        return true
    }

    override fun getPhotoPath(path: String): String = "${urls.fullBaseUrl}/api$API_VERSION_1/public/uploads/$path"

    /**
     * Removes all sizes of provided image
     */
    override fun removeImage(path: String?, dir: StoreDir): Boolean {
        if (path == null) return false
        val largeFile = File("${root.name}/$path")
        val mediumFile = File(largeFile.toMediumFileName())
        val smallFile = File(largeFile.toSmallFileName())
        return root.resolve(largeFile.absolutePath).deleteIfExists() && root.resolve(mediumFile.absolutePath)
            .deleteIfExists() && root.resolve(smallFile.absolutePath).deleteIfExists()
    }

    override fun checkAllowedExtension(file: File): MediaType {
        return when (file.extension) {
            "png" -> MediaType.IMAGE_PNG
            "jpg", "jpeg" -> MediaType.IMAGE_JPEG
            "gif" -> MediaType.IMAGE_GIF
            else -> throw ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, i18n("exception.media.invalidFile"))
        }
    }

    private fun checkMime(stream: InputStream): String {
        val (mime, ext) = Tika().detect(stream).split("/")
        if (mime != "image")
            throw ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, i18n("exception.media.invalidFile"))
        return ext
    }
}
