package com.wanda.db;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.wanda.rest.Login;

/**
 * Getter Class to lookup the the DBsource form the defined name (JNDI)
 * @author Sascha Haseloff
 */
public class MySqlDataSource{

	//the defined JDNI name (to change this, look into 
	//src/main/webapp/META-INF/context.xml + ../WEB-INF/web.xml)
	final static String dbJNDI = "jdbc/db";
	
	static final Logger LOGGER = Logger.getLogger(Login.class);
	
	static DataSource mySqlDataSource =null;
	
	public static DataSource getMySDataSource(){
		
		//check if the DataSource is already created
		if (mySqlDataSource!=null)
			
			return mySqlDataSource;
		
		try{
			mySqlDataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + dbJNDI);
			LOGGER.debug("MySqlDataSource created");
		}catch(NamingException e){
			e.printStackTrace();
			LOGGER.debug("failed to get MySqlDataSource");
		}
		
		return mySqlDataSource;
	}
}
