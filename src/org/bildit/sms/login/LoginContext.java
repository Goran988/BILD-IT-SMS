package org.bildit.sms.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bildit.sms.test.interfaces.Login;
import org.bildit.sms.test.login.AttemptedUser;

/**
 * @author Ognjen Mišiæ
 *
 */
public class LoginContext implements Login {
	// username and password for connecting to our database (CONN_STRING)
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";

	// address of the database
	private static final String CONN_STRING = "jdbc:mysql://localhost:3306/bildit_sms";

	// prepared statement, selecting all fields from users table
	private static final String SEARCH_ALL = "SELECT * FROM users";

	/**
	 * Method that takes
	 * 
	 * @param username
	 * 
	 * @param password
	 *            tries to log us onto the database, and @return an
	 *            AttemptedUser in any case.
	 **/
	@Override
	public AttemptedUser logIn(String username, String password)
			throws SQLException {
		// TODO Auto-generated method stub
		// initializing what we want to return
		AttemptedUser au = new AttemptedUser();

		// declaring resources, not initializing them
		Connection conn = null;
		Statement stmnt = null;
		ResultSet rs = null;
		try {

			// initializing connection
			conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);

			// initializing statement with resultset being sensitive to
			// changes, and not able to edit values (read only)
			stmnt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);

			// initializing resource set (aka pointer) and executing the
			// prepared sql statement
			rs = stmnt.executeQuery(SEARCH_ALL);

			// moving te pointer to the final position
			rs.last();

			// checking to see how many rows we have in the table
			int numOfRows = rs.getRow();

			// moving the pointer back to first position
			rs.first();

			// since the pointer is on the first entry row already, no need to
			// start i from 1
			for (int i = 2; i <= numOfRows; i++) {

				// checking to see if the username from the row that the pointer
				// is on matches our entered username
				if (rs.getString("username").equals(username)) {

					// checking the password
					if (rs.getString("password").equals(password)) {

						// and in the end checking to see if the user might be
						// deleted
						if (rs.getString("is_deleted").equals("false")) {

							// if everything is good, set values
							au.setFirstName(rs.getString("first_name"));
							au.setLastName(rs.getString("last_name"));
							au.setRoleID(rs.getInt("role_id"));
							au.setValid(true);
							au.setUsername(rs.getString("username"));
							au.setErrorMessage(null);

							// we break cause are done
							break;
						} else {

							// if deleted we break so we still return a null
							// value AttemptedUser with an error message
							au.setErrorMessage("User is deleted.");
							break;
						}
					} else {

						// if bad password we break so we still return a null
						// value AU with an error message
						au.setErrorMessage("Bad password.");
						break;
					}
				} else {

					// if the username is wrong, we just move the pointer to i
					au.setErrorMessage("Bad username");
					rs.absolute(i);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

			// finally we close resources in the reversed order we opened them
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (stmnt != null) {
				stmnt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return au;
	}

}
