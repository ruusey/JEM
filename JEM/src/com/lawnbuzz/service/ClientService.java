package com.lawnbuzz.service;

import java.util.List;

import com.lawnbuzz.models.Client;
import com.lawnbuzz.models.JobRequest;

public interface ClientService {
	public List<Client> getClients();
	public Client getClientById(int id);
	public void registerClient(Client client);
	public List<JobRequest> getClientJobsById(int clientId);
}
