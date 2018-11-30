package com.jem.service;

import java.util.List;

import com.jem.models.Client;
import com.jem.models.GeoLocation;
import com.jem.models.JobRequest;

public interface ClientService {
	public List<Client> getClients();
	public Client getClientById(int id);
	public void registerClient(Client client);
	public List<JobRequest> getClientJobsById(int clientId);
	public GeoLocation getGeoLocClientJob(int geoLocId);
	public GeoLocation getGeoLocClient(int geoLocId);
}
