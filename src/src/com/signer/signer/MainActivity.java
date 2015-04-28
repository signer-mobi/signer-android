package com.signer.signer;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.Utils;
import com.signer.signer.R;
import com.signer.signer.SignerApplication;
import com.signer.utils.SignRequest;

public class MainActivity extends Activity {
	Button btn_showaddr = null;
	Button btn_scanrequest = null;
	// Button btn_inputrequest = null;
	// Button btn_history = null;
	Button btn_settings = null;
	Button btn_others = null;
	Button btn_accountManage = null;
	Button btn_about = null;
	int level;
	int scale;
	Button button;
	ImageView lowbattery;
	private SharedPreferences pres;
	SignerApplication app;
	String fullRequest = "";
	byte[] tempbytearray = new byte[0];
	int currentpage = 0;
	int totalpage = 0;
	byte[] scandatastream = new byte[0];
	byte qrCommand = 0x00;
	boolean haspower;
	Intent intent;
	Bundle bundle;
	boolean totalUpdate = false;
	protected TextView title;
	protected TextView txt_current_account;
	private ArrayAdapter<String> adapter;
	private Spinner spinner_accountList;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				app = (SignerApplication) MainActivity.this.getApplication();
				btn_showaddr.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								ShowAddressActivity.class);
						startActivity(intent);
					}
				});
				btn_scanrequest.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						currentpage = 0;
						totalpage = 0;
						intent = new Intent(MainActivity.this,
								CaptureActivity.class);
						intent.putExtra("title", R.string.title_scan_request);
						intent.putExtra("info", getString(R.string.scanqr));
						startActivityForResult(intent, 40);
					}
				});
				btn_settings.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						intent = new Intent(MainActivity.this,
								QrLengthActivity.class);
						startActivity(intent);
					}
				});
				btn_accountManage
						.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								intent = new Intent(MainActivity.this,
										AccountManageActivity.class);
								// intent.putExtra("info","re_backup");
								startActivityForResult(intent, 9);

							}
						});
				btn_about.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this,
								AboutActivity.class);
						startActivity(intent);
					}
				});
				verifyAccount();

				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// KeyguardManager mKeyGuardManager =
		// (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
		// KeyguardLock mLock =
		// mKeyGuardManager.newKeyguardLock("MainActivity");

		// mLock.disableKeyguard();
		title = (TextView) MainActivity.this.findViewById(R.id.title);
		// txt_current_account=(TextView)
		// MainActivity.this.findViewById(R.id.txt_current_account);
		Log.println(3, "signer", "started");
		btn_showaddr = (Button) MainActivity.this
				.findViewById(R.id.btn_showaddr);
		btn_scanrequest = (Button) MainActivity.this
				.findViewById(R.id.btn_scanrequest);
		// btn_inputrequest = (Button)
		// MainActivity.this.findViewById(R.id.btn_inputrequest);
		// btn_history = (Button)
		// MainActivity.this.findViewById(R.id.btn_history);
		btn_settings = (Button) MainActivity.this
				.findViewById(R.id.btn_settings);
		btn_accountManage = (Button) this.findViewById(R.id.accountmanage);
		btn_about = (Button) this.findViewById(R.id.about);
		spinner_accountList = (Spinner) findViewById(R.id.spinner1);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int displayWidth = displayMetrics.widthPixels;
		int displayHeight = displayMetrics.heightPixels;
		float rate = (float) ((float) displayWidth / (float) 480);
		if (rate < 0.6)
			rate = (float) 0.6;
		LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.7f + 0.5f),
				(int) (displayHeight * 0.12f + 0.5f));
		double uOffset = displayHeight * 0.015 * 2;
		double lOffset = displayWidth * 0.22 * 2;
		params0.setMargins((int) lOffset, (int) uOffset, 0, 0);
		title.setLayoutParams(params0);
		LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
				(int) (displayWidth), (int) (displayHeight * 0.1f + 0.5f));
		double uOffset3 = displayHeight * 0.0;
		params3.setMargins(0, (int) uOffset3, 0, 0);
		// txt_current_account.setLayoutParams(params3);
		spinner_accountList.setLayoutParams(params3);

		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.7f + 0.5f),
				(int) (displayHeight * 0.12f + 0.5f));
		double uOffset2 = displayHeight * 0.01 * 2;
		double rOffset = displayWidth * 0.05 * 2;

		params1.setMargins(0, (int) uOffset2, (int) rOffset, 0);
		btn_showaddr.setLayoutParams(params1);

		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				(int) (displayWidth * 0.7f + 0.5f),
				(int) (displayHeight * 0.12f + 0.5f));
		params2.setMargins(0, 2, (int) rOffset, 0);
		btn_scanrequest.setLayoutParams(params2);
		// btn_inputrequest.setLayoutParams(params2);
		// btn_history.setLayoutParams(params2);
		btn_settings.setLayoutParams(params2);
		btn_accountManage.setLayoutParams(params2);
		btn_about.setLayoutParams(params2);

		Thread thread = new Thread() {
			public void run() {
				try {
					sleep(6500);
					handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
				}
			};
		};
		thread.start();

	}

	public void verifybackup() {
		pres = this.getSharedPreferences("Signer", Context.MODE_PRIVATE);
		String backupstatus = pres.getString("backupstatus", "null");
		Log.println(3, "signer", "verify backup status:" + backupstatus);
		if (backupstatus.equals("NoNeed")) {// show dialog to notify backup
			// Toast.makeText(MainActivity.this, getString(R.string.unbackuped),
			// Toast.LENGTH_LONG).show();
		} else if (backupstatus.equals("Backuped")) {
		} else {// backupstatus wrong,start backup workflow
			String[] accountString = app.getBackupQr();
			if (accountString != null) {
				intent = new Intent(MainActivity.this, FeedbackActivity.class);
				bundle = new Bundle();
				bundle.putString("title", getString(R.string.beifenqianbao));
				bundle.putString("info", getString(R.string.scanqr));
				bundle.putStringArray("qrs", accountString);
				intent.putExtras(bundle);
				startActivityForResult(intent, 6);
				return;
			} else {
				verifyAccount();
				return;
			}
		}
	}

	public void verifyAccount() {
		pres = this.getSharedPreferences("Signer", Context.MODE_PRIVATE);
		boolean accountstatus = pres.getBoolean("accountstatus", false);
		if (!accountstatus)// verify failure
		{
			Log.println(3, "signer", "accountstatus:" + accountstatus);
			intent = new Intent(MainActivity.this, InitAccountActivity.class);
			startActivityForResult(intent, 2);
		} else {// verify OK,go next step
			adapter = new ArrayAdapter<String>(MainActivity.this,
					android.R.layout.simple_spinner_item, app.accountList);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_accountList.setAdapter(adapter);
			spinner_accountList
					.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							app.switchAccount(adapter.getItem(arg2));
						}

						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
			int position = adapter.getPosition(app.getAddressString("BTC"));
			Log.println(3, "signer", "position:" + position);
			Log.println(3, "signer", "position0:" + adapter.getItem(0));
			Log.println(3, "signer", "address:" + app.getAddressString("BTC"));
			spinner_accountList.setSelection(position, true);
			spinner_accountList.setVisibility(View.VISIBLE);
			// verifybackup();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean verifyQr(String qr) {
		Log.println(3, "signer", "verify qr " + qr);
		if (!qr.substring(0, 4).equals("Sign")) {
			Log.println(3, "signer",
					"verify qr error: wrong header" + qr.substring(0, 4));
			return false;
		}
		if (!isNumeric(qr.substring(4, 6))) {
			Log.println(3, "signer", "verify qr error: wrong cp");
			return false;
		}
		if (!qr.substring(6, 7).equals("/")) {
			Log.println(3, "signer", "verify qr error: wrong /");
			return false;
		}
		if (!isNumeric(qr.substring(7, 9))) {
			Log.println(3, "signer", "verify qr error: wrong tp");
			return false;
		}
		if (Long.valueOf(qr.substring(4, 6)) > Long.valueOf(qr.substring(7, 9))) {
			Log.println(3, "signer", "verify qr error: wrong cp/tp");
			return false;
		}
		return true;
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean receiveMultipage(String qr, String title, int requestcode) {
		int tp = Long.valueOf(qr.substring(7, 9)).intValue();
		int cp = Long.valueOf(qr.substring(4, 6)).intValue();
		if (tp == 1) {// only one page
			fullRequest = qr.substring(9);
			return true;
		}
		if (cp == currentpage + 1) {// normal
			if (totalpage == 0) {// first received page
				totalpage = tp;
				currentpage = 1;
				fullRequest = qr.substring(9);
			} else {// normal
				currentpage++;
				// append
				fullRequest += qr.substring(9);
			}
			if (tp == totalpage) {// last page,finish
				return true;
			}
			// normal, scan next page
			Toast.makeText(MainActivity.this, getString(R.string.continuescan),
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("info", getString(R.string.info_scanpage)
					+ (currentpage + 1));
			startActivityForResult(intent, requestcode);
			return false;
		} else if (cp <= currentpage) {// duplicate scan
			Toast.makeText(MainActivity.this,
					getString(R.string.duplicatescan), Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("info", getString(R.string.info_scanpage)
					+ (currentpage + 1));
			startActivityForResult(intent, requestcode);
			return false;
		} else if (cp > currentpage + 1) {// jumped scan
			Toast.makeText(MainActivity.this, getString(R.string.jumpedscan),
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
			intent.putExtra("title", title);
			intent.putExtra("info", getString(R.string.info_scanpage)
					+ (currentpage + 1));
			startActivityForResult(intent, requestcode);
			return false;
		}
		return false;
	}

	public void processSignRequest(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String qr = bundle.getString("result");
			if (qr == null) {
				Log.println(3, "signer", "processsignrequest:empty scan result");
				Toast.makeText(MainActivity.this,
						getString(R.string.scan_request_fail),
						Toast.LENGTH_SHORT).show();
				return;
			}
			// get data from qr code
			if (!verifyQr(qr)) {
				Toast.makeText(MainActivity.this,
						getString(R.string.scan_request_fail),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (receiveMultipage(qr, getString(R.string.title_scan_request), 40)) {

				try {
					SignRequest sr = new SignRequest(fullRequest);
					Address requestAddress = new Address(
							SignerApplication.NETWORK_PARAMETERS, sr.address);
					if (sr.command.equals("signHashes")
							&& Arrays.equals(requestAddress.getHash160(),
									app.getPubKeyHash())) {
						// ask for password
						Intent intent = new Intent(MainActivity.this,
								InputPasswordActivity.class);
						intent.putExtra("title", R.string.getpassword);
						intent.putExtra("info", getString(R.string.getpassword));
						startActivityForResult(intent, 41);
					} else {
						Log.println(3, "signer",
								"processsignrequest:cmd or address mismatch");
						Toast.makeText(MainActivity.this,
								getString(R.string.scan_request_fail),
								Toast.LENGTH_SHORT).show();
					}
					return;
				} catch (Exception e) {// if is not json array ,return error
					Log.println(3, "signer",
							"processsignrequest:content is not json array" + e);
					Toast.makeText(MainActivity.this,
							getString(R.string.scan_request_fail),
							Toast.LENGTH_SHORT).show();
					return;
				}
			} else
				return;
		}
	}

	public void doSignRequest(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			Log.println(3, "signer", "get sign password:" + resultCode + result);
			if (result.contains("passwordok")) {
				String[] signFeedback = app.doSignRequest(fullRequest);
				if (signFeedback == null)
					Toast.makeText(MainActivity.this,getString(R.string.scan_request_fail),Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(MainActivity.this,FeedbackActivity.class);
				intent.putExtra("qrs", signFeedback);
				intent.putExtra("action", getString(R.string.signresult));
				startActivity(intent);
			}
		}
	}

	public void processIniAccount(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String iniResult = bundle.getString("result");
			if (iniResult.equals("auto")) {
				intent = new Intent(MainActivity.this,
						CreatePasswordActivity.class);
				intent.putExtra("type", "auto");
				startActivityForResult(intent, 3);

			}
			if (iniResult.equals("manual")) {
				intent = new Intent(MainActivity.this,
						ManualCreateAccountActivity.class);
				startActivityForResult(intent, 7);
			}
			if (iniResult.equals("import")) {
				currentpage = 0;
				totalpage = 0;
				intent = new Intent(MainActivity.this, CaptureActivity.class);
				intent.putExtra("title",(R.string.import_account));
				intent.putExtra("info", getString(R.string.scanwallet));
				startActivityForResult(intent, 8);
			}
			return;
		}
		verifyAccount();
	}

	public void processAccountManage(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String iniResult = bundle.getString("result");
			// Log.println(3, "signer","account manager:"+iniResult);
			if (iniResult.equals("backup")) {
				intent = new Intent(MainActivity.this,
						InputPasswordActivity.class);
				intent.putExtra("title", R.string.beifen);
				intent.putExtra("info",
						MainActivity.this.getString(R.string.inputpassword));
				startActivityForResult(intent, 5);
			}
			if (iniResult.equals("new")) {
				intent = new Intent(MainActivity.this,
						InitAccountActivity.class);
				startActivityForResult(intent, 2);
			}
			if (iniResult.equals("changepwd")) {
				intent = new Intent(MainActivity.this,
						CreatePasswordActivity.class);
				intent.putExtra("type", "changepwd");
				startActivityForResult(intent, 30);
			}

			if (iniResult.equals("import")) {
				intent = new Intent(MainActivity.this, CaptureActivity.class);
				intent.putExtra("title", R.string.import_account);
				intent.putExtra("info", getString(R.string.scanwallet));
				startActivityForResult(intent, 8);
			}
			return;
		}
		verifyAccount();
	}

	public void processManualCreateAccount(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			intent = new Intent(MainActivity.this, CreatePasswordActivity.class);
			intent.putExtra("type", "manual");
			intent.putExtra("key", bundle.getString("result"));
			// write the privatekey data in hwwApplication
			startActivityForResult(intent, 3);
			return;
		}
		verifyAccount();
	}

	public void backuppage1(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			Log.println(3, "signer", "backuppage1result:" + scanResult);
			if (scanResult.equals("Y")) {
				intent = new Intent(MainActivity.this,
						InputPasswordActivity.class);
				intent.putExtra("title", R.string.beifen);
				intent.putExtra("info",
						MainActivity.this.getString(R.string.inputpassword));
				startActivityForResult(intent, 5);
				return;
			}
			if (scanResult.equals("N")) {
				Log.println(3, "signer", "backuppage:refuse");
				pres = this
						.getSharedPreferences("Signer", Context.MODE_PRIVATE);
				if (!pres.getString("backupstatus", "").equals("Backuped")) {
					SharedPreferences.Editor ed = pres.edit();
					ed.putString("backupstatus", "NoNeed");
					ed.commit();
					verifybackup();
				}
				return;
			}

		} else {
			verifybackup();
			return;
		}
	}

	public void backuppage2(int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			Log.println(3, "signer", "backuppage2:" + resultCode + result);
			if (result.contains("passwordok")) {
				String[] accountString = app.getBackupQr();
				if (accountString != null) {
					intent = new Intent(MainActivity.this,
							FeedbackActivity.class);
					bundle = new Bundle();
					bundle.putString("title", getString(R.string.beifenqianbao));
					bundle.putString("info", getString(R.string.scanqr));
					bundle.putStringArray("qrs", accountString);
					intent.putExtras(bundle);
					startActivityForResult(intent, 6);
					return;
				} else {
					verifyAccount();
					return;
				}

			} else {
				verifybackup();
				return;
			}

		} else {
			verifybackup();
			return;
		}
	}

	public byte[] trimQr(byte[] resultbytes) {
		int trim = 3;
		if (resultbytes.length > 250)
			trim = 5;
		String hexbytes = Utils.bytesToHexString(resultbytes);
		Log.println(3, "signer",
				"main.trimqr:" + hexbytes.substring(trim, hexbytes.length()));
		return Utils.hexStringToBytes(hexbytes.substring(trim,
				hexbytes.length()));
	}

	public boolean verifyQrOld(byte[] fulldata, int requestcode) {
		byte[] tempint = { 0, 0, fulldata[3], fulldata[4] };
		int pagelength = SignerApplication.byteArrayToInt(tempint, 0);
		if (requestcode == 8)
			pagelength -= 4;// for backup only, minus the hash 4 bytes
		if (pagelength > fulldata.length - 5) {
			return false;
		}
		byte[] trimedbytes = new byte[pagelength + 5];
		System.arraycopy(fulldata, 0, trimedbytes, 0, trimedbytes.length);
		byte[] hash = Utils.sha256hash160(trimedbytes);
		byte[] shorthash1 = new byte[4];
		byte[] shorthash2 = new byte[4];
		System.arraycopy(hash, 0, shorthash1, 0, 4);
		System.arraycopy(fulldata, pagelength + 5, shorthash2, 0, 4);
		if (!Arrays.equals(shorthash2, shorthash1)) {
			Log.println(3, "signer", "hasherror:");
			return false;
		}
		return true;
	}

	public void processImportAccount(int resultCode, Intent data) {

		if (resultCode == -1) {
			Bundle bundle = data.getExtras();
			byte[] resultbytes = bundle.getByteArray("resultbytes");
			if (resultbytes == null) {
				Log.println(3, "signer", "empty wallet result");
				Toast.makeText(MainActivity.this,
						getString(R.string.import_fail), Toast.LENGTH_SHORT)
						.show();
				return;
			}
			// get data from qr code
			byte[] fulldata = trimQr(resultbytes);
			byte[] wallettosave = new byte[153];

			if (fulldata[0] == (byte) 0xba) {// new backup format
				if (!verifyQrOld(fulldata, 8)) {
					Log.println(3, "signer", "bad wallet result");
					Toast.makeText(MainActivity.this,
							getString(R.string.import_fail), Toast.LENGTH_SHORT)
							.show();
					verifyAccount();
					return;
				}
				byte[] tempint = { 0, 0, fulldata[3], fulldata[4] };
				int pagelength = SignerApplication.byteArrayToInt(tempint, 0);
				scandatastream = new byte[pagelength];
				System.arraycopy(fulldata, 5, scandatastream, 0, pagelength);
				Log.println(3, "signer", "scandatastream length:" + pagelength);
				Log.println(
						3,
						"signer",
						"scandatastream :"
								+ Utils.bytesToHexString(scandatastream));
				byte[] tempbyte = new byte[3];
				System.arraycopy(scandatastream, 0, tempbyte, 0, 3);
				String walletType = "";
				try {
					walletType = new String(tempbyte, "ISO-8859-1");
				} catch (UnsupportedEncodingException e) {
				}
				if (!walletType.equals("HB1")) {
					Log.println(3, "signer", "bad wallet type:" + walletType);
					Toast.makeText(MainActivity.this,
							getString(R.string.import_fail), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				byte[] tempint1 = { 0, 0, scandatastream[3], scandatastream[4] };
				int walletLength = SignerApplication
						.byteArrayToInt(tempint1, 0);
				if (walletLength != scandatastream.length - 9) {
					Log.println(3, "signer", "bad wallet length:"
							+ walletLength);
					Toast.makeText(MainActivity.this,
							getString(R.string.import_fail), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				wallettosave = new byte[scandatastream.length - 9];
				System.arraycopy(scandatastream, 5, wallettosave, 0,
						scandatastream.length - 9);
				if (!app.importWallet(wallettosave)) {
					Log.println(3, "signer", "import wallet fail:"
							+ walletLength);
					Toast.makeText(MainActivity.this,
							getString(R.string.import_fail), Toast.LENGTH_SHORT)
							.show();
				} else
					Toast.makeText(MainActivity.this,
							getString(R.string.import_OK), Toast.LENGTH_SHORT)
							.show();
				verifyAccount();
				return;
			} else {
				Log.println(3, "signer", "wrong command");
				Toast.makeText(MainActivity.this,
						getString(R.string.import_fail), Toast.LENGTH_SHORT)
						.show();
				verifyAccount();
				return;
			}

		}
		verifyAccount();
		return;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2:// createaccount step1,choose method
			processIniAccount(resultCode, data);
			return;
		case 3: // createaccount result
			Log.println(3, "signer", "Mainactivity:createaccount result");
			verifyAccount();
			return;
		case 4: // backuppage1 result
			backuppage1(resultCode, data);
			return;
		case 5:
			// backuppage2 result
			backuppage2(resultCode, data);
			return;
		case 6:
			// backuppage3 result
			Log.println(3, "signer", "backuppage3:" + resultCode);
			pres = getSharedPreferences("Signer", Context.MODE_PRIVATE);
			SharedPreferences.Editor ed = pres.edit();
			ed.putString("backupstatus", "Backuped");
			ed.commit();
			return;
		case 7:// manual create account
			processManualCreateAccount(resultCode, data);
			return;
		case 8:// import account scan result
			processImportAccount(resultCode, data);
			return;
		case 9:
			processAccountManage(resultCode, data);
			return;
		case 30:// changepwdOK
			verifyAccount();
			return;
		case 40:// scan request
			processSignRequest(resultCode, data);
			return;
		case 41:// request password
			doSignRequest(resultCode, data);
			return;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			break;
		// return true;
		case KeyEvent.KEYCODE_MENU:
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onDestroy() {
		super.onDestroy();
	}
	// public void onPause(){
	// }

}
