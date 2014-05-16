package com.wanda.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wanda.data.MetaData;
import com.wanda.data.TransmissionData;
import com.wanda.json.JsonReader;
import com.wanda.json.JsonWriter;

/**
 * Root resource (exposed at "myresources" path)
 */

@Path("login")
public class Login {

	static final Logger LOGGER = Logger.getLogger(Login.class);
	
	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 * 
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Got it!";
	}

	/**
	 * Login-Methode called by HTTP-POST ../login
	 * @param inputJson, input string (JSON-object)
	 * @return 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String testPost(String inputJsonString) {
		
		LOGGER.debug("login attempt, start parsing");
		JsonWriter jsonWriter = new JsonWriter();
		
		if (inputJsonString == null || inputJsonString.equals("")){
			// empty request
			LOGGER.debug("POST-Request empty");
			return jsonWriter.buildErrorMessage("Empty request");
		}
		
		JsonReader jsonReader = new JsonReader();
		MetaData metaData = null;

		try {
			metaData = jsonReader.parseLogin(inputJsonString);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LOGGER.debug("Couldn't parse Login Informaation"+ e);
			return jsonWriter.buildErrorMessage("Failed to parse login information");
		}
		
		LOGGER.debug("Login attempt from user: "+metaData.getUsername());
		

		return "Got it! " + metaData.getUsername();
	}
}
