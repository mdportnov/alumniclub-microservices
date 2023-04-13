package ru.mephi.alumniclub.app.model.dto.excel

open class ExcelFieldGetter<T>(
    val name: String,
    val getValue: (T) -> Any?
)
