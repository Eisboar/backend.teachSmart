package com.wanda.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.wanda.data.MetaData;
import com.wanda.data.questionsheet.AnswerRequestType;
import com.wanda.data.questionsheet.MultipleChoiceAnswer;
import com.wanda.data.questionsheet.MultipleChoiceQuestion;
import com.wanda.data.questionsheet.Question;
import com.wanda.data.questionsheet.QuestionAnswer;
import com.wanda.data.questionsheet.QuestionCreator;
import com.wanda.data.questionsheet.QuestionSheet;
import com.wanda.data.questionsheet.QuestionSheetAnswers;
import com.wanda.data.questionsheet.QuestionType;
import com.wanda.data.questionsheet.RatingQuestionAnswer;
import com.wanda.data.questionsheet.UserAnswer;

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
			} else if ("with_answers".equals(namefield)) {
				String type = (jsonParser.getText());
				if (type.equals("all")){
					questionSheet.setAnswerRequestType(AnswerRequestType.ALL);
				} else if (type.equals("single")){
					questionSheet.setAnswerRequestType(AnswerRequestType.SINGLE);
				}
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
		Question question = null;
		Vector<QuestionAnswer> answers = null;
		String questionText = null;
		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
			// LOGGER.debug(jsonParser.getText());
			if (jsonParser.getCurrentToken() == JsonToken.END_OBJECT) {
				question.setQuestionText(questionText);
				if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
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
				// question.setQuestionText(jsonParser.getText());
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
		String answerText = null;
		int position = 0;
		// TODO parse for the object - now the order is relevant! (and yeah,
		// this is bad :) )
		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
			if (jsonParser.getCurrentToken() == JsonToken.END_OBJECT) {
				answer = new QuestionAnswer();
				answer.setAnswerText(answerText);
				answer.setPosition(position);
				answers.add(answer);
			}

			String namefield = jsonParser.getCurrentName();
			if ("position".equals(namefield)) {
				jsonParser.nextToken();
				position = (Integer.valueOf(jsonParser.getText()));
			} else if ("answerText".equals(namefield)) {
				jsonParser.nextToken();
				LOGGER.debug(jsonParser.getText());
				answerText = jsonParser.getText();
			}
		}
		return answers;
	}

	public QuestionSheetAnswers parseQuestionSheetAnswers(
			String requestJsonString) throws NumberFormatException,
			JsonParseException, IOException {

		QuestionSheetAnswers questionSheetAnswers = new QuestionSheetAnswers();

		JsonParser jsonParser = jsonFactory.createJsonParser(requestJsonString);
		jsonParser.nextToken();

		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String namefield = jsonParser.getCurrentName();
			jsonParser.nextToken();
			if ("question_sheet_id".equals(namefield)) {
				questionSheetAnswers.setQuestionSheetID(Integer
						.parseInt(jsonParser.getText()));
			} else if ("answers".equals(namefield)) {
				questionSheetAnswers.setAnswers(parseUserAnswers(jsonParser));
			}
		}
		LOGGER.debug(questionSheetAnswers.toString());
		return questionSheetAnswers;
	}

	private Vector<UserAnswer> parseUserAnswers(JsonParser jsonParser)
			throws JsonParseException, NumberFormatException, IOException {
		Vector<UserAnswer> answers = new Vector<UserAnswer>();

		LOGGER.debug("enter methoder");
		// get into the array
		jsonParser.nextToken();
		// parse answers until end of array is reached
		UserAnswer answer = null;
		String textualFeedback = null;
		int position = -1;
		Vector<String> answerData = null;
		while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
			String namefield = jsonParser.getCurrentName();
			if ("type".equals(namefield)) {
				jsonParser.nextToken();
				String type = jsonParser.getText();
				if (type.equals("multiple_choice"))
					answer = new MultipleChoiceAnswer();
				else if (type.equals("rating"))
					answer = new RatingQuestionAnswer();
			} else if ("position".equals(namefield)) {
				jsonParser.nextToken();
				position = Integer.valueOf(jsonParser.getText());
			} else if ("textual_feedback".equals(namefield)) {
				jsonParser.nextToken();
				textualFeedback = jsonParser.getText();
			} else if ("answer_data".equals(namefield)) {
				// save the answer_data till it's know with question-type it is
				answerData = new Vector<String>();
				while (jsonParser.nextToken() != JsonToken.END_OBJECT)
					answerData.add(jsonParser.getText());
				answerData.add(jsonParser.getText());
				//printVec(answerData);
			}
			if (jsonParser.getCurrentToken() == JsonToken.END_OBJECT) {
				LOGGER.debug("enter addingprocess");
				if (answer != null) {
					 LOGGER.debug("addAnswer");
					// set answer data
					if (answerData != null) {
						Map<String, String> answerDataMap = readSimpleJsonObject(answerData);
						if (answer.getType() == QuestionType.MULTIPLE_CHOICE)
							((MultipleChoiceAnswer) answer)
									.setAnswerData(answerDataMap);
						else if (answer.getType() == QuestionType.RATING)
							((RatingQuestionAnswer) answer)
									.setAnswerData(answerDataMap);
					}
					// set parsed values
					if (textualFeedback != null
							&& !textualFeedback.trim().equals(""))
						answer.setTextualFeedback(textualFeedback);
					answer.setPosition(position);

					//add answers
					answers.add(answer);
					
					// return them to default
					textualFeedback = null;
					position = -1;
					answerData = null;
				}
				answer = null;
			}

		}
		// jsonParser.nextToken();
		// LOGGER.debug(questionsCounter + " Questions parsed");
		
		return answers;
	}

//	private void printVec(Vector<String> data) {
//		for (String string : data)
//			LOGGER.debug(string);
//	}

	public Map<String, String> readSimpleJsonObject(Vector<String> data) {
		Map<String, String> dataMap = new HashMap<String, String>();

		Iterator<String> it = data.iterator();
		while (it.hasNext()) {
			String token = it.next();
			if (!token.equals("{") && !token.equals("}")) {
				dataMap.put(token, it.next());
			}
		}

		return dataMap;
	}

}
