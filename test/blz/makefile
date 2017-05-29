#!/usr/bin/make

# process German Bankleitzahl (Bank Id Number) files
# @(#) $Id: makefile 806 2011-09-20 16:53:03Z gfis $
# 2017-05-29: git again
# 2011-09-20: dbat.jar separately
# 2010-10-19: Dbat from gramword.jar for worddb.infos
# 2007-04-10: revised for current BLZ file
# 2005-12-31: Dr. Georg Fischer
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
DBAT=java -classpath ../../../dbat/dist/dbat.jar org.teherba.dbat.Dbat -c worddb -e ISO-8859-1

all: prepare bankleitzahlen extract cm_no_use cm_freq cm_pos4 genmap

prepare:
	find . -iname "blz2*.txt" | sort -r | head -1 | xargs -l grep "^........1" > blz1.tmp
	wc blz1.tmp
extract:
	perl -ne 'print substr($$_, 150, 2). "\t". substr($$_, 0, 8). "\n";' blz1.tmp > blz2.tmp
methods:
	perl -ne 'print substr($$_, 0, 8). "\t". substr($$_, 168, 6) . "\t" . substr($$_, 150, 2). "\n";' blz1.tmp > blz3.tmp
rule_count:
	cut -f 2 blz3.tmp | sort | uniq -c | tee rule_count.tmp
method_count:
	cut -f 3 blz3.tmp | sort | uniq -c | tee method_count.tmp
infos: prepare blz_infos bic_infos
	$(DBAT) -n infos
#--------------------------------------------
bankleitzahlen: bankleitzahlen_create bankleitzahlen_populate bankleitzahlen_count
bankleitzahlen_create:
	$(DBAT) -f bankleitzahlen_create.sql
bankleitzahlen_populate:
	grep -vE "^........2" BLZ2_20131209.txt \
	| $(DBAT) -m fix -l 8,1,58,5,35,27,5,11,2,6,1,1,8,4,2 -r bankleitzahlen
bankleitzahlen_count:
	$(DBAT) -n bankleitzahlen
	# currently 19195 BLZs (with "[1-2]" in position 9), 4038 with "1"
#--------------------------------------------
blz_infos:
	perl blz_infos.pl blz1.tmp > blz_infos.ssv
	$(DBAT) "delete from infos where morph = 'blz'"
	$(DBAT) -r infos < blz_infos.ssv
bic_infos:
	perl bic_infos.pl blz1.tmp > bic_infos.ssv
	# cut -b 1-4,23- bic_infos.ssv | sort | uniq -c | less
	$(DBAT) "delete from infos where morph = 'bic'"
	$(DBAT) -r infos < bic_infos.ssv
bic_coba:
	$(DBAT) "select entry, enrel from infos where morph = 'bic' and substr(entry,4, 1) = '4'"  | sort | uniq -c
bic2:
	$(DBAT) "select entry, enrel from infos where morph = 'bic'"
#
special: special1 special2
special1:
	$(DBAT) "delete from infos where enrel = 'iban'"
	$(DBAT) -r infos < special-iban.ok.txt
special2:
	$(DBAT) "select distinct a.entry, b.enrel from infos a, infos b where substr(a.entry, 5,8) = b.entry and a.morph='iban' and b.morph='bic'"

# check methods which are not used
cm_no_use:
	cut -f 1 blz2.tmp | sort | uniq | perl missing.pl | tee missing.tmp
	sed -e "s/^/method_/" missing.tmp | tr -d "\r" > missing_methods.tmp
	grep -iH -f missing_methods.tmp ../../src/main/java/org/teherba/checkdig/account/DeAccount*.java

# frequencies of check methods
cm_freq:
	cut -f 1 blz2.tmp | sort | uniq -c > cmcount1.tmp
	sed -e "s/^/      0 /" missing.tmp > cmcount2.tmp
	sort -rn cmcount1.tmp cmcount2.tmp | tee cmcount.tmp

# check methods related to position 4 of the BLZ
# 06 12345678
# 0123456
cm_pos4:
	perl -ne 'print substr($$_, 6, 1). "\t". substr($$_, 0, 2). "\n";' blz2.tmp | sort | uniq -c | tee  blz3.tmp

# check methods related to BIC (first 4 places)
cm_bic:
	perl -ne 'print substr($$_, 150, 2). "\t". substr($$_, 139, 4) . "\n";' blz1.tmp | sort | uniq -c | tee  blz4.tmp

genmap:
	perl gen_blzmap.pl -java blz3.tmp blz1.tmp > ../../src/main/java/org/teherba/checkdig/account/BlzCheckMap.java
	perl gen_blzmap.pl -perl blz3.tmp blz1.tmp > ../account/add_method.pl

#-- finis --
