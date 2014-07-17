package com.wanda.data.questionsheet;

/**
 * simple enum to determine to the answer request type:
 * 		all: 	request for all collected answers to a question
 * 		single:	request for own given answers, typically from a user
 */

public enum AnswerRequestType {
	ALL, SINGLE
}