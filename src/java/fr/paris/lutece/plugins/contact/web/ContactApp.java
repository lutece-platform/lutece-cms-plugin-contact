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
import fr.paris.lutece.plugins.contact.service.ContactPlugin;
import fr.paris.lutece.portal.service.captcha.CaptchaSecurityService;
import fr.paris.lutece.portal.service.content.XPageAppService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.web.xpages.XPageApplication;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.date.DateUtil;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.string.StringUtil;
import fr.paris.lutece.util.url.UrlItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * This class manages Contact page.
 */
public class ContactApp implements XPageApplication
{
    /** Serial id */
    private static final long serialVersionUID = 6553298772139973292L;

    ////////////////////////////////////////////////////////////////////////////
    // Constants
    private static final String TEMPLATE_XPAGE_CONTACT = "skin/plugins/contact/page_contact.html";
    private static final String TEMPLATE_XPAGE_LISTS = "skin/plugins/contact/page_lists.html";
    private static final String TEMPLATE_MESSAGE_CONTACT = "skin/plugins/contact/message_contact.html";
    private static final String MARK_CONTACTS_LIST = "contacts_list";
    private static final String MARK_DEFAULT_CONTACT = "default_contact";
    private static final String MARK_CONTACT_ALERT = "alert";
    private static final String MARK_VISITOR_LASTNAME = "visitor_last_name";
    private static final String MARK_VISITOR_FIRSTNAME = "visitor_first_name";
    private static final String MARK_VISITOR_ADDRESS = "visitor_address";
    private static final String MARK_VISITOR_EMAIL = "visitor_email";
    private static final String MARK_OBJECT = "message_object";
    private static final String MARK_MESSAGE = "message";
    private static final String MARK_STYLE_LAST_NAME = "style_last_name";
    private static final String MARK_STYLE_FIRST_NAME = "style_first_name";
    private static final String MARK_STYLE_EMAIL = "style_email";
    private static final String MARK_STYLE_OBJECT = "style_object";
    private static final String MARK_STYLE_MESSAGE = "style_message";
    private static final String MARK_STYLE_CONTACT = "style_contact";
    private static final String MARK_PORTAL_URL = "portal_url";
    private static final String MARK_CONTACT_NAME = "contact_name";
    private static final String MARK_CURRENT_DATE = "current_date";
    private static final String MARK_CAPTCHA = "captcha";
    private static final String MARK_IS_ACTIVE_CAPTCHA = "is_active_captcha";
    //private static final String MARK_ROLE = "role";
    private static final String MARK_LIST_OF_LISTS = "list_of_lists";
    private static final String MARK_ID_CONTACT_LIST = "id_contact_list";
    private static final String MARK_MYLUTECE_USER = "mylutece_user";
    private static final String PARAMETER_PAGE = "page";
    private static final String PARAMETER_CONTACT = "contact";
    private static final String PARAMETER_VISITOR_LASTNAME = "visitor_last_name";
    private static final String PARAMETER_VISITOR_FIRSTNAME = "visitor_first_name";
    private static final String PARAMETER_VISITOR_ADDRESS = "visitor_address";
    private static final String PARAMETER_VISITOR_EMAIL = "visitor_email";
    private static final String PARAMETER_MESSAGE_OBJECT = "message_object";
    private static final String PARAMETER_MESSAGE = "message";
    private static final String PARAMETER_SEND = "send";
    private static final String PARAMETER_PORTAL_URL = "portal_url";
    private static final String PARAMETER_ID_CONTACT_LIST = "id_contact_list";
    private static final String PROPERTY_SENDING_OK = "contact.message_contact.sending.ok";
    private static final String PROPERTY_MANDATORY_FIELD_MISSING = "contact.message_contact.mandatory.field";
    private static final String PROPERTY_SENDING_NOK = "contact.message_contact.sending.nok";
    private static final String PROPERTY_RECIPIENT_MISSING = "contact.message_contact.recipient.missing";
    private static final String PROPERTY_ERROR_EMAIL = "contact.message_contact.error.email";
    private static final String PROPERTY_COMBO_CHOOSE = "contact.message_contact.comboChoose";
    private static final String PROPERTY_CAPTCHA_ERROR = "contact.message_contact.captchaError";
    private static final String PROPERTY_PAGE_TITLE = "contact.pageTitle";
    private static final String PROPERTY_PAGE_PATH = "contact.pagePathLabel";
    //private static final String PROPERTY_NO_ID_FOUND = "contact.message_contact.noIdFound";
    private static final String PROPERTY_LIST_NOT_EXISTS = "contact.message_contact.listNotExists";
    private static final String PROPERTY_NOT_AUTHORIZED = "contact.message_contact.notauthorized";
    private static final String PROPERTY_NO_LIST_VISIBLE = "contact.message_contact.noListVisible";
    private static final String JCAPTCHA_PLUGIN = "jcaptcha";
    private static final String EMPTY_STRING = "";

    //Captcha
    private CaptchaSecurityService _captchaService;

    // private fields
    private Plugin _plugin;

    /**
     * Returns the content of the page Contact. It is composed by a form which
     * to capture the data to send a message to
     * a contact of the portal.
     * @return the Content of the page Contact
     * @param request The http request
     * @param nMode The current mode
     * @param plugin The plugin object
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException
     *             Message displayed if an exception occures
     */
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin ) throws SiteMessageException
    {
        XPage page = new XPage( );

        String strPluginName = request.getParameter( PARAMETER_PAGE );
        _plugin = PluginService.getPlugin( strPluginName );

        page.setTitle( AppPropertiesService.getProperty( PROPERTY_PAGE_TITLE ) );
        page.setPathLabel( AppPropertiesService.getProperty( PROPERTY_PAGE_PATH ) );

        String strSendMessage = request.getParameter( PARAMETER_SEND );
        String strIdContactList = request.getParameter( PARAMETER_ID_CONTACT_LIST );

        if ( strIdContactList != null )
        {
            page.setContent( getForm( request, strIdContactList, strSendMessage ) );
        }
        else
        {
            page.setContent( getLists( request ) );
        }

        return page;
    }

    /**
     * Checks if the page is visible for the current user
     * @param request The HTTP request
     * @param strRole the role to check
     * @return true if the page could be shown to the user
     * @since v1.3.1
     */
    private boolean isVisible( HttpServletRequest request, String strRole )
    {
        if ( ( strRole == null ) || ( strRole.trim( ).equals( EMPTY_STRING ) ) )
        {
            return true;
        }

        if ( !strRole.equals( ContactList.ROLE_NONE ) && SecurityService.isAuthenticationEnable( ) )
        {
            return SecurityService.getInstance( ).isUserInRole( request, strRole );
        }

        return true;
    }

    /**
     * Return form
     * @param request the page request
     * @param strIdContactList the id of contact list
     * @param strSendMessage the send message
     * @return the corresponding form
     * @throws SiteMessageException occurs during treatment
     */
    private String getForm( HttpServletRequest request, String strIdContactList, String strSendMessage )
            throws SiteMessageException
    {
        String strPortalUrl = request.getRequestURI( );
        Map<String, Serializable> model = new HashMap<String, Serializable>( );

        model.put( MARK_PORTAL_URL, strPortalUrl );

        boolean bIsCaptchaEnabled = PluginService.isPluginEnable( JCAPTCHA_PLUGIN );
        model.put( MARK_IS_ACTIVE_CAPTCHA, bIsCaptchaEnabled );

        if ( bIsCaptchaEnabled )
        {
            _captchaService = new CaptchaSecurityService( );
            model.put( MARK_CAPTCHA, _captchaService.getHtmlCode( ) );
        }

        String strVisitorLastName = ( request.getParameter( PARAMETER_VISITOR_LASTNAME ) != null ) ? request
                .getParameter( PARAMETER_VISITOR_LASTNAME ) : "";
        String strVisitorFirstName = ( request.getParameter( PARAMETER_VISITOR_FIRSTNAME ) != null ) ? request
                .getParameter( PARAMETER_VISITOR_FIRSTNAME ) : "";
        String strVisitorEmail = ( request.getParameter( PARAMETER_VISITOR_EMAIL ) != null ) ? request
                .getParameter( PARAMETER_VISITOR_EMAIL ) : "";
        String strVisitorAddress = ( request.getParameter( PARAMETER_VISITOR_ADDRESS ) != null ) ? request
                .getParameter( PARAMETER_VISITOR_ADDRESS ) : "";
        String strObject = ( request.getParameter( PARAMETER_MESSAGE_OBJECT ) != null ) ? request
                .getParameter( PARAMETER_MESSAGE_OBJECT ) : "";
        String strMessage = ( request.getParameter( PARAMETER_MESSAGE ) != null ) ? request
                .getParameter( PARAMETER_MESSAGE ) : "";
        String strContact = ( request.getParameter( PARAMETER_CONTACT ) != null ) ? request
                .getParameter( PARAMETER_CONTACT ) : "";

        if ( strSendMessage != null )
        {
            String strStyleLastName = strVisitorLastName.equals( "" ) ? "error" : "";
            String strStyleFirstName = strVisitorFirstName.equals( "" ) ? "error" : "";
            String strStyleEmail = ( strVisitorEmail.equals( "" ) || ( StringUtil.checkEmail( strVisitorEmail ) != true ) ) ? "error"
                    : "";
            String strStyleObject = strObject.equals( "" ) ? "error" : "";
            String strStyleMessage = strMessage.equals( "" ) ? "error" : "";
            String strStyleContact = strContact.equals( "0" ) ? "error" : "";
            String strAlert = "";

            if ( strSendMessage.equals( "done" ) )
            {
                UrlItem url = new UrlItem( strPortalUrl );
                url.addParameter( XPageAppService.PARAM_XPAGE_APP, ContactPlugin.PLUGIN_NAME );
                url.addParameter( PARAMETER_ID_CONTACT_LIST, strIdContactList );
                SiteMessageService.setMessage( request, PROPERTY_SENDING_OK, SiteMessage.TYPE_INFO, url.getUrl( ) );
            }

            else if ( strSendMessage.equals( "error_exception" ) )
            {
                strAlert = I18nService.getLocalizedString( PROPERTY_SENDING_NOK, request.getLocale( ) );
            }

            else if ( strSendMessage.equals( "error_captcha" ) )
            {
                strAlert = I18nService.getLocalizedString( PROPERTY_CAPTCHA_ERROR, request.getLocale( ) );
            }

            else if ( strSendMessage.equals( "error_field" ) )
            {
                strAlert = I18nService.getLocalizedString( PROPERTY_MANDATORY_FIELD_MISSING, request.getLocale( ) );
            }

            else if ( strSendMessage.equals( "error_recipient" ) )
            {
                strAlert = I18nService.getLocalizedString( PROPERTY_RECIPIENT_MISSING, request.getLocale( ) );
            }
            else if ( strSendMessage.equals( "error_email" ) )
            {
                strAlert = I18nService.getLocalizedString( PROPERTY_ERROR_EMAIL, request.getLocale( ) );
            }

            model.put( MARK_CONTACT_ALERT, strAlert );
            model.put( MARK_STYLE_LAST_NAME, strStyleLastName );
            model.put( MARK_STYLE_FIRST_NAME, strStyleFirstName );
            model.put( MARK_STYLE_OBJECT, strStyleObject );
            model.put( MARK_STYLE_EMAIL, strStyleEmail );
            model.put( MARK_STYLE_MESSAGE, strStyleMessage );
            model.put( MARK_STYLE_CONTACT, strStyleContact );
        }

        int nIdContactList = Integer.parseInt( strIdContactList );
        ContactList contactList = ContactListHome.findByPrimaryKey( nIdContactList, _plugin );

        if ( !ContactListHome.listExists( contactList.getId( ), _plugin ) )
        {
            SiteMessageService.setMessage( request, PROPERTY_LIST_NOT_EXISTS, SiteMessage.TYPE_ERROR );
        }

        if ( !isVisible( request, contactList.getRole( ) ) )
        {
            SiteMessageService.setMessage( request, PROPERTY_NOT_AUTHORIZED, SiteMessage.TYPE_STOP );
        }

        String strComboItem = I18nService.getLocalizedString( PROPERTY_COMBO_CHOOSE, request.getLocale( ) );

        // Contacts Combo
        ReferenceList listContact = ContactHome.getContactsByListWithString( contactList.getId( ), strComboItem,
                _plugin );

        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

            if ( user != null )
            {
                model.put( MARK_MYLUTECE_USER, user );
            }
        }

        model.put( MARK_CONTACTS_LIST, listContact );
        model.put( MARK_VISITOR_LASTNAME, strVisitorLastName );
        model.put( MARK_VISITOR_FIRSTNAME, strVisitorFirstName );
        model.put( MARK_VISITOR_EMAIL, strVisitorEmail );
        model.put( MARK_VISITOR_ADDRESS, strVisitorAddress );
        model.put( MARK_OBJECT, strObject );
        model.put( MARK_MESSAGE, strMessage );
        model.put( MARK_ID_CONTACT_LIST, nIdContactList );

        model.put( MARK_DEFAULT_CONTACT, ( ( strContact == null ) || ( strContact.equals( "" ) ) ) ? "0" : strContact );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_XPAGE_CONTACT, request.getLocale( ), model );

        return template.getHtml( );
    }

    /**
     * Get lists
     * @param request the page request
     * @return the lists
     * @throws SiteMessageException occurs during treatment
     */
    private String getLists( HttpServletRequest request ) throws SiteMessageException
    {
        String strTemplate = "";
        Map<String, Collection<ContactList>> model = new HashMap<String, Collection<ContactList>>( );
        Collection<ContactList> listOfLists = ContactListHome.findAll( _plugin );

        Collection<ContactList> visibleList = new ArrayList<ContactList>( ); // filter the list of lists by role

        for ( ContactList currentList : listOfLists )
        {
            if ( isVisible( request, currentList.getRole( ) ) )
            {
                visibleList.add( currentList );
            }
        }

        if ( visibleList.size( ) == 0 )
        {
            SiteMessageService.setMessage( request, PROPERTY_NO_LIST_VISIBLE, SiteMessage.TYPE_WARNING );
        }
        else if ( visibleList.size( ) == 1 )
        {
            String strContactListId = StringUtils.EMPTY;

            for ( ContactList onlyList : visibleList )
            {
                strContactListId = Integer.toString( onlyList.getId( ) );
            }

            return getForm( request, strContactListId, null );
        }
        else
        {
            model.put( MARK_LIST_OF_LISTS, visibleList );
            strTemplate = TEMPLATE_XPAGE_LISTS;
        }

        HtmlTemplate template = AppTemplateService.getTemplate( strTemplate, request.getLocale( ), model );
        model.put( MARK_LIST_OF_LISTS, visibleList );

        return template.getHtml( );
    }

    /**
     * This method tests the parameters stored in the request and send the
     * message if they are corrects. Otherwise, it
     * displays an error message.
     * @return The result of the process of the sending message.
     * @param request The http request
     * @throws fr.paris.lutece.portal.service.message.SiteMessageException
     *             Message displayed if an exception occures
     */
    public String doSendMessage( HttpServletRequest request ) throws SiteMessageException
    {
        String strPortalUrl = request.getParameter( PARAMETER_PORTAL_URL );
        String strIdContactList = request.getParameter( PARAMETER_ID_CONTACT_LIST );

        String strUrl = strPortalUrl + "?page=contact&id_contact_list=" + strIdContactList + "&send=done";
        String strVisitorLastName = ( request.getParameter( PARAMETER_VISITOR_LASTNAME ) == null ) ? "" : request
                .getParameter( PARAMETER_VISITOR_LASTNAME );
        String strVisitorFirstName = ( request.getParameter( PARAMETER_VISITOR_FIRSTNAME ) == null ) ? "" : request
                .getParameter( PARAMETER_VISITOR_FIRSTNAME );
        String strVisitorAddress = ( request.getParameter( PARAMETER_VISITOR_ADDRESS ) == null ) ? "" : request
                .getParameter( PARAMETER_VISITOR_ADDRESS );
        String strVisitorEmail = ( request.getParameter( PARAMETER_VISITOR_EMAIL ) == null ) ? "" : request
                .getParameter( PARAMETER_VISITOR_EMAIL );
        String strObject = ( request.getParameter( PARAMETER_MESSAGE_OBJECT ) == null ) ? "" : request
                .getParameter( PARAMETER_MESSAGE_OBJECT );
        String strMessage = ( request.getParameter( PARAMETER_MESSAGE ) == null ) ? "" : request
                .getParameter( PARAMETER_MESSAGE );
        String strDateOfDay = DateUtil.getCurrentDateString( request.getLocale( ) );
        String strContact = request.getParameter( PARAMETER_CONTACT );
        int nContact = ( strContact == null ) ? 0 : Integer.parseInt( strContact );
        int nIdContactList = Integer.parseInt( request.getParameter( PARAMETER_ID_CONTACT_LIST ) );

        //test the captcha
        if ( PluginService.isPluginEnable( JCAPTCHA_PLUGIN ) )
        {
            _captchaService = new CaptchaSecurityService( );

            if ( !_captchaService.validate( request ) )
            {
                return strPortalUrl + "?page=contact&id_contact_list=" + strIdContactList
                        + "&send=error_captcha&visitor_last_name=" + strVisitorLastName + "&visitor_first_name="
                        + strVisitorFirstName + "&visitor_email=" + strVisitorEmail + "&visitor_address="
                        + strVisitorAddress + "&contact=" + strContact + "&message_object=" + strObject + "&message="
                        + strMessage;
            }
        }

        //test the selection of the contact
        if ( nContact == 0 )
        {
            return strPortalUrl + "?page=contact&id_contact_list=" + strIdContactList
                    + "&send=error_recipient&visitor_last_name=" + strVisitorLastName + "&visitor_first_name="
                    + strVisitorFirstName + "&visitor_email=" + strVisitorEmail + "&visitor_address="
                    + strVisitorAddress + "&contact=" + strContact + "&message_object=" + strObject + "&message="
                    + strMessage;
        }

        String strPluginName = request.getParameter( PARAMETER_PAGE );
        _plugin = PluginService.getPlugin( strPluginName );

        Contact contact = ContactHome.findByPrimaryKey( nContact, _plugin );
        String strEmailContact = contact.getEmail( );
        String strContactName = contact.getName( );

        //tests the length of the message  ( 1000 characters maximums )
        if ( strMessage.length( ) > 1000 )
        {
            strMessage = strMessage.substring( 0, 1000 );
        }

        // Mandatory fields
        if ( strVisitorLastName.equals( "" ) || strVisitorFirstName.equals( "" ) || strVisitorEmail.equals( "" )
                || strContact.equals( "" ) || strObject.equals( "" ) || strMessage.equals( "" ) )
        {
            return strPortalUrl + "?page=contact&id_contact_list=" + strIdContactList
                    + "&send=error_field&visitor_last_name=" + strVisitorLastName + "&visitor_first_name="
                    + strVisitorFirstName + "&visitor_email=" + strVisitorEmail + "&visitor_address="
                    + strVisitorAddress + "&contact=" + strContact + "&message_object=" + strObject + "&message="
                    + strMessage;
        }

        //test the email of the visitor
        //Checking of the presence of the email address and of its format (@ caracter in the address).
        if ( StringUtil.checkEmail( strVisitorEmail ) != true )
        {
            return strPortalUrl + "?page=contact&id_contact_list=" + strIdContactList
                    + "&send=error_email&visitor_last_name=" + strVisitorLastName + "&visitor_first_name="
                    + strVisitorFirstName + "&visitor_email=" + strVisitorEmail + "&visitor_address="
                    + strVisitorAddress + "&contact=" + strContact + "&message_object=" + strObject + "&message="
                    + strMessage;
        }

        Map<String, String> model = new HashMap<String, String>( );
        model.put( MARK_VISITOR_LASTNAME, strVisitorLastName );
        model.put( MARK_VISITOR_FIRSTNAME, strVisitorFirstName );
        model.put( MARK_VISITOR_ADDRESS, strVisitorAddress );
        model.put( MARK_VISITOR_EMAIL, strVisitorEmail );
        model.put( MARK_CONTACT_NAME, strContactName );
        model.put( MARK_MESSAGE, strMessage );
        model.put( MARK_CURRENT_DATE, strDateOfDay );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MESSAGE_CONTACT, request.getLocale( ), model );

        String strMessageText = template.getHtml( );

        MailService.sendMailHtml( strEmailContact, strVisitorLastName, strVisitorEmail, strObject, strMessageText );
        ContactHome.updateHits( nContact, nIdContactList, _plugin );

        return strUrl;
    }
}
