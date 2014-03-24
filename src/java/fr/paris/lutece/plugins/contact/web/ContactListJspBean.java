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
package fr.paris.lutece.plugins.contact.web;

import fr.paris.lutece.plugins.contact.business.Contact;
import fr.paris.lutece.plugins.contact.business.ContactHome;
import fr.paris.lutece.plugins.contact.business.ContactList;
import fr.paris.lutece.plugins.contact.business.ContactListHome;
import fr.paris.lutece.portal.business.role.RoleHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage contact features ( manage,
 * create, modify, remove, change order of
 * contact )
 */
public class ContactListJspBean extends PluginAdminPageJspBean
{
    // Right
    public static final String RIGHT_MANAGE_CONTACT = "CONTACT_MANAGEMENT";

    // properties for page titles
    private static final String PROPERTY_PAGE_TITLE_CONTACTS = "contact.manage_contact_lists.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CONTACTS = "contact.create_contact_list.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_LIST_ASSIGNATIONS = "contact.contact_list_assignations.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CONTACT_ASSIGNATIONS = "contact.contact_assignations.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY = "contact.modify_contact_list.pageTitle";

    // templates
    private static final String TEMPLATE_MANAGE_CONTACT_LISTS = "/admin/plugins/contact/manage_contact_lists.html";
    private static final String TEMPLATE_CREATE_CONTACT = "/admin/plugins/contact/create_contact_list.html";
    private static final String TEMPLATE_MANAGE_LIST_ASSIGNATIONS = "/admin/plugins/contact/manage_list_assignations.html";
    private static final String TEMPLATE_MANAGE_CONTACT_ASSIGNATIONS = "/admin/plugins/contact/manage_contact_assignations.html";
    private static final String TEMPLATE_MODIFY_CONTACT_LIST = "/admin/plugins/contact/modify_contact_list.html";

    // Markers
    private static final String MARK_CONTACT_LIST = "contact_list"; //one contactList
    private static final String MARK_LIST_CONTACTS = "list_contacts";
    private static final String MARK_LIST_CONTACT_LIST = "list_contact_list";
    private static final String MARK_CONTACTS_NUMBER = "contacts_number";
    private static final String MARK_CONTACT_ORDER_LIST = "order_list";
    private static final String MARK_ASSIGNED_CONTACT_LIST = "assigned_contacts";
    private static final String MARK_CONTACT = "contact";
    private static final String MARK_NOT_ASSIGNED_LISTS = "not_assigned_lists";
    private static final String MARK_ASSIGNED_LISTS = "assigned_lists";
    private static final String MARK_WORKGROUPS_LIST = "workgroups_list";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";
    private static final String MARK_ROLES_LIST = "roles_list";

    //Parameters
    private static final String PARAMETER_CONTACT_LIST_LABEL = "contact_list_label";
    private static final String PARAMETER_CONTACT_LIST_DESCRIPTION = "contact_list_description";
    private static final String PARAMETER_PAGE_INDEX = "page_index";
    private static final String PARAMETER_ID_CONTACT_LIST = "id_contact_list";
    private static final String PARAMETER_ID_CONTACT = "id_contact";
    private static final String PARAMETER_CONTACT_LIST = "list_contact";
    private static final String PARAMETER_CONTACTS_ORDER = "contacts_order";
    private static final String PARAMETER_CONTACT_LIST_ORDER = "contact_list_order";
    private static final String PARAMETER_WORKGROUP = "workgroup";
    private static final String PARAMETER_ROLE = "role";
    private static final String PARAMETER_CANCEL = "cancel";

    // Properties
    private static final String PROPERTY_DEFAULT_CONTACT_LIST_PER_PAGE = "contact.contactList.itemsPerPage";

    //messages
    private static final String MESSAGE_CONFIRM_REMOVE_CONTACT_LIST = "contact.message.confirmRemoveContactList";

    //JSP
    private static final String JSP_MANAGE_LIST_ASSIGNATIONS = "ManageListAssignations.jsp";
    private static final String JSP_MANAGE_CONTACT_ASSIGNATIONS = "ManageContactAssignations.jsp";
    private static final String JSP_MANAGE_CONTACT_LISTS = "ManageContactLists.jsp";
    private static final String JSP_MANAGE_CONTACTS = "ManageContacts.jsp";
    private static final String JSP_MANAGE_CONTACTS_LISTS = "jsp/admin/plugins/contact/ManageContactLists.jsp";
    private static final String JSP_DO_REMOVE_CONTACT_LIST = "jsp/admin/plugins/contact/DoRemoveContactList.jsp";

    //Variables
    private int _nDefaultItemsPerPage;
    private String _strCurrentPageIndex;
    private int _nItemsPerPage;

    /**
     * returns the template of the contactLists management
     * @param request The HttpRequest
     * @return template of lists management
     */
    public String getManageContactLists( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CONTACTS );

        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_CONTACT_LIST_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        Collection<ContactList> listContactList = ContactListHome.findAll( getPlugin( ) );

        listContactList = AdminWorkgroupService.getAuthorizedCollection( listContactList, getUser( ) );

        LocalizedPaginator paginator = new LocalizedPaginator( (List<ContactList>) listContactList, _nItemsPerPage,
                getUrlPage( ), PARAMETER_PAGE_INDEX, _strCurrentPageIndex, getLocale( ) );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_NB_ITEMS_PER_PAGE, "" + _nItemsPerPage );
        model.put( MARK_CONTACT_ORDER_LIST, getContactListOrderList( ) );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_LIST_CONTACT_LIST, paginator.getPageItems( ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CONTACT_LISTS, getLocale( ), model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * returns the form of contactList creation
     * @param request The HttpRequest
     * @return the HTML code of contactList form
     */
    public String getCreateContactList( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_CREATE_CONTACTS );

        Map<String, ReferenceList> model = new HashMap<String, ReferenceList>( );
        ReferenceList workgroupsList = AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) );
        model.put( MARK_WORKGROUPS_LIST, workgroupsList );
        model.put( MARK_ROLES_LIST, RoleHome.getRolesList( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CREATE_CONTACT, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * Inserts a contactList into Database
     * @param request The HttpRequest
     * @return the lsit of contactLists
     */
    public String doCreateContactList( HttpServletRequest request )
    {
        ContactList contactList = new ContactList( );
        String strLabel = request.getParameter( PARAMETER_CONTACT_LIST_LABEL );
        String strDescription = request.getParameter( PARAMETER_CONTACT_LIST_DESCRIPTION );
        String strWorkgroup = request.getParameter( PARAMETER_WORKGROUP );
        String strRole = request.getParameter( PARAMETER_ROLE );

        if ( ( strLabel.equals( "" ) ) || ( strDescription.equals( "" ) ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        contactList.setLabel( strLabel );
        contactList.setDescription( strDescription );
        contactList.setWorkgroup( strWorkgroup );
        contactList.setRole( strRole );
        ContactListHome.create( contactList, getPlugin( ) );

        // if the operation occurred well, redirects towards the list of the ContactList
        return JSP_MANAGE_CONTACT_LISTS;
    }

    /**
     * returns the template of modification form
     * @param request The HttpRequest
     * @return the HTML code of modify contactList form
     */
    public String getModifyContactList( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MODIFY );

        Map<String, Object> model = new HashMap<String, Object>( );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT_LIST ) );

        ReferenceList workgroupsList = AdminWorkgroupService.getUserWorkgroups( getUser( ), getLocale( ) );
        ContactList contactList = ContactListHome.findByPrimaryKey( nId, getPlugin( ) );

        if ( contactList == null )
        {
            return getManageContactLists( request );
        }

        model.put( MARK_WORKGROUPS_LIST, workgroupsList );
        model.put( MARK_CONTACT_LIST, contactList );
        model.put( MARK_ROLES_LIST, RoleHome.getRolesList( ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MODIFY_CONTACT_LIST, getLocale( ), model );

        return getAdminPage( template.getHtml( ) );
    }

    /**
     * updates the contactList in database
     * @param request The HttpRequest
     * @return the contactList list
     */
    public String doModifyContactList( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT_LIST ) );
        ContactList contactList = ContactListHome.findByPrimaryKey( nId, getPlugin( ) );
        contactList.setLabel( request.getParameter( PARAMETER_CONTACT_LIST_LABEL ) );
        contactList.setDescription( request.getParameter( PARAMETER_CONTACT_LIST_DESCRIPTION ) );
        contactList.setWorkgroup( request.getParameter( PARAMETER_WORKGROUP ) );
        contactList.setRole( request.getParameter( PARAMETER_ROLE ) );

        // Mandatory fields
        if ( request.getParameter( PARAMETER_CONTACT_LIST_LABEL ).equals( "" ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        ContactListHome.update( contactList, getPlugin( ) );

        return JSP_MANAGE_CONTACT_LISTS;
    }

    /**
     * Manages the removal form of a contact list
     * 
     * @param request The Http request
     * @return the html code to confirm
     */
    public String getConfirmRemoveContactList( HttpServletRequest request )
    {
        UrlItem url = new UrlItem( JSP_DO_REMOVE_CONTACT_LIST );
        url.addParameter( PARAMETER_ID_CONTACT_LIST, request.getParameter( PARAMETER_ID_CONTACT_LIST ) );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CONTACT_LIST, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * removes a record from database
     * @param request The HttpRequest
     * @return the contactList list
     */
    public String doRemoveContactList( HttpServletRequest request )
    {
        int nIdContactList = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT_LIST ) );
        int nOrder = ContactListHome.getContactListOrderById( nIdContactList, getPlugin( ) );
        int nNewOrder = ContactListHome.getMaxOrderContactList( getPlugin( ) );
        ContactListHome.unassignContactsForList( nIdContactList, getPlugin( ) );
        modifyContactListsOrder( nOrder, nNewOrder, nIdContactList );
        ContactListHome.remove( nIdContactList, getPlugin( ) );

        // Go to the parent page
        return getHomeUrl( request );
    }

    /**
     * returns the template of list assignations
     * @param request The HttpRequest
     * @return the HTML code of list assignations
     */
    public String getManageListAssignations( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_LIST_ASSIGNATIONS );

        String strIdContactList = request.getParameter( PARAMETER_ID_CONTACT_LIST );
        int nIdContactList = Integer.parseInt( strIdContactList );
        ContactList contactList = ContactListHome.findByPrimaryKey( nIdContactList, getPlugin( ) ); //The contactList object concerned -- One obecjt

        if ( contactList == null )
        {
            return getManageContactLists( request );
        }

        Collection<Contact> notAssignedContacts = ContactListHome.getNotAssignedContactsFor( nIdContactList,
                getPlugin( ) ); //The list of contacts objects -- One list of saveral objects
        notAssignedContacts = AdminWorkgroupService.getAuthorizedCollection( notAssignedContacts, getUser( ) );

        ReferenceList refListNotAssigned = new ReferenceList( );

        for ( Contact contact : notAssignedContacts )
        {
            ReferenceItem item = new ReferenceItem( );
            item.setCode( Integer.toString( contact.getId( ) ) );
            item.setName( "[ " + contact.getWorkgroup( ) + " ] " + contact.getName( ) );
            refListNotAssigned.add( item );
        }

        Collection<Contact> assignedContacts = ContactListHome.getAssignedContactsFor( nIdContactList, getPlugin( ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_CONTACT_LIST, contactList );
        model.put( MARK_LIST_CONTACTS, refListNotAssigned );
        model.put( MARK_ASSIGNED_CONTACT_LIST, assignedContacts );
        model.put( MARK_CONTACTS_NUMBER, ContactListHome.countContactsForList( nIdContactList, getPlugin( ) ) );
        model.put( MARK_CONTACT_ORDER_LIST, getContactOrderList( nIdContactList ) );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_LIST_ASSIGNATIONS, getLocale( ),
                model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * assigns a contact to a list in database
     * @param request The HttpRequest
     * @return the template of list assignations
     */
    public String doAssignContactsToList( HttpServletRequest request )
    {
        String strReturn;

        String strActionCancel = request.getParameter( PARAMETER_CANCEL );

        if ( strActionCancel != null )
        {
            strReturn = JSP_MANAGE_CONTACT_LISTS;
        }
        else
        {
            //retrieve the selected portlets ids
            String[] arrayContactsIds = request.getParameterValues( PARAMETER_CONTACT_LIST );
            int nIdContactList = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT_LIST ) );

            if ( ( arrayContactsIds != null ) )
            {
                for ( int i = 0; i < arrayContactsIds.length; i++ )
                {
                    if ( !ContactListHome.isAssigned( Integer.parseInt( arrayContactsIds[i] ), nIdContactList,
                            getPlugin( ) ) )
                    {
                        ContactListHome.assign( Integer.parseInt( arrayContactsIds[i] ), nIdContactList, getPlugin( ) );
                    }
                }
            }

            strReturn = JSP_MANAGE_LIST_ASSIGNATIONS + "?" + PARAMETER_ID_CONTACT_LIST + "=" + nIdContactList;
        }

        return strReturn;
    }

    /**
     * unassigns contact from list
     * @param request The HttpRequest
     * @return the HTML code of list assignations
     */
    public String doUnAssignContact( HttpServletRequest request )
    {
        int nIdContactList = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT_LIST ) );
        int nIdContact = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT ) );

        int nOrder = ContactHome.getContactOrderById( nIdContact, nIdContactList, getPlugin( ) );
        int nNewOrder = ContactListHome.getMaxOrderContact( nIdContactList, getPlugin( ) );
        modifyContactsOrder( nIdContact, nOrder, nNewOrder, nIdContactList );
        ContactListHome.unAssign( nIdContact, nIdContactList, getPlugin( ) );

        return JSP_MANAGE_LIST_ASSIGNATIONS + "?" + PARAMETER_ID_CONTACT_LIST + "=" + nIdContactList;
    }

    /**
     * unassigns list from a contact
     * @param request The HttpRequest
     * @return the html code of contact assignations
     */
    public String doUnAssignList( HttpServletRequest request )
    {
        int nIdContactList = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT_LIST ) );
        int nIdContact = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT ) );
        ContactListHome.unAssign( nIdContact, nIdContactList, getPlugin( ) );

        return JSP_MANAGE_CONTACT_ASSIGNATIONS + "?" + PARAMETER_ID_CONTACT + "=" + nIdContact;
    }

    /**
     * gets the assignations page of a contact
     * @param request The HttpRequest
     * @return the HTML code of contact assignations
     */
    public String getManageContactAssignations( HttpServletRequest request )
    {
        setPageTitleProperty( PROPERTY_PAGE_TITLE_MANAGE_CONTACT_ASSIGNATIONS );

        int nIdContact = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT ) );
        Contact contact = ContactHome.findByPrimaryKey( nIdContact, getPlugin( ) );
        Collection<ContactList> notAssignedLists = ContactListHome.getNotAssignedListsFor( nIdContact, getPlugin( ) );
        notAssignedLists = AdminWorkgroupService.getAuthorizedCollection( notAssignedLists, getUser( ) );

        ReferenceList refListNotAssigned = new ReferenceList( );

        for ( ContactList contactList : notAssignedLists )
        {
            ReferenceItem item = new ReferenceItem( );
            item.setCode( Integer.toString( contactList.getId( ) ) );
            item.setName( contactList.getLabel( ) );
            refListNotAssigned.add( item );
        }

        Collection<ContactList> assignedLists = ContactListHome.getAssignedListsFor( nIdContact, getPlugin( ) );

        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_CONTACT, contact );
        model.put( MARK_NOT_ASSIGNED_LISTS, refListNotAssigned );
        model.put( MARK_ASSIGNED_LISTS, assignedLists );

        HtmlTemplate templateList = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CONTACT_ASSIGNATIONS, getLocale( ),
                model );

        return getAdminPage( templateList.getHtml( ) );
    }

    /**
     * assigns lists to one contact
     * @param request The HttpRequest
     * @return the HTML code of contact assignations
     */
    public String doAssignListsToContact( HttpServletRequest request )
    {
        String strReturn;

        String strActionCancel = request.getParameter( PARAMETER_CANCEL );

        if ( strActionCancel != null )
        {
            strReturn = JSP_MANAGE_CONTACTS;
        }
        else
        {
            //retrieve the selected portlets ids
            String[] arrayListsIds = request.getParameterValues( PARAMETER_CONTACT_LIST );
            int nIdContact = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT ) );

            if ( ( arrayListsIds != null ) )
            {
                for ( int i = 0; i < arrayListsIds.length; i++ )
                {
                    if ( !ContactListHome.isAssigned( nIdContact, Integer.parseInt( arrayListsIds[i] ), getPlugin( ) ) )
                    {
                        ContactListHome.assign( nIdContact, Integer.parseInt( arrayListsIds[i] ), getPlugin( ) );
                    }
                }
            }

            strReturn = JSP_MANAGE_CONTACT_ASSIGNATIONS + "?" + PARAMETER_ID_CONTACT + "=" + nIdContact;
        }

        return strReturn;
    }

    /**
     * Modifies the order in the list of contacts
     * 
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyContactsOrder( HttpServletRequest request )
    {
        int nIdContact = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT ) );
        int nIdContactList = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT_LIST ) );

        int nOrder = ContactHome.getContactOrderById( nIdContact, nIdContactList, getPlugin( ) );
        int nNewOrder = Integer.parseInt( request.getParameter( PARAMETER_CONTACTS_ORDER ) );
        modifyContactsOrder( nIdContact, nOrder, nNewOrder, nIdContactList );

        return JSP_MANAGE_LIST_ASSIGNATIONS + "?" + PARAMETER_ID_CONTACT_LIST + "=" + nIdContactList;
    }

    /**
     * Builts a list of sequence numbers
     * @param nIdContactList the id of the contactList
     * @return the list of sequence numbers
     */
    private ReferenceList getContactOrderList( int nIdContactList )
    {
        int nMax = ContactListHome.getMaxOrderContact( nIdContactList, getPlugin( ) );
        ReferenceList list = new ReferenceList( );

        for ( int i = 1; i < ( nMax + 1 ); i++ )
        {
            list.addItem( i, Integer.toString( i ) );
        }

        return list;
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     * Modify the place in the list for a contact
     * 
     * @param nId the contact identifier
     * @param nOrder the actual place in the list
     * @param nNewOrder the new place in the list
     * @param nIdContactList the id of the contactList
     */
    private void modifyContactsOrder( int nId, int nOrder, int nNewOrder, int nIdContactList )
    {
        if ( nNewOrder < nOrder )
        {
            for ( int i = nOrder - 1; i > ( nNewOrder - 1 ); i-- )
            {
                int nIdContactOrder = ContactHome.getContactIdByOrder( i, nIdContactList, getPlugin( ) );
                ContactHome.updateContactOrder( i + 1, nIdContactOrder, nIdContactList, getPlugin( ) );
            }

            ContactHome.updateContactOrder( nNewOrder, nId, nIdContactList, getPlugin( ) );
        }
        else
        {
            for ( int i = nOrder; i < ( nNewOrder + 1 ); i++ )
            {
                int nIdContactOrder = ContactHome.getContactIdByOrder( i, nIdContactList, getPlugin( ) );
                ContactHome.updateContactOrder( i - 1, nIdContactOrder, nIdContactList, getPlugin( ) );
            }

            ContactHome.updateContactOrder( nNewOrder, nId, nIdContactList, getPlugin( ) );
        }
    }

    /**
     * Modifies the order in the list of contactLists
     * 
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    public String doModifyContactListsOrder( HttpServletRequest request )
    {
        int nIdContactList = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT_LIST ) );

        int nOrder = ContactListHome.getContactListOrderById( nIdContactList, getPlugin( ) );
        int nNewOrder = Integer.parseInt( request.getParameter( PARAMETER_CONTACT_LIST_ORDER ) );
        modifyContactListsOrder( nOrder, nNewOrder, nIdContactList );

        return JSP_MANAGE_CONTACT_LISTS;
    }

    /**
     * Builts a list of sequence numbers
     * @return the list of sequence numbers
     */
    private ReferenceList getContactListOrderList( )
    {
        int nMax = ContactListHome.getMaxOrderContactList( getPlugin( ) );
        ReferenceList list = new ReferenceList( );

        for ( int i = 1; i < ( nMax + 1 ); i++ )
        {
            list.addItem( i, Integer.toString( i ) );
        }

        return list;
    }

    //////////////////////////////////////////////////////////////////////////////////
    // Private implementation

    /**
     * Modify the place in the list for a contact
     * 
     * @param nId the contact identifier
     * @param nOrder the actual place in the list
     * @param nNewOrder the new place in the list
     * @param nIdContactList the id of the contactList
     */
    private void modifyContactListsOrder( int nOrder, int nNewOrder, int nIdContactList )
    {
        if ( nNewOrder < nOrder )
        {
            for ( int i = nOrder - 1; i > ( nNewOrder - 1 ); i-- )
            {
                int nIdContactListOrder = ContactListHome.getContactListIdByOrder( i, getPlugin( ) );
                ContactListHome.updateContactListOrder( i + 1, nIdContactListOrder, getPlugin( ) );
            }

            ContactListHome.updateContactListOrder( nNewOrder, nIdContactList, getPlugin( ) );
        }
        else
        {
            for ( int i = nOrder; i < ( nNewOrder + 1 ); i++ )
            {
                int nIdContactListOrder = ContactListHome.getContactListIdByOrder( i, getPlugin( ) );
                ContactListHome.updateContactListOrder( i - 1, nIdContactListOrder, getPlugin( ) );
            }

            ContactListHome.updateContactListOrder( nNewOrder, nIdContactList, getPlugin( ) );
        }
    }

    /**
     * Return UrlPage Url
     * @return url
     */
    private String getUrlPage( )
    {
        UrlItem url = new UrlItem( JSP_MANAGE_CONTACTS_LISTS );

        return url.getUrl( );
    }
}
