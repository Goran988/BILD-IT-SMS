package org.bildit.sms.test.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class AbstractConnection {
	protected Connection conn = null;
	protected Statement stmnt = null;
	protected ResultSet rs = null;

	// username and password for connecting to our database (CONN_STRING)
	protected static final String USERNAME = "root";
	protected static final String PASSWORD = "root";

	// address of the database
	protected static final String CONN_STRING = "jdbc:mysql://localhost:3333/bildit_sms";

	protected Connection connectToDb() throws SQLException {
		return conn = DriverManager.getConnection(CONN_STRING, USERNAME,
				CONN_STRING);
	}

	protected void closeConnection(Connection conn) throws SQLException {
		if (conn != null) {
			conn.close();
		}
	}

	protected Statement createStatement(Connection conn, ResultSet rs) {
		return null;
	}
}
