package ru.mephi.alumniclub.app.security

import io.github.bucket4j.ConsumptionProbe

interface IRateLimiter {
    /**
     * Returns a ConsumptionProbe for specific UserId
     * @property key is userId string to define a personal bucket for each user.
     * @property isAdmin checks the user's admin rights. Depending on this parameter,
     * the bucket with the desired settings is selected.
     */
    fun tryAuthorizedAccess(key: String, isAdmin: Boolean): ConsumptionProbe

    /**
     * Returns a ConsumptionProbe for specific IP address
     * @property key is IP address string to define a personal bucket for each IP address.
     */
    fun tryUnAuthorizedAccess(key: String): ConsumptionProbe

    fun trySwaggerAccess(): ConsumptionProbe
}