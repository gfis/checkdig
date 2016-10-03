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
<!-- function="isin", parm1="", parm2=" -->
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
<option value="isin" selected>Internat. Stock Id Number (ISIN)</option>
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
ID100002360<span class="OK">9</span>	!OK
IDA0000001J<span class="OK">1</span>	!OK
HRPLVARA000<span class="OK">4</span>	!OK
HRRIBAO011A<span class="OK">2</span>	!OK
HRVABAPA000<span class="OK">5</span>	!OK
HRRH00O110A<span class="OK">8</span>	!OK
EU000965814<span class="OK">5</span>	!OK
ES0178430E1<span class="OK">8</span>	!OK
FR000013110<span class="OK">4</span>	!OK
FR000012053<span class="OK">7</span>	!OK
FR000013190<span class="OK">6</span>	!OK
IT000006207<span class="OK">2</span>	!OK
IE000019783<span class="OK">4</span>	!OK
NL000000953<span class="OK">8</span>	!OK
NL000030176<span class="OK">0</span>	!OK
BE000380118<span class="OK">1</span>	!OK
GB000798059<span class="OK">1</span>	!OK
CH001200526<span class="OK">7</span>	!OK
SE000010865<span class="OK">6</span>	!OK
DE000502090<span class="OK">3</span>	!OK
US775043102<span class="OK">2</span>	!OK
DE000PREM11<span class="OK">1</span>	!OK
DE000A0D9PT<span class="OK">0</span>	!OK
DE000A0CAYB<span class="OK">2</span>	!OK
DE000TUAG00<span class="OK">0</span>	!OK
DE000717200<span class="OK">9</span>	!OK
DE000803200<span class="OK">4</span>	!OK
AT000096998<span class="OK">5</span>	!OK
FI000900068<span class="OK">1</span>	!OK
WRONGISINXX<span class="NOK">3</span>	?NOK

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
