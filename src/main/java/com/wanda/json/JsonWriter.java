package com.wanda.json;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;

import org.codehaus.jackson.JsonFactory;	
import org.codehaus.jackson.JsonGenerator;

import com.wanda.data.Question;
import com.wanda.data.QuestionSheet;

/**
 * Class to write all necessary json-data out of the used data objects utilizing the jackson 
 * library. 
 * @author Sascha Haseloff
 *
 */
public class JsonWriter {

	private JsonFactory jsonFactory;
	private JsonGenerator jsonGenerator;
	
	public JsonWriter(){
		jsonFactory = new JsonFactory();
	}
	
	/**
	 * Methode to build a error message in JSON
	 * @param errorMessage
	 * @return
	 */
	public String buildErrorMessage(String errorMessage) {
		StringWriter stringWriter = new StringWriter();
		try {
			jsonGenerator = jsonFactory.createJsonGenerator(stringWriter);
			jsonGenerator.writeStartObject();
			jsonGenerator.writeStringField("status", "error");
			jsonGenerator.writeStringField("message", errorMessage);
			jsonGenerator.writeEndObject();
			jsonGenerator.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return stringWriter.toString();
	}
	
	public String buildQuestionSheets(Vector<QuestionSheet> questionSheets){
		StringWriter stringWriter = new StringWriter();
		try{ 
			jsonGenerator = jsonFactory.createJsonGenerator(stringWriter);
			jsonGenerator.writeStartObject();
				jsonGenerator.writeFieldName("questionSheets");
				jsonGenerator.writeStartArray();
					for (QuestionSheet questionSheet: questionSheets){
						jsonGenerator.writeStartObject();
						jsonGenerator.writeStringField("ID", String.valueOf(questionSheet.getID()));
						jsonGenerator.writeStringField("name", questionSheet.getName());
						jsonGenerator.writeStringField("create_date", questionSheet.getCreateDate().toString());
						jsonGenerator.writeEndObject();
					}
				jsonGenerator.writeEndArray();
			jsonGenerator.writeEndObject();
			jsonGenerator.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stringWriter.toString();
	}
	
	public String buildQuestionSheet(QuestionSheet questionSheet){
		StringWriter stringWriter = new StringWriter();
		try{ 
			jsonGenerator = jsonFactory.createJsonGenerator(stringWriter);
			jsonGenerator.writeStartObject();
			
			jsonGenerator.writeStringField("ID",String.valueOf(questionSheet.getID()));
			jsonGenerator.writeStringField("name", questionSheet.getName());
			jsonGenerator.writeStringField("create_date", questionSheet.getCreateDate().toString());
			
			jsonGenerator.writeFieldName("questions");
			jsonGenerator.writeStartArray();
			for (Question question: questionSheet.getQuestions()){
				jsonGenerator.writeStartObject();
				jsonGenerator.writeStringField("position",String.valueOf(question.getPosition()));
				jsonGenerator.writeStringField("questionText",(question.getQuestionText()));
				jsonGenerator.writeEndObject();
			}
			jsonGenerator.writeEndArray();
			
			jsonGenerator.writeEndObject();
			jsonGenerator.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stringWriter.toString();
	}
	
	
}
