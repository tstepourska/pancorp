<%@ taglib uri="/struts-tags" prefix="s" %>


<div class="row mrgn-bttom-sm">
	<div class="col-sm-3"><s:text name="id"/></div> 
	<div class="col-sm-9"><strong><s:property value="#session.employee.id" /></strong></div>
</div>

<div class="row mrgn-bttom-sm">
	<div class="col-sm-3"><s:text name="name"/></div>
	<div class="col-sm-9"><strong><s:property value="#session.employee.name" /></strong></div>
</div>

<div class="row mrgn-bttom-sm">
	<div class="col-sm-3"><s:text name="dept"/></div>	
	<div class="col-sm-9"><strong><s:property value="#session.employee.dept" /></strong></div>
</div>

<div class="row mrgn-bttom-sm">
	<div class="col-sm-3"><s:text name="job"/></div>
	<div class="col-sm-9"><strong><s:property value="#session.employee.job" /></strong></div>
</div>

<div class="row mrgn-bttom-sm">
	<div class="col-sm-3"><s:text name="years"/></div>
	<div class="col-sm-9"><strong><s:property value="#session.employee.years" /></strong></div>
</div>

<div class="row mrgn-bttom-sm">
	<div class="col-sm-3"><s:text name="salary"/></div>
	<div class="col-sm-9">	    
        <strong>
		<s:text name="format.currency_display">
			<s:param name="value" value="#session.employee.salary"/>
		</s:text>
        </strong>
	</div>
</div>

<div class="row mrgn-bttom-sm">
	<div class="col-sm-3"><s:text name="comm"/></div>
	<div class="col-sm-9">
        <strong>
		<s:if test="%{#session.employee.comm == null}">
			&nbsp;
		</s:if>
		<s:else>
			<s:text name="format.currency_display">
		        <s:param name="value" value="#session.employee.comm"/>
		    </s:text>
		</s:else>
        </strong>
	</div>
</div>