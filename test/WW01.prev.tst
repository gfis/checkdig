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
<!-- function="account", parm1="", parm2=" -->
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
012322060<span class="OK">0</span>	!OK	50010060
016621466<span class="OK">6</span>	!OK	59010066
189018279<span class="OK">4</span>	!OK	70020270
002039220<span class="OK">7</span>	!OK	68291200
002201955<span class="OK">0</span>	!OK	68050101
000231450<span class="OK">9</span>	!OK	67090000
000283204<span class="OK">9</span>	!OK	60050101
003134575<span class="OK">3</span>	!OK	66010075
039406070<span class="OK">4</span>	!OK	60010070
000005260<span class="OK">3</span>	!OK	50010060
0575818<span class="OK">0</span>12	!OK	50040000
0494606<span class="OK">6</span>12	!OK	20080000
<span class="OK"></span>0068001507	!OK	68000000
740204579<span class="OK">0</span>	!OK	60050101
<span class="OK"></span>0007805260	!FORM	30050000
<span class="OK"></span>0000085995	!FORM	66050000
009700001<span class="OK">8</span>	!FORM	68092000
WRONGACCTS	?CHAR	00000000
000206245<span class="NOK">3</span>	?NOK	68052025
000006325<span class="NOK">0</span>	?NOK	76350000
0575818<span class="NOK">0</span>12	?NOK	50040000
0494606<span class="NOK">6</span>12	?NOK	20080000

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
