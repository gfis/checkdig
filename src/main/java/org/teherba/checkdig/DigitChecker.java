/*  Check digits of various entities: account numbers, VAT ids, IBANs ...
    @(#) $Id: DigitChecker.java 519 2010-07-25 09:28:45Z gfis $
    2017-05-29: javadoc 1.8
    2016-07-29: DeTaxIdChecker
    2014-01-20: account.IBANChecker
    2009-01-09: mode HTML, colored check digits
    2008-11-18: EAN
    2008-11-03: ISBN, ISMN, ISSN, PND
    2007-02-02: package org.teherba.checkdig
    2005-11-09: with -isin
    2005-10-06: with -iban, -vatid
    2005-08-26: copied from numword
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
import  org.teherba.checkdig.account.DeAccountChecker;
import  org.teherba.checkdig.DeTaxIdChecker;
import  org.teherba.checkdig.EAN13Checker;
import  org.teherba.checkdig.account.IBANChecker;
import  org.teherba.checkdig.ISBNChecker;
import  org.teherba.checkdig.ISINChecker;
import  org.teherba.checkdig.ISMNChecker;
import  org.teherba.checkdig.ISSNChecker;
import  org.teherba.checkdig.PNDChecker;
import  org.teherba.checkdig.UCIChecker;
import  org.teherba.checkdig.VATIdChecker;
import  java.io.StringWriter;

/** Implements the commandline interface to various
 *  check digit generation classes.
 *  @author Dr. Georg Fischer
 */
public class DigitChecker {
    public final static String CVSID = "@(#) $Id: DigitChecker.java 519 2010-07-25 09:28:45Z gfis $";

    /** No-args Constructor
     */
    public DigitChecker() {
    } // Constructor

    /** Convenience overlay method with a single string argument instead
     *  of an array of strings.
     *  @param commandLine all parameters of the commandline in one string
     *  @return output of the call depending on the function: a digit sequence,
     *  a number word, a month name etc.
     */
    public String process(String commandLine) {
        return process(commandLine.split("\\s+"));
    } // process

    /** Evaluates the arguments of the command line, and processes them.
     *  @param args Arguments; if missing, print the following:
     *  <pre>
     *  usage:\tjava org.teherba.checkdig.DigitChecker [-m html]
     *        \t                           -account &lt;German account-number&gt; &lt;German BLZ&gt;
     *        \t                           -ean     &lt;EAN-13 or EAN-8&gt;
     *        \t                           -iban    &lt;IBAN&gt;
     *        \t                           -isbn    &lt;ISBN&gt;
     *        \t                           -isin    &lt;ISIN&gt;
     *        \t                           -ismn    &lt;ISMN&gt;
     *        \t                           -issn    &lt;ISSN&gt;
     *        \t                           -pnd     &lt;PND-Id&gt;
     *        \t                           -taxid   &lt;German Tax Identification Number&gt;
     *        \t                           -uci     &lt;SEPA Unique Creditor Id&gt;
     *        \t                           -vatid   &lt;VAT-Id Number&gt;
     *  </pre>
     *  @return input parameter with correct(ed) check digit,
     *  or empty string if the input value is syntactically malformed
     */
    public String process(String args[]) {
        String nl = System.getProperty("line.separator"); /* newline string (CR/LF or LF only) */
        StringWriter out = new StringWriter(16384); /* internal buffer for the string to be output */
        try {
            int iarg = 0; // index for command line arguments
            if (iarg >= args.length) { // usage
                out.write("usage:\tjava org.teherba.checkdig.DigitChecker [-f filename] [-m html]" + nl);
                out.write("      \t     -account <German Account Number> -blz <German BLZ>" + nl);
                out.write("      \t     -ean     <EAN-13 or EAN-8>        " + nl);
                out.write("      \t     -iban    <IBAN>     " + nl);
                out.write("      \t     -isbn    <ISBN-13 or ISBN-10>     " + nl);
                out.write("      \t     -isin    <ISIN>     " + nl);
                out.write("      \t     -ismn    <ISMN-13 or ISMN-10>     " + nl);
                out.write("      \t     -issn    <ISSN>     " + nl);
                out.write("      \t     -pnd     <PND>      " + nl);
                out.write("      \t     -taxid   <German Tax Identification Number>" + nl);
                out.write("      \t     -uci     <SEPA Unique Creditor Id>" + nl);
                out.write("      \t     -vatid   <VAT-Id Number>" + nl);
            } else { // >= 1 argument
                String  parm1   = "";
                String  parm2   = "";
                String  mode    = "text";
                BaseChecker checker = null;
                String function = "";
                String fileName = "";
                int iargs = 0;
                while (iargs < args.length) {
                    String option = args[iargs ++];
                    if (! option.startsWith("-")) {
                        parm1    = option;
                    } else if (option.equals    ("-f"      ) && iargs < args.length) {
                        fileName = args[iargs ++];
                    } else if (option.equals    ("-m"      ) && iargs < args.length) {
                        mode     = args[iargs ++];
                    } else if (option.equals    ("-blz"    ) && iargs < args.length) {
                        parm2    = args[iargs ++];
                    } else if (option.startsWith("-acc"    )) {
                        checker = new DeAccountChecker();
                    } else if (option.startsWith("-ean"    )) {
                        checker = new EAN13Checker();
                    } else if (option.startsWith("-iban"   )) {
                        checker = new IBANChecker();
                    } else if (option.startsWith("-isbn"   )) {
                        checker = new ISBNChecker();
                    } else if (option.startsWith("-isin"   )) {
                        checker = new ISINChecker();
                    } else if (option.startsWith("-ismn"   )) {
                        checker = new ISMNChecker();
                    } else if (option.startsWith("-issn"   )) {
                        checker = new ISSNChecker();
                    } else if (option.startsWith("-pnd"    )) {
                        checker = new PNDChecker();
                    } else if (option.startsWith("-taxid"  )) {
                        checker = new DeTaxIdChecker();
                    } else if (option.startsWith("-uci"    )) {
                        checker = new UCIChecker();
                    } else if (option.startsWith("-vat"    )) {
                        checker = new VATIdChecker();
                    } else {
                        out.write("unknown option \"-" + option + "\"");
                    }
                } // while commandline options
                if (checker != null) { // valid checker
                    checker.setMode(mode);
                    if (parm1.equals("")) { // without any parameters - show test cases
                        out.write(checker.checkTestCases());
                    } else { // at least 1 parameter
                        if (parm2.equals("")) {
                            out.write(checker.check(parm1));
                        } else {
                            out.write(checker.check(parm1, parm2));
                        }
                    } // at least 1 parameter
                } else {
                    out.write("no checker specified" + nl);
                }
            } // args.length >= 1
        } catch (Exception exc) { // will never happen since StringWriter always works?
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        } // try
        return out.toString();
    } // process

    public static void main(String args[]) {
        DigitChecker checker = new DigitChecker();
        System.out.print(checker.process(args));
    } // main

} // DigitChecker
