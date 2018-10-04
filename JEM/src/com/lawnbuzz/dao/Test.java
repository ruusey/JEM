package com.lawnbuzz.dao;

import java.util.ArrayList;
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

	public static void main(String[] args) {
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    testJobAdd(20,1);
	    
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

	public static void testJobAdd(int numberOfJobs,int ownerId) {
	   
	    ServiceProvider s = LawnBuzzDao.serviceProviderService.getServiceProviderById(3);
		double atlLat = s.getLoc().getLat();
		double atlLng = s.getLoc().getLng();
		Random r = new Random();
		for (int i = 0; i < numberOfJobs; i++) {
			double randLat = atlLat + r.nextDouble();
			double randLng = atlLng + r.nextDouble();

			GeoLocation geo = new GeoLocation(randLat, randLng,
					com.lawnbuzz.util.Util.getCurrentDateTime());
			JobRequest jr = new JobRequest();
			jr.setJobId(ownerId);
			jr.setComplete(false);
			jr.setLoc(geo);
			jr.setService(Service.values()[r.nextInt(Service.values().length)]);
			jr.setLongDescrption("Test Long Description");
			jr.setShortDescription("Mow my fucking lawn");
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