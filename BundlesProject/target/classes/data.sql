ALTER TABLE bundles ADD CONSTRAINT IF NOT EXISTS ExpirationDateError CHECK ( expiration<>null AND ((expiration < CURRENT_TIMESTAMP()  AND activated=false) OR (expiration > CURRENT_TIMESTAMP())) );
ALTER TABLE bundles ADD CONSTRAINT IF NOT EXISTS AvailabilityDateError CHECK ( (availability > CURRENT_TIMESTAMP() AND activated=false) OR (availability < CURRENT_TIMESTAMP()));
ALTER TABLE bundles ADD CONSTRAINT IF NOT EXISTS NameLengthError CHECK ( LENGTH(name) > 0 AND  LENGTH(name) < 101);
ALTER TABLE bundles ADD CONSTRAINT IF NOT EXISTS ExpirationBeforeThanAvailabilityError CHECK ( (expiration<>null)AND(expiration>availability) );

/*___________Bundles(code, name, price, activated, expiration, availability)___________*/


/***************INITIALIZE DATA ONLY IF DATABASE FILE DOESN'T EXIST****************
___________________________________________________________________________________
INSERT INTO Bundles(code, name, price, activated, expiration, availability) VALUES(101, 'tv', 25, true, '2030-3-4 12:32:23', '2010-2-2 23:23:21');
INSERT INTO Bundles(code, name, price, activated, expiration, availability) VALUES(102, 'adsl', 16.9, true, '2030-3-4 12:32:23', '2005-2-2 23:23:21');
INSERT INTO Bundles(code, name, price, activated, expiration, availability) VALUES(103, 'vdsl', 26.9, true, '2040-3-4 12:32:23', '2015-2-2 23:23:21');
INSERT INTO Bundles(code, name, price, activated, expiration, availability) VALUES(104, 'callerID', 0, true, null, '2000-2-2 23:23:21');
INSERT INTO Bundles(code, name, price, activated, expiration, availability) VALUES(105, '3D-TV', 45, FALSE, null, '2021-2-2 23:23:21');
*/


/***************IF AVAILABILITY DATE IS AFTER CURRENT DATE THEN BUNDLE MUST BE DEACTIVATED****************/
/*______________________________________________ERROR_____________________________________________________*/
--INSERT INTO Bundles(code, name, price, activated, expiration, availability) VALUES(100000000006, 'TEST', 0, TRUE, null, '2021-2-2 23:23:21');


/***************IF EXPIRATION DATE IS BEFORE CURRENT DATE THEN BUNDLE MUST BE DEACTIVATED****************/
/*______________________________________________ERROR_____________________________________________________*/
--INSERT INTO Bundles(code, name, price, activated, expiration, availability) VALUES(100000000007, 'TEST', 0, TRUE, '2020-2-2 23:23:21', '2001-2-2 23:23:21');

 
/***************EXPIRATION DATE MUST BE AFTER AVAILABILITY DATE****************/
/*_________________________________ERROR______________________________________*/
--INSERT INTO Bundles(code, name, price, activated, expiration, availability) VALUES(100000000008, 'TEST', 0, TRUE, '2021-2-2 23:23:21', '2022-2-2 23:23:21');
