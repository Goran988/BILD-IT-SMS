package org.bildit.sms.permissions;

import java.sql.SQLException;

import org.bildit.sms.test.login.AbstractConnection;
import org.bildit.sms.test.login.AttemptedUser;

/**
 * @author Ognjen Mišiæ
 *
 */
public abstract class AbstractPermissions extends AbstractConnection {
	private static String errorMessage;

	public static final void setErrorMessage(String errorMessage) {
		AbstractPermissions.errorMessage = errorMessage;
	}

	/** We set the error message to null after we access it
	 * @return result string which is the error message before it is nullified
	 */
	public static final String getErrorMessage() {
		String result = errorMessage;
		errorMessage = null;
		return result;
	}

	/**
	 * Method that checks if the image path is valid
	 * @param path passed image path
	 * @return true if it matches the regex, false if not
	 */
	protected static boolean isImagePathValid(String path) {
		String imageRegex = "([^\\s]+(\\.(?i)(jpg|png))$)";
		if (path.matches(imageRegex)) {
			return true;
		} else {
			setErrorMessage("Wrong image path or type.");
			return false;
		}

	}
	


	/**
	 * Method that changes own user image
	 * @author Radenko Goliæ
	 * @param sessionUser current user that wants to change 
	 * @param path to new image
	 * @throws SQLException
	 */
	public static void changeOwnImage(AttemptedUser sessionUser, String path)
			throws SQLException {
		if (isImagePathValid(path)) {
			try {
				conn = connectToDb();
				stmnt = createUpdateableStatement(conn);
				stmnt.executeUpdate("UPDATE users SET image_path = '" + path
						+ "' WHERE username = '" + sessionUser.getUsername()
						+ "';");

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatement(stmnt);
				closeConnection(conn);
			}
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
	protected static boolean isPasswordValid(String newPassword) {
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
				closeStatement(stmnt);
				closeConnection(conn);
			}

		} else {
			setErrorMessage("Password must be at least 6 characters long and must contain a number and a capital letter.");
		}
	}

	/**Method that changes phone number of user
	 * @author Ognjen Laziæ
	 * @param sessionUser user that wants to change his number
	 * @param phoneNumber new number
	 * @throws SQLException
	 */
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
				setErrorMessage("Must contain numbers only.");
			} else {
				setErrorMessage("Phone number not long enough.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatement(stmnt);
			closeConnection(conn);
		}
	}

	/**
	 * Method that changes first name
	 * @param sessionUser is the current user, so he can change his own name
	 * @param firstName name to which users name will be change
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
			setErrorMessage("First name must be at least two caharacters long");
		}
	}

	/**Method that changes the last name
	 * @param sessionUser current user who wants his name to be changted
	 * @param lastName value of the new last name that the user wants
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
			setErrorMessage("Last name must be at least two caharacters long");
		}
	}

}
