/*  EAN13Checker.java - check EANs
    @(#) $Id: EAN13Checker.java 77 2009-01-16 08:14:16Z gfis $
 *  2016-10-13: less imports
    2009-01-09: result of 'check' is: new (formatted) number, space, [questionmark|exclamationmark]returnstring
    2008-11-03: Georg Fischer: copied from ISINChecker.java

    read lines with EAN-13
    check them according to the international algorithm

    Activation (test data at the end of this source program):
        java -cp dist/checkdig.jar org.teherba.checkdig.EAN13Checker checkdig/EAN13Checker.java
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

/** Class for the checkdigit in International Article Numbers (EANs)of length 13.
 *  @author Dr. Georg Fischer
 */
public class EAN13Checker extends BaseChecker {
    public final static String CVSID = "@(#) $Id: EAN13Checker.java 77 2009-01-16 08:14:16Z gfis $";

    /** No-args constructor
     */
    public EAN13Checker() {
        super();
    } // constructor

    /** length of the EAN13 */
    protected static final int LEN_EAN13 = 13;

    /** Computes the check digits of an EAN 13.
     *  @param rawNumber EAN to be tested
     *  @return return code, may be with recomputed EAN with proper check digit
     */
    public String check(String rawNumber) {
        String result = null;
        String ean = trim(rawNumber);
        int ipos   = 0;
        int sum    = 0;
        int toggle = 0;
        if (ean.length() == LEN_EAN13) { // EAN-13
            while (ipos < ean.length() - 1) { // omit the last character ( = check digit)
                char ch = ean.charAt(ipos);
                int imap = Character.digit(ch, 10);
                if (imap < 0) {
                    result = checkResponse(rawNumber, BaseChecker.WRONG_CHAR);
                    return result;
                }
                ipos ++;
                toggle = 1 - toggle;
                if (toggle == 1) {
                    sum += imap;
                } else {
                    sum += imap * 3;
                }
            } // while ipos
            sum = 10 - sum % 10;
            String newCheck = sum <= 9 ? String.valueOf(sum) : "0";
            result = checkResponse(rawNumber, format(ean), newCheck);
        } else {
            result = checkResponse(rawNumber, BaseChecker.WRONG_LENGTH);
        }
        return result;
    } // check

    /** Formats a string of digits (without separators) and introduces standard EAN13 hyphenation.
     *  @param ean EAN13 to be formatted
     *  @return restructured EAN13 with proper hyphenation
     */
    public String format(String ean) {
        StringBuffer result = new StringBuffer(16);
        if (ean.startsWith("97")) {
            result.append(ean.substring( 0, 3));
            result.append('-');
            result.append(ean.substring( 3, 7));
            result.append('-');
            result.append(ean.substring( 7,11));
            result.append('-');
            result.append(ean.substring(11,13));
        } else {
            result.append(ean.substring( 0, 1));
            result.append('-');
            result.append(ean.substring( 1, 7));
            result.append('-');
            result.append(ean.substring( 7,12));
            result.append('-');
            result.append(ean.substring(12,13));
        } // switch length
        return result.toString();
    } // format

    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
    public String[] getTestCases() {
        return new String[] //
                { "978-3-8364-0692-5" //  Tiberius von Györy (Hrsg.): ''Semmelweis' gesammelte Werke''. VDM Müller, Saarbrücken 2007, ISBN 978-3-8364-0692-5
                , "979-0201804842" // * Alexandr N. Skrjabin: ''24 Préludes op. 11 für Klavier'', G. Henle Verlag, München, ISBN 979-0201804842
                , "4-022107-072243" // www.pearl.de
                , "4-002373-582769" // Gutenberg Werk Büromat. Mainz
                , "3001-9317"       // Scotch
                , "2000-6426"       // Lidl, Neckarsulm
                , "4-002432-328635" // Leitz
                , "8-713407-001915" // CD-R
                , "WRONG_EANs"
                , "3-123456-12345-6"
                , "0-88698-19277-1" // HP Toner
                , "4-006381-33627"  // Stabilo Boss
                , "7-40617-08585-3" // Kingston, China
                , "4-002432-382246" // Leitz
            };
    } // getTestCases

    /** Test Frame, reads lines with EAN13s and checks each.
     *  @param args commandline arguments:
     *  <ul>
     *  <li>args[0] = name of file containing single EAN13s on a line</li>
     *  </ul>
     */
    public static void main (String args[]) {
        (new EAN13Checker()).process(args);
    } // main

} // EAN13Checker

