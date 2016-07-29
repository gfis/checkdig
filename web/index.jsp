<%@page import="org.teherba.checkdig.BaseChecker"%>
<%@page import="org.teherba.checkdig.DigitChecker"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%--
    Display a test form for various check digit methods
    @(#) $Id: index.jsp 78 2009-02-05 17:11:47Z gfis $
    2016-07-29: DeTaxIdChecker
    2014-01-16: with Dbat specs for blz etc.
    2009-01-13: colored check digit range
    2008-11-18: EAN, ISBN, ISMN, ISSN, PND, UCI
    2007-12-30: metaInf.jsp
    2007-10-10: toUpperCase in result comparision
    2007-02-13: package org.teherba.checkdig
    2005-11-09: with ISIN
    2005-10-08: copied from numword
--%>
<%--
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
--%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Checkdigits</title>
    <link rel="stylesheet" type="text/css" href="stylesheet.css">
</head>
<%
    String CVSID = "@(#) $Id: index.jsp 78 2009-02-05 17:11:47Z gfis $";
    DigitChecker digitChecker = new DigitChecker();

    // 2 parallel arrays, element [0] is ignored
    String[] optFunction = new String []
            /*  0 */ { "dummy"
            /*  1 */ , "acc"
            /*  2 */ , "ean"
            /*  3 */ , "iban"
            /*  4 */ , "isbn"
            /*  5 */ , "isin"
            /*  6 */ , "ismn"
            /*  7 */ , "issn"
            /*  8 */ , "pnd"
            /*  9 */ , "taxid"
            /* 10 */ , "uci"
            /* 11 */ , "vat"
            } ;
    String[] enFunction = new String []
            /*  0 */ { "dummy"
            /*  1 */ , "German Bank Account Number (+ BLZ)"
            /*  2 */ , "Internat. Article Number (EAN)"
            /*  3 */ , "Internat. Bank Account Number (IBAN)"
            /*  4 */ , "Internat. Standard Book Number (ISBN)"
            /*  5 */ , "Internat. Stock Id Number (ISIN)"
            /*  6 */ , "Internat. Standard Music Number (ISMN)"
            /*  7 */ , "Internat. Standard Serial Number (ISSN)"
            /*  8 */ , "Person-Name Database Id (PND-Id)"
            /*  9 */ , "German Tax Identification Number"
            /* 10 */ , "SEPA Unique Creditor Id (UCI)"
            /* 11 */ , "European Value Added Tax (VAT) Id"
            } ;
    Object
    field = session.getAttribute("function");
    String function = (field != null) ? (String) field : "iban";
    field = session.getAttribute("parm1");
    String parm1    = (field != null) ? (String) field : "";
    field = session.getAttribute("parm2");
    String parm2    = (field != null) ? (String) field : "";
%>
<body>
    <!--
    function="<%=function%>", parm1="<%=parm1%>", parm2="<%=parm2%>"
    -->
    <h2>Checkdigits</h2>
    <form action="checkservlet" method="post">
        <table>
            <tr>
                <th align="left">Check Method</th>
                <th>&nbsp;</th>
                <th align="left">Number to be checked</th>
            </tr>
            <tr valign="top">
                <td>
                    <select name="function" size="<%= enFunction.length - 1 %>">
                    <%
                        int ind = 1; // element [0] is ignored
                        while (ind < optFunction.length) {
                            out.write("<option value=\""
                                    + optFunction[ind] + "\""
                                    + (optFunction[ind].equals(function) ? " selected" : "")
                                    + ">"
                                    + enFunction[ind] + "</option>\n");
                            ind ++;
                        } // while ind
                    %>
                    </select>
                </td>
                <th>&nbsp;&nbsp;&nbsp;&nbsp;</th>
                <td>
                    <input name="parm1" maxsize="80" size="40" value="<%=parm1%>"/>
                    <br />&nbsp;
                    <br />Optional Parameter (BLZ):
                    <br />
                    <input name="parm2" maxsize="80" size="40" value="<%=parm2%>"/>
                    <br />
                    <input type="submit" value="Submit">
    <h3>
    <!-- function="<%=function%>", parm1="<%=parm1%>", parm2="<%=parm2%>" -->
    <%
        String result = digitChecker.process(new String[] { "-m", "html", "-" + function, parm1, "-blz", parm2 });
        if (parm1.equals("")) {
    %>
<pre>
<% out.write(result);
%>
</pre>
    <%
        } else { // >= 1 parameter(s)
            out.write(result);
        }
    %>
    </h3>

                </td>
                 <td>
                    &nbsp;
                    &nbsp;
                    &nbsp;
                    &nbsp;
                </td>
                <td>
                    <strong><a href="docs/api/index.html">API</a> Documentation</strong>
                    <br /><a href="de_account.xml">XML definition</a> of German account check methods
                    <br />Stylesheets <a href="account.xsl">1</a>,
                        <a href="checkdig.xsl">2</a> generating corresp. Java methods
                    <br /><a href="metaInf.jsp?view=manifest">Manifest</a>,
                          <a href="metaInf.jsp?view=license" >License</a>,
                          <a href="metaInf.jsp?view=notice"  >References</a>
                    <br />
                    <br /><a href="servlet?spec=blz/blz_search">BLZ Search</a>
                </td>
           </tr>
        </table>
    </form>
    </p>
    <p>
    <font size="-1">
    Questions, remarks to: <a href="mailto:punctum@punctum.com">Dr. Georg Fischer</a>
    </font>
    </p>
</body>
</html>
