/*  IBANRuleBean.java - specific data for a German Bankleitzahl record
 *  @(#) $Id: IBANRuleBean.java 37 2008-09-08 06:11:04Z gfis $
 *  2017-05-29: javadoc 1.8
 *  2014-01-22: Georg Fischer
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
import  org.teherba.checkdig.account.IBANRuleBeanBase;
import  java.util.regex.Matcher;
import  java.util.regex.Pattern;
import  java.sql.ResultSet;

/** Specific properties for a German Bankleitzahl.
 *  <p />
 *  See also the document
 *  <a href="http://www.bundesbank.de/Navigation/DE/Kerngeschaeftsfelder/Unbarer_Zahlungsverkehr/Bankleitzahlen/bankleitzahlen.html">Bankleitzahlen</a>
 *  (77 pages in Jan. 2014).
 */
public class IBANRuleBean extends IBANRuleBeanBase {
    public static final String CVSID = "@(#) $Id: IBANRuleBean.java 37 2008-09-08 06:11:04Z gfis $";

    /** Level of test output (0 = none, 1 = some, 2 = more ...) */
    private int debug = 1;

    /** No-args Constructor
     */
    public IBANRuleBean() {
        // set non-null properties
        setOrid        ("");
        setOkey        ("");
        setOblz        ("");
        setOacct       ("");
        setOcheck      ("");
        setNblz        ("");
        setNacct       ("");
        setNbic        ("");
        setNiban       ("");
        setNrid        ("");
        setNkey        ("");
    } // Contructor(0)

    /** Gets the SQL which reads the bean properties,
     *  and which corresponds to {@link #fillFromRow}.
     *  @return SQL SELECT statement with FROM and start of WHERE clause
     */
    public String getSelectSQL() {
        return  ( "SELECT r.orid "
                + ", r.okey      "
                + ", r.oblz      "
                + ", r.oacct     "
                + ", r.ocheck    "
                + ", r.nblz      "
                + ", r.nacct     "
                + ", r.nbic      "
                + ", r.niban     "
                + ", r.nrid      "
                + ", r.nkey      "
                + " FROM iban_rules r "
                + " WHERE r.orid " // = ...
                );
    } // getSelectSQL

    /** Fills the bean properties from the current row in a result set.
     *  @param resultSet read from this ResultSet
     */
    public void fillFromRow(ResultSet resultSet) {
        try {
            int ipar = 1;
            setOrid        (resultSet.getString(ipar ++));
            setOkey        (resultSet.getString(ipar ++));
            setOblz        (resultSet.getString(ipar ++));
            setOacct       (resultSet.getString(ipar ++));
            setOcheck      (resultSet.getString(ipar ++));
            setNblz        (resultSet.getString(ipar ++));
            setNacct       (resultSet.getString(ipar ++));
            setNbic        (resultSet.getString(ipar ++));
            setNiban       (resultSet.getString(ipar ++));
            setNrid        (resultSet.getString(ipar ++));
            setNkey        (resultSet.getString(ipar ++));
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
        }
    } // fillFromRow

    /** Checks whether the parameter matches {@link #oacct},
     *  and reassignes {@link #nacct} in that case.
     *  @param account account number to be matched, without leading zeroes
     *  @return whether a match was found
     */
    public boolean matches(String account) {
        boolean result = false; // assume failure
        String opat = getOacct();
        if (opat.equals("\\d}")) {
            opat =      "\\d+"; // restore regular expression syntax
        }
        Pattern pattern = Pattern.compile(opat);
        Matcher matcher = pattern.matcher(account);
        if (debug > 0) {
            System.out.print("# matches " + account + " =~ " + getOacct());
        }
        if (matcher.matches()) {
            result = true; // success
            String nextAccount = getNacct();
            if (nextAccount.length() == 0) {
                nextAccount = account;
            }
            if (debug > 0) {
                System.out.print(" " + account + ".replaceAll(" + getOacct() + ", " + nextAccount + ")");
            }
            nextAccount = account.replaceAll(getOacct(), nextAccount);
            setNacct(nextAccount);
            if (debug > 0) {
                System.out.println(" ! nextAccount = " + nextAccount);
            }
        } else {
            if (debug > 0) {
                System.out.println(" ?");
            }
        }
        return result;
    } // matches

} // IBANRuleBean
