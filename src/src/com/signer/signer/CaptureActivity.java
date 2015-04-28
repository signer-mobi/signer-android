package com.signer.signer;

import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.signer.signer.R;

import com.google.bitcoin.core.Utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.zxing.camera.CameraManager;
import com.zxing.decoding.CaptureActivityHandler;
import com.zxing.decoding.InactivityTimer;
import com.zxing.view.ViewfinderView;
/**
 * Initial the camera
 * @author Ryan.Tang
 */
public class CaptureActivity extends Activity implements Callback {

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;	
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private Button cancelScanButton;
	TextView title = null;
	TextView qr_scan_text = null;
	float rate = 0.0f;
	ImageView erweimakuang = null;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private static final String TAG = CaptureActivity.class.getSimpleName();
	
	public CameraManager getCameraManager() {
	    return cameraManager;
	  }

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Window window = getWindow();
	    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.saomiao);
		//ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
		cameraManager = new CameraManager(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setInit(cameraManager, 0);
		cancelScanButton = (Button) this.findViewById(R.id.btn_cancel_scan);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		Intent intent = this.getIntent();		
		int title1 = intent.getExtras().getInt("title");		
		title = (TextView)this.findViewById(R.id.title);
		title.setText(getString(title1));
		qr_scan_text = (TextView)this.findViewById(R.id.qr_scan_text);
		erweimakuang = (ImageView)this.findViewById(R.id.erweimakuang);
		 DisplayMetrics displayMetrics = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         int displayWidth = displayMetrics.widthPixels;
         int displayHeight = displayMetrics.heightPixels;
         rate = (float)(displayWidth/480);
         if (rate<0.6)
        	 rate=(float)0.6;
         LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                 (int) (displayWidth * 0.7f + 0.5f),
                 (int) (displayHeight * 0.062f + 0.5f));
         double jushang =  displayHeight * 0.03 * 2;
         double juzuo = displayWidth * 0.3 * 2;
         params0.setMargins((int)juzuo,  (int)jushang, 0, 0);         
         title.setLayoutParams(params0);
         
         LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                 (int) (displayWidth * 0.7f + 0.5f),
                 (int) (displayHeight * 0.07f + 0.5f));
         double jushang2 =  displayHeight * 0.020 * 2;    
     
      
         double juzuo2 = displayWidth * 0.04 * 2;
         
         params1.setMargins((int)juzuo2, (int)jushang2, 0, 0);         
         qr_scan_text.setLayoutParams(params1);
         
         LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                 (int) (displayWidth),
                 (int) (displayWidth));
         double jushang3 =  displayHeight * 0.005 * 2;    
           
         
         params2.setMargins(0, -(int)jushang3, 0, 0);         
         erweimakuang.setLayoutParams(params2);
         LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
        		 (int) (displayWidth * 0.8f + 0.5f),
                 (int) (displayHeight * 0.1f + 0.5f));
         double juzuo3 = displayWidth * 0.06 * 2;
         params3.setMargins((int)juzuo3, 6, 0, 0);                	         
         cancelScanButton.setLayoutParams(params3);
	}

	@Override
	protected void onResume() {
		super.onResume();
		cameraManager = new CameraManager(getApplication());
		//CameraManager.setFront(true);
	    viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
	    viewfinderView.setInit(cameraManager, 0);

	    handler = null;
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		Log.println(3, "signer", "hasSurface:"+hasSurface);
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		
		//quit the scan view
		cancelScanButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		cameraManager.closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		byte[] resultbytes=result.getRawBytes();
		String resultString = result.toString();//getText();		
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
		}else {
//			System.out.println("Result:"+resultString);
			Intent resultIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putByteArray("resultbytes", resultbytes);
			bundle.putString("result", resultString);
			resultIntent.putExtras(bundle);
			this.setResult(RESULT_OK, resultIntent);
		}
		CaptureActivity.this.finish();
	}
	
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
		      throw new IllegalStateException("No SurfaceHolder provided");
		 }
		    if (cameraManager.isOpen()) {
		    	Log.println(3, "signer", "initCamera() while already open -- late SurfaceView callback?");
		      return;
		    }
		    try {
		      cameraManager.openDriver(surfaceHolder);
		      // Creating the handler starts the preview, which can also throw a RuntimeException.
		      if (handler == null) {
		        handler = new CaptureActivityHandler(this, null, null, cameraManager);
		      }
		    } catch (IOException ioe) {
		    	Log.println(3, "signer", ioe.toString());
		    } catch (RuntimeException e) {
		      // Barcode Scanner has seen crashes in the wild of this variety:
		      // java.?lang.?RuntimeException: Fail to connect to camera service
		    	Log.println(3, "signer", "Unexpected error initializing camera:"+ e.toString());
		    }
		    Log.println(3, "signer", "initcamera OK");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}