<%@ taglib uri="/struts-tags" prefix="s" %>

<h1 id="wb-cont" property="name"><s:text name="heading.deleteEmployee"/></h1>

<p>
<s:text name="instruction.deleteForm"/>
</p>

<s:include value="employeeDetails.jsp"/>

<br/>

<s:form action="dltSbmt" theme="simple">
	<div> 
        <s:submit action="cncl" name="buttonName" value="%{getText('cancel')}" cssClass="btn btn-primary" />        
        <s:submit action="dltSbmt" name="buttonName" value="%{getText('delete')}" cssClass="btn btn-default" />    
	</div>
</s:form>



