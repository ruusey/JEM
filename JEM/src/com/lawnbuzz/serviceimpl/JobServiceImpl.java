package com.lawnbuzz.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawnbuzz.dao.LawnBuzzDao;
import com.lawnbuzz.mappers.JobMapper;
import com.lawnbuzz.models.GeoLocation;
import com.lawnbuzz.models.JobRequest;
import com.lawnbuzz.rest.Test;
import com.lawnbuzz.service.JobService;

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
		mapper.finalizeJobRegistration(jr.getId());
		mapper.registerJobGeoLocation(jr.getId(), jr.getLoc());
		LOGGER.info("Succesfully added job "+jr.getId());
		
	}
	@Override
	public ArrayList<JobRequest> getJobsInRadius(GeoLocation center, int radius) {
		return LawnBuzzDao.geoService.getJobsInRadius(center, radius);
	}
	@Override
	public List<JobRequest> getJobsByService(com.lawnbuzz.models.Service s) {
		return mapper.getJobsByService(s);
	}

}
