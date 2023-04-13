package ru.mephi.alumniclub.app.model.mapper.permission

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.mephi.alumniclub.shared.dto.security.PermissionsDTO
import javax.persistence.AttributeConverter

class PermissionsConverter : AttributeConverter<PermissionsDTO, String> {
    override fun convertToDatabaseColumn(attribute: PermissionsDTO): String? =
        if (attribute.noPermissions()) null else jacksonObjectMapper().writeValueAsString(attribute)

    override fun convertToEntityAttribute(dbData: String?): PermissionsDTO =
        if (dbData == null) PermissionsDTO() else jacksonObjectMapper().readValue<PermissionsDTO>(dbData)
}
