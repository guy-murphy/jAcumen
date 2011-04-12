<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet 
  version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
  <xsl:include href="layout:layout.xsl" />
	<xsl:include href="layout:navigation.xsl" />
	<xsl:include href="layout:wiki-content.xsl" />
	
	<!-- RIGHT COLUMN -->
	<xsl:template match="view-state" mode="right-col">
		<xsl:call-template name="search-box-control" />
		<xsl:call-template name="admin-control" />
	</xsl:template>
	
	<!-- CONTENT -->
	<xsl:template match="view-state" mode="view">
		<h2>User Login</h2>
		<form name="login" method="post" action="/user/login">
		  <table>
		    <tr>
		      <th>
		        user name
		      </th>
		      <td>
		        <input type="text" name="uname" value="{$user-name}" />
		      </td>
		    </tr>
		    <tr>
		      <th>
		        user password
		      </th>
		      <td>
		        <input type="password" name="upassword" />
		      </td>
		    </tr>
		    <tr>
				  <th>&#32;</th>
				  <td><button type="submit">Login</button></td>
				</tr>
		  </table>
		</form>
		
		<h3>Help &amp; Guidance</h3>
		<p>
		  Please use your credentials (user <em>name</em> and <em>password</em>)
		  to log into the CMS. If you do not have credentials, please ask a
		  System Administrator (<a href="mailto:gmurphy@acumen.es">Guy Murphy</a>)
		  to create an account for you so that you can use the CMS.
		</p>
		<p>
		  Once you have logged in, you may want to go to either
		  <a href="/topic/view/{$home-id}">the home page</a> or
		  go to or create your personal page 
		  (<a href="/topic/view/{$user-home}"><xsl:value-of select="$user-home" /></a>).
		</p>
	</xsl:template>
		
</xsl:stylesheet>
