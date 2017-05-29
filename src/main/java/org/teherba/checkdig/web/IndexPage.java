/*  IndexPage.java - main web page for CheckDig
 *  @(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $
 *  2017-05-29: javadoc 1.8
 *  2016-10-12: less imports
 *  2016-08-31: Dr. Georg Fischer: copied from Xtool
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
package org.teherba.checkdig.web;
import  org.teherba.checkdig.DigitChecker;
import  org.teherba.common.web.BasePage;
import  java.io.PrintWriter;
import  java.io.Serializable;
import  javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;
import  org.apache.log4j.Logger;

/** CheckDig main dialog page
 *  @author Dr. Georg Fischer
 */
public class IndexPage implements Serializable {
    public final static String CVSID = "@(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $";
    public final static long serialVersionUID = 19470629;

    /** log4j logger (category) */
    private Logger log;

    /** No-args Constructor
     */
    public IndexPage() {
        log      = Logger.getLogger(IndexPage.class.getName());
    } // Constructor

    /** Output the main dialog page for RaMath
     *  @param request request with header fields
     *  @param response response with writer
     *  @param basePage refrence to common methods and error messages
     *  @param function code for the particular check digit method (iban, isbn and so on).
     *  @param parm1    the main number to be checke
     *  @param parm2    optional 2nd parameter, for excample German BLZ
     */
    public void dialog(HttpServletRequest request, HttpServletResponse response
            , BasePage basePage
            , String function
            , String parm1
            , String parm2
            ) {
        String language = "en";
        try {
            PrintWriter out = basePage.writeHeader(request, response, language);

            out.write("<title>" + basePage.getAppName() + " Main Page</title>\n");
            out.write("</head>\n<body>\n");

            DigitChecker digitChecker = new DigitChecker();

            // 2 parallel arrays, element [0] is ignored
            String[] optFunction = new String []
                    /*  0 */ { "dummy"
                    /*  1 */ , "acc"
                    /*  2 */ , "ean"
                    /*  3 */ , "iban"
                    /*  4 */ , "isbn"
                    /*  5 */ , "isin"
                    /*  6 */ , "ismn"
                    /*  7 */ , "issn"
                    /*  8 */ , "pnd"
                    /*  9 */ , "taxid"
                    /* 10 */ , "uci"
                    /* 11 */ , "vat"
                    } ;
            String[] enFunction = new String []
                    /*  0 */ { "dummy"
                    /*  1 */ , "German Bank Account Number (+ BLZ)"
                    /*  2 */ , "Internat. Article Number (EAN)"
                    /*  3 */ , "Internat. Bank Account Number (IBAN)"
                    /*  4 */ , "Internat. Standard Book Number (ISBN)"
                    /*  5 */ , "Internat. Stock Id Number (ISIN)"
                    /*  6 */ , "Internat. Standard Music Number (ISMN)"
                    /*  7 */ , "Internat. Standard Serial Number (ISSN)"
                    /*  8 */ , "Person-Name Database Id (PND-Id)"
                    /*  9 */ , "German Tax Identification Number"
                    /* 10 */ , "SEPA Unique Creditor Id (UCI)"
                    /* 11 */ , "European Value Added Tax (VAT) Id"
                    } ;
            out.write("<!-- function=\"" + function + "\", parm1=\"" + parm1 + "\", parm2=\"" + parm2 + " -->\n");
            out.write("<h2>Checkdigits</h2>\n");
            out.write("<form action=\"servlet\" method=\"post\">\n");
            out.write("    <table>\n");
            out.write("        <tr><th align=\"left\">Check Method</th>\n");
            out.write("            <th>&nbsp;</th>\n");
            out.write("            <th align=\"left\">Number to be checked</th>\n");
            out.write("        </tr>\n");
            out.write("        <tr valign=\"top\">\n");
            out.write("            <td><select name=\"function\" size=\"" + String.valueOf(enFunction.length - 1) + "\">\n");

            int ind = 1; // element [0] is ignored
            while (ind < optFunction.length) {
                out.write("<option value=\""
                        + optFunction[ind] + "\""
                        + (optFunction[ind].equals(function) ? " selected" : "")
                        + ">"
                        + enFunction[ind] + "</option>\n");
                ind ++;
            } // while ind

            out.write("                </select>\n");
            out.write("                <br />\n");
            basePage.writeAuxiliaryLinks(language, "main");
            out.write("            </td>\n");
            out.write("            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>\n");
            out.write("            <td>\n");
            out.write("                <input name=\"parm1\" maxsize=\"80\" size=\"40\" value=\"" + parm1 + "\"/>\n");
            out.write("                <br />&nbsp;\n");
            out.write("                <br />Optional Parameter (BLZ):\n");
            out.write("                <br />\n");
            out.write("                <input name=\"parm2\" maxsize=\"80\" size=\"40\" value=\"" + parm2 + "\"/>\n");
            out.write("                <br />\n");
            out.write("                <input type=\"submit\" value=\"Submit\">\n");
            out.write("                <h3>\n");
            String result = digitChecker.process(new String[] { "-m", "html", "-" + function, parm1, "-blz", parm2 });
            if (parm1.equals("")) {
                out.write("\n<pre>\n" + result + "\n</pre>\n");
            } else { // >= 1 parameter(s)
                out.write(result + "\n");
            }
            out.write("                </h3>\n");
            out.write("            </td>\n");
            out.write("       </tr>\n");
            out.write("    </table>\n");
            out.write("</form>\n");

            basePage.writeTrailer(language, "quest");
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
    } // dialog

    //================
    // Main method
    //================

    /** Test driver
     *  @param args language code: "en", "de"
     */
    public static void main(String[] args) {
        IndexPage help = new IndexPage();
        System.out.println("no messages");
    } // main

} // IndexPage
