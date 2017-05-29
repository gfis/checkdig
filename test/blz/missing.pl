#!/usr/bin/perl

#
#  Copyright 2006 Dr. Georg Fischer <punctum at punctum dot kom>
# 
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
# 
#       http://www.apache.org/licenses/LICENSE-2.0
# 
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#
# find check methods which are not used
# @(#) $Id: missing.pl 37 2008-09-08 06:11:04Z gfis $
# Copyright (c) 2005 Dr. Georg Fischer <punctum@punnctum.com>
# 2005-10-22
#
# activation:
#   perl nolead0.pl infile > outfile
#-----------------------------------------------------------------
use strict;

    my $miss = 0;
    while (<>) {
        $miss ++;
        s[\r?\n][]; # chompr
        s[A][10]i;
        s[B][11]i;
        s[C][12]i;
        my $cm = $_;
        while ($miss < $cm) {
            my $num = $miss;
            if ($num >= 100) {
                $num =~ s[\A10][A];
                $num =~ s[\A11][B];
                $num =~ s[\A12][C];
            }
            print "$num\n";
            $miss ++;
        }
    } # while
    