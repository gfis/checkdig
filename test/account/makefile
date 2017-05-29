#!/usr/bin/make

# @(#) $Id: makefile 71 2009-01-08 20:33:41Z gfis $
# 2009-01-07, Dr. Georg Fischer <punctum@punctum.com>

all: bav_gen

bav_gen:
	perl bav_gen.pl verify.ini | sort | tee bav.tst

