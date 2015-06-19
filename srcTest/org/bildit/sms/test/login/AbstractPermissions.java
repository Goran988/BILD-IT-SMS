package org.bildit.sms.test.login;

import java.sql.ResultSet;

import org.bildit.sms.test.beans.User;

public abstract class AbstractPermissions {

	public void changeImgPath(User user, String path) {
		// jpg, png,
		// do validation with regex
		user.setImagePath(path);

	}

	public void changeOwnPassword(User user, ResultSet rs, String password) {

	}

	public void changePhoneNumber(User user, ResultSet rs, String phoneNumber) {

	}

	public void changeFirstName(User user, ResultSet rs, String firstName) {

	}

	public void changeLastName(User user, ResultSet rs, String lastName) {

	}

}
