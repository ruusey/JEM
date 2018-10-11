package com.lawnbuzz.mappers;

import java.util.List;

import org.apache.ibatis.annotations.*;

import com.lawnbuzz.models.GeoLocation;
import com.lawnbuzz.models.Service;
import com.lawnbuzz.models.ServiceProvider;

public interface ServiceProviderMapper {

	@Select("SELECT * FROM lb.service_provider")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "email", column = "email"),
			@Result(property = "userName", column = "username"),
			@Result(property = "firstName", column = "firstname"),
			@Result(property = "lastName", column = "lastname"),
			@Result(property = "rating", column = "rating"),
			@Result(property = "friendlyLocation", column = "friendly_geoloc"),
			@Result(property = "services", javaType = List.class, column = "service_id", many = @Many(select = "getServices")),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLoc")) })
	public List<ServiceProvider> getAllServiceProviders();
	
	
	@Select("SELECT * FROM lb.service_provider WHERE id=#{id} ")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "email", column = "email"),
			@Result(property = "userName", column = "username"),
			@Result(property = "firstName", column = "firstname"),
			@Result(property = "lastName", column = "lastname"),
			@Result(property = "rating", column = "rating"),
			@Result(property = "friendlyLocation", column = "friendly_geoloc"),
			@Result(property = "services", javaType = List.class, column = "service_id", many = @Many(select = "getServices")),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLoc")) })
	public ServiceProvider getServiceProviderById(@Param("id") int id);
	
	@Select("SELECT * FROM lb.service_provider WHERE email=#{email} ")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "email", column = "email"),
			@Result(property = "userName", column = "username"),
			@Result(property = "firstName", column = "firstname"),
			@Result(property = "lastName", column = "lastname"),
			@Result(property = "rating", column = "rating"),
			@Result(property = "friendlyLocation", column = "friendly_geoloc"),
			@Result(property = "services", javaType = List.class, column = "service_id", many = @Many(select = "getServices")),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLoc")) })
	public ServiceProvider getServiceProviderByEmail(@Param("email") String email);
	
	@Select("SELECT * FROM lb.service_provider WHERE username=#{username} ")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "email", column = "email"),
			@Result(property = "userName", column = "username"),
			@Result(property = "firstName", column = "firstname"),
			@Result(property = "lastName", column = "lastname"),
			@Result(property = "rating", column = "rating"),
			@Result(property = "friendlyLocation", column = "friendly_geoloc"),
			@Result(property = "services", javaType = List.class, column = "service_id", many = @Many(select = "getServices")),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLoc")) })
	public ServiceProvider getServiceProviderByUsername(@Param("username") String username);

	@Select("SELECT service"
			+ " FROM lb.service_provider_service WHERE service_id = #{service_id}")
	public List<Service> getServices(int serviceId);

	@Select("SELECT lng,lat,datetime"
			+ " FROM lb.service_provider_geoloc WHERE geoloc_id = #{geoloc_id}")
	public GeoLocation getGeoLoc(int geoLocId);
	
	@Select("SELECT friendly_geoloc"
		+ " FROM lb.service_provider WHERE sp_id = #{sp_id}")
	public String getGeoLocPretty(@Param("sp_id") int spId);

	@Insert("INSERT INTO lb.service_provider (email, username, firstname, lastname,service_id, geoloc_id, rating) VALUES"
			+ "(#{email},#{userName}, #{firstName}, #{lastName}, #{id}, #{id}, #{rating);")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public void registerServiceProvider(ServiceProvider sp);
	
	@Update("UPDATE lb.service_provider SET service_id=#{id}, geoloc_id=#{id} WHERE id=#{id}")
	public void finalizeServiceProviderRegistration(@Param("id") int id);

	@Insert("INSERT INTO  lb.service_provider_service (service_id,service) VALUES "
			+ "(#{id},#{service})")
	public void registerServiceProviderService(@Param("id") int id, @Param("service")Service service);

	@Insert("INSERT INTO  lb.service_provider_geoloc (geoloc_id,lat,lng,datetime) VALUES "
			+ "(#{id},#{loc.lat},#{loc.lng},#{loc.dateTime})")
	public void registerServiceProviderGeoLoc(@Param("id") int id, @Param("loc") GeoLocation loc);
	
	//UPDATES
	@Update("UPDATE lb.service_provider SET email=#{newEmail} WHERE id=#{id}")
	public void updateServiceProviderEmail(@Param("id") int id, @Param("newEmail") String newEmail);
	
	@Update("UPDATE lb.service_provider SET username=#{newUserName} WHERE id=#{id}")
	public void updateServiceProviderUserName(@Param("id") int id, @Param("newUserName") String newUserName);
	
	@Update("UPDATE lb.service_provider SET firstname=#{newFirstName} WHERE id=#{id}")
	public void updateServiceProviderFirstName(@Param("id") int id, @Param("newFirstName") String newFirstName);
	
	@Update("UPDATE lb.service_provider SET lastname=#{newLastName} WHERE id=#{id}")
	public void updateServiceProviderLastName(@Param("id") int id, @Param("newLastName") String newLastName);
	
	@Update("UPDATE lb.service_provider_geoloc SET lat=#{newLocation.lat},lng=#{newLocation.lng},datetime=#{newLocation.dateTime} WHERE geoloc_id=#{geoloc_id}")
	public void updateServiceProviderGeoLoc(@Param("geoloc_id") int geoLocId, @Param("newLocation") GeoLocation newLocation);
	
	@Update("UPDATE lb.service_provider SET rating=#{newRating} WHERE id=#{id}")
	public void updateServiceProviderRating(@Param("id") int id, @Param("newRating") int newRating);
	
	@Update("UPDATE lb.service_provider SET friendly_geoloc=#{friendly_geoloc} WHERE id=#{id}")
	public void updateServiceProviderGeolocPretty(@Param("id") int id, @Param("friendly_geoloc") String prettyLoc);
	
	
	//DELETE t1 FROM lb.service_provider_service t1 INNER JOIN lb.service_provider_service t2 WHERE t1.id > t2.id AND t1.service = t2.service;
	
	
	
	
	@Delete("DELETE FROM lb.service_provider_service WHERE service_id=#{service_id}")
	public void deleteServiceProviderServices(@Param("service_id") int serviceId);
	@Delete("DELETE t1 FROM lb.service_provider_service t1 INNER JOIN lb.service_provider_service t2 WHERE t1.id > t2.id AND t1.service = t2.service")
	public void deleteServiceProviderServiceDuplicates();
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
