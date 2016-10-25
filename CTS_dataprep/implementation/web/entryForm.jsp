<%@ taglib uri="/struts-tags" prefix="s" %>

<h1 id="wb-cont" property="name"><s:text name="heading.entryForm"/></h1>

<s:if test="fieldErrors.size > 0" >
    <section class="alert alert-danger">    
        <h2><s:text name="errors_found"/></h2>
    </section>
</s:if> 

<p>
    <s:text name="instruction.entryForm"/>  
</p>
 
<s:form namespace="/prot" action="ntrSbmt" theme="simple">
<fieldset>
    <legend><s:text name="retrieve_employee"/></legend>
  
	<div class="form-group<s:property value="%{fieldErrors.containsKey('id') ? ' has-error' : ''}"/>">
	    <label class="required" for="id">
	        <s:text name="id"/>
	        <a href="#id-help-section" title="<s:text name="id_help_heading"/>" aria-controls="centred-popup" class="wb-lbx" role="button">
	            <span class="glyphicon glyphicon-question-sign"><span class="wb-inv"><s:text name="id_help_heading"/></span></span></a>	        
	        <s:text name="required"/>
	        <s:fielderror theme="custom">
	            <s:param>id</s:param>
	        </s:fielderror>
	    </label>
            
        <s:textfield cssClass="form-control" name="id" key="id" size="4" maxlength="4" id="id" />
	</div>        
    <div>
        <s:submit key="get_submit" cssClass="btn btn-primary" />
    </div>
</fieldset>
</s:form>


<section id="id-help-section"
    class="mfp-hide modal-dialog modal-content overlay-def">
    <header class="modal-header">
        <h2 class="modal-title"><s:text name="id_help_heading"/></h2>
    </header>
    
    <div id="id-help" class="modal-body"><s:text name="id_help_text"/></div>
    
</section>