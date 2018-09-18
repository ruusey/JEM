package com.lawnbuzz.models;

public enum APIStatus {
	
	SUCCESS(0),
	ERROR(1);
	
	private int id;
	
	private APIStatus(int id) {
		this.id = id;
	}

	public int id() {
		return id;
	}

}
