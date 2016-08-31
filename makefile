#!/usr/bin/make

# Test Xtool schema utility programs
# @(#) $Id: 2bbd9511422674a354fe5a19f2d55437adbebce0 $
# 2016-08-31: Georg Fischer: copied from xtool

APPL=checkdig
JAVA=java -cp dist/$(APPL).jar
DIFF=diff -y --suppress-common-lines --width=160
DIFF=diff -w -rs -C0
SRC=src/main/java/org/teherba/$(APPL)
TOM=c:/var/lib/tomcat/
TOMC=$(TOM)/webapps/$(APPL)
TESTDIR=test
# the following can be overriden outside for single or subset tests,
# for example make regression TEST=U%
TEST="%"
# for Windows, SUDO should be empty
SUDO=

all: regression
#-------------------------------------------------------------------
# Perform a regression test 
regression: 
	java -cp dist/$(APPL).jar org.teherba.common.RegressionTester $(TESTDIR)/all.tests $(TEST) 2>&1 \
	| tee $(TESTDIR)/regression.log
	grep FAILED $(TESTDIR)/regression.log
#
# Recreate all testcases which failed (i.e. remove xxx.prev.tst)
# Handle with care!
# Failing testcases are turned into "passed" and are manifested by this target!
recreate: recr1 regr2
recr0:
	grep -E '> FAILED' $(TESTDIR)/regression*.log | cut -f 3 -d ' ' | xargs -l -ißß echo rm -v test/ßß.prev.tst
recr1:
	grep -E '> FAILED' $(TESTDIR)/regression*.log | cut -f 3 -d ' ' | xargs -l -ißß rm -v test/ßß.prev.tst
regr2:
	make regression TEST=$(TEST) > x.tmp
# test whether all defined tests in common.tests have *.prev.tst results and vice versa
check_tests:
	grep -E "^TEST" $(TESTDIR)/all.tests   | cut -b 6-8 | sort | uniq -c > $(TESTDIR)/tests_formal.tmp
	ls -1 $(TESTDIR)/*.prev.tst            | cut -b 6-8 | sort | uniq -c > $(TESTDIR)/tests_actual.tmp
	diff -y --suppress-common-lines --width=32 $(TESTDIR)/tests_formal.tmp $(TESTDIR)/tests_actual.tmp
#---------------------------------------------------
jfind:
	find src -iname "*.java" | xargs -l grep -H $(JF)
rmbak:
	find src -iname "*.bak"  | xargs -l rm -v
#---------------------------------------------------
generator:
	$(JAVA) org.teherba.checkdig.account.IBANGenerator 2651505  68092000
	$(JAVA) org.teherba.checkdig.account.IBANGenerator 22062459 68050101
run:
	$(JAVA)
javadoc:
	ant javadoc
deploy:
	ant transform deploy
zip:
	rm -f $(APPL).zip
	find . | grep -v "test/" | zip -@ $(APPL).zip
#--------------------
# preparations
compile:
	ant transform dist
doc:
	ant javadoc
test_prepare:
	cd test/account ; perl cut2.pl test.tsv > method.tst
#---------------------
# generation
swift_registry:
	perl test/iban/swift_registry.pl test/iban/2013-12-29.IBAN_Registry.txt | tee test/iban/swift_registry.xml
	xmllint --noout --schema test/iban/swift_registry.xsd test/iban/swift_registry.xml
	grep -H "error" test/iban/swift_registry.xml
map1:
	xsltproc test/iban/sepa_countries.xsl   test/iban/swift_registry.xml
extr1:
	xsltproc test/iban/lengths.xsl          test/iban/swift_registry.xml
#---------------------
# tests
account1:
	$(JAVA) checkdig.account.DeAccountChecker test/account/test1.lst
account2:
	$(JAVA) checkdig.account.DeAccountChecker test/account/dbb_test.lst
	# grep -E "\?"
account4:
	$(JAVA) checkdig.account.DeAccountChecker test/account/example.dat

pztest:
	$(JAVA) checkdig.account.DeAccountChecker test/account/pztest.lst > test/account/pztest.tmp
	perl test/account/eval_method.pl test/account/pztest.tmp | tee test/account/pztest.eval

b8:
	$(JAVA) checkdig.account.DeAccountChecker test/account/b8.lst | sort > test/account/b8.tmp
	perl test/account/eval_method.pl test/account/b8.tmp | tee test/account/b8.eval
b8bad:
	grep "?" test/account/b8.tmp > test/account/b8bad.tmp
	cd test/account ; cut -f 1 b8bad.tmp | wc
	cd test/account ; cut -f 1 b8bad.tmp | sort | uniq -c | sort -n | tee b8bad.occ

21:
	$(JAVA) checkdig.account.DeAccountChecker test/account/21.lst > test/account/21.tmp
	less test/account/21.tmp
29:
	$(JAVA) checkdig.account.DeAccountChecker test/account/29.lst > test/account/29.tmp
	less test/account/29.tmp
74:
	$(JAVA) checkdig.account.DeAccountChecker test/account/74bad.lst > test/account/74.tmp
	less test/account/74.tmp
87:
	$(JAVA) checkdig.account.DeAccountChecker test/account/87.lst > test/account/87.tmp
	less test/account/87.tmp

pb10:
	$(JAVA) checkdig.account.DeAccountChecker test/account/pb10.lst > test/account/pb10.tmp
	perl test/account/eval_method.pl test/account/pb10.tmp | tee test/account/pb10.eval
#------------------
blz1:
	$(JAVA) checkdig.blz.BlzCheckMap          test/account/pztest.dat
#------------------
iban:
	$(JAVA) org.teherba.checkdig.IbanChecker checkdig/IbanChecker.java
#------------------
isin1:
	$(JAVA) org.teherba.checkdig.IsinChecker src/checkdig/IsinChecker.java
isin2:
	perl test/isin/boersenspiel.pl > test/isin/boersenspiel.tmp
	$(JAVA) org.teherba.checkdig.IsinChecker test/isin/boersenspiel.tmp
isin3:
	perl isin3.pl
#------------------
vatid:
	$(JAVA) org.teherba.checkdig.VatIdChecker checkdig/VatIdChecker.java

# utility targets
xgrep:
	find src -name "*.java" | xargs -l grep -iH $(GREP)
tgz:
	tar czvf checkdig_`/bin/date +%Y%m%d`.tgz src test etc web *.wsd* *.xml makefile

#
# sum  548297 !    4861 ?   0.009, now correct 29:
# sum  549818 !    3340 ?   0.006, now correct 21:
# sum  549933 !    3225 ?   0.006, now correct 30:
# sum  549961 !    3197 ?   0.006, now correct 87:
# sum  550100 !    3058 ?   0.006, now correct 22:
# sum  550146 !    3012 ?   0.005,