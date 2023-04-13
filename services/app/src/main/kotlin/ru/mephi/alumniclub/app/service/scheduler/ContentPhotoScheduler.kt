package ru.mephi.alumniclub.app.service.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import ru.mephi.alumniclub.app.service.ContentPhotoService
import ru.mephi.alumniclub.app.service.StorageManager
import ru.mephi.alumniclub.shared.logging.AlumniLogger
import ru.mephi.alumniclub.shared.model.enums.StoreDir
import ru.mephi.alumniclub.shared.util.extension.createIfNotExist
import kotlin.io.path.Path
import kotlin.io.path.pathString

@Component
class ContentPhotoScheduler(
    private val storageManager: StorageManager,
    private val contentPhotoService: ContentPhotoService,
    private val logger: AlumniLogger
) {

    @Scheduled(cron = "0 0 0 * * SUN") // every Sunday
    fun handle() {
        logger.info("Start cleaning up unattended media files")
        try {
            val path = Path(storageManager.root.pathString + StoreDir.CONTENT_PHOTO.path).createIfNotExist()
            val folder = path.toFile()
            folder.listFiles()?.forEach {
                val photoPath = (StoreDir.CONTENT_PHOTO.path + it.name).drop(1)
                if (!contentPhotoService.contentExists(photoPath)) {
                    logger.info("Deleting unattended media file: $photoPath")
                    contentPhotoService.deleteContentPhoto(photoPath)
                    storageManager.removeImage(photoPath, StoreDir.CONTENT_PHOTO)
                }
            }
        } catch (e: Exception) {
            logger.error(e.stackTraceToString())
        } finally {
            logger.info("Cleaning of unattended media files has been completed")
        }
    }
}