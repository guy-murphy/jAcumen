<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
	
	<xsl:template match="text()" mode="wiki">
		<xsl:value-of select="." />
	</xsl:template>

	<xsl:template match="content" mode="wiki">
		<div class="wiki-content">
			<xsl:apply-templates mode="wiki" />
		</div>
	</xsl:template>
	
	<!--formatting -->
	
	<xsl:template match="strong" mode="wiki">
		<strong><xsl:apply-templates mode="wiki" /></strong>
	</xsl:template>
	
	<xsl:template match="emph" mode="wiki">
		<em><xsl:apply-templates mode="wiki" /></em>
	</xsl:template>
	
	<xsl:template match="code" mode="wiki">
		<code class="highlight"><xsl:apply-templates mode="wiki" /></code>
	</xsl:template>
	
	<!--  headings -->
	
	<xsl:template match="h1" mode="wiki">
		<h2><xsl:apply-templates mode="wiki" /></h2>
	</xsl:template>
	
	<xsl:template match="h2" mode="wiki">
		<h3><xsl:apply-templates mode="wiki" /></h3>
	</xsl:template>
	
	<xsl:template match="h3" mode="wiki">
		<h4><xsl:apply-templates mode="wiki" /></h4>
	</xsl:template>
	
	<xsl:template match="h4" mode="wiki">
		<h5><xsl:apply-templates mode="wiki" /></h5>
	</xsl:template>
	
	<!-- blocks -->
	
	<xsl:template match="block" mode="wiki">
		<p class="block">
			<xsl:apply-templates mode="wiki" />
		</p>
	</xsl:template>
	
	<xsl:template match="block/line" mode="wiki">
		<div class="line">
			<xsl:apply-templates mode="wiki" />
		</div>
	</xsl:template>
	
	<xsl:template match="empty-line" mode="wiki">
		<div class="empty-line">&#32;</div>
	</xsl:template>
	
	<xsl:template match="code-block" mode="wiki">
		<pre class="brush: {title}; wrap-lines: false">
			<xsl:apply-templates xml:space="preserve" mode="wiki" />
		</pre>
	</xsl:template>
	
	<xsl:template match="code-block/title" mode="wiki"><!-- nada --></xsl:template>
	
	<!-- lists -->
	
	<xsl:template match="list" mode="wiki">
		<ul class="topic-links">
			<xsl:apply-templates select="item" mode="wiki" />
		</ul>
	</xsl:template>
	
	<xsl:template match="list/item" mode="wiki">
		<li><xsl:apply-templates mode="wiki" /></li>
	</xsl:template>
	
	<!-- links -->
	
	<xsl:template match="topic-link[@label]" mode="wiki">
		<a href="{.}"><xsl:value-of select="@label" /></a>
	</xsl:template>
	
	<xsl:template match="topic-link" mode="wiki">
		<a href="{.}"><xsl:value-of select="." /></a>
	</xsl:template>

	<xsl:template match="image-link" mode="wiki">
		<img src="{$img-url}/{.}" alt="{@label}" title="{@label}" align="{@param}" />
	</xsl:template>

	<xsl:template match="url-link" mode="wiki">
		<a href="{.}" title="{if (@label) then @label else .}">
			<xsl:attribute name="target"><xsl:value-of select="if (@param) then @param else '_blank'" /></xsl:attribute>
			<xsl:value-of select="if (@label) then @label else ." />
		</a>
	</xsl:template>

</xsl:stylesheet>