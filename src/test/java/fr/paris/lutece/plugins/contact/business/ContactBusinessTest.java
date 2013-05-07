/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.contact.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;


public class ContactBusinessTest extends LuteceTestCase
{
    private final static String NAME1 = "Contact Name 1";
    private final static String EMAIL1 = "email1@domain.com";
    private final static String NAME2 = "Contact Name 2";
    private final static String EMAIL2 = "email2@domain.com";
    private final static String WORKGROUP1 = "all_1";
    private final static String WORKGROUP2 = "all_2";

    public void testBusinessContact(  )
    {
        Plugin plugin = PluginService.getPlugin( "contact" );

        if ( ( plugin != null ) && plugin.isInstalled(  ) )
        {
            // Initialize an object
            Contact contact = new Contact(  );
            contact.setName( NAME1 );
            contact.setEmail( EMAIL1 );
            contact.setWorkgroup( WORKGROUP1 );

            // Create test
            ContactHome.create( contact, plugin );

            Contact contactStored = ContactHome.findByPrimaryKey( contact.getId(  ), plugin );

            assertEquals( contactStored.getName(  ), contact.getName(  ) );
            assertEquals( contactStored.getEmail(  ), contact.getEmail(  ) );
            assertEquals( contactStored.getWorkgroup(  ), contact.getWorkgroup(  ) );

            // Update test
            contact.setName( NAME2 );
            contact.setEmail( EMAIL2 );
            contact.setWorkgroup( WORKGROUP2 );

            ContactHome.update( contact, plugin );
            contactStored = ContactHome.findByPrimaryKey( contact.getId(  ), plugin );
            assertEquals( contactStored.getName(  ), contact.getName(  ) );
            assertEquals( contactStored.getEmail(  ), contact.getEmail(  ) );
            assertEquals( contactStored.getWorkgroup(  ), contact.getWorkgroup(  ) );

            // Delete test
            ContactHome.remove( contact, plugin );
            contactStored = ContactHome.findByPrimaryKey( contact.getId(  ), plugin );
            assertNull( contactStored );
        }
    }
}
