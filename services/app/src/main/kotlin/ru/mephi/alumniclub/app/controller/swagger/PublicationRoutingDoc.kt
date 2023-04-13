package ru.mephi.alumniclub.app.controller.swagger

import io.swagger.v3.oas.annotations.Operation
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.web.bind.annotation.RequestMethod
import ru.mephi.alumniclub.app.service.PublicationService
import ru.mephi.alumniclub.shared.util.constants.API_VERSION_1


@SwaggerDocumentation
@RouterOperations(
    RouterOperation(
        path = "$API_VERSION_1/public/feed/publication",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listAllPublications",
        operation = Operation(
            operationId = "public/feed/publication",
            description = """Returns list of all publications with page pagination."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/public/feed/event/preview",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "previewEvents",
        operation = Operation(
            operationId = "public/feed/event/preview",
            description = """Returns 5 of last events."""
        )
    ),

    RouterOperation(
        path = "$API_VERSION_1/feed/event",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listAllEvents",
        operation = Operation(
            operationId = "GET feed/event",
            description = """List events from all feeds."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/feed/default/{feedName}/publication",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listPublicationsByDefaultFeedName",
        operation = Operation(
            operationId = "GET feed/default/{feedName}/publication",
            description = """List publications from default feed by feed name.
                Supported names: announcements, partnerships, achievements,
                 projects, endowments, polls, atoms, distribution."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/feed/{feedId}/publication",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listPublicationsByFeedId",
        operation = Operation(
            operationId = "GET feed/{feedId}/publication",
            description = """List publications in feed."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/feed/{feedId}/publication/{publicationId}",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "findPublicationById",
        operation = Operation(
            operationId = "GET feed/{feedId}/publication/{publicationId}",
            description = """Get publication, increase views count."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/feed/{feedId}/publication/{publicationId}/like",
        method = [RequestMethod.POST],
        beanClass = PublicationService::class,
        beanMethod = "like",
        operation = Operation(
            operationId = "POST feed/{feedId}/publication/{publicationId}/like",
            description = """Like publication. User can like publication only once."""
        )
    ),

    RouterOperation(
        path = "$API_VERSION_1/feed/{feedId}/event",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listEventsByFeedId",
        operation = Operation(
            operationId = "GET feed/{feedId}/event",
            description = """List events in feed."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/feed/{feedId}/event/{eventId}",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "findEventById",
        operation = Operation(
            operationId = "GET feed/{feedId}/event/{eventId}",
            description = """Get event, increase views count."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/feed/{feedId}/event/{eventId}/like",
        method = [RequestMethod.POST],
        beanClass = PublicationService::class,
        beanMethod = "like",
        operation = Operation(
            operationId = "POST feed/{feedId}/event/{eventId}/like",
            description = """Like event. User can like event only once."""
        )
    ),

    RouterOperation(
        path = "$API_VERSION_1/feed/{feedId}/event/{eventId}/join",
        method = [RequestMethod.POST],
        beanClass = PublicationService::class,
        beanMethod = "join",
        operation = Operation(
            operationId = "POST feed/{feedId}/event/{eventId}/join",
            description = """Join event. User can join event only once."""
        )
    ),


    RouterOperation(
        path = "$API_VERSION_1/admin/feed/event",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listAllEvents",
        operation = Operation(
            operationId = "GET admin/feed/event",
            description = """List events from all feeds."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/default/{feedName}",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "findDefaultFeedByName",
        operation = Operation(
            operationId = "GET admin/feed/default/{feedName}",
            description = """Get feed information by default feed name.
                Supported names: announcements, partnerships, achievements, projects, 
                endowments, polls, atoms, distribution."""
        )
    ),

    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/publication",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listPublicationsByFeedId",
        operation = Operation(
            operationId = "GET admin/feed/{feedId}/publication",
            description = """List publications in feed."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/publication",
        method = [RequestMethod.POST],
        beanClass = PublicationService::class,
        beanMethod = "postPublication",
        operation = Operation(
            operationId = "POST admin/feed/{feedId}/publication",
            description = """Create publication."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/publication/{publicationId}",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "findPublicationById",
        operation = Operation(
            operationId = "GET admin/feed/{feedId}/publication/{publicationId}",
            description = """Get publication information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/publication/{publicationId}",
        method = [RequestMethod.PUT],
        beanClass = PublicationService::class,
        beanMethod = "updatePublication",
        operation = Operation(
            operationId = "PUT admin/feed/{feedId}/publication/{publicationId}",
            description = """Update publication information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/publication/{publicationId}/photo",
        method = [RequestMethod.POST],
        beanClass = PublicationService::class,
        beanMethod = "uploadPublicationPhoto",
        operation = Operation(
            operationId = "GET admin/feed/{feedId}/publication/{publicationId}/photo",
            description = """Upload publication photo."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/publication/{publicationId}",
        method = [RequestMethod.DELETE],
        beanClass = PublicationService::class,
        beanMethod = "deletePublication",
        operation = Operation(
            operationId = "DELETE admin/feed/{feedId}/publication/{publicationId}",
            description = """Delete publication."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/publication/{publicationId}/like",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listLikes",
        operation = Operation(
            operationId = "GET admin/feed/{feedId}/publication/{publicationId}/like",
            description = """List publication likes."""
        )
    ),

    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/event",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listEventsByFeedId",
        operation = Operation(
            operationId = "GET admin/feed/{feedId}/event",
            description = """List events in feed."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/event",
        method = [RequestMethod.POST],
        beanClass = PublicationService::class,
        beanMethod = "postEvent",
        operation = Operation(
            operationId = "POST admin/feed/{feedId}/event",
            description = """Create event."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/event/{eventId}",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "findEventById",
        operation = Operation(
            operationId = "GET admin/feed/{feedId}/event/{eventId}",
            description = """Get event information."""
        )
    ),

    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/event/{eventId}",
        method = [RequestMethod.PUT],
        beanClass = PublicationService::class,
        beanMethod = "updateEvent",
        operation = Operation(
            operationId = "PUT admin/feed/{feedId}/event/{eventId}",
            description = """Update event information."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/event/{eventId}/photo",
        method = [RequestMethod.POST],
        beanClass = PublicationService::class,
        beanMethod = "uploadEventPhoto",
        operation = Operation(
            operationId = "GET admin/feed/{feedId}/event/{eventId}/photo",
            description = """Upload event photo."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/event/{eventId}",
        method = [RequestMethod.DELETE],
        beanClass = PublicationService::class,
        beanMethod = "deleteEvent",
        operation = Operation(
            operationId = "DELETE admin/feed/{feedId}/event/{eventId}",
            description = """Delete event."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/event/{eventId}/like",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listLikes",
        operation = Operation(
            operationId = "GET admin/feed/{feedId}/event/{eventId}/like",
            description = """List event likes."""
        )
    ),
    RouterOperation(
        path = "$API_VERSION_1/admin/feed/{feedId}/event/{eventId}/join",
        method = [RequestMethod.GET],
        beanClass = PublicationService::class,
        beanMethod = "listJoins",
        operation = Operation(
            operationId = "GET admin/feed/{feedId}/event/{eventId}/join",
            description = """List event joins."""
        )
    ),
)
annotation class PublicationRoutingDoc