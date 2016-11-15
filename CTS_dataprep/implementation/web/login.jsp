<%@ taglib uri="/struts-tags" prefix="s"%>

<p>
<h2 class="modal-title">
	<s:text name="title.login" />
</h2>
</p>
<%
	String result = request.getParameter("result");
	if (result != null && result.equalsIgnoreCase("error")) {
%>
<!--  s:if test="result=='error'"-->
<section class="alert alert-danger">
	<h2>
		<s:text name="error.login" />
	</h2>
	<br>
</section>
<!--  /s:if-->
<%
	} 
%>
<form role="form" method="POST" namespace="/prot" 
	action="j_security_check">
	<fieldset>
		<legend>
			<!--  s:text name="instruction.login"/-->
		</legend>
		<div
			class="form-group <s:property value="%{fieldErrors.containsKey('j_username') ? ' has-error' : ''}"/>">
			<label for="j_username" class="required"> <s:text
					name="login.username" /> <a href="#userName-help-section"
				title="<s:text name="login_userName_help_heading"/>"
				aria-controls="centred-popup" class="wb-lbx" role="button"> <span
					class="glyphicon glyphicon-question-sign"><span
						class="wb-inv"><s:text name="login_userName_help_heading" /></span></span></a>
				<strong class="required"><s:text name="required" /></strong> <s:fielderror>
					<s:param>j_username</s:param>
				</s:fielderror>
			</label><br /> <input type="text" name="j_username" size="15" maxlength="15"
				class="form-control input-sm mrgn-bttm-md" id="userName"
				placeholder="User Name" />
		</div>
		<div
			class="form-group <s:property value="%{fieldErrors.containsKey('j_password') ? ' has-error' : ''}"/>">
			<label for="j_password" class="required"> <s:text
					name="login.password" /> <a href="#password-help-section"
				title="<s:text name="login_password_help_heading"/>"
				aria-controls="centred-popup" class="wb-lbx" role="button"> <span
					class="glyphicon glyphicon-question-sign"><span
						class="wb-inv"><s:text name="login_password_help_heading" /></span></span></a>
				<strong class="required"><s:text name="required" /></strong> <s:fielderror>
					<s:param>j_password</s:param>
				</s:fielderror>
			</label><br /> <input type="password" name="j_password" size="15"
				maxlength="15" id="password"
				class="form-control input-sm mrgn-bttm-md" placeholder="Password" />
		</div>
		<br /> <br />
		<div>
			<input type="submit" class="btn btn-primary"
				value="<s:text name="button.login"  />"> &nbsp;&nbsp;<input
				type="reset" class="btn btn-default"
				value="<s:text name="button.reset"/>" />
		</div>
	</fieldset>
</form>

<section id="userName-help-section"
	class="mfp-hide modal-dialog modal-content overlay-def">
	<header class="modal-header">
		<h2 class="modal-title">
			<s:text name="login_userName_help_heading" />
		</h2>
	</header>
	<div id="userName-help" class="modal-body">
		<s:text name="login_userName_help_text" />
	</div>
</section>

<section id="password-help-section"
	class="mfp-hide modal-dialog modal-content overlay-def">
	<header class="modal-header">
		<h2 class="modal-title">
			<s:text name="login_password_help_heading" />
		</h2>
	</header>
	<div id="password-help" class="modal-body">
		<s:text name="login_password_help_text" />
	</div>
</section>