<?xml version="1.0"  encoding="UTF-8"?>
<!--
    Extract IBAN test data
    @(#) $Id: extract_test.xsl 62 2008-12-03 19:25:15Z gfis $
    2014-01-23: Georg Fischer

    Activation: xsltproc rule_test.xsl iban_rules.xml | tee rule_test.raw
    Output: raw tab separated file with fields
            oacct oblz niban [nbic]
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
    <xsl:output method="text" /><!-- tab separated lines -->
    <xsl:strip-space elements="*" /><!-- remove whitespace in all nodes -->

    <xsl:template match="ci:IBAN-Rules/ci:rule">
        <xsl:for-each select="ci:test">
            <xsl:value-of select="concat(translate(@oacct,' ','')   , '&#9;')" />
            <xsl:value-of select="concat(translate(@oblz ,' ','')   , '&#9;')" />
            <xsl:value-of select="concat(translate(@niban,' ','')   , '&#9;')" />
            <xsl:choose>
                <xsl:when test="string-length(@nbic) != 0">
                    <xsl:value-of select="concat(translate(@nbic, ' ','')   , '&#9;')" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat('ubicDEnnxxx'              , '&#9;')" />
                </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="concat(@remark                    , '&#9;')" />
            <xsl:value-of select="'&#10;'" />
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
