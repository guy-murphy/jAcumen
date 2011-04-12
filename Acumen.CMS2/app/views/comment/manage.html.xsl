<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
>

	<xsl:import href="layout:layout.xsl" />
	<xsl:import href="layout:navigation.xsl" />
	
	<!-- RIGHT COLUMN -->
	<xsl:template match="view-state" mode="right-col">
		<xsl:call-template name="search-box-control" />
		<xsl:call-template name="topic-details-control" />
		<xsl:call-template name="admin-control" />
	</xsl:template>
	
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="view">
	  <h2 id="{$current-topic/@id}" class="metadata-value-edit inplace" path="{$topic-id}::label::{$current-language}::{$current-scope}"><xsl:value-of select="$current-topic/@label" /></h2>
	  
    <h3>Manage Comments</h3>
    <xsl:apply-templates select="$view-state/item[@name='comments']/list" mode="comment" />
    
    <h3>Create New Comment</h3>
    
    <form method="post" action="/comment/create">
      <input type="hidden" name="id" value="{$topic-id}" />
      <table>
        <tr>
          <th><label for="other">topic-id</label></th>
          <td>
            <input type="text" name="from" value="{/view-state/item[@name='uuid']/@value}" />
          </td>
          <td class="help" rowspan="2">
            <p>
              If you are not sure what ID to give the comment then
              you can use the ID that has been provided as this is
              garenteed to be unique. If you plan on the ID being human
              readable then you should choose an ID for the blog entry
              to be created.
            </p>
          </td>
        </tr>
        <tr>
          <th><label for="label">label</label></th>
          <td>
            <input type="text" name="label" />
            <button type="submit">Save</button>
          </td>
        </tr>
      </table>
    </form>
    
    <h3>Link Topic As Comment</h3>
    <form method="post" action="/comment/link">
      <input type="hidden" name="id" value="{$topic-id}" />
      <table class="metadata">
				<tr>
					<th>
						<label for="from">topic-id</label>
					</th>
					<td>
						<input type="text" name="from" class="autocomplete" />
						<button type="submit">Save</button>
					</td>
					<td class="help">
					  <p>
              Type in the ID of the topic that you wish to attach to
              this one as a comment. The text input box will autocomplete
              what you enter to assist you in getting the correct ID.
            </p>
					</td>
				</tr>
			</table>
    </form>
    
    <h3>Help &amp; Guidance</h3>
    
    <p>
      A <em>comment</em> is simply one page attached to the other with a
      link where the link is of type comment. This 'other' page
      is simply displayed along with the main page as a comment.
    </p>
    
    <p>
      If you take a look the <a href="/association/view/{$topic-id}">links</a>
      for a topic with commentss attached to it, you will see those links 
      that are of type comment.
    </p>
      
	</xsl:template>
	
	<xsl:template match="item[@name='comments']/list" mode="comment">
	  <table>
	    <thead>
	      <th>comment</th><th>created on</th><th>created by</th><th>&#32;</th>
	    </thead>
	    <tbody>
  	    <xsl:apply-templates select="topic" mode="comment">
  	      <xsl:sort select="metadata/item[@name='timestamp']/@value" />
  	    </xsl:apply-templates>
  	  </tbody>
	  </table>
	</xsl:template>
	
	<xsl:template match="item[@name='comments']/list/topic" mode="comment">
	  <tr>
	    <td>
	      <a href="/topic/view?id={@id}" title="{@id}"><xsl:value-of select="@label" /></a>
	    </td>
	    <td>
	      <xsl:value-of select="metadata/item[@name='created-on']/@value" />
	    </td>
	    <td>
	      <xsl:value-of select="metadata/item[@name='created-by']/@value" />
	    </td>
	    <td>
	      <a href="/comment/delink?id={$topic-id}&amp;from={@id}">delink</a>
	    </td>
	  </tr>
	</xsl:template>
	
	
</xsl:stylesheet>
