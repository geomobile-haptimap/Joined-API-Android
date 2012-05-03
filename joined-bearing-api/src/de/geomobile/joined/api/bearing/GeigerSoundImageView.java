package de.geomobile.joined.api.bearing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GeigerSoundImageView extends ImageView implements RotatorImage {
	private float rotationAngle = 0;
	private final Context context;
	private boolean hasBeenMoved = false;
	@SuppressWarnings("unused")
	private boolean isGeiger = false;
	private SoundPool soundPool;
	private int soundID;
	private float volume;
	private boolean isActive = true;
	private Thread vibSoundThread;
	private boolean flag = false;

	private int soundPause = Integer.MAX_VALUE;

	public GeigerSoundImageView(Context context, AttributeSet attrs,
			int defStyle, MediaPlayer mp) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	public GeigerSoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public GeigerSoundImageView(Context context) {
		super(context);
		this.context = context;
	}

	public void create() {
		enableSoundPool();
		createThread();
		vibSoundThread.start();
		setButtons();
	}

	private void setButtons() {
		LinearLayout soundButton = (LinearLayout) ((Activity) context)
				.findViewById(R.id.soundButton);
		LinearLayout vibrationButton = (LinearLayout) ((Activity) context)
				.findViewById(R.id.vibrationButton);
		if (flag) {
			soundButton.setBackgroundResource(R.drawable.links_dark);
			vibrationButton.setBackgroundResource(R.drawable.rechts_light);
		} else {
			soundButton.setBackgroundResource(R.drawable.links_light);
			vibrationButton.setBackgroundResource(R.drawable.rechts_dark);
		}
	}

	public void setSoundActive() {
		flag = true;
		setButtons();
	}

	public void setVibratorActive() {
		flag = false;
		setButtons();
	}

	public void stopThread() {
		try {
			isActive = false;
			if (vibSoundThread != null) {
				vibSoundThread.interrupt();
				vibSoundThread.join();
			}
			vibSoundThread = null;
			hasBeenMoved = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.rotate(rotationAngle, getWidth() / 2, getHeight() / 2);
		super.onDraw(canvas);
	}

	public void rotate(float degree) {
		if (degree <= 90 && degree >= -90) {
			soundPause = 580 + (int) (Math
					.cos(degree * Math.PI / 180 + Math.PI) * 500);
		} else {
			soundPause = Integer.MAX_VALUE;
		}
		rotationAngle = degree;
		invalidate();
		hasBeenMoved = true;
		isActive = true;
	}

	public void isGeiger(boolean isGeiger) {
		this.isGeiger = isGeiger;
	}

	public void enableSoundPool() {
		soundPool = new SoundPool(400, AudioManager.STREAM_MUSIC, 0);

		soundID = soundPool.load(context, R.raw.tap_2, 1);
		AudioManager mgr = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		volume = streamVolumeCurrent / streamVolumeMax;
	}

	public void playSound() {
		playSound(1f);
	}

	public void playSound(float lenght) {
		soundPool.play(soundID, volume, volume, 1, 0, lenght);
	}

	public void createThread() {
		vibSoundThread = new Thread() {
			private Vibrator vibrator = (Vibrator) context
					.getSystemService(Context.VIBRATOR_SERVICE);

			@Override
			public void run() {
				while (isActive) {

					long currentTime = System.currentTimeMillis();

					if (hasBeenMoved) {
						if (flag) {
							playSound();
						} else {
							doAction();
						}
					}

					try {
						sleep(1);
					} catch (InterruptedException e) {
					}

					while (isActive
							&& System.currentTimeMillis() - currentTime < soundPause) {
						try {
							sleep(10);
						} catch (InterruptedException e) {
						}
					}
				}
			}

			public void doAction() {
				new Thread() {
					public void run() {
						if (vibrator == null) {
						}
						if (rotationAngle > 35 && rotationAngle < 58
								&& hasBeenMoved) {
							vibrator.vibrate(200);
						} else if (rotationAngle > 30 && rotationAngle < 58
								&& hasBeenMoved) {
							vibrator.vibrate(65);
						} else if (rotationAngle > 25 && rotationAngle < 58
								&& hasBeenMoved) {
							vibrator.vibrate(55);
						} else if (rotationAngle > 20 && rotationAngle < 58
								&& hasBeenMoved) {
							vibrator.vibrate(40);
						} else if (rotationAngle > 15 && rotationAngle < 58
								&& hasBeenMoved) {
							vibrator.vibrate(30);
						} else if (rotationAngle > 0 && rotationAngle < 58
								&& hasBeenMoved) {
							vibrator.vibrate(25);
						}
					};
				}.start();
			}
		};
	}

}
