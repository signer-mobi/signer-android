package com.signer.signer;


import java.text.DecimalFormat;

import com.signer.signer.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class QrLengthActivity extends Activity {
	
	TextView erweimachangdu_display,erweimachangdu_title;
	SeekBar erweimachangdu_seekbar;
	Button erweimachangdu_default,erweimachangdu_quxiao,erweimachangdu_queren;
	Button add,cut;
	TextView title = null;
	float rate = 1.0f;
	int pos;
	SignerApplication ini;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrlength);
		ini=(SignerApplication) this.getApplication();
		title=(TextView)this.findViewById(R.id.title);
		 erweimachangdu_display=(TextView)this.findViewById(R.id.erweimachangdu_display);
		 erweimachangdu_seekbar=(SeekBar)this.findViewById(R.id.erweimachangdu_seekbar);
		 erweimachangdu_seekbar.setProgress(ini.pagelength-100);
		 erweimachangdu_default=(Button)this.findViewById(R.id.erweimachangdu_default);;
		 erweimachangdu_quxiao=(Button)this.findViewById(R.id.erweimachangdu_quxiao);
		 erweimachangdu_queren=(Button)this.findViewById(R.id.erweimachangdu_queren);
		 add=(Button)this.findViewById(R.id.erweimachangdu_add);
		 cut=(Button)this.findViewById(R.id.erweimachangdu_cut);
		 
		 
		 erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+ini.pagelength);
		 erweimachangdu_default.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					erweimachangdu_seekbar.setProgress(324);
					pos=erweimachangdu_seekbar.getProgress()+100;
					   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos+getString(R.string.bytee)); 
				
				}			
			});
		 erweimachangdu_quxiao.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}			
			});
		 erweimachangdu_queren.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					pos=erweimachangdu_seekbar.getProgress()+100;
					pos=pos+pos%2;
					dialog();
					//finish();
				}			
			});
		 add.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					erweimachangdu_seekbar.setProgress(erweimachangdu_seekbar.getProgress()+1);
					   pos=erweimachangdu_seekbar.getProgress()+100;
					   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos+getString(R.string.bytee)); 
					
				}			
			});
		 cut.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					erweimachangdu_seekbar.setProgress(erweimachangdu_seekbar.getProgress()-1);
					   int pos1=erweimachangdu_seekbar.getProgress()+100;
					   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos1+getString(R.string.bytee));
					   
				}			
			});
		 
		//Typeface mFace = Typeface.createFromAsset(this.getAssets(), "fonts/MSYHBD.TTF");
		//erweimachangdu_title.setTypeface(mFace);
		 DisplayMetrics displayMetrics = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         int displayWidth = displayMetrics.widthPixels;
         int displayHeight = displayMetrics.heightPixels;
//         if(Constant.displayWidth / 480 > 1)
//         {
//        	  rate = (float)Constant.displayWidth/480;
//        	 
//         }else {
//			rate = 1;
//		 }
         rate = (float)(displayWidth/480);
         if (rate<0.6)
        	 rate=(float)0.6;
         LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                 (int) (displayWidth * 0.4f + 0.5f),
                 (int) (displayHeight * 0.12f + 0.5f));
         double jushang =  displayHeight * 0.015 * 2;
         double juzuo = displayWidth * 0.28 * 2;
         params0.setMargins((int)juzuo,  (int)jushang, 0, 0);
         //title.setTextSize((int)(title.getTextSize()*rate));
         title.setLayoutParams(params0);
         
         LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                 (int) (displayWidth * 0.95f + 0.5f),
                 LayoutParams.WRAP_CONTENT);
         
        // Log.i("aaa", "gao" +  Constant.displayHeight + "kuan" + Constant.displayWidth);
        
         double juzuo2 = displayWidth * 0.02 * 2;
         
         params1.setMargins((int)juzuo2,   2, 0, 0);
         //erweimachangdu_display.setTextSize((int)(erweimachangdu_display.getTextSize()*rate));
         erweimachangdu_display.setLayoutParams(params1);
         
         
         LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                 (int) (displayWidth * 0.8f + 0.5f),
                LayoutParams.WRAP_CONTENT);
         double jushang2 =  displayHeight * 0.035 * 2;       	         
         params3.setMargins((int)juzuo2,(int) jushang2, 0, 0);
          
         erweimachangdu_seekbar.setLayoutParams(params3);
         
         LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(
         		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
         double jushang3 =  displayHeight * 0.017 * 2; 
         params4.setMargins(0,(int) jushang3, 0, 0);
         cut.setLayoutParams(params4);
         LinearLayout.LayoutParams params7 = new LinearLayout.LayoutParams(
          		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
          double jushang4 =  displayHeight * 0.015 * 2; 
          params4.setMargins(0,(int) jushang4, 0, 0);
          add.setLayoutParams(params7);
          
         
         LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(
        		 (int) (displayWidth * 0.5f + 0.5f),
                 (int) (displayHeight * 0.1f + 0.5f));
         double juzuo3 = displayWidth * 0.12 * 2;
         params5.setMargins((int)juzuo3, 0, 0, 0);
         //erweimachangdu_default.setTextSize((int)(erweimachangdu_default.getTextSize()*rate));       	         
         erweimachangdu_default.setLayoutParams(params5);
         
         LinearLayout.LayoutParams params6 = new LinearLayout.LayoutParams(
           		 (int) (displayWidth * 0.5f + 0.5f),
                    (int) (displayHeight * 0.1f + 0.5f));
        
            //erweimachangdu_quxiao.setTextSize((int)(erweimachangdu_quxiao.getTextSize()*rate));
            //erweimachangdu_queren.setTextSize((int)(erweimachangdu_queren.getTextSize()*rate));
            erweimachangdu_quxiao.setLayoutParams(params6);
            erweimachangdu_queren.setLayoutParams(params6);

		erweimachangdu_seekbar.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				  
				   case MotionEvent.ACTION_DOWN:
				   {
					   int pos=erweimachangdu_seekbar.getProgress()+100;
					   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos+getString(R.string.bytee));
					   
				    break;
				   }
				   case MotionEvent.ACTION_MOVE:
				   {
					   int pos=erweimachangdu_seekbar.getProgress()+100;
					   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos+getString(R.string.bytee));
					   
				    //�ƶ��¼������ִ�д��������
				    break;
				   }
				   case MotionEvent.ACTION_UP:
				   {
					   int pos=erweimachangdu_seekbar.getProgress()+100;
					   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos+getString(R.string.bytee));
					   
				    break;
				   }
				 
				  
				   default:
					   int pos=erweimachangdu_seekbar.getProgress()+100;
					   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos+getString(R.string.bytee));
					   
				    break;
				   }

				
				return false;
			}
		});
		erweimachangdu_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int pos=erweimachangdu_seekbar.getProgress()+100;
				   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos+getString(R.string.bytee));
				   
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				int pos=erweimachangdu_seekbar.getProgress()+100;
				   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos+getString(R.string.bytee));
				   
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int pos=erweimachangdu_seekbar.getProgress()+100;
				   erweimachangdu_display.setText(getString(R.string.erweimachangdu_content)+pos+getString(R.string.bytee));
				   
				
				
			}
		});

	}
	
	protected void dialog() {
    	
    	AlertDialog.Builder builder = new Builder(QrLengthActivity.this);
    	 
    	 builder.setTitle(this.getString(R.string.tishi));  
    	 builder.setMessage(this.getString(R.string.qrlengthis)+pos+this.getString(R.string.confirmqrlength));
    	
    	 
    	 builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences pres =QrLengthActivity.this.getSharedPreferences("hbol", Context.MODE_PRIVATE);
				SharedPreferences.Editor ed = pres.edit(); 	
				ed.putInt("pagelength", pos);
				 ed.commit();
				 ini.pagelength=pos;				
				dialog.dismiss();
				QrLengthActivity.this.finish();	
			}
		});
    	 builder.setNegativeButton(this.getString(R.string.quxiao), new  OnClickListener(){
 			@Override
 			public void onClick(DialogInterface dialog, int which) {		
 				dialog.dismiss(); 
 				return;
 					
 			}
 		});
    	builder.create().show();
    }
}
