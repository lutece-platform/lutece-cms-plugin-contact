/*
 * Copyright (c) 2002-2014, Mairie de Paris
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

import java.util.Collection;


/**
 * IContactDAO Interface
 */
public interface IContactListDAO
{
    /**
     * Insert a new record in the table.
     * @param contactList the instance of contactList to insert into DB
     * @param plugin the plugin contact
     */
    void insert( ContactList contactList, Plugin plugin );

    /**
     * Load the data of Contact from the table
     * @param nContactListId the Id of the contactList to load
     * @param plugin the plugin contact
     * @return the instance of contactList object loaded
     */
    ContactList load( int nContactListId, Plugin plugin );

    /**
     * Delete a record from the table
     * @param nIdContactList the id of contactlist to delete
     * @param plugin The plugin
     */
    void delete( int nIdContactList, Plugin plugin );

    /**
     * Update the record in the table
     * @param contactList The reference of contactList
     * @param plugin The plugin
     */
    void store( ContactList contactList, Plugin plugin );

    /**
     * Load the list of contactsList
     * 
     * @param plugin The plugin
     * @return The Collection of the Contacts
     */
    Collection<ContactList> selectAll( Plugin plugin );

    /**
     * counts how many contacts are associated to the specified list
     * @param nIdContactList the Id of contactList
     * @param plugin the plugin contact
     * @return the number of contacts for the list
     */
    int countContactsForList( int nIdContactList, Plugin plugin );

    /**
     * Selects all contacts associated to a specified list
     * @param nIdContactList the id of contactList
     * @param plugin the plugin contact
     * @return list of contacts
     */
    Collection<Contact> selectContactsForList( int nIdContactList, Plugin plugin );

    /**
     * returns true if a contact is assigned to a list
     * @param nIdContact The id of the contact
     * @param nIdContactList The id of the contactList
     * @param plugin the plugin contact
     * @return boolean: true if is assigned, false if not
     */
    boolean isAssigned( int nIdContact, int nIdContactList, Plugin plugin );

    /**
     * Inserts 2 keys in association table
     * @param nIdContact The id of the contact
     * @param nIdContactList The contact List that will be associated
     * @param plugin The plugin
     */
    void assign( int nIdContact, int nIdContactList, Plugin plugin );

    /**
     * Unassigns a contact of a list, or a list of a contact
     * @param nIdContact the id of the contact
     * @param nIdContactList the id of the contactList
     * @param plugin The plugin
     */
    void unAssign( int nIdContact, int nIdContactList, Plugin plugin );

    /**
     * Selects the list of all contacts that are not assigned to the specified
     * list
     * @param nIdContactList the id of the contact List
     * @param plugin the plugin contact
     * @return list of not assigned contacts
     */
    Collection<Contact> selectNotAssignedContactsFor( int nIdContactList, Plugin plugin );

    /**
     * Selects assigned lists for a contact
     * @param nIdContact the id of the contact
     * @param plugin the plugin contact
     * @return collection of lists, the contact is associated to
     */
    Collection<ContactList> selectAssignedListsFor( int nIdContact, Plugin plugin );

    /**
     * Selects lists for a role key
     * @param strRoleKey The role key
     * @param plugin the plugin contact
     * @return collection of lists
     */
    Collection<ContactList> selectByRoleKey( String strRoleKey, Plugin plugin );

    /**
     * selects all lists, the contact is not associated to
     * @param nIdContact the id of the contact
     * @param plugin the plugin contact
     * @return collection of contactLists
     */
    Collection<ContactList> selectNotAssignedListsFor( int nIdContact, Plugin plugin );

    /**
     * Unassigns all contacts for a specified list
     * @param nIdContactList the id of contactlist
     * @param plugin the plugin contact
     */
    void unassignContactsForList( int nIdContactList, Plugin plugin );

    ////////////////////////////////////////// ORDER MANAGEMENT ////////////////////////////////

    /**
     * Calculate the new max order
     * @return the max order of contactList
     * @param plugin The plugin
     */
    int maxOrderContactList( Plugin plugin );

    /**
     * Calculate the new max order in a list
     * @return the max order of contact
     * @param nIdContactList the id of the contact list
     * @param plugin The plugin
     */
    int maxOrderContact( int nIdContactList, Plugin plugin );

    /**
     * Modify the order of a contact
     * @param nNewOrder The order number
     * @param nId The contactList identifier
     * @param plugin The plugin
     */
    void storeContactListOrder( int nNewOrder, int nId, Plugin plugin );

    /**
     * Returns a contact identifier in a distinct order
     * @return The order of the ContactList
     * @param nContactListOrder The order number
     * @param plugin The plugin
     */
    int selectContactListIdByOrder( int nContactListOrder, Plugin plugin );

    /**
     * Returns the order of a contactList
     * @param nIdContactList the id of contactList
     * @param plugin the plugin contact
     * @return the order of the contactList
     */
    int selectContactListOrderById( int nIdContactList, Plugin plugin );

    // ASSIGNMENT 

    /**
     * Unassigns all contacts for a specified list
     * @param nIdContact the id of contact
     * @param plugin the plugin contact
     */
    void unassignListsForContact( int nIdContact, Plugin plugin );

    /**
     * counts how many lists the contact is associated to
     * @param nIdContact the Id of concerned contact
     * @param plugin the plugin contact
     * @return the number of counted lists
     */
    int countListsForContact( int nIdContact, Plugin plugin );

    /**
     * Returns true if the contactList exists
     * @return boolean the existance of the list
     * @param nIdContactList The if of contactList
     * @param plugin The Plugin object
     */
    boolean listExists( int nIdContactList, Plugin plugin );
}
