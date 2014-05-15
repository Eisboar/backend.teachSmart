package com.wanda.rest;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wanda.data.MetaData;
import com.wanda.data.TransmissionData;

/**
 * Root resource (exposed at "myresources" path)
 */
@Path("login")
public class Login {

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 * 
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Got it!";
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String testPost(String inputJson) {
		// TransmissionData transmissionData = null;
		MetaData metaData = null;
		JsonFactory jsonFactory = new JsonFactory();
		
		
		try {
			JsonParser jsonParser = jsonFactory.createJsonParser(inputJson);
			jsonParser.nextToken();
			
			while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
				  String fieldname = jsonParser.getCurrentName();
				  jsonParser.nextToken();
				  if (fieldname.equals("meta"))
					  metaData = parseMetaData(jsonParser);
			}
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ObjectMapper mapper = new ObjectMapper();
		// try {
		// transmissionData = mapper.readValue(inputJson,
		// TransmissionData.class);
		// } catch (JsonParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (JsonMappingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return "Got it! " + metaData.getUsername();
	}

	private MetaData parseMetaData(JsonParser jsonParser)
			throws JsonParseException, IOException {
		MetaData metaData = new MetaData();
		while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
			String namefield = jsonParser.getCurrentName();
			jsonParser.nextToken();
			if ("username".equals(namefield)) {
				metaData.setUsername(jsonParser.getText());
			} else if ("password".equals(namefield)) {
				metaData.setPassword(jsonParser.getText());
			} else if ("sessionID".equals(namefield)) {
				if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL)
					metaData.setSessionID(null);
				else 
					metaData.setSessionID(jsonParser.getLongValue());
			}
		}
		return metaData;
	}
}
