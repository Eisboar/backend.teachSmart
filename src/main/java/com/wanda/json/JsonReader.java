package com.wanda.json;

import java.io.IOException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import com.wanda.data.MetaData;

/**
 * Basic class to parse all incoming JsonString's into the proper Objects
 * @author Sascha Haseloff
 *
 */
public class JsonReader {
	
	private JsonFactory jsonFactory;
	
	/**
	 * simple Constructor
	 */
	public JsonReader(){
		jsonFactory = new JsonFactory();
	}
	
	/**
	 * Methode to parse the Login-infromation (username, password)
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
}
