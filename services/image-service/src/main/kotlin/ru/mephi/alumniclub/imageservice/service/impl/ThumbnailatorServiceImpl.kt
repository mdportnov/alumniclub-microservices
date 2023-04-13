package ru.mephi.alumniclub.imageservice.service.impl

import net.coobird.thumbnailator.Thumbnails
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.imageservice.service.ThumbnailatorService
import ru.mephi.alumniclub.imageservice.util.Sizes
import ru.mephi.alumniclub.shared.dto.photo.CompressTarget
import ru.mephi.alumniclub.shared.dto.photo.ThumbnailRequest
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import ru.mephi.alumniclub.shared.util.toMediumFileName
import ru.mephi.alumniclub.shared.util.toSmallFileName
import java.io.File

@Service
class ThumbnailatorServiceImpl(
    private val fileManager: StorageFileManager,
    private val logger: AlumniLogger
) : ThumbnailatorService {

    private fun logInfo(message: String) = logger.info("THUMBNAILATOR: $message")

    private fun logError(message: String, e: Exception) = logger.error("THUMBNAILATOR: $message", e)

    /**
     * Handles the creation of image thumbnails for a [ThumbnailRequest].
     * - Retrieves the base image from the file manager using the path in [ThumbnailRequest].
     * - Logs that the image is being compressed and its file path.
     * - Checks if the base image exists, returns if it doesn't exist.
     * - Compresses the base image to a full size image of size 1920 x 1080.
     * - Creates a medium size image of size 800 x 450 and saves it to a file.
     * - Logs any exceptions that occur during the creation of the medium size image.
     *
     * @param request The [ThumbnailRequest] containing information about the image to be compressed.
     */
    override fun handle(request: ThumbnailRequest) {
        logInfo("Received request \"${request}\" for compressing")

        val baseImage = fileManager.retrieveImage(request.path)

        if (!baseImage.exists()) {
            logInfo("Image \"${baseImage.absolutePath}\" doesn't exist")
            return
        }

        logInfo("Compressing image: \"${baseImage.absolutePath}\"")

        when (request.target) {
            CompressTarget.CONTENT -> handleContentPhoto(baseImage)
            CompressTarget.USER -> handleUserPhoto(baseImage)
        }
    }

    private fun handleUserPhoto(imageToCompress: File) {
        // Default normalized size - used in full user profile view
        try {
            logInfo("Compressing image \"${imageToCompress.name}\" to L size starts")
            Thumbnails.of(imageToCompress)
                .size(Sizes.User.Width.L, Sizes.User.Height.L)
                .toFile(imageToCompress)
        } catch (e: Exception) {
            logError("Error creating large (default) user size image: ${e.message}", e)
        } finally {
            logInfo("Compressing image \"${imageToCompress.name}\" to L size stops")
        }

        // Medium size - used in pagination view in cards
        try {
            logInfo("Compressing image \"${imageToCompress.name}\" to M size starts")
            Thumbnails.of(imageToCompress)
                .size(Sizes.User.Width.M, Sizes.User.Height.M)
                .toFile(imageToCompress.toMediumFileName())
        } catch (e: Exception) {
            logError("Error creating medium user size image: ${e.message}", e)
        } finally {
            logInfo("Compressing image \"${imageToCompress.name}\" to M size stops")
        }

        // Small size - used in pagination view in lists
        try {
            logInfo("Compressing image \"${imageToCompress.name}\" to S size starts")
            Thumbnails.of(imageToCompress)
                .size(Sizes.User.Width.S, Sizes.User.Height.S)
                .toFile(imageToCompress.toSmallFileName())
        } catch (e: Exception) {
            logError("Error creating small user size image: ${e.message}", e)
        } finally {
            logInfo("Compressing image \"${imageToCompress.name}\" to S size stops")
        }
    }

    private fun handleContentPhoto(imageToCompress: File) {
        // Default normalized size - used in full content view
        try {
            logInfo("Compressing image \"${imageToCompress.name}\" to L size starts")
            Thumbnails.of(imageToCompress)
                .size(Sizes.Content.Width.L, Sizes.Content.Height.L)
                .toFile(imageToCompress)
        } catch (e: Exception) {
            logError("Error creating large (default) content size image: ${e.message}", e)
        } finally {
            logInfo("Compressing image \"${imageToCompress.name}\" to L size stops")
        }

        // Medium size - used in pagination view
        try {
            logInfo("Compressing image \"${imageToCompress.name}\" to M size starts")
            Thumbnails.of(imageToCompress)
                .size(Sizes.Content.Width.M, Sizes.Content.Height.M)
                .toFile(imageToCompress.toMediumFileName())
        } catch (e: Exception) {
            logError("Error creating medium content size image: ${e.message}", e)
        } finally {
            logInfo("Compressing image \"${imageToCompress.name}\" to M size stops")
        }
    }
}