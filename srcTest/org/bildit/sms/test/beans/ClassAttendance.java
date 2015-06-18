package org.bildit.sms.test.beans;

/**
 * @author Novislav Sekulic
 *
 */
public class ClassAttendance {
	private int classID;
	private String classDescription;
	private String date;
	private int duration;

	public ClassAttendance(int classID, String classDescription, String date,
			int duration) {
		this.classID = classID;
		this.classDescription = classDescription;
		this.date = date;
		this.duration = duration;
	}

	public int getClassID() {
		return classID;
	}

	public void setClassID(int classID) {
		this.classID = classID;
	}

	public String getClassDescription() {
		return classDescription;
	}

	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
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