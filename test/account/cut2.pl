#!/usr/bin/perl

# cut method number and account
# @(#) $Id: cut2.pl 71 2009-01-08 20:33:41Z gfis $
# Copyright (c) 2005 punctum GmbH, D-79341 Kenzingen <punctum@punnctum.com>
# 2005-10-14, Georg Fischer
#
# activation:
#   perl cut2.pl test.tsv > method.tst
#-----------------------------------------------------------------
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
use strict;

    while (<>) {
        my ($method, $blz, $account) = split;
        $account = substr ("0000000000", 0, 10 - length ($account)) . $account;
        next if $method =~ m[z];
        print substr ($method, 0,2), " ", $account, "\n";
    } # while
    
