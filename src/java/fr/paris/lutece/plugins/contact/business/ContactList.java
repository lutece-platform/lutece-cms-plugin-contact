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

import fr.paris.lutece.plugins.contact.service.ContactListWorkgroupRemovalListener;
import fr.paris.lutece.portal.service.role.RoleRemovalListenerService;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;
import fr.paris.lutece.portal.service.workgroup.WorkgroupRemovalListenerService;


/**
 * This class represents business object Contact
 */
public class ContactList implements AdminWorkgroupResource
{
    public static final String RESOURCE_TYPE = "CONTACT_LIST";
    public static final String ROLE_NONE = "none";
    private static ContactListWorkgroupRemovalListener _listenerWorkgroup;
    private static ContactListRoleRemovalListener _listenerRole;
    private static final String EMPTY_STRING = "";

    /////////////////////////////////////////////////////////////////////////////////
    // Constants
    private int _nId;
    private String _strContactListLabel;
    private String _strContactListDescription;
    private int _nContactsNumber;
    private String _strAdminWorkgroup;
    private String _strRole;
    private int _nContactListOrder;

    /**
     * Initialize the ContactList
     */
    public static void init( )
    {
        // Create removal listeners and register them
        if ( _listenerWorkgroup == null )
        {
            _listenerWorkgroup = new ContactListWorkgroupRemovalListener( );
            WorkgroupRemovalListenerService.getService( ).registerListener( _listenerWorkgroup );
        }

        if ( _listenerRole == null )
        {
            _listenerRole = new ContactListRoleRemovalListener( );
            RoleRemovalListenerService.getService( ).registerListener( _listenerRole );
        }
    }

    /**
     * Returns the identifier of this contactList.
     * @return the contact identifier
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the identifier of the contactList to the specified integer.
     * @param nId the new identifier
     */
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the label of this list.
     * @return the contact name
     */
    public String getLabel( )
    {
        return _strContactListLabel;
    }

    /**
     * Sets the label of the contact List to the specified string.
     * 
     * @param strContactListLabel the new name
     */
    public void setLabel( String strContactListLabel )
    {
        _strContactListLabel = ( strContactListLabel == null ) ? EMPTY_STRING : strContactListLabel;
    }

    /**
     * Returns the description of this contact list.
     * @return the contact name
     */
    public String getDescription( )
    {
        return _strContactListDescription;
    }

    /**
     * Sets the name of the contact to the specified string.
     * @param strContactListDescription the description of the contact list
     */
    public void setDescription( String strContactListDescription )
    {
        _strContactListDescription = ( strContactListDescription == null ) ? EMPTY_STRING : strContactListDescription;
    }

    /**
     * gets the number of contacts assigned to list
     * @return returns the number of contacts assigned to this list
     */
    public int getContactsNumber( )
    {
        return _nContactsNumber;
    }

    /**
     * sets to the specified integer the number of contacts assigned to list
     * @param nContactsNumber the number of contacts to set
     */
    public void setContactsNumber( int nContactsNumber )
    {
        _nContactsNumber = nContactsNumber;
    }

    /**
     * Returns the workgroup
     * @return The workgroup
     */
    public String getWorkgroup( )
    {
        return _strAdminWorkgroup;
    }

    /**
     * Sets the workgroup
     * @param strAdminWorkgroup The workgroup
     */
    public void setWorkgroup( String strAdminWorkgroup )
    {
        _strAdminWorkgroup = AdminWorkgroupService.normalizeWorkgroupKey( strAdminWorkgroup );
    }

    /**
     * Sets the role
     * @param strRole The role
     */

    /**
     * Gets the contactList role
     * @return contactList's role as a String
     * @since v1.1
     */
    public String getRole( )
    {
        return _strRole;
    }

    /**
     * allows the contact List to be saw by a role
     * @param strRole The role that can see the contact list
     */
    public void setRole( String strRole )
    {
        _strRole = ( ( strRole == null ) || ( strRole.equals( "" ) ) ) ? ROLE_NONE : strRole;
    }

    /**
     * Returns the order
     * @return The order
     */
    public int getContactListOrder( )
    {
        return _nContactListOrder;
    }

    /**
     * Sets the workgroup
     * @param nContactListOrder the contact list
     */
    public void setContactListOrder( int nContactListOrder )
    {
        _nContactListOrder = nContactListOrder;
    }
}
