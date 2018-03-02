package android.projekt.thiemo;

public class QuestionItem {
	private String question;
	private String answerone;
	private String answertwo;
	private String answerthree;

	private int correctanswer;

	/*
	 * Constructor with Four Strings and one int - question, answersOne, Two, Three, and correctAnswer as an int
	 * Sets the Instance Variables to the assigned Strings and int.
	 */
	public QuestionItem(String question, String answerone, String answertwo,
			String answerthree, int correctanswer) {
		this.answerone = answerone;
		this.answertwo = answertwo;
		this.answerthree = answerthree;

		this.question = question;
		this.correctanswer = correctanswer;
	}

	// Returns the String question
	public String getQuestion() {
		return question;
	}

	// Sets the question to new question
	public void setQuestion(String question) {
		this.question = question;
	}

	// Sets the answerOne to new answer
	public void setAnswerone(String answerone) {
		this.answerone = answerone;
	}

	// Sets the answerTwo to new answer
	public void setAnswertwo(String answertwo) {
		this.answertwo = answertwo;
	}

	// Sets the answerThree to new answer
	public void setAnswerthree(String answerthree) {
		this.answerthree = answerthree;
	}

	// Returs the correct Answer
	public int getCorrectanswer() {
		return correctanswer;
	}
	
	// Sets the correct Answer to new Correct Answer
	public void setCorrectanswer(int correctanswer) {
		this.correctanswer = correctanswer;
	}

	// Gets the correct Answer for each case
	public String getCurrentAnswer(int currentAnswer) {
		switch (currentAnswer) {
		case 0:
			return answerone;
		case 1:
			return answertwo;
		case 2:
			return answerthree;

		}
		return "";
	}
}
