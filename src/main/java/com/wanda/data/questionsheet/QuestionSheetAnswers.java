package com.wanda.data.questionsheet;

import java.util.Vector;

/**
 * simple class, representing a collecton of given answers to a question sheet (maybe answersheet 
 * would be nicer or at least more precise)
 * @author Sascha Haseloff <sascha.haseloff@gmail.com>
 *
 */

public class QuestionSheetAnswers {
	
	private int questionSheetID;
	
	private Vector<UserAnswer> answers;

	public int getQuestionSheetID() {
		return questionSheetID;
	}

	public void setQuestionSheetID(int questionSheetID) {
		this.questionSheetID = questionSheetID;
	}

	public Vector<UserAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Vector<UserAnswer> answers) {
		this.answers = answers;
	}
	
	@Override
	public String toString(){
		String output = "";
		output+= "id: ";
		output+= questionSheetID;
		output+= "\n";
		
		for (UserAnswer userAnswer: answers){
			output+= userAnswer.toString();
		}
		
		return output;
	}
}
