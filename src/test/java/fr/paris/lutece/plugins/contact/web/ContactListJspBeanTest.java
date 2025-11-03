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

import fr.paris.lutece.portal.business.rbac.RBACRole;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.test.AdminUserUtils;
import fr.paris.lutece.test.LuteceTestCase;

import java.util.HashMap;
import java.util.Locale;
import fr.paris.lutece.test.mocks.MockHttpServletResponse;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

public class ContactListJspBeanTest extends LuteceTestCase
{
    // Parameters
    private static final String PARAMETER_CONTACT_LIST_LABEL = "contact_list_label";
    private static final String PARAMETER_CONTACT_LIST_DESCRIPTION = "contact_list_description";
    private static final String PARAMETER_ID_CONTACT_LIST = "id_contact_list";
    private static final String PARAMETER_ADDED_CONTACTS = "contact_list_2";
    private static final String PARAMETER_ID_CONTACT = "id_contact";
    private static final String PARAMETER_ADDED_LISTS = "list_2";
    private static final String PARAMETER_CONCERNED_CONTACT = "id_contact";
    private static final String PARAMETER_CONTACTS_ORDER = "contacts_order";
    private static final String PARAMETER_WORKGROUP = "workgroup";
    private static final String PARAMETER_ROLE = "role";
    private static final String PARAMETER_TOS = "active_tos";
    private static final String PARAMETER_TOS_MESSAGE = "tos_message";
    private static final String ACTION_CREATE_CONTACT_LIST = "actionCreateContactList";
    private static final String ACTION_MODIFY_CONTACT_LIST = "actionModifyContactList";
    private static final String VIEW_CONFIRM_REMOVE_CONTACT_LIST = "viewConfirmRemoveContactList";
    private @Inject ContactListJspBean instance;

    /**
     * Test of getManageContactLists method, of class fr.paris.lutece.plugins.contact.web.ContactListJspBean.
     */
    @Test
    public void testGetManageContactLists( ) throws AccessDeniedException
    {
        System.out.println( "getManageContactLists" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        user.setRoles( AdminUserHome.getRolesListForUser( user.getUserId( ) ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        String result = instance.getManageContactLists( request );
    }

    /**
     * Test of getCreateContactList method, of class fr.paris.lutece.plugins.contact.web.ContactListJspBean.
     */
    @Test
    public void testGetCreateContactList( ) throws AccessDeniedException
    {
        System.out.println( "getCreateContactList" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( new HashMap<String, RBACRole>( ) );
        user.setLocale( Locale.getDefault( ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        String result = instance.getCreateContactList( request );
    }

    /**
     * Test of getModifyContactList method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
     */
    @Test
    public void testGetModifyContactList( ) throws AccessDeniedException
    {
        System.out.println( "getModifyContactList" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( new HashMap<String, RBACRole>( ) );
        user.setLocale( Locale.getDefault( ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addParameter( PARAMETER_ID_CONTACT_LIST, "1" );

        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        String result = instance.getModifyContactList( request );
    }

    /**
     * Test of getConfirmRemoveContactList method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
     */
    @Test
    public void testGetConfirmRemoveContactList( ) throws AccessDeniedException
    {
        System.out.println( "getConfirmRemoveContactList" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( new HashMap<String, RBACRole>( ) );
        user.setLocale( Locale.getDefault( ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_CONFIRM_REMOVE_CONTACT_LIST );
        request.addParameter( PARAMETER_ID_CONTACT_LIST, "1" );

        MockHttpServletResponse response = new MockHttpServletResponse( );

        String result = instance.processController( request, response );
    }

    /**
     * Test of getManageListAssignations method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
     */
    @Test
    public void testGetManageListAssignations( ) throws AccessDeniedException
    {
        System.out.println( "getManageListAssignations" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        user.setRoles( AdminUserHome.getRolesListForUser( user.getUserId( ) ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        request.addParameter( PARAMETER_ID_CONTACT_LIST, "1" );

        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        String result = instance.getManageListAssignations( request );
    }

    /**
     * Test of getManageContactAssignations method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
     */
    @Test
    public void testGetManageContactAssignations( ) throws AccessDeniedException
    {
        System.out.println( "getManageContactAssignations" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        user.setRoles( AdminUserHome.getRolesListForUser( user.getUserId( ) ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        request.addParameter( PARAMETER_ID_CONTACT, "1" );

        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        String result = instance.getManageContactAssignations( request );
    }

    /**
     * Test of doCreateContactList method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
     */
    @Test
    public void testDoCreateContactList( ) throws AccessDeniedException
    {
        System.out.println( "doCreateContactList" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( new HashMap<String, RBACRole>( ) );
        user.setLocale( Locale.getDefault( ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, ACTION_CREATE_CONTACT_LIST );
        request.addParameter( PARAMETER_CONTACT_LIST_LABEL, "label" );
        request.addParameter( PARAMETER_CONTACT_LIST_DESCRIPTION, "description " );
        request.addParameter( PARAMETER_WORKGROUP, "all" );
        request.addParameter( PARAMETER_ROLE, "role" );
        request.addParameter( PARAMETER_TOS_MESSAGE, "" );

        MockHttpServletResponse response = new MockHttpServletResponse( );

        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        String result = instance.processController( request, response );
    }

    /**
     * Test of doModifyContactList method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
     */
    @Test
    public void testDoModifyContactList( ) throws AccessDeniedException
    {
        System.out.println( "doCreateContactList" );

        MockHttpServletRequest request = new MockHttpServletRequest( );
        AdminUser user = new AdminUser( );
        user.setRoles( new HashMap<String, RBACRole>( ) );
        user.setLocale( Locale.getDefault( ) );
        AdminUserUtils.registerAdminUserWithRight( request, user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addParameter( MVCUtils.PARAMETER_ACTION, ACTION_MODIFY_CONTACT_LIST );
        request.addParameter( PARAMETER_ID_CONTACT_LIST, "1" );
        request.addParameter( PARAMETER_CONTACT_LIST_LABEL, "label" );
        request.addParameter( PARAMETER_CONTACT_LIST_DESCRIPTION, "description " );
        request.addParameter( PARAMETER_WORKGROUP, "all" );
        request.addParameter( PARAMETER_ROLE, "role" );
        request.addParameter( PARAMETER_TOS, "1" );
        request.addParameter( PARAMETER_TOS_MESSAGE, "test" );

        MockHttpServletResponse response = new MockHttpServletResponse( );

        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        String result = instance.processController( request, response );
    }
}
