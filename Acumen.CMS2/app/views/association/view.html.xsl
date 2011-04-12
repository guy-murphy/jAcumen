<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
>

	<xsl:import href="layout:layout.xsl" />
	<xsl:import href="layout:navigation.xsl" />
	<xsl:import href="layout:wiki-content.xsl" />
	
	<!-- RIGHT COLUMN -->
	<xsl:template match="view-state" mode="right-col">
		<xsl:call-template name="search-box-control" />
		<xsl:call-template name="topic-details-control" />
		<xsl:call-template name="admin-control" />
	</xsl:template>
	
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="view">
    <xsl:apply-templates select="item[@name='topic']/topic" mode="view" />
  
    <h3>Add Link</h3>
		<form method="post" action="/association/add">
			<input type="hidden" name="id" value="{$topic-id}" />
			<input type="hidden" name="lang" value="{$current-language}" />
			<input type="hidden" name="scope" value="{$current-scope}" />
			<table class="associations">
				<tr>
					<th>
						reference
					</th>
					<td>
						<input type="text" name="reference" class="autocomplete" />
					</td>
				</tr>
				<tr>
					<th>
						label
					</th>
					<td>
						<input type="text" name="label" />
					</td>
				</tr>
				<tr>
					<th>
						type
					</th>
					<td>
						<input type="text" name="type" value="categorisation" />
					</td>
				</tr>
				<tr>
					<th>
						role
					</th>
					<td>
						<input type="text" name="role" value="member" />
					</td>
				</tr>
				<th>&#32;</th>
				<td><button type="submit">Save</button></td>
			</table>
		</form>
	</xsl:template>
	
	<xsl:template match="topic" mode="view">
 	  <h2 id="{@id}" class="metadata-value-edit inplace" path="{$topic-id}::label::{$current-language}::{$current-scope}"><xsl:value-of select="@label" /></h2>
 	  
 	  <div class="topic-nav">
   	  <table class="link-editor" border="0" cellpadding="0" cellspacing="0">
        <xsl:for-each-group select="associations/item/association" group-by="@type">
          <tr>
            <td colspan="3" class="link-related-type">
              <xsl:value-of select="current-grouping-key()" />
            </td>
          </tr>
          <xsl:for-each-group select="current-group()" group-by="@role">
            <tr>
              <td colspan="3" class="link-related-role">
                <xsl:value-of select="current-grouping-key()" />
              </td>
            </tr>
            <xsl:for-each select="current-group()">
              <tr>
                <td class="link-spacer">&#32;</td>
                <td class="link-reference association-value-edit inplace" path="{@id}::label::{@label}">
                  <xsl:value-of select="@reference" />
                </td>
                <td class="link-role association-value-edit inplace" path="{@id}::reference::{@reference}">
                  <xsl:value-of select="if (@label = @id) then ('- no label -') else (@label)" />
                </td>
                <td>
                  <a href="/metadata/assoc/{$topic-id}?assoc={@id}">meta</a>
                </td>
                <td class="link-remove">
                  <form method="post" action="/association/remove">
					          <input type="hidden" name="id" value="{$topic-id}" />
					          <input type="hidden" name="lang" value="{$current-language}" />
					          <input type="hidden" name="scope" value="{$current-scope}" />
					          <input type="hidden" name="assoc" value="{@id}" />
					          <input type="image" src="/images/icons/silk/delete.png" title="Remove this Link." />
				          </form>
                </td>
              </tr>
   	        </xsl:for-each>
          </xsl:for-each-group>
        </xsl:for-each-group>
   	  </table>
   	</div>
	</xsl:template>	

</xsl:stylesheet>
