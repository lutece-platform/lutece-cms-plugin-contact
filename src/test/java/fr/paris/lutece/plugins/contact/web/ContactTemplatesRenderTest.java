/*
 * Copyright (c) 2002-2025, City of Paris
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
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.editor.RichTextContentMethod;
import fr.paris.lutece.portal.service.template.HtmlMarkup;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.core.HTMLOutputFormat;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * Renders with HTML auto-escaping (LUT-33153) the contact templates that receive values typed by visitors or HTML from
 * Java: the contact form (captcha put in the model as markup by ContactApp, terms of service typed by an administrator
 * and written with richText()) and the body of the e-mail sent to the contact. The rich text is sanitized by the core,
 * hence the CDI container of LuteceTestCase.
 */
public class ContactTemplatesRenderTest extends LuteceTestCase
{
    private static final String TEMPLATES_ROOT = "webapp/WEB-INF/templates";
    private static final String TEMPLATE_FORM = "skin/plugins/contact/page_contact.html";
    private static final String TEMPLATE_MAIL = "skin/plugins/contact/message_contact.html";
    /** Stand-ins of the core macros page_contact.html calls, escaping their arguments as the core macros do. */
    private static final String MACROS = "contact_test_macros.ftl";
    private static final String MACROS_SOURCE = "<#macro cTpl rest...><#nested></#macro><#macro cContainer rest...><#nested></#macro>"
            + "<#macro cRow rest...><#nested></#macro><#macro cCol rest...><#nested></#macro><#macro cTitle rest...><#nested></#macro>"
            + "<#macro cForm rest...><form><#nested></form></#macro><#macro cField rest...><#nested></#macro>"
            + "<#macro cSelect name rest...><select name=\"${name}\"><#nested></select></#macro><#macro cBreadCrumb rest...></#macro>"
            + "<#macro input name value='' rest...><input name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#macro cInput name value='' rest...><input name=\"${name}\" value=\"${value}\"></#macro>"
            + "<#macro cFormHelp id label rest...><p id=\"help_${id}\">${label}</p></#macro>"
            + "<#macro cOption value label rest...><option value=\"${value}\">${label}</option></#macro>"
            + "<#macro cTextArea name rest...><textarea name=\"${name}\"><#nested></textarea></#macro>"
            + "<#macro cCheckboxList name label rest...><label>${label} <#nested></label></#macro>"
            + "<#macro cAlert title='' rest...><div class=\"alert\">${title}</div></#macro>"
            + "<#macro cBtn rest...></#macro><#macro cSetPageTitle rest...></#macro>";

    private static final String PAYLOAD = "\"><svg onload=alert(1)>";
    private static final String PAYLOAD_ESCAPED = "&quot;&gt;&lt;svg onload=alert(1)&gt;";
    private static final String CAPTCHA = "<div class=\"captcha\"><img src=\"captcha.png\" alt=\"Code\"></div>";
    private static final String TOS_KEPT = "<p>Conditions <em>generales</em></p>";
    private static final String TOS = TOS_KEPT + "<script>alert(2)</script>";

    /**
     * The signed-in user the form reads the first name from, as the template calls LuteceUser.
     */
    public static class SignedInUser
    {
        /**
         * Gives the payload for any user information.
         *
         * @param strKey
         *            the key of the information
         * @return the payload
         */
        public String getUserInfo( String strKey )
        {
            return PAYLOAD;
        }
    }

    /**
     * The configuration of AbstractFreeMarkerTemplateService for HTML templates, with the richText() method of the core.
     *
     * @return the configuration
     * @throws IOException
     *             if the templates directory cannot be read
     */
    private static Configuration configuration( ) throws IOException
    {
        StringTemplateLoader macros = new StringTemplateLoader( );
        macros.putTemplate( MACROS, MACROS_SOURCE );
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_0 );
        cfg.setOutputFormat( HTMLOutputFormat.INSTANCE );
        cfg.setAutoEscapingPolicy( Configuration.ENABLE_IF_DEFAULT_AUTO_ESCAPING_POLICY );
        cfg.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader [ ] {
                new FileTemplateLoader( new File( TEMPLATES_ROOT ) ), macros
        } ) );
        cfg.addAutoInclude( MACROS );
        cfg.setSharedVariable( "richText", new RichTextContentMethod( ) );

        return cfg;
    }

    /**
     * Renders a template of the plugin.
     *
     * @param strTemplate
     *            the template path
     * @param model
     *            the model
     * @return the rendering
     * @throws IOException
     *             if the template cannot be read
     * @throws TemplateException
     *             if the rendering fails
     */
    private static String render( String strTemplate, Map<String, ? extends Object> model ) throws IOException, TemplateException
    {
        StringWriter writer = new StringWriter( );
        configuration( ).getTemplate( strTemplate ).process( model, writer );

        return writer.toString( );
    }

    /**
     * The model of the contact form as ContactApp builds it, every value typed by the visitor set to the payload.
     *
     * @return the model
     */
    private static Map<String, Object> formModel( )
    {
        ReferenceList listContacts = new ReferenceList( );
        listContacts.addItem( "0", "Choisir" );
        listContacts.addItem( "1", "Accueil " + PAYLOAD );

        Map<String, Object> model = new HashMap<>( );
        model.put( "portal_url", "jsp/site/Portal.jsp" );
        model.put( "is_active_captcha", Boolean.TRUE );
        model.put( "captcha", HtmlMarkup.of( CAPTCHA ) );
        model.put( "contacts_list", listContacts );
        model.put( "visitor_last_name", PAYLOAD );
        model.put( "visitor_first_name", PAYLOAD );
        model.put( "visitor_email", PAYLOAD );
        model.put( "visitor_address", PAYLOAD );
        model.put( "message_object", PAYLOAD );
        model.put( "message", PAYLOAD + "</textarea>" );
        model.put( "id_contact_list", 1 );
        model.put( "is_tos_required", Boolean.TRUE );
        model.put( "tos_message", TOS );
        model.put( "accept_tos", Boolean.FALSE );
        model.put( "alert_msg", PAYLOAD );
        model.put( "default_contact", "0" );

        return model;
    }

    /**
     * The contact form writes the values typed by the visitor escaped, the captcha and the mark of the mandatory fields
     * as HTML, and the terms of service as sanitized rich text.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testFormEscapesTheVisitorValuesAndKeepsTheHtml( ) throws Exception
    {
        String strHtml = render( TEMPLATE_FORM, formModel( ) );

        assertFalse( strHtml.contains( "<svg" ), strHtml );
        assertTrue( strHtml.contains( "value=\"" + PAYLOAD_ESCAPED + "\"" ), strHtml );
        assertTrue( strHtml.contains( ">" + PAYLOAD_ESCAPED + "&lt;/textarea&gt;</textarea>" ), strHtml );
        assertTrue( strHtml.contains( CAPTCHA ), strHtml );
        assertTrue( strHtml.contains( "<span class=\"text-danger\">*</span>" ), strHtml );
        assertTrue( strHtml.contains( TOS_KEPT ), strHtml );
        assertFalse( strHtml.contains( "alert(2)" ), strHtml );
    }

    /**
     * The first name of a signed-in user is written escaped too.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testFormEscapesTheFirstNameOfTheUser( ) throws Exception
    {
        Map<String, Object> model = formModel( );
        model.put( "visitor_first_name", "" );
        model.put( "mylutece_user", new SignedInUser( ) );

        String strHtml = render( TEMPLATE_FORM, model );

        assertFalse( strHtml.contains( "<svg" ), strHtml );
        assertTrue( strHtml.contains( "name=\"visitor_first_name\" value=\"" + PAYLOAD_ESCAPED + "\"" ), strHtml );
    }

    /**
     * The body of the e-mail sent to the contact writes the values typed by the visitor as text.
     *
     * @throws Exception
     *             if the rendering fails
     */
    @Test
    public void testMailEscapesTheVisitorValues( ) throws Exception
    {
        Map<String, String> model = new HashMap<>( );
        model.put( "visitor_last_name", PAYLOAD );
        model.put( "visitor_first_name", PAYLOAD );
        model.put( "visitor_address", PAYLOAD );
        model.put( "visitor_email", PAYLOAD );
        model.put( "contact_name", "Accueil" );
        model.put( "message", "<b>Bonjour</b> & " + PAYLOAD );
        model.put( "current_date", "13/11/2026" );

        String strHtml = render( TEMPLATE_MAIL, model );

        assertFalse( strHtml.contains( "<svg" ), strHtml );
        assertFalse( strHtml.contains( "<b>" ), strHtml );
        assertTrue( strHtml.contains( "&lt;b&gt;Bonjour&lt;/b&gt; &amp; " + PAYLOAD_ESCAPED ), strHtml );
        assertTrue( strHtml.contains( "<strong>" ), strHtml );
    }
}
