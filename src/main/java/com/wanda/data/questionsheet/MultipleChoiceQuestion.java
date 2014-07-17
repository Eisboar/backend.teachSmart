package com.wanda.data.questionsheet;

import java.util.Vector;

/**
 * simple class, representing a multiple choice question
 * @author Sascha Haseloff <sascha.haseloff@gmail.com>
 *
 */

public class MultipleChoiceQuestion extends Question {

	
	
	private Vector<QuestionAnswer> answers;
	
	
	public MultipleChoiceQuestion(){
		super(QuestionType.MULTIPLE_CHOICE);
	}
	
	public Vector<QuestionAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Vector<QuestionAnswer> answers) {
		this.answers = answers;
	}


}
