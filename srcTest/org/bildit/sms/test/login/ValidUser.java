package org.bildit.sms.test.login;

/**
 * @author Marina Sljivic
 *
 */

public class ValidUser {

	private String firstName;
	private String lastName;
	private String username;
	private int roleID;
	private boolean isValid;
	private String errorMessage;

	public ValidUser() {
	}

	public ValidUser(String firstName, String lastName, String username,
			int roleID, boolean isValid, String errorMessage) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.roleID = roleID;
		this.isValid = isValid;
		this.errorMessage = errorMessage;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}