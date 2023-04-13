package ru.mephi.alumniclub.app.database.repository.news

import org.springframework.stereotype.Repository
import ru.mephi.alumniclub.app.database.entity.news.Carousel
import ru.mephi.alumniclub.shared.database.repository.AbstractRepository

@Repository
interface CarouselDao : AbstractRepository<Carousel>
