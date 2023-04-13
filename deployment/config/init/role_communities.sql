DROP TABLE IF EXISTS roles_names;
CREATE TEMPORARY TABLE roles_names
(
    name VARCHAR(255)
);

INSERT INTO roles_names (name)
VALUES ('Лицеисты'),
       ('Студенты'),
       ('Выпускники'),
       ('Сотрудники'),
       ('Наставники'),
       ('Администраторы'),
       ('Модераторы');

CREATE TABLE IF NOT EXISTS Community
(
    id        bigint      not null auto_increment,
    createdAt datetime(6) not null,
    hidden    bit         not null,
    name      varchar(60) not null,
    photoPath varchar(100),
    role      bit         not null,
    projectId bigint,
    primary key (id)
);
INSERT INTO Community (createdAt, hidden, name, photoPath, role, projectId)
SELECT CURRENT_TIMESTAMP(), false, roles_names.name, NULL, true, NULL
FROM roles_names
WHERE NOT EXISTS
    (SELECT * FROM Community WHERE name = roles_names.name);
