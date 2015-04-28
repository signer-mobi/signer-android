package com.signer.signer;

import com.google.bitcoin.core.Utils;
import com.signer.signer.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InputPasswordActivity extends Activity {
	//�������뵽ʱ�� ��ֵ 
	String passwd = "";
	String resetpassword="hardbitreset";
	EditText pwd1 = null;
    Button btn_1 = null;
    Button btn_2 = null;
    TextView title=null;
    TextView info=null;
    SignerApplication ini;
    int action;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ini= (SignerApplication)this.getApplication();
		setContentView(R.layout.inputpassword);		
		pwd1 = (EditText)this.findViewById(R.id.pwd1);		
		title=(TextView)this.findViewById(R.id.title);
		info=(TextView)this.findViewById(R.id.info);
		Intent intent = this.getIntent();
		action=intent.getIntExtra("title",0);
		Log.println(3,"signer","inputpwdact.oncreate:title:"+getString(action));
		if (action!=0)
			title.setText(getString(action));
		if (intent.getStringExtra("info")!=null)
			info.setText(intent.getStringExtra("info"));
		btn_1 = (Button)this.findViewById(R.id.btn_1);
		btn_2 = (Button)this.findViewById(R.id.btn_2);
		btn_1.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
			
				if(pwd1.length() < 1 )
				{
					pwd1.setText("");
					
					pwd1.requestFocus();
					Toast.makeText(InputPasswordActivity.this, getString(R.string.input10digits), Toast.LENGTH_SHORT).show();
				}else{
					passwd = pwd1.getText().toString();
					//Toast.makeText(Beifenqianbao2Activity.this, getString(R.string.mimayanzheng), Toast.LENGTH_SHORT).show();	
//					if(action==R.string.getpassword&&ini.verifypassword(passwd)){
//						Intent resultIntent = new Intent(InputPasswordActivity.this,SignerApplication.class);
//						Bundle bundle = new Bundle();
//						bundle.putString("password", passwd);
//						resultIntent.putExtras(bundle);
//						InputPasswordActivity.this.setResult(RESULT_OK, resultIntent);					
//						InputPasswordActivity.this.finish();
//					}
					if(((action==R.string.resetaccount)&&passwd.equals(resetpassword)||ini.verifypassword(passwd))) {
						if (action==R.string.btn_changepwd||action==R.string.getpassword)
							ini.tempstring=passwd;
						Intent resultIntent = new Intent(InputPasswordActivity.this,SignerApplication.class);
						Bundle bundle = new Bundle();
						bundle.putString("result", "passwordok");
						resultIntent.putExtras(bundle);
						InputPasswordActivity.this.setResult(RESULT_OK, resultIntent);					
						InputPasswordActivity.this.finish();
					}
					else{
					pwd1.setText("");					
					pwd1.requestFocus();
					Toast.makeText(InputPasswordActivity.this, getString(R.string.mimacuowu), Toast.LENGTH_SHORT).show();
					}
				}
				
				
			}
		});
		
		btn_2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			pwd1.setText("");
			pwd1.requestFocus();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.beifenqianbao2, menu);
		return true;
	}

}
