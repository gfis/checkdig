<?xml version="1.0"  encoding="UTF-8"?>
<!--
    How to access rows in table iban_rules
    @(#) $Id: extract_test.xsl 62 2008-12-03 19:25:15Z gfis $
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
    <xsl:output method="text" /> <!-- a single SQL INSERT statement per line -->
    <xsl:strip-space elements="*" /> <!-- remove whitespace in all nodes -->

<!--
from iban_access_create.sql:
    ( orid          VARCHAR( 8)  NOT NULL
    , okey          VARCHAR( 8)  NOT NULL
-->
    <xsl:template match="ci:IBAN-Rules/ci:rule">
        <xsl:value-of select="concat(@orid, '&#9;', @okey, '&#10;')" />
    </xsl:template>

</xsl:stylesheet>
