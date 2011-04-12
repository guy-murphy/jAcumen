<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout:layout.xsl" />
	<xsl:import href="layout:navigation.xsl" />
	<xsl:import href="layout:wiki-content.xsl" />
	<xsl:import href="layout:image-include.xsl" />
	<xsl:import href="layout:blog-include.xsl" />
	<xsl:import href="layout:comment-include.xsl" />
	
	<xsl:output 
	  indent="yes"
	/>
	
	<!-- HEAD -->
	<xsl:template match="view-state" mode="head">
		<!-- Syntax Highlighter - This can be removed for production Web sites, it's really only for the intranet. -->
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shCore.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushBash.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushCpp.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushCSharp.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushCss.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushDelphi.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushDiff.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushGroovy.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushJava.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushJScript.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushPhp.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushPlain.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushPython.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushRuby.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushScala.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushSql.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushVb.js"></script>
		<script type="text/javascript" src="{$javascript-url}/SyntaxHighlighter/scripts/shBrushXml.js"></script>
		<script type="text/javascript" src="{$javascript-url}/galleria/galleria.js"></script>		
		<link type="text/css" rel="stylesheet" href="{$javascript-url}/SyntaxHighlighter/styles/shCore.css"/>
		<link type="text/css" rel="stylesheet" href="{$javascript-url}/SyntaxHighlighter/styles/shThemeDefault.css"/>
	</xsl:template>
	
	<!-- RIGHT COLUMN -->
	<xsl:template match="view-state" mode="right-col">
		<xsl:call-template name="search-box-control" />
		<xsl:call-template name="metadata-box" />
		<xsl:call-template name="topic-details-control" />
		<xsl:call-template name="admin-control" />
	</xsl:template>
	
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="view">
		<xsl:apply-templates select="item[@name='topic']/topic" mode="view" />
		<xsl:apply-templates select="." mode="image" />
		<xsl:apply-templates select="." mode="comment" />
		<xsl:apply-templates select="." mode="blog" />
	</xsl:template>
	
	<xsl:template match="topic" mode="view">
    <h2 id="{@id}" class="metadata-value-edit inplace" path="{$topic-id}::label::{$current-language}::{$current-scope}"><xsl:value-of select="@label" /></h2>
    <!-- wiki -->
		<xsl:apply-templates select="occurences/occurence[@behaviour='wiki' and @role='Page']/content" mode="wiki" />
	</xsl:template>

</xsl:stylesheet>
