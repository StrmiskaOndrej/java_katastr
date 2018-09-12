alter session set current_schema = XSTRMI08;
--Add ownership

--Select actual snapshot of ...

-------------------------
--Select structures only
select m.oid, s.type, s.height
from KAT_STRUCTURES s
join KAT_MAP_OBJECTS m on s.object_pk=m.object_pk
where vto = FUTURE();
-------------------------

-------------------------
--Select territories only
select m.oid, s.type, s.height
from KAT_STRUCTURES s
  FULL JOIN KAT_MAP_OBJECTS m on s.object_pk=m.object_pk
where vto = FUTURE() and s.OBJECT_PK is null;
-------------------------

-------------------------
--Update of map structure -- actual
declare tmp number;
  graphix SDO_GEOMETRY; -- contains new
begin

  update KAT_MAP_OBJECTS set vto = systimestamp
  where vto = FUTURE() and oid = :var
  RETURNING OBJECT_PK, REGION into tmp, graphix;
  --RETURNING OBJECT_PK into tmp;

  graphix := :newSdo;
  --update graphix

  insert into KAT_MAP_OBJECTS (oid, region, type, surface, VFROM, vto) (
  select m.oid, graphix, m.type, surface, systimestamp, FUTURE()
  from kat_map_objects m
  where object_pk = tmp);

  --now do almost useless update
  update KAT_MAP_OBJECTS set vto = FUTURE()
  where vto = future() and oid = :var
  RETURNING OBJECT_PK into tmp;

  --RETURNING OBJECT_PK into tmp;
  --get new id and insert
  insert into KAT_STRUCTURES (
    select *
    from KAT_STRUCTURES
    where object_pk = tmp); --should fail if none or something like that

  --now updaate rest specifically
  --and finally check

--(select * from KAT_MAP_OBJECTS m join KAT_STRUCTURES s on s.OBJECT_PK=m.OBJECT_PK where vto is null and oid = :var));

  --if no overlap between territories ok
    --commit
  --else if edit
    --repeat editing phase with extra info
  --else
    --rollback
end;
-------------------------

-------------------------
--Under construction, update for ownership
DECLARE p
update KAT_OWNERSHIP set PROPERTY_SHARE = total;
(select PERSON_PK from KAT_PERSONS p where BIRTH_ID in :var);
select * from ... where vfrom <= :tst and vto

select m.oid from KAT_MAP_OBJECTS m where

select o.PERSON_FK, o.PROPERTY_SHARE
from KAT_OWNERSHIP o JOIN KAT_MAP_OBJECTS m
  --pick one of oid assigned keys from over time
    on o.OBJECT_FK in (select m.OBJECT_PK from KAT_MAP_OBJECTS where m.OID = :object)
WHERE VTO = FUTURE() and (o.OBJECT_FK, o.oid) in
      (select o.OFFICER_FK)

select sum(o.PROPERTY_SHARE) edits from KAT_OWNERSHIP o WHERE VTO = FUTURE() and
-------------------------

-------------------------
--Select object keys for given OID
select OBJECT_PK from KAT_OWNERSHIP join KAT_MAP_OBJECTS m
    on OBJECT_FK in
       (select OBJECT_PK from KAT_MAP_OBJECTS
        where oid in
              (SELECT oid from KAT_MAP_OBJECTS where OBJECT_FK=m.OBJECT_PK)) where oid = 5;
-------------------------

-------------------------
--Trivial spartial query
SELECT SDO_GEOM.SDO_AREA(REGION, 1) plocha_objektu
FROM KAT_MAP_OBJECTS where OBJECT_PK = :var ;
-------------------------


