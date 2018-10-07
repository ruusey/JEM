package com.lawnbuzz.serviceimpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lawnbuzz.mappers.UserMapper;
import com.lawnbuzz.models.User;
import com.lawnbuzz.service.UserService;
import com.lawnbuzz.util.Util;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper mapper;

    @Override
    public void createUserAuth(int uId, String username) {
	mapper.createUserAuth(uId, username);

    }

    @Override
    public void registerUserAuth(int uId, String token, String expires) {
	mapper.registerUserAuth(uId, token, expires);
    }

    @Override
    public boolean userHasToken(int uId) {
	String token = mapper.getUserToken(uId);
	if (token != null) {
	    return true;
	} else {
	    return false;
	}

    }

    @Override
    public boolean isRegistered(int uId) {
	return mapper.checkUserExistsById(uId);
    }

    @Override
    public String getUserToken(int uId) {
	return mapper.getUserToken(uId);
    }

    @Override
    public boolean isRegistered(String username) {
	return mapper.checkUserExistsByUsername(username);
    }

    @Override
    public boolean validToken(String token) {
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss");
	Date now = Calendar.getInstance().getTime();
	String tokenExp = mapper.getTokenTimestamp(token);
	Date tokenDate = null;
	try {
	    tokenDate = format.parse(tokenExp);
	} catch (ParseException e) {
	    return false;
	}
	if (now.before(tokenDate)) {
	    return true;
	} else {
	    return false;
	}

    }

    @Override
    public String getUserPass(String username) {
	return mapper.getUserPasswordByUsername(username);
    }

    @Override
    public String getUserPass(int uId) {
	return mapper.getUserPasswordById(uId);
    }

}
