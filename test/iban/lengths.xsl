<?xml version="1.0" encoding="UTF-8"?>
<!--
    Extract all SEPA country codes from the SWIFT IBAN Registry
    @(#) $Id: gen_parm_xref.xsl 864 2012-01-23 19:31:18Z gfis $
    2013-12-30: Dr. Georg Fischer: copied from dbat/etc/xslt/gen_parm_xref.xsl
-->
<!--
 * Copyright 2013 Dr. Georg Fischer <punctum at punctum dot kom>
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
    <xsl:output method="text" /> <!-- i.e. Java source code -->
    <xsl:strip-space elements="*" /> <!-- remove whitespace in all nodes -->
    <xsl:variable name="init">
        <xsl:value-of select='""' />
    </xsl:variable>
    <xsl:variable name="term">
        <xsl:value-of select='"&#10;"' />
    </xsl:variable>
    <xsl:variable name="sep">
        <xsl:value-of select='"&#9;"' />
    </xsl:variable>

    <xsl:template match="ci:IBAN-Registry">
    <!--
        <xsl:text>.dummy-s
</xsl:text>
	-->
        <xsl:for-each select="//ci:country">
            <xsl:value-of select="concat(ci:Country-code, ' ', ci:BBAN-length, ' ', ci:IBAN-length, '&#10;')" />
            <xsl:if test="ci:BBAN-length + 4 != ci:IBAN-length">
            	<xsl:value-of select="concat('BBAN + 4 != IBAN', '&#10;')" />
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
