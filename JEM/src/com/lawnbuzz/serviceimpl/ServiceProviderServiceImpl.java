package com.lawnbuzz.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import com.lawnbuzz.mappers.ServiceProviderMapper;
import com.lawnbuzz.models.GeoLocation;
import com.lawnbuzz.models.ServiceProvider;

import com.lawnbuzz.service.ServiceProviderService;

@Service("serviceProviderService")
public class ServiceProviderServiceImpl implements ServiceProviderService{
	@Autowired
	private ServiceProviderMapper mapper;
	@Override
	public List<ServiceProvider> getServiceProviders() {
		// TODO Auto-generated method stub
		return mapper.getAllServiceProviders();
	}
	@Override
	public void registerServiceProvider(ServiceProvider sp) {
		//CREATE SERVICE PROVIDER ENTRY FOR INFO AND GEO/SERVICE DATA
		mapper.registerServiceProvider(sp);
		//UPDATE THE POINTERS TO GEOLOC AND SERVICE
		mapper.finalizeServiceProviderRegistration(sp.getId());
		for(com.lawnbuzz.models.Service s:sp.getServices()){
			registerServiceProviderService(sp.getId(),s);
		}
		registerServiceProviderGeoLoc(sp.getId(),sp.getLoc());	
	}
	
	@Override
	public void registerServiceProviderService(int id,
			com.lawnbuzz.models.Service service) {
		mapper.registerServiceProviderService(id, service);
		
	}
	@Override
	public void registerServiceProviderGeoLoc(int id,GeoLocation loc) {
		mapper.registerServiceProviderGeoLoc(id, loc);
		
	}
	@Override
	public ServiceProvider getServiceProviderById(int id) {
		return mapper.getServiceProviderById(id);
	}
	@Override
	public void updateServiceProvider(ServiceProvider sp) {
		if(sp.getEmail()!=null){
			mapper.updateServiceProviderEmail(sp.getId(), sp.getEmail());
		}else if(sp.getUserName()!=null){
			mapper.updateServiceProviderUserName(sp.getId(), sp.getUserName());
		}else if(sp.getFirstName()!=null){
			mapper.updateServiceProviderFirstName(sp.getId(), sp.getFirstName());
		}else if(sp.getLastName()!=null){
			mapper.updateServiceProviderLastName(sp.getId(), sp.getLastName());
		}else if(sp.getServices()!=null){
			mapper.deleteServiceProviderServices(sp.getId());
			for(com.lawnbuzz.models.Service s : sp.getServices()){
				mapper.registerServiceProviderService(sp.getId(), s);
			}
		}else if(sp.getLoc()!=null){
			mapper.updateServiceProviderGeoLoc(sp.getId(), sp.getLoc());
		}else if(sp.getRating()!=-1){
			mapper.updateServiceProviderRating(sp.getId(), sp.getRating());
		}
		
	}
	@Override
	public List<com.lawnbuzz.models.Service> getServices(int serviceId) {
	    return mapper.getServices(serviceId);
	}
	@Override
	public ServiceProvider getServiceProviderByEmail(String email) {
	    return mapper.getServiceProviderByEmail(email);
	}
	@Override
	public ServiceProvider getServiceProviderByUsername(String userName) {
	    return mapper.getServiceProviderByUsername(userName);
	}

}
