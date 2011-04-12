<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
		
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="image">
		<h2>Images</h2>
		<xsl:apply-templates select="$current-topic/occurences/occurence[@behaviour='images' and @role='default']" mode="image" />
	</xsl:template>
	
	<xsl:template match="occurence[@behaviour='images' and @role='default']" mode="image">
		<div id="topic_images">
		  <xsl:apply-templates select="items/file" mode="image">
		    <xsl:with-param name="reference"><xsl:value-of select="@reference" /></xsl:with-param>
		  </xsl:apply-templates>
		</div>
	</xsl:template>
	
	<xsl:template match="file" mode="image">
	  <xsl:param name="reference" />
	  <img src="{$repository-img-url}{$reference}/{@name}{@extension}" alt="{@description}" title="{@description}" />
	</xsl:template>
	
</xsl:stylesheet>
