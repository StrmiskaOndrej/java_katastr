-- Author Imrich Štoffa xstoff02@stud.fit.vutbr.cz
-- created: 2017-10-08


ALTER SESSION SET NLS_DATE_FORMAT='YYYY-MM-DD';
alter session set CURRENT_SCHEMA = XSTRMI08;

-- Drop all relevant tables
drop table kat_structures CASCADE CONSTRAINTS ;
drop table kat_pictures CASCADE CONSTRAINTS ;
drop table kat_map_objects CASCADE CONSTRAINTS ;
drop table kat_ownership CASCADE CONSTRAINTS ;
drop table kat_officers CASCADE CONSTRAINTS ;
drop table kat_persons CASCADE CONSTRAINTS ;
drop sequence seq_oid;

CREATE OR REPLACE FUNCTION future
  RETURN timestamp DETERMINISTIC
AS
  BEGIN
    RETURN timestamp '9999-12-31 23:59:59.999999';
  END future;

-- Table with map objects, it is generalisation of all subclasses
-- created by merging enviroment, infrastructure, structures, territories, and properties
-- Identities of objects are assigned across many time intervals.
CREATE TABLE kat_map_objects (
  object_pk NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
  region    SDO_GEOMETRY,
  --oid for all map objects represents object across time
  oid       number not null,
  type      int,
  surface   real,
  vfrom     TIMESTAMP not null,
  vto       TIMESTAMP not null-- future() if opened interval
);


CREATE TABLE kat_structures (
  object_pk      NUMBER,
  type           int,
  living_surface NUMBER,
  height         NUMBER
);

-- Pictures of point of interests across time, assigned through OID
create TABLE kat_pictures (
  picture_pk NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
  photo    ORDSYS.ORDimage,
  photo_si ORDSYS.SI_StillImage,
  photo_ac ORDSYS.SI_AverageColor,
  photo_ch ORDSYS.SI_ColorHistogram,
  photo_pc ORDSYS.SI_PositionalColor,
  photo_tx ORDSYS.SI_Texture,

  title    VARCHAR(50),
  taken    TIMESTAMP,

  object_fk number
);

-- Table of entities who can own a property
create table kat_persons (
  person_pk NUMBER GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1),
  name      varchar(100),
  surname   varchar(100),
  extra     varchar(50),
  contact   varchar(15),
  birth     DATE,
  birth_id  VARCHAR(12),
  sex       int
);

-- Extra information for people who work at geodesy
create table kat_officers (
  officer_pk  NUMBER,
  login       varchar(10),
  passwd      varchar(8),    --name and passwd hashed together
  employed_as smallint -- 0-no, 1-officer, 2-admin
);

-- Relational table, describes validity in time and share
create table kat_ownership (
  person_fk      number,
  officer_fk     number,
  object_fk      number,
  --Fictive number of contract
  property_share real,
  vfrom          TIMESTAMP not null,
  vto            timestamp not null
);

CREATE SEQUENCE seq_oid
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

alter table kat_map_objects add ( PRIMARY KEY (object_pk),
                                  UNIQUE (OBJECT_PK, VFROM, VTO));

alter table kat_structures add ( PRIMARY KEY (object_pk),
                                 FOREIGN KEY (object_pk)
                                   REFERENCES kat_map_objects(object_pk));

alter table kat_pictures add ( PRIMARY key (picture_pk),
                               foreign key (object_fk)
                                 REFERENCES kat_map_objects(object_pk));

alter table kat_persons add ( PRIMARY Key (person_pk));

alter table kat_officers add ( PRIMARY Key (officer_pk),
                               FOREIGN KEY (officer_pk)
                                 REFERENCES kat_persons(person_pk));

alter table kat_ownership add ( PRIMARY KEY (person_fk, officer_fk, object_fk, vfrom, vto),
                                foreign key (object_fk) references kat_map_objects(object_pk),
                                foreign key (person_fk) references kat_persons(person_pk),
                                foreign key (officer_fk) references kat_officers(officer_pk));

insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');
insert into kat_persons (extra) VALUES (' ');

UPDATE KAT_PERSONS SET NAME = 'Peter', SURNAME = 'Parker', EXTRA = null, CONTACT = '119946098', BIRTH = TO_DATE('1956-01-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '195601194357', SEX = 0 WHERE PERSON_PK = 1;
UPDATE KAT_PERSONS SET NAME = 'Jena', SURNAME = 'Brown', EXTRA = null, CONTACT = '125910084', BIRTH = TO_DATE('1959-09-15 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '195909152208', SEX = 1 WHERE PERSON_PK = 2;
UPDATE KAT_PERSONS SET NAME = 'Alojz', SURNAME = 'Skacel', EXTRA = null, CONTACT = '178019824', BIRTH = TO_DATE('1959-09-23 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '195909237901', SEX = 0 WHERE PERSON_PK = 3;
UPDATE KAT_PERSONS SET NAME = 'Martin', SURNAME = 'Luther', EXTRA = null, CONTACT = '178948624', BIRTH = TO_DATE('1964-02-09 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '196402092489', SEX = 0 WHERE PERSON_PK = 4;
UPDATE KAT_PERSONS SET NAME = 'Petra', SURNAME = 'Pretty', EXTRA = null, CONTACT = '116958213', BIRTH = TO_DATE('1966-01-08 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '196601086276', SEX = 1 WHERE PERSON_PK = 5;
UPDATE KAT_PERSONS SET NAME = 'Wade', SURNAME = 'Vilson', EXTRA = null, CONTACT = '138200318', BIRTH = TO_DATE('1967-08-31 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '196708312998', SEX = 0 WHERE PERSON_PK = 6;
UPDATE KAT_PERSONS SET NAME = 'Bruce', SURNAME = 'Wayne', EXTRA = null, CONTACT = '106411198', BIRTH = TO_DATE('1976-10-11 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '197610119197', SEX = 0 WHERE PERSON_PK = 7;
UPDATE KAT_PERSONS SET NAME = 'Natalia', SURNAME = 'Romanova', EXTRA = 'Allanovna', CONTACT = '110695147', BIRTH = TO_DATE('1979-02-11 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '197902111370', SEX = 1 WHERE PERSON_PK = 8;
UPDATE KAT_PERSONS SET NAME = 'Loki', SURNAME = 'Laufeyson', EXTRA = null, CONTACT = '131903672', BIRTH = TO_DATE('1982-03-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '198203192449', SEX = 0 WHERE PERSON_PK = 9;
UPDATE KAT_PERSONS SET NAME = 'Galactus', SURNAME = 'Mighty', EXTRA = null, CONTACT = '122894527', BIRTH = TO_DATE('1984-04-19 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '198404192765', SEX = 0 WHERE PERSON_PK = 10;
UPDATE KAT_PERSONS SET NAME = 'Maz', SURNAME = 'Eisenhardt', EXTRA = 'Magneto', CONTACT = '100881535', BIRTH = TO_DATE('1987-11-12 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '198711121025', SEX = 0 WHERE PERSON_PK = 11;
UPDATE KAT_PERSONS SET NAME = 'Charles ', SURNAME = 'Xavier', EXTRA = 'Francis', CONTACT = '160562848', BIRTH = TO_DATE('1988-08-30 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '198808301175', SEX = 0 WHERE PERSON_PK = 12;
UPDATE KAT_PERSONS SET NAME = 'Rocket', SURNAME = 'Racoon', EXTRA = null, CONTACT = '196257683', BIRTH = TO_DATE('1990-01-23 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '199001238389', SEX = 0 WHERE PERSON_PK = 13;
UPDATE KAT_PERSONS SET NAME = 'Lena', SURNAME = 'Lenon', EXTRA = null, CONTACT = '158583461', BIRTH = TO_DATE('1991-01-28 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '199101285674', SEX = 1 WHERE PERSON_PK = 14;
UPDATE KAT_PERSONS SET NAME = 'Merlin', SURNAME = 'Monroe', EXTRA = null, CONTACT = '197891104', BIRTH = TO_DATE('1993-05-29 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '199305296341', SEX = 1 WHERE PERSON_PK = 15;
UPDATE KAT_PERSONS SET NAME = 'Ororo', SURNAME = 'Monroe', EXTRA = null, CONTACT = '159756465', BIRTH = TO_DATE('1994-02-13 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), BIRTH_ID = '199402139741', SEX = 1 WHERE PERSON_PK = 16;
Commit;

ALTER TABLE KAT_PERSONS MODIFY NAME DEFAULT NULL NOT NULL;

--Insert persons from csv in dbcontent/persons.csv
--Insert admins
select * from kat_persons;
insert into kat_officers (officer_pk, login, passwd, employed_as)
  (select person_pk as officer_pk, lower(concat(substr(name,1,1),surname)) as login,
          'passwd' as passwd, 1 as employed_as from kat_persons
   where (name like 'Alojz')
          and person_pk not in (select officer_pk from kat_officers));

insert into kat_officers (officer_pk, login, passwd, employed_as)
  (select person_pk as officer_pk, lower(concat(substr(name,1,1),surname)) as login,
          'passwd' as passwd, 1 as employed_as from kat_persons
   where name = 'Ororo' and person_pk not in (select officer_pk from kat_officers));

--Insert officers
insert into kat_officers (officer_pk, login, passwd, employed_as)
  (select person_pk as officer_pk, lower(concat(substr(name,1,1),surname)) as login,
          'passwd' as passwd, 0 as employed_as from kat_persons
   where (((name = 'Natalia' and surname = 'Romanova') or (name = 'Loki' and surname = 'Laufeyson'))
          and person_pk not in (select officer_pk from kat_officers)));
commit;

--template
--CREATE OR REPLACE PROCEDURE kat_pictures_generateFeatures IS
--  cursor c is select * from kat_photos for update;
--  si ORDSYS.SI_StillImage;
--  BEGIN
--    for cp in c loop
--      si := new SI_StillImage(cp.photo.getContent());
--      update products p set photo_si = si,
--        photo_ac = SI_AverageColor(si),      photo_ch = SI_ColorHistogram(si),
--        photo_pc = SI_PositionalColor(si),   photo_tx = SI_Texture(si)
--      where p.picture_pk = cp.picture_pk;
--    end loop;
--  END;
--/


-- nazvy tabulky a sloupce musi byt velkymi pismeny
DELETE FROM USER_SDO_GEOM_METADATA WHERE
  TABLE_NAME = 'KAT_MAP_OBJECTS' AND COLUMN_NAME = 'REGION';

INSERT INTO USER_SDO_GEOM_METADATA VALUES (
  'kat_map_objects', 'region',
  -- souradnice X,Y s hodnotami 0-150 a presnosti 0.1 bod (velikost mrizky a hustota budu v planu z prikladu, napr. kulate rohy komunikace s presnosti 1 bod a stromy v zeleni s presnosti 0.1 bod)
  SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 10000, 0.01), SDO_DIM_ELEMENT('Y', 0, 10000, 0.01)),
  -- lokalni (negeograficky) souradnicovy system (v analytickych fcich neuvadet jednotky)
  NULL
);

COMMIT;


-- inserti

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
-- s udanim presnosti
SELECT region, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(region, 0.01) valid -- 0.1=presnost
FROM kat_map_objects;
-- bez udani presnosti (presne dle nastaveni v metadatech)
SELECT m.oid, m.region.ST_IsValid()
FROM kat_map_objects m;

--Generated from composeSDO.py
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2352.31,1690.02, 2369.70,1690.02, 2381.39,1729.55, 2382.29,1732.59, 2352.31,1732.59, 2352.31,1690.02)),
    seq_oid.nextval,
    0,
    timestamp '1992-05-04 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2456.72,1684.83, 2499.29,1684.83, 2525.85,1724.33, 2483.29,1724.35, 2456.72,1684.83)),
    seq_oid.nextval,
    0,
    timestamp '1992-05-04 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2536.10,1684.83, 2580.43,1684.83, 2580.43,1724.31, 2562.65,1724.32, 2536.10,1684.83)),
    seq_oid.nextval,
    0,
    timestamp '1992-05-04 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2666.57,1684.81, 2708.52,1684.81, 2709.17,1724.27, 2666.62,1724.29, 2666.57,1684.81)),
    seq_oid.nextval,
    0,
    timestamp '1992-05-04 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2755.53,1684.81, 2820.54,1684.81, 2794.67,1724.26, 2756.18,1724.26, 2755.53,1684.81)),
    seq_oid.nextval,
    0,
    timestamp '1992-11-10 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2357.73,1767.93, 2387.71,1767.93, 2388.72,1771.34, 2400.30,1810.50, 2357.73,1810.50, 2357.73,1767.93)),
    seq_oid.nextval,
    0,
    timestamp '1992-11-10 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2614.79,1724.31, 2614.82,1684.82, 2635.22,1684.82, 2635.27,1724.30, 2614.79,1724.31)),
    seq_oid.nextval,
    0,
    timestamp '1992-11-10 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2427.08,1809.25, 2415.50,1770.09, 2453.41,1770.08, 2427.08,1809.25)),
    seq_oid.nextval,
    0,
    timestamp '1992-11-10 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2486.11,1776.61, 2521.70,1776.60, 2521.99,1815.77, 2459.78,1815.77, 2486.11,1776.61)),
    seq_oid.nextval,
    0,
    timestamp '1992-11-10 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2666.52,1776.58, 2709.09,1776.57, 2709.30,1815.76, 2666.73,1815.76, 2666.52,1776.58)),
    seq_oid.nextval,
    0,
    timestamp '1994-01-03 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2445.70,1724.36, 2405.65,1724.38, 2393.96,1684.84, 2419.13,1684.84, 2445.70,1724.36)),
    seq_oid.nextval,
    0,
    timestamp '1994-01-03 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2581.37,1776.59, 2623.95,1776.58, 2624.16,1815.77, 2581.59,1815.77, 2581.37,1776.59)),
    seq_oid.nextval,
    0,
    timestamp '1994-01-03 08:00:00',
    future() );
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2751.76,1776.57, 2794.71,1776.57, 2794.54,1815.76, 2751.97,1815.76, 2751.76,1776.57)),
    seq_oid.nextval,
    0,
    timestamp '1994-01-03 08:00:00',
    future() );
declare okey number;
begin
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2356.84,1705.06, 2368.74,1705.08, 2371.25,1725.13, 2359.59,1725.13, 2357.25,1719.63, 2356.84,1705.06)),
    seq_oid.nextval,
    1,
    timestamp '1994-03-11 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2623.10,1730.30, 2620.79,1710.54, 2633.16,1710.57, 2635.50,1730.30, 2623.10,1730.30)),
    seq_oid.nextval,
    1,
    timestamp '1994-03-11 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2700.86,1730.38, 2700.92,1710.76, 2718.71,1710.80, 2718.65,1730.38, 2700.86,1730.38)),
    seq_oid.nextval,
    1,
    timestamp '1994-03-11 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2612.26,1803.27, 2611.54,1793.66, 2621.97,1793.66, 2621.97,1784.69, 2629.87,1784.69, 2630.05,1803.25, 2612.26,1803.27)),
    seq_oid.nextval,
    1,
    timestamp '1994-03-11 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2422.56,1796.79, 2422.62,1778.28, 2434.36,1778.28, 2436.93,1796.77, 2422.56,1796.79)),
    seq_oid.nextval,
    1,
    timestamp '1994-06-08 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2784.94,1803.23, 2784.99,1784.75, 2800.47,1784.75, 2800.47,1803.22, 2784.94,1803.23)),
    seq_oid.nextval,
    1,
    timestamp '1994-06-08 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2575.18,1728.60, 2575.12,1708.78, 2587.52,1708.81, 2587.20,1728.60, 2575.18,1728.60)),
    seq_oid.nextval,
    1,
    timestamp '1994-06-08 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2529.96,1781.12, 2528.16,1799.37, 2505.52,1799.37, 2505.03,1781.15, 2510.73,1781.14, 2511.27,1790.41, 2520.26,1789.87, 2519.54,1781.13, 2529.96,1781.12)),
    seq_oid.nextval,
    1,
    timestamp '1994-06-08 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2353.32,1783.96, 2354.56,1783.16, 2365.24,1783.16, 2365.58,1801.88, 2353.32,1801.90, 2353.32,1783.96)),
    seq_oid.nextval,
    1,
    timestamp '1994-06-08 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2406.75,1724.76, 2406.57,1704.76, 2427.78,1704.81, 2427.42,1716.64, 2415.91,1716.08, 2415.55,1724.76, 2406.75,1724.76)),
    seq_oid.nextval,
    1,
    timestamp '1996-02-28 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2780.02,1730.40, 2778.54,1710.83, 2798.58,1710.86, 2798.58,1730.40, 2780.02,1730.40)),
    seq_oid.nextval,
    1,
    timestamp '1996-02-28 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    SDO_GEOMETRY(2003, NULL, NULL, SDO_ELEM_INFO_ARRAY(1, 1003, 1), SDO_ORDINATE_ARRAY(2668.93,1803.98, 2670.75,1785.42, 2681.44,1785.42, 2681.23,1796.62, 2693.10,1796.62, 2692.31,1803.95, 2668.93,1803.98)),
    seq_oid.nextval,
    1,
    timestamp '1996-02-28 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);
end;commit;

begin
  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    SELECT person_pk, officer_pk, object_pk, 1.0, vfrom, future()
    from (SELECT *
          FROM kat_map_objects
            CROSS JOIN (SELECT *
                        FROM kat_persons
                          CROSS JOIN kat_officers
                        where person_pk <> officer_pk
                              and login = 'omonroe'
                              and surname like 'Laufeyson')
          where vto = future()
    )
    where rownum = 1); --generally does not work

  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    select person_pk, officer_pk, object_pk, 0.5, vfrom, future()
    from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
           FROM (SELECT *
                 FROM kat_map_objects
                   CROSS JOIN (SELECT *
                               FROM kat_persons
                                 CROSS JOIN kat_officers
                               WHERE person_pk <> officer_pk
                                     AND login = 'omonroe'
                                     AND surname LIKE 'Laufeyson')
                 WHERE vto = future()
           ) where rownum < 15 and object_pk not in(select object_pk from kat_structures) --limit for performance
    )
    WHERE rn = 2
  );

  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    select person_pk, officer_pk, object_pk, 0.5, vfrom, future()
    from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
           FROM (SELECT *
                 FROM kat_map_objects
                   CROSS JOIN (SELECT *
                               FROM kat_persons
                                 CROSS JOIN kat_officers
                               WHERE person_pk <> officer_pk
                                     AND login = 'omonroe'
                                     AND surname LIKE 'Xavier')
                 WHERE vto = future()
           ) where rownum < 15 and object_pk not in(select object_pk from kat_structures) --limit for performance
    )
    WHERE rn = 2
  );

  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    select person_pk, officer_pk, object_pk, 1, vfrom, future()
    from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
           FROM (SELECT *
                 FROM kat_map_objects
                   CROSS JOIN (SELECT *
                               FROM kat_persons
                                 CROSS JOIN kat_officers
                               WHERE person_pk <> officer_pk
                                     AND login = 'omonroe'
                                     AND surname LIKE 'Xavier')
                 WHERE vto = future()
           ) where rownum < 15 and object_pk not in(select object_pk from kat_structures) --limit for performance
    )
    WHERE rn = 3
  );

  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    select person_pk, officer_pk, object_pk, 1, vfrom, future()
    from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
           FROM (SELECT *
                 FROM kat_map_objects
                   CROSS JOIN (SELECT *
                               FROM kat_persons
                                 CROSS JOIN kat_officers
                               WHERE person_pk <> officer_pk
                                     AND login = 'nromanova'
                                     AND surname LIKE 'Xavier')
                 WHERE vto = future()
           ) where rownum < 15 and object_pk not in(select object_pk from kat_structures) --limit for performance
    )
    WHERE rn = 4
  );

  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    select person_pk, officer_pk, object_pk, 1, vfrom, future()
    from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
           FROM (SELECT *
                 FROM kat_map_objects
                   CROSS JOIN (SELECT *
                               FROM kat_persons
                                 CROSS JOIN kat_officers
                               WHERE person_pk <> officer_pk
                                     AND login = 'omonroe'
                                     AND surname LIKE 'Wayne')
                 WHERE vto = future()
           ) where rownum < 15 and object_pk not in(select object_pk from kat_structures) --limit for performance
    )
    WHERE rn = 5
  );

  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    select person_pk, officer_pk, object_pk, 0.5, vfrom, future()
    from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
           FROM (SELECT *
                 FROM kat_map_objects
                   CROSS JOIN (SELECT *
                               FROM kat_persons
                                 CROSS JOIN kat_officers
                               WHERE person_pk <> officer_pk
                                     AND login = 'omonroe'
                                     AND surname LIKE 'Wayne')
                 WHERE vto = future()
           ) where rownum < 15 and object_pk not in(select object_pk from kat_structures) --limit for performance
    )
    WHERE rn = 6
  );

  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    select person_pk, officer_pk, object_pk, 1, vfrom, future()
    from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
           FROM (SELECT *
                 FROM kat_map_objects
                   CROSS JOIN (SELECT *
                               FROM kat_persons
                                 CROSS JOIN kat_officers
                               WHERE person_pk <> officer_pk
                                     AND login = 'omonroe'
                                     AND surname LIKE 'Eisenhardt')
                 WHERE vto = future()
           ) where rownum < 15 and object_pk not in(select object_pk from kat_structures) --limit for performance
    )
    WHERE rn = 7
  );

  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    select person_pk, officer_pk, object_pk, 0.5, vfrom, future()
    from ( SELECT person_pk, officer_pk, object_pk,  vfrom, vto, rownum AS rn
           FROM (SELECT *
                 FROM kat_map_objects
                   CROSS JOIN (SELECT *
                               FROM kat_persons
                                 CROSS JOIN kat_officers
                               WHERE person_pk <> officer_pk
                                     AND login = 'omonroe'
                                     AND surname LIKE 'Racoon')
                 WHERE vto = future()
           ) where rownum < 15 and object_pk not in(select object_pk from kat_structures) --limit for performance
    )
    WHERE rn = 8
  );

  insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
    select person_pk, officer_pk, object_pk, 0.5, vfrom, future()
    from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
           FROM (SELECT *
                 FROM kat_map_objects
                   CROSS JOIN (SELECT *
                               FROM kat_persons
                                 CROSS JOIN kat_officers
                               WHERE person_pk <> officer_pk
                                     AND login = 'askacel'
                                     AND surname LIKE 'Romanova')
                 WHERE vto = future()
           ) where rownum < 15 and object_pk not in(select object_pk from kat_structures) --limit for performance
    )
    WHERE rn = 9
  );
end;
commit;

begin
insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
  select person_pk, officer_pk, object_pk, 1, vfrom, future()
  from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
         FROM (SELECT *
               FROM kat_map_objects
                 CROSS JOIN (SELECT *
                             FROM kat_persons
                               CROSS JOIN kat_officers
                             WHERE person_pk <> officer_pk
                                   AND login = 'omonroe'
                                   AND surname LIKE 'Eisenhardt')
               WHERE vto = future()
         ) where rownum < 15 and object_pk in(select object_pk from kat_structures) --limit for performance
  )
  WHERE rn = 1
);

insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
  select person_pk, officer_pk, object_pk, 1, vfrom, future()
  from ( SELECT person_pk, officer_pk, object_pk,  vfrom, vto, rownum AS rn
         FROM (SELECT *
               FROM kat_map_objects
                 CROSS JOIN (SELECT *
                             FROM kat_persons
                               CROSS JOIN kat_officers
                             WHERE person_pk <> officer_pk
                                   AND login = 'omonroe'
                                   AND surname LIKE 'Racoon')
               WHERE vto = future()
         ) where rownum < 15 and object_pk in(select object_pk from kat_structures) --limit for performance
  )
  WHERE rn = 2
);

insert into kat_ownership (person_fk, officer_fk, object_fk, property_share, vfrom, vto) (
  select person_pk, officer_pk, object_pk, 1, vfrom, future()
  from ( SELECT person_pk, officer_pk, object_pk, vfrom, vto, rownum AS rn
         FROM (SELECT *
               FROM kat_map_objects
                 CROSS JOIN (SELECT *
                             FROM kat_persons
                               CROSS JOIN kat_officers
                             WHERE person_pk <> officer_pk
                                   AND login = 'askacel'
                                   AND surname LIKE 'Romanova')
               WHERE vto = future()
         ) where rownum < 15 and object_pk in(select object_pk from kat_structures) --limit for performance
  )
  WHERE rn = 3
);
end;
commit;

