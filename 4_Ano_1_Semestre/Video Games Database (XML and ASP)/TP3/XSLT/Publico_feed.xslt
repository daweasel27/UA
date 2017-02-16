<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
>
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="rss">
    <rss>
      <xsl:for-each select="channel">
        <xsl:sort select="title"/>
        <channel>
          <xsl:attribute name="title">
            <xsl:value-of select="title"/>
          </xsl:attribute>

          <xsl:attribute name="link">
            <xsl:value-of select="link"/>
          </xsl:attribute>

          <xsl:attribute name="description">
            <xsl:value-of select="description"/>
          </xsl:attribute>
          
          <xsl:attribute name="copyright">
            <xsl:value-of select="copyright"/>
          </xsl:attribute> 
            
          <xsl:attribute name="language">
            <xsl:value-of select="language"/>
          </xsl:attribute>   
        
          <xsl:attribute name="lastBuildDate">
            <xsl:value-of select="lastBuildDate"/>
          </xsl:attribute>
            
          <xsl:attribute name="category">
            <xsl:value-of select="category"/>
          </xsl:attribute>

          <xsl:attribute name="image">
            <xsl:value-of select="image/url"/>
          </xsl:attribute>

          <listaDeItems>
            <xsl:for-each select="item">
              <xsl:sort select="guid"/>
              <item>

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

                <xsl:attribute name="guid">
                  <xsl:value-of select="guid"/>
                </xsl:attribute>

                <xsl:attribute name="dc:creator">
                  <xsl:value-of select="dc:creator"/>
                </xsl:attribute>

              </item>
            </xsl:for-each>
          </listaDeItems>
          
        </channel>
      </xsl:for-each>
    </rss>
  </xsl:template>
</xsl:stylesheet>
