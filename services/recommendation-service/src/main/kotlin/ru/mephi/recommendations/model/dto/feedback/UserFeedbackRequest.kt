package ru.mephi.recommendations.model.dto.feedback

import java.util.*

class UserFeedbackRequest(
    val userId: UUID,
    val publicationId: UUID,
    val type: UserFeedbackType,
)