package android.projekt.johannes;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.projekt.philipp.MapActivity;
import android.projekt.thiemo.QuizActivity;
import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This Class is the "START" for the Application
 * "Sprache&Dialekt der Oberpfalz". In this Activity there are three Buttons
 * which are animated and connected to a ClickListener. If the User clicks a
 * Button the other Activities start.
 * 
 * NOTICE: IN THE WHOLE PROJECT THE "HARD - BUTTONS" ARE NOT USEABLE (those are
 * covered) ALSO THIS APPLICATION IS ONLY MADE FOR ONE SPECIAL TABLET BECAUSE
 * THE APP IS ONLY USED ON THIS THE APPLICATION COMES TO USE IN SEVERAL MUSEUM
 * IN GERMANY (OBERPFALZ)
 * 
 * @author Aigner Johannes (Universität Regensburg:
 *         Medieninformatik/Informationswissenschaft)
 * 
 */
public class StartActivity extends Activity implements OnClickListener {

	// CONSTANT FOR THE TIME OF THE ANIMATION
	private static final int DELAY = 5000;

	// INSTANCE VARIABLES FOR BETTER DECOMPOSITION AND HANDLING CODE
	private RelativeLayout container;

	private Button startQuiz;
	private Button startMap;
	private Button startInterview;

	private boolean onResume;

	private MyTimerTask myTimerTask;
	private Timer myTimer;

	private Animation animRotate;
	private Toast toast;

	/*
	 * The onCreate Method setups the UI for the StartActivity, the Animation,
	 * the "Help"-Toast and plays the Animation
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupUI();
		setupAnimation();
		setupToast();
		playAnimation();
	}

	/*
	 * This Method setups the UI Components and connects them to the
	 * ClickListener There are three Buttons and the whole Layout that are
	 * connected to the Listener
	 */
	private void setupUI() {
		setContentView(R.layout.activity_start);
		setupComponents();
		connectToClickListener();
	}

	/*
	 * This Method setups the "Map", "Quiz" and "Interview" Buttons and the
	 * whole RelativeLayout "container"
	 */
	private void setupComponents() {
		startMap = (Button) findViewById(R.id.start_map_button);
		startQuiz = (Button) findViewById(R.id.start_quiz_button);
		startInterview = (Button) findViewById(R.id.start_interview_button);
		container = (RelativeLayout) findViewById(R.id.start_buttons_container);
	}

	/*
	 * This Method connects the Buttons to the OnClickListener (Activity) and
	 * the RelativeLayout to a separate ClickListener (Inner-Class) only to show
	 * the toast if the Layout is clicked.
	 */
	private void connectToClickListener() {
		startMap.setOnClickListener(this);
		startQuiz.setOnClickListener(this);
		startInterview.setOnClickListener(this);

		// Show the Help-Toast if the whole Layout is clicked
		container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				toast.show();
			}
		});
	}

	/*
	 * This Method setups the Animation for the Buttons to "rotate"
	 * (R.anim.anim_rotate) and the Timer, myTimerTask to get the Animation
	 * every X seconds. The Runtime Variable onResume is set to false (used to
	 * stop the Animation if another Activity starts)
	 */
	private void setupAnimation() {
		animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		myTimer = new Timer();
		myTimerTask = new MyTimerTask();
		onResume = false;
	}

	/*
	 * This Method setups the Custom Toast (Layout: R.layout.toast) with two
	 * Images on the left and right side of the Toast and a TextView between
	 * those Images. The Toast has a transparent background and is set
	 * (.setGravity) to the Bottom. Duration is set to Long
	 */
	private void setupToast() {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,
				(ViewGroup) findViewById(R.id.toast_layout));

		ImageView imageOne = (ImageView) layout.findViewById(R.id.image);
		imageOne.setImageResource(R.drawable.info_button);

		ImageView imageTwo = (ImageView) layout.findViewById(R.id.image_two);
		imageTwo.setImageResource(R.drawable.info_button);

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(R.string.info_text);

		// Toast Setup
		toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.BOTTOM, 0, 30);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
	}

	/*
	 * This Method only plays the Animation every "DELAY" (with the myTimerTask)
	 */
	private void playAnimation() {
		myTimer.scheduleAtFixedRate(myTimerTask, 0, DELAY);
	}

	/*
	 * The onClick Method handles the User clicks on the three Buttons to start
	 * other Activities
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_map_button:
			startMapActivity();
			break;
		case R.id.start_quiz_button:
			startQuizActivity();
			break;
		case R.id.start_interview_button:
			startInterviewActivity();
		}
	}


	/*
	 * This Method starts the Interview/Dialog Activity with an Intent
	 */
	private void startInterviewActivity() {
		Intent interviewIntent = new Intent(this, InterviewActivity.class);
		startActivity(interviewIntent);
	}

	/*
	 * This Method starts the Quiz Activity with an Intent
	 */
	private void startQuizActivity() {
		Intent quizIntent = new Intent(this, QuizActivity.class);
		startActivity(quizIntent);
	}

	/*
	 * This Method starts the Map Activity with an Intent
	 */
	private void startMapActivity() {
		Intent mapIntent = new Intent(this, MapActivity.class);
		startActivity(mapIntent);
	}

	/*
	 * This Task with a Runnable is for the Animation and the Toast to show The
	 * Runnable calls the Method startAnimation and toast.show() everyTime its
	 * called.
	 */
	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					startAnimations();
					toast.show();
				}
			});
		}
	}

	/*
	 * This Method starts the Animation for each Button
	 */
	public void startAnimations() {
		startMap.startAnimation(animRotate);
		startQuiz.startAnimation(animRotate);
		startInterview.startAnimation(animRotate);
	}

	/*
	 * This Method is used then the Activity is in Background and no more shown.
	 * It stops the Animation and cancels the Toast to be shown.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		onResume = true;
		myTimer.cancel();
		toast.cancel();

	}

	/*
	 * This Method is called then the Activity is "started" (again) to start the
	 * Animations again.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		if (onResume) {
			setupAnimation();
			playAnimation();
		}
	}

}
