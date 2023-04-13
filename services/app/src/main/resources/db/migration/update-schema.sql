USE AlumniClubDev;

CREATE TABLE IF NOT EXISTS affiliate
(
    id    BIGINT AUTO_INCREMENT NOT NULL,
    token VARCHAR(30)           NOT NULL,
    CONSTRAINT pk_affiliate PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS atom
(
    id            char(36)     NOT NULL,
    `description` VARCHAR(120) NOT NULL,
    amount        INT          NOT NULL,
    sign          BIT(1)       NOT NULL,
    user_id       BIGINT       NOT NULL,
    created_at    datetime     NOT NULL,
    CONSTRAINT pk_atom PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bio_visibility
(
    user_id    BIGINT NOT NULL,
    country    BIT(1) NOT NULL,
    city       BIT(1) NOT NULL,
    job_area   BIT(1) NOT NULL,
    company    BIT(1) NOT NULL,
    job        BIT(1) NOT NULL,
    experience BIT(1) NOT NULL,
    hobbies    BIT(1) NOT NULL,
    CONSTRAINT pk_biovisibility PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS biography
(
    user_id    BIGINT       NOT NULL,
    country    VARCHAR(500) NULL,
    city       VARCHAR(500) NULL,
    job_area   VARCHAR(500) NULL,
    company    VARCHAR(500) NULL,
    job        VARCHAR(500) NULL,
    experience VARCHAR(500) NULL,
    hobbies    VARCHAR(500) NULL,
    CONSTRAINT pk_biography PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS broadcast
(
    id          char(36)     NOT NULL,
    title       VARCHAR(300) NOT NULL,
    content     LONGTEXT     NOT NULL,
    views_count BIGINT       NOT NULL,
    photo_path  VARCHAR(100) NULL,
    created_at  datetime     NOT NULL,
    author_id   BIGINT       NULL,
    CONSTRAINT pk_broadcast PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS carousel
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    title      VARCHAR(60)           NOT NULL,
    link       VARCHAR(300)          NULL,
    photo_path VARCHAR(100)          NULL,
    CONSTRAINT pk_carousel PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS community
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    name       VARCHAR(60)           NOT NULL,
    `role`     BIT(1)                NOT NULL,
    hidden     BIT(1)                NOT NULL,
    photo_path VARCHAR(100)          NULL,
    project_id BIGINT                NULL,
    CONSTRAINT pk_community PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS content_photo
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    photo_path VARCHAR(255)          NULL,
    content_id char(36)              NULL,
    CONSTRAINT pk_contentphoto PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS degree
(
    id            BIGINT AUTO_INCREMENT                                                        NOT NULL,
    created_at    datetime                                                                     NOT NULL,
    enrollment    INT                                                                          NOT NULL,
    graduation    INT                                                                          NULL,
    degree        ENUM ('LYCEUM', 'BACHELOR', 'MASTER', 'SPECIALTY', 'POSTGRADUATE', 'WORKER') NOT NULL,
    `description` VARCHAR(500)                                                                 NOT NULL,
    user_id       BIGINT                                                                       NOT NULL,
    CONSTRAINT pk_degree PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS errored_fcm_token
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    errored_push_id BIGINT                NULL,
    token_user_id   BIGINT                NOT NULL,
    push_error      INT                   NOT NULL,
    user_id         BIGINT                NOT NULL,
    fingerprint     VARCHAR(255)          NOT NULL,
    CONSTRAINT pk_erroredfcmtoken PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS errored_push
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    push_id BIGINT                NOT NULL,
    CONSTRAINT pk_erroredpush PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event
(
    id                         char(36)     NOT NULL,
    title                      VARCHAR(300) NOT NULL,
    content                    LONGTEXT     NOT NULL,
    views_count                BIGINT       NOT NULL,
    photo_path                 VARCHAR(100) NULL,
    created_at                 datetime     NOT NULL,
    author_id                  BIGINT       NULL,
    human_url                  VARCHAR(300) NOT NULL,
    time                       datetime     NOT NULL,
    place                      VARCHAR(300) NULL,
    publication_date           datetime     NOT NULL,
    external_registration_link VARCHAR(300) NULL,
    tag                        VARCHAR(60)  NOT NULL,
    is_hidden                  BIT(1)       NOT NULL,
    registration_is_open       BIT(1)       NOT NULL,
    feed_id                    BIGINT       NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS event_join
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT                NOT NULL,
    created_at datetime              NOT NULL,
    event_id   char(36)              NOT NULL,
    CONSTRAINT pk_eventjoin PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS fcm_token
(
    user_id     BIGINT       NOT NULL,
    fingerprint VARCHAR(255) NOT NULL,
    token       VARCHAR(255) NOT NULL,
    is_valid    BIT(1)       NOT NULL,
    update_time datetime     NOT NULL,
    CONSTRAINT pk_fcmtoken PRIMARY KEY (user_id, fingerprint)
);

CREATE TABLE IF NOT EXISTS feature_toggle
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    feature             VARCHAR(60)           NULL,
    feature_description VARCHAR(60)           NULL,
    enabled             BIT(1)                NOT NULL,
    CONSTRAINT pk_featuretoggle PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS feed
(
    id       BIGINT AUTO_INCREMENT                                                                                      NOT NULL,
    type     VARCHAR(31)                                                                                                NULL,
    name     VARCHAR(60)                                                                                                NULL,
    category ENUM ('EVENTS', 'PROJECTS', 'ENDOWMENTS', 'PARTNERSHIPS', 'ACHIEVEMENTS', 'SURVEYS', 'ATOMS', 'BROADCAST') NOT NULL,
    CONSTRAINT pk_feed PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS feed_permission
(
    feed_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_feedpermission PRIMARY KEY (feed_id, user_id)
);

CREATE TABLE IF NOT EXISTS fingerprint
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    device_id  VARCHAR(255)          NOT NULL,
    last_used  datetime              NOT NULL,
    user_id    BIGINT                NOT NULL,
    CONSTRAINT pk_fingerprint PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS form_become_mentor
(
    id                     BIGINT AUTO_INCREMENT                           NOT NULL,
    status                 ENUM ('UNSEEN', 'SEEN', 'ACCEPTED', 'DECLINED') NOT NULL,
    author_id              BIGINT                                          NOT NULL,
    created_at             datetime                                        NOT NULL,
    company                VARCHAR(300)                                    NULL,
    position               VARCHAR(300)                                    NULL,
    expert_area            VARCHAR(300)                                    NULL,
    why_are_you_mentor     VARCHAR(500)                                    NULL,
    help_area              VARCHAR(500)                                    NULL,
    time_for_mentoring     VARCHAR(500)                                    NULL,
    formats_of_interaction VARCHAR(500)                                    NULL,
    CONSTRAINT pk_formbecomementor PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS form_buy_merch
(
    id         BIGINT AUTO_INCREMENT                           NOT NULL,
    status     ENUM ('UNSEEN', 'SEEN', 'ACCEPTED', 'DECLINED') NOT NULL,
    author_id  BIGINT                                          NOT NULL,
    created_at datetime                                        NOT NULL,
    merch_id   BIGINT                                          NOT NULL,
    merch_name VARCHAR(255)                                    NULL,
    CONSTRAINT pk_formbuymerch PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS form_find_mentor
(
    id            BIGINT AUTO_INCREMENT                           NOT NULL,
    status        ENUM ('UNSEEN', 'SEEN', 'ACCEPTED', 'DECLINED') NOT NULL,
    author_id     BIGINT                                          NOT NULL,
    created_at    datetime                                        NOT NULL,
    motivation    VARCHAR(500)                                    NULL,
    `description` VARCHAR(500)                                    NULL,
    targets       VARCHAR(500)                                    NOT NULL,
    mentor_id     BIGINT                                          NOT NULL,
    mentor_name   VARCHAR(255)                                    NULL,
    CONSTRAINT pk_formfindmentor PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS form_join_partnership
(
    id             BIGINT AUTO_INCREMENT                           NOT NULL,
    status         ENUM ('UNSEEN', 'SEEN', 'ACCEPTED', 'DECLINED') NOT NULL,
    author_id      BIGINT                                          NOT NULL,
    created_at     datetime                                        NOT NULL,
    contribution   VARCHAR(500)                                    NOT NULL,
    partnership_id BIGINT                                          NOT NULL,
    CONSTRAINT pk_formjoinpartnership PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS form_offer_community
(
    id         BIGINT AUTO_INCREMENT                           NOT NULL,
    status     ENUM ('UNSEEN', 'SEEN', 'ACCEPTED', 'DECLINED') NOT NULL,
    author_id  BIGINT                                          NOT NULL,
    created_at datetime                                        NOT NULL,
    text       VARCHAR(500)                                    NOT NULL,
    CONSTRAINT pk_formoffercommunity PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS form_offer_partnership
(
    id                  BIGINT AUTO_INCREMENT                           NOT NULL,
    status              ENUM ('UNSEEN', 'SEEN', 'ACCEPTED', 'DECLINED') NOT NULL,
    author_id           BIGINT                                          NOT NULL,
    created_at          datetime                                        NOT NULL,
    requirements        VARCHAR(300)                                    NOT NULL,
    self_description    VARCHAR(500)                                    NULL,
    name                VARCHAR(300)                                    NOT NULL,
    current_until       datetime                                        NOT NULL,
    project_description VARCHAR(500)                                    NOT NULL,
    help_description    VARCHAR(500)                                    NULL,
    CONSTRAINT pk_formofferpartnership PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS form_offer_poll
(
    id         BIGINT AUTO_INCREMENT                           NOT NULL,
    status     ENUM ('UNSEEN', 'SEEN', 'ACCEPTED', 'DECLINED') NOT NULL,
    author_id  BIGINT                                          NOT NULL,
    created_at datetime                                        NOT NULL,
    text       VARCHAR(500)                                    NOT NULL,
    CONSTRAINT pk_formofferpoll PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS likes
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    user_id        BIGINT                NOT NULL,
    created_at     datetime              NOT NULL,
    publication_id char(36)              NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS meeting_participation
(
    join_id           BIGINT                     NOT NULL,
    format            ENUM ('ONLINE', 'OFFLINE') NOT NULL,
    department_meetup BIT(1)                     NOT NULL,
    movie             BIT(1)                     NOT NULL,
    presentations     BIT(1)                     NOT NULL,
    exhibition        BIT(1)                     NOT NULL,
    greetings         BIT(1)                     NOT NULL,
    enjoy             BIT(1)                     NOT NULL,
    performance       BIT(1)                     NOT NULL,
    help              BIT(1)                     NOT NULL,
    telegram          BIT(1)                     NOT NULL,
    CONSTRAINT pk_meetingparticipation PRIMARY KEY (join_id)
);

CREATE TABLE IF NOT EXISTS mentor_data
(
    user_id                 BIGINT       NOT NULL,
    company                 VARCHAR(500) NULL,
    position                VARCHAR(500) NULL,
    expert_area             VARCHAR(500) NOT NULL,
    why_are_you_mentor      VARCHAR(500) NULL,
    graduation              VARCHAR(300) NULL,
    formats_of_interactions VARCHAR(500) NOT NULL,
    tags                    VARCHAR(500) NULL,
    interview_link          VARCHAR(120) NULL,
    available               BIT(1)       NOT NULL,
    created_at              datetime     NOT NULL,
    CONSTRAINT pk_mentordata PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS merch
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(60)           NOT NULL,
    `description` VARCHAR(500)          NOT NULL,
    cost          INT                   NOT NULL,
    availability  BIT(1)                NOT NULL,
    photo_path    VARCHAR(100)          NULL,
    CONSTRAINT pk_merch PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS notification
(
    id             BIGINT AUTO_INCREMENT                NOT NULL,
    created_at     datetime                             NOT NULL,
    broadcast_type ENUM ('USERS', 'COMMUNITIES', 'ALL') NOT NULL,
    publication_id char(36)                             NOT NULL,
    CONSTRAINT pk_notification PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS notification_receivers
(
    notification_id BIGINT NOT NULL,
    receiver_id     BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS partnership
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    created_at          datetime              NOT NULL,
    requirements        VARCHAR(300)          NOT NULL,
    self_description    VARCHAR(500)          NULL,
    name                VARCHAR(300)          NOT NULL,
    current_until       datetime              NOT NULL,
    project_description VARCHAR(500)          NOT NULL,
    help_description    VARCHAR(500)          NULL,
    color               VARCHAR(6)            NOT NULL,
    tag                 VARCHAR(300)          NOT NULL,
    photo_path          VARCHAR(100)          NULL,
    author_id           BIGINT                NOT NULL,
    CONSTRAINT pk_partnership PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS partnership_join
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    created_at     datetime              NOT NULL,
    contribution   VARCHAR(500)          NOT NULL,
    user_id        BIGINT                NOT NULL,
    partnership_id BIGINT                NOT NULL,
    CONSTRAINT pk_partnershipjoin PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS project
(
    id                  BIGINT AUTO_INCREMENT NOT NULL,
    type                VARCHAR(31)           NULL,
    created_at          datetime              NOT NULL,
    name                VARCHAR(60)           NOT NULL,
    `description`       VARCHAR(500)          NOT NULL,
    archive             BIT(1)                NOT NULL,
    photo_path          VARCHAR(100)          NULL,
    color               VARCHAR(6)            NULL,
    publication_feed_id BIGINT                NOT NULL,
    community_id        BIGINT                NOT NULL,
    event_feed_id       BIGINT                NULL,
    CONSTRAINT pk_project PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS project_permission
(
    project_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    CONSTRAINT pk_projectpermission PRIMARY KEY (project_id, user_id)
);

CREATE TABLE IF NOT EXISTS publication
(
    id               char(36)     NOT NULL,
    title            VARCHAR(300) NOT NULL,
    content          LONGTEXT     NOT NULL,
    views_count      BIGINT       NOT NULL,
    photo_path       VARCHAR(100) NULL,
    created_at       datetime     NOT NULL,
    author_id        BIGINT       NULL,
    publication_date datetime     NOT NULL,
    human_url        VARCHAR(300) NOT NULL,
    is_hidden        BIT(1)       NOT NULL,
    feed_id          BIGINT       NOT NULL,
    CONSTRAINT pk_publication PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS push_notification
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    title      VARCHAR(60)           NOT NULL,
    text       VARCHAR(300)          NOT NULL,
    type       INT                   NOT NULL,
    status     INT                   NOT NULL,
    CONSTRAINT pk_pushnotification PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS push_notification_receivers
(
    push_id     BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS referral_user
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    affiliate_id BIGINT                NOT NULL,
    referral_id  BIGINT                NOT NULL,
    status       INT                   NOT NULL,
    CONSTRAINT pk_referraluser PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS refresh_token
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    expires_at     datetime              NOT NULL,
    hash           VARCHAR(60)           NOT NULL,
    fingerprint_id BIGINT                NOT NULL,
    CONSTRAINT pk_refreshtoken PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS reset_password_token
(
    user_id    BIGINT      NOT NULL,
    created_at datetime    NOT NULL,
    token      VARCHAR(20) NOT NULL,
    CONSTRAINT pk_resetpasswordtoken PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS survey
(
    id          char(36)     NOT NULL,
    title       VARCHAR(300) NOT NULL,
    content     LONGTEXT     NOT NULL,
    views_count BIGINT       NOT NULL,
    photo_path  VARCHAR(100) NULL,
    created_at  datetime     NOT NULL,
    author_id   BIGINT       NULL,
    ends_at     datetime     NOT NULL,
    allow_count INT          NOT NULL,
    type        INT          NOT NULL,
    CONSTRAINT pk_survey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS survey_free_answer
(
    survey_answer_id BIGINT       NOT NULL,
    free_answer      VARCHAR(500) NOT NULL,
    CONSTRAINT pk_surveyfreeanswer PRIMARY KEY (survey_answer_id)
);

CREATE TABLE IF NOT EXISTS survey_item_answer
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    survey_id char(36)              NOT NULL,
    answer    VARCHAR(60)           NOT NULL,
    CONSTRAINT pk_surveyitemanswer PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS survey_selected_answers
(
    id               BIGINT AUTO_INCREMENT NOT NULL,
    survey_answer_id BIGINT                NOT NULL,
    item_answer_id   BIGINT                NOT NULL,
    CONSTRAINT pk_surveyselectedanswers PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS survey_user_answer
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    survey_id  char(36)              NULL,
    user_id    BIGINT                NULL,
    CONSTRAINT pk_surveyuseranswer PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NOT NULL,
    email      VARCHAR(120)          NOT NULL,
    phone      VARCHAR(15)           NULL,
    vk         VARCHAR(60)           NULL,
    tg         VARCHAR(60)           NULL,
    name       VARCHAR(30)           NOT NULL,
    surname    VARCHAR(30)           NOT NULL,
    patronymic VARCHAR(30)           NULL,
    gender     BIT(1)                NOT NULL,
    birthday   date                  NOT NULL,
    photo_path VARCHAR(100)          NULL,
    banned     BIT(1)                NOT NULL,
    lyceum     BIT(1)                NOT NULL,
    student    BIT(1)                NOT NULL,
    alumnus    BIT(1)                NOT NULL,
    worker     BIT(1)                NOT NULL,
    mentor     BIT(1)                NOT NULL,
    `admin`    BIT(1)                NOT NULL,
    moderator  BIT(1)                NOT NULL,
    hash       VARCHAR(60)           NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_activity
(
    user_id BIGINT   NOT NULL,
    time    datetime NOT NULL,
    CONSTRAINT pk_useractivity PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS user_notification
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    notification_id BIGINT                NOT NULL,
    user_id         BIGINT                NOT NULL,
    is_read         BIT(1)                NOT NULL,
    CONSTRAINT pk_usernotification PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS user_permission
(
    user_id    BIGINT                                                                                                                                                NOT NULL,
    permission ENUM ('FULL', 'USERS_VIEW', 'USERS_BAN', 'COMMUNITIES', 'PROJECTS_CREATE', 'PROJECTS_ALL', 'FEEDS', 'MENTORS', 'FORMS', 'POLLS', 'CAROUSEL', 'ATOMS') NOT NULL,
    CONSTRAINT pk_userpermission PRIMARY KEY (user_id, permission)
);

CREATE TABLE IF NOT EXISTS user_preferences
(
    user_id      BIGINT NOT NULL,
    enable_push  BIT(1) NOT NULL,
    enable_email BIT(1) NOT NULL,
    CONSTRAINT pk_userpreferences PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS user_visibility
(
    user_id    BIGINT NOT NULL,
    email      BIT(1) NOT NULL,
    phone      BIT(1) NOT NULL,
    vk         BIT(1) NOT NULL,
    tg         BIT(1) NOT NULL,
    gender     BIT(1) NOT NULL,
    birthday   BIT(1) NOT NULL,
    degrees    BIT(1) NOT NULL,
    created_at BIT(1) NOT NULL,
    CONSTRAINT pk_uservisibility PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS users_communities
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NOT NULL,
    user_id      BIGINT                NULL,
    community_id BIGINT                NULL,
    CONSTRAINT pk_userscommunities PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS value_table
(
    id    VARCHAR(30) NOT NULL,
    value VARCHAR(60) NULL,
    CONSTRAINT pk_valuetable PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS verify_email_token
(
    user_id    BIGINT       NOT NULL,
    created_at datetime     NOT NULL,
    token      VARCHAR(300) NOT NULL,
    CONSTRAINT pk_verifyemailtoken PRIMARY KEY (user_id)
);

ALTER TABLE affiliate
    ADD CONSTRAINT uc_affiliate_token UNIQUE (token);

ALTER TABLE event
    ADD CONSTRAINT uc_event_humanurl UNIQUE (human_url);

ALTER TABLE feature_toggle
    ADD CONSTRAINT uc_featuretoggle_feature UNIQUE (feature);

ALTER TABLE publication
    ADD CONSTRAINT uc_publication_humanurl UNIQUE (human_url);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

CREATE INDEX idx_17cc63c655fb8ce3320dfa11a ON content_photo (photo_path);

CREATE INDEX idx_f9526803ed01e78f7bce6822e ON likes (publication_id);

ALTER TABLE affiliate
    ADD CONSTRAINT FK_AFFILIATE_ON_ID FOREIGN KEY (id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE atom
    ADD CONSTRAINT FK_ATOM_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE biography
    ADD CONSTRAINT FK_BIOGRAPHY_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE bio_visibility
    ADD CONSTRAINT FK_BIOVISIBILITY_ON_USERID FOREIGN KEY (user_id) REFERENCES biography (user_id) ON DELETE CASCADE;

ALTER TABLE broadcast
    ADD CONSTRAINT FK_BROADCAST_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id);

ALTER TABLE community
    ADD CONSTRAINT FK_COMMUNITY_ON_PROJECTID FOREIGN KEY (project_id) REFERENCES project (id);

ALTER TABLE degree
    ADD CONSTRAINT FK_DEGREE_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE errored_fcm_token
    ADD CONSTRAINT FK_ERROREDFCMTOKEN_ON_ERROREDPUSHID FOREIGN KEY (errored_push_id) REFERENCES errored_push (id);

ALTER TABLE errored_fcm_token
    ADD CONSTRAINT FK_ERROREDFCMTOKEN_ON_USFI FOREIGN KEY (user_id, fingerprint) REFERENCES fcm_token (user_id, fingerprint) ON DELETE CASCADE;

ALTER TABLE errored_push
    ADD CONSTRAINT FK_ERROREDPUSH_ON_PUSHID FOREIGN KEY (push_id) REFERENCES push_notification (id);

ALTER TABLE event_join
    ADD CONSTRAINT FK_EVENTJOIN_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE event
    ADD CONSTRAINT FK_EVENT_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id);

ALTER TABLE event
    ADD CONSTRAINT FK_EVENT_ON_FEEDID FOREIGN KEY (feed_id) REFERENCES feed (id) ON DELETE CASCADE;

ALTER TABLE feed_permission
    ADD CONSTRAINT FK_FEEDPERMISSION_ON_FEEDID FOREIGN KEY (feed_id) REFERENCES feed (id) ON DELETE CASCADE;

ALTER TABLE feed_permission
    ADD CONSTRAINT FK_FEEDPERMISSION_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE fingerprint
    ADD CONSTRAINT FK_FINGERPRINT_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE form_become_mentor
    ADD CONSTRAINT FK_FORMBECOMEMENTOR_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE form_buy_merch
    ADD CONSTRAINT FK_FORMBUYMERCH_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE form_buy_merch
    ADD CONSTRAINT FK_FORMBUYMERCH_ON_MERCHID FOREIGN KEY (merch_id) REFERENCES merch (id) ON DELETE CASCADE;

ALTER TABLE form_find_mentor
    ADD CONSTRAINT FK_FORMFINDMENTOR_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE form_find_mentor
    ADD CONSTRAINT FK_FORMFINDMENTOR_ON_MENTORID FOREIGN KEY (mentor_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE form_join_partnership
    ADD CONSTRAINT FK_FORMJOINPARTNERSHIP_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE form_join_partnership
    ADD CONSTRAINT FK_FORMJOINPARTNERSHIP_ON_PARTNERSHIPID FOREIGN KEY (partnership_id) REFERENCES partnership (id) ON DELETE CASCADE;

ALTER TABLE form_offer_community
    ADD CONSTRAINT FK_FORMOFFERCOMMUNITY_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE form_offer_partnership
    ADD CONSTRAINT FK_FORMOFFERPARTNERSHIP_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE form_offer_poll
    ADD CONSTRAINT FK_FORMOFFERPOLL_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE likes
    ADD CONSTRAINT FK_LIKES_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE meeting_participation
    ADD CONSTRAINT FK_MEETINGPARTICIPATION_ON_JOINID FOREIGN KEY (join_id) REFERENCES event_join (id) ON DELETE CASCADE;

ALTER TABLE mentor_data
    ADD CONSTRAINT FK_MENTORDATA_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE partnership_join
    ADD CONSTRAINT FK_PARTNERSHIPJOIN_ON_PARTNERSHIPID FOREIGN KEY (partnership_id) REFERENCES partnership (id) ON DELETE CASCADE;

ALTER TABLE partnership_join
    ADD CONSTRAINT FK_PARTNERSHIPJOIN_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE partnership
    ADD CONSTRAINT FK_PARTNERSHIP_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE project_permission
    ADD CONSTRAINT FK_PROJECTPERMISSION_ON_PROJECTID FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE;

ALTER TABLE project_permission
    ADD CONSTRAINT FK_PROJECTPERMISSION_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE project
    ADD CONSTRAINT FK_PROJECT_ON_COMMUNITYID FOREIGN KEY (community_id) REFERENCES community (id);

ALTER TABLE project
    ADD CONSTRAINT FK_PROJECT_ON_EVENTFEEDID FOREIGN KEY (event_feed_id) REFERENCES feed (id);

ALTER TABLE project
    ADD CONSTRAINT FK_PROJECT_ON_PUBLICATIONFEEDID FOREIGN KEY (publication_feed_id) REFERENCES feed (id);

ALTER TABLE publication
    ADD CONSTRAINT FK_PUBLICATION_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id);

ALTER TABLE publication
    ADD CONSTRAINT FK_PUBLICATION_ON_FEEDID FOREIGN KEY (feed_id) REFERENCES feed (id) ON DELETE CASCADE;

ALTER TABLE referral_user
    ADD CONSTRAINT FK_REFERRALUSER_ON_AFFILIATEID FOREIGN KEY (affiliate_id) REFERENCES affiliate (id) ON DELETE CASCADE;

ALTER TABLE referral_user
    ADD CONSTRAINT FK_REFERRALUSER_ON_REFERRALID FOREIGN KEY (referral_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE refresh_token
    ADD CONSTRAINT FK_REFRESHTOKEN_ON_FINGERPRINTID FOREIGN KEY (fingerprint_id) REFERENCES fingerprint (id) ON DELETE CASCADE;

ALTER TABLE reset_password_token
    ADD CONSTRAINT FK_RESETPASSWORDTOKEN_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE survey_free_answer
    ADD CONSTRAINT FK_SURVEYFREEANSWER_ON_SURVEYANSWERID FOREIGN KEY (survey_answer_id) REFERENCES survey_user_answer (id) ON DELETE CASCADE;

ALTER TABLE survey_item_answer
    ADD CONSTRAINT FK_SURVEYITEMANSWER_ON_SURVEYID FOREIGN KEY (survey_id) REFERENCES survey (id) ON DELETE CASCADE;

ALTER TABLE survey_selected_answers
    ADD CONSTRAINT FK_SURVEYSELECTEDANSWERS_ON_ITEMANSWERID FOREIGN KEY (item_answer_id) REFERENCES survey_item_answer (id) ON DELETE CASCADE;

ALTER TABLE survey_selected_answers
    ADD CONSTRAINT FK_SURVEYSELECTEDANSWERS_ON_SURVEYANSWERID FOREIGN KEY (survey_answer_id) REFERENCES survey_user_answer (id) ON DELETE CASCADE;

ALTER TABLE survey_user_answer
    ADD CONSTRAINT FK_SURVEYUSERANSWER_ON_SURVEYID FOREIGN KEY (survey_id) REFERENCES survey (id) ON DELETE CASCADE;

ALTER TABLE survey_user_answer
    ADD CONSTRAINT FK_SURVEYUSERANSWER_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE survey
    ADD CONSTRAINT FK_SURVEY_ON_AUTHORID FOREIGN KEY (author_id) REFERENCES user (id);

ALTER TABLE user_activity
    ADD CONSTRAINT FK_USERACTIVITY_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE user_notification
    ADD CONSTRAINT FK_USERNOTIFICATION_ON_NOTIFICATIONID FOREIGN KEY (notification_id) REFERENCES notification (id) ON DELETE CASCADE;

ALTER TABLE user_notification
    ADD CONSTRAINT FK_USERNOTIFICATION_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE user_permission
    ADD CONSTRAINT FK_USERPERMISSION_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE user_preferences
    ADD CONSTRAINT FK_USERPREFERENCES_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE users_communities
    ADD CONSTRAINT FK_USERSCOMMUNITIES_ON_COMMUNITYID FOREIGN KEY (community_id) REFERENCES community (id) ON DELETE CASCADE;

ALTER TABLE users_communities
    ADD CONSTRAINT FK_USERSCOMMUNITIES_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE user_visibility
    ADD CONSTRAINT FK_USERVISIBILITY_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE verify_email_token
    ADD CONSTRAINT FK_VERIFYEMAILTOKEN_ON_USERID FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE;

ALTER TABLE notification_receivers
    ADD CONSTRAINT fk_notificationreceivers_on_notification FOREIGN KEY (notification_id) REFERENCES notification (id);

ALTER TABLE push_notification_receivers
    ADD CONSTRAINT fk_pushnotificationreceivers_on_push_notification FOREIGN KEY (push_id) REFERENCES push_notification (id);