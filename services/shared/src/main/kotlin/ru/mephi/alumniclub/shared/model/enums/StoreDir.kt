package ru.mephi.alumniclub.shared.model.enums

enum class StoreDir(val path: String) {
    USER("/users"), COMMUNITY("/communities"), PROJECT("/projects"), EVENT("/events"),
    PUBLICATION("/publications"), CAROUSEL("/news"), SURVEY("/surveys"),
    BROADCAST("/broadcast"), CONTENT_PHOTO("/content_photos"), MERCH("/merch")
}