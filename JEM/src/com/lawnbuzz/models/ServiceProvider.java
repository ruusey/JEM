package com.lawnbuzz.models;

import java.io.Serializable;
import java.util.List;

public class ServiceProvider implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id = -1;
    private String email = null;
    private String userName = null;
    private String firstName = null;
    private String lastName = null;
    private List<Service> services = null;
    private GeoLocation loc = null;
    private int rating = -1;
    private String friendlyLocation=null;

    public ServiceProvider() {
	super();
    }

    public ServiceProvider(int id, String email, String userName, String firstName, String lastName,
	    List<Service> services, GeoLocation loc, int rating, String friendlyLocation) {
	super();
	this.id = id;
	this.email = email;
	this.userName = userName;
	this.firstName = firstName;
	this.lastName = lastName;
	this.services = services;
	this.loc = loc;
	this.rating = rating;
	this.friendlyLocation = friendlyLocation;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public List<Service> getServices() {
	return services;
    }

    public void setServices(List<Service> services) {
	this.services = services;
    }

    public GeoLocation getLoc() {
	return loc;
    }

    public void setLoc(GeoLocation loc) {
	this.loc = loc;
    }

    public int getRating() {
	return rating;
    }

    public void setRating(int rating) {
	this.rating = rating;
    }

    public String getFriendlyLocation() {
	return this.friendlyLocation;
    }

    public void setFriendlyLocation(String friendlyLocation) {
	this.friendlyLocation = friendlyLocation;
    }

}
