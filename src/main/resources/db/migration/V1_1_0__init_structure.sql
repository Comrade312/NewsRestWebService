create table app_user
(
    id       bigserial    not null,
    username varchar(255) not null,
    password varchar(255) not null,
    active   boolean      not null,
    primary key (id)
);

create table user_role
(
    user_id bigint not null,
    roles   varchar(255),
    CONSTRAINT fk_user_role_user
        FOREIGN KEY (user_id)
            REFERENCES app_user (id)

);

CREATE TABLE NEWS
(
    id      BIGSERIAL    not null,
    date    TIMESTAMP    not null,
    title   varchar(255) not null,
    text    varchar(255) not null,
    user_id BIGINT       not null,
    primary key (id),
    CONSTRAINT fk_news_app_user
        FOREIGN KEY (user_id)
            REFERENCES app_user (id)

);

CREATE TABLE COMMENT
(
    id      BIGSERIAL    not null,
    date    timestamp    NOT NULL,
    text    varchar(255) not null,
    user_id BIGINT       not null,
    news_id BIGINT       not null,
    primary key (id),
    CONSTRAINT fk_comment_app_user
        FOREIGN KEY (user_id)
            REFERENCES app_user (id),
    CONSTRAINT fk_comment_news
        FOREIGN KEY (news_id)
            REFERENCES NEWS (id)
);




