package com.lawnbuzz.search;

import java.util.HashMap;
import java.util.List;

import com.lawnbuzz.models.JobRequest;

public class JobIndex extends IndexInteger{

    @Override
    public void buildJobIndex(List<JobRequest> objectList) {
	for(JobRequest job: objectList) {
	    StringBuffer sb = new StringBuffer();
	    sb.append(job.getService().toString().replace("_","")+ " ");
	    sb.append(job.getShortDescription()+ " ");
	    objectMap.put(job.getId(),job);
	    index(job.getId(),sb.toString());
	}
	
    }

   

}
