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

import fr.paris.lutece.api.user.User;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupResource;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.workgroup.AdminWorkgroupService;

/**
 * Workgroup checks of the contacts and contact lists reached by their id in the back office
 */
final class WorkgroupAccess
{
    private static final String MESSAGE_NOT_AUTHORIZED = "The resource belongs to a workgroup of which the user is not a member";

    /**
     * Private constructor
     */
    private WorkgroupAccess( )
    {
    }

    /**
     * Tells whether a user can manage a resource of a workgroup
     *
     * @param resource
     *            the contact or the contact list, may be null
     * @param user
     *            the admin user
     * @return true if the resource is null or authorized for the user
     */
    static boolean isAuthorized( AdminWorkgroupResource resource, AdminUser user )
    {
        return ( resource == null ) || AdminWorkgroupService.isAuthorized( resource, (User) user );
    }

    /**
     * Refuses the access to a resource of a workgroup of which the user is not a member
     *
     * @param resource
     *            the contact or the contact list, may be null (nothing to check)
     * @param user
     *            the admin user
     * @throws AccessDeniedException
     *             if the user is not a member of the workgroup of the resource
     */
    static void check( AdminWorkgroupResource resource, AdminUser user ) throws AccessDeniedException
    {
        if ( !isAuthorized( resource, user ) )
        {
            throw new AccessDeniedException( MESSAGE_NOT_AUTHORIZED );
        }
    }
}
