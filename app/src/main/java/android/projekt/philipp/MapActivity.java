package android.projekt.philipp;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.projekt.johannes.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Activity that lets the user listen to different audio samples of oberpaelzer
 * dialect. On a map, the user can select, of which place he wants to hear the
 * samples. He can go through the samples, that belong to a selected place, he
 * also can change the place and compare the pronunciation on the different
 * places. there is a help button, that gives the user a short explanation, how
 * to use the activity
 */
public class MapActivity extends Activity {

	private RelativeLayout playSoundButtonsContainer;
	private RelativeLayout slidingWindow;

	private TextView infoBoxTitle, infoBoxText, thanxText;
	private Button[] mapButtons;
	private TextView[] playButtonText;
	private Animation slideOut;
	private Animation slideIn;
	private AnimationDrawable[] mapButtonAnimations;

	private FinishActivityTimer myFinishTimer;
	private InfoBoxMediaPlayer myPlayer;

	private Button backButton;
	private Button helpButton;

	private int lastChosen = 0;
	private int lastPlace = 0;
	private int lastPlaceId = 0;
	private int lastTitle = 0;

	private boolean justStarted = true;
	private boolean helpMode = true;
	private boolean useOpener = false; // flag to set whether opener sound is
										// used or not

	private int buttonAnimLoopCounter; // necessary for the run method inside
										// the anonymous inner Runnable class

	private static final int TIR = 0;
	private static final int PFAT = 1;
	private static final int PRESS = 2;
	private static final int NEUM = 3;
	private static final int WALDM = 4;
	private static final int KALLM = 5;
	private static final int NIED = 6;

	private static final int SAMPLE_1 = 1;
	private static final int SAMPLE_2 = 2;
	private static final int SAMPLE_3 = 3;
	private static final int SAMPLE_4 = 4;
	private static final int SAMPLE_5 = 5;
	private static final int SAMPLE_6 = 6;
	private static final int SAMPLE_7 = 7;
	private static final int SAMPLE_8 = 8;
	private static final int SAMPLE_9 = 9;
	private static final int SAMPLE_10 = 10;

	private static final int TIR_TITLE = R.string.Tirschenreuth;
	private static final int PFAT_TITLE = R.string.Pfatter;
	private static final int PRESS_TITLE = R.string.Pressath;
	private static final int NEUM_TITLE = R.string.Neumarkt;
	private static final int WALDM_TITLE = R.string.Waldmuenchen;
	private static final int KALLM_TITLE = R.string.Kallmuenz;
	private static final int NIED_TITLE = R.string.Niedermurach;
	private static final int INFO_BOX_INSTRUCTIONS_TITLE = R.string.info_box_instructions_title;
	private static final int DEFAULT_TEXT_COLOR = R.color.default_text_color_light;
	private static final int HIGHLIGHTED_TEXT_COLOR = R.color.yellow_text_color;

	private static final int NUM_OF_PLACES = 7;
	private static final int NUM_OF_SAMPLES = 10;

	private static final String TAG = "Test";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setupUI();
		setListeners();
		setupAnimations();
		firstStartMapButtonAnimation();
		setupFinishTimer();

		myPlayer = new InfoBoxMediaPlayer(this);
	}

	/*
	 * setup the FinishTimer with the default time
	 */
	private void setupFinishTimer() {
		myFinishTimer = new FinishActivityTimer(this);
		myFinishTimer.startTimer();
	}

	/*
	 * setup the animations for slide in and slide out the info box
	 */
	private void setupAnimations() {
		slideOut = AnimationUtils.loadAnimation(this, R.anim.anim_slide_out);
		slideIn = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in);
	}

	/*
	 * reset and start the FinishTimer
	 */
	private void resetStartFinishTimer() {
		myFinishTimer.resetTimer();
		myFinishTimer.startTimer();
	}

	private void setupUI() {
		setContentView(R.layout.activity_map);
		setupButtons();
		setupTextViews();
		setupLayouts();
	}

	private void setupLayouts() {
		playSoundButtonsContainer = (RelativeLayout) findViewById(R.id.play_button_container);
		slidingWindow = (RelativeLayout) findViewById(R.id.info_box_container);
	}

	private void setupTextViews() {
		playButtonText = new TextView[NUM_OF_SAMPLES];

		playButtonText[0] = (TextView) findViewById(R.id.info_box_playsound_button_text1);
		playButtonText[1] = (TextView) findViewById(R.id.info_box_playsound_button_text2);
		playButtonText[2] = (TextView) findViewById(R.id.info_box_playsound_button_text3);
		playButtonText[3] = (TextView) findViewById(R.id.info_box_playsound_button_text4);
		playButtonText[4] = (TextView) findViewById(R.id.info_box_playsound_button_text5);
		playButtonText[5] = (TextView) findViewById(R.id.info_box_playsound_button_text6);
		playButtonText[6] = (TextView) findViewById(R.id.info_box_playsound_button_text7);
		playButtonText[7] = (TextView) findViewById(R.id.info_box_playsound_button_text8);
		playButtonText[8] = (TextView) findViewById(R.id.info_box_playsound_button_text9);
		playButtonText[9] = (TextView) findViewById(R.id.info_box_playsound_button_text10);

		infoBoxTitle = (TextView) findViewById(R.id.titel_view);
		infoBoxText = (TextView) findViewById(R.id.box_text_content);
		thanxText = (TextView) findViewById(R.id.thanx_text_view);

	}

	private void setupButtons() {
		mapButtons = new Button[NUM_OF_PLACES];

		mapButtons[KALLM] = (Button) findViewById(R.id.button_kallm);
		mapButtons[NEUM] = (Button) findViewById(R.id.button_neumar);
		mapButtons[NIED] = (Button) findViewById(R.id.button_niedermur);
		mapButtons[PFAT] = (Button) findViewById(R.id.button_pfat);
		mapButtons[PRESS] = (Button) findViewById(R.id.button_pressath);
		mapButtons[TIR] = (Button) findViewById(R.id.button_tir);
		mapButtons[WALDM] = (Button) findViewById(R.id.button_waldm);

		backButton = (Button) findViewById(R.id.map_activity_back_button);
		helpButton = (Button) findViewById(R.id.map_help_button);
	}

	/*
	 * only the backButton and the helpButton have registered an
	 * OnClickListener. The MapButtons and PlaySoudButtons have methods
	 * registered in their xml, that are called on click.
	 */

	private void setListeners() {
		/*
		 * if in help mode, return to last info box state, else finish activity
		 */
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!helpMode || (helpMode && justStarted))
					returnToStartActivity();
				else if (!justStarted)
					returnToLastInfoBoxState();
			}
		});

		helpButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				helpMode = true;
				resetStartFinishTimer();
				chgeInfoBoxDisplButtonsToInstr();
			}
		});

	}

	/*
	 * called from helpMode, to return to last info Box state
	 */
	private void returnToLastInfoBoxState() {
		chgeInfoBoxDisplInstrToButtons();
		infoBoxTitle.setText(lastTitle);
	}

	/*
	 * first time, button animation is started, it must be added to the queue,
	 * this is done by the .post(Runnable)
	 */
	private void firstStartMapButtonAnimation() {
		mapButtonAnimations = new AnimationDrawable[NUM_OF_PLACES];
		buttonAnimLoopCounter = 0;
		for (int i = 0; i < NUM_OF_PLACES; i++) {

			mapButtons[i]
					.setBackgroundResource(R.drawable.map_button_animation_list);
			mapButtonAnimations[i] = (AnimationDrawable) mapButtons[i]
					.getBackground();
			mapButtons[i].post(new Runnable() {
				@Override
				public void run() {
					mapButtonAnimations[buttonAnimLoopCounter].start();
					buttonAnimLoopCounter++;// is necessary, because it is not
											// possible, to use local variable i
											// in anonymous inner class
				}
			});
		}
	}

	/*
	 * method that first stops and then starts the frame animation on the
	 * mapButtons. first stops for synchronizing the animation after a button
	 * was clicked
	 */
	private void startMapButtonAnimation() {
		for (int i = 0; i < NUM_OF_PLACES; i++) {
			mapButtons[i]
					.setBackgroundResource(R.drawable.map_button_animation_list);
			mapButtonAnimations[i] = (AnimationDrawable) mapButtons[i]
					.getBackground();
			mapButtonAnimations[i].stop();
			mapButtonAnimations[i].start();
		}
	}

	/*
	 * called when any of the map buttons is clicked
	 */
	public void onMapButtonClick(View v) {
		Log.d(TAG, "es wurde geklickt");
		startMapButtonAnimation();
		highlightClickedButton(v.getId());

		new SlideOutTask(v).execute(); // SlideInTask is created and executed
										// from within SlideOutTask
	}

	/*
	 * This method has to be separate from the setInfoboxContent method, because
	 * that one is executed in AsyncTask and only after slideOut Animation,
	 * highlight the button should be executed instantly after pressing button
	 */
	private void highlightClickedButton(int id) {
		switch (id) {
		case R.id.button_kallm:
			mapButtons[KALLM]
					.setBackgroundResource(R.drawable.map_button_pressed);
			break;
		case R.id.button_neumar:
			mapButtons[NEUM]
					.setBackgroundResource(R.drawable.map_button_pressed);
			break;
		case R.id.button_niedermur:
			mapButtons[NIED]
					.setBackgroundResource(R.drawable.map_button_pressed);
			break;
		case R.id.button_pfat:
			mapButtons[PFAT]
					.setBackgroundResource(R.drawable.map_button_pressed);
			break;
		case R.id.button_pressath:
			mapButtons[PRESS]
					.setBackgroundResource(R.drawable.map_button_pressed);
			break;
		case R.id.button_waldm:
			mapButtons[WALDM]
					.setBackgroundResource(R.drawable.map_button_pressed);
			break;
		case R.id.button_tir:
			mapButtons[TIR]
					.setBackgroundResource(R.drawable.map_button_pressed);
			break;

		default:
			break;
		}
	}

	/*
	 * changes the content of the info box from instructions to play sound
	 * buttons
	 */
	private void chgeInfoBoxDisplInstrToButtons() {
		helpMode = false;
		thanxText.setVisibility(View.GONE);
		infoBoxText.setVisibility(View.GONE);
		playSoundButtonsContainer.setVisibility(View.VISIBLE);
	}

	/*
	 * changes the content of the info box from play sound buttons to
	 * instructions
	 */
	private void chgeInfoBoxDisplButtonsToInstr() {
		helpMode = true;
		thanxText.setVisibility(View.VISIBLE);
		infoBoxText.setVisibility(View.VISIBLE);
		infoBoxTitle.setText(INFO_BOX_INSTRUCTIONS_TITLE);
		playSoundButtonsContainer.setVisibility(View.GONE);
	}

	/*
	 * called, when any of the sound buttons is clicked
	 */

	public void onPlaySoundButtonClick(View v) {
		Log.d(TAG, "es wurde Soundbutton geklickt");
		justStarted = false;

		switch (v.getId()) {

		case R.id.info_box_playsound_button1:
		case R.id.info_box_playsound_button_text1:
			lastChosen = SAMPLE_1;
			performPlayback();
			break;

		case R.id.info_box_playsound_button2:
		case R.id.info_box_playsound_button_text2:
			lastChosen = SAMPLE_2;
			performPlayback();
			break;

		case R.id.info_box_playsound_button3:
		case R.id.info_box_playsound_button_text3:
			lastChosen = SAMPLE_3;
			performPlayback();
			break;

		case R.id.info_box_playsound_button4:
		case R.id.info_box_playsound_button_text4:
			lastChosen = SAMPLE_4;
			performPlayback();
			break;

		case R.id.info_box_playsound_button5:
		case R.id.info_box_playsound_button_text5:
			lastChosen = SAMPLE_5;
			performPlayback();
			break;

		case R.id.info_box_playsound_button6:
		case R.id.info_box_playsound_button_text6:
			lastChosen = SAMPLE_6;
			performPlayback();
			break;

		case R.id.info_box_playsound_button7:
		case R.id.info_box_playsound_button_text7:
			lastChosen = SAMPLE_7;
			performPlayback();
			break;

		case R.id.info_box_playsound_button8:
		case R.id.info_box_playsound_button_text8:
			lastChosen = SAMPLE_8;
			performPlayback();
			break;

		case R.id.info_box_playsound_button9:
		case R.id.info_box_playsound_button_text9:
			lastChosen = SAMPLE_9;
			performPlayback();
			break;

		case R.id.info_box_playsound_button10:
		case R.id.info_box_playsound_button_text10:
			lastChosen = SAMPLE_10;
			performPlayback();
			break;

		default:
			break;
		}
	}

	private void performPlayback() {
		highlightItem();
		myPlayer.start(lastChosen);
		startTimerWithDelay();
	}

	/*
	 * start the FinishTimer adding the duration of the actual audio track
	 */
	private void startTimerWithDelay() {
		myFinishTimer.resetTimer();
		myFinishTimer.strtTimerWithDelay(myPlayer.getDuration());
	}

	/*
	 * highlights the last chosen item in the info box
	 */
	private void highlightItem() {
		// first set for all items the default text color
		for (int i = 0; i < NUM_OF_SAMPLES; i++) {
			playButtonText[i].setTextColor(getResources().getColor(
					DEFAULT_TEXT_COLOR));
		}
		// then set the highlighted text color
		playButtonText[lastChosen - 1].setTextColor(getResources().getColor(
				HIGHLIGHTED_TEXT_COLOR));
	}

	/*
	 * is called in the SlideOutTask onPostExecute method. changes the content
	 * of the info box depending on which mapButton was clicked
	 */
	private void setInfoBoxContent(View v) {

		switch (v.getId()) {
		case R.id.button_kallm:
			performChanges(KALLM_TITLE, KALLM);
			break;
		case R.id.button_neumar:
			performChanges(NEUM_TITLE, NEUM);
			break;
		case R.id.button_niedermur:
			performChanges(NIED_TITLE, NIED);
			break;
		case R.id.button_pfat:
			performChanges(PFAT_TITLE, PFAT);
			break;
		case R.id.button_pressath:
			performChanges(PRESS_TITLE, PRESS);
			break;
		case R.id.button_waldm:
			performChanges(WALDM_TITLE, WALDM);
			break;
		case R.id.button_tir:
			performChanges(TIR_TITLE, TIR);
			break;

		default:
			break;
		}
	}

	/*
	 * sets the title of the info box, sets the clicked place as lastPlace. sets
	 * up the InfoBoxPlayer, so that its array holds the references to the sound
	 * files of the place and starts the playback of the lastChosen. starts the
	 * FinishActivityTimer with delay
	 */
	private void performChanges(int title, int place) {
		lastTitle = title;
		infoBoxTitle.setText(lastTitle);
		lastPlace = place;
		if (useOpener) {
			setupAndPlayMediaPlayer();
		} else {
			if (lastChosen != 0) {
				setupAndPlayMediaPlayer();
			} else {
				resetStartFinishTimer();
			}
		}
	}

	private void setupAndPlayMediaPlayer() {
		myPlayer.setupInfoBoxPlayer(lastPlace, lastChosen);
		myPlayer.start();
		startTimerWithDelay();
	}

	/*
	 * stops the player and finishes the activity. In its stop method, the
	 * player is also released and nulled
	 */
	private void returnToStartActivity() {
		myPlayer.stop();
		finish();
	}

	private void slideOutAnim() {
		slidingWindow.startAnimation(slideOut);
		slidingWindow.setVisibility(View.INVISIBLE);
	}

	private void slideInAnim() {
		slidingWindow.setVisibility(View.VISIBLE);
		slidingWindow.startAnimation(slideIn);
	}

	@Override
	protected void onStart() {
		myPlayer = new InfoBoxMediaPlayer(this);
		myPlayer.setupInfoBoxPlayer(lastPlace, lastChosen);
		super.onStart();
	}

	@Override
	protected void onStop() {
		myPlayer.stop();
		myPlayer = null;
		super.onStop();
	}

	/*
	 * AsyncTask for the SlideOutAnimation. As soon as the info box slid out,
	 * the content is being changed. The idea to use AsyncTask came from
	 * Johannes Aigner.
	 */
	class SlideOutTask extends AsyncTask<Void, Void, Void> {

		private View v;

		public SlideOutTask(View v) {
			super();
			this.v = v;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (v.getId() != lastPlaceId) // check, if the button has been
											// clicked just before
				slideOutAnim();
		}

		/*
		 * sleep during the animation
		 */
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(slideOut.getDuration());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		/*
		 * onPostExecute change the content of the info box and start
		 * SlideInTask
		 */
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (helpMode) {
				chgeInfoBoxDisplInstrToButtons();
				helpMode = false;
			}
			setInfoBoxContent(v);
			justStarted = false;

			new SlideInTask(v).execute();

		}

	}

	/*
	 * AsyncTask for SlideInAnimation. The idea to use AsyncTask came from
	 * Johannes Aigner.
	 */
	class SlideInTask extends AsyncTask<Void, Void, Void> {

		private View v;

		public SlideInTask(View v) {
			super();
			this.v = v;
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
			if (v.getId() != lastPlaceId)
				slideInAnim();
			lastPlaceId = v.getId(); // variable used to avoid animation playing
										// in case same button is pressed
										// repeatedly
		}
	}
}
