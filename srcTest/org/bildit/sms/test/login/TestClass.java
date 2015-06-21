package org.bildit.sms.test.login;

import java.sql.SQLException;
import java.text.ParseException;

import org.bildit.sms.test.beans.ClassAttendance;
import org.bildit.sms.test.permissions.AbstractPermissions;
import org.bildit.sms.test.permissions.AdminPermissions;


public class TestClass {

	public static void main(String[] args) throws SQLException, ParseException   {
		
//		User returnUserFromDB = AdminPermissions.returnUserFromDB("miog");
//		System.out.println(returnUserFromDB.getFirstName());
//		System.out.println(returnUserFromDB.getLastName());
//		returnUserFromDB.setImagePath("imagepath.jpg");
//		returnUserFromDB.setRoleID(2);
//		System.out.println(returnUserFromDB.getFirstName() + " "+ returnUserFromDB.getDayOfBirth()+ " "+returnUserFromDB.getGender()+" "+ returnUserFromDB.getImagePath());
//		AdminPermissions.editUser(returnUserFromDB, "novica");
//		System.out.println(returnUserFromDB.getGender());
		ClassAttendance ca = AdminPermissions.returnClassAttendance(2);
		ca.setClassDescription("Drugi cas bajo moj");
		ca.setDate("2015-12-16");
		AdminPermissions.editClass(ca, 2);
		System.err.println(AbstractPermissions.getErrorMessage());
		System.out.println(ca.getClassID());
		System.out.println(ca.getDuration());
		System.out.println(ca.getDate());
		System.out.println(ca.getClassDescription());
	}

}
