<?xml version="1.0"  encoding="UTF-8"?>
<!--
    Checks whether nacct is at the end and nblz is at position 5 of niban
    @(#) $Id: extract_test.xsl 62 2008-12-03 19:25:15Z gfis $
    2014-01-20: Georg Fischer

    Activation: xsltproc check_niban.xsl iban_rules.xml | tee rule_insert.raw
-->
<!--
 * Copyright 2014 Dr. Georg Fischer <punctum at punctum dot kom>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
-->
<xsl:stylesheet version="1.0"
        xmlns:xsl ="http://www.w3.org/1999/XSL/Transform"
        xmlns:ci  ="http://www.teherba.org/2013/checkiban"
        >
    <xsl:output method="text" /><!-- a single SQL INSERT statement per line -->
    <xsl:strip-space elements="*" /><!-- remove whitespace in all nodes -->

    <xsl:template match="ci:IBAN-Rules/ci:rule">
        <xsl:for-each select="ci:map">
            <xsl:if test="string-length(@nacct) != 0 and string-length(@niban) != 0">
                <xsl:value-of select="concat(@niban, '&#9;', substring('          ', 1, 10 - string-length(@nacct)), @nacct, '&#9;')" />
                <xsl:choose>
                    <xsl:when test="substring(@niban, string-length(@niban) - string-length(@nacct) + 1) = @nacct">
                        <xsl:value-of select="'nacct ok&#10;'" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'nacct ERROR&#10;'" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
            <xsl:if test="string-length(@nblz) != 0 and string-length(@niban) != 0">
                <xsl:value-of select="concat(@niban, '&#9;', @nblz, '&#9;&#9;&#9;')" />
                <xsl:choose>
                    <xsl:when test="substring(@niban, 5, string-length(@nblz)) = @nblz">
                        <xsl:value-of select="'nblz ok&#10;'" />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="concat('nblz ERROR ', string-length(@nblz), '&#10;')" />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
