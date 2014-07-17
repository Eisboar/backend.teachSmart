package com.wanda.data.questionsheet;

import java.util.Map;

/**
 * simple class, representing the a useranswer of a multiple choice question
 * @author Sascha Haseloff <sascha.haseloff@gmail.com>
 *
 */


public class MultipleChoiceAnswer extends UserAnswer {

 public MultipleChoiceAnswer() {
		super();
		this.setType(QuestionType.MULTIPLE_CHOICE);
	}
	
	private int answerPosition;
	
	public int getAnswerPosition() {
		return answerPosition;
	}

	public void setAnswerPosition(int answerPosition) {
		this.answerPosition = answerPosition;
	}

	@Override
	public void setAnswerData(Map<String,String> data) {
		this.answerPosition = Integer.valueOf(data.get("answer_position"));
	}	
	
	@Override
	public String toString(){
		String output = super.toString();

		
		output+= "answerPosition: ";
		output+= answerPosition;
		output+= "\n";
		
		return output;
	}
}
