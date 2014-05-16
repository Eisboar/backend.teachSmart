package com.wanda.json;

import org.codehaus.jackson.JsonFactory;	

public class JsonWriter {

	private JsonFactory jsonFactory;
	
	public JsonWriter(){
		jsonFactory = new JsonFactory();
	}
	
	public String buildErrorMessage(String errorMessage){
		
		return "";
	}
}
