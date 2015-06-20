package org.bildit.sms.test.permissions;

import java.sql.SQLException;

import org.bildit.sms.test.login.AbstractConnection;
import org.bildit.sms.test.login.AttemptedUser;

/**
 * @author BILD-IT
 *
 */
public abstract class AbstractPermissions extends AbstractConnection {
	private static String errorMessage;

	public final String getErrorMessage() {
		return errorMessage;
	}

	public static void changeOwnImage(AttemptedUser sessionUser, String path)
			throws SQLException {
		final String imageRegex = "([^\\s]+(\\.(?i)(jpg|png))$)";
		if (path.matches(imageRegex)) {
			try {
				conn = connectToDb();
				stmnt = createUpdateableStatement(conn);
				stmnt.executeUpdate("UPDATE users SET image_path = '" + path
						+ "' WHERE username = '" + sessionUser.getUsername()
						+ "';");

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeConnection(conn);
				closeStatement(stmnt);
			}
		} else {
			errorMessage = "Wrong image path or type.";
		}
	}

	/**
	 * This method checks if the password is valid. It's valid when it is at
	 * least 6 characters long, contains uppercase and lowercase characters and
	 * one number
	 * 
	 * @param newPassword
	 *            is the provided password
	 * @return true if it matches both regexes, false if not
	 */
	public static boolean isPasswordValid(String newPassword) {
		String regexOne = ".*[0-9].*";
		String regexTwo = ".*[A-Z].*";
		return newPassword.matches(regexOne) && newPassword.matches(regexTwo)
				&& newPassword.length() > 5;
	}

	/**
	 * @author Marina Sljivic
	 * 
	 *         This method changes the current sessionUser's password if the
	 *         password is valid (see the method isPasswordValid)
	 * 
	 * @param password
	 *            is the provided password
	 */
	public static void changeOwnPassword(AttemptedUser sessionUser,
			String password) throws SQLException {

		if (isPasswordValid(password)) {

			try {
				conn = connectToDb();
				stmnt = createUpdateableStatement(conn);
				stmnt.executeUpdate("UPDATE users SET password = '" + password
						+ "' WHERE username = '" + sessionUser.getUsername()
						+ "';");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (stmnt != null) {
					stmnt.close();
				}
				if (conn != null) {
					conn.close();
				}
			}

		} else {
			errorMessage = "Password must be at least 6 characters long and must contain a number and a capital letter.";
		}
	}

	public static void changePhoneNumber(AttemptedUser sessionUser,
			String phoneNumber) throws SQLException {
		try {
			conn = connectToDb();
			stmnt = createUpdateableStatement(conn);
			if (phoneNumber.length() >= 6 && phoneNumber.matches("^[0-9]*$")) {
				stmnt.executeUpdate("UPDATE users \nSET phone_number='"
						+ phoneNumber + "'WHERE username =" + "'"
						+ sessionUser.getUsername() + "'" + ";");
			} else if (!phoneNumber.matches("^[0-9]*$")) {
				errorMessage = "Must contain numbers only.";
			} else {
				errorMessage = "Phone number not long enough.";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeConnection(conn);
			closeStatement(stmnt);
		}
	}

	/**
	 * @param firstName
	 * @throws SQLException
	 */
	public static void changeFirstName(AttemptedUser sessionUser,
			String firstName) throws SQLException {

		if (firstName.length() > 2) {
			try {

				conn = connectToDb();
				stmnt = createUpdateableStatement(conn);

				String newFirstName = firstName.substring(0, 1).toUpperCase()
						+ firstName.substring(1, firstName.length());

				stmnt.executeUpdate("UPDATE users SET first_name = '"
						+ newFirstName + "' WHERE username = '"
						+ sessionUser.getUsername() + "';");

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatement(stmnt);
				closeConnection(conn);
			}

		} else {
			errorMessage = "First name must at least two caharacters long";
		}
	}

	/**
	 * @param lastName
	 * @throws SQLException
	 */
	public static void changeLastName(AttemptedUser sessionUser, String lastName)
			throws SQLException {

		if (lastName.length() > 2) {

			try {

				conn = connectToDb();
				stmnt = createUpdateableStatement(conn);

				String newFirstName = lastName.substring(0, 1).toUpperCase()
						+ lastName.substring(1, lastName.length());

				stmnt.executeUpdate("UPDATE users SET last_name = '"
						+ newFirstName + "' WHERE username = '"
						+ sessionUser.getUsername() + "';");

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatement(stmnt);
				closeConnection(conn);
			}

		} else {
			errorMessage = "Last name must at least two caharacters long";
		}
	}

}
