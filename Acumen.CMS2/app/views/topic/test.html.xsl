<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
    <xsl:template match="node()">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="/">
        <html>
            <head>
                <title>
                    Test for XSL Transformation
                </title>
            </head>
            <body>
                <h1>Test for XSL Transformation</h1>
                <xsl:apply-templates />
            </body>
        </html>
    </xsl:template>

    <xsl:template match="view-state">
        <h2>view-state template</h2>
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="view-state/item">
        <h3>item - <xsl:value-of select="@name" /></h3>
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="topic">
        <div>
            <h4>topic - @<xsl:value-of select="@id" />: <xsl:value-of select="@label" /></h4>
        </div>
    </xsl:template>

</xsl:stylesheet>