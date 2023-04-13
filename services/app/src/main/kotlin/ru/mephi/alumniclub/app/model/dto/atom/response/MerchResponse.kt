package ru.mephi.alumniclub.app.model.dto.atom.response

import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed


class MerchResponse(
    val id: Long,
    val name: String,
    val description: String,
    val cost: Int,
    val availability: Boolean,
    override var photoPath: String? = "",
) : PhotoPathed