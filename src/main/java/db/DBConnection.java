/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JLabel;
import java.sql.PreparedStatement;
import oracle.jdbc.pool.OracleDataSource;


/**
 * Třída pro připojení a udržování spojení s DB.
 */
public class DBConnection {
	private Connection conn; // udržované spojení
	private String login; // uživatelské jméno
	private String password; // heslo
	
    /**
	 * Provede přihlášení zadaného uživatele do DB.
	 * @param login uživatelské jméno
	 * @param password heslo
	 * @param warning předaný jLabel pro vypsání chybového hlášení
	 * @return true, pokud bylo přihlášení úspěšné
	 */
//	public Connection pripoj() {
//        // SQLite connection string
//        String url = "jdbc:oracle:thin:@gort.fit.vutbr.cz:1521:gort";
//        
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(url, "xstrmi08", "Fishbone");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//        return conn;
//    }
//        
        public boolean connect() {
		this.login = System.getProperty("login");
		this.password = System.getProperty("password");
		closeConnection();
		try {
			OracleDataSource ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@gort.fit.vutbr.cz:1521:gort");
			ods.setUser(login);
			ods.setPassword(password);
			conn = ods.getConnection();
			return true;
		} 
		catch (SQLException sqlEx) {
		//	if (warning != null)
		//		warning.setText("<html>"+ sqlEx.getMessage() +"</html>");
			closeConnection();
			return false;
		}
	}
        
        public boolean connect2(String login, String password) {
		System.out.println("Connect...");
		this.login = login;
		this.password = password;
		try {
			OracleDataSource ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@gort.fit.vutbr.cz:1521:gort");
			ods.setUser(login);
			ods.setPassword(password);
			conn = ods.getConnection();
			return true;
		} 
		catch (SQLException sqlEx) {
			closeConnection();
			return false;
		}
	}
	
	/**
	 * Získá vytvoření spojení nebo jej aktualizuje a rovněž vrátí.
	 * @return 
	 */
	public Connection getConnection()
	{
		if (conn == null) // ??? možná nefunguje přihlášení po timeoutu
			connect();
		return conn;
	}
	
	/**
	 * Uzavře spojení s DB.
	 */
	public void closeConnection(){
		try {
			if (conn != null)
				conn.close();	
		}
		catch (SQLException sqlEx) {
            System.err.println("SQLException (closeConnection): " + sqlEx.getMessage());
        }		
	}
        
         
}