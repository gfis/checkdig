/*  ISMNChecker.java - check International Standard Music Numbers
    of the form M-vvvv-xxxx-c or 977-0-098-99999-c
    @(#) $Id: ISMNChecker.java 77 2009-01-16 08:14:16Z gfis $
    2016-10-12: less imports
    2009-01-09: result of 'check' is: new (formatted) number, space, [questionmark|exclamationmark]returnstring
    2008-11-03: Georg Fischer: copied from ISINChecker.java

    read lines with ISMNs
    check them according to the international algorithm

    Activation (test data in method getTestCases()):
        java -cp dist/checkdig.jar org.teherba.checkdig.ISMNChecker [infile]
*/
/*
 * Copyright 2008 Dr. Georg Fischer <punctum at punctum dot kom>
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
import  org.teherba.checkdig.EAN13Checker;
import  org.teherba.checkdig.ISMNRanges;

/** Class for the checkdigit in International Standard Music Numbers (ISMNs),
 *  of the form M-vvvv-xxxx-c or 977-0-098-99999-c
 *  @author Dr. Georg Fischer
 */
public class ISMNChecker extends EAN13Checker {
    public final static String CVSID = "@(#) $Id: ISMNChecker.java 77 2009-01-16 08:14:16Z gfis $";

    /** handle for ISMN publisher ranges */
    private ISMNRanges ranges;

    /** No-args constructor
     */
    public ISMNChecker() {
        super();
        ranges = new ISMNRanges();
    } // constructor

    /** length of the ISMN */
    private static final int LEN_ISMN = 10;

    /** Removes whitespace and punctuation from an ISMN.
     *  @param rawNumber ISMN to be formatted, possibly with whitespace and hyphens
     *  @return bare sequence of digits and 'X'
     */
    public String trim(String rawNumber) {
        return ranges.trim(rawNumber);
    } // trim

    /** Formats an ISMN with proper hyphenation.
     *  @param rawNumber ISMN to be formatted, possibly with whitespace and hyphensM-030-67118-9
     *  @return properly formatted ISMN: [musicland-0-|M-]group-publisher-check
     */
    public String format(String rawNumber) {
        String result = rawNumber.trim();
        switch (result.length()) {
            case 10:
                result = "M" + ranges.format("0" + result.substring(1)).substring(1);
                break;
            default:
            case 13:
                result = ranges.format(result);
                break;
        } // switch length
        return result;
    } // format

    /** Computes the check digits of an ISMN
     *  @param rawNumber ISMN to be tested
     *  @return reformatted/corrected number, tab, success or error code
     */
    public String check(String rawNumber) {
        String result = null;
        String ismn = trim(rawNumber);
        int ipos   = 1;
        int sum    = 9; // value for 3 * 'M'.
        int toggle = 0;
        if (false) {
        } else if (ismn.length() == LEN_ISMN + 3) { // ISMN-13
            if (ismn.startsWith("97")) { // 978- or 979-
                result = super.check(ismn); // check in EAN13Checker
            } else {
                result = checkResponse(rawNumber, BaseChecker.WRONG_RANGE);
            }
        } else if (ismn.length() == LEN_ISMN) { // ISMN-10
            while (ipos < ismn.length() - 1) { // omit the last character ( = check digit)
                char ch = ismn.charAt(ipos);
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
            sum = (10 - sum % 10) % 10; // 10 -> 0
            String newCheck = String.valueOf(sum);
            result = checkResponse(rawNumber, format(ismn), newCheck); // not ranges.format() !
        } else {
            result = checkResponse(rawNumber, BaseChecker.WRONG_LENGTH);
        }
        return result;
    } // check

    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
    public String[] getTestCases() {
        return new String[] // from de.wikipedia.org dump dated 2008-10-11
            { "M-030-67118-9" // artificial
            , "M-2306-7118-7" // [[ISMN]] in de.wikipedia.org
            , "M-43067-118-5" // artificial
            , "M-830671-18-1" // artificial
            , "M-9306711-8-0" // artificial
            , "M-2000-0084-9" // Alice Vollenweider: ''Italiens Provinzen und ihre Küche'', Wagenbach, Mai 1999, ISMN 3-8031-3052-2
            , "M-2042-2521-7" //* ''Sagen und Landschaften'', ISMN M-2042-2521-7
            , "M-2042-2528-6" //* ''Gitarrengeschichten 1'', ISMN M-2042-2528-6
            , "M-2042-2529-3" //* ''Gitarrengeschichten 2'', ISMN M-2042-2529-3
            , "M-2042-2547-7" //* ''Secrets'', ISMN M-2042-2547-7
            , "M-2042-2579-8" //* ''Suite for Lovers'', ISMN M-2042-2579-8
            , "M-2042-2582-8" //* ''Klangbilder für 2 Gitarren'', ISMN M-2042-2582-8
            , "M-2042-2652-8" //* ''Ausgewählte Folklorestücke'', ISMN M-2042-2652-8
            , "M-2042-2689-4" //* ''Chinesische Szenen'', ISMN M-2042-2689-4
            , "979-0-030-67118-9" // artificial
            , "WRONG_ISMN"
            };
    } // getTestCases

    /** Test Frame, reads lines with ISMNs and checks each.
     *  @param args commandline arguments:
     *  <ul>
     *  <li>args[0] = name of file containing single ISMNs on a line</li>
     *  </ul>
     */
    public static void main (String args[]) {
        (new ISMNChecker()).process(args);
    } // main

} // ISMNChecker

