<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="contactList" scope="session" class="fr.paris.lutece.plugins.contact.web.ContactListJspBean" />


<% contactList.init( request, contactList.RIGHT_MANAGE_CONTACT ); %>
<%= contactList.getManageListAssignations( request ) %>


<%@ include file="../../AdminFooter.jsp" %>