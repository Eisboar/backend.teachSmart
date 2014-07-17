package com.wanda.data.questionsheet;

/**
 * simple class, representing the answers of a multiple choice question
 * @author Sascha Haseloff <sascha.haseloff@gmail.com>
 *
 */

public class QuestionAnswer {

	public QuestionType type;
	
	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public QuestionAnswer(){
		
	}
	
	private int position;
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	private String answerText;
	
	
}
