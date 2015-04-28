package com.signer.signer;


import com.signer.db.CoinClass;
import com.signer.db.CoindbService;
import com.signer.signer.R;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ShowAddressActivity extends Activity {	
	ImageView qrImgImageView;
	 TextView title = null;
	 float rate = 0.0f;
	 EditText address;	 
	SignerApplication app;
	String coinType;	
	private TextView info;	
	LinearLayout switchcoin = null;
	ImageView cointype_icon = null;
	boolean is_all_bz = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showaddress);
		app=(SignerApplication)this.getApplication();
		title = (TextView)this.findViewById(R.id.title);
		address = (EditText)this.findViewById(R.id.addresstext);		
		qrImgImageView = (ImageView) this.findViewById(R.id.qrimg);
		coinType=app.currentCoinType;
		cointype_icon = (ImageView)this.findViewById(R.id.bz_icon);
		cointype_icon.setImageBitmap(app.getCoinIconBmp(coinType));	
		address.setText(app.getAddressString(coinType));//getString(R.string.shoukuandizhi)+
		showqr(0);
		 DisplayMetrics displayMetrics = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;

         rate = (float)(displayWidth/480);
         if (rate<0.6)
        	 rate=(float)0.6;
         LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                 (int) (displayWidth * 0.7f + 0.5f),
                 (int) (displayHeight * 0.08f + 0.5f));
         double jushang =  displayHeight * 0.03 * 2;
         double juzuo = displayWidth * 0.2 * 2;
         params0.setMargins((int)juzuo,  (int)jushang, 0, 0);
         //title.setTextSize((int)(title.getTextSize()*rate));
         title.setLayoutParams(params0);
         
         
//         LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
//                 (int) (displayWidth * 0.7f + 0.5f),
//                 (int) (displayHeight * 0.06f + 0.5f));
//         
//         
//         double jushang2 =  displayHeight * 0.04 * 2;
         double juzuo2 = displayWidth * 0.02 * 2;
//         
//         params1.setMargins((int)juzuo2,   2, 0, 0);
//         //mubiaodizhi_bt.setTextSize((int)(mubiaodizhi_bt.getTextSize()*rate));
//         address.setLayoutParams(params1);
         
         LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                 (int) (displayWidth),
                LayoutParams.WRAP_CONTENT);
                	         
         params2.setMargins(0,   0, 0, 0);
         //mubiaodizhi.setTextSize((int)(mubiaodizhi.getTextSize()*rate));
         address.setLayoutParams(params2);
         
         
         
         
         
         
         
         LinearLayout.LayoutParams params6 = new LinearLayout.LayoutParams(
                 (int) (displayWidth),
                 (int) (displayWidth));
         //double juzuo3 = displayWidth * 0.06 * 2;       	         
         params6.setMargins(0,   0, 0, 0);
         qrImgImageView.setLayoutParams(params6);	
         switchcoin = (LinearLayout)this.findViewById(R.id.switchcoin);
 		
	       switchcoin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ShowAddressActivity.this, SwtichCointypeActivity.class);
				intent.putExtra("cointype", coinType);
				intent.putExtra("all", false);
				startActivityForResult(intent, 100);
			}
		});
		
		
		
	}
	

	
	public void showqr(long value){
		CoindbService cs=new CoindbService(this); 
		CoinClass cc=cs.find(coinType);
		 String qrstring="";
		try {
			if (value==0){
				qrstring = cc.coinName[0].toLowerCase()+":"+app.getAddressString(coinType);
			}else{
				qrstring = cc.coinName[0].toLowerCase()+":"+app.getAddressString(coinType)+"?amount="+String.valueOf(value/Math.pow(10, cc.digits_after_dot));
			}
			Log.println(3,"signer","qrstring:"+qrstring);
		} catch (Exception e1) {			
			e1.printStackTrace();
		}
	        // qrImgImageView = (ImageView) this.findViewById(R.id.qrimg);
	         try {	     		
					Bitmap qrCodeBitmap = EncodingHandler.createQRCode(qrstring, 240);
					qrImgImageView.setImageBitmap(qrCodeBitmap);
				} catch (WriterException e) {			
				}
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==100){//cointype change

			if (resultCode==RESULT_OK)
			{
				coinType = data.getStringExtra("result");				
				app.currentCoinType=coinType;
				address.setText(app.getAddressString(coinType));				
				cointype_icon.setImageBitmap(app.getCoinIconBmp(coinType));
				showqr(0);				
			}
			
		}
		
	}
}
