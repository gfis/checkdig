<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml;charset=UTF-8" />
<meta name="robots" content="noindex, nofollow" />
<link rel="stylesheet" type="text/css" href="stylesheet.css" />
<title>CheckDig Main Page</title>
</head>
<body>
<!-- function="iban", parm1="", parm2=" -->
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
<option value="iban" selected>Internat. Bank Account Number (IBAN)</option>
<option value="isbn">Internat. Standard Book Number (ISBN)</option>
<option value="isin">Internat. Stock Id Number (ISIN)</option>
<option value="ismn">Internat. Standard Music Number (ISMN)</option>
<option value="issn">Internat. Standard Serial Number (ISSN)</option>
<option value="pnd">Person-Name Database Id (PND-Id)</option>
<option value="taxid">German Tax Identification Number</option>
<option value="uci">SEPA Unique Creditor Id (UCI)</option>
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
AD<span class="OK">12</span> 0001 2030 2003 5910 0100	!OK
AT<span class="OK">61</span> 1904 3002 3457 3201	!OK
AT<span class="OK">35</span> 1200 0515 6805 2701	!OK
BE<span class="OK">68</span> 5390 0754 7034	!OK
BE<span class="OK">62</span> 5100 0754 7061	!OK
CH<span class="OK">93</span> 0076 2011 6238 5295 7	!OK
CY<span class="OK">17</span> 0020 0128 0000 0012 0052 7600	!OK
CZ<span class="OK">65</span> 0800 0000 1920 0014 5399	!OK
DE<span class="OK">89</span> 3704 0044 0532 0130 00	!OK
DK<span class="OK">50</span> 0040 0440 1162 43	!OK
EE<span class="OK">38</span> 2200 2210 2014 5685	!OK
ES<span class="OK">91</span> 2100 0418 4502 0005 1332	!OK
FI<span class="OK">21</span> 1234 5600 0007 85	!OK
FO<span class="OK">62</span> 6460 0001 6316 34	!OK
FO<span class="OK">95</span> 6460 0002 0016 77	!OK
FO<span class="OK">36</span> 9181 0002 9324 33	!OK
FR<span class="OK">76</span> 1820 6000 1030 5696 6400 117	!OK
FR<span class="OK">14</span> 2004 1010 0505 0001 3M02 606	!OK
GB<span class="OK">29</span> NWBK 6016 1331 9268 19	!OK
GL<span class="OK">50</span> 6471 0001 4414 82	!OK
GL<span class="OK">41</span> 6471 0001 0015 55	!OK
GI<span class="OK">75</span> NWBK 0000 0000 7099 453	!OK
GR<span class="OK">16</span> 0110 1250 0000 0001 2300 695	!OK
HR<span class="OK">12</span> 1001 0051 8630 0016 0	!OK
HU<span class="OK">42</span> 1177 3016 1111 1018 0000 0000	!OK
IE<span class="OK">29</span> AIBK 9311 5212 3456 78	!OK
IL<span class="OK">62</span> 0108 0000 0009 9999 999	!OK
IS<span class="OK">14</span> 0159 2600 7654 5510 7303 39	!OK
IT<span class="OK">60</span> X054 2811 1010 0000 0123 456	!OK
LI<span class="OK">21</span> 0881 0000 2324 013A A	!OK
LV<span class="OK">80</span> BANK 0000 4351 9500 1	!OK
LT<span class="OK">12</span> 1000 0111 0100 1000	!OK
LU<span class="OK">28</span> 0019 4006 4475 0000	!OK
MC<span class="OK">58</span> 1244 8610 1776 1053 3010 111	!OK
MC<span class="OK">75</span> 1273 9000 7201 0919 0000 J37	!OK
MC<span class="OK">58</span> 1149 8000 0162 5023 9000 377	!OK
MC<span class="OK">58</span> 1261 9000 2300 0038 2435 419	!OK
MC<span class="OK">10</span> 1273 9000 7201 1468 3000 U30	!OK
MK<span class="OK">07</span> 3000 0000 0042 425	!FORM
MT<span class="OK">84</span> MALT 0110 0001 2345 MTLC AST0 01S	!OK
NL<span class="OK">91</span> ABNA 0417 1643 00	!OK
NO<span class="OK">93</span> 8601 1117 947	!OK
PL<span class="OK">27</span> 1140 2004 0000 3002 0135 5387	!OK
PT<span class="OK">50</span> 0002 0123 1234 5678 9015 4	!OK
RO<span class="OK">49</span> AAAA 1B31 0075 9384 0000	!OK
CS<span class="OK">73</span> 2600 0560 1001 6113 79	!OK
SA<span class="OK">03</span> 8000 0000 6080 1016 7519	!OK
SE<span class="OK">35</span> 5000 0000 0549 1000 0003	!OK
SI<span class="OK">56</span> 1910 0000 0123 438	!OK
SK<span class="OK">31</span> 1200 0000 1987 4263 7541	!OK
SM<span class="OK">86</span> U032 2509 8000 0000 0270 100	!OK
TN<span class="OK">59</span> 1420 7207 1007 0712 9648	!OK
TR<span class="OK">62</span> 0001 2009 8890 0058 0088 88	!OK
TR<span class="OK">07</span> 0006 2000 0140 0006 2996 08	!OK
?CHAR
DE<span class="NOK">20</span> 6601 0075 0352 3097 57	?NOK
DE<span class="NOK">50</span> 6723 0000 4059 5290 56	?NOK

</pre>
                </h3>
            </td>
       </tr>
    </table>
</form>
<p><span style="font-size:small">
Questions, remarks: email to  <a href="mailto:punctum@punctum.com?&subject=CheckDig">Dr. Georg Fischer</a></span></p>
</body></html>
