<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout:layout.xsl" />
	<xsl:import href="layout:navigation.xsl" />
	
	<xsl:output indent="yes" />
	
	<!-- RIGHT COLUMN -->
	<xsl:template match="view-state" mode="right-col">
		<xsl:call-template name="search-box-control" />
		<xsl:call-template name="topic-details-control" />
		<xsl:call-template name="admin-control" />
	</xsl:template>
	
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="view">
		<h2>Add New Topic</h2>
		<form name="wiki" action="/topic/create" method="post">
			<table class="add-topic">
			  <tr>
			    <th>topic id</th>
			    <td>
			      <input id="id" type="text" name="id" class="subdued" value="{/view-state/item[@name='uuid']/@value}" />
			    </td>
			  </tr>
			  <tr>
			    <th>topic label</th>
			    <td>
			      <input type="text" name="label" />
			    </td>
			  </tr>
			  <tr>
				  <th>&#32;</th>
				  <td><button type="submit">Create</button></td>
				</tr>
			</table>
		</form>
		
		<h3>Help &amp; Guidance</h3>
	
	  <p>
	    Each page must have an identity (often refered to as an ID), and this
	    ID <strong>must</strong> be unique to this inidividual page.
	  </p>
	  <p>
	    By default, when you start to create a new page and ID is provided
	    for you that is garenteed to be unique, but it's a pretty ugly
	    ID and certaily not 'human readable'. For this reason it's
	    suggested that you spend a couple of moments and pick a suitable ID
	    for this page.
	  </p>
		<p>
		  The ID that you pick for this page will form park of the URL
		  that points to this page. The exact URL will depend on what site
		  this page is being accessed from but it should look something like...
		  <code class="highlight">http://www.somesite.com/blah/<b>the-page-id</b></code>
      ...where <code class="highlight">the-page-id</code> is the ID that you
      specify for the page here.
    </p>
    <p>
      Please keep in mind that not only humans might be reading this page
      but other computer systems such as Google that will determine what
      you page is about by looking at the words that you use. Such computer
      systems (like Google) will pay particular attention to the URL of
      your page, and hence the ID that you choose for your page.
    </p>
	</xsl:template>
	
</xsl:stylesheet>
