package it.angelobabini.calcifer.backend;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Setting implements Serializable {
	private static final long serialVersionUID = 9106782590772796666L;
	
	public static Object getRaw(String key) {		
		String value = null;

		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null; 

		try {
			conn = DBHelper.getConnection();
			String sql = "SELECT value FROM settings WHERE key='"+key+"'";
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if (resultSet.next()) {
				value = resultSet.getString("value");
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
			} catch (SQLException e) { }
			try {
				statement.close();
			} catch (SQLException e) { }
			DBHelper.closeConnection(conn);
		}

		return value;
	}

	public static String getAsString(String key) {
		Object raw = getRaw(key);
		if(raw instanceof String)
			return (String) raw;
		else
			return String.valueOf(raw);
	}

	public static Integer getAsInt(String key) {
		Object raw = getRaw(key);
		if(raw instanceof Integer)
			return (Integer) raw;
		else {
			try {
				return Integer.parseInt(String.valueOf(raw));
			} catch(Exception e) {
				return null;
			}
		}
	}

	public static Double getAsDouble(String key) {
		Object raw = getRaw(key);
		if(raw instanceof Double)
			return (Double) raw;
		else {
			try {
				return Double.parseDouble(String.valueOf(raw));
			} catch(Exception e) {
				return null;
			}
		}
	}
}
