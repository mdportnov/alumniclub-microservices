package ru.mephi.alumniclub.app.service.helpers

import ru.mephi.alumniclub.app.model.dto.auth.request.LoginRequest

enum class UserMock(val value: String) {
    EMAIL("example@mail.ru"),
    PASSWORD("password"),
    FINGERPRINT("36a154f0-38f2-40b1-9eea-84dce50ed979"),
    DEPARTMENT("ICIS");

    companion object {
        fun loginRequest() = LoginRequest(EMAIL.value, PASSWORD.value, FINGERPRINT.value)
    }
}
