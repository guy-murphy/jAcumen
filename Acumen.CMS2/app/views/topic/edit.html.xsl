<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout:layout.xsl" />
	<xsl:import href="layout:navigation.xsl" />
	<xsl:import href="layout:wiki-content.xsl" />
	
	<xsl:template match="view-state" mode="head">
	  <script type="text/javascript" src="{$public-url}/markitup/jquery.markitup.js"></script>
	  <script type="text/javascript" src="{$public-url}/markitup/sets/default/set.js"></script>
	  <link rel="stylesheet" type="text/css" href="{$public-url}/markitup/skins/markitup/style.css" />
    <link rel="stylesheet" type="text/css" href="{$public-url}/markitup/sets/default/style.css" />    
	</xsl:template>
	
	<!-- RIGHT COLUMN -->
	<xsl:template match="view-state" mode="right-col">
		<xsl:call-template name="search-box-control" />
		<xsl:call-template name="topic-details-control" />
		<xsl:call-template name="admin-control" />
	</xsl:template>
	
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="view">
		<xsl:apply-templates select="item[@name='topic']/topic" mode="view" />
	</xsl:template>
	
	<xsl:template match="topic" mode="view">
		<h2 id="{@id}" class="topic-label-edit inplace"><xsl:value-of select="@label" /></h2>
		<form name="wiki" action="/topic/update" method="post">
			<input type="hidden" name="id" value="{$topic-id}" />
			<textarea id="editor" name="wikitext" class="editor">
				<xsl:value-of select="occurences/occurence[@behaviour='wiki' and @role='Page']" />
			</textarea>
			<div class="container">
				<div class="span-10"><button type="submit">save wiki</button></div>
				<div class="last">
					[<a href="/topic/view/{$topic-id}">return to topic</a>]
				</div>
			</div>
		</form>
	</xsl:template>


</xsl:stylesheet>
