#!/usr/bin/perl

# Generates XML from the text of the SWIFT IBAN Registry PDF
# @(#) $Id: cut2.pl 37 2008-09-08 06:11:04Z gfis $
# 2014-01-20: initial 2!a in JOrdan structure pattern
# 2013-12-29: copied from ../account/bav_gen.pl
#
# activation:
#   wget "http://www.swift.com/dsp/resources/documents/IBAN_Registry.pdf"
#   extract text into registry.txt
#   perl swift_registry.pl registry.txt > registry.xml
#-----------------------------------------------------------------
#  Copyright 2013 Dr. Georg Fischer <punctum at punctum dot kom>
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
#-----------------------------------------------------------------
use strict;

# relevant lines start with one of the following:
# Name of country
# Country code
# BBAN length
# IBAN structure
# IBAN length
# IBAN print format example
# SEPA Country
# Contact details
#
# The value is in the next (non-empty) line.
#---------------------
    print <<'GFis';
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!--
	SWIFT IBAN Registry - extract from PDF text and generate XML
	@(#) \$Id\$
	download  "http://www.swift.com/dsp/resources/documents/IBAN_Registry.pdf"
	extract text into registry.txt (in Adobe Reader)
	perl swift_registry.pl registry.txt > swift_registry.xml
	automatically generated - do not edit here!
-->
<IBAN-Registry xmlns ="http://www.teherba.org/2013/checkiban">
GFis
    my $tag = "";
    my $cc = ""; # country code
    my $width = 0; # length of IBAN
    my $regex = ""; # IBAN structure translated into Perl regex
    my $id = 0;
    my $buffer = ""; # assemble a country here
    my $placeholder = "####"; # placaholder for $cc
    while (<>) {
        next if m/\A\s*\Z/; # skip empty lines
        s/\s+\Z//; # chompr
        my $line = $_;
        if (0) {
        } elsif (length($tag) > 0) {
            # tests before
            if (0) {
            } elsif ($tag =~ m/\A(Name\-of\-country)/) { # first tag
                $id ++;
                $buffer = <<GFis;
  <country id=\"$id\">
GFis
            } elsif ($tag =~ m/\A(Country-code)/) { # "GB" has additional comments behind
                $line = substr($line, 0, 2);
                $cc = $line;
                # $line = $placeholder;
            } elsif ($tag =~ m/\A(IBAN-structure)/) { # transform it into a Java Pattern
                $regex = substr($line, 0, 2); # Country Code
                if (0) {
                } elsif ($cc eq "GI") {
                    $line =~ s/\://;        # patch an error in Nov. 2013 release
                } elsif ($cc eq "JO") {
                    $line =~ s/\A2\!a/JO/;  # patch an error in Jan. 2014 release
                    $regex = "JO";
                } elsif ($cc eq "KW") {
                    $line = $line . "c";    # patch an error in Nov. 2013 release
                } elsif ($cc eq "KZ") {
                    $line =~ s/\A2\!a/KZ/;  # patch an error in Nov. 2013 release
                    $regex = "KZ";
                } elsif ($cc eq "MR") {
                    $line =~ s/\AMR13/MR2\!n/;  # patch a problem in Nov. 2013 release
                } elsif ($cc eq "PL") {
                    $line =~ s/16n/16\!n/;  # patch an error in Nov. 2013 release
                } elsif ($cc eq "TN") {
                    $line =~ s/\ATN59/TN2\!n/;  # patch a problem in Nov. 2013 release
                }
                my @patterns = ($line =~ m/(\d+\!?[acn])/g);
                if (scalar(@patterns) == 0) {
                    print "<!-- error in $cc: invalid pattern: $line -->\n";
                } else {
                    $width = 2; # for the country code
                    foreach my $pattern (@patterns) {
                        # print "<!--$pattern-->";
                        if ($pattern =~ m/\A(\d+)(\!)([acn])\Z/) {
                            my $repeat = $1;
                            my $strict = $2;
                            my $code   = $3;
                            if (0) {
                            } elsif ($code eq "a") {
                                $regex .= "([A-Z]\{$repeat\})";
                            } elsif ($code eq "c") {
                                $regex .= "([A-Za-z0-9]\{$repeat\})";
                            } elsif ($code eq "n") {
                                $regex .= "([0-9]\{$repeat\})";
                            }
                            $width += $repeat;
                        } else {
                            print "<!-- error in $cc: missing \"!\" in IBAN structure $line -->\n";
                        }
                    } # foreach
                }
                $line = $regex;
                # IBAN structure
            } elsif ($tag =~ m/\A(IBAN-length)/) {
                if ($width != $line) {
                    print "<!-- error in $cc: structure width $width != IBAN length $line -->\n";
                }
             } elsif ($tag =~ m/\A(BBAN-length)/) {
                if ($cc eq 'FI') {
                    $line = 14;
                }
            } elsif ($tag =~ m/\A(IBAN-electronic-format-example)/) { # transform it into a Java Pattern
                if (0) {
                } elsif ($cc eq "CR") {
                    $line =~ s/\AIBAN\s+//; # patch an error in Nov. 2013 release
                } elsif ($cc =~ m/FI/) {
                    $line =~ s/ or .*//;
                } elsif ($cc =~ m/dummy(MT|MU)/) {
                    $line .= <>; # there is a rest on the next line
                }
                $line =~ s/\s//g;
                if (length($cc) > 0 and ($line !~ m/\A$regex\Z/)) {
                    print "<!-- error in $cc: IBAN structure $regex does not match example $line -->\n";
                }
            } elsif ($tag =~ m/\A(SEPA-Country)/) { # patch
                if ($line =~ m/\A(Yes|No)/) {
                    $line = $1;
                } else {
                    $line = "No";
                }
            }

            $buffer .= <<GFis;
    <$tag>$line</$tag>
GFis

            # tests after
            if (0) {
            } elsif ($tag =~ m/\A(SEPA\-Country)/) { # last tag
                $buffer .= <<GFis;
  </country>
GFis
                print $buffer;
                if ($cc eq "DK") {
                    $cc = "FO";
                    $id ++;
                    $buffer =~ s{ id=\"\d+\"\>}        { id=\"$id\"\>};
                    $buffer =~ s{code\>[A-Z]{2}\<}     {code\>$cc\<};
                    $buffer =~ s{structure\>[A-Z]{2}\<}{structure\>$cc\<};
                    $buffer =~ s{example\>\w+\<}       {example\>FO6264600001631634\<};
                    $buffer =~ s{Name\-of\-country\>[^\<]+\<} {Name\-of\-country\>Faroe Islands\<};
                    print $buffer;
                    $cc = "GL";
                    $id ++;
                    $buffer =~ s{ id=\"\d+\"\>}        { id=\"$id\"\>};
                    $buffer =~ s{code\>[A-Z]{2}\<}     {code\>$cc\<};
                    $buffer =~ s{structure\>[A-Z]{2}\<}{structure\>$cc\<};
                    $buffer =~ s{example\>\w+\<}       {example\>GL8964710001000206\<};
                    $buffer =~ s{Name\-of\-country\>[^\<]+\<} {Name\-of\-country\>Greenland\<};
                    print $buffer;
                } # DK
            }
            $tag = ""; # expect next tag
        } elsif (   ($line =~ m/\A(Name of country)/)
                or  ($line =~ m/\A(Country code)/)
                or  ($line =~ m/\A(BBAN length)/)
                or  ($line =~ m/\A(IBAN structure)/i)
                or  ($line =~ m/\A(IBAN length)/)
                or  ($line =~ m/\A(IBAN electronic format example)/)
                or  ($line =~ m/\A(SEPA Country)/)
                ) {
            $tag = $1;
            $tag =~ s/ Structure/ structure/; # patch
            $tag =~ s/\s+/\-/g; # insert hyphens
        } else {
            # ignore
        }
    } # while
    print <<'GFis';
</IBAN-Registry>
GFis
__DATA__

This section shows the IBAN format of each country that has implemented the IBAN
standard. The countries are listed in alphabetical order.


2.1 Albania




Data element

Example

Name of country

Albania

Country code as defined in ISO 3166

AL

Domestic account number example

0000000235698741

BBAN

BBAN structure

8!n16!c

BBAN length

24

Bank identifier position within the BBAN

Bank Identifier 1-3, Branch Identifier:4-7,
Check Digit 8

Bank identifier length

8!n

Bank identifier example

212-1100-9

BBAN example

212110090000000235698741

IBAN

IBAN structure

AL2!n8!n16!c

IBAN length

28

IBAN electronic format example

AL47212110090000000235698741

IBAN print format example

AL47 2121 1009 0000 0002 3569 8741

SEPA Country

No

Contact details

Miho Valer
Deputy Director

Payment Systems Department
BANK OF ALBANIA

Kompleksi Halili
Rruga e Dibres
1000 TIRANA

ALBANIA
Tel: 355 4 2419301/2/3 ext 3061
Fax: 355 4 2419408
Email: vmiho@bankofalbania.org


















2.2 Andorra




Data element

Example

Name of country

Andorra

Country code as defined in ISO 3166

AD

Domestic account number example

2030200359100100

BBAN

BBAN structure

4!n4!n12!c

BBAN length

20

Bank identifier position within the BBAN

Positions 1-4, Branch identifier: positions 5-8

Bank identifier length

4!n, Branch identifier length: 4!n

Bank identifier example

0001, Branch identifier example: 2030
