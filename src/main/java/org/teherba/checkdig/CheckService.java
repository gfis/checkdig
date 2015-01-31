/*  Check digits of various entities: account numbers, VAT Ids, IBANs ...
    @(#) $Id: CheckService.java 77 2009-01-16 08:14:16Z gfis $
	2009-01-13: function.startsWith instead of .equals
	2008-11-06: -isbn -ismn -issn -pnd
    2006-12-14: without 'main', simplified
    2005-08-26: copied from numword 2005-11-09: with -isin
    
    Service to be called via SOAP, offering the functions of DigitChecker
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
import  org.teherba.checkdig.DigitChecker;

/** Compute check digits for VAT ids, account numbers, IBANs etc.
 *  This class is the SOAP service interface to <em>DigitChecker</em>, 
 *  and ressembles the functionality of the commandline interface
 *  in that class.
 *  @author Dr. Georg Fischer
 */
public class CheckService { 
    public final static String CVSID = "@(#) $Id: CheckService.java 77 2009-01-16 08:14:16Z gfis $";

    /** initialize checkdig object */
    private DigitChecker checker = new DigitChecker();
    
    /** Returns the results of an activation of <em>DigitChecker</em>
     *  to a SOAP client.
     *  @param function a code for the desired function: iban, vatid, account ...
     *  @param parm1 primary entity to be checked (account number, VAT-Id, IBAN etc.)
     *  @param parm2 additional parameter (e.g. German Bankleitzahl)
     *  @return primary entity with correct(ed) check digit
     */
    public String getResponse(String function, String parm1, String parm2)  {
        String response = "";
        try {
            if  (   false
                ||  function.startsWith("acc"       )
                ||  function.startsWith("ean"       )
                ||  function.startsWith("iban"      )
                ||  function.startsWith("isbn"      )
                ||  function.startsWith("isin"      )
                ||  function.startsWith("ismn"      )
                ||  function.startsWith("issn"      )
                ||  function.startsWith("pnd"       )
                ||  function.startsWith("uci"       )
                ||  function.startsWith("vat"       )        
                ) 
            {
                response = checker.process(new String[] { "-" + function, parm1, parm2 });
            } else { // invalid function
                response = "001 - invalid function \"" + function + "\"";
            }
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        }
        return response;
    } // getResponse

 } // CheckService
