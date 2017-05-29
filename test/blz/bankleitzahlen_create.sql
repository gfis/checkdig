-- Bankleitzahlendatei BLZ2_jjjjmmtt.txt der Deutschen Bundesbank
-- c.f. 14 Felder, mit IBAN-Regeln
-- @(#) $Id$
-- 2014-01-17: _regel, _version separate fields
-- 2014-01-15, Dr. Georg Fischer
DROP   TABLE IF EXISTS bankleitzahlen;
CREATE TABLE           bankleitzahlen
    ( blz           VARCHAR(8)  NOT NULL -- key
    , fuehrend      CHAR(1)     -- 1: fuehrend, 2 = nicht fuehrend
    , institut      VARCHAR(58)
    , plz           VARCHAR(5)
    , ort           VARCHAR(35)
    , inst          VARCHAR(27)
    , instNr        VARCHAR(5)
    , bic           VARCHAR(11)
    , pzMethode     VARCHAR(2)
    , satzNr        VARCHAR(6)
    , aendKnz       CHAR(1)     -- (A)ddition, (D)eletion, (U)nchanged, (M)odified
    , loeschung     CHAR(1)     -- 0: fortbestehend, 1: zur Loeschung vorgesehen
    , folgeBlz      VARCHAR(8)  -- Nachfolge-BLZ wenn loeschung = 1
    , ibanRegel     VARCHAR(4)
    , ibanVersion   VARCHAR(2)
    , key blzIndex  (blz, fuehrend)
    );
COMMIT;
