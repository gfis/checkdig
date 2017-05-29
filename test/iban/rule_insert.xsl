<?xml version="1.0"  encoding="UTF-8"?>
<!--
    Extracts rows for table iban_rules
    @(#) $Id: extract_test.xsl 62 2008-12-03 19:25:15Z gfis $
    2014-01-24: do not pad xacct with leading zeroes for okey="(blz)mact"
    2014-01-22: okey, nkey, oacct
    2014-01-20: Georg Fischer

    Activation: xsltproc rule_insert.xsl iban_rules.xml | tee rule_insert.raw
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
        <xsl:for-each select="ci:map">
<!--
from iban_rules_create.sql:
    ( orid          VARCHAR( 8)  NOT NULL
    , okey          VARCHAR( 8)  NOT NULL
    , oblz          VARCHAR( 8)  NOT NULL
    , okto          VARCHAR(20)  NOT NULL
    , ocheck        VARCHAR( 8)
    , nblz          VARCHAR( 8)
    , nacct         VARCHAR(20)
    , nbic          VARCHAR(11)
    , niban         VARCHAR(22)
    , nrule         VARCHAR( 8)
    , nkey          VARCHAR( 8)
-->
            <xsl:value-of select="concat(../@orid , '&#9;')" />
            <xsl:value-of select="concat(../@okey , '&#9;')" />
            <xsl:value-of select="concat(translate(@oblz, ' ','')   , '&#9;')" />
            <xsl:choose>
                <xsl:when test="contains(@oacct, '\')">
                    <xsl:value-of select="concat(@oacct, '&#9;')" />
                </xsl:when>
                <xsl:when test="string-length(@oacct) != 0">
                    <xsl:choose>
                        <xsl:when test="@oacct = '\d+'">
                            <!-- "otherwise" pattern: turn this into a value which is greater than all possible patterns for SELECT ORDER BY -->
                            <xsl:value-of select="concat('\d}' , '&#9;')" />
                        </xsl:when>
                         <xsl:when test="contains(../@okey, 'mact')">
                            <xsl:value-of select="concat(translate(@oacct, ' ','')   , '&#9;')" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="concat(substring('000000000', string-length(translate(@oacct, ' ',''))), translate(@oacct, ' ','')   , '&#9;')" />
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="'&#9;'" />
                </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="concat(@ocheck, '&#9;')" />
            <xsl:value-of select="concat(translate(@nblz, ' ','')   , '&#9;')" />
            <xsl:choose>
                <xsl:when test="contains(@nacct, '\')">
                    <xsl:value-of select="concat(@nacct, '&#9;')" />
                </xsl:when>
                <xsl:when test="string-length(@nacct) != 0">
                    <xsl:choose>
                        <xsl:when test="contains(../@okey, 'mact')">
                            <xsl:value-of select="concat(translate(@nacct, ' ','')   , '&#9;')" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="concat(substring('000000000', string-length(translate(@nacct, ' ',''))), translate(@nacct, ' ','')   , '&#9;')" />
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="'&#9;'" />
                </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="concat(translate(@nbic, ' ','')   , '&#9;')" />
            <xsl:value-of select="concat(translate(@niban,' ','')   , '&#9;')" />
            <xsl:choose>
                <xsl:when test="string-length(@nrid) != 0">
                    <xsl:value-of select="concat(@nrid  , '&#9;')" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat('0000'  , '&#9;')" />
                </xsl:otherwise>
            </xsl:choose>
            <xsl:choose>
                <xsl:when test="string-length(@nkey) != 0">
                    <xsl:value-of select="concat(@nkey   , '&#9;')" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat('rule'  , '&#9;')" />
                </xsl:otherwise>
            </xsl:choose>
            <xsl:value-of select="'&#10;'" />
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
