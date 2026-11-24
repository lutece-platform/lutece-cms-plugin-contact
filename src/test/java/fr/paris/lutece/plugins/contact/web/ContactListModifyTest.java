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

import java.util.HashMap;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.contact.business.ContactList;
import fr.paris.lutece.plugins.contact.business.ContactListHome;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import jakarta.enterprise.inject.spi.CDI;

/**
 * Modification of a contact list: the description is mandatory, as on the creation
 */
public class ContactListModifyTest extends LuteceTestCase
{
    private Plugin _plugin;
    private ContactList _list;

    @BeforeEach
    public void createList( )
    {
        _plugin = PluginService.getPlugin( "contact" );
        _list = new ContactList( );
        _list.setLabel( "ContactListModifyTest" );
        _list.setDescription( "description" );
        _list.setWorkgroup( "all" );
        _list.setRole( ContactList.ROLE_NONE );
        _list.setTosMessage( "" );
        ContactListHome.create( _list, _plugin );
    }

    @AfterEach
    public void removeList( )
    {
        ContactListHome.remove( _list.getId( ), _plugin );
    }

    @Test
    public void testEmptyDescriptionIsRefused( ) throws AccessDeniedException
    {
        modify( "" );

        assertEquals( "description", ContactListHome.findByPrimaryKey( _list.getId( ), _plugin ).getDescription( ) );
    }

    @Test
    public void testDescriptionIsSaved( ) throws AccessDeniedException
    {
        modify( "new description" );

        assertEquals( "new description", ContactListHome.findByPrimaryKey( _list.getId( ), _plugin ).getDescription( ) );
    }

    private void modify( String strDescription ) throws AccessDeniedException
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( new HashMap<String, RBACRole>( ) );
        user.setLocale( Locale.getDefault( ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, "actionModifyContactList" );
        request.addParameter( "id_contact_list", String.valueOf( _list.getId( ) ) );
        request.addParameter( "contact_list_label", "ContactListModifyTest" );
        request.addParameter( "contact_list_description", strDescription );
        request.addParameter( "workgroup", "all" );
        request.addParameter( "role", ContactList.ROLE_NONE );
        request.addParameter( "tos_message", "" );

        CDI.current( ).select( ContactListJspBean.class ).get( ).processController( request, new MockHttpServletResponse( ) );
    }
}
