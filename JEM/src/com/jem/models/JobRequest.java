package com.jem.models;

import java.io.Serializable;

public class JobRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private Service service;
    private String shortDescription;
    private String longDescription;
    private GeoLocation loc;
    private double pay;
    private boolean complete;
    private int jobId;
    private String friendlyLocation;

    public JobRequest() {
	super();
    }

    public JobRequest(int id, Service service, String shortDescription, String longDescription, GeoLocation loc,
	    double pay, boolean complete, int jobId, String friendlyLocation) {
	super();
	this.id = id;
	this.service = service;
	this.shortDescription = shortDescription;
	this.longDescription = longDescription;
	this.loc = loc;
	this.pay = pay;
	this.complete = complete;
	this.jobId = jobId;
	this.friendlyLocation = friendlyLocation;
    }

    public String getFriendlyLocation() {
	return this.friendlyLocation;
    }

    public void setFriendlyLocation(String friendlyLocation) {
	this.friendlyLocation = friendlyLocation;
    }

    public int getJobId() {
	return jobId;
    }

    public void setJobId(int jobId) {
	this.jobId = jobId;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public Service getService() {
	return service;
    }

    public void setService(Service service) {
	this.service = service;
    }

    public String getShortDescription() {
	return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
	this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
	return longDescription;
    }

    public void setLongDescrption(String longDescription) {
	this.longDescription = longDescription;
    }

    public GeoLocation getLoc() {
	return loc;
    }

    public void setLoc(GeoLocation loc) {
	this.loc = loc;
    }

    public double getPay() {
	return pay;
    }

    public void setPay(double pay) {
	this.pay = pay;
    }

    public boolean isComplete() {
	return complete;
    }

    public void setComplete(boolean complete) {
	this.complete = complete;
    }

    @Override
    public String toString() {
	return (this.longDescription.toLowerCase() + this.shortDescription.toLowerCase()
		+ this.service.toString().toLowerCase());
    }

}
