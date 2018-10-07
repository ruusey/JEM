package com.lawnbuzz.models;

import java.io.Serializable;

public class Credentials implements Serializable {

    

    private String username;
    private String password;
    private static final long serialVersionUID = 1L;

    public Credentials() {
	// TODO Auto-generated constructor stub
    }

    public Credentials(String username, String password) {
	super();
	this.username = username;
	this.password = password;
    }
    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

}
