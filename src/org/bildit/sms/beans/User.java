package org.bildit.sms.beans;

/**
 * @author Novislav Sekulic
 *
 */

public class User {
	private int userID;
	private String firstName;
	private String lastName;
	private String dayOfBirth;
	private String phoneNumber;
	private String email;
	private String username;
	private String password;
	private String imagePath;
	private boolean isDeleted;
	private int cityID;
	private int roleID;

	public User() {

	}

	public User(int userID, String firstName, String lastName,
			String dayOfBirth, String phoneNumber, String email,
			String username, String password, String imagePath, int cityID,
			int roleID) {

		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dayOfBirth = dayOfBirth;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.username = username;
		this.password = password;
		this.imagePath = imagePath;
		this.cityID = cityID;
		setRoleID(roleID);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDayOfBirth() {
		return dayOfBirth;
	}

	public void setDayOfBirth(String dayOfBirth) {
		this.dayOfBirth = dayOfBirth;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String toString() {
		return "\nUserID:\t\t" + userID + "\nName:\t\t" + firstName
				+ "\nLast Name:\t" + lastName + "\nDOB:\t\t" + dayOfBirth
				+ "\nPhone:\t\t" + phoneNumber + "\nemail:\t\t" + email
				+ "\nusername:\t" + username + "\npass:\t\t" + password
				+ "\nImage Path:\t" + imagePath + "\nCityID:\t\t" + cityID
				+ "\nRoleID:\t\t" + roleID;
	}
}