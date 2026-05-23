-- liquibase formatted sql

-- changeset Sergalas:1779450844783-1
ALTER TABLE entry_chat
    DROP content

-- changeset Sergalas:1779450844783-2
ALTER TABLE entry_chat
    ADD type_enum LONGTEXT NULL;
