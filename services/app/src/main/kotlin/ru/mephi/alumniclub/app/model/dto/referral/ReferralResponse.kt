package ru.mephi.alumniclub.app.model.dto.referral

import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.app.model.enumeration.referral.ReferralStatus
import ru.mephi.alumniclub.shared.dto.photo.PhotoPathed

class ReferralResponse(
    val user: UserShortResponse,
    val status: ReferralStatus,
    override var photoPath: String?
) : PhotoPathed