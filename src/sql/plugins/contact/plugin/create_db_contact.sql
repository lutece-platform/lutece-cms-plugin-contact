--
-- Structure for table contact
--

DROP TABLE IF EXISTS contact;
CREATE TABLE contact (
  id_contact int DEFAULT '0' NOT NULL,
  description varchar(50) DEFAULT '' NOT NULL,
  email varchar(100) DEFAULT '' NOT NULL,
  workgroup_key varchar(50) DEFAULT 'all' NOT NULL,
  hits int DEFAULT '0' NOT NULL,
  PRIMARY KEY  (id_contact)
);

--
-- Structure for table list
--

DROP TABLE IF EXISTS contact_list;
CREATE TABLE contact_list (
  id_contact_list int DEFAULT '0' NOT NULL,
  label_contact_list varchar(50) DEFAULT '' NOT NULL,
  description_contact_list varchar(255) DEFAULT '' NOT NULL,
  workgroup_key varchar(50) DEFAULT 'all' NOT NULL,
  role varchar(50) DEFAULT 'none' NOT NULL,
  contact_list_order int DEFAULT '0' NOT NULL,
  PRIMARY KEY  (id_contact_list)
);


--
-- Couples of lists and contacts. Permits to join a list and a contact
--

DROP TABLE IF EXISTS contact_list_contact;
CREATE TABLE contact_list_contact (
  id_contact_list int DEFAULT '0' NOT NULL,
  id_contact int DEFAULT '0' NOT NULL,
  contact_order int DEFAULT '0' NOT NULL,
  hits int DEFAULT '0' NOT NULL,
  PRIMARY KEY  (id_contact_list,id_contact, contact_order)
);
