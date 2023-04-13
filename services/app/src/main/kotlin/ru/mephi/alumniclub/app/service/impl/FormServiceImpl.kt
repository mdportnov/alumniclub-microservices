package ru.mephi.alumniclub.app.service.impl

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.atom.Merch
import ru.mephi.alumniclub.app.database.entity.user.MentorData
import ru.mephi.alumniclub.app.database.repository.MerchDao
import ru.mephi.alumniclub.app.database.repository.form.*
import ru.mephi.alumniclub.app.database.repository.user.MentorDao
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.form.request.AbstractFormRequest
import ru.mephi.alumniclub.app.model.dto.form.request.FormChangeStatusRequest
import ru.mephi.alumniclub.app.model.dto.form.response.FormResponse
import ru.mephi.alumniclub.app.model.dto.form.response.FormShortResponse
import ru.mephi.alumniclub.app.model.dto.form.response.FormTypeResponse
import ru.mephi.alumniclub.app.model.dto.form.response.SelfFormResponse
import ru.mephi.alumniclub.app.model.enumeration.FormType
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.form.*
import ru.mephi.alumniclub.app.service.AtomService
import ru.mephi.alumniclub.app.service.FormService
import ru.mephi.alumniclub.app.service.PartnershipService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.dto.common.ApiError
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class FormServiceImpl(
    private val findMentorDao: FormFindMentorDao,
    private val becomeMentorDao: FormBecomeMentorDao,
    private val mentorDao: MentorDao,
    private val offerPartnershipDao: FormOfferPartnershipDao,
    private val joinPartnershipDao: FormJoinPartnershipDao,
    private val offerPollDao: FormOfferPollDao,
    private val offerCommunityDao: FormOfferCommunityDao,
    private val buyMerchDao: FormBuyMerchDao,
    private val merchDao: MerchDao,
    private val userService: UserService,
    private val partnershipService: PartnershipService,
    private val becomeMentorFormMapper: BecomeMentorFormMapper,
    private val findMentorFormMapper: FindMentorFormMapper,
    private val offerPollFormMapper: OfferPollFormMapper,
    private val offerPartnershipFormMapper: OfferPartnershipFormMapper,
    private val joinPartnershipFormMapper: JoinPartnershipFormMapper,
    private val offerCommunityFormMapper: OfferCommunityFormMapper,
    private val buyMerchFormMapper: BuyMerchFormMapper,
    private val atomService: AtomService
) : ResponseManager(), FormService {
    private val requestsLimit = 5

    fun throwNotFoundForm(formId: Long, formType: FormType): Nothing {
        val message = i18n("exception.notFound.formWithType", formType.description, "$formId")
        throw ApiError(HttpStatus.NOT_FOUND, message)
    }

    override fun listAllUserForms(userId: Long): List<SelfFormResponse> {
        val allUserForms = mutableListOf<SelfFormResponse>()
        val user = userService.findUserEntityById(userId)
        allUserForms.apply {
            addAll(
                becomeMentorFormMapper.asSelfFormResponseList(becomeMentorDao.findAllByAuthor(user))
            )
            addAll(
                buyMerchFormMapper.asSelfFormResponseList(buyMerchDao.findAllByAuthor(user))
            )
            addAll(
                findMentorFormMapper.asSelfFormResponseList(findMentorDao.findAllByAuthor(user))
            )
            addAll(
                offerCommunityFormMapper.asSelfFormResponseList(offerCommunityDao.findAllByAuthor(user))
            )
            addAll(
                offerPollFormMapper.asSelfFormResponseList(offerPollDao.findAllByAuthor(user))
            )
            addAll(
                offerPartnershipFormMapper.asSelfFormResponseList(offerPartnershipDao.findAllByAuthor(user))
            )
            addAll(
                joinPartnershipFormMapper.asSelfFormResponseList(joinPartnershipDao.findAllByAuthor(user))
            )
        }
        return allUserForms
    }

    private fun listBuyMerch(pageRequest: ExtendedPageRequest): PageResponse<FormShortResponse> {
        return buyMerchFormMapper.asShortResponseList(
            buyMerchDao.findByOrderByCreatedAtDesc(pageRequest.pageable)
        )
    }

    override fun listByType(formType: FormType, pageRequest: ExtendedPageRequest): PageResponse<FormShortResponse> {
        return when (formType) {
            FormType.BuyMerch -> listBuyMerch(pageRequest)
            FormType.BecomeMentor -> listBecomeMentor(pageRequest)
            FormType.FindMentor -> listFindMentor(pageRequest)
            FormType.OfferPartnership -> listOfferPartnership(pageRequest)
            FormType.JoinPartnership -> listJoinPartnership(pageRequest)
            FormType.OfferPoll -> listOfferPoll(pageRequest)
            FormType.OfferCommunity -> listOfferCommunity(pageRequest)
        }
    }

    private fun listFindMentor(pageRequest: ExtendedPageRequest): PageResponse<FormShortResponse> {
        return findMentorFormMapper.asShortResponseList(
            findMentorDao.findByOrderByCreatedAtDesc(pageRequest.pageable)
        )
    }

    private fun listBecomeMentor(pageRequest: ExtendedPageRequest): PageResponse<FormShortResponse> {
        return becomeMentorFormMapper.asShortResponseList(
            becomeMentorDao.findByOrderByCreatedAtDesc(pageRequest.pageable)
        )
    }

    private fun listOfferPoll(pageRequest: ExtendedPageRequest): PageResponse<FormShortResponse> {
        return offerPollFormMapper.asShortResponseList(
            offerPollDao.findByOrderByCreatedAtDesc(pageRequest.pageable)
        )
    }

    private fun listOfferPartnership(pageRequest: ExtendedPageRequest): PageResponse<FormShortResponse> {
        return offerPartnershipFormMapper.asShortResponseList(
            offerPartnershipDao.findByOrderByCreatedAtDesc(pageRequest.pageable)
        )
    }

    private fun listJoinPartnership(pageRequest: ExtendedPageRequest): PageResponse<FormShortResponse> {
        return joinPartnershipFormMapper.asShortResponseList(
            joinPartnershipDao.findByOrderByCreatedAtDesc(pageRequest.pageable)
        )
    }

    private fun listOfferCommunity(pageRequest: ExtendedPageRequest): PageResponse<FormShortResponse> {
        return offerCommunityFormMapper.asShortResponseList(
            offerCommunityDao.findByOrderByCreatedAtDesc(pageRequest.pageable)
        )
    }

    @Transactional
    override fun saveForm(userId: Long, request: AbstractFormRequest): FormResponse {
        val user = userService.findUserEntityById(userId)

        if (user.banned) {
            throw ApiError(HttpStatus.FORBIDDEN, i18n("exception.forbidden.userBanned"))
        }

        val requestedAt = LocalDateTime.now()

        when (request) {
            is AbstractFormRequest.FormBecomeMentorRequest -> {
                val newEntity = becomeMentorFormMapper.asEntity(user, request)
                val entityFromDB = becomeMentorDao.findByAuthorId(user.id)
                val savedEntity = if (entityFromDB.isPresent) {
                    becomeMentorFormMapper.update(entityFromDB.get(), request)
                } else becomeMentorDao.save(newEntity)
                return becomeMentorFormMapper.asFullResponse(savedEntity)
            }

            is AbstractFormRequest.FormFindMentorRequest -> {
                if (findMentorDao.countByCreatedAtBetweenAndAuthor(
                        requestedAt.minusHours(1), requestedAt, user
                    ) >= requestsLimit
                ) throw ApiError(HttpStatus.TOO_MANY_REQUESTS, i18n("exception.tooManyRequests.forms"))

                if (findMentorDao.countAllByAuthorId(user.id) >= requestsLimit) {
                    throw ApiError(HttpStatus.TOO_MANY_REQUESTS, i18n("exception.tooManyRequests.forms"))
                }

                if (!mentorDao.existsByUserId(request.mentorId)) throw ResourceNotFoundException(
                    MentorData::class.java, request.mentorId
                )

                if (findMentorDao.countAllByAuthorIdAndMentorId(user.id, request.mentorId) > 0) {
                    throw ApiError(HttpStatus.CONFLICT, i18n("exception.alreadyExists.formToThisMentor"))
                }

                val mentorUser = userService.findUserEntityById(request.mentorId)

                val formEntity = findMentorFormMapper.asEntity(user, mentorUser, request)

                val savedEntity = findMentorDao.save(formEntity)
                savedEntity.mentorName = mentorUser.surname + " " + mentorUser.name

                return findMentorFormMapper.asFullResponse(savedEntity)
            }

            is AbstractFormRequest.FormBuyMerchRequest -> {
                if (buyMerchDao.countByCreatedAtBetweenAndAuthor(
                        requestedAt.minusHours(1), requestedAt, user
                    ) >= requestsLimit
                ) throw ApiError(HttpStatus.TOO_MANY_REQUESTS, i18n("exception.tooManyRequests.forms"))

                val merch = merchDao.findByIdAndAvailabilityIsTrue(request.merchId).orElseGet {
                    throw ResourceNotFoundException(Merch::class.java, request.merchId)
                }

                val atomCount = atomService.getUserAtomsCount(userId)
                if (atomCount < merch.cost)
                    throw ApiError(HttpStatus.BAD_REQUEST, i18n("exception.tooFewAtoms", "${merch.cost}", "$atomCount"))

                val formEntity = buyMerchFormMapper.asEntity(user, merch)
                val savedEntity = buyMerchDao.save(formEntity)
                savedEntity.merchName = merch.name

                return buyMerchFormMapper.asFullResponse(savedEntity)
            }

            is AbstractFormRequest.FormOfferPartnershipRequest -> {
                if (offerPartnershipDao.countByCreatedAtBetweenAndAuthor(
                        requestedAt.minusHours(1), requestedAt, user
                    ) >= requestsLimit
                ) throw ApiError(HttpStatus.TOO_MANY_REQUESTS, i18n("exception.tooManyRequests.forms"))
                val savedEntity = offerPartnershipDao.save(offerPartnershipFormMapper.asEntity(user, request))
                return offerPartnershipFormMapper.asFullResponse(savedEntity)
            }

            is AbstractFormRequest.FormJoinPartnershipRequest -> {
                if (joinPartnershipDao.countByCreatedAtBetweenAndAuthor(
                        requestedAt.minusHours(1), requestedAt, user
                    ) >= requestsLimit
                ) throw ApiError(HttpStatus.TOO_MANY_REQUESTS, i18n("exception.tooManyRequests.forms"))
                val partnership = partnershipService.findEntityById(request.partnershipId)
                val entity = joinPartnershipFormMapper.asEntity(user, partnership, request)
                return joinPartnershipFormMapper.asFullResponse(joinPartnershipDao.save(entity))
            }

            is AbstractFormRequest.FormOfferPollRequest -> {
                if (offerPollDao.countByCreatedAtBetweenAndAuthor(
                        requestedAt.minusHours(1), requestedAt, user
                    ) >= requestsLimit
                ) throw ApiError(HttpStatus.TOO_MANY_REQUESTS, i18n("exception.tooManyRequests.forms"))
                val savedEntity = offerPollDao.save(offerPollFormMapper.asEntity(user, request))
                return offerPollFormMapper.asFullResponse(savedEntity)
            }

            is AbstractFormRequest.FormOfferCommunityRequest -> {
                if (offerCommunityDao.countByCreatedAtBetweenAndAuthor(
                        requestedAt.minusHours(1), requestedAt, user
                    ) >= requestsLimit
                ) throw throw ApiError(HttpStatus.TOO_MANY_REQUESTS, i18n("exception.tooManyRequests.forms"))
                val savedEntity = offerCommunityDao.save(offerCommunityFormMapper.asEntity(user, request))
                return offerCommunityFormMapper.asFullResponse(savedEntity)
            }
        }
    }

    override fun listTypes(): List<FormTypeResponse> = FormType.values().map { FormTypeResponse(it, it.description) }

    override fun getById(formId: Long, formType: FormType): FormResponse {
        return when (formType) {
            FormType.BuyMerch -> {
                val entity = buyMerchDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                buyMerchFormMapper.asFullResponse(entity)
            }

            FormType.BecomeMentor -> {
                val entity = becomeMentorDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                becomeMentorFormMapper.asFullResponse(entity)
            }

            FormType.FindMentor -> {
                val entity = findMentorDao.findFormById(formId) ?: throwNotFoundForm(formId, formType)
                findMentorFormMapper.asFullResponse(entity)
            }

            FormType.OfferPartnership -> {
                val entity = offerPartnershipDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                offerPartnershipFormMapper.asFullResponse(entity)
            }

            FormType.JoinPartnership -> {
                val entity = joinPartnershipDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                joinPartnershipFormMapper.asFullResponse(entity)
            }

            FormType.OfferPoll -> {
                val entity = offerPollDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                offerPollFormMapper.asFullResponse(entity)
            }

            FormType.OfferCommunity -> {
                val entity = offerCommunityDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                offerCommunityFormMapper.asFullResponse(entity)
            }
        }
    }

    override fun changeStatus(
        formType: FormType, formId: Long, newStatus: FormChangeStatusRequest
    ): FormResponse {
        return when (formType) {
            FormType.BuyMerch -> {
                buyMerchDao.updateStatusById(formId, newStatus.formStatus)
                val entity = buyMerchDao.findById(formId).orElseGet {
                    throwNotFoundForm(formId, formType)
                }
                buyMerchFormMapper.asFullResponse(entity)
            }

            FormType.BecomeMentor -> {
                becomeMentorDao.updateStatusById(formId, newStatus.formStatus)
                val entity = becomeMentorDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                becomeMentorFormMapper.asFullResponse(entity)
            }

            FormType.FindMentor -> {
                findMentorDao.updateStatusById(formId, newStatus.formStatus)
                val entity = findMentorDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                findMentorFormMapper.asFullResponse(entity)
            }

            FormType.OfferPartnership -> {
                offerPartnershipDao.updateStatusById(formId, newStatus.formStatus)
                val entity = offerPartnershipDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                offerPartnershipFormMapper.asFullResponse(entity)
            }

            FormType.JoinPartnership -> {
                joinPartnershipDao.updateStatusById(formId, newStatus.formStatus)
                val entity = joinPartnershipDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                joinPartnershipFormMapper.asFullResponse(entity)
            }

            FormType.OfferPoll -> {
                offerPollDao.updateStatusById(formId, newStatus.formStatus)
                val entity = offerPollDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                offerPollFormMapper.asFullResponse(entity)
            }

            FormType.OfferCommunity -> {
                offerCommunityDao.updateStatusById(formId, newStatus.formStatus)
                val entity = offerCommunityDao.findById(formId).orElseGet { throwNotFoundForm(formId, formType) }
                offerCommunityFormMapper.asFullResponse(entity)
            }
        }
    }

    override fun getFormByTypeAndUserId(formType: FormType, formId: Long, userId: Long): FormResponse {
        return when (formType) {
            FormType.BuyMerch -> {
                buyMerchFormMapper.asFullResponse(buyMerchDao.findByIdAndAuthorId(formId, userId).orElseGet {
                    throwNotFoundForm(formId, formType)
                })
            }

            FormType.BecomeMentor -> {
                becomeMentorFormMapper.asFullResponse(becomeMentorDao.findByIdAndAuthorId(formId, userId).orElseGet {
                    throwNotFoundForm(formId, formType)
                })
            }

            FormType.FindMentor -> {
                findMentorFormMapper.asFullResponse(findMentorDao.findByIdAndAuthorId(formId, userId).orElseGet {
                    throwNotFoundForm(formId, formType)
                })
            }

            FormType.OfferPartnership -> {
                offerPartnershipFormMapper.asFullResponse(
                    offerPartnershipDao.findByIdAndAuthorId(formId, userId).orElseGet {
                        throwNotFoundForm(formId, formType)
                    })
            }

            FormType.JoinPartnership -> {
                joinPartnershipFormMapper.asFullResponse(
                    joinPartnershipDao.findByIdAndAuthorId(formId, userId).orElseGet {
                        throwNotFoundForm(formId, formType)
                    })
            }

            FormType.OfferPoll -> {
                val entity = offerPollDao.findByIdAndAuthorId(formId, userId).orElseGet {
                    throwNotFoundForm(formId, formType)
                }
                offerPollFormMapper.asFullResponse(entity)
            }

            FormType.OfferCommunity -> {
                offerCommunityFormMapper.asFullResponse(
                    offerCommunityDao.findByIdAndAuthorId(formId, userId).orElseGet {
                        throwNotFoundForm(formId, formType)
                    })
            }
        }
    }

    override fun delete(formType: FormType, formId: Long) = when (formType) {
        FormType.BuyMerch -> buyMerchDao
        FormType.BecomeMentor -> becomeMentorDao
        FormType.FindMentor -> findMentorDao
        FormType.OfferPartnership -> offerPartnershipDao
        FormType.JoinPartnership -> joinPartnershipDao
        FormType.OfferPoll -> offerPollDao
        FormType.OfferCommunity -> offerCommunityDao
    }.deleteById(formId)
}
