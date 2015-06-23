package org.bildit.sms.test.login;

import java.sql.SQLException;
import java.text.ParseException;

import org.bildit.sms.test.beans.VolunteerAttendance;
import org.bildit.sms.test.permissions.AbstractPermissions;
import org.bildit.sms.test.permissions.AdminPermissions;


public class TestClass {

	public static void main(String[] args) throws SQLException, ParseException   {
		AdminPermissions.removeAllUsersFromClass(-2);
		System.err.println(AbstractPermissions.getErrorMessage());
		
	}


}
