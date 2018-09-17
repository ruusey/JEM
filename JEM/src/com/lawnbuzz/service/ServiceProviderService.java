package com.lawnbuzz.service;

import java.util.List;

import com.lawnbuzz.models.GeoLocation;
import com.lawnbuzz.models.Service;
import com.lawnbuzz.models.ServiceProvider;

public interface ServiceProviderService {
	public List<ServiceProvider> getServiceProviders();
	public ServiceProvider getServiceProviderById(int id);
	public void registerServiceProvider(ServiceProvider sp);
	public void registerServiceProviderService(int id,Service service);
	public void registerServiceProviderGeoLoc(int id,GeoLocation loc);
	public void updateServiceProvider(ServiceProvider sp);
	
}
