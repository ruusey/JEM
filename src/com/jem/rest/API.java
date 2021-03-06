package com.jem.rest;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jem.dao.JEMDao;
import com.jem.models.APIStatus;
import com.jem.models.Client;
import com.jem.models.Credentials;
import com.jem.models.GeoLocation;
import com.jem.models.JobRequest;
import com.jem.models.Pong;
import com.jem.models.ServiceProvider;
import com.jem.util.APIUtils;
import com.jem.util.ApiScanner;
import com.jem.util.SHAHash;
import com.jem.util.SHAValidate;
import com.jem.util.UAgentInfo;
import com.jem.util.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "/v1")
@Path("/")
public class API {
	static Logger LOGGER = Logger.getLogger(API.class.getName());

	@POST
	@Path("/auth")
//	@ApiOperation("Authorize user endpoint")
//	@ApiResponses({ @ApiResponse(code = 200, message = "User succesfully authenticated", response = String.class),
//			@ApiResponse(code = 404, message = "User succesfully authenticated", response = WebApplicationException.class) })

	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(Credentials credentials) throws WebApplicationException {

		String username = credentials.getUsername();
		String password = credentials.getPassword();
		ServiceProvider sp = JEMDao.serviceProviderService.getServiceProviderByUsername(username);
		if (sp == null) {
			try {
				int id = Integer.parseInt(username);
				sp = JEMDao.serviceProviderService.getServiceProviderById(id);
			} catch (Exception e) {
				sp = JEMDao.serviceProviderService.getServiceProviderByEmail(username);
				if (sp == null) {
					throw APIUtils.buildWebApplicationException(Status.BAD_REQUEST, APIStatus.ERROR,
							"ServiceProvider not found.", "Login Invalid. Please try again.");
				}
			}
		}

		if (password.length() == 0) {
			password = JEMDao.userService.getUserPass(sp.getId());
		}
		String token = null;
		try {
			if (JEMDao.userService.isRegistered(sp.getId())) {
				token = JEMDao.userService.getUserToken(sp.getId());
				if (SHAValidate.validatePassword(password, token)) {
					return APIUtils.buildSuccess("Token succesfully created", token);
				}
			} else {
				token = SHAHash.generateStrongPasswordHash(password);
				JEMDao.userService.createUserAuth(sp.getId(), username, password);
				JEMDao.userService.registerUserAuth(sp.getId(), token, Util.getCurrentDateTime2());
				return APIUtils.buildSuccess("Token succesfully created", token);
			}

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw APIUtils.buildWebApplicationException(Status.BAD_REQUEST, APIStatus.ERROR, "ServiceProvider not found.",
				"Login Invalid. Please try again.");
		// Authenticate the user, issue a token and return a response
	}

	@GET
	@Path("/ping")
	@Produces("application/json")

	@ApiOperation("Handle ping request from client")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK: pong", response = Pong.class) })

	public Response ping(@HeaderParam("user-agent") String userAgent, @HeaderParam("accept") String accept,
			@Context HttpServletRequest request) {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss").format(Calendar.getInstance().getTime());
		UAgentInfo mobile = new UAgentInfo(userAgent, accept);
		if (mobile.detectMobileLong()) {
			Pong p = new Pong(request.getRemoteAddr(), timeStamp, "pong");
			return APIUtils.buildSuccess("true", p);
		} else {
			Pong p = new Pong(request.getRemoteAddr(), timeStamp, "pong");
			return APIUtils.buildSuccess("false", p);
		}
	}

	@GET
	@Secured
	@Path("/methods")
	@Produces("application/json")

	@ApiOperation("Retrieve available endpoints from JEM API")
	@ApiResponses({ @ApiResponse(code = 200, message = "API methods retrieved succesfully", response = List.class) })

	public Response getApiMethods(@Context HttpServletRequest request) {
		List<String> methods = ApiScanner.apiListing();
		return APIUtils.buildSuccess("API methods retrieved succesfully", methods);
	}

	////////////////
	// CLIENT METHODS
	////////////////
	@ApiOperation(value = "Returns all registered Clients", notes = "Returns the Member associated with the ID passed.", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Clients retrieved succesfully.", response = Client.class),
			@ApiResponse(code = 400, message = "No clients registered.", response = WebApplicationException.class) })
	@GET
	@Path("/client-all")
	@Produces("application/json")
	public Response getClient(@Context HttpServletRequest request) throws WebApplicationException {
		List<Client> clients = JEMDao.clientService.getClients();
		return APIUtils.buildSuccess("Succesfully retrieved all Clients", clients);
	}

	@ApiOperation(value = "Returns the Client by ID", notes = "Returns the Client associated with the ID passed.", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Client retrieved succesfully.", response = Client.class),
			@ApiResponse(code = 400, message = "Client not found.", response = WebApplicationException.class) })
	@GET
	@Secured
	@Path("/client/{client_id}")
	@Produces("application/json")
	public Response getClientById(@Context HttpServletRequest request, @PathParam("client_id") int clientId)
			throws WebApplicationException {
		Client c = JEMDao.clientService.getClientById(clientId);
		if (c != null) {
			return APIUtils.buildSuccess("Succesfully retrieved Client", c);
		} else {
			throw APIUtils.buildWebApplicationException(Status.BAD_REQUEST, APIStatus.ERROR, "Client not found",
					"The Client ID supplied does not match any existing Clients.");
		}

	}

	@ApiOperation(value = "Returns the Client's available Jobs by ID", notes = "Returns the Client available jobs", response = List.class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Client Jobs retrieved succesfully.", response = List.class),
			@ApiResponse(code = 400, message = "No Jobs found for Client", response = WebApplicationException.class) })
	@GET
	@Path("/client-jobs/{client_id}")
	@Produces("application/json")
	public Response getClientJobsById(@Context HttpServletRequest request, @PathParam("client_id") int clientId)
			throws WebApplicationException {
		List<JobRequest> jobs = JEMDao.clientService.getClientJobsById(clientId);
		if (jobs != null) {
			return APIUtils.buildSuccess("Succesfully retrieved Client", jobs);
		} else {
			throw APIUtils.buildWebApplicationException(Status.BAD_REQUEST, APIStatus.ERROR, "No jobs available",
					"The Client ID supplied does not match any existing Clients.");
		}
	}

	@ApiOperation("Register a new client")
	@ApiResponses({ @ApiResponse(code = 200, message = "Client succesfully registered", response = String.class) })
	@POST
	@Path("/client-register")
	@Produces("application/json")
	@Consumes("application/json")
	public Response registerClient(@Context HttpServletRequest request, Client c) {
		JEMDao.clientService.registerClient(c);
		return APIUtils.buildSuccess("Succesfully registered client", c);
	}

	////////////////
	// JOBS METHODS
	////////////////
	@ApiOperation("Get all jobs, complete or not")
	@ApiResponses({ @ApiResponse(code = 200, message = "All jobs retrieved", response = List.class) })
	@GET
	@Secured
	@Path("/job-all")
	public Response getJobs(@Context HttpServletRequest request) {
		List<JobRequest> jobs = JEMDao.jobService.getAllJobs();
		GenericEntity<List<JobRequest>> entity = new GenericEntity<List<JobRequest>>(jobs) {
		};
		return APIUtils.buildSuccess("Succesfully retrieved Client", entity);

	}

	@ApiOperation("Get all incomplete jobs")
	@ApiResponses({ @ApiResponse(code = 200, message = "All incomplete jobs retrieved", response = List.class) })
	@GET
	@Secured
	@Path("/job-incomplete")
	@Produces("application/json")
	public Response getIncompleteJobs(@Context HttpServletRequest request) {
		List<JobRequest> jobs = JEMDao.jobService.getAllIncompleteJobs();
		GenericEntity<List<JobRequest>> entity = new GenericEntity<List<JobRequest>>(jobs) {
		};
		return APIUtils.buildSuccess("Incomplete jobs retrieved", entity);
	}

//    @ApiOperation("Get jobs by Service")
//    @ApiResponses({@ApiResponse(code = 200, message = "Jobs retrieved by Service", response = List.class)})
//    @GET
//    @Path("/job-search/service/{service}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response getJobsByService(@Context HttpServletRequest request, @PathParam("service") com.jem.models.Service s) {
//	
//	List<JobRequest> jobs = LawnBuzzDao.jobService.getJobsByService(s);
//	GenericEntity<List<JobRequest>> entity  = APIUtils.toGeneric(jobs);
//	if(jobs.size()>0) {
//	    return APIUtils.buildSuccess("Best jobs retrieved", entity);
//	}else {
//	    return APIUtils.buildSuccess("No search results", entity);
//	}
//
//    }
	@ApiOperation("Get jobs by query")
	@ApiResponses({ @ApiResponse(code = 200, message = "Jobs retrieved by Service", response = List.class) })
	@GET
	@Secured
	@Path("/job-search/{query}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response getJobsBySearch(@Context HttpServletRequest request, @PathParam("query") String query) {
		query = query.toLowerCase().replace("_", "").trim();
		List<JobRequest> jobs = JEMDao.jobSearch.search(query, 5);
		GenericEntity<List<JobRequest>> entity = APIUtils.toGeneric(jobs);
		if (jobs.size() > 0) {
			return APIUtils.buildSuccess("Best jobs retrieved", entity);
		} else {
			return APIUtils.buildSuccess("No search results", entity);
		}
	}

	@GET

	@Path("/job-radius-query-search/{sp_id}/{query}/{radius}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public Response getJobsByQueryRadius(@Context HttpServletRequest request, @PathParam("sp_id") int spId,
			@PathParam("query") String query, @PathParam("radius") int radius) {
		ServiceProvider sp = JEMDao.serviceProviderService.getServiceProviderById(spId);
		List<JobRequest> jobs = JEMDao.jobSearch.search(query, 30);
		ArrayList<JobRequest> toRemove = new ArrayList<JobRequest>();
		for (JobRequest job : jobs) {
			if (!JEMDao.geoService.isInRadius2(sp.getLoc(), job.getLoc(), radius)) {
				toRemove.add(job);
			}
		}
		jobs.removeAll(toRemove);

		GenericEntity<List<JobRequest>> entity = APIUtils.toGeneric(jobs);
		if (jobs.size() > 0) {
			return APIUtils.buildSuccess("Best jobs retrieved", entity);
		} else {
			return APIUtils.buildSuccess("No search results", entity);
		}
	}

	@GET
	@Path("/job-radius-search/{sp_id}/{radius}")
	@Produces("application/json")
	public Response getJobsInRadius(@Context HttpServletRequest request, @PathParam("sp_id") int id,
			@PathParam("radius") int radius) {
		ServiceProvider logged = JEMDao.serviceProviderService.getServiceProviderById(1);
		List<JobRequest> jobs = JEMDao.jobService.getJobsInRadius(logged.getLoc(), radius);
		GenericEntity<List<JobRequest>> entity = APIUtils.toGeneric(jobs);
		return APIUtils.buildSuccess("No search results", entity);

	}

	@POST
	@Path("/job-add")
	@Produces("application/json")
	@Consumes("application/json")
	public Response addJob(@Context HttpServletRequest request, JobRequest job) {
		JEMDao.jobService.addJob(job);
		return Response.ok(job).build();
	}

	@GET
	@Path("/job/geoloc/{job_id}")
	@Produces("application/json")
	public Response getJobGeoloc(@Context HttpServletRequest request, @PathParam("job_id") int jobId) {
		GeoLocation loc = null;
		loc = JEMDao.jobService.getGeoLocJob(jobId);
		if (loc != null) {
			return APIUtils.buildSuccess("Succesfully retrieved Job Geolocation", loc.reverseGeocode());
		} else {
			throw APIUtils.buildWebApplicationException(Status.BAD_REQUEST, APIStatus.ERROR,
					"ServiceProvider not found",
					"The ServiceProvider ID supplied does not match any existing Clients.");
		}
	}
	//////////////////////////
	// SERVICE PROVIDER METHODS
	//////////////////////////

	@ApiOperation(value = "Returns all registered ServiceProviders", notes = "SP", response = List.class)
	@ApiResponses({
			@ApiResponse(code = 200, message = "ServiceProviders retrieved succesfully.", response = List.class),
			@ApiResponse(code = 400, message = "No ServiceProviders registered.", response = WebApplicationException.class) })
	@GET
	@Path("/sp-all")
	@Produces("application/json")
	public Response getServiceProviders(@Context HttpServletRequest request) {
		List<ServiceProvider> sPs = JEMDao.serviceProviderService.getServiceProviders();
		return Response.ok(sPs).build();
	}

	@GET
	@Path("/services")
	@Secured
	@Produces("application/json")
	public Response getServices(@Context HttpServletRequest request) {
		com.jem.models.Service[] services = com.jem.models.Service.values();
		return APIUtils.buildSuccess("Retrieved all services", services);
	}

	@ApiOperation(value = "Returns A ServiceProvider by ID, username, or email", notes = "Returns the ServiceProvier associated with the given input ID", response = List.class)
	@ApiResponses({
			@ApiResponse(code = 200, message = "ServiceProvider retrieved succesfully.", response = ServiceProvider.class),
			@ApiResponse(code = 400, message = "No ServiceProvider found with the given ID.", response = WebApplicationException.class) })
	@GET
	@Secured
	@Path("/sp/{sp_id}")
	@Produces("application/json")
	public Response getServiceProviderById(@Context HttpServletRequest request, @PathParam("sp_id") String spId) {
		ServiceProvider sp = null;
		spId.trim();
		if (StringUtils.isNumeric(spId)) {
			sp = JEMDao.serviceProviderService.getServiceProviderById(Integer.parseInt(spId));
		} else if (spId.contains("@")) {
			sp = JEMDao.serviceProviderService.getServiceProviderByEmail(spId);
		} else if (StringUtils.isAlpha(spId)) {
			sp = JEMDao.serviceProviderService.getServiceProviderByUsername(spId);
		}
		if (sp != null) {
			return APIUtils.buildSuccess("Succesfully retrieved ServiceProvider", sp);
		} else {
			throw APIUtils.buildWebApplicationException(Status.BAD_REQUEST, APIStatus.ERROR,
					"ServiceProvider not found",
					"The ServiceProvider ID supplied does not match any existing Clients.");
		}
	}

	@GET
	@Secured
	@Path("/sp/geoloc/{sp_id}")
	@Produces("application/json")
	public Response getServiceProviderGeoloc(@Context HttpServletRequest request, @PathParam("sp_id") String spId) {
		ServiceProvider sp = null;
		try {
			sp = JEMDao.serviceProviderService.getServiceProviderById(Integer.parseInt(spId));
		} catch (Exception e) {
			sp = JEMDao.serviceProviderService.getServiceProviderByUsername(spId);
		}

		if (sp != null) {
			return APIUtils.buildSuccess("Succesfully retrieved ServiceProvider Geolocation",
					sp.getLoc().reverseGeocode());
		} else {
			throw APIUtils.buildWebApplicationException(Status.BAD_REQUEST, APIStatus.ERROR,
					"ServiceProvider not found",
					"The ServiceProvider ID supplied does not match any existing Clients.");
		}
	}

	@GET
	@Path("/sp/geoloc/user/{username}")
	@Produces("application/json")
	@Secured
	public Response getServiceProviderGeolocUser(@Context HttpServletRequest request,
			@PathParam("username") String username) {
		ServiceProvider sp = null;
		sp = JEMDao.serviceProviderService.getServiceProviderByUsername(username);

		if (sp != null) {
			return APIUtils.buildSuccess("Succesfully retrieved ServiceProvider Geolocation",
					sp.getLoc().reverseGeocode());
		} else {
			throw APIUtils.buildWebApplicationException(Status.BAD_REQUEST, APIStatus.ERROR,
					"ServiceProvider not found",
					"The ServiceProvider ID supplied does not match any existing Clients.");
		}
	}

	@POST
	@Path("/sp-register")
	@Produces("application/json")
	@Consumes("application/json")
	public Response registerServiceProvider(@Context HttpServletRequest request, ServiceProvider sp) {
		JEMDao.serviceProviderService.registerServiceProvider(sp);
		return Response.ok(sp).build();
	}

	@POST
	@Path("/sp-register-service/{sp_id}/{service}")
	@Produces("application/json")
	@Consumes("application/json")
	@Secured
	public Response registerServiceProviderService(@Context HttpServletRequest request, @PathParam("sp_id") int spId,
			@PathParam("service") com.jem.models.Service service) {
		JEMDao.serviceProviderService.registerServiceProviderService(spId, service);
		return Response.ok(service).build();
	}

	@POST
	@Path("/sp-register-geoloc/{sp_id}")
	@Produces("application/json")
	@Consumes("application/json")
	@Secured
	public Response registerServiceProviderGeoLoc(@Context HttpServletRequest request, @PathParam("sp_id") int spId,
			GeoLocation loc) {
		JEMDao.serviceProviderService.registerServiceProviderGeoLoc(spId, loc);
		return Response.ok(loc).build();
	}

	@POST
	@Path("/sp/{sp_id}")
	@Produces("application/json")
	@Consumes("application/json")
	@Secured
	public Response updateServiceProvider(@Context HttpServletRequest request, @PathParam("sp_id") int spId,
			ServiceProvider sp) {
		JEMDao.serviceProviderService.updateServiceProvider(sp);
		return Response.ok(sp).build();
	}

	@PUT
	@Path("/sp/{sp_id}")
	@Produces("application/json")
	@Consumes("application/json")
	@Secured
	public Response updateServiceProvider2(@Context HttpServletRequest request, @PathParam("sp_id") int spId,
			ServiceProvider sp) {
		JEMDao.serviceProviderService.updateServiceProvider(sp);
		return Response.ok(sp).build();
	}

}
