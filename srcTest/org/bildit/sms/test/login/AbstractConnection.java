package org.bildit.sms.test.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Ognjen Mišiæ
 *
 */
public abstract class AbstractConnection {
	protected static Connection conn = null;
	protected static Statement stmnt = null;
	protected static ResultSet rs = null;
	
	// select all from our db
	protected static final String SELECT_ALL = "SELECT * FROM users";
	
	// username and password for connecting to our database (CONN_STRING)
	protected static final String USERNAME = "root";
	protected static final String PASSWORD = "root";

	// address of the database
	protected static final String CONN_STRING = "jdbc:mysql://localhost:3333/bildit_sms";

	protected static Connection connectToDb() throws SQLException {
		return conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
	}

	protected static void closeConnection(Connection conn) throws SQLException {
			conn.close();
	}
	
	protected static void closeStatement(Statement stmnt) throws SQLException {
		stmnt.close();
	}
	
	//currently these two do the same thing...
	protected static Statement createReadOnlyStatement(Connection conn) throws SQLException {
		return conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	protected static Statement createUpdateableStatement (Connection conn) throws SQLException  {
		return conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	}
	
	
}
