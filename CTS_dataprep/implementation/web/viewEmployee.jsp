<%@ taglib uri="/struts-tags" prefix="s" %>

<h1 id="wb-cont" property="name"><s:text name="heading.employeeDetails"/></h1>

<s:include value="employeeDetails.jsp"/>

<br/>

<div>         
    <s:url var="modify" namespace="/prot" action="mdfy" />
    <s:a href="%{modify}" cssClass="btn btn-default"><s:text name="modify" /></s:a>
    <s:url var="delete" namespace="/prot" action="dlt" />
    <s:a href="%{delete}" cssClass="btn btn-default"><s:text name="delete" /></s:a>
</div>

