<?xml version="1.0"  encoding="UTF-8"?>
<!--
    Extracts test cases from account checking method descriptions
    @(#) $Id: extract_test.xsl 62 2008-12-03 19:25:15Z gfis $
    2014-01-20: LF and no tabs
    2008-09-29: 4 columns: method, account, blz, polarity
    2007-04-26, Georg Fischer

    Activation: xalan -i 0 -o outputfile inputfile extract_test.xsl
-->
<!--
 * Copyright 2006 Dr. Georg Fischer <punctum at punctum dot kom>
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
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="text"/>

    <xsl:template match="methods">
        <xsl:for-each select=".//test">
            <xsl:choose>
                <xsl:when test="string-length(@blz) &gt; 0">
                    <xsl:value-of select="concat(@id, ' ', @account, ' ', @blz, ' ', @result, '&#10;')" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="concat(@id, ' ', @account, ' 00000000 ', @result, '&#10;')" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
