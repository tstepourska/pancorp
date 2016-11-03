<%@ page session="false" 
    contentType="text/html;charset=utf-8"
    pageEncoding="iso-8859-1" %>
<%
if( request.isRequestedSessionIdValid() ) {
	request.getSession().invalidate();
}
%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<!--[if lt IE 9]><html class="no-js lt-ie9" lang="en"><![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en">
<!--<![endif]-->
<head>
<meta charset="utf-8"/>
<title>Session expired / Session a expiré</title>
<meta content="width=device-width, initial-scale=1" name="viewport"/>
<meta name="robots" content="noindex, nofollow, noarchive">
<!--[if gte IE 9 | !IE ]><!-->
<link href="/ebci/wet/v4.0.21/theme-gcwu-fegc/assets/favicon.ico" rel="icon" type="image/x-icon">
<link rel="stylesheet" href="/ebci/wet/v4.0.21/theme-gcwu-fegc/css/theme-sp-pe.min.css"/>
<!--<![endif]-->
<!--[if lt IE 9]>
<link href="/ebci/wet/v4.0.21/theme-gcwu-fegc/assets/favicon.ico" rel="shortcut icon" />
<link rel="stylesheet" href="/ebci/wet/v4.0.21/theme-gcwu-fegc/css/ie8-theme.min.css" />
<script src="/ebci/wet/v4.0.21/wet-boew/js/jquery/1.11.1/jquery.min.js"></script>
<script src="/ebci/wet/v4.0.21/wet-boew/js/ie8-wet-boew.min.js"></script>
<![endif]-->
<noscript><link rel="stylesheet" href="/ebci/wet/v4.0.21/wet-boew/css/noscript.min.css" /></noscript>
</head>
<body vocab="http://schema.org/" typeof="WebPage">
<header role="banner">
<div id="wb-bnr" class="container">
<object id="gcwu-sig" type="image/svg+xml" tabindex="-1" role="img" data="/ebci/wet/v4.0.21/theme-gcwu-fegc/assets/sig-alt-en.svg" aria-label="Government of Canada"></object>
</div>
</header>
<main role="main" property="mainContentOfPage" class="container">
<div class="col-md-12">
    <h1>Session expired / <span lang="fr">Session a expiré</span></h1>
	<section class="col-md-6">
    <h2 class="h3">Session expired</h2>
    <p>Your session has timed out due to inactivity.</p>
    <p>Select the link below to start a new session.</p>
	<ul>
	<li>
		<s:url var="entryPoint" namespace="/pub" action="ntr"><s:param name="request_locale">en_CA</s:param></s:url>
		<s:a href="%{entryPoint}">Return to application</s:a>.
	</li>
	</ul>
	</section>
	<section class="col-md-6" lang="fr">
    <h2 class="h3">Session a expiré</h2>
    <p>Votre session a pris fin pour cause d'inactivité.</p>
    <p>Sélectionnez le lien ci-dessous pour commencer une nouvelle session.</p>
	<ul>
	<li>
		<s:url var="entryPoint" namespace="/pub" action="ntr"><s:param name="request_locale">fr_CA</s:param></s:url>
	    <s:a href="%{entryPoint}">Retourner à l'application</s:a>.
	</li>
	</ul>
	</section>
</div>
</main>
<footer role="contentinfo" class="container">
<object id="wmms" type="image/svg+xml" tabindex="-1" role="img" data="/ebci/wet/v4.0.21/theme-gcwu-fegc/assets/wmms-alt.svg" aria-label="Symbol of the Government of Canada"></object>
</footer>
<!--[if gte IE 9 | !IE ]><!-->
<script src="/ebci/wet/v4.0.21/wet-boew/js/jquery/2.1.4/jquery.min.js"></script>
<script src="/ebci/wet/v4.0.21/wet-boew/js/wet-boew.min.js"></script>
<!--<![endif]-->
<!--[if lt IE 9]>
<script src="/ebci/wet/v4.0.21/wet-boew/js/ie8-wet-boew2.min.js"></script>
<![endif]-->
<script src="/ebci/wet/v4.0.21/theme-gcwu-fegc/js/theme.min.js"></script>
</body>
</html>
