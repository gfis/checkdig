/*  Servlet interface to class DigitChecker
    @(#) $Id: CheckdigServlet.java 77 2009-01-16 08:14:16Z gfis $
    2016-10-12: less imports; moved to package web
    2016-09-03: without JSPs and session
    2016-07-29: DeTaxIdChecker
    2014-01-20: LF, no tabs
    2008-11-06: -isbn -ismn -issn -pnd
    2007-04-23: import explicit java packages
    2005-11-09: with -isin
    2005-08-26, Georg Fischer
*/
/*
 * Copyright 2005 Dr. Georg Fischer <punctum at punctum dot kom>
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
import  org.teherba.checkdig.web.IndexPage;
import  org.teherba.checkdig.web.Messages;
import  org.teherba.common.web.BasePage;
import  org.teherba.common.web.MetaInfPage;
import  java.io.IOException;
import  javax.servlet.ServletConfig;
import  javax.servlet.ServletException;
import  javax.servlet.http.HttpServlet;
import  javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;
import  org.apache.log4j.Logger;

/** Compute check digits for VAT ids, account numbers, IBANs etc.
 *  This class is the servlet interface to <em>DigitChecker</em>,
 *  and ressembles the functionality of the commandline interface
 *  in that class.
 *  @author Dr. Georg Fischer
 */
public class CheckdigServlet extends HttpServlet {
    public final static String CVSID = "@(#) $Id: CheckdigServlet.java 77 2009-01-16 08:14:16Z gfis $";
    public final static long serialVersionUID = 19470629;

    /** URL path to this application */
    private String applPath;
    /** log4j logger (category) */
    private Logger log;
    /** common code and messages for auxiliary web pages */
    private BasePage basePage;
    /** name of this application */
    private static final String APP_NAME = "CheckDig";

    /** Called by the servlet container to indicate to a servlet
     *  that the servlet is being placed into service.
     *  @param config object containing the servlet's configuration and initialization parameters
     *  @throws ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config); // ???
        log = Logger.getLogger(CheckdigServlet.class.getName());
        basePage = new BasePage(APP_NAME);
        Messages.addMessageTexts(basePage);
    } // init

    /** Creates the response for a HTTP GET request.
     *  @param request fields from the client input form
     *  @param response data to be sent back the user's browser
     *  @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        generateResponse(request, response);
    } // doGet

    /** Creates the response for a HTTP POST request.
     *  @param request fields from the client input form
     *  @param response data to be sent back the user's browser
     *  @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        generateResponse(request, response);
    } // doPost

    /** Creates the response for a HTTP GET or POST request.
     *  @param request fields from the client input form
     *  @param response data to be sent back the user's browser
     *  @throws IOException
     */
    public void generateResponse(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        if (true) { // try {
            String language = "en";
            String view     = BasePage.getInputField(request, "view"    , "index");
            String function = BasePage.getInputField(request, "function", "iban" );
            String parm1    = BasePage.getInputField(request, "parm1"   , ""     ); // empty: will print a list of testcases
            String parm2    = BasePage.getInputField(request, "parm2"   , ""     );

            if (false) {
            } else if (view.equals("index")) {
                if (false) {
                } else if (function.startsWith("acc"  )
                        || function.startsWith("ean"  )
                        || function.startsWith("iban" )
                        || function.startsWith("isbn" )
                        || function.startsWith("isin" )
                        || function.startsWith("ismn" )
                        || function.startsWith("issn" )
                        || function.startsWith("pnd"  )
                        || function.startsWith("taxid")
                        || function.startsWith("uci"  )
                        || function.startsWith("vat"  )
                        ) {
                    (new IndexPage    ()).dialog(request, response, basePage, function, parm1, parm2);
                } else { // failure: invalid function
                    basePage.writeMessage(request, response, language, new String[] { "401", "function", function });
                }

            } else if (view.equals("license")
                    || view.equals("manifest")
                    || view.equals("notice")
                    ) {
                (new MetaInfPage    ()).showMetaInf (request, response, basePage, language, view, this);
            } else {
                basePage.writeMessage(request, response, language, new String[] { "401", "view", view });
            }
    /*
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
    */
        }
    } // generateResponse

} // CheckdigServlet
