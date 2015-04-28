package com.signer.signer;

import com.signer.signer.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutActivity extends Activity {
	TextView guanyu = null;
	TextView title = null;
	float rate = 0.0f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guanyu);
		title = (TextView)this.findViewById(R.id.title);
		guanyu = (TextView)this.findViewById(R.id.guanyu);
		 DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
       int displayHeight = displayMetrics.heightPixels;

        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                (int) (displayWidth * 0.7f + 0.5f),
                (int) (displayHeight * 0.12f + 0.5f));
        double jushang =  displayHeight * 0.015 * 2;
        double juzuo = displayWidth * 0.22 * 2;
        params0.setMargins((int)juzuo,  (int)jushang, 0, 0);
        
        title.setLayoutParams(params0);
        
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.guanyu, menu);
//		return true;
//	}

}
