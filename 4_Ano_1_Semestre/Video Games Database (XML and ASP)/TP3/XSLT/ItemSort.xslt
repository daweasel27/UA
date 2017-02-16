<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
                 xmlns:dc="http://purl.org/dc/elements/1.1/"
>
  <xsl:output method="xml" indent="yes"/>

  <xsl:variable name="vrtfMonths">
    <m name="Jan" num="01"/>
    <m name="Feb" num="02"/>
    <m name="Mar" num="03"/>
    <m name="Apr" num="04"/>
    <m name="May" num="05"/>
    <m name="Jun" num="06"/>
    <m name="Jul" num="07"/>
    <m name="Aug" num="08"/>
    <m name="Sep" num="09"/>
    <m name="Oct" num="10"/>
    <m name="Nov" num="11"/>
    <m name="Dec" num="12"/>
  </xsl:variable>

  <xsl:variable name="vMonths" select=
   "document('')/*/xsl:variable
                   [@name='vrtfMonths']/*"
   />
  
  <xsl:template match="all">
    <all>

          <xsl:for-each select="item">
            <xsl:sort data-type="number" order="descending" select=
        "concat(substring(pubDate,13,4),
                $vMonths[@name 
                        = 
                         substring(current()/pubDate,9,3)]/@num,

                substring(pubDate,6,2),
                translate(substring(pubDate,18,8),
                          ':',
                          ''
                          )
                )
         "/>
            <item>

              <xsl:attribute name="author">
                <xsl:value-of select="author"/>
              </xsl:attribute>
              
              <xsl:attribute name="title">
                <xsl:value-of select="title"/>
              </xsl:attribute>

              <xsl:attribute name="description">
                <xsl:value-of select="description"/>
              </xsl:attribute>

              <xsl:attribute name="link">
                <xsl:value-of select="link"/>
              </xsl:attribute>

              <xsl:attribute name="category">
                <xsl:value-of select="category"/>
              </xsl:attribute>

              <xsl:attribute name="pubDate">
                <xsl:value-of select="pubDate"/>
              </xsl:attribute>

            </item>
          </xsl:for-each>
    </all>
  </xsl:template>
</xsl:stylesheet>
