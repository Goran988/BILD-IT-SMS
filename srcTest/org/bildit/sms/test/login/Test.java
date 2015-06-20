package org.bildit.sms.test.login;

import java.sql.SQLException;

import org.bildit.sms.test.session.SessionContext;

public class Test {

	public static void main(String[] args) {

		SessionContext lc = new SessionContext();
		AttemptedUser attemptedUser = new AttemptedUser();
		try {
			attemptedUser = lc.logIn("laog", "ognjen");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(attemptedUser.getFirstName());
		System.out.println(attemptedUser.getLastName());
		System.out.println(attemptedUser.getRoleID());
		System.out.println(attemptedUser.getErrorMessage());

	}
}
