package ru.mephi.alumniclub.app.controller.handler.impl

import org.springframework.stereotype.Component
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.body
import ru.mephi.alumniclub.app.controller.handler.FeatureToggleHandler
import ru.mephi.alumniclub.app.model.dto.FeatureToggleUpdateRequest
import ru.mephi.alumniclub.app.service.FeatureToggleService
import ru.mephi.alumniclub.shared.dto.common.ApiMessage
import ru.mephi.alumniclub.shared.model.enums.Authority
import ru.mephi.alumniclub.shared.util.extension.assertHasAuthority
import ru.mephi.alumniclub.shared.util.extension.toOkBody
import ru.mephi.alumniclub.shared.util.response.ResponseManager

@Component
class FeatureToggleHandlerImpl(
    private val service: FeatureToggleService
) : ResponseManager(), FeatureToggleHandler {

    override fun changeFeatureState(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        val feature = request.body<FeatureToggleUpdateRequest>()
        validate(feature)
        return ApiMessage(
            message = i18n("label.feature.toggle"),
            data = service.changeFeatureState(feature)
        ).toOkBody()
    }

    override fun listAllFeatures(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        return service.listFeatures().toOkBody()
    }

    override fun getFeatureByName(request: ServerRequest): ServerResponse {
        request.assertHasAuthority(Authority.ADMIN)
        val featureName = request.pathVariable("featureName")
        return service.findByFeatureName(featureName).toOkBody()
    }
}
