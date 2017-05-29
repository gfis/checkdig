#!/usr/bin/perl

# @(#) $Id: gen_ranges.pl 76 2009-01-09 07:12:30Z gfis $
# 2017-05-29: generate timestamp; @return
# 2013-12-29: no tabs
# 2008-11-11, Dr. Georg Fischer
# ${TYPE} Prefixes and Publishers Number Ranges
# from http://www.isbn-international.org/converter/ranges.htm
# Group Number  Area    Valid Publisher Numbers
# c.f. also: International ISMN Agency, e-mail: ismn@ismn-international.org
#
# Activation:
#   perl gen_ranges.pl [-perl|-java] [-isbn|-ismn] ranges.txt > outputfile

use strict;

    my ($sec, $min, $hour, $mday, $mon, $year, $wday, $yday, $isdst) = localtime (time);
    $mon += 1;
    $year += 1900;
    my $timestamp   = sprintf ("%04d-%02d-%02dT%02d:%02d:%02d", $year, $mon, $mday, $hour, $min, $sec);

    my $co = "//"; # comment symbol for the output language
    my $L  = "L"; # postfix for long numeric constants
    my $lang = lc(shift(@ARGV));
    my $type = lc(substr(shift(@ARGV), 1));
    my $TYPE = uc($type);
    $lang =~ s{[\W]}{}g;
    if ($lang eq "") { # default: java
        $lang = "java";
    } elsif ($lang eq "perl") {
        $co = "#";
        $L  = "";
    }
    my $width = 9; # width of all ${TYPE}s, without check digit and EAN bookland code
    my @groups = ();
    my $text = "";
    my $line = "";
    my $group;
    my $range_len = 3;
    my $group_len = 5;

    print <<"GFis" if $lang eq "java";
/*  ${TYPE}Ranges.java - country groups and publisher ranges in International Standard Numbers (${TYPE}s)
    \@\$Id\$
    $timestamp: generated by etc/isbn/gen_ranges.pl, do not edit here!

    Activation (test data at the end of this source program):
        java -cp dist/checkdig.jar org.teherba.checkdig.${TYPE}Ranges group-id
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

/** Class for the definition of publisher ranges in International Standard Numbers (${TYPE}s)
 *  \@author Dr. Georg Fischer
 */
public class ${TYPE}Ranges {
    public final static String CVSID = "\@(#) \$Id: gen_ranges.pl 76 2009-01-09 07:12:30Z gfis ${TYPE}Ranges.java 37 2008-09-08 06:11:04Z gfis \$";

    /** No-args constructor
     */
    public ${TYPE}Ranges() {
        super();
    } // constructor

    /** number of elements in a range tuple */
    private static final int RANGE_LEN = $range_len;

    /** Array of 9-digit numbers defining the publisher ranges for all country groups.
     *  The array stores tuples (low number, high number, range-width) for all number ranges,
     *  The first tuple (0, 0, 0) is not used.
     */
    private static long[] RANGES = new long[] { 0$L, 0$L, 0$L // dummy tuple
GFis
    print <<"GFis" if $lang eq "perl";
#   ${type}_ranges.pl - country groups and publisher ranges in International Standard Numbers (${TYPE}s)
#   \@\$Id\$
#   $timestamp: generated by etc/isbn/gen_ranges.pl, do not edit here!
#
#   Activation (test data at the end of this source program):
#       perl ${type}_ranges.pl group-id
#-------------------------------------------------------------------
# * Copyright 2008 Dr. Georg Fischer <punctum at punctum dot kom>
# *
# * Licensed under the Apache License, Version 2.0 (the "License");
# * you may not use this file except in compliance with the License.
# * You may obtain a copy of the License at
# *
# *      http://www.apache.org/licenses/LICENSE-2.0
# *
# * Unless required by applicable law or agreed to in writing, software
# * distributed under the License is distributed on an "AS IS" BASIS,
# * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# * See the License for the specific language governing permissions and
# * limitations under the License.
#
use strict;

    my \$CVSID = "\\\@(#) \\\$Id: gen_ranges.pl 76 2009-01-09 07:12:30Z gfis ${TYPE}Ranges.java 37 2008-09-08 06:11:04Z gfis \\\$";
    # number of elements in a range tuple */
    my \$RANGE_LEN = $range_len;

    #  Array of 9-digit numbers defining the publisher ranges for all country groups.
    #  The array stores tuples (low number, high number, range-width) for all number ranges,
    #  The first tuple (0, 0, 0) is not used.
    my \@RANGES = ( 0$L, 0$L, 0$L # dummy tuple
GFis
    my $tcount = 0; # number of tuples for current group
    my $tindex = 3; # points to start of current tuple
    my $rowno = 1; # current tuple index

    while (<>) { # was <DATA>
        next if ! m{\A\d};
        s{\r?\n}{};
        $line = $_;
        if ($line =~ m{[a-zA_Z]}) { # start of group - country line
            &close_group(0);
        }
        # now the line has "low - high"
        $line =~ m{(\d+)\s*\-\s*(\d+)};
        my ($low, $high) = ($1, $2);
        print "                , "
                . &fill($group . $low, '0') . ", " . &fill($group . $high, '9')
                . ", " . length($high) . "$L $co" . sprintf("%5d", $rowno * $range_len) . "\n";
        $tcount ++;
        $rowno ++;
    } # DATA
    &close_group(1);
    print <<"GFis" if $lang eq "java";
                }; $co RANGES
GFis
    print <<"GFis" if $lang eq "perl";
                ); $co RANGES
GFis
    print <<"GFis" if $lang eq "java";

    /** width of all ${TYPE} numbers, without check digit and EAN bookland code */
    private static final int WIDTH = 9;

    /** number of elements in a group tuple */
    private static final int GROUP_LEN = $group_len;

    /** Array of group (country) descriptors  as tuples
     *  (low number, high number, group-width, range-index, range-count). The latter
     *  is an anchor into RANGES where the number of preceeding range tuples
     *  can be found.
     *  The first tuple is not used.
     */
    private static long[] GROUPS = new long[] { 0$L, 0$L, 0$L, 0$L, 0$L // dummy tuple
GFis
    print <<"GFis" if $lang eq "perl";

    # width of all ${TYPE} numbers, without check digit and EAN bookland code */
    my \$WIDTH = 9;

    # number of elements in a group tuple */
    my \$GROUP_LEN = $group_len;

    # Array of group (country) descriptors  as tuples
    # (low number, high number, group-width, range-index, range-count). The latter
    # is an anchor into RANGES where the number of preceeding range tuples
    # can be found.
    # The first tuple is not used.
    my \@GROUPS = ( 0$L, 0$L, 0$L, 0$L, 0$L # dummy tuple
GFis
    shift(@groups);
    print join("", @groups);
    print <<"GFis" if $lang eq "java";
                }; $co GROUPS
GFis
    print <<"GFis" if $lang eq "perl";
                ); $co GROUPS
GFis
    print <<"GFis" if $lang eq "java";

    /** Prints the ranges for the specified country group.
     *  \@param group print ranges for this group
     */
    protected void print(String group) {
        long grp = 300000000$L;
        //         123456789
        try {
            grp = Long.parseLong(this.trim(group));
        } catch (Exception exc) {
            // ignore, default set above
        }
        int igrp = GROUP_LEN; // skip [0]
        while (igrp < GROUPS.length) {
            if (grp >= GROUPS[igrp + 0] && grp <= GROUPS[igrp + 1]) { // found
                int rindex = (int) GROUPS[igrp + 3];
                int rcount = (int) GROUPS[igrp + 4];
                while (rcount > 0) {
                    System.out.println("[" + rindex + "]: " + RANGES[rindex + 0] + ", " + RANGES[rindex + 1] + " - " + RANGES[rindex + 2]);
                    rindex += RANGE_LEN;
                    rcount --;
                } // while rcount
                igrp = GROUPS.length; // break loop
            } // found
            igrp += GROUP_LEN;
        } // while igrp
    } // print
GFis
    print <<"GFis" if $lang eq "java";

    /** Removes all spaces und punctuation from an ${TYPE}-10 or ${TYPE}-13.
     *  \@param rawIsbn trim this number, may contain hyphens, dots or spaces
     *  \@return trimmed number
     */
    public String trim(String rawIsbn) {
        return rawIsbn.replaceAll("[\\\\-\\\\. ]", "").toUpperCase(); // because of trailing "x"
    } // trim

    /** Formats an ${TYPE}-10 or ${TYPE}-13 and inserts hyphens after the bookland (if any),
     *  the country group, the publisher code and before the check digit.
     *  \@param rawIsbn format this number, may contain hyphens, dots or spaces
     *  \@return formatted number
     */
    public String format(String rawIsbn) {
        StringBuffer result = new StringBuffer();
        String ${type} = this.trim(rawIsbn);
        String ${type}9 = "";
        String check = "-";
        int groupWidth = 0;
        int rangeWidth = 0;
        long num = 300000000L;
        //         123456789
        switch (${type}.length()) {
            case 10:
                ${type}9 = ${type}.substring( 0,  9); // without check digit
                check = ${type}.substring( 9, 10);
                break;
            case 13:
                result.append(${type}.substring(0, 3)); // bookland 978, 979
                result.append('-');
                ${type}9 = ${type}.substring( 3, 12); // without bookland and check digit
                check = ${type}.substring(12, 13);
                break;
            default:
                result.append(${type});
                result.append("/wrong_length");
        } // switch length
        try {
            num = Long.parseLong(${type}9);
            int gindex = GROUP_LEN; // skip [0]
            while (gindex < GROUPS.length) {
                if (num >= GROUPS[gindex + 0] && num <= GROUPS[gindex + 1]) { // found
                    groupWidth = (int) GROUPS[gindex + 2];
                    int rindex = (int) GROUPS[gindex + 3];
                    int rcount = (int) GROUPS[gindex + 4];
                    while (rcount > 0) { // loop2
                    /*
                        System.out.println("[" + rindex + "]: " + RANGES[rindex + 0]
                                + ", " + RANGES[rindex + 1] + " - " + RANGES[rindex + 2]);
                    */
                        if (num >= RANGES[rindex + 0] && num <= RANGES[rindex + 1]) { // found2
                            rangeWidth = (int) RANGES[rindex + 2];
                            rcount = 0; // break loop2
                        } // found2
                        rindex += RANGE_LEN;
                        rcount --;
                    } // while rcount
                    gindex = GROUPS.length; // break loop
                } // found
                gindex += GROUP_LEN;
            } // while gindex loop
        } catch (Exception exc) {
            // ignore, default set above
        }
        if (groupWidth > 0 && rangeWidth > 0) {
            result.append(${type}9.substring(0, groupWidth));
            result.append('-');
            result.append(${type}9.substring(groupWidth, groupWidth + rangeWidth));
            result.append('-');
            result.append(${type}9.substring(            groupWidth + rangeWidth));
            result.append('-');
            result.append(check);
        } else {
            result.append(${type}9);
            result.append(check);
            result.append(' ');
            result.append(BaseChecker.WRONG_RANGE);
        }
        return result.toString();
    } // format
GFis
    print <<"GFis" if $lang eq "perl";

    # Removes all spaces und punctuation from an ${TYPE}-10 or ${TYPE}-13.

    # \@param rawIsbn trim this number, may contain hyphens, dots or spaces
    #
    sub trim() {
        my (\$rawIsbn) = \@_;
        \$rawIsbn =~ s{[\\-\\. ]}{}g;
        return uc(\$rawIsbn); # because of trailing "x"
    } # trim

    # Formats an ${TYPE}-10 or ${TYPE}-13 and inserts hyphens after the bookland (if any),
    # the country group, the publisher code and before the check digit.
    # \@param rawIsbn format this number, may contain hyphens, dots or spaces
    sub format() {
        my (\$rawIsbn) = \@_;
        my \$result = "";
        my \$${type} = &trim(\$rawIsbn);
        my \$${type}9 = "";
        my \$check = "-";
        my \$groupWidth = 0;
        my \$rangeWidth = 0;
        my \$num = 300000000;
        #          123456789
        if (0) {
        } elsif (length(\$${type}) == 10) {
                \$${type}9 = substr(\$${type}, 0,  9); # without check digit
                \$check = substr(\$${type}, 9, 1);
        } elsif (length(\$${type}) == 13) {
                \$result .= (substring(\$${type}, 0, 3)); # bookland 978, 979
                \$result .= ('-');
                \$${type}9 = substr(\$${type},  3, 9); # without bookland and check digit
                \$check = substr(\$${type}, 12, 1);
        } else {
                \$result .= (\$${type});
                \$result .= (" ?WLEN");
        } # switch length
        my \$test = 'test ';
            \$num = (\$${type}9);
            my \$gindex = \$GROUP_LEN; # skip [0]
            while (\$gindex < scalar(\@GROUPS)) {
                \$test .=  "\$num < " . \$GROUPS[\$gindex + 0] . " || \$num > " . \$GROUPS[\$gindex + 1] . "\\n";
                if (\$num >= \$GROUPS[\$gindex + 0] && \$num <= \$GROUPS[\$gindex + 1]) { # found
                    \$groupWidth = \$GROUPS[\$gindex + 2];
                    my \$rindex  = \$GROUPS[\$gindex + 3];
                    my \$rcount  = \$GROUPS[\$gindex + 4];
                    \$test .=  "[" . \$rindex . "]: " . \$RANGES[\$rindex + 0]
                            . ", " . \$RANGES[\$rindex + 1] . " - " . \$RANGES[\$rindex + 2] ."\\n";
                    while (\$rcount > 0) { # loop2
                        if (\$num >= \$RANGES[\$rindex + 0] && \$num <= \$RANGES[\$rindex + 1]) { # found2
                            \$rangeWidth = \$RANGES[\$rindex + 2];
                            \$rcount = 0; # break loop2
                        } # found2
                        \$rindex += \$RANGE_LEN;
                        \$rcount --;
                    } # while rcount
                    \$gindex = scalar(\@GROUPS); # break loop
                } # found
                \$gindex += \$GROUP_LEN;
            } # while gindex loop

        if (\$groupWidth > 0 && \$rangeWidth > 0) {
            \$result .= substr(\$${type}9, 0, \$groupWidth);
            \$result .= ('-');
            \$result .= substr(\$${type}9, \$groupWidth, \$rangeWidth);
            \$result .= ('-');
            \$result .= substr(\$${type}9, \$groupWidth + \$rangeWidth);
            \$result .= ('-');
            \$result .= (\$check);
        } else {
            \$result .= "\$test: \$groupWidth, \$rangeWidth, \$${type}9";
            \$result .= (\$check);
            \$result .= (" ?WRNG");
        }
        return \$result;
    } # format
GFis
    print <<"GFis" if $lang eq "java";

    /** Test Frame, prints all ranges for the specified country group.
     *  \@param args commandline arguments:
     *  <ul>
     *  <li>args[0] = country group (1-6 digits, default 3x = German speaking countries)
     *  </ul>
     */
    public static void main (String args[]) {
        try {
            ${TYPE}Ranges ranges = new ${TYPE}Ranges();
            String group = "300000000";
            if (args.length > 0) {
                String ${type} = args[0];
                System.out.println(ranges.format(${type}));
            } else {
                ranges.print(group);
            }
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            exc.printStackTrace();
        } // catch
    } // main

} // ${TYPE}Ranges

GFis
    print <<"GFis" if $lang eq "perl";

    # Test Frame, prints all ranges for the specified country group.
    # \@param args commandline arguments:
    # <ul>
    # <li>args[0] = country group (1-6 digits, default 3x = German speaking countries)
    # </ul>
    #
    # main

    while (<>) {
        s[(${TYPE}\\s*=?\\s*)([\\-\\d\\.Xx]{10,20})][\$1 . &format(\$2)]eg;
        print;
    } # while <>

GFis

sub close_group {
        my ($last) = @_; # 1 behind last row
        if ($tcount > 0) {
            print "                    $co ${tindex}$L, ${tcount}$L, $text\n";
        }
        $text =~m[group\s*(\d+)];
        $group = $1;
        push @groups, "                , "
                . &fill($group, '0') . ", "
                . &fill($group, '9') . ", "
                . sprintf("%2d$L, %5d$L, %2d$L ", length($group), $tindex, $tcount)
                . "$co $text\n";
        my $range;
        if ($line =~ m[\t\Z]) {
            $line .= '0 - 9';
        }
        ($group, $text, $range) = split(/\t/, $line);
        $text = sprintf("group %6d: $text", $group);
        $line = $range;
        $tcount = 0;
        $tindex = $rowno * $range_len;
    } # close_group

sub fill {
        my ($num, $ch) = @_;
        $num = $num . ($ch x ($width - length($num))) . $L;
        $num =~ s[\A(0{1,8})][' ' x length($1)]e; # trim leading zeroes, keep 0L
        return $num;
    } # fill

__DATA__
0   English speaking area   00 - 19
200 - 699
7000 - 8499
85000 - 89999
900000 - 949999
9500000 - 9999999
1   English speaking area   00 - 09
100 - 399
4000 - 5499
55000 - 86979
869800 - 998999
2   French speaking area    00 - 19
200 - 349
35000 - 39999
...

