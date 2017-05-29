/*  Messages.java - Static help texts and other language specific messages.
 *  @(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $
 *  2017-05-29: javadoc 1.8
 *  2016-08-31: Dr. Georg Fischer: copied from Xtool; Rainer=74
 */
/*
 * Copyright 2016 Dr. Georg Fischer <punctum at punctum dot kom>
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
package org.teherba.checkdig.web;
import  org.teherba.common.web.BasePage;
import  java.io.Serializable;

/** Language specific message texts and formatting for the web user interface.
 *  Currently implemented natural languages (denoted by 2-letter ISO <em>country</em> codes) are:
 *  <ul>
 *  <li>en - English</li>
 *  <li>de - German</li>
 *  </ul>
 *  @author Dr. Georg Fischer
 */
public class Messages implements Serializable {
    public final static String CVSID = "@(#) $Id: 57d01d0860aef0c2f2783647be70c3c381710c86 $";

    /** No-args Constructor
     */
    public Messages() {
    } // Constructor

    /** Sets the application-specific error message texts
     *  @param basePage reference to the hash for message texts
     */
    public static void addMessageTexts(BasePage basePage) {
        String appLink = "<a href=\"servlet?view=index\">" + basePage.getAppName() + "</a>";
        //--------
        basePage.add("en", "001", appLink);
        basePage.add("en", "002"
                , " <a href=\"mailto:punctum@punctum.com"
                + "?&subject=" + basePage.getAppName()
                + "\">Dr. Georg Fischer</a>"
                );
        //--------
        String laux = basePage.LANG_AUX;  // pseudo language code for links to auxiliary information
        int imess   = basePage.START_AUX; // start of messages    for links to auxiliary information
        String smess = null;
    /*
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"main\"        href=\"servlet?view=index\">");
        basePage.add("en", smess, "{parm}CheckDig</a> Main Page");
        basePage.add("de", smess, "{parm}CheckDig</a>-Startseite");
    */
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"account\"     href=\"spec/de_account.xml\">");
        basePage.add("en", smess, "{parm}XML definition</a> of <br />&nbsp;&nbsp;German account check methods");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"xsl\"         href=\"xslt/account.xsl\">");
        basePage.add("en", smess, "{parm}Stylesheet</a> generating<br />&nbsp;&nbsp;corresponding Java Methods");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"deblz\"       href=\"servlet?spec=blz/blz_search\">");
        basePage.add("en", smess, "{parm}German BLZ search</a>");

        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"wiki\"        href=\"http://www.teherba.org/index.php/CheckDig\" target=\"_new\">");
        basePage.add("en", smess, "{parm}Wiki</a> Documentation");
        basePage.add("de", smess, "{parm}Wiki</a>-Dokumentation");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"github\"      href=\"https://github.com/gfis/checkdig\" target=\"_new\">");
        basePage.add("en", smess, "{parm}Git Repository</a>");
        basePage.add("de", smess, "{parm}Git Repository</a>");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"api\"         href=\"docs/api/index.html\">");
        basePage.add("en", smess, "{parm}Java API</a> Documentation");
        basePage.add("de", smess, "{parm}Java API</a>-Dokumentation");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"manifest\"    href=\"servlet?view=manifest\">");
        basePage.add("en", smess, "{parm}Manifest</a>, ");
        basePage.add("de", smess, "{parm}Manifest</a>, ");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"license\"     href=\"servlet?view=license\">");
        basePage.add("en", smess, "{parm}License</a>, ");
        basePage.add("de", smess, "{parm}Lizenz</a>, ");
        smess = String.format("%03d", imess ++);
        basePage.add(laux, smess, "<a title=\"notice\"      href=\"servlet?view=notice\">");
        basePage.add("en", smess, "{parm}References</a>");
        basePage.add("de", smess, "{parm}Referenzen</a>");
        //--------
        basePage.add("en", "401", "Invalid {parm} <em>{par2}</em>");
        basePage.add("en", "405", "No input file specified");
        basePage.add("en", "406", "Unknown tool");
        //--------
    } // addMessageTexts

    //================
    // Main method
    //================

    /** Test driver
     *  @param args language code: "en", "de"
     */
    public static void main(String[] args) {
        Messages help = new Messages();
        System.out.println("no messages");
    } // main

} // Messages
