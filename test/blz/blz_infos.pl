#!/usr/bin/perl

# Generate tab separated data to be loaded in the 'infos' table
# blz8 \t "blz" \t name of bank \t method
# @(#) $Id: blz_infos.pl 806 2011-09-20 16:53:03Z gfis $
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
		my $meth = substr($_, 150,  2);

		my $locn = substr($_,  72, 35);
		$locn =~ s[\s+\Z][];
		$locn =~ s[\,.*][]; # remove ", Meckl" etc.
		# my $name = substr($_, 107, 27);
		my $name = substr($_,   9, 58); # this is the longer name
		$name =~ s[\s+(ex|Zw|Zndl|ehem|eh|Fil)\s+.*][];
		$name =~ s[\s+\Z][];
		if ($name !~ m[$locn]i) { # if the location is not contained in the name
			$name .= " " . $locn;
		}
		print join("\t", ($blz, "blz", $name, $meth)), "\n";
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
100208901Bayer Hypo- und Vereinsbank                               10896Berlin                             HypoVereinsbank Berlin     22014HYVEDEMM48899039785U000000000
100208902Bayer Hypo- und Vereinsbank                               14532Kleinmachnow                       HypoVereinsbank Kleinmachno22014HYVEDEMM12499049352U000000000
100208902Bayer Hypo- und Vereinsbank                               14776Brandenburg an der Havel           HypoVereinsbank Brandenburg22014HYVEDEMM16199049746U000000000
100208902Bayer Hypo- und Vereinsbank                               15517Fürstenwalde /Spree                HypoVereinsbank Fürstenwald22014HYVEDEMM07099050515U000000000
100208902Bayer Hypo- und Vereinsbank                               15711Königs Wusterhausen                HypoVereinsbank Königs-Wust22014HYVEDEMM10899049747U000000000
100208902Bayer Hypo- und Vereinsbank                               16515Oranienburg                        HypoVereinsbank Oranienburg22014HYVEDEMM16599049745U000000000
100222001Landesbank Berlin - ehemals Bankgesellschaft Berlin       10889Berlin                             Landesbank Berlin          29410BELADEBE22269048367M110050000
100302001Berlin-Hannoversche Hypothekenbank                        10773Berlin                             Berlin Hyp                 26706BHYPDEB1XXX09037413U000000000
100304001ABK-Kreditbank                                            10789Berlin                             ABK-Kreditbank Berlin      25686ABKBDEB1XXX09044427U000000000
100305001Bankhaus Löbbecke                                         10117Berlin                             Bankhaus Löbbecke Berlin   26225LOEBDEBBXXX09043961M000000000
100306001Bankhaus Kruber                                           13469Berlin                             Bankhaus Kruber Berlin     26205GENODEF1OGK88000532U000000000
100307001Gries & Heissel - Bankiers                                65189Wiesbaden                          Gries & Heissel Wiesbaden  26535DLGHDEB1XXX16044339U000000000
100400001Commerzbank Berlin (West)                                 10891Berlin                             Commerzbank -West- Berlin  24100COBADEBBXXX13024463U000000000
100400601Commerzbank Gf 160                                        10891Berlin                             Commerzbank Gf 160 Berlin  24101COBADEFF06009052880U000000000
100400611Commerzbank Gf 161                                        10891Berlin                             Commerzbank Gf 161 Berlin  24102COBADEFF06109052894U000000000
100450501Commerzbank Service-BZ                                    10785Berlin                             Commerzbank Service-BZ          COBADEFFBZB13050927U000000000
100500001Landesbank Berlin - Berliner Sparkasse                    10889Berlin                             Landesbank - Berliner Spk  51000BELADEBEXXXB8002745U000000000
100500011Landesbank Berlin                                         10889Berlin                             Landesbank Berlin          51001BELADEBE00120046768U000000000
100500051Landesbank Berlin - E 1 -                                 10889Berlin                             Landesbank Berlin - E 1 -  51005BELADEB1DB510047222U000000000
100500061Landesbank Berlin - E 2 -                                 10889Berlin                             Landesbank Berlin - E 2 -  51006BELADEB1DB609047223M000000000
100505001LBS Ost Berlin                                            10405Berlin                             LBS Ost Berlin                  LBSODEB1BLN09046141U000000000
100506001WestLB Berlin                                             10117Berlin                             WestLB Berlin              51099WELADEBBXXX08049322U000000000
100509991DekaBank Berlin                                           10249Berlin                             DekaBank Berlin                 DGZFDEFFBER09004788U000000000
100601981Pax-Bank                                                  14005Berlin                             Pax-Bank Berlin                 GENODED1PA606047622U000000000
100602371Evangelische Darlehnsgenossenschaft                       10503Berlin                             Ev Darlehnsgenossensch Bln      GENODEF1EDB33046278U000000000
100700001Deutsche Bank Fil Berlin                                  10883Berlin                             Deutsche Bank Fil Berlin   27700DEUTDEBBXXX63004787U000000000
100700241Deutsche Bank Privat und Geschäftskunden F 700            10883Berlin                             Deutsche Bank PGK Berlin   21700DEUTDEDBBER63049758U000000000
100701001Deutsche Bank Fil Berlin II                               10883Berlin                             Deutsche Bank Fil Berlin II27701DEUTDEBB10163053102U000000000
100701241Deutsche Bank Privat und Geschäftskd Berlin II            10883Berlin                             Deutsche Bank PGK Berlin II21701DEUTDEDB10163053103U000000000
100709891Eurohypo Ndl DB                                           10117Berlin                             Eurohypo Berlin Ndl DB          EHYPDEFFDBB63051414D000000000
100800001Dresdner Bank                                             10877Berlin                             Dresdner Bank Berlin       28100DRESDEBBXXX76024465U000000000
