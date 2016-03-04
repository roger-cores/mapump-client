package com.vosaye.mapump.transfer_objects;

/**
 * This class defines user's current location
 * @author Roger Cores
 *
 */
public class Location {

	private Double latitude;
	
	private Double longitude;
	
	
	
	public Location() {
	}

	public Location(Double latitude, Double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "Location [latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
	
}
