package ru.mephi.alumniclub.imageservice.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.util.unit.DataSize
import ru.mephi.alumniclub.imageservice.config.properties.StorageProperties
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.extension.createIfNotExist
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.notExists
import kotlin.io.path.pathString

@Component
class ImageCompressorScheduler(
    private val logger: AlumniLogger, private val properties: StorageProperties
) {
    val root = Path("uploads").also { if (it.notExists()) Files.createDirectory(it) }

    @Scheduled(fixedDelay = 1 * 24 * 3600 * 1000L) // 1 day
    fun compression() {
        logInfo("Image processing Scheduler launched")
        try {
            processAllStoreDirs { folder ->
                folder.listFiles()?.forEach { file ->
                    processFile(file)
                }
            }
        } catch (e: Exception) {
            logError("Error while processing files", e)
        } finally {
            logInfo("Image Processing Scheduler stopped")
        }
    }

    private fun processAllStoreDirs(action: (folder: File) -> Unit) {
        StoreDir.values().forEach { dir ->
            val folder = getFolder(dir)
            if (folder.isDirectory) {
                action(folder)
            }
        }
    }

    private fun getFolder(dir: StoreDir): File {
        val path = Path(root.pathString + dir.path).createIfNotExist()
        return path.toFile()
    }

    private fun processFile(file: File) {
        try {
            compressionIfNeed(file)
        } catch (e: Exception) {
            logError("Error while processing file $file", e)
        }
    }

    private fun logInfo(message: String) = logger.info("SCHEDULER: $message")

    private fun logError(message: String, e: Exception) = logger.error("SCHEDULER: $message", e)

    private fun compression(image: BufferedImage, file: File, extension: String, scale: Double) {
        val width = (image.width * scale).toInt() // 1000
        val height = (image.height * scale).toInt() // 1000 * image.height / image.width
        val resultingImage: Image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT)
        val outputImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        outputImage.graphics.drawImage(resultingImage, 0, 0, null)
        ImageIO.write(outputImage, extension, file)
    }

    /**
     * No need if size less than 1MB or properties.maxStoredSize
     */
    private fun compressionIfNeed(file: File) {
        val criticalSize = properties.maxStoredSize?.toBytes() ?: DataSize.ofMegabytes(1).toBytes()
        if (Files.size(file.toPath()) <= criticalSize) return
        val image = ImageIO.read(file)
        val extension = file.extension
        val scale = 0.7
        compression(image, file, extension, scale)
    }
}