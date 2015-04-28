package com.signer.signer;

import com.signer.signer.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePasswordActivity extends Activity {

	 EditText pwd1 = null;
	 EditText pwd2 = null;
	 Button btn_1 = null;
	 Button btn_2 = null;
	 String passwd = null;
	 TextView title;
	 TextView info1;
	 TextView info2;
	 //String headerText=null;
	 SignerApplication app;
	 boolean clicked=false;
	 String type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app=(SignerApplication)getApplication();
		setContentView(R.layout.createpassword);
		pwd1 = (EditText)this.findViewById(R.id.pwd1);
		pwd2 = (EditText)this.findViewById(R.id.pwd2);
		btn_1 = (Button)this.findViewById(R.id.btn_1);
		btn_2 = (Button)this.findViewById(R.id.btn_2);
		title = (TextView)this.findViewById(R.id.title);
		info1 = (TextView)this.findViewById(R.id.info1);
		info2 = (TextView)this.findViewById(R.id.info2);
		// information about create account
 
		Intent intent = this.getIntent();
		type=intent.getStringExtra("type");
		if (type.equals("manual"))
				app.keyString=intent.getStringExtra("key");
		if (type.equals("changepwd")){
			title.setText(getString(R.string.btn_changepwd));
			info1.setText(getString(R.string.inputnewpwd));
			info2.setText(getString(R.string.inputnewpwd2));
		}
		else
			dialog(); 
		//headerText=intent.getStringExtra("info");
		
		
		btn_1.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (clicked)return;
				clicked=true;
				if(pwd1.length() < 10 || pwd2.length() < 10)
				{
					pwd1.setText("");
					pwd2.setText("");
					pwd1.requestFocus();
					Toast.makeText(CreatePasswordActivity.this, getString(R.string.input10digits), Toast.LENGTH_SHORT).show();
				}else if(pwd1.getText().toString().equals(pwd2.getText().toString()))
				{
					
					passwd = pwd1.getText().toString();
					Log.println(3, "signer", "password OK");
					Toast.makeText(CreatePasswordActivity.this, getString(R.string.creatingaccount) , Toast.LENGTH_SHORT).show();
					if (type.equals("changepwd")){
						if(app.changepwd(passwd))
						{
							Log.println(3, "signer", "changepwd OK");
							dialogsuccess();
						}else{
							Toast.makeText(CreatePasswordActivity.this, getString(R.string.createfailure), Toast.LENGTH_SHORT).show();
						}
					}
					else{
						if(app.createAccount(passwd,type))
						{
							Log.println(3, "signer", "createaccount OK");
							dialogsuccess();
						}else{
							Toast.makeText(CreatePasswordActivity.this, getString(R.string.createfailure), Toast.LENGTH_SHORT).show();
						}
					}
				}else {
					pwd1.setText("");
					pwd2.setText("");
					pwd1.requestFocus();
					Toast.makeText(CreatePasswordActivity.this, getString(R.string.mismatch), Toast.LENGTH_SHORT).show();
				}
				clicked=false;
			}
		});
		btn_2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			pwd1.setText("");
			pwd2.setText("");
			pwd1.requestFocus();
			}
		});
		pwd1.addTextChangedListener(mTextWatcher);
		pwd2.addTextChangedListener(mTextWatcher);
	}
	 TextWatcher mTextWatcher = new TextWatcher() {
		 @Override
		 public void onTextChanged(CharSequence s, int start, int before, int count) {
	        }  
		 @Override  
	        public void afterTextChanged(Editable s){
			 if (checkStrength(pwd1.getText().toString())){				 
				 pwd1.setTextColor(0xff000000);
			 }else{				
				 pwd1.setTextColor(0xffff0000);
			 }
			 if (pwd2.getText().toString().equals(pwd1.getText().toString())){				 
				 pwd2.setTextColor(0xff000000);
			 }else{				
				 pwd2.setTextColor(0xffff0000);
			 }
		 }
		 @Override  
	        public void beforeTextChanged(CharSequence s, int start, int count,  
	                int after) { 	        }  
	};  
	public boolean checkStrength(String pwd){
		if (pwd.length()<10) return false;
		byte[] pwdBytes=pwd.getBytes();		
		int strength=1;	
		int mode=0;
		for (int i=1; i<pwd.length();i++){			
			if (mode==0){
				if (pwdBytes[i]==pwdBytes[i-1]){
					mode=1;					
				}else if (pwdBytes[i]==(byte)(pwdBytes[i-1]+1)){
					mode=2;					
				}else if ((byte)(pwdBytes[i]+1)==pwdBytes[i-1]){
					mode=3;					
				}else{
					strength++;
				}
			}else if (mode==1){
				if (pwdBytes[i]!=pwdBytes[i-1]){
					strength++;
					mode=0;
					if (pwdBytes[i]==(byte)(pwdBytes[i-1]+1)){
						mode=2;					
					}else if ((byte)(pwdBytes[i]+1)==pwdBytes[i-1]){
						mode=3;	
					}
				}
			}else if (mode==2){
				if (pwdBytes[i]!=(byte)(pwdBytes[i-1]+1)){
					strength++;
					mode=0;
					if (pwdBytes[i]==(pwdBytes[i-1])){
						mode=1;					
					}else if ((byte)(pwdBytes[i]+1)==pwdBytes[i-1]){
						mode=3;	
					}
				}
			}else if (mode==3){
				if ((byte)(pwdBytes[i]+1)==pwdBytes[i-1]){
					strength++;
					mode=0;
					if (pwdBytes[i]==(pwdBytes[i-1])){
						mode=1;					
					}else if(pwdBytes[i]==(byte)(pwdBytes[i-1]+1)){
						mode=2;	
					}
				}
			}
		}
		if (strength>=3)
			return true;
		return false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.createaccount, menu);
		return true;
	}
	protected void dialog() {    	
    	AlertDialog.Builder builder = new Builder(CreatePasswordActivity.this);
    	 builder.setMessage(this.getString(R.string.createaccountnoticecontent));
    	 builder.setTitle(this.getString(R.string.createaccountnotice));
    	 builder.setPositiveButton(this.getString(R.string.oknext), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				dialog.dismiss();
			}
		});    
    	builder.create().show();
    	 
    	
    }
	protected void dialogsuccess() {
    	// Log.i("aaa", "show");
    	AlertDialog.Builder builder = new Builder(CreatePasswordActivity.this);
    	 builder.setMessage(this.getString(R.string.rememberpassword));
    	 if (type.equals("changepwd"))
    		 builder.setTitle(this.getString(R.string.changepwdok));
    	 else
    		 builder.setTitle(this.getString(R.string.createaccountok));
    	 
    	 builder.setPositiveButton(this.getString(R.string.next), new  OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {				
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putBoolean("result", true);
				resultIntent.putExtras(bundle);
				CreatePasswordActivity.this.setResult(RESULT_OK, resultIntent);				
				dialog.dismiss();
				CreatePasswordActivity.this.finish();
				
				Log.println(3, "signer", "create account result sent");						
			
			}
		});
    
    	builder.create().show();

    }
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//	    switch (keyCode) {
//	        case KeyEvent.KEYCODE_BACK:
//	        return true;
//	        case KeyEvent.KEYCODE_MENU:
//	        return true;	
//	    }
//	    return super.onKeyDown(keyCode, event);
//	}
	// disable "home" button
	

}
