package com.wanda.rest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;

import com.wanda.data.MultipleChoiceQuestion;
import com.wanda.data.Question;
import com.wanda.data.QuestionAnswer;
import com.wanda.data.QuestionCreator;
import com.wanda.data.QuestionSheet;
import com.wanda.data.QuestionType;
import com.wanda.db.MySqlDataSource;
import com.wanda.json.JsonReader;
import com.wanda.json.JsonWriter;


@Path("getSheet")
public class GetSheet {

	static final Logger LOGGER = Logger.getLogger(GetSheet.class);
	
	private Connection connection = null;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String onPost(String inputJsonString) {
		
		// TODO: read userlogin/session
		
		//read jsonstring
		JsonReader jsonReader = new JsonReader();
		JsonWriter jsonWriter = new JsonWriter();
		QuestionSheet questionSheet;
		try {
			questionSheet = jsonReader.parseQuestionSheetGetRequest(inputJsonString);
			LOGGER.debug("Request for QuestionSheet: "+ questionSheet.getID());
		} catch (IOException e) {
			LOGGER.debug("Couldn't parse question sheet request"+ e);
			return jsonWriter.buildErrorMessage("Failed to parse question sheet request");
		}
		
		//search the db for the question sheet
		
		PreparedStatement preparedStatement;
		ResultSet resultSet=null;
		
		try{
			connection = MySqlDataSource.getMySDataSource().getConnection();
			String query= "SELECT A.name, A.create_date,"
					+ " B.questionText, B.position, B.type, B.ID FROM Sheets AS A JOIN Questions AS B ON (A.ID=B.sheetID) WHERE A.ID = ? ORDER BY B.position ASC";
			preparedStatement = connection.prepareStatement(query);
			
			//fill the preparedStatement with the sheetID
			preparedStatement.setInt(1, questionSheet.getID());
			
			
			resultSet = preparedStatement.executeQuery();
			
			if (resultSet.next()==false){
				LOGGER.debug("no question found");
				return jsonWriter.buildErrorMessage("no question found");
			}
				else{
					questionSheet.setName(resultSet.getString(1));
					questionSheet.setCreateDate(resultSet.getTimestamp(2));
				}
			//read the resultset
			Vector<Question> questions  = new Vector<Question>();
			do {
				Question question = QuestionCreator.createQuestion(resultSet.getString(5));
				question.setQuestionText(resultSet.getString(3));
				question.setPosition(resultSet.getInt(4));
				if (question.getType()==QuestionType.MULTIPLE_CHOICE){
					getAnswers(question, resultSet.getInt(6));
				}
				questions.add(question);
			} while (resultSet.next()!=false);
			
			questionSheet.setQuestions(questions);

		} catch (SQLException e){
			e.printStackTrace();	
			LOGGER.debug("error - could not retrieve question sheet");
			return jsonWriter.buildErrorMessage("error - could not retrieve question sheet");
		}
		
		//return questionSheet as JSON
		return jsonWriter.buildQuestionSheet(questionSheet);
	}
	
	/**
	 * fills the question with the answers from DB
	 * @param question
	 * @param questionID
	 * @throws SQLException 
	 */
	private void getAnswers(Question question, int questionID) throws SQLException{
		String query = "SELECT position, answerText FROM Answers WHERE questionID = ? ORDER BY position ASC";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setInt(1, questionID);
		
		ResultSet resultSet = preparedStatement.executeQuery();
		
		Vector<QuestionAnswer> answers = new Vector<QuestionAnswer>();
		while (resultSet.next()){
			QuestionAnswer answer = new QuestionAnswer();
			answer.setPosition(resultSet.getInt(1));
			answer.setAnswerText(resultSet.getString(2));
			answers.add(answer);
		}
		((MultipleChoiceQuestion) question).setAnswers(answers);
	}
	
}
