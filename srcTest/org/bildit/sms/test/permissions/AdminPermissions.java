package org.bildit.sms.test.permissions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;

import org.bildit.sms.test.beans.ClassAttendance;
import org.bildit.sms.test.beans.User;
import org.bildit.sms.test.beans.VolunteerAttendance;

import com.sun.rmi.rmid.ExecOptionPermission;

public class AdminPermissions extends AbstractPermissions {
	// regex that checks email validity
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * @author Ognjen Mi�i� Method that validates date of birth (i.e. you can't
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
	 * @author Ognjen Mi�i� Method that checks the match of passed email with a
	 *         regex
	 * @param str
	 *            is the passed email argument
	 * @return true if regex check passes, false if not
	 * @throws SQLException
	 */
	protected static boolean isEmailValid(String str) throws SQLException {
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
	 * @author Ognjen Mi�i�
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
	 * @author Ognjen Mi�i� Method that creates a new user.
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
		try {
			if (firstName.length() > 1 && firstName.length() < 45
					&& firstName.matches("^[\\p{L}.'-]+$")) {

				newUser.setFirstName(firstName.substring(0, 1).toUpperCase()
						+ firstName.substring(1, firstName.length())
								.toLowerCase());

			} else {
				setErrorMessage("Invalid first name.");
			}
			if (lastName.length() > 1 && lastName.length() < 45
					&& lastName.matches("^[\\p{L}.'-]+$")) {

				newUser.setLastName(lastName.substring(0, 1).toUpperCase()
						+ lastName.substring(1, lastName.length())
								.toLowerCase());

			} else {
				setErrorMessage("Invalid last name.");
			}

			if (isUsernameValid(username)) {
				newUser.setUsername(username);
			}

			if (isValidDate(dayOfBirth)) {
				newUser.setDayOfBirth(dayOfBirth);
			} else {
				setErrorMessage("Invalid date.");
			}
			if (phoneNumber.length() >= 9 && phoneNumber.matches("^[0-9]*$")) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		// this checks if there is anything in errormessage so it returns a null
		// object if there is an error
		if (getErrorMessage() != null && !getErrorMessage().isEmpty()) {
			return null;
		} else {
			return newUser;
		}
	}

	/**
	 * Method that takes a passed user and adds him/her to the database.
	 * 
	 * @author Ognjen Mi�i�
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
	 * @author Ognjen Mi�i�
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
		// this checks if there is anything in errormessage so it returns a null
		// object if there is an error
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
	 * @author Ognjen Mi�i�
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

	/**
	 * Method that returns object of class attendance
	 * 
	 * @author Ognjen Mi�i�
	 * @param classID
	 *            we identify entry by its classID
	 * @return Object ClassAttendance
	 * @throws SQLException
	 */
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
		// this checks if there is anything in errormessage so it returns a null
		// object if there is an error
		if (getErrorMessage() != null && !getErrorMessage().isEmpty()) {
			return null;
		} else {
			return ca;
		}
	}

	/**
	 * Method that edits a class from a database
	 * 
	 * @author Ognjen Mi�i�
	 * @param ca
	 *            Class attendance we want to edit
	 * @param classID
	 *            we identify the class attendance by class id parameter
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static void editClass(ClassAttendance ca, int classID)
			throws ParseException, SQLException {
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
		} finally {
			if (rs != null) {
				rs.close();
			}
			closeStatement(stmnt);
			closeConnection(conn);
		}
	}

	/**
	 * Method that edits a volunteer action from a database
	 * 
	 * @author Ognjen Mi�i�
	 * @param va
	 *            volunteer attendance we want to edit
	 * @param volunteerAttendanceId
	 *            we identify the volunteer attendance by id parameter
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static void editVolunteerAttendance(VolunteerAttendance va,
			int volunteerAttendanceId) throws ParseException, SQLException {
		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);
			rs = stmnt.executeQuery(SELECT_ALL_FROM_VOLUNTEER_ATTENDANCE);
			String sql = "UPDATE volunteer_attendance SET volunteer_action_description='"
					+ va.getVolunteerActionDescription()
					+ "', date='"
					+ va.getDate()
					+ "', duration='"
					+ va.getDuration()
					+ "' WHERE volunteer_attendance_id='"
					+ volunteerAttendanceId + "';";
			while (rs.next()) {
				if (rs.getInt("volunteer_attendance_id") == va
						.getVolunteerAttendanceId()) {
					Statement stmnt2 = createUpdateableStatement(conn);
					if (va.getVolunteerActionDescription().length() < 2) {
						setErrorMessage("Invalid class description");
						break;
					} else if (!isValidDate(va.getDate())) {
						setErrorMessage("Invalid date.");
						break;
					} else if (va.getDuration() < 1) {
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
		} finally {
			if (rs != null) {
				rs.close();
			}
			closeStatement(stmnt);
			closeConnection(conn);
		}
	}

	/**
	 * Method that returns object of volunteer attendance
	 * 
	 * @author Ognjen Mi�i�
	 * @param volunteerActionId
	 *            we identify entry by its volunteer action id
	 * @return Object Volunteer Attendance with fields set from db
	 * @throws SQLException
	 */
	public static VolunteerAttendance returnVolunteerAttendance(
			int volunteerActionId) throws SQLException {
		VolunteerAttendance va = new VolunteerAttendance();
		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);
			rs = stmnt.executeQuery(SELECT_ALL_FROM_VOLUNTEER_ATTENDANCE);
			while (rs.next()) {
				if (rs.getInt("volunteer_attendance_id") == volunteerActionId) {
					va.setVolunteerActionDescription(rs
							.getString("volunteer_action_description"));
					va.setDate(rs.getString("date"));
					va.setDuration(rs.getInt("duration"));
					va.setVolunteerAttendanceId(rs
							.getInt("volunteer_attendance_id"));
					setErrorMessage(null);
					break;
				} else {
					setErrorMessage("Could not find selected volunteer attendance id.");
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
		// this checks if there is anything in errormessage so it returns a null
		// object if there is an error
		if (getErrorMessage() != null && !getErrorMessage().isEmpty()) {
			return null;
		} else {
			return va;
		}
	}

	/**
	 * Method that takes a passed class attendance object and adds it to the
	 * table in db
	 * 
	 * @author Ognjen Mi�i�
	 * @param ca
	 *            a new entry in to be added to the table
	 */
	public static void addClass(ClassAttendance ca) throws SQLException {
		if (ca != null) {
			try {
				conn = connectToDb();
				stmnt = createUpdateableStatement(conn);
				String sql = "INSERT INTO classes_attendance (class_description, date, duration) VALUES('"
						+ ca.getClassDescription()
						+ "','"
						+ ca.getDate()
						+ "','" + ca.getDuration() + "');";
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
	 * Method that creates a ClassAttendance object
	 * 
	 * @author Ognjen Mi�i�
	 * @param classDescription
	 *            argument describing the class
	 * @param date
	 *            of the class
	 * @param duration
	 *            duration of the class
	 * @return an object
	 * @throws ParseException
	 */
	public static ClassAttendance createClass(String classDescription,
			String date, double duration) throws ParseException {
		ClassAttendance ca = new ClassAttendance();
		if (classDescription.length() > 2 && classDescription.length() < 45) {
			ca.setClassDescription(classDescription);
		} else {
			setErrorMessage("Class description invalid.");
		}
		// we reverse validation cause the point is to plan classes ahead and
		// enter them to table
		if (!isValidDate(date)) {
			ca.setDate(date);
		} else {
			setErrorMessage("Can't plan classes in past.");
		}
		if (duration > 0) {
			ca.setDuration((int) duration);
		} else {
			setErrorMessage("Invalid duraton.");
		}
		// this checks if there is anything in errormessage so it returns a null
		// object if there is an error
		if (getErrorMessage() != null && !getErrorMessage().isEmpty()) {
			return null;
		} else {
			return ca;
		}
	}

	/**
	 * Method that takes a passed volunteer attendance object and adds it to the
	 * table in db
	 * 
	 * @author Ognjen Mi�i�
	 * @param va
	 *            a new entry in to be added to the table
	 */
	public static void addVolunteerAction(VolunteerAttendance va)
			throws SQLException {
		if (va != null) {
			try {
				conn = connectToDb();
				stmnt = createUpdateableStatement(conn);
				String sql = "INSERT INTO volunteer_attendance (volunteer_action_description, date, duration) VALUES('"
						+ va.getVolunteerActionDescription()
						+ "','"
						+ va.getDate() + "','" + va.getDuration() + "');";
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
	 * Method that creates a VolunteerAttendance object
	 * 
	 * @author Ognjen Mi�i�
	 * @param volunteerActionDescription
	 *            argument describing the class
	 * @param date
	 *            of the class
	 * @param duration
	 *            duration of the class
	 * @return an object of type volunteer attendance
	 * @throws ParseException
	 */
	public static VolunteerAttendance createVolunteerAction(
			String volunteerActionDescription, String date, double duration)
			throws ParseException {
		VolunteerAttendance va = new VolunteerAttendance();
		if (volunteerActionDescription.length() > 2
				&& volunteerActionDescription.length() < 45) {
			va.setVolunteerActionDescription(volunteerActionDescription);
		} else {
			setErrorMessage("Volunteer action description invalid.");
		}
		// we reverse validation cause the point is to plan volunteer actions
		// ahead and enter them to table
		if (!isValidDate(date)) {
			va.setDate(date);
		} else {
			setErrorMessage("Can't plan classes in past.");
		}
		if (duration > 0) {
			va.setDuration((int) duration);
		} else {
			setErrorMessage("Invalid duraton.");
		}
		// this checks if there is anything in errormessage so it returns a null
		// object if there is an error
		if (getErrorMessage() != null && !getErrorMessage().isEmpty()) {
			return null;
		} else {
			return va;
		}
	}

	/**
	 * Method that takes username and classID and adds the relation in the table
	 * of user-class relation
	 * 
	 * @author Ognjen Mi�i�
	 * @param username
	 *            of the user found from the users table
	 * @param classID
	 *            id of the class we want to put the user in
	 * @throws SQLException
	 * @throws InputMismatchException
	 */
	@SuppressWarnings("resource")
	public static void addUserToClass(String username, int classID)
			throws SQLException {
		Statement stmnt2 = null;
		Statement stmnt3 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		Statement stmnt4 = null;
		int userId = 0;
		boolean canWrite = false;
		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);
			rs = stmnt.executeQuery(SELECT_ALL_FROM_USERS);
			stmnt2 = createReadOnlyStatement(conn);
			rs2 = stmnt2
					.executeQuery("SELECT * FROM classes_attendance_user_relation");

			stmnt4 = createReadOnlyStatement(conn);
			rs3 = stmnt4.executeQuery(SELECT_ALL_FROM_CLASSES_ATTENDANCE);

			// get the final value of classes attendance class id
			rs3.last();
			int rows = rs3.getInt("class_id");

			// label that helps us break the outer loop
			outerloop: while (rs.next()) {
				if (rs.getString("username").equals(username)) {
					userId = rs.getInt("user_id");
					while (rs2.next()) {
						if (rows < classID) {
							setErrorMessage("No such class");
							break outerloop;
						} else if (rs2.getInt("users_user_id") == userId) {
							if (rs2.getInt("classes_attendance_class_id") == classID) {
								canWrite = false;
								setErrorMessage("User is already taking that class.");
								break outerloop;
							} else {
								canWrite = true;
							}
						} else {
							canWrite = true;
						}
					}
				} else {
					setErrorMessage("No such user.");
				}
			}
			if (canWrite) {
				String sql = "INSERT INTO classes_attendance_user_relation (users_user_id, classes_attendance_class_id) VALUES ('"
						+ userId + "','" + classID + "');";
				stmnt3 = createUpdateableStatement(conn);
				stmnt3.executeUpdate(sql);
				setErrorMessage(null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs2 != null) {
				rs2.close();
			}
			if (rs3 != null) {
				rs3.close();
			}
			closeStatement(stmnt4);
			if (rs != null) {
				rs.close();
			}
			closeStatement(stmnt3);
			closeStatement(stmnt2);
			closeStatement(stmnt);
			closeConnection(conn);
		}
	}

	/** Check this method and apply changes to remove from volunteer attendance, and write this javadoc correctlxy..
	 * @author Ognjen Lazi�
	 * @param classID
	 * @throws SQLException
	 */
	public static void removeAllUsersFromClass(int classID) throws SQLException {

		try {
			conn = connectToDb();
			stmnt = createReadOnlyStatement(conn);
			rs = stmnt
					.executeQuery("SELECT * FROM classes_attendance_user_relation");
			boolean canWrite = false;
			while (rs.next()) {
				if (rs.getInt("classes_attendance_class_id") == classID) {
					canWrite = true;
					setErrorMessage(null);
					break;
				} else {
					setErrorMessage("Could not delete, invalid class ID.");
				}
			}
			if (canWrite) {
				String statement = "DELETE FROM `bildit_sms`.`classes_attendance_user_relation` WHERE `classes_attendance_class_id`='";
				statement += classID + "';";
				stmnt.executeUpdate(statement);
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
