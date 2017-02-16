<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
>
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="root">
    <root>
      <xsl:for-each select="Game">
        <Game>
          <xsl:attribute name="idJogo">
            <xsl:value-of select="@idJogo"/>
          </xsl:attribute>

        </Game>
      </xsl:for-each>
    </root>
  </xsl:template>
</xsl:stylesheet>
