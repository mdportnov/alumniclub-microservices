package ru.mephi.alumniclub.app.service

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.database.entity.atom.Merch
import ru.mephi.alumniclub.app.model.dto.atom.request.MerchRequest
import ru.mephi.alumniclub.app.model.dto.atom.response.MerchResponse
import javax.servlet.http.Part

interface MerchService {
    fun createMerch(@RequestBody request: MerchRequest): MerchResponse
    fun getAllAvailableMerch(): List<MerchResponse>
    fun getAllMerch(): List<MerchResponse>
    fun getMerchById(id: Long): MerchResponse
    fun updateMerch(@PathVariable("id") id: Long, @RequestBody request: MerchRequest): MerchResponse
    fun deleteMerch(@PathVariable("id") id: Long)
    fun uploadPhoto(@PathVariable("id") id: Long, file: Part): MerchResponse
    fun findMerchEntityById(id: Long): Merch
}
