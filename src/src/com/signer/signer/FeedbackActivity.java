package com.signer.signer;





import com.signer.signer.R;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FeedbackActivity extends Activity {
	private ImageView qrImgImageView;
	//private TextView header;
	private TextView info;
	int id=0;
	Button forward;
	Button backward;
	int currentpage=0;
	String[] qrs;
	int totalpages;
	float rate = 0.0f;
	TextView title = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		info=(TextView)this.findViewById(R.id.scaninfo);
		forward = (Button)this.findViewById(R.id.btn_forward);
		backward = (Button)this.findViewById(R.id.btn_backward);
		 qrImgImageView = (ImageView) this.findViewById(R.id.show_qr);
		forward.setOnClickListener(btnListener);
		backward.setOnClickListener(btnListener);
		title = (TextView)this.findViewById(R.id.title);
		Intent intent = this.getIntent();					
		qrs=intent.getExtras().getStringArray("qrs");
		String title1 = intent.getStringExtra("title");
		if (title1!=null)
			title.setText(title1);		
		currentpage=1;
		totalpages=qrs.length;
		showqr();
		
		
		 DisplayMetrics displayMetrics = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         int displayWidth = displayMetrics.widthPixels;
         int displayHeight = displayMetrics.heightPixels;
         
         LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                 (int) (displayWidth * 0.5f + 0.5f),
                 (int) (displayHeight * 0.08f + 0.5f));
         double offsetU =  displayHeight * 0.03 * 2;
         double offsetL = displayWidth * 0.03 * 2;
         params0.setMargins((int)offsetL,  (int)offsetU, 0, 0);
         
         title.setLayoutParams(params0);
         
         LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                 (int) (displayWidth * 0.7f + 0.5f),
                (int) (displayHeight * 0.07f + 0.5f));
                  
         offsetU =  displayHeight * 0.07 * 2;
         double juyou = displayWidth * 0.09 * 2;
         
         params1.setMargins(0,  0, (int)juyou, 0);         
         info.setLayoutParams(params1);
         
         
         LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                 (int) (displayWidth),
                 (int) (displayWidth ));
         qrImgImageView.setLayoutParams(params2);
        
		
	}
	void showqr(){
		info.setText(getString(R.string.scan)+currentpage+"/"+totalpages);
		if (currentpage==1){
			backward.setActivated(false);			
		}
		else{
			backward.setActivated(true);
		}
		if (currentpage==totalpages){			
			forward.setText(getString(R.string.finish));
		}else{
			forward.setText(getString(R.string.forward));
		}
			String qrdata = qrs[currentpage-1];		
	         qrImgImageView = (ImageView) this.findViewById(R.id.show_qr);
	         try {	     		
					Bitmap qrCodeBitmap = EncodingHandler.createQRCode(qrdata, 320);
					qrImgImageView.setImageBitmap(qrCodeBitmap);
				} catch (WriterException e) {			
				}
	}
	OnClickListener btnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v == forward)
			{
				if (currentpage<totalpages){
					currentpage++;
					showqr();
				}else{
					
					Intent resultIntent = new Intent(FeedbackActivity.this,MainActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("result","OK");
					resultIntent.putExtras(bundle);
					FeedbackActivity.this.setResult(RESULT_OK, resultIntent);					
					FeedbackActivity.this.finish();	
				}
			}
			if(v == backward)
			{
				if (currentpage>1){
					currentpage--;
					showqr();
				}
				//Intent intent = new Intent(MainActivity.this, SaomiaoActivity.class);
				//startActivity(intent);
			}
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saomiaozhihou, menu);
		return true;
	}

	
}
