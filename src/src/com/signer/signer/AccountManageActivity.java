package com.signer.signer;

import com.signer.signer.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AccountManageActivity extends Activity {
	//Typeface mFace= null;
	TextView title = null;
	Button btn_backup;	
	Button btn_switch;
	Button btn_changepwd;
	Button btn_import_account;	
	Button btn_new_account;
	SignerApplication app;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accountmanage);
		app=(SignerApplication)this.getApplication();
		title = (TextView)this.findViewById(R.id.title);
		btn_backup = (Button)this.findViewById(R.id.backup);		
		btn_backup.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", "backup");
				resultIntent.putExtras(bundle);
				AccountManageActivity.this.setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
//		btn_switch = (Button)this.findViewById(R.id.switchaccount);
//		btn_switch.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent resultIntent = new Intent();
//				Bundle bundle = new Bundle();
//				bundle.putString("result", "switch");
//				resultIntent.putExtras(bundle);
//				AccountManageActivity.this.setResult(RESULT_OK, resultIntent);
//				finish();
//				
//			}
//		});
		
		btn_changepwd = (Button)this.findViewById(R.id.changepwd);
		btn_changepwd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog2(AccountManageActivity.this.getString(R.string.text_changepwd));
				
			}
		});
		btn_import_account = (Button)this.findViewById(R.id.import_account);		
		btn_import_account.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result","import");
				resultIntent.putExtras(bundle);
				AccountManageActivity.this.setResult(RESULT_OK, resultIntent);			
				finish();
			}
		});	
		btn_new_account = (Button)this.findViewById(R.id.new_account);		
		btn_new_account.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result","new");
				resultIntent.putExtras(bundle);
				AccountManageActivity.this.setResult(RESULT_OK, resultIntent);			
				finish();
			}
		});	
		DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels; 
       
        FrameLayout.LayoutParams params0 = new FrameLayout.LayoutParams(
                (int) (displayWidth * 0.7f + 0.5f),
                (int) (displayHeight * 0.12f + 0.5f));
        double uOffset =  displayHeight * 0.015 * 2;
        double lOffset = displayWidth * 0.22 * 2;
        params0.setMargins((int)lOffset,  (int)uOffset, 0, 0);			         
        title.setLayoutParams(params0);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                (int) (displayWidth * 0.7f + 0.5f),
                (int) (displayHeight * 0.12f + 0.5f));
        double uOffset2 =  displayHeight * 0.2;
        double rOffset = displayWidth * 0.05 * 2;
        
        params1.setMargins(0,   (int)uOffset2, (int)rOffset, 0);			         
        btn_backup.setLayoutParams(params1);
		
		 LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                (int) (displayWidth * 0.7f + 0.5f),
                (int) (displayHeight * 0.12f + 0.5f));
        params2.setMargins(0,2, (int)rOffset, 0);			         
//        btn_switch.setLayoutParams(params2);
        btn_changepwd.setLayoutParams(params2);
        btn_import_account.setLayoutParams(params2);
        btn_new_account.setLayoutParams(params2);
	}
	
	protected void dialog2(final String notice) {    	
    	AlertDialog.Builder builder = new Builder(AccountManageActivity.this);
    	 builder.setMessage(notice);
    	 builder.setTitle(this.getString(R.string.tishi));
    	 builder.setPositiveButton(this.getString(R.string.confirm), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(AccountManageActivity.this,InputPasswordActivity.class);
				//if (action.equals("reset")){
					intent.putExtra("title",R.string.btn_changepwd);
					intent.putExtra("info",AccountManageActivity.this.getString(R.string.inputpassword));
					startActivityForResult(intent,2);
				//}
//				if (action.equals("import")){
//					intent.putExtra("title",AccountGuanliActivity.this.getString(R.id.import_account));
//					startActivityForResult(intent,2);
//				}				
					
				dialog.dismiss();				
			}
		});
    	 builder.setNegativeButton(this.getString(R.string.quxiao), new OnClickListener() {
 			
 			@Override
 			public void onClick(DialogInterface dialog, int which) { 				
 				dialog.dismiss(); 
 			}
 		});  
    	builder.create().show();
    }
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode==2){
			if (resultCode==RESULT_OK){
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result","changepwd");
				resultIntent.putExtras(bundle);
				AccountManageActivity.this.setResult(RESULT_OK, resultIntent);			
				finish();
			}
		}

	}
}
