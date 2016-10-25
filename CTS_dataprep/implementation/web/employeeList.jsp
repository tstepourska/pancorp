<%@ taglib uri="/struts-tags" prefix="s" %>

<h1 id="wb-cont" property="name"><s:text name="heading.employeeList"/></h1>

<s:subset source="employeeList" count="%{rowsPerPage}" start="%{offset}" >
    <s:iterator status="rowstatus">
    <div class="row">
    <div class="col-xs-1 text-right">
        <s:url var="getEmployee" namespace="/prot" action="ntrSbmt" >
            <s:param name="id" value="id"/>
        </s:url>
        <s:a href="%{getEmployee}"><s:property value="id" /></s:a>
    </div>
    <div class="col-xs-11">
        <s:property value="name" />
    </div>
    </div>
    </s:iterator>
</s:subset>

<br/>



<ul class="pagination pagination-sm">
        
    <s:if test="%{showPrevious()}">
        <li>
            <s:url var="prev" namespace="/prot" action="prv" />
            <s:a href="%{prev}" rel="prev"><span class="hidden-xs"><s:text name="prev" /></span><span class="wb-invisible"><s:text name="prev" /> <s:text name="page_of_employee_list" /></span></s:a>
        </li> 
    </s:if>
        
    <s:iterator value="pages" status="rowstatus">
        <s:if test="%{showLink(#rowstatus.index)}">
	        <s:url var="getPage" namespace="/prot" action="pg" >
	            <s:param name="requestedPage" value="#rowstatus.index"/>
	        </s:url>
	        <li class="<s:property value="%{currentPage == #rowstatus.index ? 'active' : ''}"/>">
	            <s:a href="%{getPage}"><s:property value="#rowstatus.count" /><span class="wb-invisible"><s:text name="page_n_of_employee_list"><s:param name="value" value="#rowstatus.count"/></s:text></span></s:a>
	        </li>
        </s:if>
        <s:elseif test="%{showEllipsis(#rowstatus.index)}">
            <li>
                <a href="#">...</a>
            </li>        
        </s:elseif>
    </s:iterator>
        
    <s:if test="%{showNext()}">
        <li>    
            <s:url var="next" namespace="/prot" action="nxt" /> 
            <s:a href="%{next}" rel="next"><span class="hidden-xs"><s:text name="next" /></span><span class="wb-invisible"><s:text name="next" /> <s:text name="page_of_employee_list" /></span></s:a>
        </li>
    </s:if>
    
</ul>

