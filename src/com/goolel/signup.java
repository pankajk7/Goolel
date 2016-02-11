package com.goolel;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.goolel.Entities.ResponseData;
import com.goolel.utils.AlertMessage;
import com.goolel.utils.Constants;
import com.goolel.webservices.RestWebservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class signup extends AppCompatActivity {
	 EditText name,password,email,phone,id;
	   Toolbar toolbar;
       Spinner s;
       Boolean exist ;
       String txtspinner;
	  // private String array_spinner[];

	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.signup);
	        List<String> array_spinner = new ArrayList<String>();
	        array_spinner.add("ID");
	        array_spinner.add("PAN Card");
	        array_spinner.add("Adhar Card");
	        array_spinner.add("Licence");
	        array_spinner.add("Passport");
	       
	         s = (Spinner) findViewById(R.id.spinner1);
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	        		android.R.layout.simple_spinner_item, array_spinner);
	        	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        	s.setAdapter(dataAdapter);

			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.drawable.ic_user);
			getSupportActionBar().setTitle("Registration");
	        Button  b1=(Button)findViewById(R.id.submit);
	        name = (EditText)findViewById(R.id.fname);
	        password = (EditText)findViewById(R.id.pass);
	        email = (EditText)findViewById(R.id.email);
	        phone = (EditText)findViewById(R.id.number);
	        txtspinner=s.getSelectedItem().toString();
	        id = (EditText)findViewById(R.id.idproof);
	        email.setOnFocusChangeListener(new OnFocusChangeListener() {
	        	@Override
	        	public void onFocusChange(View v, boolean hasFocus) {
	        	    if(hasFocus){
	        	        //Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
	        	    }else {
	        	    	if(email.getText().toString().trim().length() >0 )
	        	       checkemail();
	        	    		//Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
	        	    }
	        	   }
	        	});

	        b1.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	 String Name = name.getText().toString();
	            	 String Email = email.getText().toString();
	            	 String mobile = phone.getText().toString();
	            	 String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	            	 if ( Name.length() == 0 )
	            	 {
	            		 new AlertMessage(signup.this)
							.showAToast(R.string.error_form_incomplete);
					      return; 
					}
	            	 else if ( Name.length() > 25)
	                 { 
	            		 new AlertMessage(signup.this)
							.showAToast(R.string.error_username_limit);
					      return; 

	                 }
	            	 else if(password.getText().toString().equals(""))
		           	  {
	            		 new AlertMessage(signup.this)
							.showAToast(R.string.error_form_incomplete);
					      return; 

		           		 }
	            	 else if(password.getText().length() > 6)
		           	  {
	            		 new AlertMessage(signup.this)
							.showAToast(R.string.error_password_limit);
					      return; 
		           		  }
	            	 else if (!Email.matches(emailPattern) && Email.length() > 0)
	                 { 
	            		 new AlertMessage(signup.this)
							.showAToast(R.string.error_email_not_valid);
					      return; 
					   }
	            	 else if (Email.length() == 0 || Email == "")
	                 { 
	            		 new AlertMessage(signup.this)
							.showAToast(R.string.error_form_incomplete);
					      return; 
					   }
	            	
	            	 else if(mobile.length() == 0 )
	            	 { 
	            		 new AlertMessage(signup.this)
						.showAToast(R.string.error_form_incomplete);
				      return; 
				      }
	            	 else if(!isValidMobile(mobile)|| phone.getText().toString().trim().length() != 10 )
	            	 {
	            		 new AlertMessage(signup.this)
							.showAToast(R.string.error_phone_not_valid);
					      return; 
	            	 }
	            	 else if(s.getSelectedItem().toString().equals("ID")){
	            		 new AlertMessage(signup.this)
	        				.showAToast(R.string.error_form_incomplete);
	        		      return; 
	            	 }
	            	 else if(id.length() == 0)	 
	            	 {
	            		 new AlertMessage(signup.this)
							.showAToast(R.string.error_form_incomplete);
					      return; 
					      }	
	            	 else{
	            		//checkemail();
	            		//if(!exist)
	            	register();
	            	 }	
	         
	            }
	        });
	 
	        }
	 private void register() {

						JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("email", email.getText().toString()
					.trim());
			jsonObject.addProperty("pwd", password.getText().toString()
					.trim());
			jsonObject.addProperty("name", name.getText().toString()
					.trim());
			jsonObject.addProperty("mobile", phone.getText().toString()
					.trim());
			String id_prf =s.getSelectedItem().toString()+"_"+ id.getText().toString();
			jsonObject.addProperty("id_proof", id_prf.trim());

			Log.d("Data==>", jsonObject.toString());
			new RestWebservices(signup.this) {
				public void onSuccess(String data,
						com.restservice.HttpResponse response) {
					try {
						ResponseData obj = new Gson().fromJson(data,
								ResponseData.class);
						if (obj.getStatus().equalsIgnoreCase("ok")) {
							Intent intent = new Intent(signup.this,
									MainActivity.class);
							startActivity(intent);
							 new AlertMessage(signup.this)
								.showAToast(R.string.confirm_register);
						     // return;  
							finish();
							return;
						} else {
							Toast.makeText(signup.this, obj.getError(),
									Toast.LENGTH_LONG).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				};
			}.serviceCall(Constants.API_REGISTER, "", jsonObject, true);

		}
	 private void checkemail() {

			JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("email", email.getText().toString()
		.trim());



Log.d("Data==>", jsonObject.toString());
new RestWebservices(signup.this) {
	public void onSuccess(String data,
			com.restservice.HttpResponse response) {
		try {
			ResponseData obj = new Gson().fromJson(data,
					ResponseData.class);
			if (obj.getStatus().equalsIgnoreCase("1")) {
				exist = true;
				 new AlertMessage(signup.this)
					.showAToast(R.string.error_email_exists);
				 email.setText(" ");
				 email.requestFocus();
			      //return; 
				return;
			} else {
				exist = false;
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
}.serviceCall(Constants.API_EMAIL_DUPLICATE, "", jsonObject, true);

}
	 private boolean isValidMobile(String phone) 
	 {
	     return android.util.Patterns.PHONE.matcher(phone).matches();   
	 }
	 
}
