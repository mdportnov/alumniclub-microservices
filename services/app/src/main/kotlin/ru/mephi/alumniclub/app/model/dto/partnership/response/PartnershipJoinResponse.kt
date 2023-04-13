package ru.mephi.alumniclub.app.model.dto.partnership.response

import ru.mephi.alumniclub.app.model.dto.user.response.UserShortResponse
import ru.mephi.alumniclub.shared.dto.AbstractResponse

class PartnershipJoinResponse(
    id: Long,
    var member: UserShortResponse,
    var partnership: PartnershipResponse,
    var contribution: String
) : AbstractResponse<Long>(id)