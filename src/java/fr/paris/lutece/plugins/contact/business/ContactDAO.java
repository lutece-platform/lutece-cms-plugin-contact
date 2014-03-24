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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;


/**
 * This class provides Data Access methods for Contact objects
 */
public final class ContactDAO implements IContactDAO
{
    // Constants
    private static final String SQL_QUERY_NEWPK = "SELECT max( id_contact ) FROM contact ";
    private static final String SQL_QUERY_SELECT = "SELECT id_contact, description, email, workgroup_key FROM contact WHERE id_contact = ? ";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_contact, description, email, workgroup_key, hits FROM contact ORDER BY id_contact DESC";
    private static final String SQL_QUERY_INSERT = "INSERT INTO contact ( id_contact , description, email, workgroup_key, hits )  VALUES ( ? , ? , ?, ?, 0 ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM contact WHERE id_contact = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE contact SET description = ? , email = ?, workgroup_key = ?  WHERE id_contact = ?  ";
    private static final String SQL_QUERY_SELECTALL_BY_LIST = "SELECT a.id_contact, b.description, b.email, a.contact_order FROM contact_list_contact a, contact b WHERE id_contact_list = ? AND b.id_contact=a.id_contact ORDER BY a.contact_order";
    private static final String SQL_QUERY_SELECT_CONTACT_HITS = "SELECT hits FROM contact WHERE id_contact = ?";
    private static final String SQL_QUERY_SELECT_CONTACT_IN_LIST_HITS = "SELECT hits FROM contact_list_contact WHERE id_contact = ? AND id_contact_list = ?";
    private static final String SQL_QUERY_UPDATE_CONTACT_HITS = "UPDATE contact SET hits = ? WHERE id_contact = ?";
    private static final String SQL_QUERY_UPDATE_CONTACT_IN_LIST_HITS = "UPDATE contact_list_contact SET hits = ? WHERE id_contact = ? AND id_contact_list = ?";

    // CONTACT ORDER
    private static final String SQL_QUERY_SELECT_CONTACT_ID_BY_ORDER = "SELECT id_contact FROM contact_list_contact WHERE contact_order = ? AND id_contact_list = ?";
    private static final String SQL_QUERY_SELECT_CONTACT_ORDER_BY_ID = "SELECT contact_order FROM contact_list_contact WHERE id_contact = ? AND id_contact_list = ?";
    private static final String SQL_QUERY_UPDATE_CONTACT_ORDER = "UPDATE contact_list_contact SET contact_order = ?  WHERE id_contact = ? AND id_contact_list = ?";

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
        daoUtil.executeQuery( );

        int nKey;

        if ( !daoUtil.next( ) )
        {
            // if the table is empty
            nKey = 1;
        }

        nKey = daoUtil.getInt( 1 ) + 1;

        daoUtil.free( );

        return nKey;
    }

    ////////////////////////////////////////////////////////////////////////
    // Methods using a dynamic pool

    /**
     * Insert a new record in the table.
     * 
     * @param contact The contact object
     * @param plugin The plugin
     */
    public void insert( Contact contact, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        contact.setId( newPrimaryKey( plugin ) );
        daoUtil.setInt( 1, contact.getId( ) );
        daoUtil.setString( 2, contact.getName( ) );
        daoUtil.setString( 3, contact.getEmail( ) );
        daoUtil.setString( 4, contact.getWorkgroup( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load the data of Contact from the table
     * @param nContactId The identifier of Contact
     * @param plugin The plugin
     * @return the instance of the Contact
     */
    public Contact load( int nContactId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nContactId );
        daoUtil.executeQuery( );

        Contact contact = null;

        if ( daoUtil.next( ) )
        {
            contact = new Contact( );
            contact.setId( daoUtil.getInt( 1 ) );
            contact.setName( daoUtil.getString( 2 ) );
            contact.setEmail( daoUtil.getString( 3 ) );
            contact.setWorkgroup( daoUtil.getString( 4 ) );
        }

        daoUtil.free( );

        return contact;
    }

    /**
     * Delete a record from the table
     * @param contact The Contact object
     * @param plugin The plugin
     */
    public void delete( Contact contact, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, contact.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Update the record in the table
     * @param contact The reference of contact
     * @param plugin The plugin
     */
    public void store( Contact contact, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        int nContactId = contact.getId( );

        daoUtil.setString( 1, contact.getName( ) );
        daoUtil.setString( 2, contact.getEmail( ) );
        daoUtil.setString( 3, contact.getWorkgroup( ) );
        daoUtil.setInt( 4, nContactId );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load the list of contacts
     * @return The Collection of the Contacts with the string at the top
     * @param nIdContactList The id of contact List
     * @param strComboItem the string to display at the top of the list
     * @param plugin The plugin
     */
    public ReferenceList selectContactsByListWithString( int nIdContactList, String strComboItem, Plugin plugin )
    {
        ReferenceList contactList = new ReferenceList( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_LIST, plugin );
        daoUtil.setInt( 1, nIdContactList );
        daoUtil.executeQuery( );
        contactList.addItem( 0, strComboItem );

        while ( daoUtil.next( ) )
        {
            contactList.addItem( daoUtil.getInt( 1 ), daoUtil.getString( 2 ) );
        }

        daoUtil.free( );

        return contactList;
    }

    /**
     * Load the list of contacts
     * 
     * @param plugin The plugin
     * @return The Collection of the Contacts
     */
    public Collection<Contact> selectAll( Plugin plugin )
    {
        Collection<Contact> contactList = new ArrayList<Contact>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            Contact contact = new Contact( );
            contact.setId( daoUtil.getInt( 1 ) );
            contact.setName( daoUtil.getString( 2 ) );
            contact.setEmail( daoUtil.getString( 3 ) );
            contact.setWorkgroup( daoUtil.getString( 4 ) );
            contact.setHits( daoUtil.getInt( 5 ) );
            contactList.add( contact );
        }

        daoUtil.free( );

        return contactList;
    }

    /**
     * Update hits of the contact in contact and contact_list_contact tables
     * @param nIdContact id of the contact
     * @param nIdContactList id of contact list
     * @param plugin the plugin Contact
     */
    public void updateHits( int nIdContact, int nIdContactList, Plugin plugin )
    {
        int nNewContactInListHits;
        int nNewContactHits;

        nNewContactHits = checkContactHits( nIdContact, plugin ) + 1;
        nNewContactInListHits = checkContactInListHits( nIdContact, nIdContactList, plugin ) + 1;

        updateContactHits( nIdContact, nNewContactHits, plugin );
        updateContactInListHits( nIdContact, nIdContactList, nNewContactInListHits, plugin );
    }

    /**
     * Returns the number of hits of a contact
     * @param nIdContact id of the contact
     * @param plugin the plugin Contact
     * @return the number of hits
     */
    private int checkContactHits( int nIdContact, Plugin plugin )
    {
        int nHits = 0;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTACT_HITS, plugin );
        daoUtil.setInt( 1, nIdContact );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            nHits = daoUtil.getInt( 1 );
        }

        daoUtil.free( );

        return nHits;
    }

    /**
     * updates the number of hits of a contact
     * @param nIdContact id of the contact
     * @param nNewContactHits new number of hits
     * @param plugin the plugin Contact
     */
    private void updateContactHits( int nIdContact, int nNewContactHits, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_CONTACT_HITS, plugin );
        daoUtil.setInt( 1, nNewContactHits );
        daoUtil.setInt( 2, nIdContact );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Returns the number of hits of a contact in a specified list
     * @param nIdContact id of the contact
     * @param nIdContactList id of contact list
     * @param plugin the plugin Contact
     * @return the number of hits
     */
    private int checkContactInListHits( int nIdContact, int nIdContactList, Plugin plugin )
    {
        int nHits = 0;

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTACT_IN_LIST_HITS, plugin );
        daoUtil.setInt( 1, nIdContact );
        daoUtil.setInt( 2, nIdContactList );
        daoUtil.executeQuery( );

        if ( daoUtil.next( ) )
        {
            nHits = daoUtil.getInt( 1 );
        }

        daoUtil.free( );

        return nHits;
    }

    /**
     * updates the number of hits of a contact in a specified list
     * @param nIdContact id of the contact
     * @param nIdContactList id of contact list
     * @param nNewContactInListHits the new number of hits
     * @param plugin the plugin Contact
     */
    private void updateContactInListHits( int nIdContact, int nIdContactList, int nNewContactInListHits, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_CONTACT_IN_LIST_HITS, plugin );
        daoUtil.setInt( 1, nNewContactInListHits );
        daoUtil.setInt( 2, nIdContact );
        daoUtil.setInt( 3, nIdContactList );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    ////////////////////////////////////////////////////////////////////////////
    // Contact Order management

    /**
     * Modify the order of a contact
     * @param nIdContactList The id of contactList
     * @param nNewOrder The order number
     * @param nId The contact identifier
     * @param plugin The plugin
     */
    public void storeContactOrder( int nNewOrder, int nId, int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE_CONTACT_ORDER, plugin );
        daoUtil.setInt( 1, nNewOrder );
        daoUtil.setInt( 2, nId );
        daoUtil.setInt( 3, nIdContactList );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Returns a contact identifier in a distinct order
     * @return The order of the Contact
     * @param nIdContactList the id of the contact List
     * @param nContactOrder The order number
     * @param plugin The plugin
     */
    public int selectContactIdByOrder( int nContactOrder, int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTACT_ID_BY_ORDER, plugin );
        int nResult;
        daoUtil.setInt( 1, nContactOrder );
        daoUtil.setInt( 2, nIdContactList );
        daoUtil.executeQuery( );

        if ( !daoUtil.next( ) )
        {
            // If number order doesn't exist
            nResult = 1;
        }
        else
        {
            nResult = daoUtil.getInt( 1 );
        }

        daoUtil.free( );

        return nResult;
    }

    /**
     * Returns the order of a contact in a list, using the Identifier
     * @param nIdContact the id of contact
     * @param nIdContactList the id of contactList
     * @param plugin the plugin contact
     * @return the order of the contact in the list
     */
    public int selectContactOrderById( int nIdContact, int nIdContactList, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CONTACT_ORDER_BY_ID, plugin );
        int nResult;
        daoUtil.setInt( 1, nIdContact );
        daoUtil.setInt( 2, nIdContactList );
        daoUtil.executeQuery( );

        if ( !daoUtil.next( ) )
        {
            // If number order doesn't exist
            nResult = 1;
        }
        else
        {
            nResult = daoUtil.getInt( 1 );
        }

        daoUtil.free( );

        return nResult;
    }
}
