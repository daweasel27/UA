<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:msxsl="urn:schemas-microsoft-com:xslt" exclude-result-prefixes="msxsl"
                 xmlns:dc="http://purl.org/dc/elements/1.1/"
>
  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="Data">
    <Data>
      
      <xsl:for-each select="Game">
        <Game>

            <xsl:attribute name="id">
              <xsl:value-of select="id"/>
            </xsl:attribute>

            <xsl:attribute name="GameTitle">
              <xsl:value-of select="GameTitle"/>
            </xsl:attribute>          
    
            <xsl:attribute name="Platform">
              <xsl:value-of select="Platform"/>
            </xsl:attribute>
    
            <xsl:attribute name="ReleaseDate">
              <xsl:value-of select="ReleaseDate"/>
            </xsl:attribute>

          <xsl:attribute name="Overview">
            <xsl:value-of select="Overview"/>
          </xsl:attribute>

          <xsl:attribute name="ESRB">
            <xsl:value-of select="ESRB"/>
          </xsl:attribute>
          
          <xsl:attribute name="boxart">
            <xsl:value-of select="Images/boxart[@side='front']"/>
          </xsl:attribute>

          <xsl:attribute name="Overview">
            <xsl:value-of select="Overview"/>
          </xsl:attribute>
          
          <xsl:for-each select="Genres">
            <Genres>
              <xsl:attribute name="genre">
                <xsl:value-of select="genre"/>
              </xsl:attribute>
            </Genres>
          </xsl:for-each>
          
          <xsl:for-each select="Similar/Game">
            <Similar>
              <xsl:attribute name="idJogo">
                <xsl:value-of select="id"/>
              </xsl:attribute>
            </Similar>
          </xsl:for-each>
        </Game>
      </xsl:for-each>
    </Data>
  </xsl:template>
</xsl:stylesheet>
