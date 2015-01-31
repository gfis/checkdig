/*  PNDChecker.java - check Personennamendatenbank identifiers
    of the form M-vvvv-xxxx-c.
    @(#) $Id: PNDChecker.java 77 2009-01-16 08:14:16Z gfis $
	2009-01-09: result of 'check' is: new (formatted) number, space, [questionmark|exclamationmark]returnstring
    2008-11-03: Georg Fischer: copied from ISINChecker.java
    
    read lines with PNDs 
    check them according to the international algorithm
    
    Activation (test data at the end of this source program):
        java -cp dist/checkdig.jar org.teherba.checkdig.PNDChecker checkdig/PNDChecker.java
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

/** Class for the checkdigit in the identifiers in the Personennamendatenbank (PNDs),
    of the form 123456789.
 *  @author Dr. Georg Fischer
 */
public class PNDChecker extends BaseChecker {
    public final static String CVSID = "@(#) $Id: PNDChecker.java 77 2009-01-16 08:14:16Z gfis $";
    
    /** No-args constructor
     */
    public PNDChecker() {
        super();
    } // constructor
    
    /** length of the PND */
    private static final int LEN_PND = 9;
    
    /** Computes the check digits of an PND
	 *  Aus de.wikipedia.org, der freien Enzyklopädie:<br />
	 *  Wie beispielsweise die ISBN enthält die PND als letzte Stelle die Prüfziffer modulo 11. Die Prüfziffer berechnet sich wie folgt:
	 *  2*1. Stelle + 3*2. Stelle + 4*3. Stelle + ... + 9*8. Stelle | durch 11 dividiert = 9. Stelle  
     *  @param rawNumber PND to be tested
     *  @return return code, may be with recomputed PND with proper check digits
     */
    public String check(String rawNumber) { 
        String result = null; 
        String pnd = trim(rawNumber);
        int ipos   = 0;
        int sum    = 0; // value for 3 * 'M'
        int weight = 1;
        if (false) {
        } else if (pnd.length() < LEN_PND) { 
           	result = checkResponse(rawNumber, BaseChecker.TOO_SHORT);
        } else if (pnd.length() > LEN_PND) { 
           	result = checkResponse(rawNumber, BaseChecker.TOO_LONG);
        } else { // length ok
	        while (ipos < pnd.length() - 1) { // omit the last character ( = check digit)
    	        char ch = pnd.charAt(ipos);
 	            int imap = Character.digit(ch, 10);
 	            if (imap < 0) {
 	            	result = checkResponse(rawNumber, BaseChecker.WRONG_CHAR);
 	            	return result;
 	            } 
 	            ipos ++;
				weight ++;
				sum += imap * weight;
    	    } // while ipos
            sum = sum % 11;
            String newCheck = sum < 10 ? String.valueOf(sum) : "X";
        	result = checkResponse(rawNumber, format(pnd), newCheck);
        } // length ok
        return result;
    } // check
     
    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
	public String[] getTestCases() {
		return new String[] // from de.wikipedia.org, dump dated 2008-10-11
			/* Ang Lee */ 				{ "119317079"
			/* Andy Warhol */			, "118629220"
			/* Anthony Minghella */ 	, "120882965"
			/* Alfred Hitchcock */ 		, "118551647"
			/* Aki KaurismÃ¤ki */ 		, "118972332"
			/* Alexander der Große */ 	, "118501828"
			/* Anthony Hope */ 			, "11901842X"
			/* Alan Turing */ 			, "118802976"
			/* Arthur Harris */ 		, "119344165"
			/* Angelina Jolie */ 		, "123089190"
			/* Archimedes */ 			, "118503863"
			/* Archimedes wrong */	    , "118503868"
			                            , "12345"
			                            , "12345678901"
			                 			, "11850A863"
            };
    } // getTestCases

    /** Test Frame, reads lines with PNDs and checks each.
     *  @param args commandline arguments:
     *  <ul>
     *  <li>args[0] = name of file containing single PNDs on a line</li>
     *  </ul>
     */     
    public static void main (String args[]) { 
    	(new PNDChecker()).process(args);
    } // main

} // PNDChecker

