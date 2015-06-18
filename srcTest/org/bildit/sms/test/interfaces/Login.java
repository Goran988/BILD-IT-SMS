package org.bildit.sms.test.interfaces;

import java.sql.SQLException;

import org.bildit.sms.test.login.AttemptedUser;

public interface Login {
	/**
	 * Method that takes username and password and returns an attempted user
	 * 
	 * @param username
	 * @param password
	 * @return AttemptedUser
	 * @throws SQLException
	 */
	AttemptedUser logIn(String username, String password) throws SQLException;
}
