/*
 * Copyright (c) 2002-2009, Mairie de Paris
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

import fr.paris.lutece.plugins.contact.web.ContactListJspBean;
import fr.paris.lutece.portal.business.rbac.AdminRole;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.business.user.AdminUserHome;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.MokeHttpServletRequest;

import java.util.HashMap;
import java.util.Locale;


public class ContactListJspBeanTest extends LuteceTestCase
{
    //Parameters
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

    /**
     * Test of getManageContactLists method, of class fr.paris.lutece.plugins.contact.web.ContactListJspBean.
     */
    public void testGetManageContactLists(  ) throws AccessDeniedException
    {
        System.out.println( "getManageContactLists" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        user.setRoles( AdminUserHome.getRolesListForUser( user.getUserId(  ) ) );
        request.registerAdminUserWithRigth( user, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        ContactListJspBean instance = new ContactListJspBean(  );
        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        String result = instance.getManageContactLists( request );
    }

    /**
    * Test of getCreateContactList method, of class fr.paris.lutece.plugins.contact.web.ContactListJspBean.
    */
    public void testGetCreateContactList(  ) throws AccessDeniedException
    {
        System.out.println( "getCreateContactList" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        AdminUser user = new AdminUser(  );
        user.setRoles( new HashMap<String, AdminRole>(  ) );
        user.setLocale( Locale.getDefault(  ) );
        request.registerAdminUserWithRigth( user, ContactListJspBean.RIGHT_MANAGE_CONTACT );

        ContactListJspBean instance = new ContactListJspBean(  );

        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        instance.getCreateContactList( request );
    }

    /**
     * Test of getModifyContactList method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
     */

    /*
    public void testGetModifyContactList(  ) throws AccessDeniedException
    {
        System.out.println( "getModifyContactList" );
    
        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        AdminUser user = new AdminUser(  );
        user.setRoles( new HashMap<String, AdminRole>(  ) );
        user.setLocale( Locale.getDefault(  ) );
        request.registerAdminUserWithRigth( user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addMokeParameters( PARAMETER_ID_CONTACT_LIST, "1" );
    
        ContactListJspBean instance = new ContactListJspBean(  );
    
        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        instance.getModifyContactList( request );
    }*/

    /**
    * Test of getConfirmRemoveContactList method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
    */
    public void testGetConfirmRemoveContactList(  ) throws AccessDeniedException
    {
        System.out.println( "getConfirmRemoveContactList" );

        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        AdminUser user = new AdminUser(  );
        user.setRoles( new HashMap<String, AdminRole>(  ) );
        user.setLocale( Locale.getDefault(  ) );
        request.registerAdminUserWithRigth( user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addMokeParameters( PARAMETER_ID_CONTACT_LIST, "1" );

        ContactListJspBean instance = new ContactListJspBean(  );

        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        instance.getConfirmRemoveContactList( request );
    }

    /**
     * Test of getManageListAssignations method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
     */

    /*
    public void testGetManageListAssignations(  ) throws AccessDeniedException
    {
        System.out.println( "getManageListAssignations" );
    
        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        user.setRoles( AdminUserHome.getRolesListForUser( user.getUserId(  ) ) );
        request.registerAdminUserWithRigth( user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
    
        request.addMokeParameters( PARAMETER_ID_CONTACT_LIST, "1" );
    
        ContactListJspBean instance = new ContactListJspBean(  );
        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
    
        String result = instance.getManageListAssignations( request );
    }
    */

    /**
    * Test of getManageContactAssignations method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
    */

    /*
    public void testGetManageContactAssignations(  ) throws AccessDeniedException
    {
        System.out.println( "getManageContactAssignations" );
    
        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        AdminUser user = AdminUserHome.findUserByLogin( "admin" );
        user.setRoles( AdminUserHome.getRolesListForUser( user.getUserId(  ) ) );
        request.registerAdminUserWithRigth( user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
    
        request.addMokeParameters( PARAMETER_ID_CONTACT, "1" );
    
        ContactListJspBean instance = new ContactListJspBean(  );
        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
    
        String result = instance.getManageContactAssignations( request );
    }
    */

    /**
    * Test of doCreateContactList method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
    */

    /*
    public void testDoCreateContactList(  ) throws AccessDeniedException
    {
        System.out.println( "doCreateContactList" );
    
        MokeHttpServletRequest request = new MokeHttpServletRequest(  );
        AdminUser user = new AdminUser(  );
        user.setRoles( new HashMap<String, AdminRole>(  ) );
        user.setLocale( Locale.getDefault(  ) );
        request.registerAdminUserWithRigth( user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        request.addMokeParameters( PARAMETER_CONTACT_LIST_LABEL, "label" );
        request.addMokeParameters( PARAMETER_CONTACT_LIST_DESCRIPTION, "description " );
        request.addMokeParameters( PARAMETER_WORKGROUP, "all" );
        request.addMokeParameters( PARAMETER_ROLE, "role" );
    
        ContactListJspBean instance = new ContactListJspBean(  );
    
        instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
        instance.doCreateContactList( request );
    }*/

    /**
    * Test of doModifyContactList method, of class fr.paris.lutece.plugins.contact.web.contactListJspBean.
    */

    /*
    public void testDoModifyContactList(  ) throws AccessDeniedException
    {
     System.out.println( "doCreateContactList" );
    
     MokeHttpServletRequest request = new MokeHttpServletRequest(  );
     AdminUser user = new AdminUser(  );
     user.setRoles( new HashMap<String, AdminRole>(  ) );
     user.setLocale( Locale.getDefault(  ) );
     request.registerAdminUserWithRigth( user, ContactListJspBean.RIGHT_MANAGE_CONTACT );
     request.addMokeParameters( PARAMETER_ID_CONTACT_LIST, "label" );
     request.addMokeParameters( PARAMETER_CONTACT_LIST_LABEL, "label" );
     request.addMokeParameters( PARAMETER_CONTACT_LIST_DESCRIPTION, "description " );
     request.addMokeParameters( PARAMETER_WORKGROUP, "all" );
     request.addMokeParameters( PARAMETER_ROLE, "role" );
    
     ContactListJspBean instance = new ContactListJspBean(  );
    
     instance.init( request, ContactListJspBean.RIGHT_MANAGE_CONTACT );
     instance.doCreateContactList( request );
    }*/
}
