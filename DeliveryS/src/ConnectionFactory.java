import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {
	public Connection getConnection() {
	     try {
	         return DriverManager.getConnection(
	 "jdbc:mysql://localhost:3306/DBD?zeroDateTimeBehavior=convertToNull", "root", ""); // No lugar de "trabalho" colocar sua senha do mysql
	     } catch (SQLException e) {
	         throw new RuntimeException(e);
	     }
	   // "jdbc:mysql://localhost:3306/SGF?zeroDateTimeBehavior=convertToNull", "root", "trabalho"); // No lugar de "trabalho" colocar sua senha do mysql
	    	 
	 }
	
}

