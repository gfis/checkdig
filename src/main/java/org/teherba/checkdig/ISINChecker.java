/*  ISINChecker.java - check International Securities Identification Numbers (ISINs)
    using the algorithm from ISO 7812:1987(E), also called "Luhn check"
    @(#) $Id: ISINChecker.java 77 2009-01-16 08:14:16Z gfis $
	2009-01-09: result of 'check' is: new (formatted) number, space, [questionmark|exclamationmark]returnstring
	2008-11-18: success and error codes
    2007-05-24: name was ISINChecker
    2005-11-09, Georg Fischer: copied from IbanChecker.java
    
    Activation (test data at the end of this source program):
        java -cp dist/checkdig.jar org.teherba.checkdig.ISINChecker checkdig/ISINChecker.java
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
import  java.io.BufferedReader;
import  java.io.FileReader;

/** Class for the checkdigit in International Securities Identification Numbers (ISINs),
 *  using the algorithm from ISO 7812:1987(E), also called "Luhn check"
 *  @author Dr. Georg Fischer
 */
public class ISINChecker extends BaseChecker {
    public final static String CVSID = "@(#) $Id: ISINChecker.java 77 2009-01-16 08:14:16Z gfis $";
    
    /** No-args constructor
     */
    public ISINChecker() {
        super();
    } // constructor
    
    /** positions = numerical value of the character */
    //                                                 1         2         3
    //                                         0123456789012345678901234567890123456789
    protected static final String LETTERMAP = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    /** length of the ISIN */
    protected static final int LEN_ISIN = 12;
    
    /** Computes the check digits of an ISIN which starts with a 2-character country
     *  code, and which may contain letters thereafter.
     *  @param rawNumber ISIN to be tested
     *  @return formatted number, tab, return code 
     */
    public String check(String rawNumber) { 
        String result = null;
        StringBuffer buffer = new StringBuffer(32);
        String isin = trim(rawNumber);
        int ipos   = isin.length() - 1;
        int sum    = 0;
        int toggle = 0;
		int imap   = 0;
        if (false) {
        } else if (isin.length() < LEN_ISIN) { 
        	result = checkResponse(rawNumber, BaseChecker.TOO_SHORT);
        } else if (isin.length() > LEN_ISIN) { 
        	result = checkResponse(rawNumber, BaseChecker.TOO_LONG);
        } else { // length ok
        	ipos = 0;
	        while (ipos < isin.length() - 1) { // omit the last character ( = check digit)
    	        char ch = isin.charAt(ipos);
    	        imap = LETTERMAP.indexOf(ch);
    	        if (imap < 0) { // not found 
		        	result = checkResponse(rawNumber, BaseChecker.WRONG_CHAR);
					return result;
    	        } else if (imap < 10) { // digit
    	            buffer.append(ch);
    	        } else { // letter
    	            buffer.append(Integer.toString(imap)); // 2 digits >= 10
    	        }
    	        ipos ++;
    	    } // while ipos // 0ABC29 => 010111229
        
    	    ipos = buffer.length() - 1;
    	    sum    = 0;
    	    toggle = 0;
    	    while (ipos >= 0) {
    	        imap = Character.digit(buffer.charAt(ipos), 10);
    	        int prod = (toggle == 0) ? 2 * imap : imap;
    	        toggle = 1 - toggle;
    	        sum += prod % 10 + (int) prod / 10;
    	        // System.out.print(prod + "/" + sum + ", ");
    	        ipos --;
    	    } // while ipos
    	    sum = (10 - sum % 10) % 10;
            String newCheck = String.valueOf(sum);
        	result = checkResponse(rawNumber, format(isin), newCheck);
        } // length ok
        return result;
    } // check
    
    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
	public String[] getTestCases() {
		return new String[]
            { "ID1000023609" // from indonesian paper
            , "IDA0000001J1" 
            , "HRPLVARA0004" // Pliva Inc.
            , "HRRIBAO011A2" // Bank of Rijeka Inc.
            , "HRVABAPA0005" // Bank of Varazdin Inc.
            , "HRRH00O110A8" // Government of Croatia
            , "EU0009658145" // Euro Stoxx 50
            , "ES0178430E18" // Telefonica de Espana
            , "FR0000131104" // BNP Parisbas
            , "FR0000120537" // Lafarge      
            , "FR0000131906" // Renault      
            , "IT0000062072" // Ass. Generali
            , "IE0000197834" // Allied Irish Banks
            , "NL0000009538" // Philips Electronics
            , "NL0000301760" // Aegon
            , "BE0003801181" // Fotis UTS
            , "GB0007980591" // BP
            , "CH0012005267" // Nestle
            , "SE0000108656" // Ericsson B
            , "DE0005020903" // Medigene
            , "US7750431022" // Rofin Sinar
            , "DE000PREM111" // Premiere
            , "DE000A0D9PT0" // MTU Aero Engines
            , "DE000A0CAYB2" // Wincor Nixdorf
            , "DE000TUAG000" // TUI
            , "DE0007172009" // Schering
            , "DE0008032004" // Commerzbank
            , "AT0000969985" // AT + S
            , "FI0009000681" // Nokia
            , "WRONGISINXXX"
            };
	} // getTestCases

    /** Test Frame, reads lines with ISINs and checks each.
     *  @param args commandline arguments:
     *  <ul>
     *  <li>args[0] = name of file containing single ISINs on a line</li>
     *  </ul>
     */     
    public static void main (String args[]) { 
    	(new ISINChecker()).process(args);
    } // main

} // ISINChecker
/*
ID1000023609
IDA0000001J1
HRPLVARA0004
HRRIBAO011A2
HRVABAPA0005
HRRH00O110A8
EU0009658145
ES0178430E18
FR0000131104
FR0000120537
FR0000131906
IT0000062072
IE0000197834
NL0000009538
NL0000301760
BE0003801181
GB0007980591
CH0012005267
SE0000108656
DE0005020903
US7750431022
DE000PREM111
DE000A0D9PT0
DE000A0CAYB2
DE000TUAG000
DE0007172009
DE0008032004
AT0000969985 
FI0009000681
WRONGISINXXX
*/
