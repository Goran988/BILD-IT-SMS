package org.bildit.sms.test.login;

import java.sql.SQLException;
import java.text.ParseException;

import org.bildit.sms.test.beans.User;
import org.bildit.sms.test.permissions.AbstractPermissions;
import org.bildit.sms.test.permissions.AdminPermissions;

public class TestClass {

	public static void main(String[] args) throws SQLException, ParseException {
		// TODO Auto-generated method stub
		// SessionContext sc = new SessionContext();
		// sc.logIn("miog", "ognjen");
		// System.out.println(sc.getSessionUser().getFirstName()+
		// " "+sc.getSessionUser().getLastName());
		//
		// UserPermissions.changeFirstName(sc.getSessionUser(), "ludjak");
		// UserPermissions.changeLastName(sc.getSessionUser(), "papak");
		// System.out.println(sc.getSessionUser().getFirstName()+" "+sc.getSessionUser().getLastName());
		// UserPermissions.changePhoneNumber(sc.getSessionUser(), "1234567");
		// boolean iDvalid = AdminPermissions.isIDvalid(-2);
		// System.out.println(iDvalid);
		User newUser = AdminPermissions.createUser("oæe", "testira", "bazuèoek", "1994-02-10",
				"053222645", "Female", "test@bajo.moj", "NekaŠifra1234",
				"C:/SomePath/slika.jpg", 1, 2);
		AdminPermissions.addUserToDB(newUser);
		System.out.println(AbstractPermissions.getErrorMessage());
//		String emailtest = "pujomir@pujomir.com";
//		boolean emailValid = AdminPermissions.isEmailValid(emailtest);
//		System.out.println(emailValid);
	}

}
