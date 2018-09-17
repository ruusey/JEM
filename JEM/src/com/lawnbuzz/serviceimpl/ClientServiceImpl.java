package com.lawnbuzz.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawnbuzz.mappers.ClientMapper;
import com.lawnbuzz.models.Client;
import com.lawnbuzz.models.JobRequest;
import com.lawnbuzz.service.ClientService;

@Service("clientService")
public class ClientServiceImpl implements ClientService{
	
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
		for(JobRequest job: client.getJobs()){
			mapper.registerClientJob(client.getId(), job);
		}
		mapper.registerClientGeoLoc(client.getId(), client.getLoc());
		
	}
	public List<JobRequest> getClientJobsById(int clientId){
		return mapper.getClientJobsById(clientId);
	}
}
