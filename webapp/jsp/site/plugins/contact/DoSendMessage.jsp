<%@ page errorPage="../../ErrorPagePortal.jsp" %>
<jsp:include page="../../PortalHeader.jsp" />


<jsp:useBean id="pagecontact" scope="request" class="fr.paris.lutece.plugins.contact.web.ContactApp" />

<%
	response.sendRedirect( pagecontact.doSendMessage( request ) );
%>