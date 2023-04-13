package ru.mephi.alumniclub.app.database.entity.feed

import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import ru.mephi.alumniclub.app.database.entity.publication.Join
import ru.mephi.alumniclub.app.model.enumeration.ParticipationFormat
import java.io.Serializable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "MeetingParticipation")
class MeetingParticipation(

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum( 'ONLINE', 'OFFLINE' )")
    val format: ParticipationFormat, // формат участия

    @Column(nullable = false)
    val departmentMeetup: Boolean = false, // Встречи на Кафедрах/Факультетах

    @Column(nullable = false)
    val movie: Boolean = false, // Официальный МИФИ: ректор, фильм

    @Column(nullable = false)
    val presentations: Boolean = false, // Презентации инициатив, открытых для выпускников

    @Column(nullable = false)
    val exhibition: Boolean = false, // Выставка научных достижений

    @Column(nullable = false)
    val greetings: Boolean = false, // Приветствие от современных студентов

    @Column(nullable = false)
    val enjoy: Boolean = false, // Развлекательные игровые форматы

    @Column(nullable = false)
    val performance: Boolean = false, // Хочу выступить перед сообществом выпускников

    @Column(nullable = false)
    val help: Boolean = false, // Хочу помочь

    @Column(nullable = false)
    val telegram: Boolean = false, // Даю согласие на добавление в телеграм чат

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "joinId")
    @MapsId
    var join: Join
) : Serializable {
    @Id
    @Column(name = "joinId", nullable = false)
    var joinId: Long? = null
}
