/*
 * Copyright (c) 2002-2021, City of Paris
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
package fr.paris.lutece.plugins.contact.web;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.contact.business.Contact;
import fr.paris.lutece.plugins.contact.business.ContactHome;
import fr.paris.lutece.plugins.contact.business.ContactList;
import fr.paris.lutece.plugins.contact.business.ContactListHome;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.web.LocalVariables;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.spi.CDI;

/**
 * Sending of a message from the front office form of a contact list
 */
public class ContactAppSendMessageTest extends LuteceTestCase
{
    private Plugin _plugin;
    private Contact _contactInList;
    private Contact _contactOutOfList;
    private ContactList _list;

    @BeforeEach
    public void createData( )
    {
        _plugin = PluginService.getPlugin( "contact" );
        _contactInList = createContact( "ContactAppSendMessageTest in" );
        _contactOutOfList = createContact( "ContactAppSendMessageTest out" );
        _list = new ContactList( );
        _list.setLabel( "ContactAppSendMessageTest" );
        _list.setDescription( "description" );
        _list.setWorkgroup( "all" );
        _list.setRole( ContactList.ROLE_NONE );
        _list.setTosMessage( "" );
        ContactListHome.create( _list, _plugin );
        ContactListHome.assign( _contactInList.getId( ), _list.getId( ), _plugin );
    }

    @AfterEach
    public void removeData( )
    {
        ContactListHome.unassignContactsForList( _list.getId( ), _plugin );
        ContactListHome.remove( _list.getId( ), _plugin );
        ContactHome.remove( _contactInList, _plugin );
        ContactHome.remove( _contactOutOfList, _plugin );
        LocalVariables.setLocal( null, null, null );
    }

    /**
     * Control: a contact of the list receives the message
     */
    @Test
    public void testSendToAContactOfTheList( ) throws SiteMessageException
    {
        String strLocation = send( String.valueOf( _list.getId( ) ), String.valueOf( _contactInList.getId( ) ) );

        assertTrue( strLocation, strLocation.contains( "send=done" ) );
    }

    /**
     * A contact that does not belong to the list of the form is refused as a missing recipient, and gets no message
     */
    @Test
    public void testSendToAContactOutOfTheList( ) throws SiteMessageException
    {
        String strLocation = send( String.valueOf( _list.getId( ) ), String.valueOf( _contactOutOfList.getId( ) ) );

        assertTrue( strLocation, strLocation.contains( "send=error_recipient" ) );
    }

    /**
     * On an error, the input given back to the form in the URL of the redirect is encoded: a # or a & does not cut it
     */
    @Test
    public void testErrorRedirectKeepsTheInput( ) throws SiteMessageException
    {
        String strLocation = send( String.valueOf( _list.getId( ) ), String.valueOf( _contactInList.getId( ) ), "pas-une-adresse", "Dossier #4521 & Co" );

        assertTrue( strLocation, strLocation.contains( "send=error_email" ) );
        assertFalse( strLocation, strLocation.contains( "#" ) );
        assertTrue( strLocation, strLocation.contains( "message_object=Dossier+%234521+%26+Co" ) );
    }

    private String send( String strIdContactList, String strIdContact ) throws SiteMessageException
    {
        return send( strIdContactList, strIdContact, "jean.dupont@example.org", "object" );
    }

    private String send( String strIdContactList, String strIdContact, String strEmail, String strObject ) throws SiteMessageException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        RedirectResponse response = new RedirectResponse( );
        LocalVariables.setLocal( null, request, response );
        if ( strIdContactList != null )
        {
            request.addParameter( "id_contact_list", strIdContactList );
        }
        request.addParameter( "contact", strIdContact );
        request.addParameter( "visitor_last_name", "Dupont" );
        request.addParameter( "visitor_first_name", "Jean" );
        request.addParameter( "visitor_email", strEmail );
        request.addParameter( "message_object", strObject );
        request.addParameter( "message", "message" );

        CDI.current( ).select( ContactApp.class ).get( ).doSendMessage( request );

        assertNotNull( response._strLocation );
        return response._strLocation;
    }

    private Contact createContact( String strName )
    {
        Contact contact = new Contact( );
        contact.setName( strName );
        contact.setEmail( "contact@example.org" );
        contact.setWorkgroup( "all" );
        ContactHome.create( contact, _plugin );
        return contact;
    }

    /**
     * Response that keeps the location of the redirect
     */
    static class RedirectResponse extends MockHttpServletResponse
    {
        String _strLocation;

        @Override
        public void sendRedirect( String strLocation ) throws IOException
        {
            _strLocation = strLocation;
        }
    }
}
