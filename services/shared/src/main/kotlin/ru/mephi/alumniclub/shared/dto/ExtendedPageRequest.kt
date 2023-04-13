package ru.mephi.alumniclub.shared.dto

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.web.bind.annotation.RequestParam

class ExtendedPageRequest(
    @RequestParam val page: Int,
    @RequestParam val size: Int,
    @RequestParam val order: Sort.Direction,
    @RequestParam var field: String
) {
    var pageable = PageRequest.of(page - 1, size, order, field)

    fun updateRequest(orders: List<Order>) {
        val sort = Sort.by(orders)
        pageable = PageRequest.of(page - 1, size, sort)
    }
}
