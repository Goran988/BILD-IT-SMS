package org.bildit.sms.test.beans;

/**
 * @author Novislav Sekulic
 *
 */
public class City {
	private int cityID;
	private String cityName;
	private int postCode;

	public City(int cityID, String cityName, int postCode) {
		this.cityID = cityID;
		this.cityName = cityName;
		this.postCode = postCode;
	}

	public int getCityID() {
		return cityID;
	}

	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public int getPostCode() {
		return postCode;
	}

	public void setPostCode(int postCode) {
		this.postCode = postCode;
	}

	@Override
	public String toString() {
		return "City ID: " + cityID + "; City Name: " + cityName
				+ "; Postal Code: " + postCode + ";";
	}
}