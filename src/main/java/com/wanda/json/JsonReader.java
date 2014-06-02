package com.wanda.json;

import java.io.IOException;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.wanda.data.MetaData;
import com.wanda.data.MultipleChoiceQuestion;
import com.wanda.data.Question;
import com.wanda.data.QuestionAnswer;
import com.wanda.data.QuestionCreator;
import com.wanda.data.QuestionSheet;
import com.wanda.data.QuestionType;
import com.wanda.rest.Login;

/**
 * Basic class to parse all incoming JsonString's into the proper Objects
 * 
 * @author Sascha Haseloff
 * 
 */
public class JsonReader {

	static final Logger LOGGER = Logger.getLogger(JsonReader.class);

	private JsonFactory jsonFactory;

	/**
	 * simple Constructor
	 */
	public JsonReader() {
		jsonFactory = new JsonFactory();
	}

	/**
	 * Method to parse the Login-information (username, password)
	 * 
	 * @throws IOException
	 * @throws JsonParseException
	 */
	public MetaData parseLogin(String loginJsonString)
			throws JsonParseException, IOException {
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
	 * Method to parse the requested ID of the Questionsheet
	 * 
	 * @param requestJsonString
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public QuestionSheet parseQuestionSheetGetRequest(String requestJsonString)
			throws JsonParseException, IOException {
		QuestionSheet questionSheet = new QuestionSheet();

		JsonParser jsonParser = jsonFactory.createJsonParser(requestJsonString);
		jsonParser.nextToken();

		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String namefield = jsonParser.getCurrentName();
			jsonParser.nextToken();
			if ("id".equals(namefield)) {
				questionSheet.setID(Integer.parseInt(jsonParser.getText()));
			}
		}

		return questionSheet;
	}

	/**
	 * Method to parse the QuestionSheet out of a posted jsonstring
	 * 
	 * @param questionSheetJsonString
	 * @return
	 * @throws JsonParseException
	 * @throws IOException
	 */
	public QuestionSheet parseQuestionSheet(String questionSheetJsonString)
			throws JsonParseException, IOException {
		QuestionSheet questionSheet = new QuestionSheet();

		JsonParser jsonParser = jsonFactory
				.createJsonParser(questionSheetJsonString);
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
	 * Method to parse the Question-Array, (called by the
	 * parseQuestionSheet-Method)
	 * 
	 * @param jsonParser
	 * @return
	 * @throws IOException
	 * @throws JsonParseException
	 */
	private Vector<Question> parseQuestions(JsonParser jsonParser)
			throws JsonParseException, IOException {
		Vector<Question> questions = new Vector<Question>();
		int questionsCounter = 0;
		// get into the array
		jsonParser.nextToken();
		// parse questions until end of array is reached
		Question question= null;
		Vector<QuestionAnswer> answers = null;
		String questionText =null ;
		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
			// LOGGER.debug(jsonParser.getText());
			if (jsonParser.getCurrentToken() == JsonToken.END_OBJECT){ 
				question.setQuestionText(questionText);
				if (question.getType()== QuestionType.MULTIPLE_CHOICE){
					((MultipleChoiceQuestion) question).setAnswers(answers);
				}
			}
			String namefield = jsonParser.getCurrentName();
			if ("type".equals(namefield)) {
				jsonParser.nextToken();
				question = QuestionCreator.createQuestion(jsonParser.getText());
				question.setPosition(++questionsCounter);
				questions.add(question);
			} else if ("questionText".equals(namefield)) {
				// jump to the question
				jsonParser.nextToken();
				LOGGER.debug(jsonParser.getText());
				//question.setQuestionText(jsonParser.getText());
				questionText = jsonParser.getText();
				// questions.addElement(new
				// Question(++questionsCounter,jsonParser.getText()));
			} else if ("answers".equals(namefield)) {
				answers = parseAnswers(jsonParser);
			}
			
		}
		// jsonParser.nextToken();
		LOGGER.debug(questionsCounter + " Questions parsed");
		return questions;
	}

	private Vector<QuestionAnswer> parseAnswers(JsonParser jsonParser)
			throws JsonParseException, IOException {
		Vector<QuestionAnswer> answers = new Vector<QuestionAnswer>();
		// get into the array
		jsonParser.nextToken();
		QuestionAnswer answer = null;
		//TODO parse for the object - now the order is relevant! (and yeah, this is bad :) )
		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
			String namefield = jsonParser.getCurrentName();
			if ("position".equals(namefield)) {
				jsonParser.nextToken();
				answer = new QuestionAnswer();
				answer.setPosition(Integer.valueOf(jsonParser.getText()));
				answers.add(answer);
			} else if ("answerText".equals(namefield)){
				jsonParser.nextToken();
				LOGGER.debug(jsonParser.getText());
				answer.setAnswerText(jsonParser.getText());
			}
		}
		return answers;
	}
}
