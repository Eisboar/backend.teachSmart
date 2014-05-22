package com.wanda.data;

/**
 * data object representing a question
 * @author sash
 *
 */
public class Question {

	private int position; // starting with 1!
	private String questionText;
	
	public Question(int position, String questionText) {
		super();
		this.position = position;
		this.questionText = questionText;
	}
	public int getPosition() {
		return position;
	}
	public String getQuestionText() {
		return questionText;
	}

	
	
	
}
