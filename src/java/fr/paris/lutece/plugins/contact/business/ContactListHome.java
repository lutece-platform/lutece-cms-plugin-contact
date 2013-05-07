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
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.Collection;


/**
 * This class provides instances management methods (create, find, ...) for Contact objects
 */
public final class ContactListHome
{
    // Static variable pointed at the DAO instance
    private static IContactListDAO _dao = (IContactListDAO) SpringContextService.getPluginBean( "contact",
            "contactListDAO" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ContactListHome(  )
    {
    }

    /**
     * Creation of an instance of List
     *
     * @param contactList The instance of the contact which contains the informations to store
     * @param plugin The Plugin object
     * @return The  instance of contactList which has been created with its primary key.
     */
    public static ContactList create( ContactList contactList, Plugin plugin )
    {
        _dao.insert( contactList, plugin );

        return contactList;
    }

    /**
     * Update of the contactList which is specified in parameter
     * @return The instance of the  contact which has been updated
     * @param contactList the contactList object with updated parameters
     * @param plugin The Plugin object
     */
    public static ContactList update( ContactList contactList, Plugin plugin )
    {
        _dao.store( contactList, plugin );

        return contactList;
    }

    /**
     * Remove the Contact whose identifier is specified in parameter
     * @param nIdContactList the Id of the contact list to remove
     * @param plugin The Plugin object
     */
    public static void remove( int nIdContactList, Plugin plugin )
    {
        _dao.delete( nIdContactList, plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a contact whose identifier is specified in parameter
     * @param nKey The Primary key of the contact
     * @param plugin The Plugin object
     * @return An instance of contact
     */
    public static ContactList findByPrimaryKey( int nKey, Plugin plugin )
    {
        return _dao.load( nKey, plugin );
    }

    /**
     * Returns a collection of contacts objects
         * @param plugin The Plugin object
     * @return A collection of contacts
     */
    public static Collection<ContactList> findAll( Plugin plugin )
    {
        return _dao.selectAll( plugin );
    }

    /**
     * Returns a collection of contacts objects
     * @param strRoleKey The role key
     * @param plugin The Plugin object
     * @return A collection of ContactList
     */
    public static Collection<ContactList> findByRoleKey( String strRoleKey, Plugin plugin )
    {
        return _dao.selectByRoleKey( strRoleKey, plugin );
    }

    /**
     * Counts how many contacts are associated to the list
     * @param nIdContactList the id of the contactList
     * @param plugin The plugin contact
     * @return the number of contacts
     */
    public static int countContactsForList( int nIdContactList, Plugin plugin )
    {
        return _dao.countContactsForList( nIdContactList, plugin );
    }

    /**
     * counts all lists for one contact
     * @param nIdContact the Id of the contact
     * @param plugin The plugin contact
     * @return the number of lists
     */
    public static int countListsForContact( int nIdContact, Plugin plugin )
    {
        return _dao.countListsForContact( nIdContact, plugin );
    }

    /**
     * selects the list of assigned contacts for one list
     * @param nIdContactList the id of contactList
     * @param plugin The plugin contact
     * @return the collection of contacts assigned to the list
     */
    public static Collection<Contact> getAssignedContactsFor( int nIdContactList, Plugin plugin )
    {
        return _dao.selectContactsForList( nIdContactList, plugin );
    }

    /**
     * get all contacts that are not assigned to one specific list
     * @param nIdContactList th id of the contactList
     * @param plugin The plugin contact
     * @return the collection of not assigned contacts
     */
    public static Collection<Contact> getNotAssignedContactsFor( int nIdContactList, Plugin plugin )
    {
        return _dao.selectNotAssignedContactsFor( nIdContactList, plugin );
    }

    /**
     * returns true if the count of couples is positive
     * @param nIdContact the id of the contact
     * @param nIdContactList the id of contact List
     * @param plugin The plugin contact
     * @return true or false
     */
    public static boolean isAssigned( int nIdContact, int nIdContactList, Plugin plugin )
    {
        return _dao.isAssigned( nIdContact, nIdContactList, plugin );
    }

    /**
     * selects the list of Lists assigned to one contact
     * @param nIdContact the Id of the contact
     * @param plugin The plugin contact
     * @return collection of assigned lists
     */
    public static Collection<ContactList> getAssignedListsFor( int nIdContact, Plugin plugin )
    {
        return _dao.selectAssignedListsFor( nIdContact, plugin );
    }

    /**
     * selects the list of Lists not assigned to one contact
     * @param nIdContact the id of the contact
     * @param plugin The plugin Contact
     * @return collection of not assigned lists
     */
    public static Collection<ContactList> getNotAssignedListsFor( int nIdContact, Plugin plugin )
    {
        return _dao.selectNotAssignedListsFor( nIdContact, plugin );
    }

    /**
     * assigns a contact to a list or a list to a contact
     * @param nIdContact the id of the contact
     * @param nIdContactList the id of the contact list
     * @param plugin The plugin Contact
     */
    public static void assign( int nIdContact, int nIdContactList, Plugin plugin )
    {
        _dao.assign( nIdContact, nIdContactList, plugin );
    }

    /**
     * Unassigns a list and a contact
     * @param nIdContact the id of the contact
     * @param nIdContactList the id of contact List
     * @param plugin The plugin Contact
     */
    public static void unAssign( int nIdContact, int nIdContactList, Plugin plugin )
    {
        _dao.unAssign( nIdContact, nIdContactList, plugin );
    }

    /**
     * unassigns all contacts for one specific list
     * @param nIdContactList the id of concerned ContactList
     * @param plugin The plugin Contact
     */
    public static void unassignContactsForList( int nIdContactList, Plugin plugin )
    {
        _dao.unassignContactsForList( nIdContactList, plugin );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Contact Order management

    /**
     * Search the max order number of contacts for one list
     * @return int the max order
     * @param plugin The Plugin object
     */
    public static int getMaxOrderContactList( Plugin plugin )
    {
        return _dao.maxOrderContactList( plugin );
    }

    /**
     * Search the max order number of contacts for one list
     * @return int the max order
     * @param plugin The Plugin object
     */
    public static int getMaxOrderContact( int nIdContactList, Plugin plugin )
    {
        return _dao.maxOrderContact( nIdContactList, plugin );
    }

    /**
     * Search the order number of contactLists
     * @return int the id by a given order
     * @param nIdContactList the id of the contactList
     * @param nContactOrder the number of order of the contact
     * @param plugin The Plugin object
     */
    public static int getContactListIdByOrder( int nContactListOrder, Plugin plugin )
    {
        return _dao.selectContactListIdByOrder( nContactListOrder, plugin );
    }

    /**
     * returns the order of a contact in a list using its Id
     * @return int  the id by a given order
     * @param nIdContact the id of the contact
     * @param nIdContactList the id of the contactList
     * @param plugin The Plugin object
     */
    public static int getContactListOrderById( int nIdContactList, Plugin plugin )
    {
        return _dao.selectContactListOrderById( nIdContactList, plugin );
    }

    /**
     * Update the number order of contact
     * @param nIdContactList the id of the contactList
     * @param nNewOrder the new number of order
     * @param plugin The Plugin object
     */
    public static void updateContactListOrder( int nNewOrder, int nIdContactList, Plugin plugin )
    {
        _dao.storeContactListOrder( nNewOrder, nIdContactList, plugin );
    }

    /**
     * Returns true if the contactList exists
     * @return boolean the existance of the list
     * @param nIdContactList The if of contactList
     * @param plugin The Plugin object
     */
    public static boolean listExists( int nIdContactList, Plugin plugin )
    {
        return _dao.listExists( nIdContactList, plugin );
    }

    ////////////////////////////////////////////////////////////////////////////
    // References List management

    /**
     * unassigns all lists for one specific contact
     * @param nIdContact The if of contact
     * @param plugin The Plugin object
     */
    public static void unassignListsForContact( int nIdContact, Plugin plugin )
    {
        _dao.unassignListsForContact( nIdContact, plugin );
    }
}
