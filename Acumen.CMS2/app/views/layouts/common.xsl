<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fn="http://www.w3.org/2005/xpath-functions"
>
	<!-- 
	XSL PARAMS
	These are set in xslt_template_handler#render	
	 -->
	<xsl:param name="root" />
	<xsl:param name="controller" />
	<xsl:param name="action" />

	<!--
	COMMON INFORMATION ITEMS
	The items are present in most views delivered by Acumen 
	 -->
	<!-- Entities -->
	<xsl:variable name="view-state" select="/view-state" />
	<xsl:variable name="current-topic" select="$view-state/item[@name='topic']/topic" />
	<xsl:variable name="current-assoc" select="$view-state/item[@name='association']/association" />
	<xsl:variable name="site-map" select="$view-state/item[@name='site-map']" />
	<xsl:variable name="properties" select="$view-state/item[@name='properties']/dictionary" />
	<!-- Values -->
	<xsl:variable name="topic-id" select="$current-topic/@id" />
	<xsl:variable name="assoc-id" select="$current-assoc/@id" />
	<xsl:variable name="current-language" select="$properties/item[@name='language']/@value" />
	<xsl:variable name="current-scope" select="$properties/item[@name='scope']/@value" />
	<xsl:variable name="current-map" select="$properties/item[@name='map']/@value" />
	<xsl:variable name="home-id" select="$properties/item[@name='home']/@value" />
	<!-- USER -->
	<xsl:variable name="user" select="$view-state/item[@name='user']/user-credentials" />
	<xsl:variable name="user-id" select="$user/@id" />
	<xsl:variable name="user-name" select="$user/@name" />
	<xsl:variable name="user-home" select="fn:replace($user-name, ' ', '')" />
	<!-- URLs -->
	<xsl:variable name="public-url"></xsl:variable>
	<xsl:variable name="repository-url"><xsl:value-of select="$public-url" />/repositories</xsl:variable>
	<xsl:variable name="repository-img-url"><xsl:value-of select="$repository-url" />/images/<xsl:value-of select="$current-map" />/<xsl:value-of select="$current-scope" />/</xsl:variable>
	<xsl:variable name="img-url"><xsl:value-of select="$public-url" />/images/<xsl:value-of select="$current-scope" /></xsl:variable>
	<xsl:variable name="site-img-url"><xsl:value-of select="$public-url" />/images/site</xsl:variable>
	<xsl:variable name="content-img-url"><xsl:value-of select="$img-url" />/<xsl:value-of select="$current-scope" /></xsl:variable>
	<xsl:variable name="javascript-url"><xsl:value-of select="$public-url" />/javascripts</xsl:variable>
	<xsl:variable name="stylesheets-url"><xsl:value-of select="$public-url" />/stylesheets</xsl:variable>
	<xsl:variable name="controller-url">/<xsl:value-of select="$controller" /></xsl:variable>
	<xsl:variable name="action-url"><xsl:value-of select="$controller-url" />/<xsl:value-of select="$action" /></xsl:variable>
	
	<xsl:template match="text()" mode="head"></xsl:template>

</xsl:stylesheet>
