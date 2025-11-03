<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%@page import="fr.paris.lutece.plugins.contact.web.ContactJspBean"%>

${ contactJspBean.init( pageContext.request, ContactJspBean.RIGHT_MANAGE_CONTACT ) }
${ contactJspBean.getManageContactsHome( pageContext.request ) }

<%@ include file="../../AdminFooter.jsp" %>
