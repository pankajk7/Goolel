package com.goolel;

import com.goolel.utils.AlertMessage;
import com.goolel.utils.Constants;
import com.goolel.utils.DevicePreferences;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
	 EditText txtemail;
	 Toolbar toolbar;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.setting_layout);
	        
			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.drawable.ic_setting);
			getSupportActionBar().setTitle("Settings");
	        TextView editprofile=(TextView)findViewById(R.id.btnSetting_ediprofile);
	        TextView  changepwd=(TextView)findViewById(R.id.btnSetting_changepassword);
	        TextView  signout=(TextView)findViewById(R.id.btnSetting_notification);	       
	        Switch swithcNotification= (Switch)findViewById(R.id.switch_notification); 
	        editprofile.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	Intent intent = new Intent(SettingActivity.this, EditProfile.class);
	        		
	        		startActivity(intent);
	            }
	        });	       
	        changepwd.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	Intent intent = new Intent(SettingActivity.this, changepassword.class);
					startActivity(intent);
	            }
	        });
	        signout.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	finish();
	            }
	        });
	        swithcNotification.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	        	   @Override
	        	   public void onCheckedChanged(CompoundButton buttonView,
	        	     boolean isChecked) {

	        	    if(isChecked){
	        	 //    signout.setText("Switch is currently ON");
	        	    	
	        	    	new DevicePreferences().addKey(SettingActivity.this, Constants.PREF_NOTIFICATION, true);
	        	    }else{
	        	    	new DevicePreferences().addKey(SettingActivity.this, Constants.PREF_NOTIFICATION, false);
	        	 //    signout.setText("Switch is currently OFF");
	        	    }

	        	   }
	        	  });
	        
	        if (new DevicePreferences().getBoolean(SettingActivity.this,
					Constants.PREF_NOTIFICATION, true)) {
	        	swithcNotification.setChecked(true);
	        }else{
	        	swithcNotification.setChecked(false);
	        }
	       
	 }
}