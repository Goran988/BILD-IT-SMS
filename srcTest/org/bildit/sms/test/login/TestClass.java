package org.bildit.sms.test.login;

import java.sql.SQLException;

import org.bildit.sms.test.permissions.UserPermissions;
import org.bildit.sms.test.session.SessionContext;

public class TestClass {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		SessionContext sc = new SessionContext();
		sc.logIn("miog", "ognjen");
		System.out.println(sc.getSessionUser().getFirstName()+ " "+sc.getSessionUser().getLastName());
		
		UserPermissions.changeFirstName(sc.getSessionUser(), "ludjak");
		UserPermissions.changeLastName(sc.getSessionUser(), "papak");
		System.out.println(sc.getSessionUser().getFirstName()+" "+sc.getSessionUser().getLastName());
		UserPermissions.changePhoneNumber(sc.getSessionUser(), "1234567");
	}

}
