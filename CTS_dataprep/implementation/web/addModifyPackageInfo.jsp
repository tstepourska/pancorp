<%@ taglib uri="/struts-tags" prefix="s" %>

<h1 id="wb-cont" property="name">
	<s:if test="%{id == null}">
		<s:text name="heading.createEmployee"/>
	</s:if>
	<s:else>
		<s:text name="heading.modifyEmployee"/>
	</s:else>
 </h1>

<s:if test="fieldErrors.size > 0" >
	<section class="alert alert-danger">    
        <h2><s:text name="errors_found"/></h2>
    </section>
</s:if> 

<s:form namespace="/prot" action="crtMdfySbmt" theme="simple">
<fieldset>  

    <legend>
        <s:text name="employee"/> 
        <s:property value="id" /> 
    </legend>
 	
 	
 	<div class="form-group<s:property value="%{fieldErrors.containsKey('name') ? ' has-error' : ''}"/>">
	    <label class="required" for="name">
	        <s:text name="name"/>
	        <s:text name="required"/>
	        <s:fielderror theme="custom">
	            <s:param>name</s:param>
	        </s:fielderror>
	    </label>
 
        <s:textfield cssClass="form-control" name="name" key="name" size="9" maxlength="9" id="name" />
    </div>
    
 
 	<div class="form-group<s:property value="%{fieldErrors.containsKey('dept') ? ' has-error' : ''}"/>">
	    <label class="required" for="dept">
	        <s:text name="dept"/>
	        <s:text name="required"/>
            <s:fielderror theme="custom">
                <s:param>dept</s:param>
            </s:fielderror>
	    </label>
     
        <s:select
         	cssClass="form-control"
            label="%{getText('dept')}"
            name="dept"
            id="dept"
            key="dept"
            list="validDepts"
            listKey="deptNumb"
            listValue="deptNumb"
            headerKey="-1"
            headerValue="%{getText('emptyOption')}"
            emptyOption="false" />
	</div>

	<div class="form-group<s:property value="%{fieldErrors.containsKey('job') ? ' has-error' : ''}"/>">
	    <label class="required" for="job">
	        <s:text name="job"/>
	        <s:text name="required"/>
            <s:fielderror theme="custom">
                <s:param>job</s:param>
            </s:fielderror>
	    </label>
	    
        <s:select
        	cssClass="form-control" 
            label="%{getText('job')}"
            name="job"
            id="job"
            key="job"
            list="%{validJobs}"
            listKey="key"
            listValue="%{getText(getKey())}"
            headerKey="-1"
            headerValue="%{getText('emptyOption')}"
            emptyOption="false" />
    </div>

	<div class="form-group<s:property value="%{fieldErrors.containsKey('years') ? ' has-error' : ''}"/>">
	    <label for="years">
	        <s:text name="years"/>
	        <s:fielderror theme="custom">
	            <s:param>years</s:param>
	        </s:fielderror>
	    </label>
	    
        <s:textfield cssClass="form-control text-right" name="years" key="years" size="2" maxlength="2" id="years" />
    </div>
        
     <div class="form-group<s:property value="%{fieldErrors.containsKey('salary') ? ' has-error' : ''}"/>">    
        <label for="salary">
            <s:text name="salary"/>
            <span class="wb-inv"><s:text name="currency_accessibility_label"/></span>
            <s:fielderror theme="custom">
                <s:param>salary</s:param>
            </s:fielderror>
        </label>
        <div class="input-group col-sm-3">
            <s:if test="%{!getText('currency_prepend').equals('')}">
                <span class="input-group-addon"><s:text name="currency_prepend"/></span>
            </s:if>
            <s:textfield name="salary" id="salary" cssClass="form-control text-right" value="%{getFormatted('format.currency_entry','salary')}" />
            <s:if test="%{!getText('currency_append').equals('')}">
                <span class="input-group-addon"><s:text name="currency_append"/></span>
            </s:if>
        </div>
    </div>  
 
    <div class="form-group<s:property value="%{fieldErrors.containsKey('comm') ? ' has-error' : ''}"/>">    
        <label for="comm">
            <s:text name="comm"/>
            <span class="wb-inv"><s:text name="currency_accessibility_label"/></span>
            <s:fielderror theme="custom">
                <s:param>comm</s:param>
            </s:fielderror>
        </label>
        <div class="input-group col-sm-3">
            <s:if test="%{!getText('currency_prepend').equals('')}">
                <span class="input-group-addon"><s:text name="currency_prepend"/></span>
            </s:if>
            <s:textfield name="comm" id="comm" cssClass="form-control text-right" value="%{getFormatted('format.currency_entry','comm')}" />
            <s:if test="%{!getText('currency_append').equals('')}">
                <span class="input-group-addon"><s:text name="currency_append"/></span>
            </s:if>
        </div>
    </div>  
 
 </fieldset>
           
<div> 
    <s:if test="%{id == null}">
        <s:submit action="crtSbmt" key="create_submit" cssClass="btn btn-primary"/>
    </s:if>
    <s:else>
        <s:submit action="mdfySbmt" key="modify_submit" cssClass="btn btn-primary"/>
    </s:else>
    <s:submit action="cncl" name="buttonName" value="%{getText('cancel')}" cssClass="btn btn-default"/>        
</div>

</s:form>