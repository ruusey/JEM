package com.jem.util;

import java.util.ArrayList;

import org.glassfish.jersey.server.model.Resource;

import com.jem.rest.API;

public class ApiScanner {
	public static void main(String[] args) {
		apiListing();
	}

	public static ArrayList<String> apiListing() {
		Resource resource = Resource.from(API.class);
		ArrayList<String> methods = getApiMethods(resource.getPath(), resource);
		return methods;
	}

	private static ArrayList<String> getApiMethods(String uriPrefix, Resource resource) {
		ArrayList<String> methods = new ArrayList<String>();
		for (Resource childResource : resource.getChildResources()) {
			String path = childResource.getPath();
			try {
				path = path.substring(1, path.indexOf("/", 2));
			} catch (Exception e) {
				path = path.substring(1);
			}
			methods.add(path);

		}
		return methods;
	}
}
