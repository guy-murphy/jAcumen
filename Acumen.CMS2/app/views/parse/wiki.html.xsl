<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet 
  version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

  <xsl:include href="layout:common.xsl" />
  <xsl:include href="layout:wiki-content.xsl" />
  
  <xsl:template match="/">
    <html>
      <head>
        <title>Acumen CMS 2.0 - Wiki Parse</title>
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
				<!-- provides bespoke CSS for this site...
						ALL SITE CSS MUST GO IN THIS FILE -->
				<link rel="stylesheet" type="text/css" media="screen" href="{$stylesheets-url}/site.css" />
				<!-- provides bespoke Javascript for this site...
						ALL SITE JAVASCRIPT MUST GO IN THIS FILE -->
				<script type="text/javascript" src="{$javascript-url}/site.js"></script>
      </head>
      <body>
        <xsl:apply-templates select="view-state" mode="view" />
      </body>
    </html>
  </xsl:template>
  
  <!-- CONTENT -->
	<xsl:template match="view-state" mode="view">
		<xsl:apply-templates select="item[@name='wiki']" mode="wiki" />
	</xsl:template>

</xsl:stylesheet>
