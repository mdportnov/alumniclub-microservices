# Module app

## Description

The `app` module is the core component of the application that manages the core business logic. It is responsible for
coordinating all services and functionalities, and can exist as a standalone service. Communication with other
microservices is carried out through the RabbitMQ channel.

The module provides essential services such as authentication, user management, community and project management,
news and notifications, loyalty programs, and initiatives, mentoring, and partnership programs.

## Core services

- **Authentication Service** 

    The authentication service is responsible for verifying the identity of the user and granting access to application
    features and functionalities based on their permissions.

- **User Service**

    The user service manages all user-related information for the application. This includes user registration, profile
    management, and user-specific settings.

- **Community and Project Services**

    These services allow users to create, join, and manage various communities and projects.

- **Feed, Publication, and Notification Services**

    These services manage all publications, events, and notifications for the application. They include features like
    creating and managing publications, sending broadcasts to users or communities.

- **Survey Service**

    The survey service allows users to create and participate in polls. It includes features like creating polls with
    multiple options, voting on a poll, and viewing the results.

- **Atom and Merch Services**

    The loyalty program services include features like creating and managing "atoms" (reward points), redeeming points for
    merchandise, and tracking user loyalty points.

- **Form, Mentor, and Partnership Services**

    These services are responsible for user interaction with the platform and the creation of user-generated content through
    initiatives. It includes features like creating and managing initiatives and partnership programs, tracking mentors.

- **Feature toggle service**

    The service allows to regulate the operation of the application without restarting: enable and disable registration,
    limit the number of registrations at a time, enable or disable confirming mail with a letter and sending forms

## Additional functionality

The `app` module provides logging, exception handling, and sets rules for accessing various application resources. This
includes access restrictions for banned users, setting a limit on requests at a certain point in time. 
