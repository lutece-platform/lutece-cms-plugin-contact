<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:useBean id="contact" scope="session" class="fr.paris.lutece.plugins.contact.web.ContactJspBean" />

<%
    contact.init( request, contact.RIGHT_MANAGE_CONTACT );
    response.sendRedirect( contact.doModifyContact( request ) );
%>

