package org.bildit.sms.test.session;

import java.sql.SQLException;

import org.bildit.sms.test.interfaces.Login;
import org.bildit.sms.test.login.AbstractConnection;
import org.bildit.sms.test.login.AttemptedUser;

/**
 * @author Ognjen Mi�i�
 *
 */
public class SessionContext extends AbstractConnection implements Login {
	private static AttemptedUser sessionUser;

	public final AttemptedUser getSessionUser() {
		return sessionUser;
	}

	// prepared statement, selecting all fields from users table

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
	public void logIn(String username, String password) throws SQLException {
		String searchAllUsers = "SELECT * FROM users";
		// TODO Auto-generated method stub
		// initializing what we want to return
		AttemptedUser au = new AttemptedUser();

		try {

			// initializing connection
			 conn = connectToDb();

			// initializing statement with resultstatement being sensitive to
			// changes, and not able to edit values (read only)
			stmnt = createReadOnlyStatement(conn);

			// initializing resource set (aka pointer) and executing the
			// prepared sql statement
			rs = stmnt.executeQuery(searchAllUsers);

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
		sessionUser = au;
	}

	public void logOut() {
		sessionUser = null;
	}
	
}
