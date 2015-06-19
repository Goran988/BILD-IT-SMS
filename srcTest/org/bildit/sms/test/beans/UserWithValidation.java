package org.bildit.sms.test.beans;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ognjen Mišiæ
 *
 */

public class UserWithValidation {

	private int userId;
	private String firstName;
	private String lastName;
	private String password;
	private String email;
	private String dateOfBirth; // format yyyy/MM/dd
	private int cityId;
	private String phoneNumber;
	private String userName;
	private int roles; // student, moderator, admin, superadmin
	private boolean isDeleted;
	private String imagePath;

	public final int getUserId() {
		return userId;
	}

	public final void setUserId(int userId) {
		if (userId > 0) {
			this.userId = userId;
		} else {
			System.err.println("Must be a number bigger than 0.");
		}
	}

	public final String getFirstName() {
		return firstName;
	}

	public final void setFirstName(String firstName) {
		if (firstName.length() > 1) {
			this.firstName = firstName;
		} else {
			System.err.println("Name must be at least 2 characters long.");
		}
	}

	public final String getLastName() {
		return lastName;
	}

	public final void setLastName(String lastName) {
		if (lastName.length() > 1) {
			this.lastName = lastName;
		} else {
			System.err.println("Last name must be at least 2 characters long.");
		}
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(String password) {
		if (isPasswordValid(password)) {
			this.password = password;
		} else {
			System.err
					.println("Password must be at least 6 characters long and must contain a number and a capital letter.");
		}
	}

	public final String getEmail() {
		return email;
	}

	public final void setEmail(String email) {
		if (isEmailValid(email)) {
			this.email = email;
		} else {
			System.err.println("Invalid email.");
		}
	}

	public final String getDateOfBirth() {
		return dateOfBirth;
	}

	public final void setDateOfBirth(String dateOfBirth) throws ParseException {
		if (isValidDate(dateOfBirth)) {
			this.dateOfBirth = dateOfBirth;
		} else {
			System.err.println("Wrong date input.");
		}
	}

	public final int getCityId() {
		return cityId;
	}

	public final void setCityId(int cityId) {
		if (cityId > 0) {
			this.cityId = cityId;
		} else {
			System.err.println("cityID value must be higher than 0");
		}
	}

	public final String getPhoneNumber() {
		return phoneNumber;
	}

	// check if all are numbers
	public final void setPhoneNumber(String phoneNumber) {
		if (!phoneNumber.isEmpty()) {
			this.phoneNumber = phoneNumber;
		} else {
			System.err.println("You have to enter a valid phone number.");
		}
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(String userName) {
		if (userName.length() >= 3) {
			this.userName = userName;
		} else {
			System.err.println("User name must be at least 3 characters long.");
		}
	}

	public final int getRoles() {
		return roles;
	}

	public final void setRoles(int roles) {
		if (roles > 0 && roles < 4) {
			this.roles = roles;
		} else {
			System.err.println("invalid role");
		}
	}

	public final boolean isDeleted() {
		return isDeleted;
	}

	public final void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public final String getImagePath() {
		return imagePath;
	}

	public final void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * Method that checks if the password is valid Is valid when it is at least
	 * 6 characters long, contains uppercase and lowercase characters and one
	 * number
	 * 
	 * @param str
	 *            is the provided password
	 * @return true if it matches both regexes, false if not
	 */
	private boolean isPasswordValid(String str) {
		String regexOne = ".*[0-9].*";
		String regexTwo = ".*[A-Z].*";
		return str.matches(regexOne) && str.matches(regexTwo)
				&& str.length() > 5;
	}

	/**
	 * Method that checks if the email is valid.
	 * 
	 * @param str
	 *            is the provided email
	 * @return true if email is of correct composition, false if not
	 */
	private boolean isEmailValid(String str) {
		// regex that checks email validity
		final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		return str.matches(EMAIL_PATTERN);

	}

	/**
	 * Method that validates date of birth (i.e. you can't have date of birth
	 * that is over today's date
	 * 
	 * @param date
	 *            argument passed
	 * @return true if date argument is before current date, false if not
	 * @throws ParseException
	 */
	private boolean isValidDate(String date) throws ParseException {
		Date currentDate = new Date();
		boolean result = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
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
						}
					}
				}
			}
		}
		return result;
	}
}
