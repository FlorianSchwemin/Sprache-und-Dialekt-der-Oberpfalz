package android.projekt.philipp;

import android.content.Context;
import android.media.MediaPlayer;
import android.projekt.johannes.R;

/**
 * this class is used, to create a MediaPlayer for the MapActivity, that is very
 * simple to control. The class holds an array with the references to 11 sound
 * files, which are the sound files for each place.
 */
public class InfoBoxMediaPlayer {

	private static final int TIR = 0;
	private static final int PFAT = 1;
	private static final int PRESS = 2;
	private static final int NEUM = 3;
	private static final int WALDM = 4;
	private static final int KALLM = 5;
	private static final int NIED = 6;

	private int[] soundFiles;
	private int place;
	private Context context;
	private MediaPlayer player;

	public InfoBoxMediaPlayer(Context context) {

		this.context = context;
	}

	/*
	 * in this method, the MediaPlayer is created and the sound file references
	 * are set
	 */
	public void setupInfoBoxPlayer(int place, int lastChosen) {
		this.place = place;
		setupSoundFiles();
		if (player != null) {
			if (player.isPlaying())
				player.stop();
			player.release();
		}
		player = MediaPlayer.create(context, soundFiles[lastChosen]);
	}

	public void start(int track) {
		if (player.isPlaying())
			player.stop();
		if (player != null)
			player.release();
		player = MediaPlayer.create(context, soundFiles[track]);
		player.start();
	}

	public void start() {
		player.start();
	}

	public void stop() {
		if (player != null) {
			if (player.isPlaying())
				player.stop();
			player.release();
			player = null;
		}
	}

	public int getDuration() {
		return player.getDuration();
	}

	private void setupSoundFiles() {
		if (soundFiles == null)
			soundFiles = new int[11];
		switch (place) {
		case TIR:
			soundFiles[0] = R.raw.tir_opener;
			soundFiles[1] = R.raw.tir1;
			soundFiles[2] = R.raw.tir2;
			soundFiles[3] = R.raw.tir3;
			soundFiles[4] = R.raw.tir4;
			soundFiles[5] = R.raw.tir5;
			soundFiles[6] = R.raw.tir6;
			soundFiles[7] = R.raw.tir7;
			soundFiles[8] = R.raw.tir8;
			soundFiles[9] = R.raw.tir9;
			soundFiles[10] = R.raw.tir10;
			break;

		case PFAT:
			soundFiles[0] = R.raw.pfat_opener;
			soundFiles[1] = R.raw.pfat1;
			soundFiles[2] = R.raw.pfat2;
			soundFiles[3] = R.raw.pfat3;
			soundFiles[4] = R.raw.pfat4;
			soundFiles[5] = R.raw.pfat5;
			soundFiles[6] = R.raw.pfat6;
			soundFiles[7] = R.raw.pfat7;
			soundFiles[8] = R.raw.pfat8;
			soundFiles[9] = R.raw.pfat9;
			soundFiles[10] = R.raw.pfat10;

			break;
		case PRESS:
			soundFiles[0] = R.raw.pres_opener;
			soundFiles[1] = R.raw.pres1;
			soundFiles[2] = R.raw.pres2;
			soundFiles[3] = R.raw.pres3;
			soundFiles[4] = R.raw.pres4;
			soundFiles[5] = R.raw.pres5;
			soundFiles[6] = R.raw.pres6;
			soundFiles[7] = R.raw.pres7;
			soundFiles[8] = R.raw.pres8;
			soundFiles[9] = R.raw.pres9;
			soundFiles[10] = R.raw.pres10;

			break;
		case NEUM:
			soundFiles[0] = R.raw.neum_opener;
			soundFiles[1] = R.raw.neum1;
			soundFiles[2] = R.raw.neum2;
			soundFiles[3] = R.raw.neum3;
			soundFiles[4] = R.raw.neum4;
			soundFiles[5] = R.raw.neum5;
			soundFiles[6] = R.raw.neum6;
			soundFiles[7] = R.raw.neum7;
			soundFiles[8] = R.raw.neum8;
			soundFiles[9] = R.raw.neum9;
			soundFiles[10] = R.raw.neum10;

			break;
		case WALDM:
			soundFiles[0] = R.raw.wald_opener;
			soundFiles[1] = R.raw.wald1;
			soundFiles[2] = R.raw.wald2;
			soundFiles[3] = R.raw.wald3;
			soundFiles[4] = R.raw.wald4;
			soundFiles[5] = R.raw.wald5;
			soundFiles[6] = R.raw.wald6;
			soundFiles[7] = R.raw.wald7;
			soundFiles[8] = R.raw.wald8;
			soundFiles[9] = R.raw.wald9;
			soundFiles[10] = R.raw.wald10;

			break;
		case KALLM:
			soundFiles[0] = R.raw.kall_opener;
			soundFiles[1] = R.raw.kall1;
			soundFiles[2] = R.raw.kall2;
			soundFiles[3] = R.raw.kall3;
			soundFiles[4] = R.raw.kall4;
			soundFiles[5] = R.raw.kall5;
			soundFiles[6] = R.raw.kall6;
			soundFiles[7] = R.raw.kall7;
			soundFiles[8] = R.raw.kall8;
			soundFiles[9] = R.raw.kall9;
			soundFiles[10] = R.raw.kall10;

			break;
		case NIED:
			soundFiles[0] = R.raw.nied_opener;
			soundFiles[1] = R.raw.nied1;
			soundFiles[2] = R.raw.nied2;
			soundFiles[3] = R.raw.nied3;
			soundFiles[4] = R.raw.nied4;
			soundFiles[5] = R.raw.nied5;
			soundFiles[6] = R.raw.nied6;
			soundFiles[7] = R.raw.nied7;
			soundFiles[8] = R.raw.nied8;
			soundFiles[9] = R.raw.nied9;
			soundFiles[10] = R.raw.nied10;

			break;

		default:
			break;
		}

	}

}
