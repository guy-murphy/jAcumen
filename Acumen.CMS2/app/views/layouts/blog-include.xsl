<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
		
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="blog">
		<h2>Blogs</h2>
		<xsl:apply-templates select="item[@name='blogs']/list/topic" mode="blog" />
	</xsl:template>
	
	<xsl:template match="topic" mode="blog">
		<hr />
		<div class="blog-entry">
			<h3><xsl:value-of select="@label" />&#32;-&#32;<span class="small">[<a href="/topic/view/{@id}">goto</a>]</span></h3>
			<h4>
			  on <span class="subdued"><xsl:value-of select="metadata/item[@name='created-on']/@value" /></span>
			  by <span class="subdued"><xsl:value-of select="metadata/item[@name='created-by']/@value" /></span>
			</h4>
			<xsl:apply-templates select="occurences/occurence[@behaviour='wiki' and @role='Page']/content" mode="wiki" />
		</div>
	</xsl:template>
	
</xsl:stylesheet>
