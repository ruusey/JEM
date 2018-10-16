package com.jem.swagger;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.jem.rest.API;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import io.swagger.models.Contact;
import io.swagger.models.Info;

@Component
@Configuration
@ApplicationPath("/api-docs")
public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {
	/* Set package to scan for application Resources/Endpoints */

	register(API.class);
	configureSwagger();

    }

    private void configureSwagger() {
    	register(SwaggerSerializers.class);
    	register(ApiListingResource.class);
    	BeanConfig beanConfig = new BeanConfig();
    	
    	beanConfig.setVersion("1.0.0");
    	beanConfig.setSchemes(new String[]{"http,https"});
    	beanConfig.setHost("localhost");
    	beanConfig.setBasePath("/v1");
    	beanConfig.setTitle("JEM Documentation & TestBed");
		beanConfig.setDescription("JEM - Java Event Management <br>API Designed to streamline Student's access to part time work.");
		beanConfig.getSwagger().addConsumes(MediaType.APPLICATION_JSON);
		beanConfig.getSwagger().addProduces(MediaType.APPLICATION_JSON);
    	beanConfig.setResourcePackage("com.jem.rest");
    	beanConfig.setPrettyPrint(true);
    	beanConfig.setScan(true);
    }
    
}
