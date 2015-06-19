package org.bildit.sms.test.session;

import java.sql.SQLException;

import org.bildit.sms.test.login.AttemptedUser;

public class Session {
	private static AttemptedUser sessionUser;

	public final static AttemptedUser getSessionUser() {
		return sessionUser;
	}

	public Session() {
		// TODO Auto-generated constructor stub
	}

	public Session(String username, String password) throws SQLException {
		// TODO Auto-generated constructor stub
		SessionContext sc = new SessionContext();
		sc.logIn(username, password);
	}

}
