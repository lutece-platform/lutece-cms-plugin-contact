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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Contact objects
 */
public final class ContactListDAO implements IContactListDAO
{
    // Constants
    private static final String SQL_QUERY_NEWPK = "SELECT max( id_contact_list ) FROM contact_list ";
    private static final String SQL_QUERY_INSERT = "INSERT INTO contact_list VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_QUERY_SELECT = "SELECT id_contact_list, label_contact_list, description_contact_list, workgroup_key, role FROM contact_list WHERE id_contact_list = ?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM contact_list WHERE id_contact_list = ?";
    private static final String SQL_QUERY_UPDATE = "UPDATE contact_list SET label_contact_list = ?, description_contact_list = ?, workgroup_key = ?, role = ? WHERE id_contact_list = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_contact_list, label_contact_list, description_contact_list,workgroup_key, role, contact_list_order FROM contact_list ORDER BY contact_list_order";
    private static final String SQL_QUERY_COUNT_CONTACTS_FOR_LIST = "SELECT COUNT(*) FROM contact_list_contact WHERE id_contact_list = ?";
    private static final String SQL_QUERY_COUNT_LISTS_FOR_CONTACT = "SELECT COUNT(*) FROM contact_list_contact WHERE id_contact = ?";
    private static final String SQL_QUERY_SELECT_CONTACTS_FOR_LIST = "SELECT a.id_contact, b.description, b.email, a.contact_order FROM contact_list_contact a, contact b WHERE id_contact_list = ? AND b.id_contact=a.id_contact ORDER BY a.contact_order";
    private static final String SQL_QUERY_SELECT_CONTACT_IN_LIST = "SELECT COUNT(*) FROM contact_list_contact WHERE id_contact_list = ? AND id_contact = ?";
    private static final String SQL_QUERY_ASSIGN = "INSERT INTO contact_list_contact(id_contact_list, id_contact, contact_order) VALUES (?, ?, ?)";
    private static final String SQL_QUERY_SELECT_BY_ROLE_KEY = "SELECT id_contact_list, label_contact_list, description_contact_list,workgroup_key, role FROM contact_list WHERE role = ? ";
    private static final String SQL_QUERY_UNASSIGN = "DELETE FROM contact_list_contact WHERE id_contact_list = ? AND id_contact = ?";
    private static final String SQL_QUERY_UNASSIGN_CONTACTS_FOR_LIST = "DELETE FROM contact_list_contact WHERE id_contact_list = ?";
    private static final String SQL_QUERY_UNASSIGN_LISTS_FOR_CONTACT = "DELETE FROM contact_list_contact WHERE id_contact = ?";
    private static final String SQL_QUERY_SELECT_NOT_ASSIGNED_CONTACTS_FOR_LIST = "SELECT id_contact, description, email, workgroup_key FROM contact WHERE id_contact NOT IN (SELECT id_contact from contact_list_contact WHERE id_contact_list = ?)";
    private static final String SQL_QUERY_SELECT_ASSIGNED_LISTS_FOR_CONTACT = "SELECT a.id_contact_list, b.label_contact_list, b.description_contact_list, b.workgroup_key FROM contact_list_contact a, contact_list b WHERE id_contact = ? AND b.id_contact_list = a.id_contact_list";
    private static final String SQL_QUERY_SELECT_NOT_ASSIGNED_LISTS_FOR_CONTACT = "SELECT id_contact_list, label_contact_list, description_contact_list, workgroup_key FROM contact_list WHERE id_contact_list NOT IN (SELECT id_contact_list from contact_list_contact WHERE id_contact = ?)";
    private static final String SQL_QUERY_COUNT_LISTS = "SELECT COUNT(*) FROM contact_list WHERE id_contact_list = ?";
    private static final String SQL_QUERY_SELECT_MAX_CONTACT_ORDER = "SELECT max(contact_order) FROM contact_list_contact WHERE id_contact_list = ?";

    // CONTACT_LIST ORDER
    private static final String SQL_QUERY_SELECT_CONTACT_LIST_ID_BY_ORDER = "SELECT id_contact_list FROM contact_list WHERE contact_list_order = ?";
    private static final String SQL_QUERY_SELECT_CONTACT_LIST_ORDER_BY_ID = "SELECT contact_list_order FROM contact_list WHERE id_contact_list = ?";
    private static final String SQL_QUERY_SELECT_MAX_CONTACT_LIST_ORDER = "SELECT max(contact_list_order) FROM contact_list";
    private static final String SQL_QUERY_UPDATE_CONTACT_LIST_ORDER = "UPDATE contact_list SET contact_list_order = ?  WHERE id_contact_list = ?";

    ///////////////////////////////////////////////////////////////////////////////////////
    //Access methods to data

    /**
     * Generates a new primary key
     * @param plugin The plugin
     * @return The new primary key
     */
    private int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEWPK, plugin );
        daoUtil.executeQuery(  );

        int nKey;

        if ( !daoUtil.next(  ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free(  );

        return nKey;
    }

    ////////////////////////////////////////////////////////////////////////
    // Methods using a dynamic pool

    /**
     * Insert a new record in the table.
     * @param contactList the instance of contactList to insert into DB
     * @param plugin the plugin contact
     */
    public void insert( ContactList contactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        contactList.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, contactList.getId(  ) );
        daoUtil.setString( 2, contactList.getLabel(  ) );
        daoUtil.setString( 3, contactList.getDescription(  ) );
        daoUtil.setString( 4, contactList.getWorkgroup(  ) );
        daoUtil.setString( 5, contactList.getRole(  ) );
        daoUtil.setInt( 6, maxOrderContactList( plugin ) + 1 );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Load the data of Contact from the table
     * @param nContactListId the Id of the contactList to load
     * @param plugin the plugin contact
     * @return the instance of contactList object loaded
     */
    public ContactList load( int nContactListId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nContactListId );
        daoUtil.executeQuery(  );

        ContactList contactList = null;

        if ( daoUtil.next(  ) )
        {
            contactList = new ContactList(  );
            contactList.setId( daoUtil.getInt( 1 ) );
            contactList.setLabel( daoUtil.getString( 2 ) );
            contactList.setDescription( daoUtil.getString( 3 ) );
            contactList.setWorkgroup( daoUtil.getString( 4 ) );
            contactList.setRole( daoUtil.getString( 5 ) );
        }

        daoUtil.free(  );

        return contactList;
    }

    /**
     * Load the list of contactsList
     *
     * @param plugin The plugin
     * @return The Collection of the Contacts
     */
    public Collection<ContactList> selectAll( Plugin plugin )
    {
        Collection<ContactList> contactListsList = new ArrayList<ContactList>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ContactList contactList = new ContactList(  );
            contactList.setId( daoUtil.getInt( 1 ) );
            contactList.setLabel( daoUtil.getString( 2 ) );
            contactList.setDescription( daoUtil.getString( 3 ) );
            contactList.setContactsNumber( countContactsForList( daoUtil.getInt( 1 ), plugin ) );
            contactList.setWorkgroup( daoUtil.getString( 4 ) );
            contactList.setRole( daoUtil.getString( 5 ) );
            contactList.setContactListOrder( daoUtil.getInt( 6 ) );
            contactListsList.add( contactList );
        }

        daoUtil.free(  );

        return contactListsList;
    }

    /**
     * Selects lists for a role key
     * @param strRoleKey The role key
     * @param plugin the plugin contact
     * @return collection of lists
     */
    public Collection<ContactList> selectByRoleKey( String strRoleKey, Plugin plugin )
    {
        Collection<ContactList> contactListsList = new ArrayList<ContactList>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_ROLE_KEY, plugin );
        daoUtil.setString( 1, strRoleKey );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            ContactList contactList = new ContactList(  );
            contactList.setId( daoUtil.getInt( 1 ) );
            contactList.setLabel( daoUtil.getString( 2 ) );
            contactList.setDescription( daoUtil.getString( 3 ) );
            contactList.setContactsNumber( countContactsForList( daoUtil.getInt( 1 ), plugin ) );
            contactList.setWorkgroup( daoUtil.getString( 4 ) );
            contactList.setRole( daoUtil.getString( 5 ) );
            contactListsList.add( contactList );
        }

        daoUtil.free(  );

        return contactListsList;
    }

    /**
     * counts how many contacts are associated to the specified list
     * @param nIdContactList the Id of contactList
     * @param plugin the plugin contact
     * @return the number of contacts for the list
     */
    public int countContactsForList( int nIdContactList, Plugin plugin )
    {
        int nCounted = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_CONTACTS_FOR_LIST, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nCounted = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCounted;
    }

    /**
     * counts how many lists the contact is associated to
     * @param nIdContact the Id of concerned contact
     * @param plugin the plugin contact
     * @return the number of counted lists
     */
    public int countListsForContact( int nIdContact, Plugin plugin )
    {
        int nCounted = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_LISTS_FOR_CONTACT, plugin );
        daoUtil.setInt( 1, nIdContact );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nCounted = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nCounted;
    }

    /**
    * Returns true if the contactList exists
    * @return boolean the existance of the list
    * @param nIdContactList The if of contactList
    * @param plugin The Plugin object
    */
    public boolean listExists( int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_LISTS, plugin );
        daoUtil.setInt( 1, nIdContactList );

        int nCounted = 0;
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nCounted = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        if ( nCounted < 1 )
        {
            return false;
        }

        return true;
    }

    /**
     * Selects all contacts associated to a specified list
     * @param nIdContactList the id of contactList
     * @param plugin the plugin contact
     * @return list of contacts
     */
    public Collection<Contact> selectContactsForList( int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTACTS_FOR_LIST, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.executeQuery(  );

        Collection<Contact> contactsList = new ArrayList<Contact>(  );

        while ( daoUtil.next(  ) )
        {
            Contact contact = new Contact(  );
            contact.setId( daoUtil.getInt( 1 ) );
            contact.setName( daoUtil.getString( 2 ) );
            contact.setEmail( daoUtil.getString( 3 ) );
            contact.setContactOrder( daoUtil.getInt( 4 ) );
            contactsList.add( contact );
        }

        daoUtil.free(  );

        return contactsList;
    }

    /**
     * returns true if a contact is assigned to a list
     * @param nIdContact The id of the contact
     * @param nIdContactList The id of the contactList
     * @param plugin the plugin contact
     * @return boolean: true if is assigned, false if not
     */
    public boolean isAssigned( int nIdContact, int nIdContactList, Plugin plugin )
    {
        int nFound = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTACT_IN_LIST, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.setInt( 2, nIdContact );

        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nFound = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        if ( nFound < 1 )
        {
            return false;
        }

        return true;
    }

    /**
     * Inserts 2 keys in association table
     * @param nIdContact The id of the contact
     * @param nIdContactList The contact List that will be associated
     * @param plugin The plugin
     */
    public void assign( int nIdContact, int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_ASSIGN, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.setInt( 2, nIdContact );
        daoUtil.setInt( 3, maxOrderContact( nIdContactList, plugin ) + 1 );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
         * Unassigns a contact of a list, or a list of a contact
         * @param nIdContact the id of the contact
         * @param nIdContactList the id of the contactList
         * @param plugin The plugin
         */
    public void unAssign( int nIdContact, int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UNASSIGN, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.setInt( 2, nIdContact );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Selects the list of all contacts that are not assigned to the specified list
     * @param nIdContactList the id of the contact List
     * @param plugin the plugin contact
     * @return list of not assigned contacts
     */
    public Collection<Contact> selectNotAssignedContactsFor( int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NOT_ASSIGNED_CONTACTS_FOR_LIST, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.executeQuery(  );

        Collection<Contact> contactsList = new ArrayList<Contact>(  );

        while ( daoUtil.next(  ) )
        {
            Contact contact = new Contact(  );
            contact.setId( daoUtil.getInt( 1 ) );
            contact.setName( daoUtil.getString( 2 ) );
            contact.setEmail( daoUtil.getString( 3 ) );
            contact.setWorkgroup( daoUtil.getString( 4 ) );
            contactsList.add( contact );
        }

        daoUtil.free(  );

        return contactsList;
    }

    /**
     * Selects assigned lists for a contact
     * @param nIdContact the id of the contact
     * @param plugin the plugin contact
     * @return collection of lists, the contact is associated to
     */
    public Collection<ContactList> selectAssignedListsFor( int nIdContact, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ASSIGNED_LISTS_FOR_CONTACT, plugin );
        daoUtil.setInt( 1, nIdContact );
        daoUtil.executeQuery(  );

        Collection<ContactList> assignedLists = new ArrayList<ContactList>(  );

        while ( daoUtil.next(  ) )
        {
            ContactList contactList = new ContactList(  );
            contactList.setId( daoUtil.getInt( 1 ) );
            contactList.setLabel( daoUtil.getString( 2 ) );
            contactList.setDescription( daoUtil.getString( 3 ) );
            contactList.setContactsNumber( countContactsForList( daoUtil.getInt( 1 ), plugin ) );
            assignedLists.add( contactList );
        }

        daoUtil.free(  );

        return assignedLists;
    }

    /**
     * selects all lists, the contact is not associated to
     * @param nIdContact the id of the contact
     * @param plugin the plugin contact
     * @return collection of contactLists
     */
    public Collection<ContactList> selectNotAssignedListsFor( int nIdContact, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_NOT_ASSIGNED_LISTS_FOR_CONTACT, plugin );
        daoUtil.setInt( 1, nIdContact );
        daoUtil.executeQuery(  );

        Collection<ContactList> notAssignedLists = new ArrayList<ContactList>(  );

        while ( daoUtil.next(  ) )
        {
            ContactList contactList = new ContactList(  );
            contactList.setId( daoUtil.getInt( 1 ) );
            contactList.setLabel( daoUtil.getString( 2 ) );
            contactList.setDescription( daoUtil.getString( 3 ) );
            contactList.setWorkgroup( daoUtil.getString( 4 ) );
            contactList.setContactsNumber( countContactsForList( daoUtil.getInt( 1 ), plugin ) );
            notAssignedLists.add( contactList );
        }

        daoUtil.free(  );

        return notAssignedLists;
    }

    /**
     * Delete a record from the table
     * @param nIdContactList the id of contactlist to delete
     * @param plugin The plugin
     */
    public void delete( int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Unassigns all contacts for a specified list
     * @param nIdContactList the id of contactlist
     * @param plugin the plugin contact
     */
    public void unassignContactsForList( int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UNASSIGN_CONTACTS_FOR_LIST, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Unassigns all lists, the specified contact is assigned to
     * @param nIdContact the Id of the contact
     * @param plugin the plugin contact
     */
    public void unassignListsForContact( int nIdContact, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UNASSIGN_LISTS_FOR_CONTACT, plugin );
        daoUtil.setInt( 1, nIdContact );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Update the record in the table
     * @param contactList The reference of contactList
     * @param plugin The plugin
     */
    public void store( ContactList contactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nContactListId = contactList.getId(  );

        daoUtil.setString( 1, contactList.getLabel(  ) );
        daoUtil.setString( 2, contactList.getDescription(  ) );
        daoUtil.setString( 3, contactList.getWorkgroup(  ) );
        daoUtil.setString( 4, contactList.getRole(  ) );
        daoUtil.setInt( 5, nContactListId );

        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    ////////////////////////////////////////////////////////////////////////////
    // ContactList Order management

    /**
    * Modify the order of a contact
    * @param nNewOrder The order number
    * @param nId The contactList identifier
    * @param plugin The plugin
    */
    public void storeContactListOrder( int nNewOrder, int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_CONTACT_LIST_ORDER, plugin );
        daoUtil.setInt( 1, nNewOrder );
        daoUtil.setInt( 2, nId );
        daoUtil.executeUpdate(  );
        daoUtil.free(  );
    }

    /**
     * Returns a contact identifier in a distinct order
     * @return The order of the ContactList
     * @param nContactListOrder The order number
     * @param plugin The plugin
     */
    public int selectContactListIdByOrder( int nContactListOrder, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTACT_LIST_ID_BY_ORDER, plugin );
        int nResult = 0;
        daoUtil.setInt( 1, nContactListOrder );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            // If number order doesn't exist
            nResult = 1;
        }
        else
        {
            nResult = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nResult;
    }

    /**
     * Returns the order of a contactList
     * @param nIdContactList the id of contactList
     * @param plugin the plugin contact
     * @return the order of the contactList
     */
    public int selectContactListOrderById( int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTACT_LIST_ORDER_BY_ID, plugin );
        int nResult = 0;
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.executeQuery(  );

        if ( !daoUtil.next(  ) )
        {
            // If number order doesn't exist
            nResult = 1;
        }
        else
        {
            nResult = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nResult;
    }

    /**
     * Calculate the new max order in a list
     * @return the max order of contact
     * @param plugin The plugin
     */
    public int maxOrderContactList( Plugin plugin )
    {
        int nOrder = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MAX_CONTACT_LIST_ORDER, plugin );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nOrder = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nOrder;
    }

    /**
    * Calculate the new max order in a list
    * @return the max order of contact
    * @param nIdContactList the id of the contact list
    * @param plugin The plugin
    */
    public int maxOrderContact( int nIdContactList, Plugin plugin )
    {
        int nOrder = 0;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_MAX_CONTACT_ORDER, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.executeQuery(  );

        if ( daoUtil.next(  ) )
        {
            nOrder = daoUtil.getInt( 1 );
        }

        daoUtil.free(  );

        return nOrder;
    }
}
