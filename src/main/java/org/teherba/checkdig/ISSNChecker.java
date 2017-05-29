/*  ISSNChecker.java - check International Standard Serial Numbers
    either ISSN-10, or ISSN-13 starting with 977 ("serial land" code).
    @(#) $Id: ISSNChecker.java 77 2009-01-16 08:14:16Z gfis $
    2016-10-12: less imports
    2009-01-09: result of 'check' is: new (formatted) number, space, [questionmark|exclamationmark]returnstring
    2008-11-03: Georg Fischer: copied from ISINChecker.java

    read lines with ISSNs
    check them according to the international algorithm

    Activation (test data at the end of this source program):
        java -cp dist/checkdig.jar org.teherba.checkdig.ISSNChecker [infile]
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

/** Class for the checkdigit in International Standard Serial Numbers (ISSNs),
 *  either ISSN-10, or ISSN-13 starting with 977 ("serial land" code).
 *  @author Dr. Georg Fischer
 */
public class ISSNChecker extends EAN13Checker {
    public final static String CVSID = "@(#) $Id: ISSNChecker.java 77 2009-01-16 08:14:16Z gfis $";

    /** No-args constructor
     */
    public ISSNChecker() {
        super();
    } // constructor

    /** length of the ISSN */
    private static final int LEN_ISSN = 8;

    /** Computes the check digits of an ISSN.
     *  from de.wikipedia.org, the free encyclopedia:
     *  Die Pruefziffer einer ISSN wird aus der Differenz von 11 zum [[Modulo]] 11
     *  der absteigend von 8 bis 2 gewichteten Quersumme
     *  (8 mal erste Stelle plus 7 mal zweite Stelle etc.) berechnet.
     *  Fuer den Wert 10 wird als Pruefziffer das Zeichen 'X' verwendet.
     *  For EAN ISSN-13: append "00" if necessary, and compute check digit
     *  according to EAN rules.
     *  @param rawNumber ISSN to be tested
     *  @return return code, may be with recomputed ISSN with proper check digits
     */
    public String check(String rawNumber) {
        String result = null;
        String issn = trim(rawNumber);
        int ipos   = 0;
        int sum    = 0;
        int toggle = 0;
        if (false) {
        } else if (issn.length() == LEN_ISSN + 5) { // ISSN-13
            if (issn.startsWith("977")) {
                result = super.check(issn);
            } else {
                result = checkResponse(rawNumber, BaseChecker.WRONG_RANGE);
            }
        } else if (issn.length() == LEN_ISSN) { // ISSN-8
            while (ipos < issn.length() - 1) { // omit the last character ( = check digit)
                char ch = issn.charAt(ipos);
                int imap = Character.digit(ch, 10);
                if (imap < 0) {
                    result = checkResponse(rawNumber, BaseChecker.WRONG_CHAR);
                    return result;
                }
                ipos ++;
                sum += imap * (LEN_ISSN + 1 - ipos);
            } // while ipos
            sum = sum % 11;
            String newCheck = String.valueOf(11 - sum);
            switch (sum) {
                case  1:
                    newCheck = "X";
                    break;
                case  0:
                    newCheck = "0";
                    break;
                case 10:
                default:
                    break;
            } // switch sum
            result = checkResponse(rawNumber, format(issn), newCheck);
        } else {
            result = checkResponse(rawNumber, BaseChecker.WRONG_LENGTH);
        }
        return result;
    } // check

    /** Formats a string of digits (without separators) and introduces standard ISSN hyphenation.
     *  ISSN-8 is divided into 4-4, whereas ISSN-13 (EAN) is divided into 977-4-4-2.
     *  @param rawNumber ISSN to be formatted
     *  @return restructured ISSN with proper hyphenation
     */
    public String format(String rawNumber) {
        StringBuffer result = new StringBuffer(16);
        String issn = trim(rawNumber);
        switch (issn.length()) {
            case  8: // 1234-5678
                result.append(issn.substring( 0, 4));
                result.append('-');
                result.append(issn.substring( 4, 8));
                break;
            case 13: // 977-1234-5678-00
                result.append(issn.substring( 0, 3));
                result.append('-');
                result.append(issn.substring( 3, 7));
                result.append('-');
                result.append(issn.substring( 7,11));
                result.append('-');
                result.append(issn.substring(11,13));
                break;
            default:
                result.append(BaseChecker.WRONG_LENGTH);
                result.append('-');
                result.append(issn);
                break;
        } // switch length
        return result.toString();
    } // format

    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
    public String[] getTestCases() {
        return new String[] // from de.wikipedia.org, dump dated 2008-10-11
                /* Dariusz Muszer */                            { "0239-6874"
                /* Erich Wasmann */                             , "0944-3266"
                /* Spartakist-Arbeiterpartei Deutschlands */    , "0173-7430"
                /* Personidae */                                , "0076-2997"
                /* Töpfergesellschaft Solothurn */              , "1423-3401"
                /* Rehabilitation psychisch Kranker */          , "0933-8462"
                /* Preventori d’Aigües */                       , "1699-1451"
                /* Undisputed Attitude */                       , "1437-8728"
                /* Schwarz-Rot-Gold */                          , "0947-9945"
                /* Anton Schwob */                              , "1862-4995"
                /* Bremen zur Zeit des Nationalsozialismus */   , "0341-1915"
                /* Stachelschnecken */                          , "0076-2997"
                /* Studienfinanzierung */                       , "1863-7663"
                /* Seidenstraßenstrategie */                    , "0936-3408"
                /* Seidenstraßenstrategie */                    , "1060-586X"
                                                                , "0258-6800"
                                                                , "1613-8910"
                                                                , "1529-4560"
                                                                , "9770023968748"
                                                                , "WRONG_ISSNs"
                /* wrong check  */                              , "0239-6875"
                };
    } // getTestCases

    /** Test Frame, reads lines with ISSNs and checks each.
     *  @param args commandline arguments:
     *  <ul>
     *  <li>args[0] = name of file containing single ISSNs on a line</li>
     *  </ul>
     */
    public static void main (String args[]) {
        (new ISSNChecker()).process(args);
    } // main

} // ISSNChecker

