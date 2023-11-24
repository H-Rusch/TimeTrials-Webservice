-- liquibase formatted sql

-- changeset hrusch:2
CREATE TABLE times (
    id SERIAL,
    track VARCHAR NOT NULL,
    time varchar(24) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    user_id INT NOT NULL,

    CONSTRAINT pk_times PRIMARY KEY (id),
    CONSTRAINT fk_times_user_id FOREIGN KEY (user_id) REFERENCES users(id)
)
-- rollback DROP TABLE times;