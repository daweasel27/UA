<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
                 xmlns:dc="http://purl.org/dc/elements/1.1/"
>
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="Played">
    <Played>

      <xsl:for-each select="User">
        <User>
          
          <xsl:attribute name="idUser">
            <xsl:value-of select="@id"/>
          </xsl:attribute>

          <xsl:for-each select="Game">
            <Game>
            <xsl:attribute name="idJogo">
              <xsl:value-of select="@idJogo"/>
            </xsl:attribute>
            <xsl:attribute name="RatingValue">
              <xsl:value-of select="@score"/>
            </xsl:attribute>
            </Game>
          </xsl:for-each>
          </User>
      </xsl:for-each>
    </Played>
  </xsl:template>
</xsl:stylesheet>
