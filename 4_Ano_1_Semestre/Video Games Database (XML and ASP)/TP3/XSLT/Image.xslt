<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="rss">
      <xsl:for-each select="channel">
        <image>
          <xsl:attribute name="name">
            <xsl:value-of select="image/url"/>
          </xsl:attribute>
        </image>
      </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>