package com.goolel;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.goolel.Entities.ResponseData;
import com.goolel.Entities.User;
import com.goolel.Entities.VideoList;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditProfile extends AppCompatActivity {
	 EditText txtuser,txtemail,txtphone,txtid;
	 Toolbar toolbar;
	 String txtspiner;
	 Spinner s;
	 ArrayAdapter<String> dataAdapter;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.editprofile);	
	        List<String> array_spinner = new ArrayList<String>();
	        array_spinner.add("ID");
	        array_spinner.add("PAN Card");
	        array_spinner.add("Adhar Card");
	        array_spinner.add("Licence");
	        array_spinner.add("Passport");
	       
	         s = (Spinner) findViewById(R.id.editspinner1);
	        dataAdapter = new ArrayAdapter<String>(this,
	        		android.R.layout.simple_spinner_item, array_spinner);
	        	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	s.setAdapter(dataAdapter);
			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.drawable.ic_edit_profile_wh);	
			getSupportActionBar().setTitle("Edit Profile");
	        txtemail =  (EditText)findViewById(R.id.editText_editProfile_email);
	        txtuser =  (EditText)findViewById(R.id.editfname);
	        txtphone =  (EditText)findViewById(R.id.editnumber);
	        txtid =  (EditText)findViewById(R.id.editidproof);
	        txtspiner = s.getSelectedItem().toString(); 
	        Button sub = (Button)findViewById(R.id.editsubmit);
              getdata();
              sub.setOnClickListener(new View.OnClickListener() {
  	            @Override
  	            public void onClick(View v) {
                   edit();
  	            }
              });
           }
	 private void getdata()
	 {
		 String extraParameters = new DevicePreferences().getString(EditProfile.this, Constants.PREF_USER_ID, "");
			new RestWebservices(EditProfile.this){
				public void onSuccess(String data, com.restservice.HttpResponse response) {
					try{
						
						User obj = new Gson().fromJson(data, User.class);
						txtemail.setText(obj.getEmail());
						txtuser.setText(obj.getName());
						txtphone.setText(obj.getMobile());
						String idprf = obj.getId_proof();
						
						String[] parts = idprf.split("_"); // escape .
						String part1 = parts[0];
						String part2 = parts[1];
						int selectionPosition= dataAdapter.getPosition(part1);
						s.setSelection(selectionPosition);
						txtid.setText(part2);
						
					}catch(Exception e){
						e.printStackTrace();
						new AlertMessage(EditProfile.this).showAToast(R.string.error_video_list);
					}
				};
			}.serviceCall(Constants.API_GET_PROFILE, extraParameters, true);
		
	 }
	 private void edit()
		{
		 String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
		 txtspiner = s.getSelectedItem().toString(); 
    	 if ( txtuser.getText().toString().trim().length() == 0 )
    	 {
    		 new AlertMessage(EditProfile.this)
				.showAToast(R.string.error_form_incomplete);
		      return; 
		}
    	 else if ( txtuser.getText().toString().trim().length() > 25)
         { 
    		 new AlertMessage(EditProfile.this)
				.showAToast(R.string.error_username_limit);
		      return; 

         }
    	
    	 else if (!txtemail.getText().toString().trim().matches(emailPattern) && txtemail.getText().toString().trim().length() > 0)
         { 
    		 new AlertMessage(EditProfile.this)
				.showAToast(R.string.error_email_not_valid);
		      return; 
		   }
    	 else if (txtemail.getText().toString().trim().length() == 0 || txtemail.getText().toString().trim() == "")
         { 
    		 new AlertMessage(EditProfile.this)
				.showAToast(R.string.error_form_incomplete);
		      return; 
		   }
    	 else if(txtphone.getText().toString().trim().length() == 0 )
    	 { 
    		 new AlertMessage(EditProfile.this)
			.showAToast(R.string.error_form_incomplete);
	      return; 
	      }
    	 else if(!isValidMobile(txtphone.getText().toString().trim()) || txtphone.getText().toString().trim().length() != 10 )
    	 {
    		 new AlertMessage(EditProfile.this)
				.showAToast(R.string.error_phone_not_valid);
		      return; 
    	 } 
    	 else if(txtspiner.toString().equals("ID")){
    		 new AlertMessage(EditProfile.this)
				.showAToast(R.string.error_form_incomplete);
		      return; 
    	 }
    	 else if(txtid.length() == 0)	 
    	 {
    		 new AlertMessage(EditProfile.this)
				.showAToast(R.string.error_form_incomplete);
		      return; 
		      }	
    	 else{
    		 
    	
		 String extraParameters = new DevicePreferences().getString(EditProfile.this, Constants.PREF_USER_ID, "");
		
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("email", txtemail.getText().toString().trim());
		    jsonObject.addProperty("name", txtuser.getText().toString().trim());
			jsonObject.addProperty("mobile", txtphone.getText().toString().trim());
			String id_prf =s.getSelectedItem().toString()+"_"+ txtid.getText().toString();
			jsonObject.addProperty("id_proof", id_prf.trim());
			jsonObject.addProperty("user_id", extraParameters);
			

			Log.d("Data==>", jsonObject.toString());
			new RestWebservices(EditProfile.this) {
				public void onSuccess(String data,
						com.restservice.HttpResponse response) {
					try {
						ResponseData obj = new Gson().fromJson(data,
								ResponseData.class);
						if (obj.getStatus().equalsIgnoreCase("ok")) {
							new AlertMessage(EditProfile.this).showAToast(R.string.confirm_update);

							Intent intent = new Intent(EditProfile.this,
									uploadscreen.class);
							startActivity(intent);
							finish();
						} else {
							new AlertMessage(EditProfile.this).showAToast(obj.getError());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.serviceCall(Constants.API_EDIT, "", jsonObject, true);
		}
		}
	 private boolean isValidMobile(String phone) 
	 {
	     return android.util.Patterns.PHONE.matcher(phone).matches();   
	 }
}
