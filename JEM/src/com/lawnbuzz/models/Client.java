package com.lawnbuzz.models;

import java.util.List;

public class Client {
	private int id;
	private String email;
	private String userName;
	private String firstName;
	private String lastName;
	private List<JobRequest> jobs;
	private GeoLocation loc;
	private int rating;
	
	public Client() {
		super();
	}
	public Client(int id, String email, String userName, String firstName,
			String lastName, List<JobRequest> jobs, GeoLocation loc, int rating) {
		super();
		this.id = id;
		this.email = email;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.jobs = jobs;
		this.loc = loc;
		this.rating = rating;
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
	public List<JobRequest> getJobs() {
		return jobs;
	}
	public void setJobs(List<JobRequest> jobs) {
		this.jobs = jobs;
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
	
	
	
}
