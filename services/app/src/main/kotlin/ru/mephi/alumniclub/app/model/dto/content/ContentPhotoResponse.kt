package ru.mephi.alumniclub.app.model.dto.content

import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed

class ContentPhotoResponse(
    override var photoPath: String?,
) : PhotoPathed