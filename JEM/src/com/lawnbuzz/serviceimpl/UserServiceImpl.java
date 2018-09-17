package com.lawnbuzz.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawnbuzz.mappers.UserMapper;
import com.lawnbuzz.models.User;
import com.lawnbuzz.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper mapper;

	public List<User> getAllUsers() {
		return mapper.getAllUsers();
	}

	@Override
	public void addUser(User user) {
		mapper.addUser(user);

	}
	@Override
	public void updateUser(User user) {
		mapper.updateUser(user);
		
	}

}
