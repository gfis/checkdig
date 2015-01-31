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
# unify all XML elements (tags)
# @(#) $Id: xml_uniq.pl 37 2008-09-08 06:11:04Z gfis $
# Copyright (c) 2005 punctum GmbH, D-79341 Kenzingen <punctum@punnctum.com>
# 2005-09-13, Georg Fischer
#
# activation:
#   perl xml_uniq.pl xml-file
#-----------------------------------------------------------------
use strict;
    
    while (<>) {
        s[\A\s+][];
        s[\<\!\-\-[^\>]+\>][]g;
        s[\s+\Z][\n];
        s[\"([^\"]*)\"]["*"]g;
        print;
    }
    