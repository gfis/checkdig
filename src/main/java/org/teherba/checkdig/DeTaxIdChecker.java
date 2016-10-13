/*  Class which checks lifelong German tax identification numbers
 *  @(#)$Id$
 *  2016-10-13: less imports
 *  2016-07-29, Georg Fischer
 */
/*
 * Copyright 2016 Dr. Georg Fischer <punctum at punctum dot kom>
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
import  java.lang.StringBuffer;

/** Check lifelong German tax identification numbers consisting of 10 digits plus a check digit.
 *  The tax id is described on <a href="https://de.wikipedia.org/wiki/Steuerliche_Identifikationsnummer">Wikipedia</a>.
 *  The check algorithm is described by <a href="http://www1.osci.de/sixcms/media.php/13/Pr%FCfziffernberechnung.pdf">osci.de</a>.
 */
public class DeTaxIdChecker extends BaseChecker {
    public final static String CVSID = "@(#) $Id: account.xsl 78 2009-02-05 17:11:47Z gfis $";

    /** length of the account number */
    private static final int LEN_TAXID = 11;

    /** accumulator for computations */
    private long sum;

    /** No-args constructor
     */
    public DeTaxIdChecker() {
        super();
        digit = new int[LEN_TAXID + 1]; // +1 because [0] is not used
    } // Constructor

    /** Computes the check digit of an German tax identification number.
     *  @param rawNumber TaxId to be tested
     *  @return return code, may be with recomputed TaxId with proper check digit
     */
    public String check(String rawNumber) {
        String result = null;
        String taxid = rawNumber.replaceAll(" ", "");
        int ipos   = 0;
        int sum    = 0;
        int prod   = 10;
        if (taxid.length() == LEN_TAXID) { // proper length
            while (ipos < taxid.length() - 1) { // omit the last character ( = check digit)
                char ch = taxid.charAt(ipos);
                int imap = Character.digit(ch, 10);
                if (imap < 0) {
                    result = checkResponse(rawNumber, BaseChecker.WRONG_CHAR);
                    return result;
                }
                ipos ++;
                sum = (imap + prod) % 10;
                if (sum == 0) {
                    sum = 10;
                }
                prod = (2 * sum) % 11;
            } // while ipos
            String newCheck = String.valueOf(11 - prod);
            if (newCheck.equals("10")) {
                newCheck = "0";
            }
            result = checkResponse(rawNumber, format(taxid), newCheck);
        } else {
            result = checkResponse(rawNumber, BaseChecker.WRONG_LENGTH);
        }
        return result;
    } // check

    /** Formats a string of digits (without separators) and introduces standard spaces
     *  in the format "nn nnn nnn nnn".
     *  @param rawNumber tax id to be formatted
     *  @return restructured tax id with proper spaces
     */
    public String format(String rawNumber) {
        StringBuffer result = new StringBuffer(16);
        String taxid = rawNumber.replaceAll(" ", "");
        result.append(taxid.substring( 0, 2));
        result.append(' ');
        result.append(taxid.substring( 2, 5));
        result.append(' ');
        result.append(taxid.substring( 5, 8));
        result.append(' ');
        result.append(taxid.substring( 8,11));
        return result.toString();
    } // format

    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
    public String[] getTestCases() {
        return new String[]
            { "01072495633"
            , "02138097565"
            , "01456280934"
            , "09736248057"
            , "04389217055"
            , "03189570460" // ok, from ZIVIT
            , "48 954 371 200"
            , "55 492 670 830"
            , "12345678901" // wrong
            };
    } // getTestCases

    /** Test Frame, reads lines with tax ids and check each.
     *  @param args commandline arguments as single Strings
     */
    public static void main (String args[]) {
        try {
            DeTaxIdChecker checker = new DeTaxIdChecker();
            if (args.length == 0) {
                System.out.print(checker.checkTestCases());
            } else { // with args
                String line; // from test input file
                BufferedReader infile = new BufferedReader (new FileReader (args[0]));
                boolean busy = true; // for loop control
                while (busy) {
                    line = infile.readLine().trim();
                    if (line == null) { // EOF
                        busy = false;
                    } else if (line.length() >= 4) {
                        // System.out.println("/" + line + "/");
                        String result = checker.check(line); // returns tax id no. with correct(ed) check digit
                        System.out.println(line + " -> " + result);
                    } // length >= 4
                } // while busy
            } // with args
        } catch (Exception exc) {
            System.out.println(exc.getMessage());
            exc.printStackTrace();
        } // catch
    } // main

} // DeTaxIdChecker
