-- liquibase formatted sql
-- changeset contact:update_db_contact-2.0.13-2.0.14.sql
-- preconditions onFail:MARK_RAN onError:WARN
ALTER TABLE contact_list ADD COLUMN contact_list_order int DEFAULT '0' NOT NULL;