package com.wanda.rest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.wanda.data.Question;
import com.wanda.data.QuestionSheet;
import com.wanda.db.MySqlDataSource;
import com.wanda.json.JsonReader;
import com.wanda.json.JsonWriter;

@Path("addSheet")
public class AddSheet {

	static final Logger LOGGER = Logger.getLogger(Login.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String onPost(String inputJsonString) {

		LOGGER.debug(inputJsonString);
		LOGGER.debug("sheet posted, start parsing");

		JsonWriter jsonWriter = new JsonWriter();

		// check if request is empty
		if (inputJsonString == null || inputJsonString.trim().equals("")) {
			LOGGER.debug("POST-Request empty");
			return jsonWriter.buildErrorMessage("Empty request");
		}

		// parse request
		JsonReader jsonReader = new JsonReader();
		QuestionSheet questionSheet = null;

		try {
			questionSheet = jsonReader.parseQuestionSheet(inputJsonString);
		} catch (IOException e) {
			LOGGER.debug("Couldn't parse QuestionSheet Information" + e);
			return jsonWriter
					.buildErrorMessage("Failed to parse question sheet");
		}

		// check if name or questions are empty
		if (questionSheet.getName() == null
				|| questionSheet.getQuestions() == null) {
			LOGGER.debug("questionssheet information not complete");
			return jsonWriter.buildErrorMessage("Name or questions not found");
		}
		
		if (questionSheet.getQuestionCount()== 0) {
			LOGGER.debug("no questions found");
			return jsonWriter.buildErrorMessage("no questions found (a question sheet "
					+ "needs questions!)");
		}
		
		//insert Questionsheet into DB
		
		PreparedStatement preparedStatement;
		Connection connection;
		ResultSet resultSet=null;
		int sheetID = -1;
		
		try{
			
			connection = MySqlDataSource.getMySDataSource().getConnection();
			//insetr the questionsheet
			String query= "INSERT INTO Sheets (name) VALUES (?)";
			preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			
			//fill the preparedStatement with the sheet name
			preparedStatement.setString(1,questionSheet.getName());
			
			preparedStatement.executeUpdate();

			//retrive the generated ID
			resultSet = preparedStatement.getGeneratedKeys();
	        if (resultSet.next()) {
	        	sheetID = resultSet.getInt(1);
	        } else {
	        	LOGGER.debug("id not found");
				return jsonWriter.buildErrorMessage("error: couldn't insert question sheet"
						+ " (could not optain ID)");
	        }
			
//			//insert Questions
	        query = "INSERT INTO Questions (questionsText, sheetID, position) VALUES (?, ?, ?)";
	        for(Question question: questionSheet.getQuestions()){
	        	preparedStatement = connection.prepareStatement(query);
	        	
	        	//fill the preparedStatement with the sheet name
				preparedStatement.setString(1,question.getQuestionText());
				preparedStatement.setInt(2,sheetID);
				preparedStatement.setInt(3,question.getPosition());
				
				preparedStatement.executeUpdate();
	        }
	        
		} catch (SQLException e){
			int errorcode = e.getErrorCode();
			if (errorcode == 1062){
				LOGGER.debug("name of the question sheet already used");
				return jsonWriter.buildErrorMessage("name of the question sheet already used"
						+ " please choose another");
		    }

			LOGGER.debug("Errorcode: "+e.getErrorCode());
			e.printStackTrace();
		}
		
		//update last use

		return "success";
	}
}
