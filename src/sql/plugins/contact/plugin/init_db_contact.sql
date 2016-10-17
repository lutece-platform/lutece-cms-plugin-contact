--
-- Initialization for table contact_list
--
INSERT INTO contact_list(id_contact_list,label_contact_list,description_contact_list,contact_list_order,is_tos_active,tos_message) VALUES (1,'Liste de contacts','Ceci est une liste de contacts',1,0,'');

--
-- Initialization for table contact
--
INSERT INTO contact(id_contact,description,email) VALUES (1,'Contact 1','adresse_email_du_contact_1@domaine.com');
INSERT INTO contact(id_contact,description,email) VALUES (2,'Contact 2','adresse_email_du_contact_2@domaine.com');

--
-- Initialization for table contact_list_contact
--
INSERT INTO contact_list_contact(id_contact,id_contact_list,contact_order) VALUES (1,1,1);
INSERT INTO contact_list_contact(id_contact,id_contact_list,contact_order) VALUES (2,1,2);