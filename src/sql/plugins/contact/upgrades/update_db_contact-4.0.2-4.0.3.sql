ALTER TABLE contact_list ADD COLUMN is_tos_active smallint DEFAULT '0' NOT NULL;
ALTER TABLE contact_list ADD COLUMN tos_message long varchar NOT NULL;
