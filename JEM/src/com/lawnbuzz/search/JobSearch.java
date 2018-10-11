package com.lawnbuzz.search;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.lawnbuzz.dao.LawnBuzzDao;
import com.lawnbuzz.models.JobRequest;

public class JobSearch extends Thread {
    Semaphore sem = new Semaphore(1);
    JobIndex systemsIndex = null;
    private static Hashtable<Integer, JobRequest> systemsHash = null;
    private static List<JobRequest> systems = null;

    private void initialLoad() {
	// LOGGER.info(loggerLabel+"Loading Systems Search Index");

	long time = 0;
	// Hashtable<Integer, com.n2.maestro.common.model.System> systemsHash=null;
	// List<com.n2.maestro.common.model.System> systems=null;

	// List<Configuration> configurationList =configuration.get();

	try {

	    systems = LawnBuzzDao.jobService.getAllIncompleteJobs();
	    // configurationList=configuration.get();

	    // BUILD THE INDEX
	    sem.acquire();
	    // BUILD THE INDEX

	    time = System.currentTimeMillis();
	    systemsIndex = new JobIndex();

	    systemsIndex.buildJobIndex(systems);

	} catch (Exception e) {

	} finally {
	    sem.release();
	}
	// LOGGER.info(loggerLabel+"Systems search Index of ("+systems.size()+") Systems
	// built in "+(System.currentTimeMillis()-time)+" ms");

    }

    @SuppressWarnings("unchecked")
    public List<JobRequest> search(String searchString, Integer maxResults) {

	List<JobRequest> matches = new ArrayList<JobRequest>();
	try {
	    sem.acquire();
	    if (searchString == null || searchString.length() < 1)
		searchString = "a";

	    List<JobRequest> searchMatches = (List<JobRequest>) (Object) systemsIndex.search(searchString, maxResults);
	    if (matches != null) {
		for (JobRequest sc : searchMatches) {

		    matches.add(sc);

		}
	    } else {
		matches = searchMatches;
	    }
	    // Collections.sort(matches, new AuditByDate().reversed());

	} catch (Exception e) {

	} finally {
	    sem.release();
	}

	return matches;
    }

    public void run() {

	while (true) {

	    initialLoad();
	    // long time=0;
	    // Hashtable<Integer, com.n2.maestro.common.model.System> systemsHash=null;
	    // List<com.n2.maestro.common.model.System> systems=null;
	    // try {
	    // systemsHash = maestro.getSystems();
	    //
	    // sem.acquire();
	    // //BUILD THE INDEX
	    //
	    // time = System.currentTimeMillis();
	    // systemsIndex = new SystemsIndex();
	    //
	    //
	    // systems = new
	    // ArrayList<com.n2.maestro.common.model.System>(systemsHash.values());
	    // systemsIndex.buildSystemIndex(systems);
	    //
	    // } catch (Exception e){
	    //
	    // }finally{
	    // sem.release();
	    // }

	    try {
		Thread.sleep(20000);
	    } catch (InterruptedException e) {
	    }

	}

    }
}
