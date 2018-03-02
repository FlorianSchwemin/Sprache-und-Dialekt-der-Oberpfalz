package android.projekt.thiemo;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.projekt.johannes.R;
import android.projekt.philipp.FinishActivityTimer;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This Class is for the QuizAcitivty and implements the OnClickListener to
 * interact with the QuizButtons.
 * 
 * The Class uses ClickListener, Animations and AsynkTasks. Also the Class needs
 * the Classes: QuestionItem.java and QuizLogik.java
 * 
 */
public class QuizActivity extends Activity implements OnClickListener {

	private int clicked;
	private ArrayList<QuestionItem> questions = new ArrayList<QuestionItem>();
	private QuizLogik qL = new QuizLogik();
	private int current = 0;
	int currentQuestion;
	private boolean wasCorrect;
	private int count = 0;
	private String rightAnswers;
	private static final int FINISH = 10;

	private TextView questionView;
	private Button[] answerButtons;
	private Button answerOne;
	private Button answerTwo;
	private Button answerThree;
	private Button backButton;
	private Button soundButton;

	private View checkOne;
	private View checkTwo;
	private View checkThree;
	private View checkFour;
	private View checkFive;
	private View[] checkboxes;

	private MediaPlayer question;
	private int[] questionSounds;
	private TextView statusView;

	private Dialog builder1;
	private FinishActivityTimer quizFinishTimer;
	private Activity activity = this;
	private Context context = this;

	private QuizTask quizTask;

	private Animation slideOut;
	private Animation slideIn;
	private RelativeLayout animContent;

	/*
	 * called when activity is created Setups UI Components, MediaPlayer,
	 * Animation and the FinishTimer.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		setupArrayList();
		setupButtons();
		setupMediaPlayer();
		setupCheckboxes();
		setupAnimContent();
		quizFinishTimer = new FinishActivityTimer(this);
		quizFinishTimer.startTimer();
		question.start();
	}

	// Setups Animation
	private void setupAnimContent() {
		animContent = (RelativeLayout) findViewById(R.id.fullscreen_content_controls);
	}

	/*
	 * Setups all Buttons in the Activity and connects them to the ClickListener
	 * The Answer Buttons are "saved" in the answerButtons Array
	 */
	private void setupButtons() {
		backButton = (Button) findViewById(R.id.back_button);
		answerOne = (Button) findViewById(R.id.antwort_1_button);
		answerTwo = (Button) findViewById(R.id.antwort_2_button);
		answerThree = (Button) findViewById(R.id.antwort_3_button);
		soundButton = (Button) findViewById(R.id.quiz_play_button);

		answerButtons = new Button[] { answerOne, answerTwo, answerThree };
		backButton.setOnClickListener(this);
		soundButton.setOnClickListener(this);

		for (int i = 0; i < 3; i++) {
			answerButtons[i].setOnClickListener(this);
		}

		questionView = (TextView) findViewById(R.id.fragetext);
		statusView = (TextView) findViewById(R.id.fortschritt);

	}

	/*
	 * Setups the QuestionItems for every Question and adds them to an ArrayList
	 * (questions) The ArrayList was used, because it wasn't really clear how
	 * much question the cooperate Team wants to be used.
	 */
	private void setupArrayList() {
		QuestionItem questionItemOne = new QuestionItem(
				getString(R.string.frage_1), getString(R.string.anwort_11),
				getString(R.string.anwort_12), getString(R.string.anwort_13), 1);
		QuestionItem questionItemTwo = new QuestionItem(
				getString(R.string.frage_2), getString(R.string.anwort_21),
				getString(R.string.anwort_22), getString(R.string.anwort_23), 2);
		QuestionItem questionItemThree = new QuestionItem(
				getString(R.string.frage_3), getString(R.string.anwort_31),
				getString(R.string.anwort_32), getString(R.string.anwort_33), 0);
		QuestionItem questionItemFour = new QuestionItem(
				getString(R.string.frage_4), getString(R.string.anwort_41),
				getString(R.string.anwort_42), getString(R.string.anwort_43), 2);
		QuestionItem questionItemFive = new QuestionItem(
				getString(R.string.frage_5), getString(R.string.anwort_51),
				getString(R.string.anwort_52), getString(R.string.anwort_53), 0);

		questions.add(questionItemOne);
		questions.add(questionItemTwo);
		questions.add(questionItemThree);
		questions.add(questionItemFour);
		questions.add(questionItemFive);

	}

	/*
	 * On Every Click the FinishTimer is reset. The onClick Method uses a
	 * switch-case to react on User Clicks. Everytime a AnswerButton is clicked
	 * the other AnswerButtons are disabled and the InstanceVariable "clicked"
	 * is assigned to the clicked button.
	 * 
	 * If the Back Button is clicked the User comes back to the StartActivity
	 * and the MediaPlayer/Animation is canceled
	 * 
	 * If the "Play again" Button is clicked the Users hears again the current
	 * Question
	 * 
	 * If the Answer Buttons are clicked a new QuizTask is started.
	 */
	@Override
	public void onClick(View v) {
		resetStartFinishTimer();

		switch (v.getId()) {
		case R.id.antwort_1_button:
			clicked = 0;
			disableButtons();
			break;
		case R.id.antwort_2_button:
			clicked = 1;
			disableButtons();
			break;
		case R.id.antwort_3_button:
			clicked = 2;
			disableButtons();
			break;
		case R.id.back_button:
			clicked = FINISH;
			if (question.isPlaying()) {
				question.stop();
			}
			if (quizTask != null) {
				quizTask.cancel(true);
			}
			question.release();
			activity.finish();
			break;

		case R.id.quiz_play_button:
			clicked = 5;
			question.start();

		}
		if (clicked < 5) {
			quizTask = new QuizTask();
			quizTask.execute();
		}

	}

	/*
	 * This class is suppose to change the Questions and Answers to new ones and
	 * shows the User whether his/her answer was correct or incorrect.
	 */
	private class QuizTask extends AsyncTask<Void, Void, Void> {

		/*
		 * in onPreExecute the Users answer is checked whether correct or
		 * incorrect and the Buttons are colored whether correct or incorrect
		 * The Instance Variable current (for the questions) is counted up.
		 */
		@Override
		protected void onPreExecute() {
			// checks if correct or incorrect
			wasCorrect = qL.rightAnswer(clicked, questions.get(current)
					.getCorrectanswer());
			// if correct count a variable up to get the score
			if (wasCorrect) {
				count++;
				answerButtons[clicked]
						.setBackgroundResource(R.drawable.quiz_button_green);
				disableButtons();
				checkboxes[current]
						.setBackgroundResource(R.drawable.checkbox_green);
			} else {

				answerButtons[clicked]
						.setBackgroundResource(R.drawable.quiz_button_red);
				answerButtons[questions.get(current).getCorrectanswer()]
						.setBackgroundResource(R.drawable.quiz_button_green);
				disableButtons();
				checkboxes[current]
						.setBackgroundResource(R.drawable.checkbox_red);
			}
			current++;
			if (current < 5) {
				question = MediaPlayer.create(context, questionSounds[current]);
			}

			super.onPreExecute();
		}

		/*
		 * in doInBackGround the Thread sleeps for 3 seconds (this is for the
		 * User to see the correct Answer)
		 */
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			return null;
		}

		/*
		 * in onPostExecute it is checked if the Quiz is not over, when new
		 * SlideOutTask is called (new Question) or is over, when the Dialog
		 * with the Users Results is shown.
		 */
		@Override
		protected void onPostExecute(Void result) {

			if (current < 5) {
				new SlideOutTask().execute();
			}

			if (current == 5) {
				showDialog();
			}

			super.onPostExecute(result);
		}

	}

	/*
	 * This Method shows the Result Dialog for the Users-Results The Custom
	 * Dialog can be found in layout/... The String in the Dialog changes
	 * whether the User was good in the Quiz or not. Then the User hits the
	 * "Back" Button the Quiz Activity is finished and the User comes back to
	 * the Start Activity
	 */
	@SuppressLint("InlinedApi")
	private void setupDialog() {
		builder1 = new Dialog(this,
				android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		builder1.setContentView(R.layout.custom_dialog);
		builder1.setCanceledOnTouchOutside(false);
		TextView title = (TextView) builder1.findViewById(R.id.more_info_name);
		if (count == 5) {
			title.setText(R.string.perfect);
		} else {
			if (count > 2) {
				title.setText(R.string.grats);
			} else {
				title.setText(R.string.try_again);
			}
		}
		title.setTextAppearance(this, R.style.more_info_title);
		TextView text = (TextView) builder1.findViewById(R.id.text_more_info);
		text.setText(rightAnswers);
		text.setTextAppearance(this, R.style.interview_more_info_text);
		builder1.setCancelable(true);
		Button dialogButton = (Button) builder1
				.findViewById(R.id.decline_button);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				builder1.dismiss();
				activity.finish();
			}
		});
	}

	// Enables all Answer Buttons
	private void enableButtons() {
		for (int i = 0; i < 3; i++) {
			answerButtons[i].setEnabled(true);
		}
	}

	// Disables all Anwer Buttons
	private void disableButtons() {
		for (int i = 0; i < 3; i++) {
			answerButtons[i].setEnabled(false);
		}

	}

	// Resets the Finish Timer
	private void resetStartFinishTimer() {
		quizFinishTimer.resetTimer();
		quizFinishTimer.startTimer();
	}

	// Setups the MediPlayer Sound Files
	private void setupMediaPlayer() {
		questionSounds = new int[5];
		questionSounds[0] = R.raw.quizfrage1;
		questionSounds[1] = R.raw.quizfrage2;
		questionSounds[2] = R.raw.quizfrage3;
		questionSounds[3] = R.raw.quizfrage4;
		questionSounds[4] = R.raw.quizfrage5;
		question = MediaPlayer.create(this, questionSounds[current]);

	}

	// Setups the CheckBoxes to let the User see his/her Progress
	private void setupCheckboxes() {
		checkOne = (View) findViewById(R.id.checkbox_1);
		checkTwo = (View) findViewById(R.id.checkbox_2);
		checkThree = (View) findViewById(R.id.checkbox_3);
		checkFour = (View) findViewById(R.id.checkbox_4);
		checkFive = (View) findViewById(R.id.checkbox_5);

		checkboxes = new View[] { checkOne, checkTwo, checkThree, checkFour,
				checkFive };
	}

	// Slide Out Animation (Slides out the Question-Layout)
	private void slideOutAnim() {
		slideOut = AnimationUtils.loadAnimation(this, R.anim.anim_slide_out);

		animContent.startAnimation(slideOut);
		animContent.setVisibility(View.INVISIBLE);
	}

	// Slide In Animation (Slides in the Question Layout)
	private void slideInAnim() {
		animContent.setVisibility(View.VISIBLE);
		slideIn = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in);
		animContent.startAnimation(slideIn);
	}

	/*
	 * Changes the content of the Question Layout with new Question and new
	 * Answer Buttons. Also the TextView with the Users progress is changed
	 */
	private void changeContent() {
		questionView.setText(questions.get(current).getQuestion());
		for (int i = 0; i < 3; i++) {
			answerButtons[i]
					.setText(questions.get(current).getCurrentAnswer(i));
			answerButtons[i]
					.setBackgroundResource(R.drawable.quiz_button_shape);

			enableButtons();
			question.start();
		}
		currentQuestion = current + 1;
		statusView.setText("Frage " + currentQuestion + " von 5");

	}

	// Shows the Dialog with the Users Result
	private void showDialog() {
		rightAnswers = "Sie haben " + count
				+ " von 5 Fragen richtig beantwortet!";
		setupDialog();
		builder1.show();

	}

	/*
	 * This Task is suppose to manage the Slide Out Task onPreExecute: The
	 * SlideOutAnimation is started doInBackGround: The Thread sleeps for a
	 * while onPostExecute: The Method changeContent() is called and the
	 * SlideInTask is executed
	 */
	class SlideOutTask extends AsyncTask<Void, Void, Void> {
		public SlideOutTask() {
			super();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			slideOutAnim();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(slideOut.getDuration());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			changeContent();
			new SlideInTask().execute();
		}

	}

	/*
	 * This Task is for the Animation to Slide the Question-Layout back in. in
	 * doInBackGround the Thread sleeps again for a while and in onPostExecute
	 * the Animation (slideInAnim) is called.
	 */
	class SlideInTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(slideOut.getDuration());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			slideInAnim();
		}

	}
}
