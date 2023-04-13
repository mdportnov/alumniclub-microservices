# Project Structure

This project has the following directory structure:

**app**:<br>
The App module is the core component of the application that manages the core business logic. It is responsible for
coordinating all services and functionalities, and can exist as a standalone service. Communication with other
microservices is carried out through the RabbitMQ channel.
<br>

The module provides essential services such as authentication, user management, community and project management,
news and notifications, loyalty programs, and initiatives, mentoring, and partnership programs.

**broadcast-service**:<br>
This service is used to create email broadcasts. Data for mailings is transmitted via the RabbitMQ queue.

Used to create such mailings:

- A letter about a new publication
- A letter to confirm registration
- Password reset email
- Broadcast from the administration

**image-service**:<br>
The image service is used to work with images. Data is transmitted via the RabbitMQ queue.

This service is used for such manipulations with pictures:

- Saving images to the local file storage
- Compressing images
- Cleaning unnecessary images

**recommendation-service**:<br>
This service is used to implement a recommendation system based on the Word2Vec algorithm. Recommendations are selected
based on feedback from users. Moderators can manually send a request to recalculate the weights to improve the
recommendation system.

**shared**:<br>
Provides common utilities for other services. Contains utility classes used throughout the application. It includes
common constants, enums, extensions, models and response-related utilities.

**buildImagesAndRun.sh**:<br>
This file is a convenience script to build the Docker images and run the services.

**docs**:<br>
This directory contains documentation related to the project generated with Dokka.

**config**:<br>
This directory contains configuration files used by the application.

<br>
All of the services are containerized using Docker and can be run using docker-compose. The buildImagesAndRun.sh script
is a convenience script that builds the Docker images for the services and starts them up using docker-compose.