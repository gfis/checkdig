/*  SOAP Client which calls CheckService
    @(#) $Id: CheckClient.java 71 2009-01-08 20:33:41Z gfis $
	2008-12-10: use service.properties
	2008-11-06: -isbn -ismn -issn -pnd
    2005-08-26, Dr. Georg Fischer
*/
/*
 * Copyright 2001-2004 The Apache Software Foundation.
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
import  org.apache.axis.client.Call;
import  org.apache.axis.client.Service;
import  java.io.File;
import  java.io.FileInputStream;
import  java.util.Properties;
import  javax.xml.namespace.QName;

/** SOAP client sample program.
 */
public class CheckClient {
    public final static String CVSID = "@(#) $Id: CheckClient.java 71 2009-01-08 20:33:41Z gfis $";

    /** Activates the <em>CheckService</em> via SOAP
     *  @param args arguments on the commandline:
     *  <ul>
     *  <li>function to be activated: account, vatid, iban ...</li>
     *  <li>primary entity to be checked: account number, VAT Id, IBAN</li>
     *  <li>additional parameter (e.g. bankleitzahl, method)</li>
     *  </ul>
     */
    public static void main(String [] args) {
        try {
	        Properties props = new Properties();
    	    String propsName = "service.properties";
    	    props.load(CheckClient.class.getClassLoader().getResourceAsStream(propsName)); // (1) load from classpath (jar)
    	    File propsFile = new File(propsName);
    	    if (propsFile.exists()) {
    	        props.load(new FileInputStream(propsFile)); // (2) add any properties from a file in the current directory
    	    }
        	String axisURL = props.getProperty("axis_url", "http://localhost:8180/axis");

            String   endpoint = axisURL + "/services/CheckService";
            Service  service  = new Service();
            Call     call     = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName(new QName(axisURL, "getResponse"));
            call.addParameter("function", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            call.addParameter("parm1"   , org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            call.addParameter("parm2"   , org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
            call.setReturnType(org.apache.axis.Constants.XSD_STRING);
            
            String function = "isbn";
            String parm1    = "";
            String parm2    = "";
            int iargs = 0;
            if (iargs < args.length) {
                function = args[iargs ++];
                if (iargs < args.length) {
                    parm1    = args[iargs ++];
                    while (iargs < args.length) {
                        parm2  += args[iargs ++] + " ";
                    }
                }
            }
            String ret = (String) call.invoke( new Object[] { function, parm1, parm2 } );
            System.out.println(ret);
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        } // try
    } // main

} // CheckClient
