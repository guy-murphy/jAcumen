package test.acumen.web;

import org.junit.*;

import acumen.util.PropertySet;
import acumen.web.view.XslTransformer;
import acumen.web.view.XslTransformerException;

/**
 * User: gmurphy
 * Date: 21-Oct-2009
 * Time: 16:23:41
 */
public class XslTransformerTest {

    @Test
    public void transformText () throws XslTransformerException {
        StringBuffer buf = new StringBuffer();
        buf.append("<test>");
        buf.append("	<item>one</item>");
        buf.append("	<item>two</item>");
        buf.append("</test>");
        String xml = buf.toString();

        buf = new StringBuffer();
        buf.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        buf.append("<xsl:stylesheet version=\"2.0\"");
        buf.append("    xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"");
        buf.append(">");
        buf.append("<xsl:import href=\"layout:common.xsl\" />");
        buf.append("	<xsl:template match=\"node()\">");
        buf.append("		<xsl:apply-templates />");
        buf.append("	</xsl:template>");
        buf.append("	");
        buf.append("	<xsl:template match=\"/\">");
        buf.append("		<html>");
        buf.append("			<head>");
        buf.append("				<title>");
        buf.append("					Test for XSL Transformation");
        buf.append("				</title>");
        buf.append("			</head>");
        buf.append("			<body>");
        buf.append("				<xsl:value-of select=\"$root\" /><xsl:apply-templates />");
        buf.append("			</body>");
        buf.append("		</html>");
        buf.append("	</xsl:template>");
        buf.append("					");
        buf.append("	<xsl:template match=\"test\">");
        buf.append("		<h1>XSL Test</h1>");
        buf.append("		<xsl:apply-templates />");
        buf.append("	</xsl:template>");
        buf.append("	");
        buf.append("	<xsl:template match=\"item\">");
        buf.append("		<h2>Item - <xsl:value-of select=\".\" /></h2>");
        buf.append("	</xsl:template>");
        buf.append("</xsl:stylesheet>");
        String xsl = buf.toString();

        XslTransformer transformer = new XslTransformer("/home/guy/workspace/Acumen.CMS/");
        transformer.turnOffCaching();
        PropertySet properties = new PropertySet();
        properties.put("root", "/home/guy/workspace/Acumen.CMS/");
        properties.put("controller", "topic");
        properties.put("action", "view");
        System.out.println(transformer.transformText("xml1", xml, "xsl1", xsl, properties));
        System.out.println(transformer.transformText("xml1", xml, "xsl1", xsl, properties));
    }
}
