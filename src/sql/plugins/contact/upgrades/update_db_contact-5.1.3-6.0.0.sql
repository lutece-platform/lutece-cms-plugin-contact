-- liquibase formatted sql
-- changeset contact:update_db_contact-5.1.3-6.0.0.sql
-- preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE contact modify COLUMN id_contact int AUTO_INCREMENT;
ALTER TABLE contact_list modify COLUMN id_contact_list int AUTO_INCREMENT;