package ru.mephi.alumniclub.app.model.dto.user

class UserVisibilityDTO(
    val email: Boolean = false,
    val phone: Boolean = false,
    val vk: Boolean = false,
    val tg: Boolean = false,
    val gender: Boolean = false,
    val birthday: Boolean = false,
    val createdAt: Boolean = false,
    val bioVisibility: BioVisibilityDTO = BioVisibilityDTO(),
    val degrees: Boolean = false
)
