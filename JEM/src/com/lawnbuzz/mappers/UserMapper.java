package com.lawnbuzz.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lawnbuzz.models.User;

public interface UserMapper{

	@Select("SELECT auth_id FROM lb.user_creds WHERE  user_id=#{uId} AND username=#{username}")
	public Integer getUserAuth(@Param("user_id") int uId, @Param("username") String username);
	
	@Insert("INSERT INTO lb.user_creds(user_id, username, password) VALUES"
	          + "(#{user_id},#{username}, #{user_id})")
	public void createUserAuth(@Param("user_id") int uId, @Param("username") String username, @Param("password") String password);
	
	@Insert("INSERT INTO lb.user_auth(user_id, user_token, expires) VALUES"
	          + "(#{user_id},#{user_token}, #{expires})")
	public void registerUserAuth(@Param("user_id") int uId, @Param("user_token") String token, @Param("expires") String expires);
	
	@Select("SELECT EXISTS(SELECT 1 FROM lb.user_creds WHERE username=#{username})")
	public boolean checkUserExistsByUsername(@Param("username") String username);
	

	@Select("SELECT EXISTS(SELECT 1 FROM lb.user_auth WHERE user_id=#{uId})")
	public boolean checkUserExistsById(@Param("uId") int uId);
	
	@Select("SELECT user_token FROM lb.user_auth WHERE user_id=#{auth_id}")
	public String getUserToken(@Param("auth_id") int authId);
	@Select("SELECT expires FROM lb.user_auth WHERE user_token=#{token}")
	public String getTokenTimestamp(@Param("token") String token);
	@Select("SELECT password FROM lb.user_creds WHERE  username=#{username}")
	public String getUserPasswordByUsername(@Param("username") String username);
	@Select("SELECT password FROM lb.user_creds WHERE  user_id=#{user_id}")
	public String getUserPasswordById(@Param("user_id") int uId);
}
