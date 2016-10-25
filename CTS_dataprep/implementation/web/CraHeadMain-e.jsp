<%@ taglib uri="/struts-tags" prefix="s" %>
<!DOCTYPE html>
<!--[if lt IE 9]><html class="no-js lt-ie9" lang="en"><![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="en">
<!--<![endif]-->
<head>
<meta charset="utf-8"/>
<title><s:property value="%{getText(#request.titleKey)}" /></title>
<meta content="width=device-width, initial-scale=1" name="viewport"/>
<meta name="robots" content="noindex, nofollow, noarchive">
<meta name="format-detection" content="telephone=no">
<!--[if gte IE 9 | !IE ]><!-->
<link href="/ebci/wet/v4.0.21/theme-gcwu-fegc/assets/favicon.ico" rel="icon" type="image/x-icon">
<link rel="stylesheet" href="/ebci/wet/v4.0.21/theme-gcwu-fegc/css/theme.min.css">
<!--<![endif]-->
<!--[if lt IE 9]>
<link href="/ebci/wet/v4.0.21/theme-gcwu-fegc/assets/favicon.ico" rel="shortcut icon" />
<link rel="stylesheet" href="/ebci/wet/v4.0.21/theme-gcwu-fegc/css/ie8-theme.min.css" />
<script src="/ebci/wet/v4.0.21/wet-boew/js/jquery/1.11.1/jquery.min.js"></script>
<script src="/ebci/wet/v4.0.21/wet-boew/js/ie8-wet-boew.min.js"></script>
<![endif]-->
<noscript><link rel="stylesheet" href="/ebci/wet/v4.0.21/wet-boew/css/noscript.min.css" /></noscript>

<!-- CustomCSSStart -->
<link rel="stylesheet" href="/ebci/wet/v4.0.21/cra-arc/apps.css"/>
<link rel="stylesheet" href="<%=request.getContextPath()%>/pub/css/custom.css"/>
<!-- CustomCSSEnd -->

</head>
<body vocab="http://schema.org/" typeof="WebPage">
<ul id="wb-tphp">
<li class="wb-slc">
<a class="wb-sl" href="#wb-cont">Skip to main content</a>
</li>
<li class="wb-slc visible-md visible-lg">
<a class="wb-sl" href="#wb-info">Skip to site information</a>
</li>
<li class="wb-slc visible-md visible-lg">
<a class="wb-sl" href="#wb-sec">Skip to secondary menu</a>
</li>
</ul>
<header role="banner">
<div id="wb-bnr">
<div id="wb-bar">
<div class="container">
<div class="row">
<object id="gcwu-sig" type="image/svg+xml" tabindex="-1" role="img" data="/ebci/wet/v4.0.21/theme-gcwu-fegc/assets/sig-en.svg" aria-label="Government of Canada"></object>
<ul id="gc-bar" class="list-inline">
<li><a href="http://www.canada.ca/en/index.html" rel="external">Canada.ca</a></li>
<li><a href="http://www.canada.ca/en/services/index.html" rel="external">Services</a></li>
<li><a href="http://www.canada.ca/en/gov/dept/index.html" rel="external">Departments</a></li>
<li id="wb-lng"><h2>Language selection</h2>
<ul class="list-inline">
<li>
    <s:url id="languageUrl" namespace="/prot" action="lngg" ><s:param name="request_locale">fr_CA</s:param></s:url>
    <s:a href="%{languageUrl}" lang="fr">Fran�ais</s:a>
</li>
</ul>
</li>
</ul>
<section class="wb-mb-links col-xs-12 visible-sm visible-xs" id="wb-glb-mn">
<h2>Menu</h2>
<ul class="pnl-btn list-inline text-right">
<li><a href="#mb-pnl" title="Menu" aria-controls="mb-pnl" class="overlay-lnk btn btn-xs btn-default" role="button"><span class="glyphicon glyphicon-th-list"><span class="wb-inv">Menu</span></span></a></li>
</ul>
<div id="mb-pnl"></div>
</section>
</div>
</div>
</div>
<div class="container">
<div class="row">
<div id="wb-sttl" class="col-md-5">
<a href="http://www.cra-arc.gc.ca/menu-eng.html">
<span>Canada Revenue Agency</span>
</a>
</div>
<object id="wmms" type="image/svg+xml" tabindex="-1" role="img" data="/ebci/wet/v4.0.21/theme-gcwu-fegc/assets/wmms.svg" aria-label="Symbol of the Government of Canada"></object>
</div>
</div>
</div>
<nav role="navigation" id="wb-sm" data-trgt="mb-pnl" class="wb-menu visible-md visible-lg" typeof="SiteNavigationElement">
<div class="container nvbar">
<h2>Site menu</h2>
<div class="row">
<ul class="list-inline menu">
    <li><a class="item" href="http://www.cra-arc.gc.ca/ndvdls-fmls/menu-eng.html">Individuals and families</a></li>
    <li><a class="item" href="http://www.cra-arc.gc.ca/bsnsss/menu-eng.html">Businesses</a></li>
    <li><a class="item" href="http://www.cra-arc.gc.ca/chrts-gvng/menu-eng.html">Charities and giving</a></li>
    <li><a class="item" href="http://www.cra-arc.gc.ca/rprsnttvs/menu-eng.html">Representatives</a></li>
</ul>
</div>
</div>
</nav>
<%--
<nav role="navigation" id="wb-bc" property="breadcrumb">
<h2>You are here:</h2>
<div class="container">
<div class="row">
<ol class="breadcrumb">
	<li><a href="/menu-eng.html">Home</a></li>
	<li>Current page</li>
</ol>
</div>
</div>
</nav>
 --%>
</header>
<div class="container">
<div class="row">
<main role="main" property="mainContentOfPage" class="col-md-9 col-md-push-3">
