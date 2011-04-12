<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
>

	<xsl:include href="layout:layout.xsl" />
	<xsl:include href="layout:navigation.xsl" />
	<xsl:include href="layout:wiki-content.xsl" />
	
	<!-- RIGHT COLUMN -->
	<xsl:template match="view-state" mode="right-col">
		<xsl:call-template name="search-box-control" />
		<xsl:call-template name="topic-details-control" />
		<xsl:call-template name="admin-control" />
	</xsl:template>
	
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="view">
		<xsl:apply-templates select="item[@name='association']/association" mode="view" />
	</xsl:template>
	
	<xsl:template match="association" mode="view">
		<h2><xsl:value-of select="$current-assoc/@label" /><br />[<xsl:value-of select="$assoc-id" />]</h2>
		<xsl:apply-templates select="metadata" mode="view" />
		<h3>Other Metadata</h3>
		<xsl:apply-templates select="/view-state/item[@name='complete_topic']/topic" mode="view" />
			
	</xsl:template>
	
	<xsl:template match="metadata" mode="view">
		<h3>Current Metadata (<xsl:value-of select="$current-language" />)</h3>
		<table class="metadata">
			<xsl:apply-templates select="item" mode="view" />
		</table>
		<h3>Add Metadata</h3>
		<form method="post" action="/metadata/update">
		  <input type="hidden" name="type" value="assoc" />
			<input type="hidden" name="id" value="{$topic-id}" />
			<input type="hidden" name="assoc" value="{$assoc-id}" />
			<input type="hidden" name="lang" value="{$current-language}" />
			<input type="hidden" name="scope" value="{$current-scope}" />
			<table class="metadata">
				<tr>
					<th>
						name
					</th>
					<td>
						<input type="text" name="name" />
					</td>
				</tr>
				<tr>
					<th>
						value
					</th>
					<td>
						<input type="text" name="value" />
					</td>
				</tr>
				<th>&#32;</th>
				<td><button type="submit">Save</button></td>
			</table>
		</form>
	</xsl:template>
	
	<xsl:template match="metadata/item" mode="view">
		<tr>
			<th><xsl:value-of select="@name" /></th>			
			<td class="metadata-value-edit inplace" path="assoc::{$assoc-id}::{@name}::{@language}::{@scope}"><xsl:value-of select="@value" /></td>
			<td>
				<form method="post" action="/metadata/remove">
				  <input type="hidden" name="type" value="assoc" />
					<input type="hidden" name="id" value="{$topic-id}" />
					<input type="hidden" name="assoc" value="{$assoc-id}" />
					<input type="hidden" name="lang" value="{$current-language}" />
					<input type="hidden" name="scope" value="{$current-scope}" />
					<input type="hidden" name="name" value="{@name}" />
					<input type="image" src="/images/icons/silk/delete.png" title="Remove this metadata." />
				</form>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template match="/view-state/item[@name='complete_topic']/topic" mode="view">
		<table class="metadata">
			<xsl:for-each-group select="metadata/item[fn:lower-case(@language) != $current-language]" group-by="@language">
				<tr>
					<td colspan="3">
						<xsl:value-of select="current-grouping-key()" />
					</td>
				</tr>
				<xsl:for-each select="current-group()">
					<tr>
						<th><xsl:value-of select="@name" /></th>			
						<td class="metadata-value-edit inplace" path="{$topic-id}::{@name}::{@language}::{@scope}"><xsl:value-of select="@value" /></td>
						<td>
							<form method="post" action="/metadata/remove">
								<input type="hidden" name="id" value="{$topic-id}" />
								<input type="hidden" name="lang" value="{$current-language}" />
								<input type="hidden" name="scope" value="{$current-scope}" />
								<input type="hidden" name="name" value="{@name}" />
								<input type="image" src="/images/icons/silk/delete.png" title="Remove this metadata." />
							</form>
						</td>
					</tr>
				</xsl:for-each>
			</xsl:for-each-group>
		</table>
	</xsl:template>

</xsl:stylesheet>
