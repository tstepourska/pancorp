<%@ page 
    contentType="text/html;charset=utf-8"
    pageEncoding="ISO-8859-1"
%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%--
    clf tiles layout
    Basic layout with header, left menu, body and footer.
    @param title page title string
    @param breadcrumb string
    @param header Header tile (jsp url or definition name)
    @param menu Menu (jsp url or definition name)
    @param body Body (jsp url or definition name)
    @param footer Footer (jsp url or definition name)
--%>    
<%--    
    Make tiles attributes accessible to child tiles
    via the request context.  This allows us to
    use the regular struts i18n mechanism to cash out
    english/french versions of these strings, rather than
    hard-coding language-specific text in tiles-defs.xml.
--%>
<tiles:useAttribute name="titleKey" scope="request" />
<tiles:useAttribute name="breadcrumbKey" scope="request"/>
<%--
    Store the current page definition in the session
    so that language-switch requests can re-render the
    same page in the new locale 
--%>
<tiles:useAttribute name="crrntPg" scope="session"/>

<tiles:insertAttribute name="header" />
<tiles:insertAttribute name="body" />
<tiles:insertAttribute name="sessionTimeout"/>
<tiles:insertAttribute name="date" />
<tiles:insertAttribute name="menu" />
<tiles:insertAttribute name="footer" /> 