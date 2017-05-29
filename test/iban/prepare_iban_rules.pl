#!/usr/bin/perl

# Prepare XML file for IBAN Rules
# @(#) $Id$
# Copyright (c) 2014 Dr. Georg Fischer
# 2014-01-17, Georg Fischer

use strict;

    print <<'GFis';
<?xml version="1.0" encoding="UTF-8" ?>
GFis
    my $tag = "";
    while (<DATA>) {
        s/\s+\Z//; # chompr
        my $line = $_;
        if (0) {
        } elsif ($line =~ m/\A\s*\Z/) { # empty
            print "$line\n";
        } elsif ($line =~ m/rule/) { # surrounding
            print "$line\n";
        } elsif ($line =~ m/\<exkto\>([^\<]+)\</) { # <exkto>
            print <<"GFis";
    <map akto="$1"\tnrule="0001" />
GFis
        } elsif ($line =~ m/\A\s+\<(mapkto.*)/) {
            print "\t<!-- $1 -->\n";
            $tag = "mapkto";
        } elsif ($line =~ m/\A\s+\<(mapblz.*)/) {
            $line =~ s/mapblz/map/;
            $line =~ s/\s+from=/ ablz=/;
            $line =~ s/\s+to=/\tnblz=/;
            $line =~ s/\s+bic=/\tnbic=/;
            print "$line\n";
        } elsif ($line =~ m/\A\s*\<\//) {
            $tag = "";
        } elsif ($line =~ m/\A\s*\d+/) {
            $line =~ s/\A\s+//;
            my @parms = split(/\s+/, $line);
            if (0) {
            } elsif ($tag eq "mapkto") {
                if (0) {
                } elsif (scalar(@parms) == 2) {
                    print <<"GFis";
    <map akto="$parms[0]"\tnkto="$parms[1]"\t/>
GFis
                } elsif (scalar(@parms) == 3) {
                    if (0) {
                    } elsif ($parms[2] =~ m{\A[A-Z]{2}\d\d}) { # is IBAN
                        print <<"GFis";
    <map akto="$parms[0]"\tnkto="$parms[1]"\tniban="$parms[2]"\t/>
GFis
                    } else {
                        print <<"GFis";
    <map ablz="$parms[0]"\takto="$parms[1]"\tnkto="$parms[2]"\t/>
GFis
                    }
                    # scalar 3
                }
                # mapkto
            }
            # lines tarts with number
        } else {
            print "$line\n";
        }
    } # while <>
__DATA__
<IBAN-rules version="2014-03">

<rule id="0001" version="00" bank="No IBAN">
    <exkto>\d+</exkto>
</rule>

<rule id="0002" version="00" bank="Augsburger Aktienbank">
    <exkto>\d{6}86\d</exkto>
    <exkto>\d{6}6\d{2}</exkto>
</rule>

<rule id="0003" version="00" bank="Areal Bank AG, ZWL Wiesbaden">
    <exkto>6161604670</exkto>
</rule>

<rule id="0004" version="00" bank="Landesbank Berlin / Berliner Sparkasse">
    <mapkto>
    135 0990021440
    1111 6600012020
    1900 0920019005
    7878 0780008006
    8888 0250030942
    9595 1653524703
    97097 0013044150
    112233 0630025819
    336666 6604058903
    484848 0920018963
    </mapkto>
</rule>

<rule id="0005" version="03" bank="Commerzbank AG">
    <special />
    <exblz>50040033</exblz>
    <mapkto>
30040000 0000000036 0261103600
47880031 0000000050 0519899900
47840065 0000000050 0150103000
47840065 0000000055 0150103000
70080000 0000000094 0928553201
70040041 0000000094 0212808000
47840065 0000000099 0150103000
37080040 0000000100 0269100000
38040007 0000000100 0119160000
37080040 0000000111 0215022000
51080060 0000000123 0012299300
36040039 0000000150 0161620000
68080030 0000000202 0416520200
30040000 0000000222 0348010002
38040007 0000000240 0109024000
69240075 0000000444 0445520000
60080000 0000000502 0901581400
60040071 0000000502 0525950200
55040022 0000000555 0211050000
39080005 0000000556 0204655600
39040013 0000000556 0106555600
57080070 0000000661 0604101200
26580070 0000000700 0710000000
50640015 0000000777 0222222200
30040000 0000000999 0123799900
86080000 0000001212 0480375900
37040044 0000001888 0212129101
25040066 0000001919 0141919100
10080000 0000001987 0928127700
50040000 0000002000 0728400300
20080000 0000002222 0903927200
38040007 0000003366 0385333000
37080040 0000004004 0233533500
37080040 0000004444 0233000300
43080083 0000004630 0825110100
50080000 0000006060 0096736100
10040000 0000007878 0267878700
10080000 0000008888 0928126501
50080000 0000009000 0026492100
79080052 0000009696 0300021700
79040047 0000009696 0680210200
39080005 0000009800 0208457000
50080000 0000042195 0900333200
32040024 0000047800 0155515000
37080040 0000055555 0263602501
38040007 0000055555 0305555500
50080000 0000101010 0090003500
50040000 0000101010 0311011100
37040044 0000102030 0222344400
86080000 0000121200 0480375900
66280053 0000121212 0625242400
16080000 0000123456 0012345600
29080010 0000124124 0107502000
37080040 0000182002 0216603302
12080000 0000212121 4050462200
37080040 0000300000 0983307900
37040044 0000300000 0300000700
37080040 0000333333 0270330000
38040007 0000336666 0105232300
55040022 0000343434 0217900000
85080000 0000400000 0459488501
37080040 0000414141 0041414100
38040007 0000414141 0108000100
20080000 0000505050 0500100600
37080040 0000555666 0055566600
20080000 0000666666 0900732500
30080000 0000700000 0800005000
70080000 0000700000 0750055500
70080000 0000900000 0319966601
37080040 0000909090 0269100000
38040007 0000909090 0119160000
70080000 0000949494 0575757500
70080000 0001111111 0448060000
70040041 0001111111 0152140000
10080000 0001234567 0920192001
38040007 0001555555 0258266600
76040061 0002500000 0482146800
16080000 0003030400 4205227110
37080040 0005555500 0263602501
75040062 0006008833 0600883300
12080000 0007654321 0144000700
70080000 0007777777 0443540000
70040041 0007777777 0213600000
64140036 0008907339 0890733900
70080000 0009000000 0319966601
61080006 0009999999 0202427500
12080000 0012121212 4101725100
29080010 0012412400 0107502000
34280032 0014111935 0645753800
38040007 0043434343 0118163500
30080000 0070000000 0800005000
70080000 0070000000 0750055500
44040037 0111111111 0320565500
70040041 0400500500 0400500500
60080000 0500500500 0901581400
60040071 0500500500 0512700600
    </mapkto>
</rule>

<rule id="0006" version="00" bank="Stadtsparkasse München">
    <mapkto>
1111111 20228888
7777777 903286003
34343434 1000506517
70000 18180018
    </mapkto>
</rule>

<rule id="0007" version="00" bank="Sparkasse KölnBonn">
    <mapkto blz="37050198">
111 1115 DE15370501980000001115
221 23002157 DE25370501980023002157
1888 18882068 DE15370501980018882068
2006 1900668508 DE57370501981900668508
2626 1900730100 DE41370501981900730100
3004 1900637016 DE39370501981900637016
3636 23002447 DE52370501980023002447
4000 4028 DE31370501980000004028
4444 17368 DE12370501980000017368
5050 73999 DE83370501980000073999
8888 1901335750 DE42370501981901335750
30000 9992959 DE22370501980009992959
43430 1901693331 DE56370501981901693331
46664 1900399856 DE98370501981900399856
55555 34407379 DE81370501980034407379
102030 1900480466 DE17370501981900480466
151515 57762957 DE64370501980057762957
222222 2222222 DE85370501980002222222
300000 9992959 DE22370501980009992959
333333 33217 DE53370501980000033217
414141 92817 DE83370501980000092817
606060 91025 DE64370501980000091025
909090 90944 DE20370501980000090944
2602024 5602024 DE24370501980005602024
3000000 9992959 DE22370501980009992959
7777777 2222222 DE85370501980002222222
8090100 38901 DE39370501980000038901
14141414 43597665 DE96370501980043597665
15000023 15002223 DE98370501980015002223
15151515 57762957 DE64370501980057762957
22222222 2222222 DE85370501980002222222
200820082 1901783868 DE54370501981901783868
222220022 2222222 DE85370501980002222222
    </mapkto>
</rule>

<rule id="0008" version="00" bank="BHF-Bank AG"><!-- method 60 -->
    <mapblz from="10020200" to="50020200" bic="BHFBDEFF500"/>
    <mapblz from="20120200" to="50020200" bic="BHFBDEFF500"/>
    <mapblz from="25020200" to="50020200" bic="BHFBDEFF500"/>
    <mapblz from="30020500" to="50020200" bic="BHFBDEFF500"/>
    <mapblz from="51020000" to="50020200" bic="BHFBDEFF500"/>
    <mapblz from="55020000" to="50020200" bic="BHFBDEFF500"/>
    <mapblz from="60120200" to="50020200" bic="BHFBDEFF500"/>
    <mapblz from="70220200" to="50020200" bic="BHFBDEFF500"/>
    <mapblz from="86020200" to="50020200" bic="BHFBDEFF500"/>
</rule>

<rule id="0009" version="00" bank="Sparkasse Schopfheim-Zell">
    <special />
    <mapblz from="68351976" to="68351557" bic="SOLADES1SFH" />
    <!-- Bei Kontonummern der ehemaligen Sparkasse Zell im Wiesental (BLZ: 683 519 76),
    die bei zehnstelliger Darstellung mit 1116 beginnen, werden die linken vier Stellen
    durch den Wert 3047 ersetzt.
    -->
</rule>

<rule id="0010" version="01" bank="Frankfurter Sparkasse">
    <mapkto blz="50050201" from="2000"   to="222000" iban="DE42500502010000222000" />
    <mapkto blz="50050201" from="800000" to="180802" iban="DE89500502010000180802" />
</rule>

<rule id="0011" version="00" bank="Sparkasse Krefeld">
    <mapkto>
    1000 8010001
    47800 47803
    </mapkto>
</rule>

<rule id="0012" version="01" bank="Landesbank Hessen-Thüringen Girozentrale">
    <mapblz to="50050000" bic="HELADEFFXXX"/>
</rule>

<rule id="0013" version="01" bank="Landesbank Hessen-Thüringen Girozentrale">
    <mapblz to="30050000" bic="WELADEDDXXX"/>
</rule>

<rule id="0014" version="00" bank="Deutsche Apotheker- und Ärztebank eG">
    <mapblz to="30060601" bic="DAAEDEDDXXX"/>
</rule>

<rule id="0015" version="01" bank="Pax-Bank eG">
<mapkto blz="37060193">
94 3008888018
556 0000101010
888 0031870011
4040 4003600101
5826 1015826017
25000 0025000110
393393 0033013019
444555 0032230016
603060 6002919018
2120041 0002130041
80868086 4007375013
400569017 4000569017
</mapkto>
</rule>

<rule id="0016" version="00" bank="Kölner Bank eG">
<mapkto blz="37160087">
300000 18128012
</mapkto>
</rule>

<rule id="0017" version="00" bank="Volksbank Bonn Rhein-Sieg">
<mapkto><!-- blz="38060186" -->
100 2009090013
111 2111111017
240 2100240010
4004 2204004016
4444 2044444014
6060 2016060014
102030 1102030016
333333 2033333016
909090 2009090013
50005000 5000500013
</mapkto>
</rule>

<rule id="0018" version="00" bank="Aachener Bank eG">
<mapkto blz="39060180">
556 120440110
5435435430 543543543
2157 121787016
9800 120800019
202050 1221864014
</mapkto>
</rule>

<rule id="0019" version="00" bank="Bethmann Bank">
    <mapblz from="50130100" to="50120383" bic="DELBDE33XXX">
    <mapblz from="50220200" to="50120383" bic="DELBDE33XXX">
    <mapblz from="70030800" to="50120383" bic="DELBDE33XXX">
</rule>

<rule id="0020" version="02" bank="Deutsche Bank AG">
    <special />
    <mapkto blz="50070010" from="9999" to="92777202" iban="DE80500700100092777202" />
    <exblz>10020000</exblz>
</rule>

<rule id="0021" version="01" bank="National-Bank AG">
    <!-- kto \d{6,7,9,10} -->
    <exkto>\d{8}</exkto>
    <exkto>\d{1,5}</exkto>
    <mapblz from="35020030" to="36020030" bic="NBAGDE3EXXX" />
    <mapblz from="36220030" to="36020030" bic="NBAGDE3EXXX" />
    <mapblz from="36520030" to="36020030" bic="NBAGDE3EXXX" />
</rule>

<rule id="0022" version="00" bank="GLS Gemeinschaftsbank eG">
    <mapkto from="1111111" to="2222200000" blz="43060967" />
</rule>

<rule id="0023" version="00" bank="Volksbank Osnabrück eG">
    <mapkto from="700" to="1000700800" blz="26590025" />
</rule>

<rule id="0024" version="00" bank="Bank im Bistum Essen eG">
<mapkto blz="36060295">
94 1694
248 17248
345 17345
400 14400
</mapkto>
</rule>

<rule id="0025" version="00" bank="Landesbank Baden-Württemberg / Baden-Württembergische Bank">
    <mapblz to="60050101" />
</rule>

<rule id="0026" version="00" bank="Bank für Kirche und Diakonie eG, KD Bank">
<kto blz="35060190" check="no">
55111
8090100
</kto>
</rule>

<rule id="0027" version="00" bank="Volksbank Krefeld eG">
<kto blz="32060362" check="no">
3333
4444
</kto>
</rule>

<rule id="0028" version="00" bank="Sparkasse Hannover">
    <mapblz from="25050299" to="25050180" bic="SPKHDE2HXXX" />
</rule>

<rule id="0029" version="00" bank="Société Générale">
    <special />
    <!--
    10-stellige Kontennummern (d. h. die 1. Ziffer der Kontonummer ist ungleich "0"), die in
    Position 4 eine "0" enthalten, können nicht 1:1 in eine gültige IBAN konvertiert werden.
    In diesen Fällen wird die korrekte Kontonummer für IBAN gebildet indem die 4. Ziffer
    (die "0") entfernt wird. Man erhält eine 9-stellige Konto-Nummer, die 1. Ziffer der Kontonummer
    in der IBAN ist damit implizit eine "0".
    -->
</rule>

<rule id="0030" version="00" bank="Pommersche Volksbank eG">
<kto blz="13091054" check="no">
1718190 22000225 49902271 49902280 101680029
104200028 106200025 108000171 108000279 108001364
108001801 108002514 300008542 9130099995 9130500002
9131100008 9131600000 9131610006 9132200006 9132400005
9132600004 9132700017 9132700025 9132700033 9132700041
9133200700 9133200735 9133200743 9133200751 9133200786
9133200808 9133200816 9133200824 9133200832 9136700003
9177300010 9177300060 9198100002 9198200007 9198200104
9198300001 9331300141 9331300150 9331401010 9331401061
9349010000 9349100000 9360500001 9364902007 9366101001
9366104000 9370620030 9370620080 9371900010 9373600005
9402900021 9605110000 9614001000 9615000016 9615010003
9618500036 9631020000 9632600051 9632600060 9635000012
9635000020 9635701002 9636010003 9636013002 9636016001
9636018004 9636019000 9636022001 9636024004 9636025000
9636027003 9636028000 9636045001 9636048000 9636051001
9636053004 9636120003 9636140004 9636150000 9636320002
9636700000 9638120000 9639401100 9639801001 9670010004
9680610000 9705010002 9705403004 9705404000 9705509996
9707901001 9736010000 9780100050 9791000030 9990001003
9990001100 9990002000 9990004002 9991020001 9991040002
9991060003 9999999993 9999999994 9999999995 9999999996
9999999997 9999999998 9999999999
</kto>
</rule>

<rule id="0031" version="01" bank="UniCredit Bank AG">
    <special />
</rule>

<rule id="0032" version="00" bank="UniCredit Bank AG">
    <special />
</rule>

<rule id="0033" version="01" bank="UniCredit Bank AG">
    <special />
    <mapall bic="HYVEDEMMXXX">
22222 70020270 5803435253 DE11700202705803435253
1111111 70020270 39908140 DE88700202700039908140
94 70020270 2711931 DE83700202700002711931
7777777 70020270 5800522694 DE40700202705800522694
55555 70020270 5801800000 DE61700202705801800000
    </mapall>
</rule>

<rule id="0034" version="00" bank="UniCredit Bank AG">
    <special />
    <mapall bic="HYVEDEMM473">
500500500 60020290 4340111112 DE82600202904340111112
502 60020290 4340118001 DE28600202904340118001
    </mapall>
</rule>

<rule id="0035" version="01" bank="UniCredit Bank AG">
    <special />
    <mapall bic="HYVEDEMM455">
9696 79020076 1490196966 DE29790200761490196966
    </mapall>
</rule>

<rule id="0036" version="00" bank="HSH Nordbank AG, Hamburg und Kiel">
    <special />
    <mapblz from="20050000" to="21050000" />
    <mapblz from="23050000" to="21050000" />
</rule>

<rule id="0037" version="00" bank="The Bank of Tokyo-Mitsubishi UFJ, Ltd.">
    <mapblz from="20110700" to="30010700" bic="BOTKDEDXXXX" />
</rule>

<rule id="0038" version="00" bank="Ostfriesische Volksbank eG">
    <mapblz from="26691213" to="28590075" bic="GENODEF1LER" />
    <mapblz from="28591579" to="28590075" bic="GENODEF1LER" />
    <mapblz from="25090300" to="28590075" bic="GENODEF1LER" />
</rule>

<rule id="0039" version="00" bank="Oldenburgische Landesbank AG">
    <mapblz from="25621327" to="28020050" />
    <mapblz from="26520017" to="28020050" />
    <mapblz from="26521703" to="28020050" />
    <mapblz from="26522319" to="28020050" />
    <mapblz from="26620010" to="28020050" />
    <mapblz from="26621413" to="28020050" />
    <mapblz from="26720028" to="28020050" />
    <mapblz from="28021002" to="28020050" />
    <mapblz from="28021301" to="28020050" />
    <mapblz from="28021504" to="28020050" />
    <mapblz from="28021623" to="28020050" />
    <mapblz from="28021705" to="28020050" />
    <mapblz from="28021906" to="28020050" />
    <mapblz from="28022015" to="28020050" />
    <mapblz from="28022412" to="28020050" />
    <mapblz from="28022511" to="28020050" />
    <mapblz from="28022620" to="28020050" />
    <mapblz from="28022822" to="28020050" />
    <mapblz from="28023224" to="28020050" />
    <mapblz from="28023325" to="28020050" />
    <mapblz from="28220026" to="28020050" />
    <mapblz from="28222208" to="28020050" />
    <mapblz from="28222621" to="28020050" />
    <mapblz from="28320014" to="28020050" />
    <mapblz from="28321816" to="28020050" />
    <mapblz from="28420007" to="28020050" />
    <mapblz from="28421030" to="28020050" />
    <mapblz from="28520009" to="28020050" />
    <mapblz from="28521518" to="28020050" />
    <mapblz from="29121731" to="28020050" />
</rule>

<rule id="0040" version="01" bank="Sparkasse Staufen-Breisach">
    <mapblz to="68052328" bic="SOLADES1STF" /><!-- from="68051310" -->
</rule>

<rule id="0041" version="00" bank="Bausparkasse Schwäbisch Hall AG">
    <special />
    <!--
    mapiban blz="62220000" iban="DE96500604000000011404" bic="GENODEFFXXX"
    -->
</rule>

<rule id="0042" version="00" bank="Deutsche Bundesbank">
    <special />
    <exkto>\d{1,7}</exkto>
    <exkto>\d{9,10}</exkto>
    <exkto>\d{3}00\d{3}</exkto>
</rule>

<rule id="0043" version="01" bank="Sparkasse Pforzheim Calw">
    <mapblz from="60651070" to="66650085" bic="PZHSDE66XXX" />
    <special />
    <!--Sobald die BLZ 606 510 70 durch die BLZ 666 500 85 ersetzt wurde, darf nur noch die
    Prüfziffernmethode 06 (Methode für BLZ 666 500 85) für die Ursprungskontonummer
    verwendet werden. Führt dies zu einem Prüfziffer-Fehler, kann die Kontonummer nicht
    auf die IBAN umgestellt werden.
    -->
</rule>

<rule id="0044" version="00" bank="Sparkasse Freiburg">
    <mapkto from="202" to="2282022" iban="DE51680501010002282022" />
</rule>

<rule id="0045" version="01" bank="SEB AG">
<!-- this BIC assigned by BLZ file
bic="ESSEDE5FXXX"
502 101 30 502 101 50 502 101 70 505 101 20 505 101 40 505 101 60
502 101 31 502 101 51 502 101 71 505 101 21 505 101 41 505 101 61
502 101 32 502 101 52 502 101 72 505 101 22 505 101 42 505 101 62
502 101 33 502 101 53 502 101 73 505 101 23 505 101 43 505 101 63
502 101 34 502 101 54 502 101 74 505 101 24 505 101 44 505 101 64
502 101 35 502 101 55 502 101 75 505 101 25 505 101 45 505 101 65
502 101 36 502 101 56 502 101 76 505 101 26 505 101 46 505 101 66
502 101 37 502 101 57 502 101 77 505 101 27 505 101 47 505 101 67
502 101 38 502 101 58 502 101 78 505 101 28 505 101 48 505 101 68
502 101 39 502 101 59 502 101 79 505 101 29 505 101 49 505 101 69
502 101 40 502 101 60 502 101 80 505 101 30 505 101 50 505 101 70
502 101 41 502 101 61 502 101 81 505 101 31 505 101 51 505 101 71
502 101 42 502 101 62 502 101 82 505 101 32 505 101 52 505 101 72
502 101 43 502 101 63 502 101 83 505 101 33 505 101 53 505 101 73
502 101 44 502 101 64 502 101 84 505 101 34 505 101 54 505 101 74
502 101 45 502 101 65 502 101 85 505 101 35 505 101 55 505 101 75
502 101 46 502 101 66 502 101 86 505 101 36 505 101 56 505 101 76
502 101 47 502 101 67 502 101 87 505 101 37 505 101 57 505 101 77
502 101 48 502 101 68 502 101 88 505 101 38 505 101 58 505 101 78
502 101 49 502 101 69 502 101 89 505 101 39 505 101 59 505 101 79
505 101 80
-->
</rule>

<rule id="0046" version="00" bank="Santander Consumer Bank">
    <mapblz to="31010833" />
</rule>

<rule id="0047" version="00" bank="Santander Consumer Bank">
    <special />
    <!-- Achtstellige Kontonummern sind rechtsbündig mit Nullen aufzufüllen.
    Alle anderen Kontonummern mit weniger als zehn Stellen werden nach der Standard-
    Regel linksbündig mit Nullen aufgefüllt.
    -->
</rule>

<rule id="0048" version="00" bank="VON ESSEN GmbH & Co. KG Bankgesellschaft">
    <mapblz from="10120800" to="36010200" bic="VONEDE33XXX" />
    <mapblz from="27010200" to="36010200" bic="VONEDE33XXX" />
    <mapblz from="60020300" to="36010200" bic="VONEDE33XXX" />
</rule>

<rule id="0049" version="00" bank="WGZ Bank">
    <special />
    <!--
    Für Kontonummern mit einer '9' an der 5. Stelle muss die Kontonummer, auf deren Basis
    die IBAN ermittelt wird, abweichend berechnet werden. Die ersten 4 Stellen (inkl.
    aufgefüllter Nullen) müssen ans Ende gestellt werden, so dass die Kontonummer dann
    immer mit der '9' anfängt.
    -->
    <mapkto>
0000000036 0002310113
0000000936 0002310113
0000000999 0001310113
0000006060 0000160602
    </mapkto>
</rule>

<rule id="0050" version="00" bank="Sparkasse LeerWittmund">
    <mapblz from="28252760" to="28550000" bic="BRLADE21LER" />
</rule>

<rule id="0051" version="00" bank="Landesbank Baden-Württemberg / Baden-Württembergische Bank">
    <mapkto check="no">
0000000333 7832500881
0000000502 0001108884
0500500500 0005005000
0502502502 0001108884
    </mapkto>
</rule>

<rule id="0052" version="00" bank="Landesbank Baden-Württemberg / Baden-Württembergische Bank">
    <mapall>
67220020 5308810004 60050101 0002662604 SOLADEST600
67220020 5308810000 60050101 0002659600 SOLADEST600
67020020 5203145700 60050101 7496510994 SOLADEST600
69421020 6208908100 60050101 7481501341 SOLADEST600
66620020 4840404000 60050101 7498502663 SOLADEST600
64120030 1201200100 60050101 7477501214 SOLADEST600
64020030 1408050100 60050101 7469534505 SOLADEST600
63020130 1112156300 60050101 0004475655 SOLADEST600
62030050 7002703200 60050101 7406501175 SOLADEST600
69220020 6402145400 60050101 7485500252 SOLADEST600
    </mapall>
</rule>

<rule id="0053" version="00" bank="Landesbank Baden-Württemberg / Baden-Württembergische Bank">
    <mapall>
55050000 35000 60050101 7401555913 SOLADEST600
55050000 119345106 60050101 7401555906 SOLADEST600
55050000 908 60050101 7401507480 SOLADEST600
55050000 901 60050101 7401507497 SOLADEST600
55050000 910 60050101 7401507466 SOLADEST600
55050000 35100 60050101 7401555913 SOLADEST600
55050000 902 60050101 7401507473 SOLADEST600
55050000 44000 60050101 7401555872 SOLADEST600
55050000 110132511 60050101 7401550530 SOLADEST600
55050000 110024270 60050101 7401501266 SOLADEST600
55050000 3500 60050101 7401555913 SOLADEST600
55050000 110050002 60050101 7401502234 SOLADEST600
55050000 55020100 60050101 7401555872 SOLADEST600
55050000 110149226 60050101 7401512248 SOLADEST600
60020030 1047444300 60050101 7871538395 SOLADEST600
60020030 1040748400 60050101 0001366705 SOLADEST600
60020030 1000617900 60050101 0002009906 SOLADEST600
60020030 1003340500 60050101 0002001155 SOLADEST600
60020030 1002999900 60050101 0002588991 SOLADEST600
60020030 1004184600 60050101 7871513509 SOLADEST600
60020030 1000919900 60050101 7871531505 SOLADEST600
60020030 1054290000 60050101 7871521216 SOLADEST600
60050000 1523 60050101 0001364934 SOLADEST600
60050000 2811 60050101 0001367450 SOLADEST600
60050000 2502 60050101 0001366705 SOLADEST600
60050000 250412 60050101 7402051588 SOLADEST600
60050000 3009 60050101 0001367924 SOLADEST600
60050000 4596 60050101 0001372809 SOLADEST600
60050000 3080 60050101 0002009906 SOLADEST600
60050000 1029204 60050101 0002782254 SOLADEST600
60050000 3002 60050101 0001367924 SOLADEST600
60050000 123456 60050101 0001362826 SOLADEST600
60050000 2535 60050101 0001119897 SOLADEST600
60050000 5500 60050101 0001375703 SOLADEST600
66020020 4002401000 60050101 7495500967 SOLADEST600
66020020 4000604100 60050101 0002810030 SOLADEST600
66020020 4002015800 60050101 7495530102 SOLADEST600
66020020 4003746700 60050101 7495501485 SOLADEST600
66050000 86567 60050101 0001364934 SOLADEST600
66050000 86345 60050101 7402046641 SOLADEST600
66050000 85304 60050101 7402045439 SOLADEST600
66050000 85990 60050101 7402051588 SOLADEST600
86050000 1016 60050101 7461500128 SOLADEST600
86050000 3535 60050101 7461505611 SOLADEST600
86050000 2020 60050101 7461500018 SOLADEST600
86050000 4394 60050101 7461505714 SOLADEST600
</mapall>
</rule>

<rule id="0054" version="01" bank="Evangelische Darlehnsgenossenschaft eG">
    <mapkto>
500 500500
502 502502
18067 180670
484848 484849
636306 63606
760440 160440
1018413 10108413
2601577 26015776
5005000 500500
10796740 10796743
11796740 11796743
12796740 12796743
13796740 13796743
14796740 14796743
15796740 15796743
16307000 163107000
16610700 166107000
16796740 16796743
17796740 17796743
18796740 18796743
19796740 19796743
20796740 20796743
21796740 21796743
22796740 22796743
23796740 23796743
24796740 24796743
25796740 25796743
26610700 266107000
26796740 26796743
27796740 27796743
28796740 28796743
29796740 29796743
45796740 45796743
50796740 50796743
51796740 51796743
52796740 52796743
53796740 53796743
54796740 54796743
55796740 55796743
56796740 56796743
57796740 57796743
58796740 58796743
59796740 59796743
60796740 60796743
61796740 61796743
62796740 62796743
63796740 63796743
64796740 64796743
65796740 65796743
66796740 66796743
67796740 67796743
68796740 68796743
69796740 69796743
1761070000 176107000
2210531180 201053180
</mapkto>
<kto check="no">
624044 DE96210602370000624044
4063060 DE11210602370004063060
20111908 DE97210602370020111908
20211908 DE92210602370020211908
20311908 DE87210602370020311908
20411908 DE82210602370020411908
20511908 DE77210602370020511908
20611908 DE72210602370020611908
20711908 DE67210602370020711908
20811908 DE62210602370020811908
20911908 DE57210602370020911908
21111908 DE47210602370021111908
21211908 DE42210602370021211908
21311908 DE37210602370021311908
21411908 DE32210602370021411908
21511908 DE27210602370021511908
21611908 DE22210602370021611908
21711908 DE17210602370021711908
21811908 DE12210602370021811908
21911908 DE07210602370021911908
22111908 DE94210602370022111908
22211908 DE89210602370022211908
22311908 DE84210602370022311908
22411908 DE79210602370022411908
22511908 DE74210602370022511908
22611908 DE69210602370022611908
46211991 DE43210602370046211991
50111908 DE52210602370050111908
50211908 DE47210602370050211908
50311908 DE42210602370050311908
50411908 DE37210602370050411908
50511908 DE32210602370050511908
50611908 DE27210602370050611908
50711908 DE22210602370050711908
50811908 DE17210602370050811908
50911908 DE12210602370050911908
51111908 DE02210602370051111908
51111991 DE89210602370051111991
51211908 DE94210602370051211908
51211991 DE84210602370051211991
51311908 DE89210602370051311908
51411908 DE84210602370051411908
51511908 DE79210602370051511908
51611908 DE74210602370051611908
51711908 DE69210602370051711908
51811908 DE64210602370051811908
51911908 DE59210602370051911908
52111908 DE49210602370052111908
52111991 DE39210602370052111991
52211908 DE44210602370052211908
52211991 DE34210602370052211991
52311908 DE39210602370052311908
52411908 DE34210602370052411908
52511908 DE29210602370052511908
52611908 DE24210602370052611908
52711908 DE19210602370052711908
52811908 DE14210602370052811908
52911908 DE09210602370052911908
53111908 DE96210602370053111908
53211908 DE91210602370053211908
53311908 DE86210602370053311908
57111908 DE90210602370057111908
58111908 DE40210602370058111908
58211908 DE35210602370058211908
58311908 DE30210602370058311908
58411908 DE25210602370058411908
58511908 DE20210602370058511908
80111908 DE07210602370080111908
80211908 DE02210602370080211908
80311908 DE94210602370080311908
80411908 DE89210602370080411908
80511908 DE84210602370080511908
80611908 DE79210602370080611908
80711908 DE74210602370080711908
80811908 DE69210602370080811908
80911908 DE64210602370080911908
81111908 DE54210602370081111908
81211908 DE49210602370081211908
81311908 DE44210602370081311908
81411908 DE39210602370081411908
81511908 DE34210602370081511908
81611908 DE29210602370081611908
81711908 DE24210602370081711908
81811908 DE19210602370081811908
81911908 DE14210602370081911908
82111908 DE04210602370082111908
82211908 DE96210602370082211908
82311908 DE91210602370082311908
82411908 DE86210602370082411908
82511908 DE81210602370082511908
82611908 DE76210602370082611908
82711908 DE71210602370082711908
82811908 DE66210602370082811908
82911908 DE61210602370082911908
99624044 DE93210602370099624044
300143869 DE30210602370300143869
</kto>
</rule>

<rule id="0055" version="00" bank="BHW Kreditservice GmbH">
    <mapblz to="25410200" bic="BHWBDE2HXXX" />
</rule>

<rule id="0056" version="00" bank="SEB AG">
    <exkto>\d{1,9}</exkto>
    <!-- BLZ with 10-digit accounts:
10010111 26510111 36210111 48010111 59010111 70010111
13010111 27010111 37010111 50010111 60010111 72010111
16010111 28010111 38010111 50510111 63010111 75010111
20010111 29010111 39010111 51010111 65310111 76010111
21010111 29210111 40010111 51310111 66010111 79010111
21210111 30010111 41010111 51410111 66610111 79510111
23010111 31010111 42010111 52010111 67010111 81010111
25010111 33010111 42610112 54210111 67210111 82010111
25410111 35010111 43010111 55010111 68010111 86010111
25910111 35211012 44010111 57010111 68310111
26010111 36010111 46010111 58510111 69010111
    -->
    <mapkto>
36 1010240003 DE29380101111010240003
50 1328506100 DE55480101111328506100
99 1826063000 DE26430101111826063000
110 1015597802 DE52250101111015597802
240 1010240000 DE13380101111010240000
333 1011296100 DE15380101111011296100
555 1600220800 DE54100101111600220800
556 1000556100 DE42390101111000556100
606 1967153801 DE70250101111967153801
700 1070088000 DE92265101111070088000
777 1006015200 DE72250101111006015200
999 1010240001 DE83380101111010240001
1234 1369152400 DE91250101111369152400
1313 1017500000 DE48570101111017500000
1888 1241113000 DE81370101111241113000
1953 1026500901 DE30250101111026500901
1998 1547620500 DE47670101111547620500
2007 1026500907 DE62250101111026500907
4004 1635100100 DE45370101111635100100
4444 1304610900 DE88670101111304610900
5000 1395676000 DE20250101111395676000
5510 1611754300 DE96290101111611754300
6060 1000400200 DE43500101111000400200
6800 1296401301 DE02670101111296401301
55555 1027758200 DE13380101111027758200
60000 1005007001 DE98500101111005007001
66666 1299807801 DE10200101111299807801
102030 1837501600 DE59370101111837501600
121212 1249461502 DE48700101111249461502
130500 1413482100 DE78300101111413482100
202020 1213431002 DE24370101111213431002
414141 1010555101 DE59380101111010555101
666666 1798758900 DE49200101111798758900
5000000 1403124100 DE62370101111403124100
500500500 1045720000 DE17600101111045720000
    </mapkto>
</rule>

<rule id="0057" version="00" bank="Badenia Bausparkasse">
    <mapblz from="50810900" to="66010200" />
</rule>

</IBAN-rules>
