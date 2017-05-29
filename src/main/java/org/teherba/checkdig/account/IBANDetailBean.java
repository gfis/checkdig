/*  IBANDetailBean.java - Country specific data for checking of IBAN subfields
 *  @(#) $Id: IBANDetailBean.java 37 2008-09-08 06:11:04Z gfis $
    2017-05-29: javadoc 1.8
 *  2014-01-20: LF, no tabs
 *  2007-12-31: Georg Fischer
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
package org.teherba.checkdig.account;
import  org.teherba.checkdig.account.IBANDetailBeanBase;

/** Country specific properties for checking of IBAN subfields.
 *  These properties may be used to check the internal country-specific structure of
 *  an IBAN, and to determine the corresponding SWIFT BIC of the bank.
 *  All IBANs start with a 2 letter ISO country code, followed
 *  by 2 numeric checkdigits calculated by a mod 97 algorithm.
 *  All starting positions count from 0.
 *  <p>
 *	See also the
 *  <a href="http://www.swift.com/dsp/resources/documents/IBAN_Registry.pdf">SWIFT IBAN Registry</a>
 *  (77 pages in Jan. 2014).
 */
public class IBANDetailBean extends IBANDetailBeanBase {
    public static final String CVSID = "@(#) $Id: IBANDetailBean.java 37 2008-09-08 06:11:04Z gfis $";

    /** Constructor which sets all properties
     *  @param cc           country code, DE, FR etc.
     *  @param width        total length of IBAN in this country
     *  @param bbidStart    starting position of bank/branch id, 0 based
     *  @param bbidLen      length of bank/branch id
     *  @param acctStart    starting position of account number, 0 based
     *  @param acctLen      length of account number
     *  @param chkMethod    code for check digit computation method
     *  @param chkStart     starting position of check digit(s), 0 based
     *  @param chkLen       length of check digit(s)
     */
    public IBANDetailBean
            ( String cc
            , int   width
            , int   bbidStart
            , int   bbidLen
            , int   acctStart
            , int   acctLen
            , int   chkMethod
            , int   chkStart
            , int   chkLen
            )
    {
        super();
        this.cc         = cc        ;
        this.width      = width     ;
        this.bbidStart  = bbidStart ;
        this.bbidLen    = bbidLen   ;
        this.acctStart  = acctStart ;
        this.acctLen    = acctLen   ;
        this.chkMethod  = chkMethod ;
        this.chkStart   = chkStart  ;
        this.chkLen     = chkLen    ;
    } // constructor

} // IBANDetailBean
