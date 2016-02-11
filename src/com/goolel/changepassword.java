package com.goolel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.goolel.Entities.ResponseData;
import com.goolel.utils.AlertMessage;
import com.goolel.utils.Constants;
import com.goolel.utils.DevicePreferences;
import com.goolel.webservices.RestWebservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class changepassword extends AppCompatActivity {
	EditText password;
	 Toolbar toolbar;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	        setContentView(R.layout.changepassword);	       
			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.drawable.ic_change_paswrd_wh);	
			getSupportActionBar().setTitle("Change Password");
	        Button  b1=(Button)findViewById(R.id.passsubmit);
	         password = (EditText)findViewById(R.id.txtnewpass);

	        b1.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	if(password.getText().toString().equals(""))
		           	  {
	            		 new AlertMessage(changepassword.this)
							.showAToast(R.string.error_password_empty);
					      return; 

		           		 }
	            	 else if(password.getText().length() > 10)
		           	  {
	            		 new AlertMessage(changepassword.this)
							.showAToast(R.string.error_password_limit);
					      return; 
		           		  }
	            	 else{
	                        change();
	            	 }
	            }
	        });
	 
	        
	 }
	 public void change() {
		 JsonObject jsonObject = new JsonObject();
		 String extraParameters = new DevicePreferences().getString(changepassword.this, Constants.PREF_USER_ID, "");
			
			jsonObject.addProperty("pwd", password.getText().toString().trim());
			jsonObject.addProperty("user_id", extraParameters);
			Log.d("Data==>", jsonObject.toString());	            			
				new RestWebservices(changepassword.this) {
					public void onSuccess(String data,
							com.restservice.HttpResponse response) {
						try {
							ResponseData obj = new Gson().fromJson(data,
									ResponseData.class);
							if (obj.getStatus().equalsIgnoreCase("ok")) {
								Intent intent = new Intent(changepassword.this,
										uploadscreen.class);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(changepassword.this, obj.getError(),
										Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.serviceCall(Constants.API_CHANGE_PASSWORD, "", jsonObject, true);
			}
}
