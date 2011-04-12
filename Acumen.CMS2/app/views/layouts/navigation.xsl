<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fn="http://www.w3.org/2005/xpath-functions"
>

	<!-- LEFT COLUMN -->
	<xsl:template match="view-state" mode="left-col">
	  <!-- area -->
	  <div class="small-wrapper">
	    <div class="pop-out">
	      <div class="pop-out-head">
	        &#8661; area: <xsl:value-of	select="$current-map" />
	      </div>
	      <div class="pop-out-body">
	        <ul>
            <xsl:for-each select="/view-state/item[@name='maps']/dictionary/item">
              <li>
                <a href="?map={@name}"><xsl:value-of select="@value" /></a>
              </li>
            </xsl:for-each>
	        </ul>
	      </div>
	    </div>
	  </div>
	  <!-- language -->
	  <div class="small-wrapper">
	    <div class="pop-out">
	      <div class="pop-out-head">
	        &#8661; language: <xsl:value-of	select="$current-language" />
	      </div>
	      <div class="pop-out-body">
	        <ul>
            <xsl:for-each select="/view-state/item[@name='languages']/dictionary/item">
              <li>
                <a href="?lang={@name}"><xsl:value-of select="@value" /> (<xsl:value-of select="@name" />)</a>
              </li>
            </xsl:for-each>
	        </ul>
	      </div>
	    </div>
	  </div>
	  <!-- scope -->
	  <div class="small-wrapper">
	    <div class="pop-out">
	      <div class="pop-out-head">
	        &#8661; scope: <xsl:value-of	select="$current-scope" />
	      </div>
	      <div class="pop-out-body">
	        <ul>
            <xsl:for-each select="/view-state/item[@name='scopes']/dictionary/item">
              <li>
                <a href="?scope={@name}"><xsl:value-of select="@value" /> (<xsl:value-of select="@name" />)</a>
              </li>
            </xsl:for-each>
	        </ul>
	      </div>
	    </div>
	  </div>
	  <!-- main link navigation -->
		<ul class="topic-links" style="margin-top: 1em">
			<li class="topic-link"><a href="/topic/view/{$home-id}">Return Home</a></li>
		</ul>
		<xsl:apply-templates select="item[@name='related']/topic-map" mode="navigation" />
	</xsl:template>

	<xsl:template match="item" mode="sub-menu">
		<xsl:param name="param" />
		<div class="sub-menu">
			<ul>
				<xsl:apply-templates select="dictionary/item"
					mode="sub-menu">
					<xsl:with-param name="param" select="$param" />
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>

	<xsl:template match="item/dictionary/item" mode="sub-menu">
		<xsl:param name="param" />
		<li>
			<a href="?{$param}={@name}">
				<xsl:value-of select="@value" />
			</a>
		</li>
	</xsl:template>

	<xsl:template match="item[@name='related']/topic-map" mode="navigation">
		<div id="topic-nav">
			<xsl:apply-templates select="related/related-by"
				mode="navigation" />
		</div>
	</xsl:template>

	<xsl:template match="related/related-by" mode="navigation">
		<div class="type-relations">
			<div class="related-type">
				<xsl:value-of select="@type" />
			</div>
			<xsl:apply-templates select="related-by" mode="navigation" />
		</div>
	</xsl:template>

	<xsl:template match="related/related-by/related-by"
		mode="navigation">
		<div class="role-relations">
			<div class="related-role">
				<xsl:value-of select="@role" />
			</div>
			<ul class="topic-links">
				<xsl:apply-templates select="topic" mode="navigation" />
			</ul>
		</div>
	</xsl:template>

	<xsl:template match="related/related-by/related-by/topic"
		mode="navigation">
		<li class="topic-link">
			<a href="{$action-url}/{@id}" title="{metadata/item[@name='meta-description']/@value}">
				<xsl:value-of select="@label" />
			</a>
		</li>
	</xsl:template>

	<!-- RIGHT-HAND COLUMN -->
	<!--
		We use named templates here that are then called from the view to
		determing what is appropriate.
	-->
	
	<!-- metadata -->
	
	<xsl:template name="metadata-box">
	  <xsl:apply-templates select="$current-topic/metadata" mode="metadata" />
	</xsl:template>
	
	<xsl:template match="topic/metadata" mode="metadata">
    <div id="metadata" class="large-wrapper">
      <div id="topic-metadata" class="pop-out">
        <div class="pop-out-head" title="Click to view the metadata for this page.">
          &#8661; metadata
        </div>
        <table class="pop-out-body">
          <xsl:apply-templates select="item" mode="metadata" />
        </table>
      </div>
    </div>
  </xsl:template>
  
  <xsl:template match="metadata/item" mode="metadata">
    <tr>
      <td><xsl:value-of select="@name" /></td>
      <td><xsl:value-of select="@value" /></td>
    </tr>
  </xsl:template>

	<xsl:template name="search-box-control">
		<div class="append-bottom">
			<form id="search" method="post" action="/search/topic">
				<input id="search-box" name="goto" type="text" value=" Search for pages."
					class="subdued autocomplete" />
				<br />
			</form>
			<ul>
			  <li>
			    <a href="/search/advanced">Advanced Search</a>
			  </li>
			  <li>
			    <a href="/user/login">Login</a>
			  </li>
			</ul>
		</div>
	</xsl:template>
	
	<!-- topic details -->

	<xsl:template name="topic-details-control">
		<div id="topic-details" class="large-wrapper">
		  <div class="pop-out">
		    <div class="pop-out-head">
  			  &#8661; topic details
  			</div>
  			<div class="pop-out-body">
			    <div>
				    <div><a href="/topic/view/{$topic-id}">Return to / Refresh Topic</a></div>
				    <div><a href="/association/view/{$topic-id}">Links</a></div>
				    <div><a href="/occurence/view/{$topic-id}">Occurences</a></div>
				    <div><a href="/metadata/view/{$topic-id}">Metadata</a></div>
			    </div>
			    <hr />
			    <h4>actions</h4>
			    <div>
				    <div><a href="/topic/add">Add Topic</a></div>
				    <div><a href="/topic/edit/{$topic-id}">Edit Topic</a></div>
				    <div><a href="/gallery/manage/{$topic-id}">Manage Images</a></div>
				    <div><a href="/attachment/manage/{$topic-id}">Manage Attachments</a></div>
				    <div><a href="/blog/manage/{$topic-id}">Manage Blog</a></div>
				    <div><a href="/comment/manage/{$topic-id}">Manage Comments</a></div>
			    </div>
		    </div>
		  </div>
		</div>
	</xsl:template>
	
	<!-- admin -->
	
	<xsl:template name="admin-control">
		<div id="admin" class="large-wrapper">
		  <div class="pop-out">
		    <div class="pop-out-head">
  			  &#8661; administration
  			</div>
			  <div class="pop-out-body">
				  <div><a href="/admin/user">Manage Users</a></div>
				  <div><a href="/admin/security">Manage Security</a></div>
				  <div><a href="/cache/clear/{$topic-id}">Clear Cache For This</a></div>
				  <div><a href="/cache/clear">Clear Cache For All</a></div>
			  </div>
		  </div>
		</div>
	</xsl:template>

</xsl:stylesheet>
