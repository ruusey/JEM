package com.jem.models;

import com.google.maps.model.GeocodingResult;
import com.jem.dao.JEMDao;

public class GeoLocation {
	private double lat;
	private double lng;
	private String dateTime;
	
	public GeoLocation() {
		super();
	}
	public GeoLocation(double lat, double lng, String dateTime) {
		super();
		this.lat = lat;
		this.lng = lng;
		this.dateTime = dateTime;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public String reverseGeocode() {
	    GeocodingResult res = JEMDao.geoService.reverseGeocode(this);
	    return res.formattedAddress;
	}
	
	
	
}
