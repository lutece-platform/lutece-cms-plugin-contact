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
import fr.paris.lutece.util.ReferenceList;

import java.util.Collection;


/**
 * IContactDAO Interface
 */
public interface IContactDAO
{
    /**
     * Delete a record from the table
     *
     * @param contact The Contact object
     * @param plugin The plugin
     */
    void delete( Contact contact, Plugin plugin );

    /**
     * Insert a new record in the table.
     *
     *
     * @param contact The contact object
     * @param plugin The plugin
     */
    void insert( Contact contact, Plugin plugin );

    /**
     * Load the data of Contact from the table
     * @param nContactId The identifier of Contact
     * @param plugin The plugin
     * @return the instance of the Contact
     */
    Contact load( int nContactId, Plugin plugin );

    /**
     * Load the list of contacts
     *
     *
     * @param plugin The plugin
     * @return The Collection of the Contacts
     */
    Collection<Contact> selectAll( Plugin plugin );

    /**
    * Load the list of contacts
    * @return The Collection of the Contacts with the string at the top
    * @param nIdContactList The id of contactList
    * @param strComboItem the string to display at the top of the list
    * @param plugin The plugin
    */
    ReferenceList selectContactsByListWithString( int nIdContactList, String strComboItem, Plugin plugin );

    /**
     * Update the record in the table
     *
     * @param contact The reference of contact
     * @param plugin The plugin
     */
    void store( Contact contact, Plugin plugin );

    /**
     * Update hits of the contact in contact and contact_list_contact tables
     * @param nIdContact id of the contact
     * @param nIdContactList id of contact list
     * @param plugin the plugin Contact
     */
    void updateHits( int nIdContact, int nIdContactList, Plugin plugin );

    ////////////////////////////////////////// ORDER MANAGEMENT ////////////////////////////////

    /**
     * Modify the order of a contact
     * @param nIdContactList the id of contactList
     * @param nNewOrder The order number
     * @param nId The contact identifier
     * @param plugin The plugin
     */
    void storeContactOrder( int nNewOrder, int nId, int nIdContactList, Plugin plugin );

    /**
     * Returns a contact identifier in a distinct order
     * @return The order of the Contact
     * @param nIdContactList the id of contactList
     * @param nContactOrder The order number
     * @param plugin The plugin
     */
    int selectContactIdByOrder( int nContactOrder, int nIdContactList, Plugin plugin );

    /**
    * Returns a contact identifier in a distinct order
    * @return The order of the Contact
    * @param nIdContact the id of contact
    * @param nIdContactList the id of the contact List
    * @param plugin The plugin
    */
    int selectContactOrderById( int nIdContact, int nIdContactList, Plugin plugin );
}
