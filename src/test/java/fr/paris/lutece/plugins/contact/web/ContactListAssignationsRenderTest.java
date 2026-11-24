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

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.plugins.contact.business.Contact;
import fr.paris.lutece.plugins.contact.business.ContactList;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;

/**
 * The assignation page of a contact list: the order form of every assigned contact, the first one included, is a form of its own, not nested in
 * the form that assigns contacts (a browser drops a form start tag met inside an open form)
 */
public class ContactListAssignationsRenderTest extends LuteceTestCase
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE = "admin/plugins/contact/manage_list_assignations.html";
    private static final String TEMPLATE_CONTACT = "admin/plugins/contact/manage_contact_assignations.html";
    private static final String MACROS = "contact_assignations_test_macros.ftl";
    /** Stand-ins of the BO macros of the core: forms, selects and buttons are written with their attributes, the other macros keep their content */
    private static final String MACROS_SOURCE = "<#macro attrs params><#list params as k, v> ${k}=\"${v}\"</#list></#macro>"
            + "<#macro tform name='' id='' params={} rest...><form name=\"${name}\" id=\"${id}\"<@attrs params/>><#nested></form></#macro>"
            + "<#macro select name id=name params={} rest...><select name=\"${name}\" id=\"${id}\"<@attrs params/>></select></#macro>"
            + "<#macro button params={} rest...><button type=\"submit\"<@attrs params/>></button></#macro>"
            + "<#macro input name value='' rest...><input name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#list ['pageContainer','pageColumn','pageHeader','row','columns','box','div','manageFeature','manageFeatureItem',"
            + "'manageFeatureItemColumn','p','formGroup','inputGroup'] as m><@'<#macro ${m} rest...><#nested></#macro>'?interpret /></#list>"
            + "<#macro aButton rest...></#macro><#macro offcanvas rest...></#macro><#macro alert rest...></#macro>";

    @Test
    public void testOrderFormsAreNotNested( ) throws Exception
    {
        String strHtml = render( );

        // the form that assigns contacts, then for each of the 2 assigned contacts its order form and its unassign form
        assertEquals( 5, countNotNestedForms( strHtml ) );
        assertTrue( strHtml.contains( "<select name=\"list_contact\" id=\"list_contact\" form=\"assignContact\">" ), strHtml );
        assertTrue( strHtml.contains( "<button type=\"submit\" form=\"assignContact\">" ), strHtml );
        assertTrue( strHtml.contains( "<input name=\"action\" value=\"actionRevokeContactsFromList\">" ), strHtml );
    }

    /**
     * The assignation page of a contact: the unassign forms of the assigned lists are not nested in the form that assigns lists
     */
    @Test
    public void testUnassignFormsOfAContactAreNotNested( ) throws Exception
    {
        Contact contact = new Contact( );
        contact.setId( 4 );
        contact.setName( "Contact" );
        contact.setEmail( "contact@example.org" );
        List<ContactList> listAssigned = new ArrayList<>( );
        for ( int i = 1; i <= 2; i++ )
        {
            ContactList contactList = new ContactList( );
            contactList.setId( i );
            contactList.setLabel( "List " + i );
            listAssigned.add( contactList );
        }
        ReferenceList listNotAssigned = new ReferenceList( );
        listNotAssigned.addItem( "3", "List 3" );
        Map<String, Object> model = new HashMap<>( );
        model.put( "contact", contact );
        model.put( "not_assigned_lists", listNotAssigned );
        model.put( "assigned_lists", listAssigned );

        String strHtml = render( TEMPLATE_CONTACT, model );

        assertEquals( 3, countNotNestedForms( strHtml ) );
        assertTrue( strHtml.contains( "<select name=\"list_contact\" id=\"list_contact\" form=\"assignContact\">" ), strHtml );
        assertTrue( strHtml.contains( "<button type=\"submit\" form=\"assignContact\">" ), strHtml );
        assertTrue( strHtml.contains( "<input name=\"action\" value=\"actionRevokeListsFromContact\">" ), strHtml );
    }

    private static int countNotNestedForms( String strHtml )
    {
        int nDepth = 0;
        int nForms = 0;
        Matcher matcher = Pattern.compile( "<form|</form>" ).matcher( strHtml );
        while ( matcher.find( ) )
        {
            if ( matcher.group( ).equals( "<form" ) )
            {
                nDepth++;
                nForms++;
                assertTrue( nDepth == 1, "a form is opened inside another form: " + strHtml );
            }
            else
            {
                nDepth--;
            }
        }
        return nForms;
    }

    private static String render( ) throws Exception
    {
        ContactList contactList = new ContactList( );
        contactList.setId( 7 );
        contactList.setLabel( "List" );
        List<Contact> listAssigned = new ArrayList<>( );
        for ( int i = 1; i <= 2; i++ )
        {
            Contact contact = new Contact( );
            contact.setId( i );
            contact.setName( "Contact " + i );
            contact.setEmail( "contact" + i + "@example.org" );
            contact.setContactOrder( i );
            listAssigned.add( contact );
        }
        ReferenceList listContacts = new ReferenceList( );
        listContacts.addItem( "3", "Contact 3" );
        ReferenceList listOrder = new ReferenceList( );
        listOrder.addItem( "1", "1" );
        listOrder.addItem( "2", "2" );

        Map<String, Object> model = new HashMap<>( );
        model.put( "contact_list", contactList );
        model.put( "contacts_number", 2 );
        model.put( "list_contacts", listContacts );
        model.put( "assigned_contacts", listAssigned );
        model.put( "order_list", listOrder );

        return render( TEMPLATE, model );
    }

    private static String render( String strTemplate, Map<String, Object> model ) throws Exception
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_32 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), macros
        } ) );
        cfg.addAutoInclude( MACROS );

        StringWriter writer = new StringWriter( );
        cfg.getTemplate( strTemplate ).process( model, writer );
        return writer.toString( );
    }
}
