package android.projekt.philipp;

/**
 * class used for debugging. for final release version, set DEBUG false!
 */

public class Log {

	public static final boolean DEBUG = false;

	public static void d(String tag, String msg) {
		if (DEBUG) {
			android.util.Log.d(tag, msg);
		}
	}
}
