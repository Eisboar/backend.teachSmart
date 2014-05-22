package com.wanda.data;

import java.util.Vector;

/**
 * data object representing a question sheet
 * @author sash
 *
 */
public class QuestionSheet {

	private Vector<Question> questions;
	
	public QuestionSheet(){
		questions = new Vector<Question>();
	}
	
	public QuestionSheet(Vector<Question> questions){
		this.questions = questions;
	}
	
	public Vector<Question> getQuestions(){
		return questions;
	}
}
