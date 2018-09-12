/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logika;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author strmiska
 */
public class Datum {
    public static String zmenaNaString(String datum){
        
        String noveDatum = datum.substring(0, 10);
        
        return noveDatum;
    }

public static java.sql.Timestamp prevedNaTimestamp(String datum) {

	java.util.Date today = new java.util.Date();
        
    try {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    Date parsedDate = dateFormat.parse(datum);
    java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
    return timestamp;
    } catch(Exception e) { //this generic but you can control another types of exception
        return null;
    // look the origin of excption 
}
        
	
        

}
    
}
