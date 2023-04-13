BEGIN;

INSERT IGNORE INTO User (createdAt, admin, banned, surname, name, patronymic, birthday, email, gender, hash,
                         lyceum, mentor, moderator, student, alumnus, worker, phone, photoPath, tg, vk)
VALUES (NOW(), true, false, 'Adminov', 'Admin', 'Adminovich', NOW(), 'admin@mail.ru', true, '',
        true, true, true, true, true, true, '', '', '', '');

COMMIT;