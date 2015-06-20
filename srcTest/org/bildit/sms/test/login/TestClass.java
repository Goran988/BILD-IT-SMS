package org.bildit.sms.test.login;

import java.sql.SQLException;

import org.bildit.sms.test.beans.User;
import org.bildit.sms.test.permissions.AbstractPermissions;
import org.bildit.sms.test.permissions.AdminPermissions;


public class TestClass {

	public static void main(String[] args) throws SQLException   {
		
		User returnUserFromDB = AdminPermissions.returnUserFromDB("papasd");
		System.out.println(returnUserFromDB.getFirstName() + " "+ returnUserFromDB.getDayOfBirth()+ " "+returnUserFromDB.getGender()+" "+ returnUserFromDB.getImagePath());
		System.err.println(AbstractPermissions.getErrorMessage());
		
	}

}
