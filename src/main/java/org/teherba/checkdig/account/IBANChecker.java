/*  IBANChecker.java - check International Bank Account Numbers
    @(#) $Id: IBANChecker.java 77 2009-01-16 08:14:16Z gfis $
    2009-01-09: result of 'check' is: new (formatted) number, space, [questionmark|exclamationmark]returnstring
    2008-11-18: success and error return codes
    2008-03-21: MA??; Berlin -> Andernach
    2008-02-13: Java 1.5 types
    2007-10-10: lowercase letters in IBAN allowed, same checksum
    2007-06-11: polishing
    2007-05-24: name was IbanChecker
    2007-04-10: few more TESTCASES, and checking without spaces in them
    2005-08-30: recoded with BigInteger
    2003-08-19
References:
    http://www.swift.com/dsp/resources/documents/IBAN_Registry.pdf

Algorithm:
    read lines with IBANs
    check them according to the international algorithm

Activation:
    java -cp dist/checkdig.jar org.teherba.checkdig.account.IBANChecker file-with-IBANs
*/
/*
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
 */
package org.teherba.checkdig.account;
import  org.teherba.checkdig.BaseChecker;
import  org.teherba.checkdig.account.IBANDetailBean;
import  java.io.BufferedReader;
import  java.io.FileReader;
import  java.math.BigInteger;
import  java.util.HashMap;

/** Class for the checkdigits in International Bank Account Numbers.
 *  Several test numbers are found below.
 *  @author Dr. Georg Fischer
 */
public class IBANChecker extends BaseChecker {
    public final static String CVSID = "@(#) $Id: IBANChecker.java 77 2009-01-16 08:14:16Z gfis $";

    /** No-args constructor
     */
    public IBANChecker() {
        super();
        defineDetails();
    } // Constructor

    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
    public String[] getTestCases() {
        return new String[]
            { "AD12 0001 2030 2003 5910 0100"
            , "AT61 1904 3002 3457 3201"
            , "AT35 1200 0515 6805 2701"
            , "BE68 5390 0754 7034"
            , "BE62 5100 0754 7061"
            , "CH93 0076 2011 6238 5295 7"
            , "CY17 0020 0128 0000 0012 0052 7600"
            , "CZ65 0800 0000 1920 0014 5399"
            , "DE89 3704 0044 0532 0130 00"
            , "DK50 0040 0440 1162 43"
            , "EE38 2200 2210 2014 5685"
            , "ES91 2100 0418 4502 0005 1332"
            , "FI21 1234 5600 0007 85"
            , "FO62 6460 0001 6316 34"
            , "FO95 6460 0002 0016 77"
            , "FO36 9181 0002 9324 33"
            , "FR76 1820 6000 1030 5696 6400 117"
            , "FR14 2004 1010 0505 0001 3M02 606"
            , "GB29 NWBK 6016 1331 9268 19"
            , "GL50 6471 0001 4414 82"
            , "GL41 6471 0001 0015 55"
            , "GI75 NWBK 0000 0000 7099 453"
            , "GR16 0110 1250 0000 0001 2300 695"
            , "HR12 1001 0051 8630 0016 0"
            , "HU42 1177 3016 1111 1018 0000 0000"
            , "IE29 AIBK 9311 5212 3456 78"
            , "IL62 0108 0000 0009 9999 999"
            , "IS14 0159 2600 7654 5510 7303 39"
            , "IT60 X054 2811 1010 0000 0123 456"
            , "LI21 0881 0000 2324 013A A"
            , "LV80 BANK 0000 4351 9500 1"
            , "LT12 1000 0111 0100 1000"
            , "LU28 0019 4006 4475 0000"
            , "MC58 1244 8610 1776 1053 3010 111"
            , "MC75 1273 9000 7201 0919 0000 J37"
            , "MC58 1149 8000 0162 5023 9000 377"
            , "MC58 1261 9000 2300 0038 2435 419"
            , "MC10 1273 9000 7201 1468 3000 U30"
            , "MK07 3000 0000 00424 25"
            , "MT84 MALT 0110 0001 2345 MTLC AST0 01S"
            , "NL91 ABNA 0417 1643 00"
            , "NO93 8601 1117 947"
            , "PL27 1140 2004 0000 3002 0135 5387"
            , "PT50 0002 0123 1234 5678 9015 4"
            , "RO49 AAAA 1B31 0075 9384 0000"
            , "CS73 2600 0560 1001 6113 79"
            , "SA03 8000 0000 6080 1016 7519"
            , "SE35 5000 0000 0549 1000 0003"
            , "SI56 1910 0000 0123 438"
            , "SK31 1200 0000 1987 4263 7541"
            , "SM86 U032 2509 8000 0000 0270 100"
            , "TN59 1420 7207 1007 0712 9648"
            , "TR62 0001 2009 8890 0058 0088 88"
            , "TR07 0006 2000 0140 0006 2996 08"
            , "WRONG_IBANs"
            , "DE21 6601 0075 0352 3097 57"  // -> DE20 ...
            , "DE51 6723 0000 4059 5290 56"  // -> DE50 ...
            };
    } // getTestCases

    /** Computes the check digits of an IBAN.
     *  @param rawNumber IBAN to be tested
     *  @return recomputed IBAN with proper check digits,
     *  or "too short" if length(IBAN) < 4
     */
    public String check(String rawNumber) {
        String result = null;
        StringBuffer buffer = new StringBuffer(64);
        String iban = trim(rawNumber).toUpperCase();
        int ipos = 4; // start behind the check digits
        if (iban.length() >= ipos) {
            while (ipos < iban.length()) {
                char ch = iban.charAt(ipos);
                int imap = BaseChecker.LETTER_MAP.indexOf(ch);
                if (imap < 0) { // not found
                    imap = BaseChecker.letter_MAP.indexOf(ch);
                    if (imap < 0) {
                        return BaseChecker.WRONG_CHAR;
                    } else { // lowercase letter
                        buffer.append(Integer.toString(imap)); // 2 digits >= 10
                    }
                } else if (imap < 10) { // digit
                    buffer.append(ch);
                } else { // uppercase letter
                    buffer.append(Integer.toString(imap)); // 2 digits >= 10
                }
                ipos ++;
            } // while ipos
            // now append the mapping of the ISO country code
            ipos = 0;
            int itar = 0;
            while (ipos < iban.length() && itar < 2) { // append the ISO country code
                char ch = iban.charAt(ipos);
                int imap = BaseChecker.LETTER_MAP.indexOf(ch);
                if (imap < 0) {
                    return BaseChecker.WRONG_CHAR;
                } else if (imap < 10) { // digit
                    buffer.append(ch);
                    itar ++;
                } else { // letter
                    buffer.append(Integer.toString(imap)); // 2 digits >= 10
                    itar ++;
                }
                ipos ++;
            } // while ipos
            buffer.append("00"); // append 2 fictional check digits
            String newCheck = Integer.toString(98 - (new BigInteger(buffer.toString()).mod(new BigInteger("97"))).intValue());
            if (newCheck.length() <= 1) {
                newCheck = "0" + newCheck;
            }
            result = checkResponse(rawNumber, format(iban), 2, 2, newCheck);
        } else {
            result = checkResponse(rawNumber, BaseChecker.TOO_SHORT);
        }
        return result;
    } // check

    /** Formats a number: inserts spaces between groups of 4 characters,
     *  and converts all letters to upper case.
     *  @param rawNumber format this number (may contain hyphens, dots or spaces)
     */
    public String format(String rawNumber) {
        StringBuffer result = new StringBuffer(64);
        int pos = 0;
        while (pos + 4 <= rawNumber.length()) {
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(rawNumber.substring(pos, pos + 4));
            pos += 4;
        } // while pos
        if (pos < rawNumber.length()) {
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(rawNumber.substring(pos));
        }
        return result.toString();
    } // format

    /** Maps country codes to IBAN details */
    private HashMap/*<1.5*/<String, IBANDetailBean>/*1.5>*/ map;

    /** Defines the country specific details of one European IBAN.
     */
    private void define1(String cc, int width, int bbidStart, int bbidLen, int acctStart, int acctLen, int chkMethod, int chkStart, int chkLen) {
        map.put(cc, new IBANDetailBean(cc, width,  bbidStart,     bbidLen ,    acctStart,     acctLen,     chkMethod,     chkStart,     chkLen));
        if (width != acctStart + acctLen || acctStart != bbidStart + bbidLen) {
            System.err.println("inconsistent lengths for " + cc);
        }
    } // define1

    /** Defines the country specific details of all known IBANs.
     *  Derived from ECBS "IBAN Registry", TR201 V3.23 - February 2007.
     *  http://www.swift.com/, and the BICIBAN+ directory
     */
    public void defineDetails() {
        map = new HashMap/*<1.5*/<String, IBANDetailBean>/*1.5>*/(64);
        //       cc   len  bs bl  as  al  cm  cs  cl
        define1("AD", 24,  4,  8, 12, 12,  0,  0,  0); // Andorra: 4 bank, 4 branch
        define1("AT", 20,  4,  5,  9, 11,  0,  0,  0); // Austria: Postgiro = 60000
        define1("BE", 16,  4,  3,  7,  9,  1, 14,  2); // Belgium: MOD 97-10 (?)
        define1("BA", 18,  4,  6, 10,  8,  2, 18,  2); // Bosnia and Herzegovina; 3 bank, 3 branch, MOD 97-10
        define1("BG", 22,  4,  8, 12, 10,  0,  0,  0); // Bulgaria; 4 bank, 4 branch, 2 id, 8 spec
        define1("CH", 21,  4,  5,  9, 12,  0,  0,  0); // Switzerland; special postgiro, c.f. LI
        define1("CY", 28,  4,  8, 12, 16,  0,  0,  0); // Cyprus
        define1("CZ", 24,  4,  4,  8, 16,  3, 23,  1); // Czechia: mod 11? special weights
        define1("DE", 22,  4,  8, 12, 10,  7,  0,  0); // Germany: > 110 different German algorithms, depending on bbid=BLZ
        define1("DK", 18,  4,  4,  8, 10,  0,  0,  0); // Denmark
        define1("EE", 20,  4,  2,  6, 14,  4, 19,  1); // Estonia: 7,3,1,...
        define1("FI", 18,  4,  6, 10,  8,  5, 17,  1); // Finland: mod 10; 2,1,2,1...
        define1("FO", 18,  4,  4,  8, 10,  0,  0,  0); // Faroer: like DK
        define1("FR", 27,  4, 10, 14, 13,  6, 25,  2); // France: mod 97
        define1("GB", 22,  4, 10, 14,  8,  0,  0,  0); // Great Britain: 4 BIC, 6 sort
        define1("GI", 23,  4,  4,  8, 15,  0,  0,  0); // Gibraltar
        define1("GL", 18,  4,  4,  8, 10,  0,  0,  0); // Greenland: like DK
        define1("GR", 27,  4,  7, 11, 16,  0,  0,  0); // Greece: 3 bank, 4 branch
        define1("HR", 21,  4,  7, 11, 10,  3, 19,  2); // Croatia, MOD 11,10
        define1("HU", 28,  4,  8, 12, 16,  8,  0,  0); // Hungary: 9,7,3,1 on bank and account separately
        define1("IS", 26,  4,  4,  8, 18,  9,  0,  0); // Iceland: 2,3,4...
        define1("IE", 22,  4, 10, 14,  8,  0,  0,  0); // Ireland
        define1("IL", 23,  4,  6, 10, 13,  0,  0,  0); // Israel: 3 bank, 3 branch
        define1("IT", 27,  4, 11, 15, 12, 10,  4,  1); // Italy: 1 check char, 5 bank, 5 branch, 12 acct; c.f. SM
        define1("LV", 21,  4,  4,  8, 13,  0,  0,  0); // Litavia: IBAN only
        define1("LI", 21,  4,  5,  9, 12,  0,  0,  0); // Liechtenstein: special postgiro, c.f. CH
        define1("LT", 20,  4,  5,  9, 11,  0,  0,  0); // Letland: IBAN only
        define1("LU", 20,  4,  3,  7, 13,  0,  0,  0); // Luxembourgh: IBAN only
        define1("MA", 24,  4,  4,  8, 16, 17, 22,  2); // Morocco (not yet registered) MAkk BBBA AACC CCCC CCCC CCKK
        define1("MC", 27,  4, 10, 14, 13,  6, 25,  2); // Monaco: like FR
        define1("ME", 22,  4,  3,  7, 15,  1, 20,  2); // Montenegro
        define1("MK", 19,  4,  3,  7, 12,  1, 17,  2); // Macedonia: MOD 97-10
        define1("MT", 31,  4,  9, 13, 18,  0,  0,  0); // Malta: 4 bank, 5 sort
        define1("MU", 30,  4,  8, 12, 18,  0,  0,  0); // Mauritius; 4 BIC, 2 bank, 2 branch, 12 acct, 3 reserved, 3 currency
        define1("NL", 18,  4,  4,  8, 10, 11, 17,  1); // Netherlands: 4 BIC, mod 11
        define1("NO", 15,  4,  4,  8,  7, 12, 14,  1); // Norway: mod 11
        define1("PL", 28,  4,  8, 12, 16, 13,  2,  2); // Poland: ISO 13616
        define1("PT", 25,  4,  8, 12, 13,  1, 23,  2); // Portugal: MOD 97-10
        define1("RO", 24,  4,  4,  8, 16,  0,  0,  0); // Romania: 4 BIC, IBAN only
        define1("RS", 22,  4,  3,  7, 15,  1, 20,  2); // Serbia; MOD 97-10
        define1("SA", 24,  4,  2,  6, 18,  0,  0,  0); // Saudi Arabia
        define1("SM", 27,  4, 11, 15, 12,  0,  0,  0); // San Marino: like IT
        define1("SK", 24,  4,  4,  8, 16, 14,  0,  0); // Slovakia: mod 11
        define1("SI", 19,  4,  5,  9, 10,  1, 18,  2); // Slovenia: MOD 97-10
        define1("ES", 24,  4,  9, 13, 11, 15,  0,  0); // Spain: 4 bank, 4 branch, 1 chk, 1 chk, 10 acct
        define1("SE", 24,  4,  3,  7, 17, 16,  0,  0); // Sweden: mod 10 or mod 11
        define1("TN", 24,  4,  5,  9, 15,  0,  0,  0); // Tunesia: MOD 97-10, IBAN check is always 59
        define1("TR", 26,  4,  5,  9, 17,  0,  0,  0); // Turkey: 5 bank, 1 reserved, 16 acct
    } //

    /** Test frame, reads lines with IBANs and checks each.
     *  @param args commandline arguments:
     *  <ul>
     *  <li>args[0] = name of file containing single IBANs on a line</li>
     *  </ul>
     */
    public static void main (String args[]) {
        (new IBANChecker()).process(args);
    } // main

} // IBANChecker
/*
BG09 BPBI 7940 1046 3720 02
BG18 BPBI 7940 1446 3720 01
BG70 BPBI 7941 1076 4717 01
BG23 BPBI 7940 1041 3099 02
BG19 FINV 9150 10RV MAXT EL
HU86 1020 1006 5023 5417 0000 0000
HU35 1080 0014 8000 0006 1139 2567
HU11 1080 0014 0000 0006 1139 2583
HU39 1171 6008 2009 1080 0000 0000
HU05 1176 3196 0618 5882 0000 0000
HU53 1171 9001 2031 9719 0000 0000
AD12 0001 2030 2003 5910 0100
BE68 5390 0754 7034
BA39 1290 0794 0102 8494
BG80 BNBG 9661 1020 3456 78
CZ42 0100 0000 1955 0503 0267
DK50 0040 0440 1162 43
DE89 3704 0044 0532 0130 00
EE38 2200 2210 2014 5685
FO97 5432 0388 8999 44
FI21 1234 5600 0007 85
FR14 2004 1010 0505 0001 3M02 606
GI75 NWBK 0000 0000 7099 453
GR16 0110 1250 0000 0001 2300 695
GL56 0444 9876 5432 10
GB29 NWBK 6016 1331 9268 19
IE29 AIBK 9311 5212 3456 78
IS14 0159 2600 7654 5510 7303 39
IL62 0108 0000 0009 9999 999
IT60 X054 2811 1010 0000 0123 456
HR12 1001 0051 8630 0016 0
LV80 BANK 0000 4351 9500 1
LI21 0881 0000 2324 013A A
LT12 1000 0111 0100 1000
LU28 0019 4006 4475 0000
MT84 MALT 0110 0001 2345 MTLC AST0 01S
MU56 BOMM 0101 1234 5678 9101 0000 00
MK07 3000 0000 0042 425
MC93 2005 2222 1001 1223 3M44 555
ME25 5050 0001 2345 6789 51
NL91 ABNA 0417 1643 00
NO93 8601 1117 947
AT61 1904 3002 3457 3201
PL27 1140 2004 0000 3002 0135 5387
PT50 0002 0123 1234 5678 9015 4
RO49 AAAA 1B31 0075 9384 0000
SM62 Y054 3219 8760 0444 5333 222
SE35 5000 0000 0549 1000 0003
CH93 0076 2011 6238 5295 7
RS35 2600 0560 1001 6113 79
SK31 1200 0000 1987 4263 7541
SI56 1910 0000 0123 438
ES91 2100 0418 4502 0005 1332
CZ65 0800 0000 1920 0014 5399
TN59 1420 7207 1007 0712 9648
TR33 0006 1005 1978 6457 8413 26
HU42 1177 3016 1111 1018 0000 0000
CY17 0020 0128 0000 0012 0052 7600
EE23 1010 2200 2797 1018
EE10 2200 2210 1062 8840
EE35 2200 2210 0510 5695
LT02 7044 0600 0071 4188
LT02 7044 0600 0074 0851
LT09 7044 0600 0309 0841
LT20 4010 0425 0012 9626
LT12 7044 0600 0557 4763
LT30 7400 0110 2412 3814
LT59 7044 0600 0110 0762
LT32 7044 0600 0332 3712
LT35 7300 0100 0245 6727
LT11 7300 0100 8654 9951
LT42 7300 0100 9589 9029
LT61 7300 0100 9763 8673
LT65 7300 0100 0002 4300
LT65 4010 0412 0008 1175
LT76 7400 0189 2892 3810
LT77 7400 0235 9072 3810
LT81 7044 0600 0521 8113
LT83 7180 9000 0207 0860
LT92 7230 0000 0064 3313
LT94 4010 0495 0000 9134
LV09 TREL 2180 3200 5040 0
LV23 UNLA 0050 0096 5465 3
LV55 HABA 0001 4080 4686 9
LV56 HABA 0551 0067 4050 9
LV63 HABA 0551 0171 3781 3
LV68 NDEA 0000 0801 3830 3
LV69 HABA 0551 0129 2489 8
LV04 HABA 0551 0148 3260 3
LV05 UBAL 1900 1703 9500 1
LV30 UNLA 0050 0078 6778 7
LV30 UNLA 0050 0079 3549 3
RO21 EGNA 1010 0000 0004 5209
RO34 ABNA 1300 8531 0000 0499
RO38 BRDE 441S V444 6465 4410
RO55 RZBR 0000 0600 0899 5959
RO63 RNCB 0076 0294 2447 0001
RO69 RZBR 0000 0600 0336 7185
RO90 BTRL 0470 1202 F132 00XX
SA03 8000 0000 6080 1016 7519
SA56 6500 0000 2132 1321 2321
AL42 CDIS 0040 0990 05
AL12 CDIS 0040 0780 11
GB39 LOYD 3097 8100 8946 64
MK 07100701000006613
ZAJJFIRN27035162063032545
MU82MCBL0905000000071017000USD
CY42 0030 0179 0000 0179 3213 5999
LV70RTMB0000005164946
 MA 19 002 780005212280372200 45 ok ARAB MA MC XXX
GR43 0172 0130 0050 1301 4234 155
BG24UBBS80021407188914
SI56 0110 0600 0000 314
MK07210300000059423
MA022810000070000503087023
MA 0102 2121158744910007 83
MA 19 002 780005212280372200 45
MA007 780 0000000 282 00 25 91 032
MC11 1273 9000 7000 1111 1000 h79
*/
