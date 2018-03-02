package android.projekt.philipp;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;

/**
 * This class provides a Timer, that will finish the evoking activity after a
 * delay, when the startTimer method is called
 */
public class FinishActivityTimer {
	Activity activity;
	private long time;

	private static final long DEFAULT_TIME = 60000;

	private Timer myTimer;
	private TimerTask myTimerTask;

	/*
	 * use this constructor, if you want to set a delay different from the
	 * default value
	 */
	public FinishActivityTimer(Activity activity, long time) {
		this.activity = activity;
		this.time = time;
		myTimerTask = new MyTimerTask();
		myTimer = new Timer();
	}

	/*
	 * use this constructor, if you want to use the default delay value
	 */
	public FinishActivityTimer(Activity activity) {
		this(activity, DEFAULT_TIME);
	}

	public void startTimer() {
		myTimer.schedule(myTimerTask, time);
	}

	public void resetTimer() {
		myTimer.cancel();
		myTimer = null;
		myTimerTask = new MyTimerTask();
		myTimer = new Timer();
	}

	/*
	 * starts the timer adding an additional delay to the time set by the
	 * constructor
	 */
	public void strtTimerWithDelay(int duration) {
		myTimer.schedule(myTimerTask, time + duration);
	}

	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			activity.finish();
		}
	}
}
