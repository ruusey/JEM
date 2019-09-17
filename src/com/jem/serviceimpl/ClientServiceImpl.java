package com.jem.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jem.mappers.ClientMapper;
import com.jem.models.Client;
import com.jem.models.GeoLocation;
import com.jem.models.JobRequest;
import com.jem.service.ClientService;

@Service("clientService")
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientMapper mapper;

	@Override
	public List<Client> getClients() {
		return mapper.getClients();
	}

	@Override
	public Client getClientById(int id) {
		return mapper.getClientById(id);
	}

	@Override
	public void registerClient(Client client) {
		mapper.registerClient(client);
		mapper.finalizeClientRegistrations(client.getId());
		for (JobRequest job : client.getJobs()) {
			mapper.registerClientJob(client.getId(), job);
		}
		mapper.registerClientGeoLoc(client.getId(), client.getLoc());
	}

	@Override
	public List<JobRequest> getClientJobsById(int clientId) {
		return mapper.getClientJobsById(clientId);
	}

	@Override
	public GeoLocation getGeoLocClientJob(int geoLocId) {
		return mapper.getGeoLocClientJob(geoLocId);
	}

	@Override
	public GeoLocation getGeoLocClient(int geoLocId) {
		return mapper.getGeoLocClient(geoLocId);
	}
}
