package com.lawnbuzz.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lawnbuzz.models.User;

public interface UserService {
    public String getUserPass(String username);
    public String getUserPass(int uId);
    public void createUserAuth( int uId,  String username);
    public void registerUserAuth(int uId,String token, String expires);
    public boolean userHasToken(int uId);
    public boolean isRegistered(int uId);
    public boolean isRegistered(String username);
    public String getUserToken(int uId);
    public boolean validToken(String token);
}
