package ru.mephi.alumniclub.app.model.dto.form.request

import java.time.LocalDateTime
import javax.validation.constraints.Size

sealed class AbstractFormRequest(
    @field:Size(max = 600)
    open val text: String = ""
) {
    class FormOfferPollRequest(
        text: String,
    ) : AbstractFormRequest(text)

    class FormOfferPartnershipRequest(
        @field:Size(max = 600)
        var requirements: String,
        @field:Size(max = 600)
        var selfDescription: String,
        @field:Size(max = 300)
        var projectName: String,
        @field:Size(max = 600)
        var projectDescription: String,
        @field:Size(max = 600)
        var helpDescription: String,
        var currentUntil: LocalDateTime,
    ) : AbstractFormRequest()

    class FormJoinPartnershipRequest(
        @field:Size(max = 600)
        var contribution: String,
        var partnershipId: Long
    ) : AbstractFormRequest()

    class FormOfferCommunityRequest(
        text: String,
    ) : AbstractFormRequest(text)

    class FormFindMentorRequest(
        @field:Size(max = 600)
        var motivation: String,
        @field:Size(max = 600)
        var description: String,
        @field:Size(max = 600)
        var targets: String,
        var mentorId: Long,
    ) : AbstractFormRequest()

    class FormBecomeMentorRequest(
        @field:Size(max = 600)
        var company: String?,
        @field:Size(max = 600)
        var position: String?,
        @field:Size(max = 600)
        var expertArea: String,
        @field:Size(max = 600)
        var whyAreYouMentor: String?,
        @field:Size(max = 600)
        var helpArea: String,
        @field:Size(max = 600)
        var timeForMentoring: String,
        @field:Size(max = 600)
        var formatsOfInteraction: String,
    ) : AbstractFormRequest()

    class FormBuyMerchRequest(var merchId: Long) : AbstractFormRequest()
}
