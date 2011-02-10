<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="contactList" scope="session" class="fr.paris.lutece.plugins.contact.web.ContactListJspBean" />

<%
   contactList.init( request,contactList.RIGHT_MANAGE_CONTACT );
   response.sendRedirect( contactList.doModifyContactsOrder( request ) );
%>

