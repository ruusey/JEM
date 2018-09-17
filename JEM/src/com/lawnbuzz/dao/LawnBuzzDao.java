package com.lawnbuzz.dao;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.lawnbuzz.serviceimpl.ClientServiceImpl;
import com.lawnbuzz.serviceimpl.JobServiceImpl;
import com.lawnbuzz.serviceimpl.ServiceProviderServiceImpl;
import com.lawnbuzz.util.GeoLocServices;
import com.lawnbuzz.util.Util;

@Component
public class LawnBuzzDao {
	static Logger LOGGER = Logger.getLogger(LawnBuzzDao.class.getName());
	public static ServiceProviderServiceImpl serviceProviderService; 
	public static JobServiceImpl jobService;
	public static GeoLocServices geoService;
	public static ClientServiceImpl clientService;
	static{
		long startTime = System.currentTimeMillis();
		ClassPathXmlApplicationContext cxt = new ClassPathXmlApplicationContext("classpath:springConfig.xml");
		LOGGER.info("Initialized LawnBuzzDao class "+cxt.getApplicationName()+" in "+Util.getTimeSince(startTime));
		LOGGER.info("Registering LawnBuzz services...");
		
		//*********************************
		//REGISTER SERVICEPROVIDER SERVICES
		//*********************************
		startTime = System.currentTimeMillis();
		serviceProviderService = (ServiceProviderServiceImpl)cxt.getBean("serviceProviderService");
		LOGGER.info("Successfully registered serviceProviderService in "+Util.getTimeSince(startTime));
		
		//*********************************
		//REGISTER JOBSERVICES
		//*********************************
		startTime = System.currentTimeMillis();
		jobService = (JobServiceImpl)cxt.getBean("jobService");
		LOGGER.info("Successfully registered jobService in "+Util.getTimeSince(startTime));
		
		//*********************************
		//REGISTER GEOSERVICES
		//*********************************
		startTime = System.currentTimeMillis();
		geoService = new GeoLocServices();
		LOGGER.info("Successfully registered geoService in "+Util.getTimeSince(startTime));
		
		//*********************************
		//REGISTER CLIENTSERVICE
		//*********************************
		startTime = System.currentTimeMillis();
		clientService = (ClientServiceImpl)cxt.getBean("clientService");
		LOGGER.info("Successfully registered clientService in "+Util.getTimeSince(startTime));
		
		LOGGER.info("Successfully Created LawnBuzz DAO");
		cxt.close();
		
	}
}
