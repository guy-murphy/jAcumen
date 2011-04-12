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
		<xsl:apply-templates select="item[@name='topic']/topic" mode="view" />
	</xsl:template>
	
	<xsl:template match="topic" mode="view">
		<h2 id="{@id}" class="metadata-value-edit inplace" path="{$topic-id}::label::{$current-language}::{$current-scope}"><xsl:value-of select="@label" /></h2>
		<xsl:apply-templates select="occurences" mode="view" />
    <h3>Other Occurences</h3>
		<xsl:apply-templates select="/view-state/item[@name='complete_topic']/topic" mode="view" />
	</xsl:template>
	
		<xsl:template match="occurences" mode="view">
		<h3>Current Occurences (<xsl:value-of select="$current-language" />)</h3>
		<table class="occurence">
			<xsl:apply-templates select="occurence" mode="view">
	      <xsl:sort select="@behaviour" />
        <xsl:sort select="@role" />
			</xsl:apply-templates>
		</table>
		<h3>Add Occurence</h3>
		<form method="post" action="/occurence/update">
			<input type="hidden" name="id" value="{$topic-id}" />
			<input type="hidden" name="lang" value="{$current-language}" />
			<input type="hidden" name="scope" value="{$current-scope}" />
			<table class="metadata">
				<tr>
					<th>
						behaviour
					</th>
					<td>
						<input type="text" name="behaviour" />
					</td>
				</tr>
				<tr>
					<th>
						role
					</th>
					<td>
						<input type="text" name="role" />
					</td>
				</tr>
				<tr>
					<th>
						reference
					</th>
					<td>
						<input type="text" name="reference" />
					</td>
				</tr>
				<tr>
				  <th>&#32;</th>
				  <td><button type="submit">Save</button></td>
				</tr>
			</table>
		</form>
	</xsl:template>

  <xsl:template match="occurences/occurence" mode="view">
		<tr>
		  <th><xsl:value-of select="@behaviour" /></th>
			<th><xsl:value-of select="@role" /></th>			
			<td class="occurence-value-edit inplace" path="{$topic-id}::{@behaviour}::{@role}::{@language}::{@scope}"><xsl:value-of select="@reference" /></td>
			<td>
				<form method="post" action="/occurence/remove">
					<input type="hidden" name="id" value="{$topic-id}" />
					<input type="hidden" name="lang" value="{$current-language}" />
					<input type="hidden" name="scope" value="{$current-scope}" />
					<input type="hidden" name="behaviour" value="{@behaviour}" />
					<input type="hidden" name="role" value="{@role}" />
					<input type="image" src="/images/icons/silk/delete.png" title="Remove this occurence." />
				</form>
			</td>
		</tr>
	</xsl:template>
	
		<xsl:template match="/view-state/item[@name='complete_topic']/topic" mode="view">
		<table class="occurence">
			<xsl:for-each-group select="occurences/occurence[fn:lower-case(@language) != $current-language]" group-by="@language">
				<tr>
					<td colspan="3">
						<xsl:value-of select="current-grouping-key()" />
					</td>
				</tr>
				<xsl:for-each select="current-group()">
					<tr>
		        <th><xsl:value-of select="@behaviour" /></th>
			      <th><xsl:value-of select="@role" /></th>			
			      <td class="occurence-value-edit inplace" path="{$topic-id}::{@behaviour}::{@role}::{@language}::{@scope}"><xsl:value-of select="@reference" /></td>
			      <td>
				      <form method="post" action="/occurence/remove">
					      <input type="hidden" name="id" value="{$topic-id}" />
					      <input type="hidden" name="lang" value="{$current-language}" />
					      <input type="hidden" name="scope" value="{$current-scope}" />
					      <input type="hidden" name="behaviour" value="{@behaviour}" />
					      <input type="hidden" name="role" value="{@role}" />
					      <input type="image" src="/images/icons/silk/delete.png" title="Remove this occurence." />
				      </form>
			      </td>
		      </tr>
				</xsl:for-each>
			</xsl:for-each-group>
		</table>
	</xsl:template>

</xsl:stylesheet>
