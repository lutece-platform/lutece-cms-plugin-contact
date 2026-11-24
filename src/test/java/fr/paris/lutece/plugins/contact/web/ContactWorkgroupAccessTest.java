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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.contact.business.Contact;
import fr.paris.lutece.plugins.contact.business.ContactHome;
import fr.paris.lutece.plugins.contact.business.ContactList;
import fr.paris.lutece.plugins.contact.business.ContactListHome;
import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.enterprise.inject.spi.CDI;

/**
 * The contacts and the lists of a workgroup are reached by their id only by the members of the workgroup
 */
public class ContactWorkgroupAccessTest extends LuteceTestCase
{
    private static final String WORKGROUP = "ContactWorkgroupAccessTest";

    private Plugin _plugin;
    private Contact _contact;
    private ContactList _list;

    @BeforeEach
    public void createData( )
    {
        _plugin = PluginService.getPlugin( "contact" );
        _contact = new Contact( );
        _contact.setName( "ContactWorkgroupAccessTest" );
        _contact.setEmail( "contact@example.org" );
        _contact.setWorkgroup( WORKGROUP );
        ContactHome.create( _contact, _plugin );
        _list = new ContactList( );
        _list.setLabel( "ContactWorkgroupAccessTest" );
        _list.setDescription( "description" );
        _list.setWorkgroup( WORKGROUP );
        _list.setRole( ContactList.ROLE_NONE );
        _list.setTosMessage( "" );
        ContactListHome.create( _list, _plugin );
    }

    @AfterEach
    public void removeData( )
    {
        ContactListHome.unassignContactsForList( _list.getId( ), _plugin );
        ContactListHome.remove( _list.getId( ), _plugin );
        if ( ContactHome.findByPrimaryKey( _contact.getId( ), _plugin ) != null )
        {
            ContactHome.remove( _contact, _plugin );
        }
    }

    @Test
    public void testContactOfAnotherWorkgroup( )
    {
        MockHttpServletRequest request = request( ContactJspBean.RIGHT_MANAGE_CONTACT );
        request.addParameter( "id_contact", String.valueOf( _contact.getId( ) ) );
        ContactJspBean bean = CDI.current( ).select( ContactJspBean.class ).get( );
        initBean( bean, request );

        assertThrows( AccessDeniedException.class, ( ) -> bean.getModifyContact( request ) );
        assertThrows( AccessDeniedException.class, ( ) -> bean.getConfirmRemoveContact( request ) );
        assertThrows( AccessDeniedException.class, ( ) -> bean.doRemoveContact( request ) );
        assertNotNull( ContactHome.findByPrimaryKey( _contact.getId( ), _plugin ) );
    }

    @Test
    public void testListOfAnotherWorkgroup( )
    {
        MockHttpServletRequest request = request( ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addParameter( "id_contact_list", String.valueOf( _list.getId( ) ) );
        request.addParameter( "id_contact", String.valueOf( _contact.getId( ) ) );
        ContactListJspBean bean = CDI.current( ).select( ContactListJspBean.class ).get( );
        initBean( bean, request );

        assertThrows( AccessDeniedException.class, ( ) -> bean.getModifyContactList( request ) );
        assertThrows( AccessDeniedException.class, ( ) -> bean.getManageListAssignations( request ) );
        assertThrows( AccessDeniedException.class, ( ) -> bean.getManageContactAssignations( request ) );
        assertThrows( AccessDeniedException.class, ( ) -> bean.doRemoveContactList( request ) );
        assertNotNull( ContactListHome.findByPrimaryKey( _list.getId( ), _plugin ) );
    }

    @Test
    public void testContactOfAllWorkgroups( ) throws AccessDeniedException
    {
        _contact.setWorkgroup( "all" );
        ContactHome.update( _contact, _plugin );
        MockHttpServletRequest request = request( ContactJspBean.RIGHT_MANAGE_CONTACT );
        request.addParameter( "id_contact", String.valueOf( _contact.getId( ) ) );
        ContactJspBean bean = CDI.current( ).select( ContactJspBean.class ).get( );
        initBean( bean, request );

        assertNotNull( bean.getModifyContact( request ) );
    }

    /**
     * The assignation page of an unknown contact (deleted since the page was bookmarked) gives the lists, not an error
     */
    @Test
    public void testAssignationsOfAnUnknownContact( ) throws AccessDeniedException
    {
        MockHttpServletRequest request = request( ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addParameter( "id_contact", "999999" );
        ContactListJspBean bean = CDI.current( ).select( ContactListJspBean.class ).get( );
        initBean( bean, request );

        assertNotNull( bean.getManageContactAssignations( request ) );
    }

    private static MockHttpServletRequest request( String strRight )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( new HashMap<String, RBACRole>( ) );
        user.setLocale( Locale.getDefault( ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, strRight );
        return request;
    }

    private static void initBean( fr.paris.lutece.portal.util.mvc.admin.MVCAdminJspBean bean, MockHttpServletRequest request )
    {
        try
        {
            bean.init( request, ContactJspBean.RIGHT_MANAGE_CONTACT );
        }
        catch( AccessDeniedException e )
        {
            fail( e.getMessage( ) );
        }
    }
}
