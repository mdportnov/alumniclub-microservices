package ru.mephi.alumniclub.shared.config

import java.util.*
import javax.validation.MessageInterpolator

class CustomMessageInterpolator(
    private val defaultInterpolator: MessageInterpolator
) : MessageInterpolator {

    override fun interpolate(messageTemplate: String?, context: MessageInterpolator.Context?): String {
        return defaultInterpolator.interpolate(messageTemplate, context)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    override fun interpolate(messageTemplate: String?, context: MessageInterpolator.Context?, locale: Locale?): String {
        return defaultInterpolator.interpolate(messageTemplate, context, locale)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}
