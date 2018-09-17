package com.lawnbuzz.service;

import java.util.List;

import com.lawnbuzz.models.User;

public interface UserService {
	List<User> getAllUsers();
	void addUser(User user);
	void updateUser(User user);
}
