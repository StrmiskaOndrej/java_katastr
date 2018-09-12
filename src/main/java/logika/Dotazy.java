/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logika;

import gui_hlavni.HlavniJFrame;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author strmiska
 */
public class Dotazy {
    public static int zjistiPKVlastnika(int objectPK, int indexVlastnika){
        int personPK =0;
        String sql = "select KAT_PERSONS.PERSON_PK FROM KAT_MAP_OBJECTS\n" +
"INNER JOIN KAT_OWNERSHIP ON KAT_MAP_OBJECTS.object_pk = KAT_OWNERSHIP.OBJECT_FK\n" +
"INNER JOIN KAT_PERSONS ON KAT_OWNERSHIP.PERSON_FK = KAT_PERSONS.PERSON_PK\n" +
"WHERE KAT_MAP_OBJECTS.object_pk=?";
        
        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
         pstmt.setInt(1, objectPK);
         ResultSet rs = pstmt.executeQuery();
         while (rs.next()) {
               if(rs.getRow() == indexVlastnika +1){
                    personPK = rs.getInt(1); 
               }               
            }
     }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
    return personPK;
 }


public static Vlastnik zjistiUdajeVlastnika(int objectPK, int indexVlastnika){
        Vlastnik vlastnikSPodilem = new Vlastnik();
        int personPK =0;
        String sql = "select KAT_PERSONS.PERSON_PK, KAT_PERSONS.NAME, KAT_PERSONS.SURNAME, KAT_OWNERSHIP.PROPERTY_SHARE  FROM KAT_MAP_OBJECTS\n" +
"INNER JOIN KAT_OWNERSHIP ON KAT_MAP_OBJECTS.object_pk = KAT_OWNERSHIP.OBJECT_FK\n" +
"INNER JOIN KAT_PERSONS ON KAT_OWNERSHIP.PERSON_FK = KAT_PERSONS.PERSON_PK\n" +
"WHERE KAT_MAP_OBJECTS.object_pk=?";
        
        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
         pstmt.setInt(1, objectPK);
         ResultSet rs = pstmt.executeQuery();
         while (rs.next()) {
               if(rs.getRow() == indexVlastnika +1){
                    vlastnikSPodilem.personPK = rs.getInt(1);
                    vlastnikSPodilem.jmeno = rs.getString(2);
                    vlastnikSPodilem.prijmeni = rs.getString(3);
                    vlastnikSPodilem.podil = (int)((rs.getFloat(4))*100);
               }               
            }
     }
    catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
    return vlastnikSPodilem;
 }

 public static float vypoctiPlochu(int objectPK){
        String sql = "SELECT SUM(SDO_GEOM.SDO_AREA(REGION, 1)) plocha_objektu FROM KAT_MAP_OBJECTS WHERE Object_PK = ?";
        float plocha = 0;
        try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql)){ 
        pstmt.setInt(1, objectPK);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
                 plocha = rs.getFloat(1);     
            }
            
    }catch (SQLException sqlEx) {
        System.err.println("SQLException: " + sqlEx.getMessage());
    }
        String sql2 = "update KAT_MAP_OBJECTS set SURFACE = ? where OBJECT_PK = ?";

       
       try(PreparedStatement pstmt = HlavniJFrame.dtb.getConnection().prepareStatement(sql2))
		{
                pstmt.setFloat(1, plocha);     
                pstmt.setInt(2, objectPK);
 
        pstmt.executeUpdate();
	}catch(SQLException SQLEx)
        {
            System.err.println("SQLxception: " + SQLEx.getMessage());
	}
       return plocha;
        
    }

}