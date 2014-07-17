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

import com.wanda.data.questionsheet.MultipleChoiceAnswer;
import com.wanda.data.questionsheet.QuestionSheetAnswers;
import com.wanda.data.questionsheet.QuestionType;
import com.wanda.data.questionsheet.RatingQuestionAnswer;
import com.wanda.data.questionsheet.UserAnswer;
import com.wanda.db.MySqlDataSource;
import com.wanda.json.JsonReader;
import com.wanda.json.JsonWriter;

@Path("addAnswers")
public class AddAnswers {

	static final Logger LOGGER = Logger.getLogger(AddAnswers.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String onPost(String inputJsonString) {

		//TODO: add sessions
		
		
		LOGGER.debug(inputJsonString);
		LOGGER.debug("answers posted, start parsing");

		JsonWriter jsonWriter = new JsonWriter();

		// check if request is empty
		if (inputJsonString == null || inputJsonString.trim().equals("")) {
			LOGGER.debug("POST-Request empty");
			return jsonWriter.buildErrorMessage("Empty request");
		}

		// parse request
		JsonReader jsonReader = new JsonReader();
		QuestionSheetAnswers questionSheetAnswers = null;

		try {
			questionSheetAnswers = jsonReader.parseQuestionSheetAnswers(inputJsonString);
		} catch (IOException e) {
			LOGGER.debug("Couldn't parse QuestionSheet Information" + e);
			return jsonWriter
					.buildErrorMessage("Failed to parse question sheet");
		}

		// check if all necessary  data is there
		if (questionSheetAnswers.getQuestionSheetID() == -1
				|| questionSheetAnswers.getAnswers() == null) {
			LOGGER.debug("answer information not complete");
			return jsonWriter.buildErrorMessage("questionsSheetID or answers not found");
		}
		
		if (questionSheetAnswers.getAnswers().size() == 0) {
			LOGGER.debug("no answers found");
			return jsonWriter.buildErrorMessage("no answers found");
		}
		
		//insert Questionsheet into DB
		
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		ResultSet resultSet=null;
		//int sheetID = -1;
		
		try{
			
			connection = MySqlDataSource.getMySDataSource().getConnection();
			connection.setAutoCommit(false);
			
			for (UserAnswer userAnswer: questionSheetAnswers.getAnswers())
			{
				//retirve questionID
				String query= "SELECT ID FROM QUESTIONS WHERE sheetID = ? AND position = ?";
				preparedStatement = connection.prepareStatement(query);
				
				preparedStatement.setInt(1, questionSheetAnswers.getQuestionSheetID());
				preparedStatement.setInt(2, userAnswer.getPosition());
				
				LOGGER.debug("useranswerposition:" + userAnswer.getPosition() + " ID: "+questionSheetAnswers.getQuestionSheetID());
				
				preparedStatement.execute();
				
				resultSet = preparedStatement.getResultSet();
				
				
				resultSet.next();
				
				int questionID = resultSet.getInt(1);
				preparedStatement.close();
				
				//insert new Answer
				if (userAnswer.getType() == QuestionType.MULTIPLE_CHOICE){
					query = "INSERT INTO MultipleChoiceAnswers (position, questionID) VALUES (?, ?)";
					preparedStatement = connection.prepareStatement(query);
					
					preparedStatement.setInt(1, ((MultipleChoiceAnswer) userAnswer).getAnswerPosition());
					preparedStatement.setInt(2, questionID);
				} else if (userAnswer.getType() == QuestionType.RATING){
					query = "INSERT INTO RatingAnswers (rating, questionID) VALUES (?, ?)";
					preparedStatement = connection.prepareStatement(query);
					
					preparedStatement.setInt(1, ((RatingQuestionAnswer) userAnswer).getRating());
					preparedStatement.setInt(2, questionID);
				}
				
				preparedStatement.executeUpdate();
				//preparedStatement.close();
			}
	        connection.commit();
	        
		} catch (SQLException e){
			LOGGER.debug("Problem inserting Answers");
			e.printStackTrace();
			try {
	              connection.rollback();
	              LOGGER.debug("rollback success");
	            } catch(SQLException e2) {
	                e2.printStackTrace();;
	            }
			return jsonWriter.buildErrorMessage("could not insert Answers");
		} finally {
			try {
				preparedStatement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "success";
	}

}