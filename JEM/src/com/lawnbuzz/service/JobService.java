package com.lawnbuzz.service;

import java.util.ArrayList;
import java.util.List;

import com.lawnbuzz.models.GeoLocation;
import com.lawnbuzz.models.JobRequest;
import com.lawnbuzz.models.Service;

public interface JobService {
	public List<JobRequest> getAllJobs();
	public List<JobRequest> getAllIncompleteJobs();
	public List<JobRequest> getJobsByService(Service s);
 	public void addJob(JobRequest jr);
	public ArrayList<JobRequest> getJobsInRadius(GeoLocation center, int radius);
}
