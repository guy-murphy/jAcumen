<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
		
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="comment">
		<h2>Comments</h2>
		<xsl:apply-templates select="item[@name='comments']/list/topic" mode="comment" />
	</xsl:template>
	
	<xsl:template match="topic" mode="comment">
		<hr />
		<div class="comment-entry">
			<h3><xsl:value-of select="@label" />&#32;-&#32;<span class="small">[<a href="/topic/view/{@id}">goto</a>]</span></h3>
			<xsl:apply-templates select="occurences/occurence[@behaviour='wiki' and @role='Page']/content" mode="wiki" />
		</div>
	</xsl:template>
	
</xsl:stylesheet>
