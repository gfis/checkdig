<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8" />
<meta name="robots" content="noindex, nofollow" />
<link rel="stylesheet" title="common" type="text/css" href="stylesheet.css" />
<title>CheckDig Main Page</title>
</head>
<body>
<!-- function="uci", parm1="", parm2=" -->
<h2>Checkdigits</h2>
<form action="servlet" method="post">
    <table>
        <tr><th align="left">Check Method</th>
            <th>&nbsp;</th>
            <th align="left">Number to be checked</th>
        </tr>
        <tr valign="top">
            <td><select name="function" size="11">
<option value="acc">German Bank Account Number (+ BLZ)</option>
<option value="ean">Internat. Article Number (EAN)</option>
<option value="iban">Internat. Bank Account Number (IBAN)</option>
<option value="isbn">Internat. Standard Book Number (ISBN)</option>
<option value="isin">Internat. Stock Id Number (ISIN)</option>
<option value="ismn">Internat. Standard Music Number (ISMN)</option>
<option value="issn">Internat. Standard Serial Number (ISSN)</option>
<option value="pnd">Person-Name Database Id (PND-Id)</option>
<option value="taxid">German Tax Identification Number</option>
<option value="uci" selected>SEPA Unique Creditor Id (UCI)</option>
<option value="vat">European Value Added Tax (VAT) Id</option>
                </select>
                <br />
<a title="account"     href="spec/de_account.xml">XML definition</a> of <br />&nbsp;&nbsp;German account check methods<br />
<a title="xsl"         href="xslt/account.xsl">Stylesheet</a> generating<br />&nbsp;&nbsp;corresponding Java Methods<br />
<a title="deblz"       href="servlet?spec=blz/blz_search">German BLZ search</a><br />
<a title="wiki"        href="http://www.teherba.org/index.php/CheckDig" target="_new">Wiki</a> Documentation<br />
<a title="github"      href="https://github.com/gfis/checkdig" target="_new">Git Repository</a><br />
<a title="api"         href="docs/api/index.html">Java API</a> Documentation<br />
<a title="manifest"    href="servlet?view=manifest">Manifest</a>, <a title="license"     href="servlet?view=license">License</a>, <a title="notice"      href="servlet?view=notice">References</a><br />
            </td>
            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
            <td>
                <input name="parm1" maxsize="80" size="40" value=""/>
                <br />&nbsp;
                <br />Optional Parameter (BLZ):
                <br />
                <input name="parm2" maxsize="80" size="40" value=""/>
                <br />
                <input type="submit" value="Submit">
                <h3>

<pre>
DE<span class="OK">79</span>01234567890	!OK
DE<span class="OK">34</span>00000001588	!OK
WR<span class="NOK">31</span>CIS	?NOK
AT<span class="NOK">61</span>01234567890	?NOK
BE<span class="NOK">68</span>123456789	?NOK
FR<span class="NOK">92</span>12345678	?NOK
FR<span class="NOK">91</span>12345678901	?NOK
FR<span class="NOK">90</span>123456789	?NOK
FR<span class="NOK">28</span>12345678901234	?NOK
HU<span class="NOK">15</span>A09450512	?NOK
HU<span class="NOK">47</span>E10011922T201	?NOK
FI<span class="NOK">22</span>12345678	?NOK
IT<span class="NOK">36</span>A1B2C	?NOK
IE<span class="NOK">84</span>123456	?NOK
LU<span class="NOK">27</span>0123456789	?NOK
NL<span class="NOK">36</span>0123456789	?NOK
PL<span class="NOK">18</span>0123456789	?NOK
PT<span class="NOK">73</span>123456	?NOK
ES<span class="NOK">50</span>M23456789	?NOK
GB<span class="OK">14</span>123456	!OK
DE<span class="NOK">79</span>01234567890	?NOK

</pre>
                </h3>
            </td>
       </tr>
    </table>
</form>
<!-- language="en", features="quest" -->
<p><span style="font-size:small">
Questions, remarks: email to  <a href="mailto:punctum@punctum.com?&subject=CheckDig">Dr. Georg Fischer</a></span></p>
</body></html>
