package ru.mephi.alumniclub.shared.validators.permissions

import ru.mephi.alumniclub.shared.dto.security.PermissionsDTO
import ru.mephi.alumniclub.shared.dto.security.ScopePermission
import ru.mephi.alumniclub.shared.validators.AbstractValidator

class PermissionsValidator : AbstractValidator<PermissionsConstraint, PermissionsDTO>() {
    override fun initialize(annotation: PermissionsConstraint) {
        message = i18n(annotation.message)
    }

    override fun checkViolations(value: PermissionsDTO) {
        validateProjectsViewPermission(value)
        validateBroadcastsPermission(value)
        validateFormsPermission(value)
        validateMentorsPermission(value)
        validateAtomsPermission(value)
        validateSurveysPermission(value)
    }

    private fun validateProjectsViewPermission(value: PermissionsDTO) {
        if (ScopePermission.PROJECTS_VIEW in value.scopes && ScopePermission.USERS !in value.scopes)
            addViolation(
                "permission" to ScopePermission.PROJECTS_VIEW,
                "constraint" to ScopePermission.USERS
            )
    }

    private fun validateBroadcastsPermission(value: PermissionsDTO) {
        if (ScopePermission.BROADCASTS in value.scopes && ScopePermission.USERS !in value.scopes)
            addViolation(
                "permission" to ScopePermission.BROADCASTS,
                "constraint" to ScopePermission.USERS
            )
    }

    private fun validateFormsPermission(value: PermissionsDTO) {
        if (ScopePermission.FORMS in value.scopes && ScopePermission.USERS !in value.scopes)
            addViolation(
                "permission" to ScopePermission.FORMS,
                "constraint" to ScopePermission.USERS
            )
    }

    private fun validateMentorsPermission(value: PermissionsDTO) {
        if (ScopePermission.MENTORS in value.scopes && ScopePermission.USERS !in value.scopes)
            addViolation(
                "permission" to ScopePermission.MENTORS,
                "constraint" to ScopePermission.USERS
            )
        if (ScopePermission.MENTORS in value.scopes && ScopePermission.FORMS !in value.scopes)
            addViolation(
                "permission" to ScopePermission.MENTORS,
                "constraint" to ScopePermission.FORMS
            )
    }

    private fun validateAtomsPermission(value: PermissionsDTO) {
        if (ScopePermission.ATOMS in value.scopes && ScopePermission.USERS !in value.scopes)
            addViolation(
                "permission" to ScopePermission.ATOMS,
                "constraint" to ScopePermission.USERS
            )
    }

    private fun validateSurveysPermission(value: PermissionsDTO) {
        if (ScopePermission.SURVEYS in value.scopes && ScopePermission.USERS !in value.scopes)
            addViolation(
                "permission" to ScopePermission.SURVEYS,
                "constraint" to ScopePermission.USERS
            )
    }
}
