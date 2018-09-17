package com.lawnbuzz.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.lawnbuzz.models.Client;
import com.lawnbuzz.models.GeoLocation;
import com.lawnbuzz.models.JobRequest;

public interface ClientMapper {

	@Select("SELECT * FROM lb.client")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "email", column = "email"),
			@Result(property = "userName", column = "username"),
			@Result(property = "firstName", column = "firstname"),
			@Result(property = "lastName", column = "lastname"),
			@Result(property = "rating", column = "rating"),
			@Result(property = "jobs", javaType = List.class, column = "job_id", many = @Many(select = "getClientJobs")),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLocClient")) })
	public List<Client> getClients();

	@Select("SELECT * FROM lb.client WHERE id=#{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "email", column = "email"),
			@Result(property = "userName", column = "username"),
			@Result(property = "firstName", column = "firstname"),
			@Result(property = "lastName", column = "lastname"),
			@Result(property = "rating", column = "rating"),
			@Result(property = "jobs", javaType = List.class, column = "job_id", many = @Many(select = "getClientJobs")),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLocClient")) })
	public Client getClientById(@Param("id") int id);

	@Select("SELECT * FROM lb.job WHERE job_id=#{job_id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "service", column = "service"),
			@Result(property = "shortDescription", column = "shortdescription"),
			@Result(property = "longDescription", column = "longdescription"),
			@Result(property = "pay", column = "pay"),
			@Result(property = "complete", column = "complete"),
			@Result(property = "jobId", column = "job_id"),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLocClientJob")) })
	public List<JobRequest> getClientJobs();

	@Select("SELECT * FROM lb.job WHERE job_id=#{id}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "service", column = "service"),
			@Result(property = "shortDescription", column = "shortdescription"),
			@Result(property = "longDescription", column = "longdescription"),
			@Result(property = "pay", column = "pay"),
			@Result(property = "complete", column = "complete"),
			@Result(property = "jobId", column = "job_id"),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLocClientJob")) })
	public List<JobRequest> getClientJobsById(@Param("id") int clientId);

	@Select("SELECT lng,lat,datetime"
			+ " FROM lb.job_geoloc WHERE geoloc_id = #{geoloc_id}")
	public GeoLocation getGeoLocClientJob(int geoLocId);

	@Select("SELECT lng,lat,datetime"
			+ " FROM lb.client_geoloc WHERE geoloc_id = #{geoloc_id}")
	public GeoLocation getGeoLocClient(int geoLocId);

	@Insert("INSERT INTO lb.client (email, username, firstname, lastname,service_id, geoloc_id, rating) VALUES"
			+ "(#{email},#{userName}, #{firstName}, #{lastName}, #{id}, #{id}, 0);")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public void registerClient(Client client);

	@Update("UPDATE lb.client SET job_id=#{id}, geoloc_id=#{id} WHERE id=#{id}")
	public void finalizeClientRegistrations(@Param("id") int id);

	@Insert("INSERT INTO  lb.job (job.service,job.shortdescription,job.longdescription,#{id},job.pay,job.complete,#{id}) VALUES "
			+ "(#{id},#{service})")
	public void registerClientJob(@Param("id") int id,
			@Param("job") JobRequest job);

	@Insert("INSERT INTO  lb.client_geoloc (geoloc_id,lat,lng,datetime) VALUES "
			+ "(#{id},#{loc.lat},#{loc.lng},#{loc.dateTime})")
	public void registerClientGeoLoc(@Param("id") int id,
			@Param("loc") GeoLocation loc);

	// UPDATES
	@Update("UPDATE lb.client SET email=#{newEmail} WHERE id=#{id}")
	public void updateClientEmail(@Param("id") int id,
			@Param("newEmail") String newEmail);

	@Update("UPDATE lb.client SET username=#{newUserName} WHERE id=#{id}")
	public void updateClientUserName(@Param("id") int id,
			@Param("newUserName") String newUserName);

	@Update("UPDATE lb.client SET firstname=#{newFirstName} WHERE id=#{id}")
	public void updateClientFirstName(@Param("id") int id,
			@Param("newFirstName") String newFirstName);

	@Update("UPDATE lb.client SET lastname=#{newFirstName} WHERE id=#{id}")
	public void updateClientLastName(@Param("id") int id,
			@Param("newLastName") String newLastName);

	@Update("UPDATE lb.client_geoloc SET lat=#{newLocation.lat},lng=#{newLocation.lng},datetime=#{newLocation.dateTime} WHERE geoloc_id=#{geoloc_id}")
	public void updateClientGeoLoc(@Param("geoloc_id") int geoLocId,
			@Param("newLocation") GeoLocation newLocation);

	@Update("UPDATE lb.client SET rating=#{newRating} id=#{id}")
	public void updateClientRating(@Param("id") int id,
			@Param("newRating") int newRating);

	

}
