--  Checks that every nblz has the proper nbic
--  @(#) $Id: extract_test.xsl 62 2008-12-03 19:25:15Z gfis $
--  2014-01-22: Georg Fischer
--  must return an empty result set
select r.orid
    ,  b.fuehrend
    ,  r.nblz
	,  b.bic
	,  r.nbic
from   iban_rules r
    ,  bankleitzahlen b
where  r.nblz =  b.blz
  and  fuehrend = '1'
  and  length(r.nblz) <> 0
  and  length(b.bic)  <> 0
  and  length(r.nbic) <> 0
  and  r.nbic <> b.bic
order by 1;

-- Checks that the blz in every niban corresponds to nbic (if present)
select r.orid
    ,  r.niban
    ,  substr(r.niban, 5, 8)
	,  b.bic
	,  r.nbic
from   iban_rules r
    ,  bankleitzahlen b
where  length(r.niban) <> 0
  and  substr(r.niban, 5, 8) =  b.blz
  and  length(r.nbic) <> 0
  and  r.nbic <> b.bic
order by 1;
