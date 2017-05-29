#!/usr/bin/perl

# reformat IBAN test results
# @(#) $Id$
# Copyright (c) 2014 Dr. Georg Fischer
# 2014-01-23, Georg Fischer

# input lines have tab separated fields:
# oacct oblz niban nbic remark => ciban cbic orid okey cremark

use strict;

    my $passed = 0;
    my $failed = 0;
    print <<"GFis";
<table border="1" cellpadding="1">
GFis
    while (<>) {
        s/\s+\Z//; # chompr
        next if m/\A\s*\#/;
        my $line = $_;
        my ($oacct, $oblz, $niban, $nbic, $remark, $arrow, $ciban, $cbic, $orid, $okey, $result) = split(/\t/, $line);
        $niban = substr($niban, 0, 4) . " " . substr($niban, 4, 8) . " " . substr($niban, 12);
        $ciban = substr($ciban, 0, 4) . " " . substr($ciban, 4, 8) . " " . substr($ciban, 12);
        my $iban_ok = $niban eq $ciban;
        my $bic_ok  = $nbic  eq $cbic;
        if ($iban_ok and $bic_ok) {
            $passed ++;
        } else {
            $failed ++;
        }
        my $iban_color = "style=\"background-color:" . ($iban_ok ? "lightgreen" : ($remark =~ m/\A\?/ ? "yellow" : "salmon")) . "\"";
        my  $bic_color = "style=\"background-color:" . ($bic_ok  ? "lightgreen" : "salmon") . "\"";
        print <<"GFis";
    <tr valign="top"><td>$oacct</td><td>$oblz</td><td>$niban</td><td>$nbic</td>
        <td $iban_color>$ciban</td><td $bic_color>$cbic</td><td>$orid</td><td>$okey</td>
        <td>$remark<br />$result</td>
    </tr>
GFis
    } # while
    print <<"GFis";
</table>
GFis
    print STDERR "" . ($passed + $failed) . " tests, $passed passed, $failed failed\n";
