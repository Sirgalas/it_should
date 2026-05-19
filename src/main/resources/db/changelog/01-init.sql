-- liquibase formatted sql

-- changeset Sergalas:1779188885447-1
CREATE TABLE chat
(
    id         UUID NOT NULL,
    title      VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_chat PRIMARY KEY (id)
);

-- changeset Sergalas:1779188885447-2
CREATE TABLE entry_chat
(
    id         UUID NOT NULL,
    content    VARCHAR(255),
    role_enum  VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE,
    chat_id    UUID,
    CONSTRAINT pk_entry_chat PRIMARY KEY (id)
);

-- changeset Sergalas:1779188885447-3
ALTER TABLE entry_chat
    ADD CONSTRAINT FK_ENTRY_CHAT_ON_CHAT
        FOREIGN KEY (chat_id)
            REFERENCES chat (id)
    ON DELETE CASCADE
    ON UPDATE RESTRICT ;

