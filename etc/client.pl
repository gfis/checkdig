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
# example client - short perl program 
# for SOAP access to CheckDigit service
# @(#) $Id: client.pl 77 2009-01-16 08:14:16Z gfis $
# Copyright (c) 2005 Dr. Georg Fischer <punctum@punctum.com>
# 2005-10-07: copied from numword/etc/client.pl
#
# Usage: 
#   perl client.pl [-]function parm1 [parm2]
#                   -iban   IBAN
#                   -isin   ISIN
#                   -vatid  VAT Id
#                   -account account-no blz
#
# The essential 4 lines in the program below are marked with # <===
#------------------------------------------------------------------ 

use strict;
use SOAP::Lite; # <===
     
    # take up to 3 parameters from the command line
    my $function = "";
    my $parm1 = "";
    my $parm2 = "";
    if (scalar (@ARGV) > 0) {
        $function = shift @ARGV;
        $function =~ s[\A\-][]; # remove any leading dash
        if (scalar (@ARGV) > 0) {
            $parm1 = shift @ARGV;
            if (scalar (@ARGV) > 0) {
                $parm2 = join (" ", @ARGV);
            }
        }
    }
    else {
        print   "usage: perl client.pl -function [parm1 [parm2]]\n"
            .   "   where   function = iban, isin, vatid, account ...\n"
            ;
    }
         
    # create a new SOAP::Lite instance from the WSDL
    my $path = $0;
    $path =~ s[client\.pl][checkdig.wsdl];
    # print "Path: \"$path\"\n";
    my $service = SOAP::Lite->service("file:$path"); # <===
    # print "Error1: $@, $!, $?\n";

    # call CheckService
    my $results = $service->getResponse ($function, $parm1, $parm2); # <===
    # print "Error2: $@, $!, $?\n";
    print $results, "\n"; # <===
