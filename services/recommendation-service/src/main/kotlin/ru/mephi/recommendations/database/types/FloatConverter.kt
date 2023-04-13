package ru.mephi.recommendations.database.types

import ru.mephi.recommendations.utils.toByteArray
import ru.mephi.recommendations.utils.toFloatArray
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = false)
class FloatConverter : AttributeConverter<Array<Float>, Array<Byte>> {
    override fun convertToDatabaseColumn(attribute: Array<Float>): Array<Byte> {
        return attribute.toByteArray()
    }

    override fun convertToEntityAttribute(dbData: Array<Byte>): Array<Float> {
        return dbData.toFloatArray()
    }
}