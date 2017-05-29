-- Uebersicht der IBAN-Regeln (im Extranet der Deutschen Bundesbank)
-- How to access the table iban_rules
-- @(#) $Id$
-- 2014-01-21, Georg Fischer
DROP   TABLE IF EXISTS iban_access;
CREATE TABLE           iban_access
    ( orid          VARCHAR( 8)  NOT NULL
    , okey          VARCHAR( 8)  NOT NULL
    , primary key   (orid)
    );
COMMIT;
