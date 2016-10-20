<jsp:useBean id="contactList" scope="session" class="fr.paris.lutece.plugins.contact.web.ContactListJspBean" />
<% String strContent = contactList.processController ( request, response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>