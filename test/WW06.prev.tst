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
<!-- function="ismn", parm1="", parm2=" -->
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
<option value="ismn" selected>Internat. Standard Music Number (ISMN)</option>
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
M-030-67118-<span class="OK">9</span>	!OK
M-2306-7118-<span class="OK">7</span>	!OK
M-43067-118-<span class="OK">5</span>	!OK
M-830671-18-<span class="OK">1</span>	!OK
M-9306711-8-<span class="OK">0</span>	!OK
M-2000-0084-<span class="OK">9</span>	!OK
M-2042-2521-<span class="OK">7</span>	!OK
M-2042-2528-<span class="OK">6</span>	!OK
M-2042-2529-<span class="OK">3</span>	!OK
M-2042-2547-<span class="OK">7</span>	!OK
M-2042-2579-<span class="OK">8</span>	!OK
M-2042-2582-<span class="OK">8</span>	!OK
M-2042-2652-<span class="OK">8</span>	!OK
M-2042-2689-<span class="OK">4</span>	!OK
979-0-030-67118-<span class="OK">9</span>	!FORM
WRONG_ISMN	?CHAR

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
