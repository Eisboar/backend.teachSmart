package com.wanda.rest;

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

import com.wanda.data.QuestionSheet;
import com.wanda.db.MySqlDataSource;
import com.wanda.json.JsonWriter;

@Path("getSheets")
public class GetSheets {

	static final Logger LOGGER = Logger.getLogger(GetSheets.class);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String onPost(String inputJsonString) {

		// TODO: read userlogin/session

		JsonWriter jsonWriter = new JsonWriter();

		// get all sheets

		PreparedStatement preparedStatement;
		Connection connection;
		ResultSet resultSet = null;
		Vector<QuestionSheet> questionSheets = null;
		
		try {
			connection = MySqlDataSource.getMySDataSource().getConnection();
			String query = "SELECT ID,name,create_date FROM Sheets ORDER BY create_date DESC";
			preparedStatement = connection.prepareStatement(query);

			resultSet = preparedStatement.executeQuery();

			questionSheets = new Vector<QuestionSheet>();
			// read the resultset
			while (resultSet.next() != false) {
				QuestionSheet questionSheet = new QuestionSheet();
				questionSheet.setID(resultSet.getInt(1));
				questionSheet.setName(resultSet.getString(2));
				questionSheet.setCreateDate(resultSet.getTimestamp(3));
				questionSheets.add(questionSheet);
			}
			if (questionSheets.size() == 0) {
				LOGGER.debug("no sheets found");
				return jsonWriter
						.buildErrorMessage("sorry, there are no avaliable questions sheets for you.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		 //write retrived questionsheets into an json-string
		return jsonWriter.buildQuestionSheets(questionSheets);
	}

}
