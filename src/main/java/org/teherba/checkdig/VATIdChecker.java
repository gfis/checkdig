/*  VATIdChecker.java - check European Value Added Tax Identification Numbers
    @(#) $Id: VATIdChecker.java 91 2009-03-03 17:46:57Z gfis $
    Caution, must be stored as UTF-8: äöüÄÖÜß
	2009-01-09: result of 'check' is: new (formatted) number, space, [questionmark|exclamationmark]returnstring
	2008-12-01: success and error codes
    2008-11-14: test case convention
    2007-05-24: name was VatIdChecker
    2007-04-17: Poland and Slovenia
    2007-04-10: more Javadoc, more TESTCASES
    2005-08-30: recoded
    2004-12-21: more 'int prod's for similiar structures
    2003-08-19, Georg Fischer: copied from iso7064.java
    reads lines with VAT ids
    checks them according to the country-specific algorithms
    
    Activation (test data in the source program):
        java -cp dist/checkdig.jar org.teherba.checkdig.VATIdChecker [infile]

    VAT IDs can be checked online at:
    http://ec.europa.eu/taxation_customs/vies/lang.do?fromWhichPage=vieshome&selectedLanguage=DE
    
    The structure in various countries is described at:
    http://ec.europa.eu/taxation_customs/vies/faqvies.do?selectedLanguage=EN#item11

        See also:
        http://ec.europa.eu/taxation_customs/vies/faqvies.do?selectedLanguage=EN
        http://sima-pc.com/nif.php
        http://chemeng.p.lodz.pl/zylla/ut/translation.html
        
        AT-Austria      ATU999999991    1 block of 9 characters
        BE-Belgium      BE999999999 or
                        BE09999999992   1 block of 9 digits or
                                        1 block of 10 digits 3
        BG-Bulgaria     BG999999999 or
                        BG9999999999    1 block of 9 digits or 1 block of 10 digits
        CY-Cyprus       CY99999999L     1 block of 9 characters
        CZ-Czech 
           Republic     CZ99999999 or
                        CZ999999999 or
                        CZ9999999999
                                        1 block of either 8, 9 or 10 digits
        DE-Germany      DE999999999     1 block of 9 digits
        DK-Denmark      DK99 99 99 99   4 blocks of 2 digits
        EE-Estonia      EE999999999     1 block of 9 digits
        EL-Greece       EL999999999     1 block of 9 digits
        ES-Spain        ESX9999999X     1 block of 9 characters
        FI-Finland      FI99999999      1 block of 8 digits
        FR-France       FRXX 999999999  1 block of 2 characters, 1 block of 9 digits
        GB-United 
           Kingdom      GB999 9999 99 or
                        GB999 9999 99 9995 or
                        GBGD9996 or
                        GBHA9997        1 block of 3 digits, 1 block of 4 digits and 1 block of 2 digits; 
                                            or the above followed by a block of 3 digits; 
                                            or 1 block of 5 characters
        HU-Hungary      HU99999999      1 block of 8 digits
        IE-Ireland      IE9S99999L      1 block of 8 characters
        IT-Italy        IT99999999999   1 block of 11 digits
        LT-Lithuania    LT999999999 or
                        LT999999999999  1 block of 9 digits, or 1 block of 12 digits
        LU-Luxembourg   LU99999999      1 block of 8 digits
        LV-Latvia       LV99999999999   1 block of 11 digits
        MT-Malta        MT99999999      1 block of 8 digits
        NL-The 
        Netherlands     NL999999999B99  1 block of 12 characters
        PL-Poland       PL9999999999    1 block of 10 digits
        PT-Portugal     PT999999999     1 block of 9 digits
        RO-Romania      RO999999999     1 block of minimum 2 digits and maximum 10 digits
        SE-Sweden       SE999999999999  1 block of 12 digits
        SI-Slovenia     SI99999999      1 block of 8 digits
        SK-Slovakia     SK9999999999    1 block of 10 digits
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
package org.teherba.checkdig;
import  org.teherba.checkdig.BaseChecker;
import  java.io.BufferedReader;
import  java.io.FileReader;
import  java.lang.reflect.Method;
import  java.util.ArrayList;

/** Class for the checkdigits in European 
 *  Value Added Tax Identification (VAT-Id) numbers.
 *  For example, the German VAT Id of punctum GmbH is DE129443785.
 *  For each country, a special method is implemented which returns the correct
 *  string of check digits only. The check response is built in {@link #check}.
 *  Several test numbers are found below.
 *  @author Dr. Georg Fischer
 */
public class VATIdChecker extends BaseChecker {
    public final static String CVSID = "@(#) $Id: VATIdChecker.java 91 2009-03-03 17:46:57Z gfis $";
    
    /** No-args constructor
     */
    public VATIdChecker() {
        super();
    } // constructor
    
     /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
	public String[] getTestCases() {
		return new String[] 
            { "ATU13585627"
            , "ATU83256904"
            , "BE123487631"
            , "DE187355628"
            , "DE129443785"
            , "DE814371180"
            , "DE235317221"
            , "DE234380421"
            , "DK13585628"
            , "DK10572649"
            , "EL094359842"
            , "EL123456783"
            , "ESA48765309"
            , "FI14839261"
            , "FR12372654988"
            , "GB357468123"
            , "IE4782521F"
            , "IE8473625E"
            , "IT00156870123"
            , "LU11523165"
            , "LU11868245"
            , "LU11917605"
            , "NL025836729B01"
            , "PL8567346215"
            , "PT195437250"
            , "PT508931746"
            , "SE679342158401"
            , "SI59082437"
            , "WRONG_VATID"
            , "GR094359842"
            , "EL12345678"
            };
	} // getTestCases

    /** Lists the ISO codes for all countries which have methods implemented;
     *  this list MUST correspond with the method names below, because the
     *	latter are "reflected".
     */
    private static String ISO_LIST 
            = ",AT,BE,BG,CY,CZ,DE,DK,EE,EL,ES,FI,FR,GB,HU,IE,IT,LT,LU,LV,MT,NL,PL,PT,RO,SE,SI,SK,";

    /** Checks the VAT Id numbers used in 
     *  Austria.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkAT(String vatId) { 
        int nlen = 8; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6   7 
        int factor[] = {  0,  1,  2,  1,  2,  1,  2,  1}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 1; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod % 10 + (int) prod / 10; 
        } // for ipos
        sum = 10 - (sum + 4) % 10;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkAT

    /** Checks the VAT Id numbers used in 
     *  Belgium.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkBE(String vatId) { 
        int nlen = 7; // number of digits to be checked
        setCheckRange(2 + nlen, 2);
        long number = 0;
		int result = 0;
        try {
            number = Long.parseLong(vatId.substring(0, nlen));
        	result = (int) (97 - (number % 97));
        } catch (Exception exc) {
        	return "?";
        }
        return (result < 10 ? "0" : "") + String.valueOf(result);
    } // checkBE

    /** Checks the VAT Id numbers used in 
     *  Bulgaria.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkBG(String vatId) { 
        //      pos       0   1   2   3   4   5   6   7   8
        int factor1[] = { 4,  3,  2,  7,  6,  5,  4,  3,  2};
        int sum = 0;
        String result = "?"; // resulting check digit
        int nlen = vatId.length() - 1; // number of digits to be checked
        int fpos = 0;
        for (int ipos = nlen - 9; ipos < nlen; ipos ++) { // fill to length 9 with leading zeroes
            int digit = (ipos < 0) ? 0 : Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor1[fpos ++];
            sum += prod;
        } // for ipos
        sum = 11 - sum % 11;
        if (sum == 11) {
            sum = 0;
        }
        if (sum != 10) {
            result = String.valueOf(sum);
        }
        return result;
    } // checkBG

    /** Checks the VAT Id numbers used in 
     *  Cyprus.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkCY(String vatId) { 
        int nlen = 8; // number of digits to be checked
        //    digit       0   1   2   3   4   5   6   7   8   9
        int factor[] =   {1,  0,  5,  7,  9,  13, 15, 17, 19, 21};
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = (ipos % 2 != 0) ? digit : factor[digit];
            sum += prod;
        } // for ipos
        sum = sum % 26 + 10;
        return BaseChecker.LETTER_MAP.substring(sum, sum + 1); // +10 skipped over digits
    } // checkCY

    /** Checks the VAT Id numbers used in 
     *  Czech Republic.
	 * <pre>
        CZ, SK: VAT Number/DIČ
        S=8*N(1)+7*N(2)+6*N(3)+5*N(4)+4*N(5)+3*N(6)+2*N(7)
        C(1)=11-S%11; if C(1)=10, C(1)=0; if C(1)=11, C(1)=1
        Special cases   N(1-8)+C(1)
        N(1)={6}        612345670
        S=8*N(2)+7*N(3)+6*N(4)+5*N(5)+4*N(6)+3*N(7)+2*N(8)
        C(1)=9-(11-S%11)%10
	 * </pre>
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkCZ(String vatId) { 
        //      pos        0   1   2   3   4   5   6  
        int factor1[] = {  8,  7,  6,  5,  4,  3,  2 }; 
        int sum = 0;
        String result = "?";
        int nlen = vatId.length() - 1; // number of digits to be checked
        if (false) {
        } else if (nlen == 7) {
            for (int ipos = nlen - 1; ipos >= 0; ipos --) {
                int digit = Character.digit(vatId.charAt(ipos), 10);
                int prod = digit * factor1[ipos];
                sum += prod;
            } // for ipos
            sum = 11 - sum % 11;
            if (sum == 10) {
                sum = 0;
            } else if (sum == 11) {
                sum = 1;
            }
            result = String.valueOf(sum);
        } else if (nlen == 8 && vatId.charAt(0) == '6') {
            for (int ipos = nlen - 1; ipos >  0; ipos --) {
                int digit = Character.digit(vatId.charAt(ipos), 10);
                int prod = digit * factor1[ipos - 1];
                sum += prod;
            } // for ipos
            sum = 9 - (11 - sum % 11) % 10;
            result = String.valueOf(sum);
        } else if (nlen == 9) {
            result = String.valueOf(sum);
        } else if (nlen == 10) {
            sum = 9 - (11 - sum % 11) % 10;
            result = String.valueOf(sum);
        } 
        // else cdig = '?'
        return result;
    } // checkCZ

    /** Checks the VAT Id numbers used in 
     *  Germany.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkDE(String vatId) { // Germany
        int nlen = 8; // number of digits to be checked
        int rest = 10;
        int sum = 0;
        for (int ipos = 0; ipos < nlen; ipos ++) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            sum = (rest + digit) % 10;
            if (sum == 0) {
                sum = 10; // 1 < 10
            }
            rest = (sum * 2) % 11;
        } // for ipos
        sum = 11 - rest; // 10 > 1
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // check

    /** Checks the VAT Id numbers used in 
     *  Denmark.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkDK(String vatId) { // Denmark
        int nlen = 7; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6
        int factor[] = {  2,  7,  6,  5,  4,  3,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = 11 - sum % 11; // values 11, 10, ... 1
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkDK

    /** Checks the VAT Id numbers used in 
     *  Estonia.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkEE(String vatId) { 
        int nlen = 8; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6   7
        int factor[] = {  3,  7,  1,  3,  7,  1,  3,  7}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = 10 - sum % 10;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkEE

    /** Checks the VAT Id numbers used in 
     *  Greece.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkEL(String vatId) { // Greece
        int nlen =  8; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6   7 
        int factor[] = {256,128, 64, 32, 16,  8,  4,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = sum % 11;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkEL

    /** Checks the VAT Id numbers used in 
     *  Greece, but with ISO 3160 country code "GR".
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkGR(String vatId) { // Greece
        return checkEL(vatId);
    } // checkGR

    /** Checks the VAT Id numbers used in 
     *  Spain.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkES(String vatId) { // France
        int nlen = 8; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6   7 
        int factor[] = {  0,  2,  1,  2,  1,  2,  1,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 1; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod % 10 + (int) prod / 10; 
        } // for ipos
        sum = 10 - sum % 10;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkES

    /** Checks the VAT Id numbers used in 
     *  Finland.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkFI(String vatId) { // Finland
        int nlen = 7; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6
        int factor[] = {  7,  9, 10,  5,  8,  4,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = 11 - sum % 11;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkFI

    /** Checks the VAT Id numbers used in 
     *  France.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkFR(String vatId) { // France
        int nlen = 10; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6   7   8   9 
        int factor[] = {  0,  0,  1,  2,  1,  2,  1,  2,  1,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 2; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod % 10 + (int) prod / 10; 
        } // for ipos
        sum = 10 - sum % 10;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkFR

    /** Checks the VAT Id numbers used in 
     *  Great Britain (United Kingdom).
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkGB(String vatId) { // Great Britain
        int nlen = 7; // number of digits to be checked
		setCheckRange(2 + nlen, 2);
        //      pos       0   1   2   3   4   5   6 
        int factor[] = {  8,  7,  6,  5,  4,  3,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = sum % 97;
        if (sum != 0) {
            sum = 97 - sum;
        }
        int result = sum;
        return (result < 10 ? "0" : "") + String.valueOf(result);
     } // checkGB

    /** Checks the VAT Id numbers used in 
     *  Hungary.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkHU(String vatId) { 
        int nlen = 7; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6 
        int factor[] = {  9,  7,  3,  1,  9,  7,  3}; 
        int sum = 0;
        // vatId.charAt(0) > '0'
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = 10 - sum % 10;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkHU

    /** Checks the VAT Id numbers used in 
     *  Ireland.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkIE(String vatId) { // Ireland
        int nlen = 7; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6 
        int factor[] = {  8,  7,  6,  5,  4,  3,  2}; 
        //                     01234567890123456789012
        String checkLetters = "WABCDEFGHIJKLMNOPQRSTUV";
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = sum % 23;
        return checkLetters.substring(sum, sum + 1);
    } // checkIE

    /** Checks the VAT Id numbers used in 
     *  Italy.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkIT(String vatId) { // Ireland
        int nlen = 10; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6   7   8   9 
        int factor[] = {  1,  2,  1,  2,  1,  2,  1,  2,  1,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod % 10 + (int) prod / 10; 
        } // for ipos
        sum = 10 - sum % 10;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkIT

    /** Checks the VAT Id numbers used in 
     *  Lithuania.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkLT(String vatId) { 
        //      pos        0   1   2   3   4   5   6   7   8   9  10
        int factor1[] = {  1,  2,  3,  4,  5,  6,  7,  8,  9,  1,  2}; 
        int factor2[] = {  3,  4,  5,  6,  7,  8,  9,  1,  2,  3,  4}; 
        int sum = 0;
        String result = "?";
        int nlen = vatId.length() - 1; // number of digits to be checked
        if (nlen == 8 || nlen == 11) {
            for (int ipos = nlen - 1; ipos >= 0; ipos --) {
                int digit = (ipos == nlen - 1) ? 1 : Character.digit(vatId.charAt(ipos), 10);
                int prod = digit * factor1[ipos];
                sum += prod;
            } // for ipos
            if (sum % 11 == 10) {
                sum = 0;
                for (int ipos = nlen - 1; ipos >= 0; ipos --) {
                    int digit = (ipos == nlen - 1) ? 1 : Character.digit(vatId.charAt(ipos), 10);
                    int prod = digit * factor2[ipos];
                    sum += prod;
                } // for ipos
            }
            sum = sum % 11;
            if (sum == 10) {
                sum = 0;
            }
            result = String.valueOf(sum);
        } // nlen == 8 or 11
        // else cdig = '?'
        return result;
    } // checkLT

    /** Checks the VAT Id numbers used in 
     *  Luxemburg.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkLU(String vatId) { // Luxemburg
        int nlen = 6; // number of digits to be checked
        setCheckRange(2 + nlen, 2);
        long number = 0;
        try {
            number = Long.parseLong(vatId.substring(0, nlen));
        } catch (Exception exc) {
        	return "?";
        }
        int mod = 89;
        int result = (int) (number % mod);
        return (result < 10 ? "0" : "") + String.valueOf(result);
    } // checkLU

    /** Checks the VAT Id numbers used in 
     *  Latvia.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkLV(String vatId) { 
        //      pos        0   1   2   3   4   5   6   7   8   9  10
        int factor1[] = {  9,  1,  4,  8,  3, 10,  2,  5,  7,  6}; 
        int sum = 0;
        String result = "?";
        int nlen = vatId.length() - 1; // number of digits to be checked
        if (nlen == 10) { 
            char ch0 = vatId.charAt(0);
            if (ch0 >= '4') { // legal persons
                for (int ipos = nlen - 1; ipos >= 0; ipos --) {
                    int digit = Character.digit(vatId.charAt(ipos), 10);
                    int prod = digit * factor1[ipos];
                    sum += prod;
                } // for ipos
                int rem11 = sum % 11;
                if (false) {
                } else if (rem11 == 4) {
                    if (ch0 == '9') {
                        sum -= 45;
                        rem11 = sum % 11;
                    }
                    result = String.valueOf( 4 - rem11);
                } else if (rem11 >  4) {
                    result = String.valueOf(14 - rem11);
                } else if (rem11 <  4) {
                    result = String.valueOf( 3 - rem11);
                }
                    // if S%11=4 and N(1)=9, S=S-45
                    // if S%11=4, C(1)=4-S%11
                    // if S%11>4, C(1)=14-S%11
                    // if S%11<4, C(1)=3-S%11                       
            } else { // natural persons: check birth date ddMMyynnnnn
                if (false) {
                } else {
                    result = vatId.substring(nlen, nlen + 1); // always ok
                }
            } // natural
        } // nlen == 11
        // else cdig = '?'
        return result;
    } // checkLV

    /** Checks the VAT Id numbers used in 
     *  Malta.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkMT(String vatId) { // Malta
        int nlen = 6; // number of digits to be checked
        setCheckRange(2 + nlen, 2);
        //      pos       0   1   2   3   4   5   6
        int factor[] = {  3,  4,  6,  7,  8,  9}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = 37 - sum % 37;
        int result = sum;
        return (result < 10 ? "0" : "") + String.valueOf(result);
    } // checkMT

    /** Checks the VAT Id numbers used in 
     *  the Netherlands (Omzetbelastingnummer, OB nummer)   
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkNL(String vatId) { // Nederlands
        int nlen = 8; // number of digits to be checked
        setCheckRange(2 + nlen, 1);
        //      pos       0   1   2   3   4   5   6   7
        int factor[] = {  9,  8,  7,  6,  5,  4,  3,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = sum % 11;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkNL

    /** Checks the VAT Id numbers used in 
     *  Poland.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkPL(String vatId) { // Portugal
        int nlen = 9; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6   7   8
        int factor[] = {  6,  5,  7,  2,  3,  4,  5,  6,  7}; 
    //  int factor[] = {  7,  6,  5,  4,  3,  2,  7,  5,  6}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = sum % 11;
        int result = (sum >= 10) ? 0 : sum;
        return (sum == 11 ? "?" : String.valueOf(result));
    } // checkPL

    /** Checks the VAT Id numbers used in 
     *  Portugal.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkPT(String vatId) { // Portugal
        int nlen = 8; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6   7  
        int factor[] = {  9,  8,  7,  6,  5,  4,  3,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = 11 - sum % 11;
        int result = (sum >= 10) ? 0 : sum;
        return String.valueOf(result);
    } // checkPT

    /** Checks the VAT Id numbers used in 
     *  Romania.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkRO(String vatId) { 
        //      pos        0   1   2   3   4   5   6   7   8   9  10  11
        int factor1[] = {  7,  5,  3,  2,  1,  7,  5,  3,  2}; // 7,5,3,2,1,7,5,3,2
        int factor2[] = {  2,  7,  9,  1,  4,  6,  3,  5,  8,  2,  7,  9}; // 2,7,9,1,4,6,3,5,8,2,7,9)
        int sum = 0;
        String result = "?";
        int nlen = vatId.length() - 1; // number of digits to be checked
        if (false) {
        } else if (nlen <= 9) {
            int fpos = 0;
            for (int ipos = nlen - 9; ipos < nlen; ipos ++) { // fill to length 9 with leading zeroes
                int digit = (ipos < 0) ? 0 : Character.digit(vatId.charAt(ipos), 10);
                int prod = digit * factor1[fpos ++];
                sum += prod;
            } // for ipos
            sum = (sum * 10) % 11;
            if (sum == 10) {
                sum = 0;
            }
            result = String.valueOf(sum);
        } else { // natural persons: XyyMMddnnnnn, X=1,2,3,4,6
            for (int ipos = nlen - 1; ipos >= 0; ipos --) {
                int digit = Character.digit(vatId.charAt(ipos), 10);
                int prod = digit * factor2[ipos];
                sum += prod;
            } // for ipos
            sum = sum % 11;
            if (sum == 10) {
                sum = 1;
            }
            result = String.valueOf(sum);
        } 
        return result;
    } // checkRO

    /** Checks the VAT Id numbers used in 
     *  Sweden.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
    public String checkSE(String vatId) { // Sverige
        int nlen = 9; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6   7   8 
        int factor[] = {  2,  1,  2,  1,  2,  1,  2,  1,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod % 10 + (int) prod / 10; 
        } // for ipos
        sum = 10 - sum % 10;
        int result = (sum >= 10) ? 0 : sum;
        setCheckRange(2 + nlen, 1);
        return String.valueOf(result);
        //    + vatId.substring(10,12);
    } // checkSE

    /** Checks the VAT Id numbers used in 
     *  Slovenia.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
        // S=8*N(3)+7*N(4)+6*N(5)+5*N(6)+4*N(7)+3*N(8)+2*N(9)
        // C(1)=11-S%11; if C(1)=10, C(1)=0; if C(1)=11, C(1)=1
    public String checkSI(String vatId) { 
        int nlen = 7; // number of digits to be checked
        //      pos       0   1   2   3   4   5   6 
        int factor[] = {  8,  7,  6,  5,  4,  3,  2}; 
        int sum = 0;
        for (int ipos = nlen - 1; ipos >= 0; ipos --) {
            int digit = Character.digit(vatId.charAt(ipos), 10);
            int prod = digit * factor[ipos];
            sum += prod;
        } // for ipos
        sum = 11 - sum % 11;
        int result = (sum >= 10) ? 0 : sum;
        return (sum == 11 ? "?" : String.valueOf(result));
    } // checkSI

    /** Checks the VAT Id numbers used in 
     *  Slovakia.
     *  @param vatId number to be checked
     *  @return new VAT Id number, eventually with correct check digit
     */ 
        /*
        SK: VAT Number/DIČ
        S=8*N(3)+7*N(4)+6*N(5)+5*N(6)+4*N(7)+3*N(8)+2*N(9)
        C(1)=11-S%11; if C(1)=10, C(1)=0; if C(1)=11, C(1)=1    
        */      
    public String checkSK(String vatId) { 
        //      pos        0   1   2   3   4   5   6  
        int factor1[] = {  8,  7,  6,  5,  4,  3,  2 }; 
        int sum = 0;
        String result = "?";
        int nlen = vatId.length() - 1; // number of digits to be checked
        if (false) {
        } else if (nlen == 9) {
            int fpos = 0;
            for (int ipos = 2; ipos < nlen; ipos ++) {
                int digit = Character.digit(vatId.charAt(ipos), 10);
                int prod = digit * factor1[fpos ++];
                sum += prod;
            } // for ipos
            sum = 11 - sum % 11;
            if (sum == 10) {
                sum = 0;
            } else if (sum == 11) {
                sum = 1;
            }
            result = String.valueOf(sum);
        } else if (nlen == 9) {
            result = String.valueOf(sum);
        } else if (nlen == 10) {
            sum = 9 - (11 - sum % 11) % 10;
            result = String.valueOf(sum);
        } 
        // else result = "?"
        return result;
    } // checkSK

    //====================================================================
    /** Computes the check digits of a VAT-Id
     *  @param rawNumber VAT-Id to be tested
     *  @return return code, may be with recomputed VAT-Id with proper check digits
     */
    public String check(String rawNumber) { // calling by reflection
        String result = null;
        String vatId = trim(rawNumber);
    	int nlen = vatId.length();
	    setCheckRange(nlen - 1, 1);
        if (nlen >= 8) {
            try {
                String isoCode = vatId.substring(0, 2);
                if (ISO_LIST.indexOf(isoCode) >= 0) {
                    String digits = vatId.substring(2);
                    // call the country-specific check routine by reflection
                    Method method = this.getClass().getMethod("check" + isoCode, new Class[] { digits.getClass() } );
                    String newCheck = (String) method.invoke(this, new Object[] { digits }); // returns the computed checksum
		        	result = checkResponse(rawNumber, format(vatId), getRangeStart(), getRangeLength(), newCheck);
                } else {
                    result = checkResponse(rawNumber, BaseChecker.WRONG_COUNTRY);
                }
            } catch (Exception exc) { // method not found - should be shielded by ISO_LIST
                result = checkResponse(rawNumber, BaseChecker.SYSTEM_ERROR);
            }
        } else {
            result = checkResponse(rawNumber, BaseChecker.TOO_SHORT);
        }
        return result;
    } // check

    /** Test Frame, reads lines with VAT id numbers and checks each.
     *  @param args commandline arguments:
     *  <ul>
     *  <li>args[0] = name of file containing single VAT Ids on a line</li>
     *  </ul>
     */     
    public static void main (String args[]) { 
    	(new VATIdChecker()).process(args);
    } // main

} // VATIdChecker
/* 
ATU13585627
ATU15249103
ATU83256904

BE0859589551
BE123487631
BE136695962
BE420383547
BE428932515
BE454419759

BG103952006
BG104695590
BG105582979
BG113570147
BG121152197
BG121186623
BG121261531
BG121553998
BG1234567892
BG124634156
BG130535465
BG175203478
BG816115948

CY10016154J
CY10072033X
CY10083474P
CY10090285L
CY10111667D
CY10122030O
CY10151376F
CY10152925D
CY10158093W
CY10170173D
CY12125450I
CY12345678F
CY30010823A
CY90000169W

CZ12345679
CZ612345670
CZ991231123
CZ6306150004
CZ00001481
CZ00001490
CZ00006947
CZ00023205
CZ00576425
CZ15270076
CZ15503020
CZ17046009
CZ25251759 
CZ25358839
CZ25401092
CZ25435400
CZ25992333
CZ26107295
CZ26119684
CZ26353008
CZ26424053
CZ26441381
CZ26728796
CZ27041867
CZ27123979
CZ27253937
CZ27471489
CZ27697665
CZ45273227 
CZ46963642
CZ46966170
CZ47671386 
CZ47717076
CZ48909173
CZ501225107
CZ5502041325
CZ5703142225
CZ60193336
CZ60793414
CZ61064025
CZ6303170522 
CZ6311081524
CZ63979632
CZ63980541
CZ63994828
CZ64938921
CZ65408781
CZ67985556
CZ68407700
CZ7109111768
CZ7610111938
CZ8607063355

DE129443785
DE136695976
DE187355628
DE811207273

DK10572649
DK13585628 

EE100013434
EE100069996
EE100087033
EE100099623
EE100099908
EE100100240
EE100119299
EE100121397
EE100164723
EE100169582
EE100174946
EE100189588
EE100202546
EE100206393
EE100240964
EE100252101
EE100288418
EE100300008
EE100301955
EE100311071
EE100311796
EE100346253
EE100371082
EE100434330
EE100480795
EE100522365
EE100524949
EE100526730
EE100569218
EE100587643
EE100601943
EE100622401
EE100639122
EE100647884
EE100662209
EE100672736
EE100686366
EE100698859
EE100701610
EE100710559
EE100731169
EE100755187
EE100785270
EE100810352
EE100830073
EE100850749
EE100857124
EE100867936
EE100902286
EE100903599
EE100919800
EE100923029
EE100926291
EE100936689
EE100938933
EE100949838
EE100979002
EE100984505
EE100986121
EE100998397
EE101009706
EE101038078
EE101043173
EE101052261
EE101089674
EE101090058
EE101103233

EL094359842
EL123456783

ESA13585625
ESA48765309

FI13669598
FI14839261

FR06304827736
FR12372654988

GB243571174
GB243805469
GB357468123
GB365462636
GB449310649
GB505821955
GB539073336
GB629388888
GB638530726
GB739917090
GB757260223
GB766030930
GB823847609
GB916239716

HU10339307
HU10378735
HU10657517
HU10670192
HU10683132
HU10755309
HU10945005
HU11181475
HU11809205
HU11919146
HU12016710
HU12058354
HU12231418
HU12568585
HU12733965
HU12782419
HU13139326
HU13144313
HU13500782
HU13751074
HU13804787
HU15329499
HU20773997
HU21944895
HU21972261
HU28187394
HU52639762

IE4782521F
IE8473625E

IT00156870123
IT01406430155
IT07341170152
IT12123450780

LT105498716
LT106814811
LT108917219
LT114495414
LT116235314
LT117601113
LT119541113
LT119633113
LT203757414
LT219244515
LT223513811
LT226012314
LT233866610
LT233901716
LT261618515
LT262422811
LT263347219
LT300590688
LT332175419
LT346277716
LT348394515
LT349163917
LT352187515
LT352187515
LT355715811
LT359796515
LT402775716
LT406560515
LT406560515
LT474520610
LT475796113
LT479973917
LT482614917
LT667637113
LT813513419
LT100000002211
LT100000882019
LT100000979812
LT100000983518
LT100001072712
LT100001097617
LT100001150516
LT100001172012
LT100001247212
LT100001481313
LT100001985913
LT100002055516
LT100002177412
LT100002177412
LT100002240117
LT100002404910
LT100002563014
LT100002571812
LT100002576510
LT100002614910
LT100002615717
LT100002874816
LT100002893812
LT123456789011

LU11523165
LU11868245
LU11917605
LU13669580

LV40003076407
LV40003167725
LV40003241394
LV40003251246
LV40003593897
LV40003933143
LV40008091188
LV40103148133
LV90001287943
LV40002078769
LV40003336515
LV40003390850
LV40003662879
LV40003668833
LV40003727925
LV42803008787
LV42803008787
LV50003056031
LV50003694471
LV40003483012
LV40003072918
LV40003242169
LV40003737463
LV40003810012
LV40003871680
LV15066312345

MT11189317
MT12345634
MT14507936
MT15900836
MT17157330
MT17207012
MT17510006 
MT17673409
MT17707511
MT18154313

NL025836729B01
NL123456782B12

PL8567349219
PL9511287906 

PT136695973
PT195437250
PT508931746

RO1234567897
RO13529500
RO15052199
RO15957066
RO17499739
RO6276774       
RO6519490 
RO15957066
RO3553943
RO11723763
RO15957066
RO1879871       
RO20624890
RO21432303
RO1630615123457

SE136695975523
SE679342158401

SK0012345675
SK0012345678
SK531231123
SK6306151234
SK1020207749
SK2020052837
SK2020237857
SK2020446054
SK2021546879
SK2021947081 
SK2022042968
SK2022119110
SK2022119572
SK2022124225
SK2022215217 
SK2022237833
WRONGVATID
*/
