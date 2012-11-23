DELETE FROM core_admin_right WHERE id_right='CONTACT_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url) VALUES 
('CONTACT_MANAGEMENT','contact.adminFeature.contact_management.name',3,'jsp/admin/plugins/contact/ManageContacts.jsp','contact.adminFeature.contact_management.description',0,'contact','APPLICATIONS','images/admin/skin/plugins/contact/contact.png', NULL);

