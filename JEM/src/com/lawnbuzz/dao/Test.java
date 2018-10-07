package com.lawnbuzz.dao;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.lawnbuzz.models.Client;
import com.lawnbuzz.models.GeoLocation;
import com.lawnbuzz.models.JobRequest;
import com.lawnbuzz.models.Service;
import com.lawnbuzz.models.ServiceProvider;
import com.lawnbuzz.serviceimpl.ClientServiceImpl;
import com.lawnbuzz.serviceimpl.ServiceProviderServiceImpl;
import com.owlike.genson.Genson;
import com.lawnbuzz.util.Util;

public class Test {
	static Logger LOGGER = Logger.getLogger(Test.class.getName());
	public static Hashtable<Service,String> labels = new Hashtable<Service,String>();
	public static void main(String[] args) {
	    labels.put(Service.BABYSITTING, "Child needs babysitting");
	    labels.put(Service.LAWN_CARE, "Lawn mowed and hedges trimmed");
	    labels.put(Service.HOME_CARE, "Need house-sitting for our vacation");
	    labels.put(Service.DELIVERY, "Requesting TacoBell delivery");
	    labels.put(Service.MOVING_SERVICES, "Need help moving houses");
	    labels.put(Service.GENERAL_HELP, "Need assistance constructing tree house");
	    labels.put(Service.CLEANING, "House floor needs to be mopped");
	    labels.put(Service.PRESSURE_WASHING, "Need back porch and driveway pressure-washed");
	    labels.put(Service.DELIVERY_SERVICES, "Take my wife to the airport");
	    labels.put(Service.DOG_SITTING, "Feed and walk our dog while we are on vacation");
	    labels.put(Service.PROJECT_ASSISTANCE, "Tutor me in CSCE146");
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    testLogin("ruboy123");
	    
	    //LawnBuzzDao dao = new LawnBuzzDao();
//		
//		ServiceProvider me = LawnBuzzDao.serviceProviderService
//				.getServiceProviderById(1);
//		int radius = 50;
//		GeocodingResult res = LawnBuzzDao.geoService
//				.reverseGeocode(me.getLoc());
//		long start = System.currentTimeMillis();
//		ArrayList<JobRequest> myJobs = new ArrayList<JobRequest>(
//				LawnBuzzDao.jobService.getJobsByService(Service.PRESSURE_WASHING));
//
//		LOGGER.info("Found " + myJobs.size() + " jobs within " + radius
//				+ "miles of " + res.formattedAddress + " in "
//				+ Util.getTimeSince(start));
//		for (JobRequest job : myJobs) {
//			System.out.println(gson.toJson(job));
//		}

	}

	public static void testSPRegister() {
		ApplicationContext cxt = new ClassPathXmlApplicationContext(
				"classpath:springConfig.xml");
		// SELECT THE DEFINED USER SERVICE FROM SERVICEIMPL
		// UserServiceImpl service =
		// (UserServiceImpl)cxt.getBean("userService");

		ServiceProviderServiceImpl service = (ServiceProviderServiceImpl) cxt
				.getBean("serviceProviderService");
		
		ServiceProvider sp = new ServiceProvider();
		sp.setEmail("nmello7337@gmail.com");
		sp.setUserName("nmello");
		sp.setFirstName("Natalie");
		sp.setLastName("Mello");
		List<com.lawnbuzz.models.Service> sList = new ArrayList<com.lawnbuzz.models.Service>();
		sList.add(com.lawnbuzz.models.Service.BABYSITTING);
		sList.add(com.lawnbuzz.models.Service.CLEANING);
		GeoLocation geo = new GeoLocation(35.2271, 80.8431,
			com.lawnbuzz.util.Util.getCurrentDateTime());

		sp.setServices(sList);
		sp.setLoc(geo);
		sp.setRating(0);

		service.registerServiceProvider(sp);

		List<ServiceProvider> test = service.getServiceProviders();
		System.out.println(test.hashCode());
	}
	public static void testCliRegister() {
		ApplicationContext cxt = new ClassPathXmlApplicationContext(
				"classpath:springConfig.xml");
		// SELECT THE DEFINED USER SERVICE FROM SERVICEIMPL
		// UserServiceImpl service =
		// (UserServiceImpl)cxt.getBean("userService");


		ServiceProviderServiceImpl service = (ServiceProviderServiceImpl) cxt
				.getBean("serviceProviderService");
		ServiceProvider sp = service.getServiceProviderById(1);
		
		List<com.lawnbuzz.models.Service> sList = new ArrayList<com.lawnbuzz.models.Service>();
		sList.add(com.lawnbuzz.models.Service.PRESSURE_WASHING);
		sList.add(com.lawnbuzz.models.Service.MOVING_SERVICES);
		sList.add(com.lawnbuzz.models.Service.LAWN_CARE);
		

		sp.setServices(sList);
		
		service.updateServiceProvider(sp);

	}
	public static void testSpAddService() {
		ApplicationContext cxt = new ClassPathXmlApplicationContext(
				"classpath:springConfig.xml");
		// SELECT THE DEFINED USER SERVICE FROM SERVICEIMPL
		// UserServiceImpl service =
		// (UserServiceImpl)cxt.getBean("userService");

		ClientServiceImpl service = (ClientServiceImpl) cxt
				.getBean("clientService");

		Client sp = new Client();
		sp.setEmail("nmello7337@gmail.com");
		sp.setUserName("nmello");
		sp.setFirstName("Natalie");
		sp.setLastName("Mello");
		
		List<com.lawnbuzz.models.Service> sList = new ArrayList<com.lawnbuzz.models.Service>();
		sList.add(com.lawnbuzz.models.Service.BABYSITTING);
		sList.add(com.lawnbuzz.models.Service.CLEANING);
		GeoLocation geo = new GeoLocation(35.2271, 80.8431,
				com.lawnbuzz.util.Util.getCurrentDateTime());

		sp.setLoc(geo);
		sp.setRating(0);

		service.registerClient(sp);

	}
	public static void testAuth(String password) {
	    ServiceProvider s = LawnBuzzDao.serviceProviderService.getServiceProviderById(1);
	    if(LawnBuzzDao.userService.isRegistered(s.getId())) {
		System.out.println(LawnBuzzDao.userService.getUserToken(s.getId()));
	    }else {
		LawnBuzzDao.userService.createUserAuth(s.getId(), s.getUserName());
		try {
		    String token = SHAHash.generateStorngPasswordHash(password);
		    System.out.println(token.length());
		    LawnBuzzDao.userService.registerUserAuth(s.getId(), token, Util.getCurrentDateTime2());
		} catch (NoSuchAlgorithmException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (InvalidKeySpecException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	public static void testLogin(String password) {
	    ServiceProvider sp = LawnBuzzDao.serviceProviderService.getServiceProviderById(1);
	    String token =	LawnBuzzDao.userService.getUserToken(sp.getId());
    	try {
	    if(SHAValidate.validatePassword(password, token)) {
	        System.out.println("valid "+token);
	    }
	} catch (NoSuchAlgorithmException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvalidKeySpecException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	}
	public static void testJobAdd(int numberOfJobs,int ownerId) {
	   
	    ServiceProvider s = LawnBuzzDao.serviceProviderService.getServiceProviderById(1);
		double atlLat = s.getLoc().getLat();
		double atlLng = s.getLoc().getLng();
		Random r = new Random();
		for (int i = 0; i < numberOfJobs; i++) {
			double randLat = atlLat;
			double randLng =atlLng;
			if(i%2==0) {
			    randLat= atlLat - r.nextDouble();
			    randLng= atlLng - r.nextDouble();
			}else {
			    randLat= atlLat + r.nextDouble();
			    randLng= atlLng + r.nextDouble();
			}
			GeoLocation geo = new GeoLocation(randLat, randLng,
					com.lawnbuzz.util.Util.getCurrentDateTime());
			Service sr = Service.values()[r.nextInt(Service.values().length)];
			JobRequest jr = new JobRequest();
			jr.setJobId(ownerId);
			jr.setComplete(false);
			jr.setLoc(geo);
			jr.setService(sr);
			jr.setLongDescrption(labels.get(sr));
			jr.setShortDescription(labels.get(sr));
			jr.setPay(r.nextInt(100));
			LawnBuzzDao.jobService.addJob(jr);
		}

	}

	public static void addRandomJobs() {
		GeoLocation geo = new GeoLocation(34.00, 84.3880,
				com.lawnbuzz.util.Util.getCurrentDateTime());
		JobRequest jr = new JobRequest();
		jr.setComplete(false);
		jr.setLoc(geo);
		jr.setService(Service.LAWN_CARE);
		jr.setLongDescrption("We need someone to edge and mow our lawn");
		jr.setShortDescription("Mow my fucking lawn");
		jr.setPay(40.00);
		LawnBuzzDao.jobService.addJob(jr);
	}
}
