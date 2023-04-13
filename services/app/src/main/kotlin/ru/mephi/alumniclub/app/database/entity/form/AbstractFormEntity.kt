package ru.mephi.alumniclub.app.database.entity.form

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.model.dto.form.FieldNameAndValue
import ru.mephi.alumniclub.app.model.enumeration.FormStatus
import ru.mephi.alumniclub.shared.database.entity.AbstractCreatedAtEntity
import javax.persistence.*
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation

@MappedSuperclass
abstract class AbstractFormEntity(
    @Column(
        name = "status",
        nullable = false,
        columnDefinition = "enum('UNSEEN', 'SEEN', 'ACCEPTED', 'DECLINED')"
    )
    @Enumerated(EnumType.STRING)
    var status: FormStatus = FormStatus.UNSEEN,

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "authorId", nullable = false)
    var author: User? = null,
) : AbstractCreatedAtEntity() {
    fun getFieldNameAndValueList(): List<FieldNameAndValue> {
        val pairs = this::class.declaredMemberProperties
            .filter { it.findAnnotation<FormField>() != null }
            .map { property ->
                FieldNameAndValue(
                    fieldName = property.name,
                    fieldTitle = titlesMap[property.name] ?: "",
                    fieldValue = property.getter.call(this)
                )
            }
        return pairs
    }
}
