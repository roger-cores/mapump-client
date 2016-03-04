package com.vosaye.mapump.transfer_objects;

import java.io.Serializable;


/**
 * This POJO represents a GasStation entity that is from the server 
 * @author Roger Cores
 *
 */
public class GasStationTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String state;
	
	private String address;
	
	private Long phone;
	
	private Double price;
	
	private Double latitude;
	
	private Double longitude;
	
	private String company;

	public GasStationTO() {
	}

	public GasStationTO(Long id, String state, String address, Long phone, Double price) {
		super();
		this.id = id;
		this.state = state;
		this.address = address;
		this.phone = phone;
		this.price = price;
	}
	
	

	public GasStationTO(Long id, String state, String address, Long phone, Double price, Double latitude,
			Double longitude, String company) {
		super();
		this.id = id;
		this.state = state;
		this.address = address;
		this.phone = phone;
		this.price = price;
		this.latitude = latitude;
		this.longitude = longitude;
		this.company = company;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Override
	public String toString() {
		return "GasStationTO [id=" + id + ", address=" + address + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", company=" + company + "]";
	}
	
	
	
	
	
	
	
}
