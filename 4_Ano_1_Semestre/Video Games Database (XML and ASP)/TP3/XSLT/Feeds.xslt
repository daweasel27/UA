<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
>
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="feeds">
    <feeds>
      <xsl:for-each select="feed">
        <xsl:sort select="nome"/>
        <feed>
          <xsl:attribute name="nome">
            <xsl:value-of select="nome"/>
          </xsl:attribute>

          <xsl:attribute name="url">
            <xsl:value-of select="url"/>
          </xsl:attribute>

        </feed>
      </xsl:for-each>
    </feeds>
  </xsl:template>
</xsl:stylesheet>
