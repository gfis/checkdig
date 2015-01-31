/*  ISBNChecker.java - check International Standard Book Numbers
    either ISBN-10, or ISBN-13 starting with 978 or 979 ("book land" code).
    @(#) $Id: ISBNChecker.java 227 2009-08-14 06:16:15Z gfis $
	2009-01-09: result of 'check' is: new (formatted) number, space, [questionmark|exclamationmark]returnstring
    2008-11-03: Georg Fischer: copied from ISINChecker.java
    
    read lines with ISBNs 
    check them according to the international algorithm
    
    Activation (test data at the end of this source program):
        java -cp dist/checkdig.jar org.teherba.checkdig.ISBNChecker checkdig/ISBNChecker.java
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
import  org.teherba.checkdig.EAN13Checker;
import  org.teherba.checkdig.ISBNRanges;
import  java.io.BufferedReader;
import  java.io.FileReader;
import  java.util.regex.Pattern;
import  java.util.regex.Matcher;

/** Class for the checkdigit in International Standard Book Numbers (ISBNs),
 *  either ISBN-10, or ISBN-13 starting with 978 or 979 ("book land" code).
 *  @author Dr. Georg Fischer
 */
public class ISBNChecker extends EAN13Checker {
    public final static String CVSID = "@(#) $Id: ISBNChecker.java 227 2009-08-14 06:16:15Z gfis $";
    
    /** handle for ISBN publisher ranges */
    private ISBNRanges ranges;
    
    /** No-args constructor
     */
    public ISBNChecker() {
        super();
        ranges = new ISBNRanges();
    } // constructor
    
    /** length of the ISBN */
    private static final int LEN_ISBN = 10;
    
    /** Removes whitespace and punctuation from an ISBN.
     *	@param rawIsbn ISBN to be formatted, possibly with whitespace and hyphens
     *  @return bare sequence of digits and 'X'
     */
    public String trim(String rawIsbn) {
        return ranges.trim(rawIsbn);
    } // trim

    /** Formats an ISBN with proper hyphenation.
     *	@param rawIsbn ISBN to be formatted, possibly with whitespace and hyphens
     *  @return properly formatted ISBN: [bookland-]group-publisher-books-check
     */
    public String format(String rawIsbn) {
        return ranges.format(rawIsbn);
    } // format

    /** Computes the check digits of an ISBN
     *  @param rawNumber ISBN to be tested
     *  @return recomputed ISBN with proper check digits, tab, return code
     */
    public String check(String rawNumber) { 
        String result = null;
        String isbn = ranges.trim(rawNumber);
        int ipos   = 0;
        int sum    = 0;
        int toggle = 0;
        // System.out.println(buffer.toString() + ": ");
        if (false) {
        } else if (isbn.length() == LEN_ISBN + 3) { // ISSN-13
        	if (isbn.startsWith("97")) {
	        	result = super.check(isbn); // check in EAN13Checker
	        } else {
	        	result = checkResponse(rawNumber, BaseChecker.WRONG_RANGE);
	    	}
        } else if (isbn.length() == LEN_ISBN) { // ISBN-10
	        while (ipos < isbn.length() - 1) { // omit the last character ( = check digit)
    	        char ch = isbn.charAt(ipos);
 	            int imap = Character.digit(ch, 10);
 	            if (imap < 0) {
		        	result = checkResponse(rawNumber, BaseChecker.WRONG_CHAR);
					return result;
 	            } 
	            ipos ++;
	            sum += imap * (ipos);
    	    } // while ipos
            sum = sum % 11;
            String newCheck = String.valueOf(sum <= 9 ? String.valueOf(sum) : "X");
        	result = checkResponse(rawNumber, ranges.format(isbn), newCheck);
		} else {
        	result = checkResponse(rawNumber, BaseChecker.WRONG_LENGTH);
        }
        return result;
    } // check
    
    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
    public String[] getTestCases() {
        return new String[] // from de.wikipedia.org, dump dated 2008-10-11
            { "3-442-15284-4" // [[Joseph E. Stiglitz]]: ''[[Die Schatten der Globalisierung]]'' Goldmann, München 2002,
            , "3-8031-3052-2" // Alice Vollenweider: ''Italiens Provinzen und ihre Küche'', Wagenbach, Mai 1999, ISBN 3-8031-3052-2
            , "1-84277-073-X" // Richard Peet: ''Unholy Trinity. The IMF, World Bank and WTO'', ISBN 1-84277-072-1, ISBN 1-84277-073-X
            , "1-84277-072-1" // dito
            , "9-04-110468-2" // Arthur Eyffinger, Arthur Witteveen, Mohammed Bedjaoui: ''La Cour internationale de Justice 1946–1996.'' Martinus Nijhoff Publishers, Den Haag und London 1999, ISBN 9-04-110468-2
            , "0-7148-9665-9" // ''Der Silberlöffel'' (Il Cucchiaio d’argento, ab 1950) ist das erfolgreichste italienische Kochbuch, Phaidon, Berlin, 2007, ISBN 0-7148-9665-9
            , "978-3-423-62239-4" // 2003 – ''El reino del dragón de oro'' (dt. ''[[Im Reich des Goldenen Drachen]]'') ISBN 978-3-423-62239-4
            , "978-3-421-04201-9" // Matthew Bogdanos mit William Patrick: ''Die Diebe von Bagdad. Raub und Rettung der ältesten Kulturschätze der Welt''. Aus dem Amerikanischen von Helmut Dierlamm (Originalausgabe: Thieves of Baghdad, Bloomsbury Publishing, New York 2005), Deutsche Verlags-Anstalt, München 2006, ISBN 3-421-04201-2, ISBN 978-3-421-04201-9
            , "3-421-04201-2" // dito
            , "978-3-8364-0692-5" //  Tiberius von Györy (Hrsg.): ''Semmelweis' gesammelte Werke''. VDM Müller, Saarbrücken 2007, ISBN 978-3-8364-0692-5      
            , "979-0201804842" // * Alexandr N. Skrjabin: ''24 Préludes op. 11 für Klavier'', G. Henle Verlag, München, ISBN 979-0201804842
            , "9979-32561-5" // * ''Die Seele des Nordens. Island, Färöer, Grönland''. Reykjavík, 2005. ISBN 9979-32561-5, ISBN 9979-32594-1. 
            , "9979-32594-1" // dito
            , "3-7979-1670-1" // ''Puig Rosados Tierleben''. Stalling, Oldenburg, Hamburg 1978, ISBN 3-7979-1670-1 
            , "978-3-515-08979-1" // Niklas Günther &amp; Sönke Zankel: ''Abrahams Enkel. Juden, Christen, Muslime und die Schoa''. 2006, ISBN 978-3-515-08979-1, beinhaltet: ''[[Norbert Masur]]: Ein Jude spricht mit Himmler'', übersetzt von Hauke Siemen
            , "979-95078-3-9" // Levelink/Mawdsley/Rijnberg: ''Vier Spaziergänge Botanischer Garten Bogor''&lt;br/&gt;Umfassende Angaben zur Geschichte, Systematik der Anlage und etliche Abbildungen. ISBN 979-95078-3-9
            , "979-0004182468" // Jan Pieterszoon Sweelinck: ''Choral- und Psalmvariationen'' (SwWV 193, 299, 301-303, 305-314, 316). Breitkopf &amp; Härtel, Wiesbaden 2006, ISBN 979-0004182468 (''Sämtliche Werke für Tasteninstrumente''. Band 3).
        	, "978-3-86680-192-9" // from ISBN article in de.Wikipedia.org
        	, "3-86680-192-0" // dito
            , "WRONGISBNX"
            };
    } // getTestCases

    /** Test Frame, reads lines with ISBNs and checks each.
     *  @param args commandline arguments:
     *  <ul>
     *  <li>args[0] = name of file containing single ISBNs on a line</li>
     *  </ul>
     */     
    public static void main (String args[]) { 
        try { 
            ISBNChecker checker = new ISBNChecker();
            if (args.length == 0) {
                System.out.print(checker.checkTestCases());
            } else {
            	Pattern isbnPat = Pattern.compile("\\s*\\=?\\s*([\\-\\.\\d Xx]+)");
            	Matcher matcher = null;
                String line; // from input file
                BufferedReader infile = new BufferedReader (new FileReader (args[0]));
                boolean busy = true; // for loop control
                
                while (busy) {
                    line = infile.readLine();
                    if (false) {
                    } else if (line == null) { // EOF 
                        busy = false;
                    } else if (line.startsWith("#")) {
                    	// ignore comment lines
                    } else if (line.length() < 10) {
                    	// ignore lines which are too short
                    } else {
                    /*
                    	int pos = line.indexOf("ISBN");
                    	if (pos >= 0) { // line contains an ISBN
                    		matcher = isbnPat.matcher(line.substring(pos + 4));
                    		if (matcher.lookingAt()) {
                    			String rawIsbn = matcher.group(1);
                    			String result = checker.check(rawIsbn);
                    			String[] fields = line.substring(1).split("\\|");
                    			if (result.startsWith("!")) { // checkdig ok + properly formatted
                    				System.out.println(checker.format(rawIsbn) + "|" + fields[0]);
                    			}
                    		} // match
                    	} else {
                    		// ignore
                    	}
				    */    
				        String compLine = checker.format(line);
                        String result = checker.check(line);
                        if (compLine.equals(result)) {
                            System.out.println(compLine + "\t!");
                        } else {
                            System.out.println(compLine + " \t-> " + result);
                        }
                    
                    } // not EOF
                } // while busy
            } // with argument(s)
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        } // catch
    } // main

} // ISBNChecker

