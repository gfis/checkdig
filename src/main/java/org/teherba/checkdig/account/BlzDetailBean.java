/*  BlzDetailBean.java - specific data for a German Bankleitzahl record
 *  @(#) $Id: BlzDetailBean.java 37 2008-09-08 06:11:04Z gfis $
    2017-05-29: javadoc 1.8
 *  2014-01-20: Georg Fischer
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
import  org.teherba.checkdig.account.BlzDetailBeanBase;
import  java.sql.ResultSet;

/** Specific properties for a German Bankleitzahl.
 *  <p>
 *  See also the document
 *  <a href="http://www.bundesbank.de/Navigation/DE/Kerngeschaeftsfelder/Unbarer_Zahlungsverkehr/Bankleitzahlen/bankleitzahlen.html">Bankleitzahlen</a>
 *  (77 pages in Jan. 2014).
 */
public class BlzDetailBean extends BlzDetailBeanBase {
    public static final String CVSID = "@(#) $Id: BlzDetailBean.java 37 2008-09-08 06:11:04Z gfis $";

    /** No-args Constructor
     */
    public BlzDetailBean() {
        // set non-null properties
        setBlz        ("");
        setFuehrend   ("");
        setInstitut   ("");
        setPlz        ("");
        setOrt        ("");
        setInst       ("");
        setInstNr     ("");
        setBic        ("");
        setPzMethode  ("");
        setSatzNr     ("");
        setAendKnz    ("");
        setLoeschung  ("");
        setFolgeBlz   ("");
        setIbanRegel  ("");
        setIbanVersion("");
        setOkey       ("");
    } // Contructor(0)

    /** Gets the SQL which reads the bean properties,
     *  and which corresponds to {@link #fillFromRow}.
     *  @return SQL SELECT statement with FROM and start of WHERE clause
     */
    public String getSelectSQL() {
        return  ( "SELECT b.blz    "
                + ", b.fuehrend    "
                + ", b.institut    "
                + ", b.plz         "
                + ", b.ort         "
                + ", b.inst        "
                + ", b.instNr      "
                + ", b.bic         "
                + ", b.pzMethode   "
                + ", b.satznr      "
                + ", b.aendKnz     "
                + ", b.loeschung   "
                + ", b.folgeBlz    "
                + ", b.ibanRegel   "
                + ", b.ibanVersion "
                + ", a.okey        "
                + " FROM bankleitzahlen b, iban_access a "
                + " WHERE b.ibanRegel = a.orid "
                + "   AND b.blz " // = ...
                );
    } // getSelectSQL


    /** Fills the bean properties from the current row in a result set.
     *  @param resultSet read from this ResultSet
     */
    public void fillFromRow(ResultSet resultSet) {
        try {
            int ipar = 1;
            setBlz          (resultSet.getString(ipar ++));
            setFuehrend     (resultSet.getString(ipar ++));
            setInstitut     (resultSet.getString(ipar ++));
            setPlz          (resultSet.getString(ipar ++));
            setOrt          (resultSet.getString(ipar ++));
            setInst         (resultSet.getString(ipar ++));
            setInstNr       (resultSet.getString(ipar ++));
            setBic          (resultSet.getString(ipar ++));
            setPzMethode    (resultSet.getString(ipar ++));
            setSatzNr       (resultSet.getString(ipar ++));
            setAendKnz      (resultSet.getString(ipar ++));
            setLoeschung    (resultSet.getString(ipar ++));
            setFolgeBlz     (resultSet.getString(ipar ++));
            setIbanRegel    (resultSet.getString(ipar ++));
            setIbanVersion  (resultSet.getString(ipar ++));
            setOkey         (resultSet.getString(ipar ++));
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
        }
    } // fillFromRow

} // BlzDetailBean
