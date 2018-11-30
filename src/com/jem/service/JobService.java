package com.jem.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jem.models.GeoLocation;
import com.jem.models.JobRequest;
import com.jem.models.Service;

public interface JobService {
	public List<JobRequest> getAllJobs();
	public List<JobRequest> getAllIncompleteJobs();
	public List<JobRequest> getJobsByService(Service service);
 	public void addJob(JobRequest jr);
	public ArrayList<JobRequest> getJobsInRadius(GeoLocation center, int radius);
	
	public void completeJob(int id);
	public GeoLocation getGeoLocJob(int geoLocId);
	public String getFriendlyGeoLocJob(int jobId);
	public void updateFriendlyGeoLocJob(String friendlyLocation,int jobId);
	public void finalizeJobRegistration(int id);
	public void registerJobGeoLocation(@Param("id") int id, @Param("loc") GeoLocation loc);
}
