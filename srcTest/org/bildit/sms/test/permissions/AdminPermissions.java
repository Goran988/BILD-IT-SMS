package org.bildit.sms.test.permissions;

import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bildit.sms.test.beans.ClassAttendance;
import org.bildit.sms.test.beans.User;

public class AdminPermissions extends AbstractPermissions {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

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
	 * @throws SQLException
	 */
	protected static boolean isEmailValid(String str) throws SQLException {
		// regex that checks email validity
		boolean result = false;
		if (str.matches(EMAIL_PATTERN)) {
			result = true;
			try {
				conn = connectToDb();
				stmnt = createReadOnlyStatement(conn);
				rs = stmnt.executeQuery(SELECT_ALL_FROM_USERS);
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
			} finally {
				if (rs != null) {
					rs.close();
				}
				closeStatement(stmnt);
				closeConnection(conn);
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
				rs = stmnt.executeQuery(SELECT_ALL_FROM_USERS);
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
				closeStatement(stmnt);
				closeConnection(conn);
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
		if (getErrorMessage() != null && !getErrorMessage().isEmpty()) {
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
				closeStatement(stmnt);
				closeConnection(conn);
			}
		}
	}

	/**
	 * This method removes the user with that username from the database,
	 * exactly it sets the field is_deleted to 'true', it doesn't remove the
	 * user permanently
	 * 
	 * @author Marina Sljivic
	 * @param username
	 * @throws SQLException
	 */
	public void deleteUser(String username) throws SQLException {
		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);
			rs = stmnt.executeQuery(SELECT_ALL_FROM_USERS);
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
			closeStatement(stmnt);
			closeConnection(conn);

		}
	}

	/**
	 * @author Novislav Sekulic
	 * 
	 * @return list of all users from database. return type List<User>.
	 * @throws SQLException
	 */
	public static List<User> listAllUsers() throws SQLException {

		List<User> userList = new ArrayList<>();

		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);

			rs = stmnt.executeQuery(SELECT_ALL_FROM_USERS);

			// Checking is there a more user in a database.
			while (rs.next()) {
				// if user flag "is_deleted" set on true, dont create User
				// object.
				if ((rs.getString("is_deleted").equals("false"))) {
					userList.add(new User(rs.getInt("user_id"), rs
							.getString("first_name"),
							rs.getString("last_name"), rs
									.getString("date_of_birth"), rs
									.getString("phone_number"), rs
									.getString("email"), rs
									.getString("username"), rs
									.getString("password"), rs
									.getString("image_path"), rs
									.getInt("city_id"), rs.getInt("role_id")));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			closeStatement(stmnt);
			closeConnection(conn);
		}

		return userList;
	}

	/**
	 * Method that finds a user by his username (since its unique) in the
	 * database and returns it
	 * 
	 * @author Ognjen Mišiæ
	 * @param username
	 *            which we use to find the user
	 * @return user object with all fields nonnull if there is such a user, if
	 *         not, return a null object and create an error message
	 * @throws SQLException
	 */
	public static User returnUserFromDB(String username) throws SQLException {
		User returnUser = new User();
		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);
			rs = stmnt.executeQuery(SELECT_ALL_FROM_USERS);
			while (rs.next()) {
				if (rs.getString("username").equals(username)) {
					returnUser.setFirstName(rs.getString("first_name"));
					returnUser.setLastName(rs.getString("last_name"));
					returnUser.setCityID(rs.getInt("city_id"));
					returnUser.setDayOfBirth(rs.getString("date_of_birth"));
					returnUser.setEmail(rs.getString("email"));
					returnUser.setUserID(rs.getInt("user_id"));
					returnUser.setGender(rs.getString("gender"));
					returnUser.setImagePath(rs.getString("image_path"));
					returnUser.setRoleID(rs.getInt("role_id"));
					returnUser.setPassword(rs.getString("password"));
					returnUser.setPhoneNumber(rs.getString("phone_number"));
					returnUser.setDeleted(rs.getBoolean("is_deleted"));
					returnUser.setUsername(rs.getString("username"));
					setErrorMessage(null);
					break;
				} else {
					setErrorMessage("Could not match username.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			closeStatement(stmnt);
			closeConnection(conn);
		}
		if (getErrorMessage() != null && !getErrorMessage().isEmpty()) {
			return null;
		} else {
			return returnUser;
		}

	}

	/**
	 * Method that takes a changedUser (can use @method returnUserFromDB for
	 * that), his username and edits fields we want
	 * 
	 * @author Ognjen Mišiæ
	 * @param changedUser
	 *            a passed user with committed changes and updates database for
	 *            that user
	 * @param username
	 *            is the placeholder username in case we decide to change the
	 *            username of changedUser, so we still have a way to access the
	 *            user in the database using his userName
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static void editUser(User changedUser, String username)
			throws ParseException, SQLException {
		if (changedUser != null) {
			try {
				conn = connectToDb();
				stmnt = createReadOnlyStatement(conn);
				rs = stmnt.executeQuery(SELECT_ALL_FROM_USERS);
				String sql = "UPDATE users SET first_name='"
						+ changedUser.getFirstName() + "', last_name ='"
						+ changedUser.getLastName() + "', date_of_birth='"
						+ changedUser.getDayOfBirth() + "', gender='"
						+ changedUser.getGender() + "', phone_number='"
						+ changedUser.getPhoneNumber() + "', email='"
						+ changedUser.getEmail() + "', password='"
						+ changedUser.getPassword() + "', image_path='"
						+ changedUser.getImagePath() + "', city_id='"
						+ changedUser.getCityID() + "', role_id='"
						+ changedUser.getRoleID() + "', username='"
						+ changedUser.getUsername() + "' WHERE username='"
						+ username + "';";
				while (rs.next()) {
					if (username.equals(rs.getString("username"))) {
						Statement stmnt2 = createUpdateableStatement(conn);
						if (changedUser.getFirstName().length() < 2
								|| changedUser.getFirstName().length() > 44) {
							setErrorMessage("Invalid first name. No update executed.");
							break;
						} else if (changedUser.getLastName().length() < 2
								|| changedUser.getLastName().length() > 44) {
							setErrorMessage("Invalid last name. No update executed.");
							break;
						} else if (!isValidDate(changedUser.getDayOfBirth())) {
							break;
						} else if (changedUser.getUsername().length() < 5) {
							setErrorMessage("Invalid username, must be at least 5 characters long.");
							break;
						} else if (changedUser.getPhoneNumber().length() < 6) {
							setErrorMessage("Invalid phone number.");
							break;
						} else if (!(changedUser.getGender().equals("Male") || changedUser
								.getGender().equals("Female"))) {
							setErrorMessage("Invalid gender.");
							break;
						} else if (!(changedUser.getEmail()
								.matches(EMAIL_PATTERN))) {
							break;
						} else if (!isImagePathValid(changedUser.getImagePath())) {
							break;
						} else if (changedUser.getCityID() < 1
								|| changedUser.getCityID() > 4) {
							setErrorMessage("Invalid city id.");
							break;
						} else if (!(changedUser.getRoleID() == 1 || changedUser
								.getRoleID() == 2)) {
							setErrorMessage("Wrong role type.");
							break;
						} else {
							stmnt2.executeUpdate(sql);
							setErrorMessage(null);
							break;
						}
					} else {
						setErrorMessage("No such username in the database.");
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (rs != null) {
					rs.close();
				}
				closeStatement(stmnt);
				closeConnection(conn);
			}
		}
	}

	public static ClassAttendance returnClassAttendance(int classID)
			throws SQLException {
		ClassAttendance ca = new ClassAttendance();
		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);
			rs = stmnt.executeQuery(SELECT_ALL_FROM_CLASSES_ATTENDANCE);
			while (rs.next()) {
				if (rs.getInt("class_id") == classID) {
					ca.setClassDescription(rs.getString("class_description"));
					ca.setDate(rs.getString("date"));
					ca.setDuration(rs.getInt("duration"));
					ca.setClassID(rs.getInt("class_id"));
					setErrorMessage(null);
					break;
				} else {
					setErrorMessage("Could not find selected class id");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			closeStatement(stmnt);
			closeConnection(conn);
		}
		if (getErrorMessage() != null && !getErrorMessage().isEmpty()) {
			return null;
		} else {
			return ca;
		}
	}

	public static void editClass(ClassAttendance ca, int classID)
			throws ParseException {
		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);
			rs = stmnt.executeQuery(SELECT_ALL_FROM_CLASSES_ATTENDANCE);
			String sql = "UPDATE classes_attendance SET class_description='"
					+ ca.getClassDescription() + "', date='" + ca.getDate()
					+ "', duration='" + ca.getDuration() + "' WHERE class_id='"
					+ classID + "';";
			while (rs.next()) {
				if (rs.getInt("class_id") == ca.getClassID()) {
					Statement stmnt2 = createUpdateableStatement(conn);
					if (ca.getClassDescription().length() < 2) {
						setErrorMessage("Invalid class description");
						break;
					} else if (!isValidDate(ca.getDate())) {
						setErrorMessage("Invalid date.");
						break;
					} else if (ca.getDuration() < 1) {
						setErrorMessage("Invalid class duration");
						break;
					} else {
						setErrorMessage(null);
						stmnt2.executeUpdate(sql);
						break;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
