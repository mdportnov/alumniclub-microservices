package ru.mephi.alumniclub.app.database.entity.outbox

enum class OutboxMessageStatus {
    /**
     * The program saved the message to a table and right now is trying to send it to a RabbitMQ for the first time.
     */
    PENDING,

    /**
     * The program has already tried at least once to send a message to the RabbitMQ, but it failed due to an error.
     */
    ERRORED,

    /**
     * Message was successfully send to RabbitMQ.
     */
    DELIVERED
}