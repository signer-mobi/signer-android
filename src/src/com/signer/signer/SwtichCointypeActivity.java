package com.signer.signer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.signer.signer.R;

import com.signer.db.CoinClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SwtichCointypeActivity extends Activity {
	ListView cointype_list = null;
	List<Map<String, Object>> mData = null;
	SignerApplication app;
	boolean allcoin=false;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.switchcointypedialog);
		app=(SignerApplication)this.getApplication();		
		Intent intent=this.getIntent();
		 allcoin=intent.getBooleanExtra("all",false);
		cointype_list = (ListView)this.findViewById(R.id.bizhong_list);
		mData = getCointype();
		cointype_list.setAdapter(new MyAdapter(SwtichCointypeActivity.this));
		cointype_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {			
				
				Intent resultIntent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("result", (String)getCointype().get(position).get("bz"));
				Log.println(3,"signer","cointype diaolog result:"+getCointype().get(position).get("bz"));
				resultIntent.putExtras(bundle);
				SwtichCointypeActivity.this.setResult(RESULT_OK, resultIntent);
				finish();
				
			}
		});
		
	}
    public class MyAdapter extends BaseAdapter{
         private LayoutInflater mInflater;
 
         
        public MyAdapter(Context context){        	
 
            this.mInflater = LayoutInflater.from(context);
 
        }
 
        @Override
 
        public int getCount() {
 
            // TODO Auto-generated method stub
 
            return mData.size();
 
        }
 
        @Override
 
        public Object getItem(int arg0)
        {
 
            // TODO Auto-generated method stub
 
            return null;
 
        }
 
 
 
        @Override
 
        public long getItemId(int arg0)
        {
 
            // TODO Auto-generated method stub
 
            return 0;
 
        }
  
        @Override
 
        public View getView(int position, View convertView, ViewGroup parent) {
             
            ViewHolder holder = null;
 
            if (convertView == null)
            {           
 
                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.bizhong_item, null);
            
                holder.cointypeImg = (ImageView)convertView.findViewById(R.id.bz_tuo);
                holder.cointype = (TextView)convertView.findViewById(R.id.bizhong_text);
                
 
 
                convertView.setTag(holder);
                 
 
            }else {               
 
                holder = (ViewHolder)convertView.getTag();
 
            }
          
 
            holder.cointype.setText((String)mData.get(position).get("bz"));
            holder.cointypeImg.setImageBitmap(app.getCoinIconBmp((String)mData.get(position).get("bz")));
           
  
            return convertView;
 
        }         
 
    }    
    public final class ViewHolder{ 
    	 public ImageView cointypeImg;
        public TextView cointype;
       
    }  
    
	
	List<Map<String, Object>> getCointype(){
		List<Map<String, Object>>data = new ArrayList<Map<String,Object>>();
		Map<String, Object> cointypeMap = new HashMap<String, Object>();
		if (allcoin){
			cointypeMap.put("bz", "ALL");
			data.add(cointypeMap);
		}
		for (int i=0; i<app.coinTypes.length;i++){
			cointypeMap = new HashMap<String, Object>();
			cointypeMap.put("bz", app.coinTypes[i]);			
			data.add(cointypeMap);		
		}	
		
		return data;
	}
	

}
