/*  UCIChecker.java - check SEPA Unique Creditor Identifiers
    @(#) $Id: UCIChecker.java 77 2009-01-16 08:14:16Z gfis $
    2017-05-29: javadoc 1.8
    2016-10-13: less imports
    2014-01-20: aoount.IBANChecker
    2009-01-09: result of 'check' is: new (formatted) number, tab, [questionmark|exclamationmark]returnstring
    2008-11-29: copied from IBANChecker

References:
    European Payments Council: SEPA Business-to-Business Direct Debit Scheme Customer-to-Bank
        Draft Implementation Guidelines v1.1 07.07.2008
    This document sets out the SEPA rules for implementing the customer-to-bank business-to-business
    direct debit UNIFI (ISO 20022) XML Message Standards based on version 1.1 of the
        SEPA Business-to-Business Direct Debit Scheme Rulebook.
    http://www.europeanpaymentscouncil.eu/knowledge_bank_detail.cfm?documents_id=141
and
    http://www.europeanpaymentscouncil.eu/documents/EPC262-08%20Creditor%20Identifier%20Overview%20v1.1.pdf

Algorithm:
    read lines with UniqueCreditorIds
    check them according to the international algorithm

Activation:
    java -cp dist/checkdig.jar org.teherba.checkdig.UCIChecker file-with-CreditorIds
*/
/*
 * Copyright 2008 Dr. Georg Fischer <punctum at punctum dot kom>
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
import  org.teherba.checkdig.account.IBANChecker;

/** Class for the checkdigits in the SEPA Unique Creditor Identifiers as defined in section 1.5.2 of the document of the
 *  European Payments Council: SEPA Business-to-Business Direct Debit Scheme Customer-to-Bank
 *          Draft Implementation Guidelines v1.1 07.07.2008.
 *  <p>
 *  This document sets out the SEPA rules for implementing the customer-to-bank business-to-business
 *  direct debit UNIFI (ISO 20022) XML Message Standards based on version 1.1 of the
 *          SEPA Business-to-Business Direct Debit Scheme Rulebook.
 *  <p>
 *  http://www.europeanpaymentscouncil.eu/knowledge_bank_detail.cfm?documents_id=141
 *  <p>
 *  Several test numbers are found below.
 *  @author Dr. Georg Fischer
 */
public class UCIChecker extends IBANChecker {
    public final static String CVSID = "@(#) $Id: UCIChecker.java 77 2009-01-16 08:14:16Z gfis $";

    /** No-args constructor
     */
    public UCIChecker() {
        super();
    } // Constructor

    /** Gets a predefined set of test numbers.
     *  @return array of all testcases as strings
     */
    public String[] getTestCases() {
        return new String[]
            { "DE79ZZZ01234567890"    // Bundesbank example with correct checksum
            , "DE34PUM00000001588"    // punctum GmbH
            , "WRONG_UCIS"
            , "AT97ZZZ01234567890"
            , "BE97ZZZ123456789"
            , "FR97ZZZ12345678"       // BEI 8
            , "FR97ZZZ12345678901"    // BEI 11
            , "FR97ZZZ123456789"      // SIREN 9
            , "FR97ZZZ12345678901234" // SIRET 14
            , "HU00ZZZA09450512"
            , "HU00ZZZE10011922T201"
            , "FI00ZZZ12345678"
            , "IT00ZZZA1B2C"          // 5 chars alphanumeric
            , "IE00ZZZ123456"         // 6 chars numeric
            , "LU00ZZZ0123456789"
            , "NL00ZZZ0123456789"
            , "PL00ZZZ0123456789"
            , "PT00ZZZ123456"
            , "ES00ZZZM23456789"
            , "GB14ZZZ123456"         // from http://www.maric.info/fin/SEPA/ddchkden.htm
            , "DE02ZZZ01234567890"    // from http://www.bundesbank.de/zahlungsverkehr/zahlungsverkehr_sepa_identifikation.php
            };
    } // getTestCases

    /** Computes the check digits of a Unique Creditor Id by cutting out the Creditor Business Code
     *  and applying the IBAN checksum calculation method (MOD 97-10, ISO 13616:2003 and ISO 7064).
     *  @param rawNumber CreditorId to be tested
     *  @return recomputed CreditorId with proper check digits, and return code
     */
    public String check(String rawNumber) {
        String result = null;
        String uci = trim(rawNumber);
        if (uci.length() < 8) {
            result = checkResponse(rawNumber, BaseChecker.TOO_SHORT);
        } else {
            result = super.check(uci.substring(0, 4) + uci.substring(7)); // check in IBANChecker
        }
        return result;
    } // check

    /** Formats a number: inserts spaces between groups of 4 characters,
     *  and converts all letters to upper case.
     *  @param rawNumber format this number (may contain hyphens, dots or spaces)
     */
    public String format(String rawNumber) {
        return trim(rawNumber.toUpperCase());
    } // format

    /** Test frame, reads lines with CreditorIds and checks each.
     *  @param args commandline arguments:
     *  <ul>
     *  <li>args[0] = name of file containing single CreditorIds on a line</li>
     *  </ul>
     */
    public static void main (String args[]) {
        (new UCIChecker()).process(args);
    } // main

} // UCIChecker
