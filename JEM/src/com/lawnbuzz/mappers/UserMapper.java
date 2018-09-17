package com.lawnbuzz.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lawnbuzz.models.User;

public interface UserMapper{

	@Select("SELECT id as id, first as firstName, "
			+ "last as lastName, email as email FROM lb.user ")
	public List<User> getAllUsers();
	
	@Insert("INSERT INTO lb.user(first, last, email) VALUES"
	          + "(#{firstName},#{lastName}, #{email})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
	public void addUser(User user);
	
	@Update("UPDATE lb.user SET "
			+ "first=#{firstName}, last=#{lastName}, email=#{email} "
	          + "WHERE id=#{id}")
	public void updateUser(User user);
	
	

}
