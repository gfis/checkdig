#   ismn_ranges.pl - country groups and publisher ranges in International Standard Numbers (ISMNs)
#   @$Id$
#   generated by etc/isbn/gen_ranges.pl, do not edit here!
#
#   Activation (test data at the end of this source program):
#       perl ismn_ranges.pl group-id
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

    my $CVSID = "\@(#) \$Id: gen_ranges.pl 76 2009-01-09 07:12:30Z gfis ISMNRanges.java 37 2008-09-08 06:11:04Z gfis \$";
    # number of elements in a range tuple */
    my $RANGE_LEN = 3;

    #  Array of 9-digit numbers defining the publisher ranges for all country groups.
    #  The array stores tuples (low number, high number, range-width) for all number ranges,
    #  The first tuple (0, 0, 0) is not used.
    my @RANGES = ( 0, 0, 0 # dummy tuple
                ,         0,   9999999, 3 #    3
                ,  10000000,  39999999, 4 #    6
                ,  40000000,  69999999, 5 #    9
                ,  70000000,  89999999, 6 #   12
                ,  90000000,  99999999, 7 #   15
                    # 3, 5, group      0: General suggestions
                ); # RANGES

    # width of all ISMN numbers, without check digit and EAN bookland code */
    my $WIDTH = 9;

    # number of elements in a group tuple */
    my $GROUP_LEN = 5;

    # Array of group (country) descriptors  as tuples
    # (low number, high number, group-width, range-index, range-count). The latter
    # is an anchor into RANGES where the number of preceeding range tuples
    # can be found.
    # The first tuple is not used.
    my @GROUPS = ( 0, 0, 0, 0, 0 # dummy tuple
                ,         0,  99999999,  1,     3,  5 # group      0: General suggestions
                ); # GROUPS

    # Removes all spaces und punctuation from an ISMN-10 or ISMN-13.

    # @param rawIsbn trim this number, may contain hyphens, dots or spaces
    #
    sub trim() {
        my ($rawIsbn) = @_;
        $rawIsbn =~ s{[\-\. ]}{}g;
        return uc($rawIsbn); # because of trailing "x"
    } # trim

    # Formats an ISMN-10 or ISMN-13 and inserts hyphens after the bookland (if any),
    # the country group, the publisher code and before the check digit.
    # @param rawIsbn format this number, may contain hyphens, dots or spaces
    sub format() {
        my ($rawIsbn) = @_;
        my $result = "";
        my $ismn = &trim($rawIsbn);
        my $ismn9 = "";
        my $check = "-";
        my $groupWidth = 0;
        my $rangeWidth = 0;
        my $num = 300000000;
        #          123456789
        if (0) {
        } elsif (length($ismn) == 10) {
                $ismn9 = substr($ismn, 0,  9); # without check digit
                $check = substr($ismn, 9, 1);
        } elsif (length($ismn) == 13) {
                $result .= (substring($ismn, 0, 3)); # bookland 978, 979
                $result .= ('-');
                $ismn9 = substr($ismn,  3, 9); # without bookland and check digit
                $check = substr($ismn, 12, 1);
        } else {
                $result .= ($ismn);
                $result .= (" ?WLEN");
        } # switch length
        my $test = 'test ';
            $num = ($ismn9);
            my $gindex = $GROUP_LEN; # skip [0]
            while ($gindex < scalar(@GROUPS)) {
                $test .=  "$num < " . $GROUPS[$gindex + 0] . " || $num > " . $GROUPS[$gindex + 1] . "\n";
                if ($num >= $GROUPS[$gindex + 0] && $num <= $GROUPS[$gindex + 1]) { # found
                    $groupWidth = $GROUPS[$gindex + 2];
                    my $rindex  = $GROUPS[$gindex + 3];
                    my $rcount  = $GROUPS[$gindex + 4];
                    $test .=  "[" . $rindex . "]: " . $RANGES[$rindex + 0]
                            . ", " . $RANGES[$rindex + 1] . " - " . $RANGES[$rindex + 2] ."\n";
                    while ($rcount > 0) { # loop2
                        if ($num >= $RANGES[$rindex + 0] && $num <= $RANGES[$rindex + 1]) { # found2
                            $rangeWidth = $RANGES[$rindex + 2];
                            $rcount = 0; # break loop2
                        } # found2
                        $rindex += $RANGE_LEN;
                        $rcount --;
                    } # while rcount
                    $gindex = scalar(@GROUPS); # break loop
                } # found
                $gindex += $GROUP_LEN;
            } # while gindex loop

        if ($groupWidth > 0 && $rangeWidth > 0) {
            $result .= substr($ismn9, 0, $groupWidth);
            $result .= ('-');
            $result .= substr($ismn9, $groupWidth, $rangeWidth);
            $result .= ('-');
            $result .= substr($ismn9, $groupWidth + $rangeWidth);
            $result .= ('-');
            $result .= ($check);
        } else {
            $result .= "$test: $groupWidth, $rangeWidth, $ismn9";
            $result .= ($check);
            $result .= (" ?WRNG");
        }
        return $result;
    } # format

    # Test Frame, prints all ranges for the specified country group.
    # @param args commandline arguments:
    # <ul>
    # <li>args[0] = country group (1-6 digits, default 3x = German speaking countries)
    # </ul>
    #
    # main

    while (<>) {
        s[(ISMN\s*=?\s*)([\-\d\.Xx]{10,20})][$1 . &format($2)]eg;
        print;
    } # while <>

