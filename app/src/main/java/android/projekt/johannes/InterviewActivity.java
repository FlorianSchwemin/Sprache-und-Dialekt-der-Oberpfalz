package android.projekt.johannes;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.projekt.philipp.FinishActivityTimer;
import android.projekt.philipp.Log;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * This Class is responsible for the Interview/Dialog Activity, it handles with
 * ClickListener and SeekBarChangeListener, the main concept is that the user
 * can interact with Sound-Files and play them with Buttons and SeekBars.
 * Layout: activity_interview.xml
 * 
 * @author Aigner Johannes (Universität Regensburg: Medieninformatik/Informationswissenschaft)
 */
public class InterviewActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {

	// CONSTANTS FOR BETTER DECOMPOSITION
	private static final int NUMBER_OF_PERSONS = 4;

	private static final int NO_PERSON = -1;
	private static final int PERSON_ONE = 0;
	private static final int PERSON_TWO = 1;
	private static final int PERSON_THREE = 2;
	private static final int PERSON_FOUR_ONE = 3;
	private static final int PERSON_FOUR_TWO = 4;

	private static final int PROGRESS_ZERO = 0;
	private static final long DELAY = 50;

	private static final float VOLUME_ZERO = 0.0F;
	private static final float VOLUME_HIGH = 1.0F;

	// INSTANCE VARIABLES FOR BETTER DECOMPOSITION AND HANDLING CODE
	private Button[] playButtons;
	private Button[] moreInfoButtons;

	private SeekBar[] seekBars;
	private SeekBar currentSeekBar;

	private int[] interviewSoundResources;
	private int[] interviewTextResources;
	private int[] interviewPersonResources;

	private MediaPlayer interview;
	private int duration;
	private boolean running;

	private int currentPerson;

	private FinishActivityTimer finishTimer;

	//LOG STRING - TO LOG GO TO android.projekt.philipp : Log.java / set DEBUG to true
	private static final String TAG = "Test Interview";


	/*
	 * Method used in onCreate: setupInterviewResources() - gets Resources for
	 * the Activity setupUI() - setups the Buttons etc. for the Activity
	 * setupSeekBars() - setups the SeekBars for the Activity
	 * connectToClickListener() - connects the Buttons for playing and showing
	 * more Info to a Listener setupFinishTimer() - setups the FinishTimer
	 * (FinishActivityTimer.java) for this Activity
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupInterviewResources();
		setupUI();
		setupSeekBars();
		connectToClickListener();
		setupFinishTimer();
	}

	/*
	 * Setup Sound, Text and Name Resources to mainly interact with MoreInfo
	 * Buttons Setup Running Parameters (see Instance Variables like "duration",
	 * "running")
	 */
	private void setupInterviewResources() {
		setupSoundResources();
		setupTextResources();
		setupNameResources();
		setupRunningParameters();
	}

	/*
	 * This Methods sets the duration to PROGRESS_ZERO ("0"), currentPerson to
	 * NO_PERSON ("-1") and running to true. The variables change in the Runtime
	 * to interact with User-Clicks
	 */
	private void setupRunningParameters() {
		duration = PROGRESS_ZERO;
		currentPerson = NO_PERSON;
		running = true;
	}

	/*
	 * This Method gets the Persons Names from the string.xml. Mainly used in
	 * the MoreInfo Dialog
	 */
	private void setupNameResources() {
		int personOneName = R.string.person_one;
		int personTwoName = R.string.person_two;
		int personThreeName = R.string.person_three;
		int personFourName = R.string.person_four;

		interviewPersonResources = new int[] { personOneName, personTwoName,
				personThreeName, personFourName };
	}

	/*
	 * This Method gets the Texts for each person from the string.xml. Mainly
	 * used in the MoreInfo Dialog to get more Information to the persons
	 */
	private void setupTextResources() {
		int textOne = R.string.interviewed_person_one;
		int textTwo = R.string.interviewed_person_two;
		int textThree = R.string.interviewed_person_three;
		int textFour = R.string.interviewed_person_four;

		interviewTextResources = new int[] { textOne, textTwo, textThree,
				textFour };
	}

	/*
	 * This Method gets the SoundResources for each Interview from res/raw/...
	 * Mainly used in the PlayButton onClick
	 */
	private void setupSoundResources() {
		int personOneSound = R.raw.zehetner_interview;
		int personTwoSound = R.raw.frances_interview;
		int personThreeSound = R.raw.boris_interview;
		int personFourSound = R.raw.laurer_interview_one;
		int personFourTwoSound = R.raw.laurer_interview_two;

		interviewSoundResources = new int[] { personOneSound, personTwoSound,
				personThreeSound, personFourSound, personFourTwoSound };
	}

	/*
	 * SETUP UI: gets the layout for this activity setups the PlayButtons (5),
	 * MoreInfoButtons(4) and the BackToStartButton (1)
	 */
	private void setupUI() {
		setContentView(R.layout.activity_interviews);
		setupPlayButtons();
		setupMoreInfoButtons();
		setupBackToStartButton();
	}

	/*
	 * Setups the PlayButtons (5) and "saved" in an Array for better code There
	 * are 5 PlayButtons because one Person ("Toni Laurer") has two
	 * Interview-Sound-Files
	 */
	private void setupPlayButtons() {
		Button personOne = (Button) findViewById(R.id.interview_button_one);
		Button personTwo = (Button) findViewById(R.id.interview_button_two);
		Button personThree = (Button) findViewById(R.id.interview_button_three);
		Button personFourOne = (Button) findViewById(R.id.interview_button_four_one);
		Button personFourTwo = (Button) findViewById(R.id.interview_button_four_two);

		playButtons = new Button[] { personOne, personTwo, personThree,
				personFourOne, personFourTwo };
	}

	/*
	 * Setups the MoreInfoButtons (4) and "saved" in an Array for better code
	 */
	private void setupMoreInfoButtons() {
		Button personOne = (Button) findViewById(R.id.more_info_button_one);
		Button personTwo = (Button) findViewById(R.id.more_info_button_two);
		Button personThree = (Button) findViewById(R.id.more_info_button_three);
		Button personFour = (Button) findViewById(R.id.more_info_button_four);

		moreInfoButtons = new Button[] { personOne, personTwo, personThree,
				personFour };
	}

	/*
	 * This Method setups the BackToStartButton and handles the click in an
	 * inner-class If the User interacts with this Button the Activity is
	 * finished and the User gets back to the StartActivity NOTICE: THE HARD
	 * BUTTONS (BACK-BUTTON FROM THE TABLET) IS COVERED AND CANNOT BE USED BY
	 * THE USER!
	 */
	private void setupBackToStartButton() {
		Button backToStart = (Button) findViewById(R.id.back_to_start);
		backToStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// If a Sound is played this releases the MediaPlayer
				if (interview != null) {
					running = false;
					interview.release();
					Log.d(TAG, "Interview Activity closed manually");
				}
				InterviewActivity.this.finish();
			}
		});
	}

	/*
	 * This Method setups the four Seekbars for the Sounds and connects them to
	 * an SeekBarChangeListener and sets them disabled. The User can only
	 * interact with the Seekbar when a Sound is played - if no MediaPlayer is
	 * playing the SeekBar is disabled.
	 */
	private void setupSeekBars() {
		SeekBar personOne = (SeekBar) findViewById(R.id.progress_bar_one);
		SeekBar personTwo = (SeekBar) findViewById(R.id.progress_bar_two);
		SeekBar personThree = (SeekBar) findViewById(R.id.progress_bar_three);
		SeekBar personFour = (SeekBar) findViewById(R.id.progress_bar_four);

		seekBars = new SeekBar[] { personOne, personTwo, personThree,
				personFour };

		for (int i = 0; i < NUMBER_OF_PERSONS; i++) {
			seekBars[i].setOnSeekBarChangeListener(this);
			seekBars[i].setEnabled(false);
		}
	}

	/*
	 * This Method connects the Buttons (Play and More Info) to the
	 * ClickListener. Because there are more PlayButtons (5) than
	 * MoreInfoButtons (4) the for-loop isn't perfect; the last playButton is
	 * connected seperately
	 */
	private void connectToClickListener() {
		for (int i = 0; i < NUMBER_OF_PERSONS; i++) {
			playButtons[i].setOnClickListener(this);
			moreInfoButtons[i].setOnClickListener(this);
		}
		playButtons[PERSON_FOUR_TWO].setOnClickListener(this);
	}

	/*
	 * Setups FinishTimer from FinishActivityTimer.java to reach that the
	 * Activity is shut down after several of minutes This is espacially usefull
	 * because the Application is mainly used in a "Museum". Each User starts
	 * with the StartActivity! More Information in FinishActivityTimer.java
	 */
	private void setupFinishTimer() {
		finishTimer = new FinishActivityTimer(this);
		finishTimer.startTimer();
	}

	/*
	 * This Method is to let the User interact and "scroll" the MediaPlayer with
	 * the SeekBars, if the User touches the SeekBar and "scrolls" is the
	 * MediaPlayer is also "scrolled" to the progress the User has chosen.
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			interview.seekTo(progress);
			seekBar.setProgress(progress);
		}
	}

	/*
	 * This Method is called then the User starts to "touch" the SeekBar This is
	 * only to avoid the noise then the MediaPlayer is in "Fast Forward mode".
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		interview.setVolume(VOLUME_ZERO, VOLUME_ZERO);
		Log.d(TAG, "User begins to scroll seekBar");
	}

	/*
	 * This Method is called then the User finished scrolling with the SeekBar
	 * The Volume is again set to "Standard"
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		interview.setVolume(VOLUME_HIGH, VOLUME_HIGH);
		Log.d(TAG, "User stops to scroll seekBar");
	}

	/*
	 * This Method interacts with UserClicks It is splitted in two other Methods
	 * which get the View, because there are two different Interaction available
	 * for the User (Play and More Info)
	 */
	@Override
	public void onClick(View v) {
		interactWithPlayButtons(v);
		interactWithMoreInfoButtons(v);
	}

	/*
	 * This Method uses Switch Case to interact with the 5 Play Buttons After
	 * jumping in one "case" the Methods updateSeekBar(int person) and
	 * changeState(int person) is called
	 */
	private void interactWithPlayButtons(View v) {
		resetStartFinishTimer();
		switch (v.getId()) {
		case R.id.interview_button_one:
			updateSeekBar(PERSON_ONE);
			changeState(PERSON_ONE);
			break;
		case R.id.interview_button_two:
			updateSeekBar(PERSON_TWO);
			changeState(PERSON_TWO);
			break;
		case R.id.interview_button_three:
			updateSeekBar(PERSON_THREE);
			changeState(PERSON_THREE);
			break;
		case R.id.interview_button_four_one:
			updateSeekBar(PERSON_FOUR_ONE);
			changeState(PERSON_FOUR_ONE);
			break;
		case R.id.interview_button_four_two:
			updateSeekBar(PERSON_FOUR_TWO);
			changeState(PERSON_FOUR_TWO);
			break;

		}
	}

	/*
	 * This Method handles with an MediaPlayer; if a Play Button is Clicked the
	 * MediaPlayer starts and the Seekbar shows the Progress The MediaPlayer
	 * gets the Resources from the Array interviewSoundResources The Fact that
	 * there are 5 Sounds but only 4 Persons needs that there is an "if-clause"
	 * to fetch the 5th Button clicked The Variable "currentPerson" is to fetch
	 * that the Button is Clicked twice.
	 */
	private void updateSeekBar(int person) {
		prepareSeekBar();
		if (person != currentPerson) {
			interview = MediaPlayer.create(getApplicationContext(),
					interviewSoundResources[person]);
			currentPerson = person;
			if (person == PERSON_FOUR_TWO) {
				person = PERSON_FOUR_ONE;
			}
			duration = interview.getDuration();
			changeSeekBar(person);
			interview.start();

		} else {
			currentPerson = NO_PERSON;
		}

	}

	/*
	 * This Method setups and changes the Seekbar for the MediaPlayer (more in
	 * onEverySecond)
	 */
	private void changeSeekBar(int person) {
		currentSeekBar = seekBars[person];
		seekBars[person].setEnabled(true);
		seekBars[person].setMax(duration);
		seekBars[person].postDelayed(onEverySecond, DELAY);
	}

	/*
	 * This Method is to prepare the SeekBar and to set the SeekBars back to
	 * zero Progress and to disabled.
	 */
	private void prepareSeekBar() {
		if (interview != null) {
			interview.release();
			for (int i = 0; i < NUMBER_OF_PERSONS; i++) {
				seekBars[i].setProgress(PROGRESS_ZERO);
				seekBars[i].setEnabled(false);
			}
		}
	}

	/*
	 * This Method changes the State of the Play Buttons. If a Button is clicked
	 * the left Image changes and the Text in the Button is changed The Fact
	 * that one Person has a special SoundFile ("Bauvertrag") needs that the
	 * if-clause fetches that fact.
	 */
	private void changeState(int person) {
		String stop = getString(R.string.stop_button_interview);
		String play = getString(R.string.play_button_interview);
		// Fetch the "special" Person
		if (person == PERSON_FOUR_TWO) {
			play = getString(R.string.play_button_bauvertrag);
		}
		// PLAY
		if (!playButtons[person].getText().toString().equals(stop)) {
			prepareButtons();
			changeToPlayState(person, stop);
			Log.d(TAG, "Interview Button pressed: Audio file plays");
		} else {
			// STOP
			changeToStopState(person, play);
			Log.d(TAG, "Interview Button pressed: Audio file stoped");
		}
	}

	/*
	 * This Method changes the State of the Button to the Stop State, with
	 * "Interview" in the Button and a Play Image on the left Side in the Button
	 * The "Design" of the Button is changed to StopState
	 * Also the RuntimeVariable running is changed to "FALSE"
	 * NOTICE: ITS A BIT TRICKY BUT THIS METHOD IS TO STOP THE MEDIAPLAYER AND GO BACK TO STANDARD
	 */
	private void changeToStopState(int person, String play) {
		if (person == PERSON_FOUR_TWO) {
			seekBars[PERSON_FOUR_ONE].setEnabled(false);
		} else {
			seekBars[person].setEnabled(false);
		}
		playButtons[person].setText(play);
		playButtons[person].setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.av_play, 0, 0, 0);
		playButtons[person]
				.setBackgroundResource(R.drawable.custom_buttons_interview);
		running = false;
	}

	/*
	 * This Method changes the State of the Button to the Play-State, with "Stop" in the Button and 
	 * a Stop Image on the left Side in the Button
	 * The "Design" of the Button is changed to PlayState
	 * Also the RuntimeVariable running is change to "TRUE"
	 * NOTICE: THIS METHOD IS TO "PLAY" THE MEDIAPLAYER 
	 */
	private void changeToPlayState(int person, String stop) {
		playButtons[person].setText(stop);
		if (person == PERSON_FOUR_TWO) {
			seekBars[PERSON_FOUR_ONE].setEnabled(true);
		} else {
			seekBars[person].setEnabled(true);
		}

		playButtons[person]
				.setBackgroundResource(R.drawable.custom_button_interview_playing);
		playButtons[person].setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.stop_audio, 0, 0, 0);
		running = true;
	}

	/*
	 * This Method changes all Buttons to "Standard". Here is also again the 5th Button handled seperately.
	 */
	private void prepareButtons() {
		for (int i = 0; i < NUMBER_OF_PERSONS; i++) {
			playButtons[i]
					.setBackgroundResource(R.drawable.custom_buttons_interview);
			playButtons[i].setText(R.string.play_button_interview);
			playButtons[i].setCompoundDrawablesWithIntrinsicBounds(
					R.drawable.av_play, 0, 0, 0);
		}
		playButtons[PERSON_FOUR_TWO]
				.setBackgroundResource(R.drawable.custom_buttons_interview);
		playButtons[PERSON_FOUR_TWO].setText(R.string.play_button_bauvertrag);
		playButtons[PERSON_FOUR_TWO].setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.av_play, 0, 0, 0);
	}

	
	/*
	 * This Method handles the Interaction with the MoreInfo Buttons
	 * If the Method jumps in one case the Method showMoreInfoDialog(int person) is called.
	 */
	private void interactWithMoreInfoButtons(View v) {
		resetStartFinishTimer();
		switch (v.getId()) {
		case R.id.more_info_button_one:
			showMoreInfoDialog(PERSON_ONE);
			break;
		case R.id.more_info_button_two:
			showMoreInfoDialog(PERSON_TWO);
			break;
		case R.id.more_info_button_three:
			showMoreInfoDialog(PERSON_THREE);
			break;
		case R.id.more_info_button_four:
			showMoreInfoDialog(PERSON_FOUR_ONE);
			break;
		}
	}

	/*
	 * This Method needs another API (min. HONEYCOMB), but its not relevant for our Application.
	 * The Method handles the UserClick with the MoreInfo Buttons.
	 * The Method gets an int (person) and changes then the Strings in the Dialog for each person.
	 * The Custom Layout for the Dialog: "R.layout.custom_dialog");
	 * The Button in the Dialog is to "close" the Dialog again (inner-class); but the user can also close the Dialog with a click 
	 * outside of the Dialog
	 */
	@SuppressLint("InlinedApi")
	private void showMoreInfoDialog(int person) {
		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
		dialog.setContentView(R.layout.custom_dialog);

		// set the custom dialog components - text, image and button
		TextView title = (TextView) dialog.findViewById(R.id.more_info_name);
		title.setText(interviewPersonResources[person]);
		title.setTextAppearance(this, R.style.more_info_title);

		TextView text = (TextView) dialog.findViewById(R.id.text_more_info);
		text.setText(interviewTextResources[person]);
		text.setTextAppearance(this, R.style.interview_more_info_text);

		Button dialogButton = (Button) dialog.findViewById(R.id.decline_button);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	
	/*
	 * This Runnable is suppose to cahnge the SeekBars current Progress every "DELAY" to the MediaPlayers progress
	 * Here comes the Variable running to game and the method must check whether the seekBar is not null and whether the 
	 * MediaPlayer is playing.
	 * If the MediaPlayer is active and playing the Progress is updated
	 * If the MediaPlayer is stopped or finished the Progress is again set to ZERO and the seekBar is again set disabled
	 */
	private Runnable onEverySecond = new Runnable() {
		@Override
		public void run() {
			if (running) {
				if (currentSeekBar != null) {
					currentSeekBar.setProgress(interview.getCurrentPosition());
				}

				if (interview.isPlaying()) {
					resetStartFinishTimer();
					currentSeekBar.postDelayed(onEverySecond, DELAY);
				}
				if (interview.isPlaying() == false) {
					currentSeekBar.setProgress(PROGRESS_ZERO);
					currentSeekBar.setEnabled(false);
					changeState(currentPerson);
					currentPerson = NO_PERSON;
					Log.d(TAG, "Runnabled stopped");
				}
			}

		}
	};

	
	/*
	 * This Method handles the FinishTimer for this Activity
	 * More in FinishActivityTimer.java
	 */
	private void resetStartFinishTimer() {
		finishTimer.resetTimer();
		Log.d(TAG, "Reset Finish Timer");
		finishTimer.startTimer();
		Log.d(TAG, "Start Finish Timer");
	}

	/*
	 * This Method is mainly for the Interaction with the "Hard-Back-Button", which is covered in our field of use.
	 * It only fetches the Case that a User hits the Back Button. 
	 */
	@Override
	protected void onStop() {
		super.onStop();
		if (interview != null) {
			running = false;
			interview.release();
		}
		InterviewActivity.this.finish();
	}

}
