package com.lawnbuzz.rest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawnbuzz.dao.LawnBuzzDao;
import com.lawnbuzz.models.JobRequest;
import com.lawnbuzz.models.ServiceProvider;

@Service("api")
@Path("/")
public class API {
	
	@GET
	@Path("/ping")
	@Produces("application/json")
	public Response ping(@Context HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(Calendar.getInstance().getTime());
		return new Response(true,ip+" received a response from the API at "+timeStamp);
	}
	@GET
	@Path("/getjobs")
	@Produces("application/json")
	public List<JobRequest> getJobs(@Context HttpServletRequest request, @DefaultValue("10") @QueryParam("radius") int radius) {
		ServiceProvider logged = LawnBuzzDao.serviceProviderService.getServiceProviderById(1);
		return LawnBuzzDao.jobService.getJobsInRadius(logged.getLoc(), radius);
				
		
	}
	
}
