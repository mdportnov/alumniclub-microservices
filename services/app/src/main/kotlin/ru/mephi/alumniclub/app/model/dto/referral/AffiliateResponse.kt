package ru.mephi.alumniclub.app.model.dto.referral

import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse

class AffiliateResponse(
    val user: UserShortResponse,
    var link: String? = null,
    val referralsCount: Int
)