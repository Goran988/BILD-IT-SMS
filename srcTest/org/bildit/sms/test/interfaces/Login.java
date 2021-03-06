package org.bildit.sms.test.interfaces;

import java.sql.SQLException;

public interface Login {
	/**
	 * Method that takes username and password and returns an attempted user
	 * 
	 * @param username
	 *            Expected User name
	 * @param password
	 *            Expected User password
	 * @return AttemptedUser Returns User object from db if login is successful.
	 * @throws SQLException
	 *             Can throw exception if connection to db fails.
	 */
	void logIn(String username, String password) throws SQLException;
}
