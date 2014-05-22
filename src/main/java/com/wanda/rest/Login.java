package com.wanda.rest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.wanda.data.MetaData;
import com.wanda.db.MySqlDataSource;
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
		
			return ":-)";
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
		
		LOGGER.debug(inputJsonString);
		LOGGER.debug("login attempt, start parsing");
		JsonWriter jsonWriter = new JsonWriter();
		
		//check if request is empty
		if (inputJsonString == null || inputJsonString.trim().equals("")){
			LOGGER.debug("POST-Request empty");
			return jsonWriter.buildErrorMessage("Empty request");
		}
		
		//parse request
		JsonReader jsonReader = new JsonReader();
		MetaData metaData = null;

		try {
			metaData = jsonReader.parseLogin(inputJsonString);
		} catch (IOException e) {
			LOGGER.debug("Couldn't parse Login Informaation"+ e);
			return jsonWriter.buildErrorMessage("Failed to parse login information");
		}
		
		//check if login name or password are empty
		if (metaData.getUsername()==null || metaData.getPassword()==null){
			LOGGER.debug("login informayion not complete");
			return jsonWriter.buildErrorMessage("Username or password not found");
		}
		
		LOGGER.debug("Login attempt from user: "+metaData.getUsername());
		
		//check db for userdata
	
		PreparedStatement preparedStatement;
		Connection connection;
		ResultSet resultSet=null;
		
		int userID =-1;
		
		try{
			connection = MySqlDataSource.getMySDataSource().getConnection();
			String query= "SELECT ID FROM Users WHERE name = ? AND password = ?";
			preparedStatement = connection.prepareStatement(query);
			
			//fill the preparedStatement with the userinformation
			preparedStatement.setString(1, metaData.getUsername());
			preparedStatement.setString(2, metaData.getPassword());
			
			resultSet = preparedStatement.executeQuery();
			
			//read the resultset
			if (resultSet.next()==false){
				LOGGER.debug("failed login, wrong username/password");
				return jsonWriter.buildErrorMessage("failed login, wrong username/password");
			} else {
				userID = resultSet.getInt(1);
			}
		} catch (SQLException e){
			e.printStackTrace();	
		}
		
		//update last use

		return "Got it! " + userID;
	}
}
