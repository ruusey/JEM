package com.jem.search;

import java.util.List;

import com.jem.models.JobRequest;

public class JobIndex extends IndexInteger {

	@Override
	public void buildJobIndex(List<JobRequest> objectList) {
		for (JobRequest job : objectList) {
			StringBuffer sb = new StringBuffer();
			sb.append(job.getService().toString().replace("_", "") + " ");
			sb.append(job.getShortDescription() + " ");
			objectMap.put(job.getId(), job);
			index(job.getId(), sb.toString());
		}
	}
}
