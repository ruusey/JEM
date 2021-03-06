package com.jem.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jem.dao.JEMDao;
import com.jem.mappers.JobMapper;
import com.jem.models.GeoLocation;
import com.jem.models.JobRequest;
import com.jem.service.JobService;

@Service("jobService")
public class JobServiceImpl implements JobService{
	
	static Logger LOGGER = Logger.getLogger(JobServiceImpl.class.getName());
	@Autowired
	private JobMapper mapper;
	
	@Override
	public List<JobRequest> getAllJobs() {
		return mapper.getAllJobs();
	}
	
	@Override
	public List<JobRequest> getAllIncompleteJobs() {
		return mapper.getAlIncompleteJobs();
	}
	
	@Override
	public void addJob(JobRequest jr) {
		mapper.addJob(jr);
		this.finalizeJobRegistration(jr.getId());
		this.registerJobGeoLocation(jr.getId(), jr.getLoc());
		LOGGER.info("Succesfully added job "+jr.getId());	
	}
	
	@Override
	public ArrayList<JobRequest> getJobsInRadius(GeoLocation center, int radius) {
		return JEMDao.geoService.getJobsInRadius(center, radius);
	}
	
	@Override
	public void completeJob(int id) {
	   mapper.completeJob(id); 
	}
	
	@Override
	public GeoLocation getGeoLocJob(int geoLocId) {
	   return mapper.getGeoLocJob(geoLocId);
	}
	
	@Override
	public void finalizeJobRegistration(int id) {
	    mapper.finalizeJobRegistration(id);
	}
	
	@Override
	public void registerJobGeoLocation(int id, GeoLocation loc) {
	    mapper.registerJobGeoLocation(id, loc);  
	}
	
	@Override
	public List<JobRequest> getJobsByService(com.jem.models.Service service) {
	    return mapper.getJobsByService(service);
	}
	
	@Override
	public String getFriendlyGeoLocJob(int jobId) {
	   return mapper.getFriendlyGeoLocJob(jobId);
	}
	
	@Override
	public void updateFriendlyGeoLocJob(String friendlyLocation, int jobId) {
	   mapper.updateJobFriendlyGeoloc(friendlyLocation, jobId); 
	}
}
