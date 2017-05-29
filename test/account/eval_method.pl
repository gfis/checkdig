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
#-----------------------------------------------------------------
# evaluate the results of various checkdigit methods
# @(#) $Id: eval_method.pl 37 2008-09-08 06:11:04Z gfis $
# 2005-10-25
#
# Activation:
#   perl eval_method.pl b8.tmp > outfile
#-----------------------------------------------------------------
use strict;

    my %hash; 
    my %hnok; 
    my $key = 0;
    while ($key < 120) {
        my $method = $key;
        if ($key >= 100) {
            $method =~ s[\A10][A];
            $method =~ s[\A11][B];
            $method =~ s[\A12][C];
            $hash{sprintf ("%-2s", $method)} = "0";
            $hnok{sprintf ("%-2s", $method)} = "0";
        }
        else {
            $hash{sprintf ("%02d", $method)} = "0";
            $hnok{sprintf ("%02d", $method)} = "0";
        }
        $key ++;
    }
    while (<>) {
        next if ! m[\A\w];
        my ($method, @rest) = split;
        if (0) {}
        elsif (m[\!]) {
            $hash{$method} ++;
        }
        elsif (m[\?]) {
            $hnok{$method} ++;
        }
    } # while

    my ($sash, $snok) = (0,0);
    foreach $key (sort keys %hash) {
        print "$key " 
            . (($hash{$key}               != 0) ? sprintf ("%8d !", $hash{$key}              ) : ' ' x 10)
            . ((              $hnok{$key} != 0) ? sprintf ("%8d ?", $hnok{$key}              ) : ' ' x 10)
            . (($hash{$key} * $hnok{$key} != 0) ? sprintf ("%8.3f", $hnok{$key} / $hash{$key}) : ' ' x 8)
            . "\n";
        $sash += $hash{$key};
        $snok += $hnok{$key};
    }
    if (1) {
        print "\n" . "sum" 
            . (($sash                     != 0) ? sprintf ("%8d !", $sash                    ) : ' ' x 10)
            . ((              $snok       != 0) ? sprintf ("%8d ?", $snok                    ) : ' ' x 10)
            . (($sash       * $snok       != 0) ? sprintf ("%8.3f", $snok       / $sash      ) : ' ' x 8)
            . "\n";
    }
    
__DATA__
52  0060231299  82054222     sum(52_53)=0 sum(52)=10 !
52  0066029197  15051732     sum(52_53)=6 sum(52)=0 !
52  0067161389  17052302     sum(52_53)=7 sum(52)=5 !
52  0081041014  81053132     sum(52_53)=1 sum(52)=1 !
52  0400077802  17052302     sum(52_53)=0 sum(52)=10 !
52  0405590256  81053272     sum(52_53)=8 sum(52)=9 ? 0408590256
52  0420971017  81053242     sum(52_53)=1 sum(52)=3 ? 0421971017
52  0426056518  13051052     sum(52_53)=3 sum(52)=5 ? 0423056518
52  0470425237  80053612     sum(52_53)=7 sum(52)=5 ? 0477425237
52  1130447770  82054222     sum(52_53)=2 sum(52)=7 ? 1132447770
52  1131238644  82054222     sum(52_53)=7 sum(52)=5 ? 1137238644
52  1131265102  82054222     sum(52_53)=4 sum(52)=4 ? 1134265102
52  4410652198  17052472     sum(52_53)=1 sum(52)=3 ? 4411652198
