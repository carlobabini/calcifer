package it.angelobabini.odkaggregatetools;

import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBHelper {

	public static Connection getConnection() throws Exception {
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/PostgreSQLDS");
		//Context initialContext = (Context) ctx.lookup("java:comp/env");
	    //DataSource ds = (DataSource) initialContext.lookup("jdbc/PostgreSQLDS");

		return ds.getConnection(); 
	}
	
	public static void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch(Exception e) { }
	}
}
