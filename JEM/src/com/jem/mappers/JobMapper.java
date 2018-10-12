package com.jem.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.jem.models.GeoLocation;
import com.jem.models.JobRequest;
import com.jem.models.Service;

public interface JobMapper {
	
	@Select("SELECT * FROM lb.job")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "service", column = "service"),
			@Result(property = "shortDescription", column = "shortdescription"),
			@Result(property = "longDescription", column = "longdescription"),
			@Result(property = "pay", column = "pay"),
			@Result(property = "complete", column = "complete"),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLocJob")) })
	public List<JobRequest> getAllJobs();
	
	@Select("SELECT * FROM lb.job WHERE complete=0")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "service", column = "service"),
			@Result(property = "shortDescription", column = "shortdescription"),
			@Result(property = "longDescription", column = "longdescription"),
			@Result(property = "pay", column = "pay"),
			@Result(property = "complete", column = "complete"),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLocJob")) })
	public List<JobRequest> getAlIncompleteJobs();
	
	@Select("SELECT * FROM lb.job WHERE complete=0 AND service=#{service}")
	@Results(value = {
			@Result(property = "id", column = "id"),
			@Result(property = "service", column = "service"),
			@Result(property = "shortDescription", column = "shortdescription"),
			@Result(property = "longDescription", column = "longdescription"),
			@Result(property = "pay", column = "pay"),
			@Result(property = "complete", column = "complete"),
			@Result(property = "loc", javaType = GeoLocation.class, column = "geoloc_id", many = @Many(select = "getGeoLocJob")) })
	public List<JobRequest> getJobsByService(@Param("service") Service service);
	
	@Update("UPDATE lb.job SET complete=1 WHERE id=#{id}")
	public void completeJob(@Param("id") int id);
	
	
	@Select("SELECT lng,lat,datetime"
			+ " FROM lb.job_geoloc WHERE geoloc_id = #{geoloc_id}")
	public GeoLocation getGeoLocJob(int geoLocId);
	
	@Insert("INSERT INTO lb.job(service, shortdescription, longdescription, geoloc_id, pay,complete,job_id) VALUES"
			+ "(#{service},#{shortDescription}, #{longDescription}, 0, #{pay},0,#{jobId});")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public void addJob(JobRequest jr);
	
	@Update("UPDATE lb.job SET geoloc_id=#{id} WHERE id=#{id}")
	public void finalizeJobRegistration(@Param("id") int id);
	
	@Insert("INSERT INTO  lb.job_geoloc (geoloc_id,lat,lng,datetime) VALUES "
			+ "(#{id},#{loc.lat},#{loc.lng},#{loc.dateTime})")
	public void registerJobGeoLocation(@Param("id") int id, @Param("loc") GeoLocation loc);
}
