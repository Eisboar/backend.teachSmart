package com.wanda.data.questionsheet;

import java.util.Map;

/**
 * simple class, representing a useranswer on a rating question
 * @author Sascha Haseloff <sascha.haseloff@gmail.com>
 *
 */

public class RatingQuestionAnswer extends UserAnswer {

	public RatingQuestionAnswer(){
		super();
		this.setType(QuestionType.RATING);
	}
	
	public int rating;

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public void setAnswerData(Map<String,String> data) {
		this.rating = Integer.valueOf(data.get("rating"));
	}
	
	@Override
	public String toString(){
		String output = super.toString();
		
		output+= "rating: ";
		output+= rating;
		output+= "\n";
		
		return output;
	}
}
