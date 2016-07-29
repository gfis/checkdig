/*  Servlet interface to class DigitChecker
    @(#) $Id: CheckServlet.java 77 2009-01-16 08:14:16Z gfis $
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

package org.teherba.checkdig;
import  org.teherba.checkdig.DigitChecker;
import  java.io.IOException;
import  javax.servlet.RequestDispatcher;
import  javax.servlet.ServletConfig;
import  javax.servlet.ServletContext;
import  javax.servlet.ServletException;
import  javax.servlet.http.HttpServlet;
import  javax.servlet.http.HttpServletRequest;
import  javax.servlet.http.HttpServletResponse;
import  javax.servlet.http.HttpSession;

/** Compute check digits for VAT ids, account numbers, IBANs etc.
 *  This class is the servlet interface to <em>DigitChecker</em>,
 *  and ressembles the functionality of the commandline interface
 *  in that class.
 *  @author Dr. Georg Fischer
 */
public class CheckServlet extends HttpServlet {
    public final static String CVSID = "@(#) $Id: CheckServlet.java 77 2009-01-16 08:14:16Z gfis $";

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

    /** Gets the value of an HTML input field, maybe as empty string
     *  @param request request for the HTML form
     *  @param name name of the input field
     *  @return non-null (but possibly empty) string value of the input field
     */
    private String getInputField(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value == null) {
            value = "";
        } else if (value.length() > 256) { // sufficient for this application
            value = value.substring(0, 256);
        }
        return value;
    } // getInputField

    /** Creates the response for a HTTP GET or POST request.
     *  @param request fields from the client input form
     *  @param response data to be sent back the user's browser
     *  @throws IOException
     */
    public void generateResponse(HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            HttpSession session = request.getSession();
            String function = getInputField(request, "function");
            String parm1    = getInputField(request, "parm1"   );
            String parm2    = getInputField(request, "parm2"   );
            session.setAttribute("parm1"   , parm1   );
            session.setAttribute("parm2"   , parm2   );
            String newPage = "index";
            session.setAttribute("function"  , function); // assume success
            if (false) {
            } else if (   function.startsWith("acc" )          ) {
            } else if (   function.startsWith("ean" )          ) {
            } else if (   function.startsWith("iban")          ) {
            } else if (   function.startsWith("isbn")          ) {
            } else if (   function.startsWith("isin")          ) {
            } else if (   function.startsWith("ismn")          ) {
            } else if (   function.startsWith("issn")          ) {
            } else if (   function.startsWith("pnd" )          ) {
            } else if (   function.startsWith("taxid")         ) {
            } else if (   function.startsWith("uci" )          ) {
            } else if (   function.startsWith("vat" )          ) {
            } else { // failure: invalid function
                newPage = "message";
                session.setAttribute("messno"  , "001");
            }
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/" + newPage + ".jsp");
            dispatcher.forward(request, response);
        } catch (Exception exc) {
            response.getWriter().write(exc.getMessage());
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        }
    } // generateResponse

} // CheckServlet
