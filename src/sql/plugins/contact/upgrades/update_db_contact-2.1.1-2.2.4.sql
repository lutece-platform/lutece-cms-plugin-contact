/* N.B.: IF THERES VALUES IN THE CONTACT TABLE YOU SHOULD ADD VALUE IN OTHER PLUGIN TABLES */

ALTER TABLE contact ADD COLUMN workgroup_key varchar(50)  NOT NULL;

CREATE TABLE contact_list
(
  id_contact_list int NOT NULL DEFAULT 0, 
  label_contact_list varchar(50) NOT NULL, 
  description_contact_list varchar(255) NOT NULL, 
  workgroup_key varchar(50) NOT NULL DEFAULT 'all', 
  role varchar(50) NOT NULL DEFAULT 'none'
);


ALTER TABLE contact_list 
ADD CONSTRAINT pk_contact_list PRIMARY KEY 
(
  id_contact_list
);

CREATE TABLE contact_list_contact
(
  id_contact_list int NOT NULL DEFAULT 0, 
  id_contact int NOT NULL DEFAULT 0, 
  contact_order int NOT NULL DEFAULT 0
);


ALTER TABLE contact_list_contact 
ADD CONSTRAINT pk_contact_list_contact PRIMARY KEY 
(
  contact_order, 
  id_contact, 
  id_contact_list
);  
