package org.bildit.sms.test.interfaces;

import java.sql.SQLException;

public interface Login {
	/**
	 * Method that takes username and password and returns an attempted user
	 * 
	 * @param username
	 * @param password
	 * @return AttemptedUser
	 * @throws SQLException
	 */
	void logIn(String username, String password) throws SQLException;
}
