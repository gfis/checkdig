/*  BaseChecker.java - abstract checker class
    Copyright (c) 2005 Dr. Georg Fischer, D-79341 Kenzingen <punctum@punctum.com>
    @(#) $Id: BaseChecker.java 78 2009-02-05 17:11:47Z gfis $
    2017-05-29: javadoc 1.8
    2014-01-20: LF and no tabs
    2009-01-09: result of 'check' is: new (formatted) number, space, [questionmark|exclamationmark]returnstring
    2008-11-14: test cases convention; process; success + error codes
    2003-10-12, Georg Fischer
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
import  java.io.BufferedReader;
import  java.io.FileReader;

/** Wrapper class for all check digit classes.
 *  @author Dr. Georg Fischer
 */
public abstract class BaseChecker {
    public final static String CVSID = "@(#) $Id: BaseChecker.java 78 2009-02-05 17:11:47Z gfis $";

    /* success codes */
    /** successful check, number had proper formatting */
    public    static final String OK            = "!OK";
    /** successful check, but number was reformatted (upper case, hyphenation etc.) */
    public    static final String REFORMATTED   = "!FORM";
    /** successful check, but number was reformatted and made human readable (spaces between groups of 4 characters */
    public    static final String READABLE      = "!PRINT";

    /* error codes, with corrected/reformatted number */
    /** check failure */
    public    static final String WRONG_CHECK   = "?NOK";
    /** invalid length */
    public    static final String WRONG_LENGTH  = "?LEN";
    /** number too short */
    public    static final String TOO_SHORT     = "?SHORT";
    /** number too long */
    public    static final String TOO_LONG      = "?LONG";
    /** no number found which could be checked */
    public    static final String EMPTY         = "?EMPTY";
    /** bad character in number to be checked */
    public    static final String WRONG_CHAR    = "?CHAR";
    /** invalid country code */
    public    static final String WRONG_COUNTRY = "?CNTRY";
    /** invalid range */
    public    static final String WRONG_RANGE   = "?RANGE";
    /** system error */
    public    static final String SYSTEM_ERROR  = "?SYSERR";

    /** Map for numerical values of letters */
    protected static final String LETTER_MAP = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    protected static final String letter_MAP = "0123456789abcdefghijklmnopqrstuvwxyz";

    /** output mode: text (default), html */
    protected String outputMode;
    /** offset of the start of the check substring: 0, 1, etc. */
    protected int rangeStart;
    /** length of check substring (1 for one check digit) */
    protected int rangeLength;

    /** integer values of the digits of the account number (right to left = 1..10) */
    protected int [] digit;

    /** array of digits */                   /* [0123456789012] */
    protected static final String CHAR_DIGITS = "0123456789XXX";

    /** No-args constructor
     */
    public BaseChecker() {
        setCheckRange(0, 0); // position of check substring is unknown so far
        setMode("text");
    } // constructor

    /** Sets the output mode
     *  @param mode one of "text" (default) or "html"
     */
    public void setMode(String mode) {
        outputMode = mode;
    } // setMode

    /** Gets the output mode
     *  @return mode one of "text" (default) or "html"
     */
    public String getMode() {
        return outputMode;
    } // getMode

    /** Sets the starting position and length of the check digit range in the number to
     *  be checked
     *  @param start  offset of the start of the check substring: 0, 1, etc.
     *  @param length length of check substring (1 for one check digit)
     */
    public void setCheckRange(int start, int length) {
        rangeStart     = start;
        rangeLength    = length;
    } // setCheckRange

    /** Gets the checkdigit string, and sets the corresponding range.
     *  @param number number which contains a check digit sequence
     *  @param start  offset of the start of the check substring: 0, 1, etc.
     *  @param length length of check substring (1 for one check digit)
     *  @return contiguous sequence of check digits extracted from the number to be tested
     */
    public String getCheckString(String number, int start, int length) {
        rangeStart     = start;
        rangeLength    = length;
        return (start + length <= number.length())
                ? number.substring(start, start + length)
                : "?";
    } // getCheckString

    /** Gets the checkdigit string from a previously defined range.
     *  @param number number which contains a check digit sequence
     *  @return contiguous sequence of check digits extracted from the number to be tested
     */
    public String getCheckString(String number) {
        return getCheckString(number, rangeStart, rangeLength);
    } // getCheckString

    /** String which separates the number from the return code in check responses */
    public static final String RESPONSE_SEPARATOR = "\t";

    /** Builds a check response with:
     *  <ul>
     *  <li>the original number,</li>
     *  <li>a tab character</li>
     *  <li>a question mark for fatal failure,</li>
     *  <li>a string indicating the success or failure reason.</li>
     *  </ul>
     *  @param rawNumber the original, raw number which was checked
     *  @param errorCode one of the failure codes for fatal errors
     *  @return original <em>rawNumber</em>, a tab character, and the return code
     */
    public String checkResponse(String rawNumber, String errorCode) {
        StringBuffer result = new StringBuffer(64);
        result.append(rawNumber);
        result.append(RESPONSE_SEPARATOR);
        result.append(errorCode);
        return result.toString();
    } // checkResponse(2)

    /** Builds a check response with:
     *  <ul>
     *  <li>the reformatted/printable/corrected number,</li>
     *  <li>a tab character</li>
     *  <li>an exclamation mark for success or a question mark for failure,</li>
     *  <li>a string indicating the success or failure reason.</li>
     *  </ul>
     *  In this variant, the check digit is the last character of the number.
     *  @param rawNumber the original, raw number which was checked
     *  @param formNumber raw number, trimmed and reformatted/readable
     *  @param newCheck corrected check digits which must be inserted into <em>formNumber</em> at
     *  position <em>checkStart</em>, with length <em>checkLen</em>, or the error code in case of a fatal error
     *  @return corrected <em>formNumber</em>, a tab character, and the return code
     */
    public String checkResponse(String rawNumber, String formNumber, String newCheck) {
        return checkResponse(rawNumber, formNumber, formNumber.length() - 1, 1, newCheck);
    } // checkResponse(3)

    /** Builds a check response with:
     *  <ul>
     *  <li>the reformatted/printable/corrected number,</li>
     *  <li>a tab character</li>
     *  <li>an exclamation mark for success or a question mark for failure,</li>
     *  <li>a string indicating the success or failure reason.</li>
     *  </ul>
     *  @param rawNumber the original, raw number which was checked
     *  @param formNumber raw number, trimmed and reformatted/readable
     *  @param checkStart starting position of check digits in <em>formNumber</em>, 0 based
     *  @param checkLen number of check digits
     *  @param newCheck corrected check digits which must be inserted into <em>formNumber</em> at
     *  position <em>checkStart</em>, with length <em>checkLen</em>
     *  @return corrected <em>formNumber</em>, a tab character, and the return code
     */
    public String checkResponse(String rawNumber, String formNumber, int checkStart, int checkLen, String newCheck) {
        StringBuffer result = new StringBuffer(64);
        setCheckRange(checkStart, checkLen);
        String returnCode = null;
        { // not fatal
            String oldCheck   = formNumber.substring(   checkStart                                  , checkStart + checkLen);
            String corrNumber = formNumber.substring(0, checkStart) + newCheck + formNumber.substring(checkStart + checkLen);
            if (false) {
            } else if (rawNumber.equals(corrNumber)) {
                returnCode = BaseChecker.OK;
            } else if (oldCheck.toUpperCase().equals(newCheck)) {
                returnCode = BaseChecker.REFORMATTED;
            } else {
                returnCode = BaseChecker.WRONG_CHECK;
            }
            result.append(formNumber.substring(0, checkStart));
            if (getMode().equals("html")) {
                result.append("<span class=\"");
                result.append(returnCode.substring(0, 1).equals(OK.substring(0,1))
                        ? OK         .substring(1)
                        : WRONG_CHECK.substring(1)
                        );
                result.append("\">");
                result.append(newCheck);
                result.append("</span>");
            } else {
                result.append(newCheck);
            }
            result.append(formNumber.substring(checkStart + checkLen));
            result.append(RESPONSE_SEPARATOR);
            result.append(returnCode);
        } // not fatal
        return result.toString();
    } // checkResponse(5)

    /** Gets the offset of the start of the check substring
     *  @return  offset of the start of the check substring,
     */
    public int getRangeStart() {
        return  rangeStart;
    } // getRangeStart

    /** Gets the length of the check substring
     *  @return  length of the check substring,
     */
    public int getRangeLength() {
        return  rangeLength;
    } // getRangeLength

    /** Checks the check digit(s) in some number/entity, and
     *  @param parm1 number or entity to be tested
     *  @return a success code (starting with "!") or a
     *  failure code (starting with "?"), followed by the reformatted / corrected
     *  number.
     */
    public abstract String check(String parm1);

    /** Computes the check digit(s) in some number/entity
     *  @param parm1 number or entity to be tested
     *  @param parm2 additional entity to be used
     *  for the check digit computation
     *  @return a success code (starting with "!") or a
     *  failure code (starting with "?"), followed by the reformatted / corrected
     *  number.
     */
    public String check(String parm1, String parm2) {
        return check(parm1);
    } // check

    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
    public String[] getTestCases() {
        return new String[]
            { "EMPTY_TESTCASES"
            };
    } // getTestCases

    /** Checks the predefined set of entities.
     *  @return one reformatted/corrected/printable test case per line, a tab,
     *  and " !" for success,
     *  or " ?" for failure, followed by a return code
     */
    public String checkTestCases() {
        String [] testCases = getTestCases();
        StringBuffer result = new StringBuffer(8192);
        String nl = System.getProperty("line.separator");
        int itest = 0;
        while (itest < testCases.length) {
            String corrNumber = check(testCases[itest]);
            result.append(corrNumber);
            result.append(nl);
            itest ++;
        } // while itest
        return result.toString();
    } // checkTestCases

    /** Returns a textual list of predefined testcases
     *  @return all testcases as strings terminated by newlines
     */
    public String listTestCases() {
        String [] testCases = getTestCases();
        StringBuffer result = new StringBuffer(8192);
        String nl = System.getProperty("line.separator");
        int itest = 0;
        while (itest < testCases.length) {
            result.append(format(testCases[itest]));
            result.append(nl);
            itest ++;
        } // while itest
        return result.toString();
    } // listTestCases

    /** Removes format specific hyphenation, dots and spaces from a number
     *  @param rawNumber trim this number
     *  @return trimmed number
     */
    public String trim(String rawNumber) {
        return rawNumber.replaceAll("[ \\-\\.]", "");
    } // trim

    /** Formats a number, for example with proper hyphenation or inserted spaces,
     *  and converts letters to upper case.
     *  @param rawNumber format this number, may NOT contain hyphens, dots or spaces
     *  @return formatted number
     */
    public String format(String rawNumber) {
        return rawNumber.toUpperCase();
    } // format

    /** Reformats a string of digits which may contain separators.
     *  @param rawNumber format this number, MAY contain hyphens, dots or spaces
     *  @return formatted number
     */
    public String reformat(String rawNumber) {
        return format(trim(rawNumber));
    } // reformat

    /** Shows the predfined test cases, or reads a file
     *  with one number as first word per line, and checks these numbers.
     *  @param args optional commandline argument:
     *  <ul>
     *  <li>args[0] = name of file containing single numbers on a line</li>
     *  </ul>
     */
    public void process(String args[]) {
        try {
            if (args.length == 0) {
                System.out.print(checkTestCases());
            } else {
                String line; // from input file
                BufferedReader infile = new BufferedReader(new FileReader(args[0]));
                boolean busy = true; // for loop control

                while (busy) {
                    line = infile.readLine();
                    if (line == null) { // EOF
                        busy = false;
                    } else {
                        String numbers[] = line.trim().split(" ");
                        if (numbers.length >= 1) {
                            String result = check(numbers[0]);
                            System.out.println(numbers[0] + " " + result);
                        } // if length >= 1
                    } // not EOF
                } // while busy
            } // with argument(s)
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        } // catch
    } // process

} // BaseChecker
