<jsp:useBean id="contact" scope="session" class="fr.paris.lutece.plugins.contact.web.ContactJspBean" />
<% String strContent = contact.processController ( request, response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>