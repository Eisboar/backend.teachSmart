package com.wanda.data.questionsheet;

/**
 * static creator class, instantiate a Question to a given question type 
 * @author Sascha Haseloff <sascha.haseloff@gmail.com>
 *
 */

public class QuestionCreator {
	public static Question createQuestion(String typeString){
		
		Question question = null;
		if (typeString.toLowerCase().equals("rating")){
			question = new RatingQuestion();
		} else if (typeString.toLowerCase().equals("multiple_choice")){
			question = new MultipleChoiceQuestion();
		}
		return question;
	}
}
