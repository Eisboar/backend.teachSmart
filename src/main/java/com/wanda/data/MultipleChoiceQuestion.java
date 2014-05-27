package com.wanda.data;

import java.util.Vector;

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
