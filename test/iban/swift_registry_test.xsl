<?xml version="1.0"  encoding="UTF-8"?>
<!--
    Extracts IBAN test cases from SWIFT IBAN Registry
    @(#) $Id: extract_test.xsl 62 2008-12-03 19:25:15Z gfis $
    2014-01-20: Georg Fischer

    Activation: xsltproc swift_registry_test.xsl swift_registry.xml > swift_registry.dat
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
    <xsl:output method="text" /> <!-- a single IBAN per line -->
    <xsl:strip-space elements="*" /> <!-- remove whitespace in all nodes -->

    <xsl:template match="ci:IBAN-Registry">
        <xsl:for-each select="//ci:country">
            <xsl:value-of select="concat(ci:IBAN-electronic-format-example, '&#10;')" />
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
