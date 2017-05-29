-- Uebersicht der IBAN-Regeln (im Extranet der Deutschen Bundesbank)
-- @(#) $Id$
-- 2014-01-22: with okey, nkey, oacct
-- 2014-01-20, Georg Fischer
DROP   TABLE IF EXISTS iban_rules;
CREATE TABLE           iban_rules
    ( orid          VARCHAR( 8)  NOT NULL -- original rule id
    , okey          VARCHAR( 8)  NOT NULL -- original key schema
    , oblz          VARCHAR( 8)  NOT NULL -- original BLZ
    , oacct         VARCHAR(32)  NOT NULL -- original account no
    , ocheck        VARCHAR( 8)           -- original checking of account no
    , nblz          VARCHAR( 8)           -- next BLZ
    , nacct         VARCHAR(24)           -- next account no
    , nbic          VARCHAR(11)           -- next BIC
    , niban         VARCHAR(22)           -- next IBAN
    , nrid          VARCHAR( 8)           -- next rule id
    , nkey          VARCHAR( 8)           -- next key schema
    , key index1 (orid, oblz, oacct)
    );
COMMIT;
