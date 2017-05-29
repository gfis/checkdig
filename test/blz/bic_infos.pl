#!/usr/bin/perl

# Generate tab separated data to be loaded in the 'infos' table
# blz8; "blz"; name of bank; method
# @(#) $Id: bic_infos.pl 806 2011-09-20 16:53:03Z gfis $
# 2011-09-20: UNDEFBIC, tab separated
# 2007-04-18: copied from gen_blzmap.pl
#
# Activation:
#   perl blz_infos.pl infile > outfile
#-----------------------------------------------------------------
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
use strict;

    while (<>) {
		my $blz  = substr($_,   0,  8);
		my $bic  = substr($_, 139,  8);
		my $meth = substr($_, 150,  2);
		if ($bic !~ m{\w{8}}) {
			print STDERR "undefined bic=\"$bic\", blz=\"$blz\"\n";
			$bic = "UNDEFBIC";
		}
		print join("\t", ($blz, "bic", $bic, $meth)), "\n";
    } # while
__DATA__
0        10        20        30        40        50        60        70        80        90       100       110       120       130       140       150       160       170        80        90
01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789
100000001Bundesbank                                                10591Berlin                             BBk Berlin                 20100MARKDEF110009011380U000000000
100100101Postbank                                                  10916Berlin                             Postbank Berlin            10010PBNKDEFF10024000538U000000000
100101111SEB                                                       10789Berlin                             SEB Berlin                 25805ESSEDE5F10013005361U000000000
100102221ABN AMRO Bank Ndl Deutschland                             10105Berlin                             ABN AMRO Bank Berlin       29190ABNADEFFBER10011142U000000000
100104241Aareal Bank                                               10666Berlin                             Aareal Bank                26910AARBDE5W10009004795U000000000
100200001Berliner Bank                                             10890Berlin                             Berliner Bank              25140BEBEDEBBXXX69013616U000000000
100202001BHF-BANK                                                  10117Berlin                             BHF-BANK Berlin            25155BHFBDEFF10060004794U000000000
100204001Parex Bank Berlin                                         10117Berlin                             Parex Bank Berlin               PARXDEBBXXX09052878U000000000
100205001Sozialbank                                                10178Berlin                             Sozialbank Berlin          25013BFSWDE33BER09000530U000000000
