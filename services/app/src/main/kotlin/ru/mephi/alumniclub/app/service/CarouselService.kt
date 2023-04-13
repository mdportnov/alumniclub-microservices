package ru.mephi.alumniclub.app.service

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import ru.mephi.alumniclub.app.database.entity.news.Carousel
import ru.mephi.alumniclub.app.model.dto.news.CarouselRequest
import ru.mephi.alumniclub.app.model.dto.news.CarouselResponse
import javax.servlet.http.Part

interface CarouselService {
    fun findEntityById(id: Long): Carousel
    fun findAll(): List<Carousel>

    fun uploadPhoto(@PathVariable id: Long, @RequestBody file: Part): CarouselResponse
    fun deleteById(@PathVariable id: Long)
    fun create(@RequestBody request: CarouselRequest): CarouselResponse
    fun update(@PathVariable id: Long, @RequestBody request: CarouselRequest): CarouselResponse
    fun getById(@PathVariable id: Long): CarouselResponse
    fun getAll(): List<CarouselResponse>
}
