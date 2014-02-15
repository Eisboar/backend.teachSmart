package com.wanda.data;


/**
 * simple data object to store & process username, sessionID, etc.
 */

public class MetaData {

	private String username;

	private Long sessionID = null;

	public MetaData(String username) {
		this.username = username;
	}

	public MetaData(String username, Long sessionID) {
		this.username = username;
		this.sessionID = sessionID;
	}

	public String getUsername() {
		return username;
	}

	public Long getSessionID() {
		return sessionID;
	}

	public void setSessionID(Long sessionID) {
		this.sessionID = sessionID;
	}
}
