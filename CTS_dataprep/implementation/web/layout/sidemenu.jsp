<%@ taglib uri="/struts-tags" prefix="s" %>

<nav role="navigation" id="wb-sec" typeof="SiteNavigationElement" class="col-md-3 col-md-pull-9 visible-md visible-lg">
<h2><s:text name="menu.secondary"/></h2>
<ul class="list-group menu list-unstyled">
    <li>
		<s:url var="ntr" namespace="/prot" action="ntr" />
        <h3><s:a href="%{ntr}"><s:text name="menu.heading"/></s:a></h3>
    	<ul class="list-group menu list-unstyled">
			<!-- <li><s:a cssClass="list-group-item" href="%{ntr}"><s:text name="retrieve_packageInfo" /></s:a></li>-->
			
			<s:url var="list" namespace="/prot" action="lst" />
			<li><s:a cssClass="list-group-item" href="%{list}"><s:text name="get_list" /></s:a></li>
			
			<s:url var="create" namespace="/prot" action="crt" />
			<li><s:a cssClass="list-group-item" href="%{create}"><s:text name="create" /></s:a></li>

			<s:url var="logout" namespace="/prot" action="lgo" />
			<li><a class="list-group-item" href="logout.action"><s:text name="menu.logout" /></a></li>
    	</ul>
    </li>
</ul>
</nav>
