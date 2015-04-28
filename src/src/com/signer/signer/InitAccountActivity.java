package com.signer.signer;

import com.signer.signer.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import android.graphics.Typeface;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

public class InitAccountActivity extends Activity {	
	LinearLayout dizhilayout = null;
	EditText dizhi=null;
	Button auto;
	Button manual;
	Button import_account;
	TextView title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initaccount);		
		title = (TextView) this.findViewById(R.id.title);
		auto = (Button)this.findViewById(R.id.auto);		
		auto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", "auto");
				resultIntent.putExtras(bundle);
				InitAccountActivity.this.setResult(RESULT_OK, resultIntent);				
				finish();
			}
		});
		manual = (Button)this.findViewById(R.id.manual);
		
		manual.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", "manual");
				resultIntent.putExtras(bundle);
				InitAccountActivity.this.setResult(RESULT_OK, resultIntent);
				finish();
				
			}
		});
		import_account = (Button)this.findViewById(R.id.import_account);
		
		import_account.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", "import");
				resultIntent.putExtras(bundle);
				InitAccountActivity.this.setResult(RESULT_OK, resultIntent);
				finish();				
			}
		});
		DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels; 
        float rate = (float)((float)displayWidth/(float)480);
        if (rate<0.6)
       	 rate=(float)0.6;
        FrameLayout.LayoutParams params0 = new FrameLayout.LayoutParams(
                (int) (displayWidth * 0.7f + 0.5f),
                (int) (displayHeight * 0.12f + 0.5f));
        double uOffset =  displayHeight * 0.015 * 2;
        double lOffset = displayWidth * 0.22 * 2;
        params0.setMargins((int)lOffset,  (int)uOffset, 0, 0);			         
        title.setLayoutParams(params0);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                (int) (displayWidth * 0.7f + 0.5f),
                (int) (displayHeight * 0.2f + 0.5f));
        double uOffset2 =  displayHeight * 0.07 * 2;
        double rOffset = displayWidth * 0.05 * 2;
        
        params1.setMargins(0,   (int)uOffset2, (int)rOffset, 0);
        auto.setLayoutParams(params1);
		
		 LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                (int) (displayWidth * 0.7f + 0.5f),
                (int) (displayHeight * 0.12f + 0.5f));
        params2.setMargins(0,2, (int)rOffset, 0);			         
        manual.setLayoutParams(params2);
        import_account.setLayoutParams(params2);
		dialog_intro();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.beifenqianbao, menu);
		return true;
	}

	protected void dialog_intro() {    	
    	AlertDialog.Builder builder = new Builder(InitAccountActivity.this);
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
