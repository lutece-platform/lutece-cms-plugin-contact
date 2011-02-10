--
-- Dumping data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'CONTACT_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url) VALUES 
('CONTACT_MANAGEMENT','contact.adminFeature.contact_management.name',3,'jsp/admin/plugins/contact/ManageContactsHome.jsp','contact.adminFeature.contact_management.description',0,'contact','APPLICATIONS','images/admin/skin/plugins/contact/contact.png', NULL);


--
-- Structure of table contact
--

ALTER TABLE contact DROP contact_order;
ALTER TABLE contact ADD COLUMN hits int DEFAULT '0' NOT NULL;

--
-- Structure of contact_list table
--

ALTER TABLE contact_list ADD role varchar(50) DEFAULT 'none' NOT NULL;
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
  PRIMARY KEY (id_contact_list)
);


--
-- Couples of lists and contacts. Permits to join a list and a contact
--

DROP TABLE IF EXISTS contact_list_contact;
CREATE TABLE contact_list_contact (
  id_contact_list int DEFAULT '0'NOT NULL,
  id_contact int DEFAULT '0' NOT NULL,
  contact_order int DEFAULT '0' NOT NULL,
  PRIMARY KEY (id_contact_list,id_contact, contact_order)
);