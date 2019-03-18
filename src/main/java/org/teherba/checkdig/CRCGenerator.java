/*  Read a CRC generator polynomial and write a table
    @(#) $Id: CRCGenerator.java 37 2008-09-08 06:11:04Z gfis $
    2019-03-17: decimal output, commandline switches
    2016-10-12: less imports
    2007-04-23, Georg Fischer: rewritten in Java
    1988-01-23: previous version in Turbo-Pascal
    punctum Gesellschaft fuer Software mbH
    Ringseisstrasse 10a
    D-8000 Muenchen 2, West Germany
    Tel. (0049) 89 532741
    --------------------------------------------------------
    Literature:

    Perez, Aram: Byte-wise CRC Calculations
    IEEE Micro, June 1983, pp. 40-50.

    Morse, Greg: Calculating CRCs by Bits && Bytes
    BYTE, Sept. 1986, pp. 115-124.

    test messages (SDLC initialized with: ones  zeroes )
    T                                     1B26  14A1
    THE                                   44BE  7D8D
    THE,QUICK,BROWN,FOX,0123456789        DF91  7DC5
    after transmission of the CRC         F0B8  F0B8
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
import  java.io.FileInputStream;
import  java.io.BufferedReader;
import  java.nio.channels.Channels;
import  java.nio.channels.ReadableByteChannel;

/** Read a CRC generator polynomial and write a table
 *  useable for bytewise calculation of the CRC remainder,
 *  and execute test cases.
 */
public class CRCGenerator {
    public final static String CVSID = "@(#) $Id: CRCGenerator.java 37 2008-09-08 06:11:04Z gfis $";

    private static final int MAX_TABLE = 256;

    /** Gets the lower byte of a 16-bit value
     *  @param value some integer
     *  @return the low byte
     */
    private static int lsByte(int value) {
        return value & 0xff;
    } // lsByte

    /** Gets the higher byte of a 16-bit value
     *  @param value some integer
     *  @return the high byte
     */
    private static int msByte(int value) {
        return (value >> 8) & 0xff;
    } // msByte

    /** Gets a byte from a character
     *  @param value some character
     *  @return the byte
     */
    private static byte ord(char value) {
        return (byte) (value & 0xff);
    } // ord

    /** Gets a character from a integer
     *  @param value some byte
     *  @return the character
     */
    private static char chr(int value) {
        return (char) (value & 0xff);
    } // chr

    /** Main program, processes the commandline arguments
     *  @param args see printout below.
     */
    public static void main(String args[]) {
        int  iarg; // index for arguments
        int  table[] = new int[1 + MAX_TABLE];
        char ch, ch2;
        int  crc;
        int  x;
        int  hib = 17;
        int  [][] c = new int[1 + hib][1 + hib]; // cf. Perez
        int  [][] m = new int[1 + hib][1 + hib];
        int  []   polynomial = new int[1 + hib]; // polynomial bits
        int  order; // order of 'g' (usually 16)
        int  i,j,k; // loop variables
        int  shiftNo = 8; // number of shifts for 1 byte (usually 8)
        int  initBits = 0; // bits for initialization of CRC
        int  t,v;  // resulting table element
        char sel; // which polynomial was selected

        // get generating polynomial exponents from args, or show possible polynomials
        if (args.length == 0) { // no argument => print usage
            System.out.println("Usage: \n"
                    + "\t" + "java -cp dist/checkdig.jar org.teherba.checkdig.CRCGenerator [options] inputfile > output\n"
                    + "\t" + "java -cp dist/checkdig.jar org.teherba.checkdig.CRCGenerator 16 12 5 0 -1 test/crc.tst\n"
                    + "\t" + "Options:\n"
                    + "\t" + "  -p polynomial's exponents, e.g. \"16,12,5,0\"\n"
                    + "\t" + "  -i initialization: -i0 => 0, -i1 => 0xffff\n"
                    + "\t" + "  -x hexadecimal output of the table\n"
                    + "\t" + "Common polynomials are:\n"
                    + "\t" + "  CRC-16              x^16 + x^15 + x^2 + 1\n"
                    + "\t" + "  SDLC (IBM, CCITT)   x^16 + x^12 + x^5 + 1\n"
                    + "\t" + "  CRC-16 reverse      x^16 + x^14 + x^1 + 1\n"
                    + "\t" + "  SDLC reverse        x^16 + x^11 + x^4 + 1\n"
                    + "\t" + "  CRC-12              x^12 + x^11 + x^3 + x^2 + x^1 + 1\n"
                    + "\t" + "  LRCC-16             x^16 + 1\n"
                    + "\t" + "  LRCC-8              x^8  + 1\n"
                    + "\t" + "The exponents can be entered in any order and with any non-digit separators.\n"
                    );
                    
        } else { // with arguments
            // initialize arrays
            for (i = 0; i <= hib; i++) {
                polynomial[i] = 0;
                for (j = 0; j <= hib; j++) {
                    c[i][j] = 0;
                    m[i][j] = 0;
                } // for (j
                // first index is register no, second is participating operand
                c[i][i] = 1;
            } // for i
    
            // extract the exponents from 'args' and determine maximum (order) 'n'
            // numbers may be entered in any order
            order          = -1;
            int exponent   = 0;
            boolean xprint = false; // whether to print table in hexadecimal
            iarg = 0;
            boolean busy = true;
            while (busy && iarg < args.length) {
                if (false) {
                } else if (args[iarg].startsWith("-i")) { // initialization bit
                    if (args[iarg].matches("0")) {
                        initBits = 0x0000;
                    } else {
                        initBits = 0xffff;
                    }
                } else if (args[iarg].equals   ("-x")) { // hex output
                    xprint = true;
                } else if (args[iarg].equals   ("-p")) { // polynomial
                    String[] expos = args[++ iarg].split("\\D+");
                    int iexpo = 0;
                    while (iexpo < expos.length) {
                        if (expos[iexpo].length() > 0) { // non-empty
                            try {
                                exponent = Integer.parseInt(expos[iexpo]);
                                polynomial[exponent] = 1;
                                if (exponent > order) {
                                    order = exponent;
                                }
                            } catch (Exception exc) { // non-numeric 
                                iexpo = expos.length; // break loop
                            }
                        } // non-empty          
                        iexpo ++;
                    } // while iexpo
                } else if (args[iarg].startsWith("-")) { // polynomial
                    System.err.println("invalid option " + args[iarg] + "\n");
                } else { 
                	busy = false; // stop reading arguments
                	iarg --; // c.f. below
                }
                iarg ++;
            } // while arguments
                
            // compute 'shiftNo' shifts symbolically
            for (k = 1; k <= shiftNo; k++) {
                for (j = 1; j <= order; j++) {
                    // cm[0] is a copy of cm[1]
                    c[0][j] = c[1][j];
                    m[0][j] = m[1][j];
                }
                m[0][k] = m[0][k] ^ 1;
                for (i = 1; i <= order; i++) {
                    if (i != order) {
                        for (j = 1; j <= order; j++) {
                            c[i][j] = c[i+1][j] ^ (c[0][j] * polynomial[16-i]);
                            m[i][j] = m[i+1][j] ^ (c[0][j] * polynomial[16-i]);
                        }
                    } else {
                        for (j = 1; j <= order; j++) {
                            c[i][j] = c[0][j];
                            m[i][j] = m[0][j];
                        }
                    }
                } // for i
            } // for k
    
            String indent = "    ";
            System.out.println(indent + "/*");
            // write the formulas for vector v
            for (i = 1; i <= order; i++) {
                System.out.print(indent + "    v[" + (i < 10 ? " " : "") + i + "] =  0");
                for (j = 1; j <= order; j++) {
                    if ((c[i][j] > 0) && (m[i][j] > 0)) {
                        System.out.print(" ^ x" + j);
                    }
                }
                System.out.println(';');
            } // for i
            System.out.println(indent + "*/");
    
            System.out.println(indent + "public static final int [] TABLE = new int[256]");
            System.out.print(indent + "    ");
            // evaluate the formula for x = 0 ... 255 and fill the table
            for (x = 0; x < 256; x ++) {
                t = 0;
                for (i = 1; i <= order; i++) {
                    v = 0;
                    for (j = 1; j <= order; j++) {
                        if ((c[i][j] > 0) && (m[i][j] > 0)) {
                            // ... ^ xj
                            v = v ^ ((x >> (j-1)) & 1);
                        }
                    }
                    t = t | (v << (i-1));
                } // for (i
                table[x] = t;
                System.out.print(x == 0 ? "{ " : ", ");
            /*
                String hex = Integer.toHexString(t & 0xffff);
                hex = "00000000".substring(0, 4 - hex.length()) + hex;
                System.out.print("0x" + hex);
            */
            	System.out.print(xprint ? String.format("0x%04x", t & 0xffff) : t & 0xffff);
                if ((x % 8) == 7) {
                    System.out.println();
                    System.out.print(indent + "    ");
                }
            } // for x
            System.out.println("};");
    
            try {
                ReadableByteChannel source = iarg < args.length
                        ? (new FileInputStream (args[iarg ++])).getChannel()
                        : Channels.newChannel(System.in);
                BufferedReader reader = new BufferedReader(Channels.newReader(source, "ISO-8859-1"));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    System.out.println(line);
                    crc = initBits;
                    int pos = 0;
                    while (pos < line.length()) {
                        ch = line.charAt(pos);
                        x   = lsByte(crc) ^ ord(ch);
                        crc = (crc >> 8) ^ (table[x]);
                        pos ++;
                    } // while pos
                    System.out.print("send 0x" + Integer.toHexString(crc));
                    // send 1COM(crc) with LSB first
                    ch  = chr(lsByte(crc) ^ 0xFF);
                    ch2 = chr(msByte(crc) ^ 0xFF);
                    x   = (lsByte(crc) ^ ord(ch)) & 0xff;
                    crc = (crc >> 8) ^ (table[x]);
                    ch  = ch2;
                    x   = (lsByte(crc) ^ ord(ch)) & 0xff;
                    crc = (crc >> 8) ^ (table[x]);
                    System.out.println(", check for 0x" + Integer.toHexString(crc));
                } // while not EOF
                reader.close();
            } catch(Exception exc) {
                System.err.println(exc.getMessage());
                exc.printStackTrace();
            }
        } // with arguments
    } // main
} // CRCGenerator
