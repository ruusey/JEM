package com.ljem.util;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.jem.dao.LawnBuzzDao;
import com.jem.models.GeoLocation;
import com.jem.models.JobRequest;

public class GeoLocServices {
	static Logger LOGGER = Logger.getLogger(GeoLocServices.class.getName());
	public GeoApiContext api;

	public GeoLocServices() {
		try {
			api = new GeoApiContext()
					.setApiKey("AIzaSyAPPbs2Yz_oie_ldvKjEyG86ZUmh_PAdiY");
			LOGGER.info("Successfully created GeoApiContext");
		} catch (Exception e) {
			LOGGER.error("Unable to create GeoApiContext",e.getCause());
		}

	}

	public GeocodingResult formatAddress(String address) {
		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.geocode(api, address).await();
		} catch (ApiException e) {
			LOGGER.error("Unable to format address",e);
		} catch (InterruptedException e) {
			LOGGER.error("Unable to format address",e);
		} catch (IOException e) {
			LOGGER.error("Unable to format address",e);
		}
		return results[0];

	}

	public GeocodingResult reverseGeocode(GeoLocation loc) {
		LatLng pos = formatGeoLocation(loc);

		GeocodingResult[] results = null;
		try {
			results = GeocodingApi.reverseGeocode(api, pos).await();
		} catch (ApiException e) {
			LOGGER.error("Unable to reverse geocode",e);
		} catch (InterruptedException e) {
			LOGGER.error("Unable to reverse geocode",e);
		} catch (IOException e) {
			LOGGER.error("Unable to reverse geocode",e);
		}
		return results[0];
	}

	public boolean isInRadius(GeoLocation center, GeoLocation other, int radius) {

		String addressOne = reverseGeocode(center).formattedAddress;
		String addressTwo = reverseGeocode(other).formattedAddress;
		DistanceMatrix result = null;
		try {
			result = DistanceMatrixApi.getDistanceMatrix(api,
					new String[] { addressOne }, new String[] { addressTwo })
					.await();
		} catch (Exception e) {
			LOGGER.error("Unable to complete DistanceMatrixRequest",e);
			return false;
		}
		Distance res = result.rows[0].elements[0].distance;
		double resultantDistance = metersToMiles(res.inMeters);
		return resultantDistance <= radius;
	}

	public LatLng formatGeoLocation(GeoLocation loc) {
		return new LatLng(loc.getLat(), loc.getLng());
	}

	public double metersToMiles(double meters) {
		double d = (double) meters;
		return 0.000621371 * d;
	}
	public ArrayList<JobRequest> getJobsInRadius(GeoLocation center, int radius){
		
		ArrayList<JobRequest> availableJobs = new ArrayList<JobRequest>(LawnBuzzDao.jobService.getAllIncompleteJobs());
		ArrayList<JobRequest> matchingJobs = new ArrayList<JobRequest>();
		for(JobRequest job : availableJobs){
			if(isInRadius2(center,job.getLoc(),radius)){
				matchingJobs.add(job);
			}
		}
		
		return matchingJobs;
	}
	public boolean isInRadius2(GeoLocation center, GeoLocation other, int radius) {

	    final int R = 6371; // Radius of the earth
	    double lat1 = center.getLat();
	    double lat2 = other.getLat();
	    
	    double lon1 = center.getLng();
	    double lon2 = other.getLng();
	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double miles = metersToMiles(distance);
	    return miles<=radius;
	}
}
