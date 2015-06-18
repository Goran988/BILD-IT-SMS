package org.bildit.sms.test.beans;

/**
 * @author Marina Sljivic
 *
 */

public class VolunteerAttendance {
	private int volunteerAttendanceId;
	private String volunteerActionDescription;
	private String date;
	private int duration;

	public VolunteerAttendance() {
	}

	public VolunteerAttendance(int volunteerAttendanceId,
			String volunteerActionDescription, String date, int duration) {
		this.volunteerAttendanceId = volunteerAttendanceId;
		this.volunteerActionDescription = volunteerActionDescription;
		this.date = date;
		this.duration = duration;
	}

	public int getVolunteerAttendanceId() {
		return volunteerAttendanceId;
	}

	public void setVolunteerAttendanceId(int volunteerAttendanceId) {
		this.volunteerAttendanceId = volunteerAttendanceId;
	}

	public String getVolunteerActionDescription() {
		return volunteerActionDescription;
	}

	public void setVolunteerActionDescription(String volunteerActionDescription) {
		this.volunteerActionDescription = volunteerActionDescription;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}