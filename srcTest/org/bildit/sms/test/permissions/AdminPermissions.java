package org.bildit.sms.test.permissions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bildit.sms.test.beans.User;

public class AdminPermissions extends AbstractPermissions {

	/**
	 * @author Ognjen Mišiæ Method that validates date of birth (i.e. you can't
	 *         have date of birth that is over today's date
	 * 
	 * @param date
	 *            argument passed
	 * @return true if date argument is before current date, false if not
	 * @throws ParseException
	 */
	protected static boolean isValidDate(String date) throws ParseException {
		Date currentDate = new Date();
		boolean result = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date parsedCurrentDate = sdf.parse(date);
		String currentDateInString = sdf.format(parsedCurrentDate);
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt(date.substring(5, 7));
		int day = Integer.parseInt(date.substring(8, 10));
		if (parsedCurrentDate.before(currentDate)) {
			{
				if (year <= Integer.parseInt(currentDateInString
						.substring(0, 4))) {
					if (month == 1 || month == 3 || month == 5 || month == 7
							|| month == 8 || month == 10 || month == 12) {
						if (day <= 31 && day > 0) {
							result = true;
						}
					} else if (month == 4 || month == 6 || month == 9
							|| month == 11) {
						if (day <= 30 && day > 0) {
							result = true;
						}
					} else if (month == 2) {
						if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
							if (day > 0 && day < 30) {
								result = true;
							}
						} else {
							if (day > 0 && day < 29) {
								result = true;
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * @author Ognjen Mišiæ Method that checks the match of passed email with a
	 *         regex
	 * @param str
	 *            is the passed email argument
	 * @return true if regex check passes, false if not
	 */
	public static boolean isEmailValid(String str) {
		// regex that checks email validity
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		boolean result = false;
		if (str.matches(EMAIL_PATTERN)) {
			result = true;
			try {
				conn = connectToDb();
				stmnt = createReadOnlyStatement(conn);
				ResultSet rs = stmnt.executeQuery(SELECT_ALL);
				while (rs.next()) {
					if (rs.getString("email").equals(str)) {
						result = false;
						setErrorMessage("Email already in use.");
						break;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return result;

	}

	/**
	 * Method that checks validity of username (must be at least 5 characters
	 * long, and must not be taken
	 * 
	 * @author Ognjen Mišiæ
	 * @param username
	 *            passed
	 * @return true if there is no such user in database, false if there is or
	 *         it is not long enough
	 * @throws SQLException
	 */
	protected static boolean isUsernameValid(String username)
			throws SQLException {
		boolean result = true;
		if (username.length() < 5) {
			setErrorMessage("Username must be at least 5 characters long.");
			result = false;
		} else {
			try {
				conn = connectToDb();
				stmnt = createReadOnlyStatement(conn);
				ResultSet rs = stmnt.executeQuery(SELECT_ALL);
				while (rs.next()) {
					if (rs.getString("username").equals(username)) {
						result = false;
						setErrorMessage("User already exists.");
						break;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
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

		}
		return result;
	}

	/**
	 * @author Ognjen Mišiæ Method that creates a new user.
	 * @param userID
	 * @param firstName
	 * @param lastName
	 * @param dayOfBirth
	 * @param phoneNumber
	 * @param email
	 * @param password
	 * @param imagePath
	 * @param cityID
	 * @param roleID
	 * @throws SQLException
	 * @throws ParseException
	 * @return returns a new user with all valid information, if there is
	 *         something wrong, returns a null value user.
	 */
	public static User createUser(String firstName, String lastName,
			String username, String dayOfBirth, String phoneNumber,
			String gender, String email, String password, String imagePath,
			int cityID, int roleID) throws SQLException, ParseException {
		User newUser = new User();

		if (firstName.length() > 1 && firstName.length() < 45) {
			newUser.setFirstName(firstName);
		} else {
			setErrorMessage("Incorrect length of first name.");
		}
		if (lastName.length() > 1 && lastName.length() < 45) {
			newUser.setLastName(lastName);
		} else {
			setErrorMessage("Incorrect length of last name.");
		}

		if (isUsernameValid(username)) {
			newUser.setUsername(username);
		}

		if (isValidDate(dayOfBirth)) {
			newUser.setDayOfBirth(dayOfBirth);
		} else {
			setErrorMessage("Invalid date.");
		}
		if (phoneNumber.length() > 5) {
			newUser.setPhoneNumber(phoneNumber);
		} else {
			setErrorMessage("Invalid phone number.");
		}

		if (gender.equals("Male") || gender.equals("Female")) {
			newUser.setGender(gender);
		} else {
			setErrorMessage("Invalid gender");
		}

		if (isEmailValid(email)) {
			newUser.setEmail(email);
		}
		if (isPasswordValid(password)) {
			newUser.setPassword(password);
		} else {
			setErrorMessage("Invalid password.");
		}
		if (isImagePathValid(imagePath)) {
			newUser.setImagePath(imagePath);
		}
		if (cityID > 0 && cityID < 5) {
			newUser.setCityID(cityID);
		} else {
			setErrorMessage("Invalid city id");
		}
		if (roleID == 1 || roleID == 2) {
			newUser.setRoleID(roleID);
		} else {
			setErrorMessage("Wrong role type.");
		}
		if (getErrorMessage() != null
				&& !getErrorMessage().isEmpty()) {
			return null;
		} else {
			return newUser;
		}
	}

	/**
	 * Method that takes a passed user and adds him/her to the database.
	 * 
	 * @author Ognjen Mišiæ
	 * @param newUser
	 *            passed user
	 * @throws SQLException
	 */
	public static void addUserToDB(User newUser) throws SQLException {
		if (newUser != null) {
			try {
				conn = connectToDb();
				stmnt = createUpdateableStatement(conn);
				String sql = "INSERT INTO users (first_name, last_name, date_of_birth, gender, phone_number, email, username, password, image_path, city_id, role_id)"
						+ "\nVALUES ('"
						+ newUser.getFirstName()
						+ "','"
						+ newUser.getLastName()
						+ "','"
						+ newUser.getDayOfBirth()
						+ "','"
						+ newUser.getGender()
						+ "','"
						+ newUser.getPhoneNumber()
						+ "','"
						+ newUser.getEmail()
						+ "','"
						+ newUser.getUsername()
						+ "','"
						+ newUser.getPassword()
						+ "','"
						+ newUser.getImagePath()
						+ "','"
						+ newUser.getCityID()
						+ "','" + newUser.getRoleID() + "');";
				stmnt.executeUpdate(sql);
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
		}
	}
	

	/**
	 * This method removes the user with that username from the database, 
	 * exactly it sets the field is_deleted to 'true',
	 * it doesn't remove the user permanently
	 *  
	 * @author Marina Sljivic
	 * @param username
	 * @throws SQLException
	 */
	public void deleteUser(String username) throws SQLException {
		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);
			rs = stmnt.executeQuery(SELECT_ALL);
			while (rs.next()) {
				if (rs.getString("username").equals(username)) {
					if (rs.getString("is_deleted").equals("false")) {
						stmnt = createUpdateableStatement(conn);
						stmnt.executeUpdate("UPDATE users SET is_deleted = 'true' WHERE username = '"
								+ username + "';");
						setErrorMessage(null);
						break;
					} else {
						setErrorMessage("That user is already deleted");
						break;
					}
				} else {
					setErrorMessage("Bad username");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	}

}
