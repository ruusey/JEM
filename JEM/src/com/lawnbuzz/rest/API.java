package com.lawnbuzz.rest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawnbuzz.dao.LawnBuzzDao;
import com.lawnbuzz.models.APIStatus;
import com.lawnbuzz.models.Client;
import com.lawnbuzz.models.GeoLocation;
import com.lawnbuzz.models.JobRequest;
import com.lawnbuzz.models.Pong;
import com.lawnbuzz.models.ServiceProvider;
import com.lawnbuzz.util.APIUtils;
import com.lawnbuzz.util.ApiScanner;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Api(value = "/v1")
@Path("/")
public class API {

    @GET
    @Path("/ping")
    @Produces("application/json")
    
    @ApiOperation("Handle ping request from client")
    @ApiResponses({@ApiResponse(code = 200, message = "OK: pong", response = Pong.class)})
    
    public Response ping(@Context HttpServletRequest request) {
	String timeStamp =
	        new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(Calendar.getInstance().getTime());
	    Pong p = new Pong(request.getRemoteAddr(), timeStamp, "pong");
	    return Response.ok(p).build();
    }
    @GET
    @Path("/methods")
    @Produces("application/json")
    
    @ApiOperation("Retrieve available endpoints from JEM API")
    @ApiResponses({@ApiResponse(code = 200, message = "API methods retrieved succesfully", response = List.class)})
    
    public Response getApiMethods(@Context HttpServletRequest request) {
	List<String> methods = ApiScanner.apiListing();
	    return APIUtils.buildSuccess("API methods retrieved succesfully", methods);
    }
    ////////////////
    //CLIENT METHODS
    ////////////////
    @ApiOperation(
	      value = "Returns all registered Clients",
	      notes = "Returns the Member associated with the ID passed.",
	      response = List.class)
	  @ApiResponses({
	    @ApiResponse(code = 200, message = "Clients retrieved succesfully.", response = Client.class),
	    @ApiResponse(
	        code = 400,
	        message = "No clients registered.",
	        response = WebApplicationException.class)
	  })
    @GET
    @Path("/client-all")
    @Produces("application/json")
    public Response getClient(@Context HttpServletRequest request) throws WebApplicationException{
	List<Client> clients = LawnBuzzDao.clientService.getClients();
	 return APIUtils.buildSuccess("Succesfully retrieved all Clients", clients);
    }
    @ApiOperation(
	      value = "Returns the Client by ID",
	      notes = "Returns the Client associated with the ID passed.",
	      response = List.class)
	  @ApiResponses({
	    @ApiResponse(code = 200, message = "Client retrieved succesfully.", response = Client.class),
	    @ApiResponse(
	        code = 400,
	        message = "Client not found.",
	        response = WebApplicationException.class)
	  })
    @GET
    @Path("/client/{client_id}")
    @Produces("application/json")
    public Response getClientById(@Context HttpServletRequest request,
	    			@PathParam("client_id") int clientId) 
	    			throws WebApplicationException {
	Client c = LawnBuzzDao.clientService.getClientById(clientId);
	if(c!=null) {
	    return APIUtils.buildSuccess("Succesfully retrieved Client", c);
	}else {
	    throw APIUtils.buildWebApplicationException(
		          Status.BAD_REQUEST,
		          APIStatus.ERROR,
		          "Client not found",
		          "The Client ID supplied does not match any existing Clients.");  
	}
	
    }
    @ApiOperation(
	      value = "Returns the Client's available Jobs by ID",
	      notes = "Returns the Client available jobs",
	      response = List.class)
	  @ApiResponses({
	    @ApiResponse(code = 200, message = "Client Jobs retrieved succesfully.", response = List.class),
	    @ApiResponse(
	        code = 400,
	        message = "No Jobs found for Client",
	        response = WebApplicationException.class)
	  })
    @GET
    @Path("/client-jobs/{client_id}")
    @Produces("application/json")
    public Response getClientJobsById(@Context HttpServletRequest request,
	    @PathParam("client_id") int clientId) throws WebApplicationException {
	List<JobRequest> jobs = LawnBuzzDao.clientService.getClientJobsById(clientId);
	if(jobs!=null) {
	    return APIUtils.buildSuccess("Succesfully retrieved Client", jobs);
	}else {
	    throw APIUtils.buildWebApplicationException(
		          Status.BAD_REQUEST,
		          APIStatus.ERROR,
		          "No jobs available",
		          "The Client ID supplied does not match any existing Clients.");  
	}
    }
    @POST
    @Path("/client-register")
    @Produces("application/json")
    @Consumes("application/json")
    public Response registerClient(@Context HttpServletRequest request, Client c) {
	LawnBuzzDao.clientService.registerClient(c);
	return Response.ok(c).build();
    }
    
    ////////////////
    //JOBS METHODS
    ////////////////
    @GET
    @Path("/job-all")
    @Produces("application/json")
    public Response getJobs(@Context HttpServletRequest request) {
	
	List<JobRequest> jobs = LawnBuzzDao.jobService.getAllJobs();
	return Response.ok(jobs).build();

    }
    @GET
    @Path("/job-incomplete")
    @Produces("application/json")
    public Response getIncompleteJobs(@Context HttpServletRequest request) {
	
	List<JobRequest> jobs = LawnBuzzDao.jobService.getAllIncompleteJobs();
	return Response.ok(jobs).build();
    }
    @GET
    @Path("/job-service/{service}")
    @Produces("application/json")
    public Response getJobsByService(@Context HttpServletRequest request, @PathParam("service") com.lawnbuzz.models.Service s) {
	
	List<JobRequest> jobs = LawnBuzzDao.jobService.getJobsByService(s);
	return Response.ok(jobs).build();

    }
    @GET
    @Path("/job-search/{radius}")
    @Produces("application/json")
    public Response getJobsInRadius(@Context HttpServletRequest request,
	    @DefaultValue("10") @PathParam("radius") int radius) {
	ServiceProvider logged = LawnBuzzDao.serviceProviderService.getServiceProviderById(1);
	List<JobRequest> jobs = LawnBuzzDao.jobService.getJobsInRadius(logged.getLoc(), radius);
	return Response.ok(jobs).build();

    }
    @POST
    @Path("/job-add")
    @Produces("application/json")
    @Consumes("application/json")
    public Response addJob(@Context HttpServletRequest request, JobRequest job) {
	LawnBuzzDao.jobService.addJob(job);
	return Response.ok(job).build();
    }
    //////////////////////////
    //SERVICE PROVIDER METHODS
    //////////////////////////
    
    @ApiOperation(
	      value = "Returns all registered ServiceProviders",
	      notes = "SP",
	      response = List.class)
	  @ApiResponses({
	    @ApiResponse(code = 200, message = "ServiceProviders retrieved succesfully.", response = List.class),
	    @ApiResponse(
	        code = 400,
	        message = "No ServiceProviders registered.",
	        response = WebApplicationException.class)
	  })
    
    @GET
    @Path("/sp-all")
    @Produces("application/json")
    public Response getServiceProviders(@Context HttpServletRequest request) {
	
	List<ServiceProvider> sPs = LawnBuzzDao.serviceProviderService.getServiceProviders();
	return Response.ok(sPs).build();

    }
    
    @ApiOperation(
	      value = "Returns A ServiceProvider by ID",
	      notes = "Returns the ServiceProvier associated with the given ID",
	      response = List.class)
	  @ApiResponses({
	    @ApiResponse(code = 200, message = "ServiceProvider retrieved succesfully.", response = ServiceProvider.class),
	    @ApiResponse(
	        code = 400,
	        message = "No ServiceProvider found with the given ID.",
	        response = WebApplicationException.class)
	  })
    @GET
    @Path("/sp/{sp_id}")
    @Produces("application/json")
    public Response getServiceProviderById(@Context HttpServletRequest request, @PathParam("sp_id") int spId) {
	
	ServiceProvider sp = LawnBuzzDao.serviceProviderService.getServiceProviderById(spId);
	if(sp!=null) {
	    return APIUtils.buildSuccess("Succesfully retrieved ServiceProvider", sp);
	}else {
	    throw APIUtils.buildWebApplicationException(
		          Status.BAD_REQUEST,
		          APIStatus.ERROR,
		          "ServiceProvider not found",
		          "The ServiceProvider ID supplied does not match any existing Clients.");  
	}
	

    }
    @POST
    @Path("/sp-register")
    @Produces("application/json")
    @Consumes("application/json")
    public Response registerServiceProvider(@Context HttpServletRequest request, ServiceProvider sp) {
	LawnBuzzDao.serviceProviderService.registerServiceProvider(sp);
	return Response.ok(sp).build();
    }
    @POST
    @Path("/sp-register-service/{sp_id}/{service}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response registerServiceProviderService(@Context HttpServletRequest request,  @PathParam("sp_id") int spId, @PathParam("service") com.lawnbuzz.models.Service service) {
	LawnBuzzDao.serviceProviderService.registerServiceProviderService(spId, service);
	return Response.ok(service).build();
    }
    @POST
    @Path("/sp-register-geoloc/{sp_id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response registerServiceProviderGeoLoc(@Context HttpServletRequest request,  @PathParam("sp_id") int spId,  GeoLocation loc) {
	LawnBuzzDao.serviceProviderService.registerServiceProviderGeoLoc(spId, loc);
	return Response.ok(loc).build();
    }
    @POST
    @Path("/sp-update/{sp_id}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response updateServiceProvider(@Context HttpServletRequest request,  @PathParam("sp_id") int spId,  ServiceProvider sp) {
	LawnBuzzDao.serviceProviderService.updateServiceProvider(sp);
	return Response.ok(sp).build();
    }

}
