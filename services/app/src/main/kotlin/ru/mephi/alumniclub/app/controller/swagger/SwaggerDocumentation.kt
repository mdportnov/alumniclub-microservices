package ru.mephi.alumniclub.app.controller.swagger

import java.lang.annotation.Inherited


@Target(allowedTargets = [AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS])
@Retention(AnnotationRetention.RUNTIME)
@Inherited
annotation class SwaggerDocumentation