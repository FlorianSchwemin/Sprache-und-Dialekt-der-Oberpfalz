package android.projekt.thiemo;

public class QuizLogik {
	
	
	public QuizLogik(){
		
	}
	
	public boolean rightAnswer(int answer, int correct){
		if(answer == correct){
			return true;
		}else{
		return false;
		}
	}

}
