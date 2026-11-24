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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.paris.lutece.portal.service.security.SecurityTokenHandler;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.test.mocks.MockHttpServletRequest;
import jakarta.enterprise.inject.spi.CDI;

/**
 * The back office controllers of the plugin check the security token of their actions, and every action can get its token from a view of its
 * controller
 */
public class ContactSecurityTokenTest extends LuteceTestCase
{
    private static final List<Class<?>> CONTROLLERS = List.of( ContactJspBean.class, ContactListJspBean.class );

    @Test
    public void testControllersEnableTheToken( )
    {
        for ( Class<?> clazz : CONTROLLERS )
        {
            assertTrue( clazz.getAnnotation( Controller.class ).securityTokenEnabled( ), clazz.getSimpleName( ) );
        }
    }

    /**
     * The token checked by an action is stored under its securityTokenAction (or its name): a view of the controller must generate it under the
     * same key (its securityTokenAction, or its name), otherwise the action can never be done
     */
    @Test
    public void testEveryActionGetsItsTokenFromAView( )
    {
        for ( Class<?> clazz : CONTROLLERS )
        {
            Set<String> setViewKeys = new HashSet<>( );
            for ( Method method : clazz.getDeclaredMethods( ) )
            {
                View view = method.getAnnotation( View.class );
                if ( view != null )
                {
                    setViewKeys.add( view.securityTokenAction( ).isEmpty( ) ? view.value( ) : view.securityTokenAction( ) );
                }
            }
            for ( Method method : clazz.getDeclaredMethods( ) )
            {
                Action action = method.getAnnotation( Action.class );
                if ( action != null )
                {
                    String strKey = action.securityTokenAction( ).isEmpty( ) ? action.value( ) : action.securityTokenAction( );
                    assertTrue( setViewKeys.contains( strKey ), clazz.getSimpleName( ) + "." + action.value( ) + " expects a token stored under " + strKey
                            + ", no view generates it: " + setViewKeys );
                }
            }
        }
    }

    /**
     * An action posted to a back office controller of the plugin goes through the token check of the filter, and is refused without a token
     */
    @Test
    public void testActionsAreCheckedByTheFilter( )
    {
        SecurityTokenHandler handler = CDI.current( ).select( SecurityTokenHandler.class ).get( );
        handler.registerAdminControllers( null );

        checkRefusedWithoutToken( handler, "/jsp/admin/plugins/contact/ManageContacts.jsp", "actionCreateContact" );
        checkRefusedWithoutToken( handler, "/jsp/admin/plugins/contact/ManageContacts.jsp", "actionRemoveContact" );
        checkRefusedWithoutToken( handler, "/jsp/admin/plugins/contact/ManageContactLists.jsp", "actionRevokeContactsFromList" );
        checkRefusedWithoutToken( handler, "/jsp/admin/plugins/contact/ManageContactLists.jsp", "actionModifyContactListOrder" );
    }

    private static void checkRefusedWithoutToken( SecurityTokenHandler handler, String strServletPath, String strAction )
    {
        MockHttpServletRequest request = new MockHttpServletRequest( );
        request.setMethod( "POST" );
        request.setServletPath( strServletPath );
        request.addParameter( "action", strAction );

        assertFalse( handler.shouldNotFilter( request ), strAction + " is not checked by the filter" );
        assertFalse( handler.validate( request, handler.resolveTokenAction( request, strAction ) ), strAction + " accepted without token" );
    }
}
