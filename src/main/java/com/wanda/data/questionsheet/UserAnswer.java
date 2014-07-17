package com.wanda.data.questionsheet;

import java.util.Map;

/**
 * simple baseclass, representing a useranswer (can be abstract maybe)
 * @author Sascha Haseloff <sascha.haseloff@gmail.com>
 *
 */
public abstract class UserAnswer {

	private String textualFeedback = null;
	private QuestionType type;
	private int position;
	private int count;
	
	public UserAnswer(){
	}
	
	public String getTextualFeedback() {
		return textualFeedback;
	}
	public void setTextualFeedback(String textualFeedback) {
		this.textualFeedback = textualFeedback;
	}
	public QuestionType getType() {
		return type;
	}
	public void setType(QuestionType type) {
		this.type = type;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	public abstract void setAnswerData(Map<String,String> data);
	
	public String toString(){
		String output = "";

		
		output+= "type: ";
		output+= type;
		output+= "\n";
		
		output+= "position: ";
		output+= position;
		output+= "\n";
		
		return output;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
