package com.wanda.json;

import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.wanda.data.MetaData;
import com.wanda.data.Question;
import com.wanda.data.QuestionSheet;
import com.wanda.rest.Login;

/**
 * Basic class to parse all incoming JsonString's into the proper Objects
 * @author Sascha Haseloff
 *
 */
public class JsonReader {
	
	static final Logger LOGGER = Logger.getLogger(JsonReader.class);
		
	private JsonFactory jsonFactory;

	/**
	 * simple Constructor
	 */
	public JsonReader(){
		jsonFactory = new JsonFactory();
	}
	
	/**
	 * Method to parse the Login-information (username, password)
	 * @throws IOException 
	 * @throws JsonParseException 
	 */
	public MetaData parseLogin(String loginJsonString) throws JsonParseException, IOException{
		MetaData metaData = new MetaData();
		
		JsonParser jsonParser = jsonFactory.createJsonParser(loginJsonString);
		jsonParser.nextToken();
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String namefield = jsonParser.getCurrentName();
			jsonParser.nextToken();
			if ("username".equals(namefield)) {
				metaData.setUsername(jsonParser.getText());
			} else if ("password".equals(namefield)) {
				metaData.setPassword(jsonParser.getText());
			}
		}
		
		return metaData;
	}
	
	/**
	 * Method to parse the QuestionSheet out of a posted jsonstring
	 * @param questionSheetJsonString
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public QuestionSheet parseQuestionSheet(String questionSheetJsonString) throws JsonParseException, IOException{
		QuestionSheet questionSheet = new QuestionSheet();
		
		JsonParser jsonParser = jsonFactory.createJsonParser(questionSheetJsonString);
		jsonParser.nextToken();
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String namefield = jsonParser.getCurrentName();
			jsonParser.nextToken();
			if ("name".equals(namefield)) {
				questionSheet.setName(jsonParser.getText());
			} else if ("questions".equals(namefield)) {
				questionSheet.setQuestions(parseQuestions(jsonParser));
			}
		}
		
		return questionSheet;
		
	}
	
	/**
	 * Method to parse the Question-Array, (called by the parseQuestionSheet-Method)
	 * @param jsonParser
	 * @return
	 * @throws IOException 
	 * @throws JsonParseException 
	 */
	private Vector<Question> parseQuestions(JsonParser jsonParser) throws JsonParseException, IOException{
		Vector<Question> questions = new Vector<Question>();
		int questionsCounter = 0; 
		//get into the array
		jsonParser.nextToken();
		//parse questions until end of array is reached
		while (jsonParser.nextToken() != JsonToken.END_ARRAY) { 
			//LOGGER.debug(jsonParser.getText());
			String namefield = jsonParser.getCurrentName();
			if ("question".equals(namefield)) {
				//jump to the question
				jsonParser.nextToken();
				LOGGER.debug(jsonParser.getText());
				questions.addElement(new Question(++questionsCounter,jsonParser.getText()));
			}
			//skip end of object
			//jsonParser.nextToken();
		}
		//jsonParser.nextToken();
		LOGGER.debug(questionsCounter+ " Questions parsed");
		return questions;
	}
}
