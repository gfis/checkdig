/*  IBANGenerator.java - check International Bank Account Numbers
    @(#) $Id: IBANGenerator.java 77 2009-01-16 08:14:16Z gfis $
    2014-01-22, Georg Fischer: copied from IBANChecker and gramword.MorphemTester
Algorithm:
    read lines with IBANs
    check them according to the international algorithm

Activation:
    java -cp dist/checkdig.jar org.teherba.checkdig.account.IBANGenerator file-with-IBANs
*/
/*
 * Copyright 2014 Dr. Georg Fischer <punctum at punctum dot kom>
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
package org.teherba.checkdig.account;
import  org.teherba.checkdig.account.BlzDetailBean;
import  org.teherba.checkdig.account.IBANChecker;
import  org.teherba.checkdig.account.IBANRuleBean;
import  org.teherba.dbat.Configuration;
import  org.teherba.xtrans.BaseTransformer;
import  java.io.BufferedReader;
import  java.io.FileReader;
import  java.sql.Connection;
import  java.sql.PreparedStatement;
import  java.sql.ResultSet;
import  java.sql.Statement;
import  org.apache.log4j.Logger;

/** Class which implements the bank-specific rules for the generation of IBANs
 *  from German account and Bankleitzahl parameters.
 *  @author Dr. Georg Fischer
 */
public class IBANGenerator {
    public final static String CVSID = "@(#) $Id: IBANGenerator.java 77 2009-01-16 08:14:16Z gfis $";

    /** Level of test output (0 = none, 1 = some, 2 = more ...) */
    private int debug = 1;
    /** log4j logger (category) */
    private Logger log;

    /** Codes for the access key of table <em>iban_rules</em> */
    private static final String ACCESS_ACCT     = "acct" ;
    private static final String ACCESS_BLZ      = "blz";
    private static final String ACCESS_BLZACCT  = "blzacct";
    private static final String ACCESS_BLZMACT  = "blzmact";
    private static final String ACCESS_MAC3     = "mac3";
    private static final String ACCESS_MACT     = "mact";
    private static final String ACCESS_RULE     = "rule";

    /** Codes for predefined IBAN rules */
    private static final String STD_RULE        = "0000";
    private static final String NOK_RULE        = "0001";

    /** No-args constructor
     */
    public IBANGenerator() {
        log = Logger.getLogger(IBANGenerator.class.getName());
        blzBean     = new BlzDetailBean();
        ruleBean    = new IBANRuleBean();
        ibanChecker = new IBANChecker();
    } // Constructor

    /** Computes the IBAN checksum */
    private IBANChecker ibanChecker;

    /** Assemble a German IBAN with standard checkdigits.
     *  @param account German account number, 10 digits.
     *  @param blz German Bankleitzahl, 8 digits.
     *  @return IBAN DEnnbbbbbbbbkkkkkkkkkk
     *  or error code starting with "?"
     */
    private String computeDeIBAN(String account, String blz) {
        String result = ibanChecker.check("DE00" + blz + account);
        int pos = result.indexOf("?");
        if (false && pos >= 0) {
            result = result.substring(pos);
        } else {
            result = result.substring(0, pos).replaceAll("\\s","");
        }
        return result;
    } // computeDeIBAN

    /** Determines the access path to SQL table <em>bankleitzahlen</em> */
    private String accessKey = ACCESS_BLZ;

    /** Database configuration */
    private Configuration config;
    /** Database connection */
    private Connection con;

    /** Bean for the properties of a BLZ */
    private BlzDetailBean blzBean;
    /** SQL statement for the access of BLZ details */
    private PreparedStatement blzStmt;

    /** Reads all details for a German Bankleitzahl from the database
     *  tables <em>bankleitzahlen, iban_access</em> using the
     *  connection {@link #con} which must be open.
     *  @param blz Bankleitzahl to be read
     *  @return code for the access key for the IBAN mapping rules table
     */
    public String fillBlzBean(String blz) {
        String result = "?NOK database error";
        try {
            blzStmt.clearParameters();
            int ipar = 1;
            blzStmt.setString(ipar ++, blz);
            ResultSet resultSet = blzStmt.executeQuery();
            int rowNo = 0; // number of rows read from database table
            while (resultSet.next()) {
                rowNo ++;
                blzBean.fillFromRow(resultSet);
            } // while resultSet
            resultSet.close();
            con.commit();
            if (false) {
            } else if (rowNo < 1) {
                result = "?NOK BLZ " + blz + " not found";
            } else if (rowNo > 1) {
                result = "?NOK BLZ " + blz + " has >= 2 entries";
            } else {
                result = blzBean.getOkey(); // exactly 1 row must have been read
            }
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
        }
        return result;
    } // fillBlzBean

    /** Bean for the properties of an IBAN mapping rule */
    private IBANRuleBean ruleBean;
    /** SQL statement for the access of IBAN mapping rules */
    private Statement ruleStmt;

    /** Reads all details for an IBAN rule from the database via
     *  connection {@link #con} which must be open.
     *  @param ibanRule IBAN rule number (4 digits) as listed the document of Deutsche Bundesbank
     *  @param access code how to access database table <em>iban_rules</em>
     *  @param account German account number (10 digits)
     *  @param blz German Bankleitzahl (8 digits)
     *  @param bic SWIFT BIC for blz
     *  @return code for the access of the IBAN rules table
     */
    public String fillRuleBean(String ibanRule, String access, String account, String blz, String bic) {
        String result = "?NOK database error";
        try {
            ruleBean.setNrid(STD_RULE);
            ResultSet resultSet = null;
            ruleStmt = con.createStatement();
            boolean readMultiple = false;
            String sql = ruleBean.getSelectSQL() + " = \'" + ibanRule;
            if (false) {
            } else if (access.equals(ACCESS_BLZ     )) {
                sql += "\' AND oblz = \'" + blz + "\'";
            } else if (access.equals(ACCESS_BLZACCT )) {
                sql += "\' AND oblz = \'" + blz + "\' AND oacct = \'" + account + "\'";
            } else if (access.equals(ACCESS_ACCT    )) {
                sql +=                            "\' AND oacct = \'" + account + "\'";
            } else if (access.equals(ACCESS_RULE    )) {
                sql += "\'";
            } else if (access.equals(ACCESS_BLZMACT )) {
                sql += "\' AND (oblz = \'" + blz + "\' OR oblz = '99999999')           ORDER BY oacct, oblz";
                readMultiple = true;
            } else if (access.equals(ACCESS_MAC3    )) {
                sql +=                            "\' AND oacct >= \'" + account + "\' ORDER BY oacct";
                readMultiple = true;
            } else if (access.equals(ACCESS_MACT    )) {
                sql +=                                                             "\' ORDER BY oacct";
                readMultiple = true;
            } else {
                log.error("invalid value of access: \"" + access + "\"");
                result = "?NOK assertion 2 " + access;
            }
            if (debug > 0) {
                System.out.println("# sql=" + sql.replaceAll("WHERE ", "\n#    WHERE ").replaceAll(" +", " "));
            }
            resultSet = ruleStmt.executeQuery(sql);
            int rowNo = 0; // number of rows read from database table
            boolean busy = true;
            while (busy && resultSet.next()) {
                rowNo ++;
                ruleBean.fillFromRow(resultSet);
                if (readMultiple) { // test all patterns
                    if (ruleBean.matches(account)
                            && (access.equals(ACCESS_BLZMACT)
                            	|| ruleBean.getOblz().length() == 0
                            	|| ruleBean.getOblz().equals(blz)
                               )
                            ) { // nacct, nrid are reassigned
                        busy = false;
                        setNextDefaults(ruleBean, account, blz, bic);
                        result = "!OK";
                    } // matches
                } else { // read a single row
                    busy = false; // break loop
                    setNextDefaults(ruleBean, account, blz, bic);
                    result = "!OK";
                } // single row
            } // while resultSet
            resultSet.close();
            ruleStmt.close();
            con.commit();

            if (false) {
            } else if (rowNo < 1) { // no matching record found -> rule 0000
                ruleBean.setNacct   (account);
                ruleBean.setNblz    (blz);
                ruleBean.setNrid    ("0000");
                result = "!OK";
            }

            if (debug > 0) {
                System.out.println("# fillRuleBean " + (new BaseTransformer()).getAttrs(ruleBean.getAttributes()));
            }
        } catch (Exception exc) {
            log.error(exc.getMessage(), exc);
            result = "?" + exc.getMessage();
        }
        return result;
    } // fillRuleBean

    /** Fills the 'next' properties in {@link #ruleBean} with defaults.
     *  @param ruleBean set the defaults in this bean
     *  @param account default for <em>nacct</em>
     *  @param blz     default for <em>nblz</em>
     *  @param bic     default for <em>nbic</em>
     */
    public void setNextDefaults(IBANRuleBean ruleBean, String account, String blz, String bic) {
        String nacct = ruleBean.getNacct();
        if (nacct.length() == 0) {
            ruleBean.setNacct(account);
        } else { // pad left with zeroes
            //                 0123456789
            ruleBean.setNacct("0000000000".substring(nacct.length()) + nacct); // fill to 10 digits with leading zeroes
        }
        if (ruleBean.getNblz ().length() == 0) {
            ruleBean.setNblz (blz    );
        } else { // read details of nblz
            fillBlzBean(ruleBean.getNblz());
            bic = blzBean.getBic();
        } // read details
        if (ruleBean.getNbic ().length() == 0) {
            ruleBean.setNbic (bic    );
        }
        if (ruleBean.getNrid ().length() == 0) {
            ruleBean.setNrid (STD_RULE);
        }
        if (ruleBean.getNkey ().length() == 0) {
            ruleBean.setNkey (ACCESS_RULE);
        }
    } // setNextDefaults

    /** Tries to compute an IBAN from a German account number and Bankleitzahl.
     *  @param account German account number, 1-10 digits, right aligned,
     *  maybe with missing leading zeroes and spaces.
     *  @param blz German Bankleitzahl, 8 digits, maybe with spaces.
     *  @return an array of string results:
     *  <ol>
     *  <li>success or error code</li>
     *  <li>generated IBAN</li>
     *  <li>applicable BIC</li>
     *  <li>short name of the bank</li>
     *  <li>Postleitzahl and Ort of the bank</li>
     *  <li>pzMethode</li>
     *  <li>IBAN-Regel and -Version</li>
     *  </ol>
     */
    public String[] generate(String account, String blz) {
        String result     = null;
        String iban       = "DEnn";
        String ibanRegel  = "none";
        String access     = "none";
        String bic        = "ubicDEnnxxxx";
        ruleBean.setNrid(NOK_RULE);
        account = account.replaceAll("\\D", ""); // remove all non-digits
        blz     = blz    .replaceAll("\\D", ""); // remove all non-digits
        if (blz.length() != 8) {
            result = "?NOK BLZ 8 digits";
        }
        int accountLen = account.length();
        if (result == null && (accountLen < 1 || accountLen > 10)) {
            result = "?NOK account 1-10 digits";
        } else { // access the DB
            if (accountLen < 10) { // pad left
                //         0123456789
                account = "0000000000".substring(accountLen) + account; // fill to 10 digits with leading zeroes
            } // pad left
            try {
                config = new Configuration();
                config.configure(config.CLI_CALL);
                con = config.getConnection();
                blzStmt = con.prepareStatement(blzBean.getSelectSQL() + " = ? ");
                result = fillBlzBean(blz);
                // check account with pzMethod first ???

                if (! result.startsWith("?")) { // BLZ record successfully read
                    String orid = blzBean.getIbanRegel();
                    String okey = blzBean.getOkey();
                    bic         = blzBean.getBic();
                    ibanRegel   = orid;
                    access      = okey;
                    if (debug > 0) {
                        System.out.println("#------------ start with orid=" + orid + ", okey=" + okey + " ----------");
                    }
                    ruleBean.setNacct   (account);
                    ruleBean.setNblz    (blz);
                    ruleBean.setNbic    (bic);

                    while (! orid.matches("000[01]")) { // not 0000 or 0001
                        account = ruleBean.getNacct();
                        blz     = ruleBean.getNblz ();
                        if (debug > 1) {
                            System.out.println("# generator1 for rule " + orid + ": " + result);
                        }
                        result = fillRuleBean(orid, okey, account, blz, bic);
                        orid = ruleBean.getNrid();
                        okey = ruleBean.getNkey();
                        blz  = ruleBean.getNblz();
                        bic  = ruleBean.getNbic();
                        if (debug > 0) {
                            System.out.println("# --> next orid=" + orid);
                        }
                    } // while orid > "0001"
                    if (orid.equals(NOK_RULE)) {
                        result = "?NOK account unusable for IBAN";
                    }
                } // BLZ record read
                if (result != null && ! result.startsWith("?")) {
                    iban = computeDeIBAN(ruleBean.getNacct(), ruleBean.getNblz());
                }
                blzStmt.close();
            } catch (Exception exc) {
                log.error(exc.getMessage(), exc);
            }
        } // access the DB
        if (bic.startsWith("COBADE") && ! blz.startsWith("200411")) { // Commerzbank AG, exception for comdirect
            if (debug > 0) {
                System.out.println("# old BIC: " + bic);
            }
            bic = bic.replaceAll("COBADE.*", "COBADEFFXXX");
        }
        return new String[]
                { iban
                , bic
                , ibanRegel
                , access
                , result
        //      , blzBean.getInst()
        //      , blzBean.getPlz() + " " + blzBean.getOrt()
                , blzBean.getPzMethode()
                };
    } // generate

    /** Computes the IBAN and/or checks a set of parameters
     *  @param params  2 to 4 parameters:
     *  <ul>
     *  <li>account blz</li>
     *  <li>account blz iban</li>
     *  <li>account blz iban bic</li>
     *  </ul>
     */
    public void processArray(String params[]) {
        if (debug > 1) {
            System.out.println("# processArray " + params[0] + " " + params[1]);
        }
        String[] results = this.generate(params[0], params[1]);
        int ipar = 0;
        while (ipar < params.length) {
            if (ipar > 0) {
                System.out.print("\t");
            }
            System.out.print(params[ipar ++]);
        } // while ipar
        System.out.print("\t=>");
        int ires = 0;
        while (ires < results.length) {
            System.out.print("\t");
            System.out.print(results[ires ++]);
        } // while ires
        System.out.println();
    } // processArray

    /** Reads a file with test data and computes or checks the IBAN data.
     *  @param args optional commandline arguments: a single filename,
     *  or 2 to to 4 parameters:
     *  <ul>
     *  <li>filename</li>
     *  <li>account blz</li>
     *  <li>account blz iban</li>
     *  <li>account blz iban bic</li>
     *  </ul>
     *  The lines in the file should contain the same 2 to 4 parameters.
     */
    public void process(String args[]) {
        try {
            if (args.length == 0) {
                System.out.print("usage: java -cp dist/checkdig.jar org.teherba.checkdig.account.IBANGenerator filename");
            } else if (args.length == 1) {
                String line; // from input file
                BufferedReader infile = new BufferedReader(new FileReader(args[0]));
                boolean busy = true; // for loop control
                while (busy) {
                    line = infile.readLine();
                    if (line == null) { // EOF
                        busy = false;
                    } else {
                        String params[] = line.trim().split("\t");
                        if (params.length >= 2) {
                            processArray(params);
                        } // if >= 2 parameters
                    } // not EOF
                } // while busy
                // with 1 filename
            } else { // >= 2 arguments
                processArray(args);
            }
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        } // catch
    } // process

    /** Test frame, reads lines with IBANs and checks each.
     *  @param args optional commandline arguments: a single filename,
     *  or 2 to to 4 parameters:
     *  <ul>
     *  <li>filename</li>
     *  <li>account blz</li>
     *  <li>account blz iban</li>
     *  <li>account blz iban bic</li>
     *  </ul>
     *  The lines in the file should contain the same 2 to 4 parameters.
     */
    public static void main (String args[]) {
        (new IBANGenerator()).process(args);
    } // main

} // IBANGenerator
