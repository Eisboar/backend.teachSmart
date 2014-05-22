package com.wanda.data;

import java.sql.Timestamp;
import java.util.Vector;

/**
 * data object representing a question sheet
 * @author sash
 *
 */
public class QuestionSheet {

	private int ID;
	
	private Timestamp createDate;

	private String name = null;
	private Vector<Question> questions = null;
	
	public QuestionSheet(){
		//questions = new Vector<Question>();
	}
	
	public QuestionSheet(Vector<Question> questions){
		this.questions = questions;
	}
	
	public Vector<Question> getQuestions(){
		return questions;
	}

	public void setQuestions(Vector<Question> questions){
		this.questions = questions;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getQuestionCount(){
		if (questions!= null)
			return questions.size();
		else 
			return 0;
	}
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
}
