<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:include href="layout:common.xsl" />

	<xsl:template match="/">
		<html>
			<head>
				<title>
					Acumen CMS 2.0 - <xsl:value-of select="$controller" />/<xsl:value-of select="$action" /> -
					<xsl:value-of select="$current-topic/@label" />
				</title>
				<!-- pull in appropriate metadata from the topic into the html head -->
				<xsl:apply-templates select="$current-topic/metadata/item" mode="meta-tags" />
				<!-- resets element rendering and provides defaults -->
				<link rel="stylesheet" href="{$stylesheets-url}/blueprint/screen.css" type="text/css" media="screen, projection" />
				<link rel="stylesheet" href="{$stylesheets-url}/blueprint/print.css" type="text/css" media="print" />
				<xsl:text disable-output-escaping="yes">&lt;!--[if lt IE 7.]></xsl:text>
					<!-- provides CSS fixes for IE -->
					<link rel="stylesheet" href="{$stylesheets-url}/blueprint/ie.css" type="text/css" media="screen, projection" />
				<xsl:text disable-output-escaping="yes">&lt;![endif]--></xsl:text>
				<!-- provides basic typography -->
				<link rel="stylesheet" href="{$stylesheets-url}/blueprint/plugins/fancy-type/screen.css" type="text/css" media="screen, projection" />
				<!-- provides platform independent buttons -->
				<link rel="stylesheet" href="{$stylesheets-url}/blueprint/plugins/buttons/screen.css" type="text/css" media="screen, projection" />
				<!-- jQuery -->
				<script type="text/javascript" src="{$javascript-url}/jquery/jquery-1.4.2.js"></script>
				<script type="text/javascript" src="{$javascript-url}/jquery/hover-intent.js"></script>
				<script type="text/javascript" src="{$javascript-url}/jquery/editinplace/src/jquery.editinplace.js"></script>
				<script type="text/javascript" src="{$javascript-url}/jquery/jquery.autocomplete.js"></script>
				<link rel="stylesheet" type="text/css" href="{$stylesheets-url}/autocomplete.css" />
				<!-- provides bespoke CSS for this site...
						ALL SITE CSS MUST GO IN THIS FILE -->
				<link rel="stylesheet" type="text/css" media="screen" href="{$stylesheets-url}/site.css" />
				<!-- provides bespoke Javascript for this site...
						ALL SITE JAVASCRIPT MUST GO IN THIS FILE -->
				<script type="text/javascript" src="{$javascript-url}/site.js"></script>
				<xsl:apply-templates select="view-state" mode="head" />
			</head>
			<body>
				<div id="page" class="container">
					<div id="header" class="span-24 last">
						<h1>Acumen CMS 2.0</h1>
					</div>
					<div id="left-col" class=" container span-5 colborder">
						<xsl:apply-templates select="view-state" mode="left-col" />
					</div>
					<div id="content" class="span-12 colborder">
					  <xsl:apply-templates select="view-state/item[@name='error']/dictionary[item]" mode="message" />					
            <xsl:apply-templates select="view-state/item[@name='notice']/dictionary[item]" mode="message" />
            <xsl:apply-templates select="view-state/item[@name='success']/dictionary[item]" mode="message" />
						<xsl:apply-templates select="view-state" mode="view" />
					</div>
					<div id="right-col" class="span-5 last">
						<xsl:apply-templates select="view-state" mode="right-col" />
					</div>
					<div id="footer" class="prepend-top span-23 box last">
						Copyright notice, license information.
					</div>	
				</div>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="view-state/item[@name='notice']/dictionary" mode="message">
	  <ul class="notice">
	    <xsl:apply-templates select="item" mode="message" />
	  </ul>
	</xsl:template>
	
  <xsl:template match="view-state/item[@name='error']/dictionary" mode="message">
	  <ul class="error">
	    <xsl:apply-templates select="item" mode="message" />
	  </ul>
	</xsl:template>
	
	<xsl:template match="view-state/item[@name='success']/dictionary" mode="message">
	  <ul class="success">
	    <xsl:apply-templates select="item" mode="message" />
	  </ul>
	</xsl:template>
	
	<xsl:template match="item" mode="message">
	  <li>
	    <xsl:value-of select="@value" />
	  </li>
	</xsl:template>

	<xsl:template match="topic/metadata/item" mode="meta-tags">
		<xsl:variable name="name-substring" select="substring(@name, 6)" />
		<xsl:choose>
			<xsl:when test="substring(@name, 1,4) = 'meta'">
				<meta name="{$name-substring}" content="{@value}" />
			</xsl:when>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
