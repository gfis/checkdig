<?xml version="1.0" encoding="US-ASCII"?>
<!--
    Stylesheet for the Java code of account checker frame,
    and special German bank account check methods
    @(#) $Id: account.xsl 78 2009-02-05 17:11:47Z gfis $
    2017-05-29: javadoc 1.8
    2014-01-20: LF and no tabs
    2009-01-15: call checkResponse and highlight check digits in HTML
    2008-11-14: test cases convention
    2007-04-26: with timestamp
    2007-04-10: more TESTNUMBERS
    2007-02-02: package org.teherba.checkdig
    2005-10-17: substyle for checkdig.xsl
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
        xmlns:date="http://exslt.org/dates-and-times"
        xmlns:func="http://exslt.org/functions"
        extension-element-prefixes="func date"
        >

<xsl:template name="header">
    <xsl:value-of select="concat('/*  Class with an array of about 120 methods for checking of German bank account numbers.', '&#10;')" />
    <xsl:value-of select="concat(' *  @(#)', '$', 'Id$', '&#10;')" />
    <xsl:value-of select="concat(' *  Automatically generated by checkdig.xsl + account.xsl from de_account.xml', '&#10;')" />
    <xsl:value-of select="concat(' *  at ', date:date-time(), ' - DO NOT EDIT HERE!', '')" />
<xsl:text>
 */
/*
 * Copyright 2006 Dr. Georg Fischer &lt;punctum at punctum dot kom&gt;
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
import  org.teherba.checkdig.account.BlzCheckMap;
import  java.io.BufferedReader;
import  java.io.FileReader;
import  java.lang.reflect.Method;
import  java.lang.StringBuffer;

/** Array of generated methods for checking of German bank account numbers,
 *  together with a few hand-crafted methods.
 */
public class DeAccountChecker extends BaseChecker {
    public final static String CVSID = "@(#) $Id: account.xsl 78 2009-02-05 17:11:47Z gfis $";

    /** length of the account number */
    private static final int LEN_ACCOUNT = 10;

    /** numerical value of the account number */
    private long accountValue;

    /** accumulator for computations */
    private long sum;

    /** Bankleitzahl for methods 52, 53 */
    private String blz;

    /** for the determination of the check method from a BLZ */
    private static BlzCheckMap checkMap = new BlzCheckMap();

    /** No-args constructor
     */
    public DeAccountChecker() {
        super();
        digit = new int[LEN_ACCOUNT + 8]; // +1 because [0] is not used; 2 more for methods 52, 53; 6 more for method D1
        accountValue = 0;
    } // Constructor

    /** Sets the BLZ for methods 52, 53
     *  @param blz BLZ (8 digits) to be set
     */
    private void setBLZ(String blz) {
        this.blz = blz;
    } // setBLZ

    /** Gets the locally stored Bankleitzahl
     *  @return 8 digits as String
     */
    private String getBLZ() {
        return  blz;
    } // getBLZ

    /** Sets the positions of the check digit range in the number to
     *  be checked; special version with constant length 1
     *  @param offset offset counted backwards from the end+1 of the check substring:
     *  1 = last (rightmost) digit, or 0 = error
     */
    private void setCheckRange(int offset) {
        if (offset == 0) {
            setCheckRange(0, 0);
        } else {
            setCheckRange(LEN_ACCOUNT - offset, 1); // = super.setCheckRange
        }
    } // setCheckRange

    /** Checks an account number by using some published method.
     *  @param account_blz 10 digits for German account number,
     *  left filled with zeroes, followed by whitespace and
     *  a German BLZ = bank identification number;
     *  (determine the check method from this number)
     *  @return correct(ed) account number;
     *  the check digit is 'X' if it cannot be computed
     */
    public String check(String account_blz) {
        String [] elements = account_blz.split("\\s+");
        String result = check(checkMap.getMethod(elements[1]), elements[0], elements[1]);
        return result + "\t" + elements[1];
    } // check

    /** Checks an account number by using some published method.
     *  @param account 10 digits for German account number,
     *  left filled with zeroes
     *  @param blz German BLZ = bank identification number;
     *  determine the check method from this number
     *  @return correct(ed) account number;
     *  the check digit is 'X' if it cannot be computed
     */
    public String check(String account, String blz) {
        return check(checkMap.getMethod(blz), account, blz);
    } // check

    /** Checks an account number by using some published method.
     *  @param method method to be used for checking, "00".."B8"
     *  @param rawNumber 10 digits for German account number,
     *  left filled with zeroes
     *  @param blz German BLZ = bank identification number
     *  (needed only for methods 52, 53, maybe "" otherwise)
     *  @return original number, return code and correct(ed) account number;
     *  the check digit is 'X' if it cannot be computed
     */
    public String check(String method, String rawNumber, String blz) {
        String result = "";
        String account = trim(rawNumber);
        if (account.length() &gt; LEN_ACCOUNT) { // fatal error
            result = checkResponse(rawNumber, BaseChecker.TOO_LONG);
        } else {
            if (account.length() &lt; LEN_ACCOUNT) { // pad left
                account = ("000000000000000000000").substring(0, LEN_ACCOUNT - account.length()) + account;
            }
            for (int ipos = 1; ipos &lt;= LEN_ACCOUNT; ipos ++) { // split into digits array
                digit[ipos] = Character.digit(account.charAt(LEN_ACCOUNT - ipos), 10);
            } // for ipos
            this.setCheckRange(1); // default: check digit at the end
            this.setBLZ(blz);
            try { // now the reflected call
                this.accountValue = Long.parseLong(account);
                Method generatedMethod = this.getClass().getMethod("method_" + method, new Class[] { account.getClass() } );
                result = (String) generatedMethod.invoke(this, new Object[] { account });
                String newCheck = getCheckString(result);
                result = checkResponse(rawNumber, format(account), getRangeStart(), getRangeLength(), newCheck);
            } catch (NumberFormatException exc) {
                result = checkResponse(rawNumber, BaseChecker.WRONG_CHAR);
            } catch (Exception exc) {
                result = checkResponse(rawNumber, BaseChecker.SYSTEM_ERROR);
            } // try
        } // not too long
        return result;
        // (rawNumber.equals(result)) ? account : result;
    } // check

    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
    public String[] getTestCases() {
        return new String[]
            { "0123220600 50010060"   // from punctum/voba*.lst
            , "0166214666 59010066"
            , "1890182794 70020270"
            , "0020392207 68291200"
            , "0022019550 68050101"
            , "0002314509 67090000"
            , "0002832049 60050101"
            , "0031345753 66010075"
            , "0394060704 60010070"
            , "0000052603 50010060"
            , "0575818012 50040000"   // Commerzbank
            , "0494606612 20080000"   // Dresdner Bank, www.telexroll.de
            , "0068001507 68000000"
            , "7402045790 60050101"
            , "7805260    30050000"
            , "85995      66050000"
            , "97000018   68092000"
            , "WRONGACCTS 00000000"
            , "2062454    68052025"   // wrong, Sparkasse Noerdl. Breisgau
            , "63251      76350000"   // wrong, GI
            , "0575818112 50040000"   // Commerzbank, c.f. above
            , "0494606712 20080000"   // Dresdner Bank, c.f. above
            };
    } // getTestCases

    //-------------------------------------------------------------------------
    /** Special method 24
     *  @param account account number to be tested
     *  @return corrected number
     */
    public String method_24_B(String account) {
        StringBuffer result = new StringBuffer(account);
        try {
            int weights[] = {1,2,3,1,2,3,1,2,3};
            sum = 0l;
            int iweight = 0; // index into weights
            int prod = 0;
            int ipos = 10;
            switch (digit[ipos]) {
                case 3:
                case 4:
                case 5:
                case 6:
                    ipos = 9;
                    break;
                case 9:
                    ipos = 7;
                    break;
                default:
                    break;
            } // switch
            while (digit[ipos] == 0 &amp;&amp; ipos &gt; 1) {
                ipos --;
            }
            for (ipos = ipos; ipos &gt;= 2; ipos --) {
                prod = digit[ipos] * weights[iweight];
                prod += weights[iweight];
                prod %= 11;
                sum += prod;
                iweight ++;
            } // for ipos
            sum %= 10l;
            if (sum != digit[1]) {
                result.setCharAt(LEN_ACCOUNT - 1, CHAR_DIGITS.charAt((int) sum)); // insert digit into account
            }
            // System.out.print(" sum(24)=" + sum); // + ", result=" + result.toString());
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
            result.setLength(0); // error
        }
        return result.toString();
    } // method_24_B

    /** Auxiliary method 52_53_common
     *  @param method method to be used
     *  @param account account number to be tested
     *  @param blz Bankleitzahl
     *  @return corrected number
     */
    public String method_52_53_common(int method, String account, String blz) {
        StringBuffer result = new StringBuffer(account);
        try {
            int weights[] = {2,4,8,5,10,9,7,3,6,1,2,4};
            // digits [C B A 9 8 7 6 5 4 3 2 1]
            int ipos = 6;
            while (digit[ipos] == 0 &amp;&amp; ipos > 1) {
                ipos --;
            }
            // digits[ipos] now first digit != 0
            ipos ++;
            int ipz = ipos;
            int pzWeight = weights[ipz - 1];
            int pzDigit  = digit  [ipz];
            digit[ipos ++] = 0;
            if (method == 52) {
                digit[ipos ++] = digit[8];
                digit[ipos ++] = Character.digit(blz.charAt(7), 10);
                digit[ipos ++] = Character.digit(blz.charAt(6), 10);
                digit[ipos ++] = Character.digit(blz.charAt(5), 10);
                digit[ipos ++] = Character.digit(blz.charAt(4), 10);
            } else { // method 53
                int t          = digit[8];
                digit[ipos ++] = digit[9];
                digit[ipos ++] = Character.digit(blz.charAt(7), 10);
                digit[ipos ++] = t;
                digit[ipos ++] = Character.digit(blz.charAt(5), 10);
                digit[ipos ++] = Character.digit(blz.charAt(4), 10);
            }
            int ndig = ipos;
            sum = 0l;
            int iweight = 0; // index into weights
            for (ipos = 1; ipos &lt; ndig; ipos ++) {
                int prod = digit[ipos] * weights[iweight];
                sum += prod;
                iweight ++;
            } // for ipos
            sum %= 11l;
            int rest = (int) sum;
            ipos = 0;
            while ((rest + ipos * pzWeight) % 11 != 10 &amp;&amp; ipos &lt; 10) {
                ipos ++;
            }
            if (ipos &lt; 10) {
                // result [0 1 2 3 4 5 6 7 8 9]
                //             X P X X X X X X
                result.setCharAt(3, CHAR_DIGITS.charAt(ipos)); // insert digit into account
            } else {
                result.setLength(0);
            }
            // System.out.print(" sum(52_53)=" + ipos); // + ", result=" + result.toString());
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
            result.delete(0, result.length()); // error
        }
        return result.toString();
    } // method_52_53_common

    /** Special method 52
     *  @param account account number to be tested
     *  @return corrected number
     */
    public String method_52_A(String account) {
        return method_52_53_common(52, account, getBLZ());
    } // method_52_A

    /** Special method 53
     *  @param account account number to be tested
     *  @return corrected number
     */
    public String method_53_A(String account) {
        return method_52_53_common(53, account, getBLZ());
    } // method_53_A

    /** Special method 87
     *  @param account account number to be tested
     *  @return corrected number
     */
    public String method_87_A(String account) {
        StringBuffer result = new StringBuffer(account);
        try {
            int i, c2, d2, a5, p;
            int konto[] = new int[LEN_ACCOUNT + 1];
            for (i = 1; i &lt;= LEN_ACCOUNT; i ++) {
                konto[i] = digit[LEN_ACCOUNT + 1 - i];
            }
            int tab1[] = { 0, 4, 3, 2, 6 };
            int tab2[] = { 7, 1, 5, 9, 8 };
            boolean check_ok = false;
            i = 4;
            while ( i &lt; 10 &amp;&amp; konto[i] == 0) {
                i ++;
            }
            c2 = i % 2;
            d2 = 0;
            a5 = 0;
            while (i &lt; 10) {
                switch (konto[i]) {
                    case 0:
                        konto[i] = 5;
                        break;
                    case 1:
                        konto[i] = 6;
                        break;
                    case 5:
                        konto[i] = 10;
                        break;
                    case 6:
                        konto[i] = 1;
                        break;
                    default:
                        break;
                } // switch
                if (c2 == d2) {
                    if (konto[i] > 5) {
                        if (c2 == 0 &amp;&amp; d2 == 0) {
                            c2 = 1;
                            d2 = 1;
                            a5 = a5 + 6 - (konto[i] - 6);
                        } else {
                            c2 = 0;
                            d2 = 0;
                            a5 = a5 + konto[i];
                        }
                    } else {
                        if (c2 == 0 &amp;&amp; d2 == 0) {
                            c2 = 1;
                            a5 = a5 + konto[i];
                        } else {
                            c2 = 0;
                            a5 = a5 + konto[i];
                        }
                    }
                } else {
                    if (konto[i] > 5) {
                        if (c2 == 0) {
                            c2 = 1;
                            d2 = 0;
                            a5 = a5 - 6 + (konto[i] - 6);
                        } else {
                            c2 = 0;
                            d2 = 1;
                            a5 = a5 - konto[i];
                        }
                    } else {
                        if (c2 == 0) {
                            c2 = 1;
                            a5 = a5 - konto[i];
                        } else {
                            c2 = 0;
                            a5 = a5 - konto[i];
                        }
                    }
                }
                i ++;
            } // while i &lt; 10
            while (a5 &lt; 0 || a5 > 4) {
                if (a5 &gt; 4) {
                    a5 = a5 - 5;
                } else {
                    a5 = a5 + 5;
                }
            } // while a5
            if (d2 == 0) {
                p = tab1[a5];
            } else {
                p = tab2[a5];
            }
            if (p == konto[10]) {
                check_ok = true;
            } else {
                if (konto[4] == 0) {
                    if (p > 4) {
                        p = p - 5;
                    } else {
                        p = p + 5;
                    }
                    if (p == konto[10]) {
                        check_ok = true;
                    }
                }
            }
            if (! check_ok) {
                result.setLength(0); // wrong
                // System.out.println("code-87:" + p);
            } else {
                // System.out.println("code-87-ok");
            }
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();;
            result.delete(0, result.length()); // error
        }
        return result.toString();
    } // method_87

    </xsl:text>
</xsl:template><!-- method_87_A -->

<xsl:template name="trailer">
    <xsl:text>
    /** Test Frame, reads lines with method id and account number,
     *  and check each.
     *  @param args commandline arguments as single strings
     */
    public static void main (String args[]) {
        try {
            DeAccountChecker checker = new DeAccountChecker();
            if (args.length == 0) {
                System.out.print(checker.checkTestCases());
            } else { // with args
                String blz = ""; // methods 52, 53 not yet
                String polar = ""; // "true" or "false"
                String line; // from test input file
                BufferedReader infile = new BufferedReader (new FileReader (args[0]));
                boolean busy = true; // for loop control
                String zeroes = "00000000000000000000000"; // many leading zeroes
                String oldMethod = "xx";
                while (busy) {
                    line = infile.readLine();
                    if (line == null) { // EOF
                        busy = false;
                    } else if (line.length() >= 4) {
                        // System.out.println("/" + line + "/");
                        String elements[] = line.split("\\s+");
                        String method = elements[0];
                        if (method.compareTo("00") &gt;= 0 &amp;&amp; method.compareTo("EE") &lt;= 0) { // ignore other lines
                            if (! method.equals(oldMethod)) {
                                System.out.println();
                                oldMethod = method;
                            }
                            String account = elements[1];
                            if (account.length() > LEN_ACCOUNT) {
                                account = account.substring(0, LEN_ACCOUNT);
                            }
                            blz   = (elements.length &gt;= 3) ? elements[2] : "00000000";
                            polar = (elements.length &gt;= 4) ? elements[3] : "true";
                            String result = checker.check(method, account, blz); // returns account no. with correct(ed) check digit
                            result               = zeroes.substring(0, LEN_ACCOUNT - result .length()) + result;
                            String paddedAccount = zeroes.substring(0, LEN_ACCOUNT - account.length()) + account;
                            System.out.print(method + "\t");
                            System.out.print(method + " " + paddedAccount + " " + blz + " " + polar.substring(0, 1) + " ");
                            if (   polar.startsWith("t") &amp;&amp;   paddedAccount.equals(result)
                                || polar.startsWith("f") &amp;&amp; ! paddedAccount.equals(result)) {
                                System.out.println(" !");
                            } else {
                                System.out.println(" ? " + result);
                            }
                        } // if relevant line
                    } // length >= 4
                } // while busy
            } // with args
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
        } // catch
    } // main

} // DeAccountChecker
</xsl:text>
</xsl:template><!-- trailer -->

</xsl:stylesheet>
