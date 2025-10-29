-- liquibase formatted sql
-- changeset contact:update_db_contact-4.0.2-4.0.3.sql
-- preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE contact_list ADD COLUMN is_tos_active smallint DEFAULT '0' NOT NULL;
ALTER TABLE contact_list ADD COLUMN tos_message long varchar NOT NULL;
