<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="contact" scope="session" class="fr.paris.lutece.plugins.contact.web.ContactJspBean" />

<% contact.init( request, contact.RIGHT_MANAGE_CONTACT ); %>
<%= contact.getModifyContact ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>