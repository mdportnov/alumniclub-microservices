package ru.mephi.alumniclub.app.service

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.database.entity.partnership.Partnership
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.partnership.request.PartnershipRequest
import ru.mephi.alumniclub.app.model.dto.partnership.response.PartnershipJoinResponse
import ru.mephi.alumniclub.app.model.dto.partnership.response.PartnershipResponse
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import javax.servlet.http.Part

interface PartnershipService {
    fun listForAdmin(pageRequest: ExtendedPageRequest): PageResponse<PartnershipResponse>
    fun listForPublic(pageRequest: ExtendedPageRequest): PageResponse<PartnershipResponse>
    fun create(userId: Long, @RequestBody request: PartnershipRequest): PartnershipResponse
    fun findById(@PathVariable id: Long): PartnershipResponse
    fun findEntityById(id: Long): Partnership
    fun update(@PathVariable id: Long, request: PartnershipRequest): PartnershipResponse
    fun uploadPhoto(@PathVariable id: Long, file: Part): PartnershipResponse
    fun delete(@PathVariable id: Long)
    fun listMembers(@PathVariable id: Long, pageRequest: ExtendedPageRequest): PageResponse<PartnershipJoinResponse>
}