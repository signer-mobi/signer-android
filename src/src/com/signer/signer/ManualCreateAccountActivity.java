package com.signer.signer;

import com.signer.signer.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import android.text.Editable;
import android.text.TextWatcher;

import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;

public class ManualCreateAccountActivity extends Activity {
	//Typeface mFace= null;
	TextView title = null;
	TextView firstHalf = null;
	EditText secondHalf=null;
	Button cancel=null;
	Button confirm=null;
	byte[] pvtKey1half=new byte[39];
	byte[] pvtKey2half=new byte[39];
	byte[] pvtKeyfull=new byte[32];
	ManualCreateKey keyGenerator=new ManualCreateKey();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createaccount);
		//initPopuptWindow();
		//mFace=Typeface.createFromAsset(this.getAssets(), "fonts/MSYHBD.TTF");
		//title = (TextView)this.findViewById(R.id.title);
		//title.setTypeface(mFace);
		//dialog_ts();
		//dialog_sm();
		firstHalf = (TextView)this.findViewById(R.id.firsthalf);
		firstHalf.setText(keyGenerator.generateFirstHalf());
		secondHalf = (EditText)this.findViewById(R.id.secondhalf);
		cancel=(Button)this.findViewById(R.id.btn_1);
		confirm=(Button)this.findViewById(R.id.btn_2);
		confirm.setEnabled(false);
		confirm.setTextColor(0xff888888);
		cancel.setOnClickListener(new View.OnClickListener() {	
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		confirm.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						
						Intent resultIntent = new Intent(ManualCreateAccountActivity.this,MainActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("result", firstHalf.getText()+secondHalf.getText().toString());
						resultIntent.putExtras(bundle);
						ManualCreateAccountActivity.this.setResult(RESULT_OK, resultIntent);					
						ManualCreateAccountActivity.this.finish();
					}					
				});
		
		secondHalf.addTextChangedListener(mTextWatcher);
	}
	 TextWatcher mTextWatcher = new TextWatcher() {
		 @Override
		 public void onTextChanged(CharSequence s, int start, int before, int count) {
	        }  
		 @Override  
	        public void afterTextChanged(Editable s){
			 if (secondHalf.length()==39){
				 confirm.setEnabled(true);
				 confirm.setTextColor(0xffffffff);
			 }else{
				 confirm.setEnabled(false);
				 confirm.setTextColor(0xff888888);
			 }
		 }
		 @Override  
	        public void beforeTextChanged(CharSequence s, int start, int count,  
	                int after) { 	        }  
	    };  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.beifenqianbao, menu);
		return true;
	}
	
	protected void dialog_ts() {
    	// Log.i("aaa", "show");
    	AlertDialog.Builder builder = new Builder(ManualCreateAccountActivity.this);
    	 builder.setMessage(this.getString(R.string.zhanghu_key));
    	 builder.setTitle(this.getString(R.string.create_zhts));   	
    	 
    	 builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				
				
				dialog.dismiss();
			}
		});
    	 builder.setNegativeButton(this.getString(R.string.quxiao), new OnClickListener() {
 			
 			@Override
 			public void onClick(DialogInterface dialog, int which) {
 				// TODO Auto-generated method stub
 				dialog.dismiss(); 
 				
 			}
 		});  
 
    
    	builder.create().show();

    }
	
	protected void dialog_sm() {
    	// Log.i("aaa", "show");
    	AlertDialog.Builder builder = new Builder(ManualCreateAccountActivity.this);
    	 builder.setMessage(this.getString(R.string.initialize_instruction));
    	 builder.setTitle(this.getString(R.string.instructions));
    	    	 
    	 builder.setPositiveButton(this.getString(R.string.next), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
    	
    
    	builder.create().show();

    }
	
	


	

	

}
