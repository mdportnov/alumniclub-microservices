package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.AtomService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1

@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/atom/my",
        method = arrayOf(RequestMethod.GET),
        beanClass = AtomService::class,
        beanMethod = "getUserAtomHistory",
        operation = Operation(
            operationId = "atom/my",
            description = "The user will receive all of their own histories about accruals / deductions atoms"
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/atom",
        method = arrayOf(RequestMethod.GET),
        beanClass = AtomService::class,
        beanMethod = "getUserAtomHistory",
        operation = Operation(
            operationId = "admin/atom",
            description = "The admin will receive all histories about accruals / deductions atoms of entered user"
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/atom/accrue",
        method = arrayOf(RequestMethod.POST),
        beanClass = AtomService::class,
        beanMethod = "accrueAtoms",
        operation = Operation(
            operationId = "admin/atom/accrue", description = """Admin can give or take away some atoms to user.
                    This request add new atoms history for entered user 
                    and return all user atom histories with this update"""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/atom",
        method = arrayOf(RequestMethod.DELETE),
        beanClass = AtomService::class,
        beanMethod = "deleteAtomHistoryById",
        operation = Operation(
            operationId = "admin/atom",
            description = """The admin can cancel his decision about accrual or taking away atoms from the user.
                    This request delete one of atoms histories of this user 
                    and return all his atom histories with this update"""
        )
    )
)
annotation class AtomRoutingDoc