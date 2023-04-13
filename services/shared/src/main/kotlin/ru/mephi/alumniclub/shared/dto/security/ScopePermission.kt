package ru.mephi.alumniclub.shared.dto.security

/**
 * Global moderator permissions that apply to a specific scope
 */
enum class ScopePermission {
    /**
     *  Create, update, upload photo and delete communities, kick participants; view statistics, list and export users
     */
    USERS,

    /**
     *  Ban/unban and delete users
     */
    USERS_MODIFY,

    /**
     * Create, update, upload photo and delete publications; list likes, participants and export participants
     */
    FEEDS_MODIFY,

    /**
     * View information about each project, project feed and project community
     *
     * **Must be set with [USERS] permission**
     */
    PROJECTS_VIEW,

    /**
     * Everything from [PROJECTS_VIEW] and [FEEDS_MODIFY]; create and delete projects
     */
    PROJECTS_CONTROL,

    /**
     *  Send broadcasts to community members
     *
     *  **Must be set with [USERS] permission**
     */
    BROADCASTS,

    /**
     *  Create, update, upload photo and delete carousel news
     */
    CAROUSEL,

    /**
     *  List forms and forms types, change forms status
     *
     *  **Must be set with [USERS] permission**
     */
    FORMS,

    /**
     *  Create, update and delete mentors, update availability status
     *
     *  **Must be set with [USERS] and [FORMS] permission**
     */
    MENTORS,

    /**
     *  View user history and delete history items; accrue and withdraw atoms;
     *  create, update, upload photo and delete merch
     *
     *  **Must be set with [USERS] permission**
     */
    ATOMS,

    /**
     *  Allows: create, update, upload photo and delete surveys
     *
     *  **Must be set with [USERS] permission**
     */
    SURVEYS;
}
