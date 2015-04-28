package com.signer.signer;
import com.signer.signer.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BackupAccountActivity extends Activity {
	
	Button btn1 = null;
	Button btn2 = null;
	TextView intro=null;
	String info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beifenqianbao);
		btn1 = (Button)this.findViewById(R.id.btn_1);
		btn2 = (Button)this.findViewById(R.id.btn_2);
		intro=(TextView)this.findViewById(R.id.shuoming);
		Intent intent = this.getIntent();
		info=intent.getStringExtra("info");
		if (info.equals("first_time")){
			
		}else{
			intro.setText(getString(R.string.beifenshuoming));
		}
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", "Y");
				resultIntent.putExtras(bundle);
				BackupAccountActivity.this.setResult(RESULT_OK, resultIntent);					
				BackupAccountActivity.this.finish();				
			}
		});
       btn2.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {				
			if (info.equals("re_backup")){
				BackupAccountActivity.this.finish();
				return;
			}	
			dialog();	
			}
		});       
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.beifenqianbao, menu);
		return true;
	}
	protected void dialog() {
    	// Log.i("aaa", "show");
    	AlertDialog.Builder builder = new Builder(BackupAccountActivity.this);
    	 builder.setMessage(this.getString(R.string.quedingbubeifen));
    	 builder.setTitle(this.getString(R.string.tishi));
    	
    	 
    	 builder.setPositiveButton(this.getString(R.string.jixubeifen), new  OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", "Y");
				resultIntent.putExtras(bundle);
				BackupAccountActivity.this.setResult(RESULT_OK, resultIntent);	
				dialog.dismiss();
				BackupAccountActivity.this.finish();	
				
//				Intent intent= new Intent(BeifenqianbaoActivity.this, Beifenqianbao2Activity.class);
//				startActivity(intent);
				
				
			}
		});
    	 builder.setNegativeButton(this.getString(R.string.qedingbubeifen), new OnClickListener() {
 			
 			@Override
 			public void onClick(DialogInterface dialog, int which) {
 				// TODO Auto-generated method stub
 				dialog.dismiss(); 
 				Intent resultIntent = new Intent(BackupAccountActivity.this,MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("result", "N");
				resultIntent.putExtras(bundle);
				BackupAccountActivity.this.setResult(RESULT_OK, resultIntent);					
				BackupAccountActivity.this.finish();
 			}
 		});  
 
    
    	builder.create().show();

    }
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    switch (keyCode) {
	        case KeyEvent.KEYCODE_BACK:
	        return true;
	        case KeyEvent.KEYCODE_MENU:
	        return true;	
	    }
	    return super.onKeyDown(keyCode, event);
	}
	

}
