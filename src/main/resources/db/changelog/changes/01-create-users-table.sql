-- liquibase formatted sql

-- changeset hrusch:1
create table users (
    id SERIAL,
    user_id varchar(64) not null unique,
    username varchar(24) not null unique,
    encrypted_password varchar(255) not null,

    CONSTRAINT pk_users PRIMARY KEY (id)
)
-- rollback DROP TABLE users;