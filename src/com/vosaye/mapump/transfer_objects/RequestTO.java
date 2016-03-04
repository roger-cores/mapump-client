package com.vosaye.mapump.transfer_objects;

import java.io.Serializable;
import java.util.List;

/**
 * This POJO represents a request made by a user on a device
 * @author Roger Cores
 *
 */
public class RequestTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Location location;
	
	/**
	 * per degree value of latitude
	 */
	private Double latKm;
	
	/**
	 * per degree value of longitude
	 */
	private Double lonKm;
	
	/**
	 * The proximity radius in KM that user has set
	 */
	private Double rangeKm;
	
	/**
	 * List of companies by which result gasStations are to be filtered
	 */
	private List<String> filterByCompanies;

	public RequestTO(Location location, Double latKm, Double lonKm, Double rangeKm, List<String> filterByCompanies) {
		super();
		this.location = location;
		this.latKm = latKm;
		this.lonKm = lonKm;
		this.rangeKm = rangeKm;
		this.filterByCompanies = filterByCompanies;
	}

	public RequestTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Double getLatKm() {
		return latKm;
	}

	public void setLatKm(Double latKm) {
		this.latKm = latKm;
	}

	public Double getLonKm() {
		return lonKm;
	}

	public void setLonKm(Double lonKm) {
		this.lonKm = lonKm;
	}

	public Double getRangeKm() {
		return rangeKm;
	}

	public void setRangeKm(Double rangeKm) {
		this.rangeKm = rangeKm;
	}

	public List<String> getFilterByCompanies() {
		return filterByCompanies;
	}

	public void setFilterByCompanies(List<String> filterByCompanies) {
		this.filterByCompanies = filterByCompanies;
	}

	@Override
	public String toString() {
		return "RequestTO [location=" + location + ", latKm=" + latKm + ", lonKm=" + lonKm + ", rangeKm=" + rangeKm
				+ ", filterByCompanies=" + filterByCompanies + "]";
	}
	
	

	
	
}
