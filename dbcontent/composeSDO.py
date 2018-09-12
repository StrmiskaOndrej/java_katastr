#!/bin/python3

if __name__ == '__main__':

    prefix ="C:/tmp"
    areas = open(prefix+"/file.sql", "r")
    struc = open(prefix+"/kat_sdo_gen_struc.sql", "r")
    dates = open("dates.txt","r")
    gen = open(prefix+"/sdo.sql", "w")

    templ = """INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    %s,
    seq_oid.nextval,
    %u,
    timestamp '%s 08:00:00',
    future() );"""


    templ2 = """INSERT INTO kat_map_objects (region, oid,  type, vfrom, vto) values (
    %s,
    seq_oid.nextval,
    %u,
    timestamp '%s 08:00:00',
    future() ) RETURNING object_pk into okey;
    INSERT INTO kat_structures (object_pk, height) values (okey, 5);"""

    x = 0
    date = dates.readline().strip("\n")
    for name in areas:
        sdo = areas.readline().strip("\n")
        x+=1
        if x >= 5:
            x=0
            date = dates.readline().strip("\n")

        if date is "":
            dates.seek(0,0)
            date = dates.readline().strip("\n")

        print(templ % (sdo, 0, date), file=gen)


    print("declare okey number;", file=gen)
    print("begin", file=gen)
    x = 0
    date = dates.readline().strip("\n")
    for name in struc:
        sdo = struc.readline().strip("\n")
        x+=1
        if x >= 5:
            x=0
            date = dates.readline().strip("\n")

        if date is "":
            dates.seek(0,0)
            date = dates.readline().strip("\n")

        print(templ2 % (sdo, 1, date), file=gen)
    print("end;commit;", file=gen)


    areas.close()
    struc.close()
    dates.close()
    gen.close()

