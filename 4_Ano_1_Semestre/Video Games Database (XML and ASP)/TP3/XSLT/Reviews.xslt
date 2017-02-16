<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
>
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="Reviews">
    <Reviews>
      <xsl:for-each select="User">
        <User>
          <xsl:attribute name="id">
            <xsl:value-of select="@id"/>
          </xsl:attribute>
          
          <xsl:attribute name="review">
            <xsl:value-of select="@review"/>
          </xsl:attribute>

          <xsl:attribute name="idJogo">
            <xsl:value-of select="@idJogo"/>
          </xsl:attribute>
        </User>
      </xsl:for-each>
    </Reviews>
  </xsl:template>
</xsl:stylesheet>
