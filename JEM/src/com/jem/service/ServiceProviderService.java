package com.jem.service;

import java.util.List;

import com.jem.models.GeoLocation;
import com.jem.models.Service;
import com.jem.models.ServiceProvider;

public interface ServiceProviderService {
	public List<ServiceProvider> getServiceProviders();
	public ServiceProvider getServiceProviderById(int id);
	public ServiceProvider getServiceProviderByEmail(String email);
	public ServiceProvider getServiceProviderByUsername(String userName);
	public void registerServiceProvider(ServiceProvider sp);
	public void registerServiceProviderService(int id,Service service);
	public void registerServiceProviderGeoLoc(int id,GeoLocation loc);
	public void updateServiceProvider(ServiceProvider sp);
	public List<Service> getServices(int serviceId);
	public String getGeoLocation(int id);
	
}
