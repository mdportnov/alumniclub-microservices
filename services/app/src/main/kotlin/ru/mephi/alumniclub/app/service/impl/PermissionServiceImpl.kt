package ru.mephi.alumniclub.app.service.impl

import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Service
import ru.mephi.alumniclub.app.database.entity.project.AbstractProject
import ru.mephi.alumniclub.app.database.entity.user.User
import ru.mephi.alumniclub.app.database.repository.project.AbstractProjectDao
import ru.mephi.alumniclub.app.model.dto.PageResponse
import ru.mephi.alumniclub.app.model.dto.security.AdminDTO
import ru.mephi.alumniclub.shared.dto.security.PermissionsDTO
import ru.mephi.alumniclub.app.model.dto.user.response.ModeratorResponse
import ru.mephi.alumniclub.app.model.enumeration.user.Role
import ru.mephi.alumniclub.app.model.exception.common.ResourceNotFoundException
import ru.mephi.alumniclub.app.model.mapper.permission.ModeratorMapper
import ru.mephi.alumniclub.app.service.PermissionService
import ru.mephi.alumniclub.app.service.ProjectService
import ru.mephi.alumniclub.app.service.UserService
import ru.mephi.alumniclub.shared.dto.ExtendedPageRequest
import ru.mephi.alumniclub.shared.util.response.ResponseManager
import javax.transaction.Transactional

@Service
@Transactional
class PermissionServiceImpl(
    private val userService: UserService,
    private val projectDao: AbstractProjectDao<AbstractProject>,
    private val moderatorMapper: ModeratorMapper
) : ResponseManager(), PermissionService {
    private fun findModeratorEntityById(id: Long): User {
        val user = userService.findUserEntityById(id)
        if (!user.admin && !user.moderator) throw ResourceNotFoundException()
        return user
    }

    override fun listModerators(query: String, pageRequest: ExtendedPageRequest): PageResponse<ModeratorResponse> {
        val moderators = userService.listByModeratorOrAdminAndSurnameStartsWith(query, pageRequest)
        return moderatorMapper.asPageResponse(moderators)
    }

    override fun findModeratorById(id: Long): ModeratorResponse {
        return moderatorMapper.asResponse(findModeratorEntityById(id))
    }

    @Modifying
    override fun updatePermissions(id: Long, request: PermissionsDTO): ModeratorResponse {
        request.projects.forEach { projectId ->
            if (!projectDao.existsById(projectId)) throw ResourceNotFoundException(AbstractProject::class.java)
        }
        val user = userService.findUserEntityById(id).apply { permissions = request }
        val roles = if (request.hasPermissions()) user.roles + Role.MODERATOR else user.roles - Role.MODERATOR
        userService.updateRoles(user, roles)
        return moderatorMapper.asResponse(userService.findUserEntityById(id))
    }

    @Modifying
    override fun updateAdminRole(request: AdminDTO): AdminDTO {
        val user = userService.findUserEntityById(request.id)
        val roles = if (request.admin) user.roles + Role.ADMIN else user.roles - Role.ADMIN
        userService.updateRoles(user, roles)
        return AdminDTO(user.id, user.admin)
    }
}
