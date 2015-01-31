<?xml version="1.0" encoding="US-ASCII"?>
<!--
    Stylesheet for the generation of checkdig's Java methods
    @(#) $Id: checkdig.xsl 78 2009-02-05 17:11:47Z gfis $
    2014-01-20: LF and no tabs
    2008-09-26: template prepend; more xsl:value-of, less xsl:text
    2007-04-26: namespace "xsl" again; timestamp
    2007-02-02: package org.teherba.checkdig
    2005-10-13: package checkdig.account; special code in account.xsl
        xmlns:rec="http://www.teherba.org/2006/checkdig/account"
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
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        >
<xsl:output method="text" />
<xsl:strip-space elements="*" />
<xsl:include href="file:account.xsl" />

<xsl:template match="methods">
    <xsl:call-template name="header"  /> <!-- in account.xsl -->
    <xsl:apply-templates select="method|variant" />
    <xsl:call-template name="trailer" /> <!-- in account.xsl -->
</xsl:template><!-- methods -->

<xsl:template match="method">
    <xsl:call-template name="outMethod">
        <xsl:with-param name="type" select="'public'" />
    </xsl:call-template>
</xsl:template><!-- method -->

<xsl:template match="variant">
    <xsl:call-template name="outMethod">
        <xsl:with-param name="type" select="'private'" />
    </xsl:call-template>
</xsl:template><!-- variant -->

<xsl:template name="outMethod">
    <xsl:param name="type" />
    <xsl:text>
    /** </xsl:text>
        <xsl:if test="$type = 'private'">
            <xsl:text>Auxiliary </xsl:text>
        </xsl:if>
        <xsl:value-of select="concat('Method ', @id)" />
        <xsl:if test="string-length(@bank) > 0">
            <xsl:value-of select="concat(', ', @bank)" />
        </xsl:if>
        <xsl:if test="string-length(@like) > 0">
            <xsl:value-of select="concat(', like ', @like)" />
        </xsl:if>
        <xsl:if test="string-length(@since) > 0">
            <xsl:value-of select="concat(' (since ', @since, ')')" />
        </xsl:if>
        <xsl:text>
     *  @param account number to be checked
     *  @return unchanged or corrected number, or empty string in case of a fatal error
     */
    </xsl:text>
    <xsl:value-of select="concat($type, ' String method_', @id, '(String account) {')" /><xsl:text>
            StringBuffer result = new StringBuffer(account);</xsl:text>
        <xsl:call-template name="statements" />
        <xsl:value-of select="concat('&#10;            // System.out.print(&quot; sum(', @id, ')=&quot; + sum);')" />
        <xsl:text>
            return result.toString();
    </xsl:text>
    <xsl:value-of select="concat('    } // method_', @id, '&#10;')" />
</xsl:template><!-- outMethod -->

<xsl:template match="account">
    <xsl:text>
            sum = accountValue;</xsl:text>
    <xsl:if test="string-length(@div) &gt; 0">
        <xsl:value-of select="concat('&#10;            sum = (long) (sum / ', @div, 'l + 0.5);')" />
    </xsl:if>
    <xsl:if test="string-length(@mod) &gt; 0">
        <xsl:value-of select="concat('&#10;            sum %= ', @mod, 'l;')" />
    </xsl:if>
</xsl:template><!-- account -->

<xsl:template match="call">
    <xsl:value-of select="concat('&#10;                result = new StringBuffer(method_', @method, '(account));')" />
</xsl:template><!-- call -->

<xsl:template match="choose">
    <xsl:apply-templates select="when|otherwise" />
</xsl:template><!-- choose -->

<xsl:template match="when">
    <xsl:if test="position() != 1">
            <xsl:text>
            else </xsl:text>
    </xsl:if>
    <xsl:value-of select="concat('&#10;            if (sum ', '')" />
    <xsl:choose>
        <xsl:when test="string-length(@eq) &gt; 0">
            <xsl:value-of select="concat('== ', @eq, 'l) {')" />
        </xsl:when>
        <xsl:when test="string-length(@lt) &gt; 0">
            <xsl:value-of select="concat('&lt;  ', @lt, 'l) {')" />
        </xsl:when>
        <xsl:when test="string-length(@le) &gt; 0">
            <xsl:value-of select="concat('&lt;= ', @le, 'l) {')" />
        </xsl:when>
        <xsl:when test="string-length(@from) &gt; 0">
            <xsl:value-of select="concat('&gt;= ', @from, 'l &amp;&amp; sum &lt;= ', @to, 'l) {')" />
        </xsl:when>
    </xsl:choose>
    <xsl:call-template name="statements" />
    <xsl:text>
            }</xsl:text>
</xsl:template><!-- when -->

<xsl:template match="otherwise">
            <xsl:text>
            else {</xsl:text>
            <xsl:call-template name="statements" />
            <xsl:text>
            }</xsl:text>
</xsl:template><!-- otherwise -->

<xsl:template match="code">
    <xsl:value-of select="concat('&#10;                result = new StringBuffer(method_', @method, '(account));')" />
</xsl:template><!-- code -->

<xsl:template match="set">
    <xsl:value-of select="concat('&#10;                sum = digit[', @pos, '];')" />
</xsl:template><!-- set -->

<xsl:template match="sequence">
    <xsl:text>
            String trial = "";</xsl:text>
    <xsl:for-each select="call|code">
        <xsl:value-of select="concat('&#10;            if (account.equals(trial += method_', @method, '(account))) {')" />
        <xsl:value-of select="concat('&#10;            } else')" />
    </xsl:for-each>
    <xsl:text>
            {
                result = new StringBuffer(trial.substring(0, LEN_ACCOUNT));
            }</xsl:text>
</xsl:template><!-- sequence -->

<xsl:template name="statements">
    <xsl:apply-templates select="account|call|change|check|choose|code|modulo|premod|prepend|product|sequence|set" />
</xsl:template><!-- statements -->

<xsl:template match="prepend"><!-- prefix the account with some additional digits -->
    <xsl:if test="string-length(@value) &gt; 0">
        <xsl:value-of select="concat('&#10;                    int vpos = ', @value, '.length();')" />
        <xsl:value-of select="concat('&#10;                    int dpos = ', @pos, ' - 1;')" />
        <xsl:value-of select="concat('&#10;                    while (vpos &gt; 0) {', '')" />
        <xsl:value-of select="concat('&#10;                        digit[++ dpos] = Character.digit(', @value, '.charAt(-- vpos), 10);')" />
        <xsl:value-of select="concat('&#10;                    } // while vpos','')" />
    </xsl:if>
</xsl:template><!-- prepend -->

<xsl:template match="product">
    <xsl:text>
            int weights[] = {</xsl:text><xsl:value-of select="@weights"/><xsl:text>};
            sum = 0l;
            int iweight = 0; // index into weights
            int prod = 0;
            </xsl:text>
            <xsl:value-of select="concat('for (int ipos = ', @from, '; ipos &lt;= ', @to, '; ipos ++) {')" /><xsl:text>
                prod = digit[ipos] * weights[iweight];</xsl:text>
        <xsl:choose>
            <xsl:when test="@special = 'sumdigits'">
                <xsl:text>
                prod = prod % 10 + (int) (prod / 10 + 0.5);</xsl:text>
            </xsl:when>
            <xsl:when test="@special = 'lastdigit'">
                <xsl:text>
                prod %= 10;</xsl:text>
            </xsl:when>
            <xsl:when test="@special = 'code24'">
                <xsl:text>
                prod += weights[iweight];</xsl:text>
            </xsl:when>
            <xsl:when test="@special = 'code27'">
                <xsl:text>
                prod = weights[10 * weights[iweight + 1] + digit[ipos]];</xsl:text>
            </xsl:when>
            <xsl:when test="@special = 'sumweight'">
                <xsl:text>
                prod = (digit[ipos] * weights[iweight] + weights[iweight]) % 11;</xsl:text>
            </xsl:when>
        </xsl:choose>
        <xsl:text>
                sum += prod;
                iweight ++;
            } // for ipos</xsl:text>
</xsl:template><!-- product -->

<xsl:template match="premod">
    <xsl:choose>
        <xsl:when test="@method = '17'">
            <xsl:text>
            sum --;</xsl:text>
        </xsl:when>
        <xsl:when test="@method = '21'">
            <xsl:text>
            sum = sum % 10
                    + ((long) (sum /  10 + 0.5)) % 10
                    +  (long) (sum / 100 + 0.5)
                    ;
            sum = sum % 10
                    + ((long) (sum /  10 + 0.5)) % 10
                    +  (long) (sum / 100 + 0.5)
                    ;</xsl:text>
        </xsl:when>
    </xsl:choose>
</xsl:template><!-- premod -->

<xsl:template match="modulo">
    <xsl:value-of         select="concat('&#10;            sum %= ', @base, 'l;')" />
    <xsl:choose>
        <xsl:when test="string-length(@subfrom) &gt; 0">
            <xsl:value-of select="concat('&#10;            sum = ', @subfrom, 'l - sum;')" />
        </xsl:when>
    </xsl:choose>
</xsl:template><!-- modulo -->

<xsl:template match="change">
    <xsl:value-of select="concat('&#10;            if (sum == ', @from, 'l) {')" />
    <xsl:value-of select="concat('&#10;                sum = ', @to, 'l;')" />
    <xsl:value-of select="concat('&#10;            }', '')" />
</xsl:template><!-- change -->

<xsl:template match="check">
    <xsl:choose>
        <xsl:when test="string-length(@pos) &gt; 0">
            <xsl:if test="@pos != 1">
                <xsl:value-of select="concat('&#10;            setCheckRange(', @pos, ');')" />
            </xsl:if>
            <xsl:value-of     select="concat('&#10;            if (sum != digit[', @pos, ']')" />
            <xsl:if test="@incr != 1"><!-- for B9 -->
                <xsl:value-of select="concat(' &amp;&amp; (sum + ', @incr, 'l) % 10l != digit[', @pos, ']')" />
            </xsl:if>
            <xsl:value-of     select="concat(') {', '')" />
            <xsl:value-of     select="concat('&#10;                result.setCharAt(LEN_ACCOUNT - ', @pos, ', CHAR_DIGITS.charAt((int) sum));')" />
            <xsl:text>
            }</xsl:text>
        </xsl:when>
        <xsl:when test="@result = 'true'">
            <xsl:text>
            setCheckRange(0, 0);
            // ok, result.equals(account)</xsl:text>
        </xsl:when>
        <xsl:when test="@result = 'false'">
            <xsl:text>
            setCheckRange(0, 0);
            result.setLength(0); // error</xsl:text>
        </xsl:when>
    </xsl:choose>
</xsl:template><!-- check -->

</xsl:stylesheet>
