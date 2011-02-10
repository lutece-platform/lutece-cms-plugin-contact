/*
 * Copyright ( c ) 2002-2008, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES ( INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION ) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT ( INCLUDING NEGLIGENCE OR OTHERWISE )
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.contact.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;


public class ContactListBusinessTest extends LuteceTestCase
{
    private static final int CONTACTS_NUMBER = 0;
    private static final String DESCRIPTION = "Description";
    private static final String LABEL = "Label";
    private static final String ROLE = "role";
    private static final String WORKGROUP = "workgroup";
    private static final int CONTACTS_NUMBER_2 = 0;
    private static final String DESCRIPTION_2 = "Description 2";
    private static final String LABEL_2 = "Label 2";
    private static final String ROLE_2 = "role 2";
    private static final String WORKGROUP_2 = "workgroup 2";
    private static final int CONTACT_ID = 48;

    public void testBusinessContactList(  )
    {
        Plugin plugin = PluginService.getPlugin( "contact" );

        if ( ( plugin != null ) && plugin.isInstalled(  ) )
        {
            Contact contact = new Contact(  );
            contact.setId( CONTACT_ID );

            ContactList contactList = new ContactList(  );
            contactList.setContactsNumber( CONTACTS_NUMBER );
            contactList.setDescription( DESCRIPTION );
            contactList.setLabel( LABEL );
            contactList.setRole( ROLE );
            contactList.setWorkgroup( WORKGROUP );

            // Create test
            ContactListHome.create( contactList, plugin );

            ContactList contactListStored = ContactListHome.findByPrimaryKey( contactList.getId(  ), plugin );
            assertEquals( contactListStored.getContactsNumber(  ), contactList.getContactsNumber(  ) );
            assertEquals( contactListStored.getDescription(  ), contactList.getDescription(  ) );
            assertEquals( contactListStored.getLabel(  ), contactList.getLabel(  ) );
            assertEquals( contactListStored.getRole(  ), contactList.getRole(  ) );
            assertEquals( contactListStored.getWorkgroup(  ), contactList.getWorkgroup(  ) );

            // Update test
            contactList.setContactsNumber( CONTACTS_NUMBER_2 );
            contactList.setDescription( DESCRIPTION_2 );
            contactList.setLabel( LABEL_2 );
            contactList.setRole( ROLE_2 );
            contactList.setWorkgroup( WORKGROUP_2 );

            ContactListHome.update( contactList, plugin );

            contactListStored = ContactListHome.findByPrimaryKey( contactList.getId(  ), plugin );

            assertEquals( contactListStored.getContactsNumber(  ), contactList.getContactsNumber(  ) );
            assertEquals( contactListStored.getDescription(  ), contactList.getDescription(  ) );
            assertEquals( contactListStored.getLabel(  ), contactList.getLabel(  ) );
            assertEquals( contactListStored.getRole(  ), contactList.getRole(  ) );
            assertEquals( contactListStored.getWorkgroup(  ), contactList.getWorkgroup(  ) );

            //assign test
            ContactListHome.assign( contact.getId(  ), contactList.getId(  ), plugin );

            //getOrder test
            int nOrder = ContactListHome.getContactListOrderById( contactList.getId(  ), plugin );

            //isAssigned test
            if ( ContactListHome.isAssigned( contact.getId(  ), contactList.getId(  ), plugin ) )
            {
                //unAssign test
                ContactListHome.unAssign( contact.getId(  ), contactList.getId(  ), plugin );
            }

            // Delete test
            ContactListHome.remove( contactList.getId(  ), plugin );
            contactListStored = ContactListHome.findByPrimaryKey( contactList.getId(  ), plugin );
            assertNull( contactListStored );
        }
    }
}
