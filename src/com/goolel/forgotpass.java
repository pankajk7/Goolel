package com.goolel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.goolel.Entities.ResponseData;
import com.goolel.utils.AlertMessage;
import com.goolel.utils.Constants;
import com.goolel.webservices.RestWebservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class forgotpass extends AppCompatActivity {
	 EditText txtemail;
	  Toolbar toolbar;
	  Boolean exist;
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.forgotpass);	      
			toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setIcon(R.drawable.ic_forgot_password);	
			getSupportActionBar().setTitle("Forgot Password");
	        Button  b1=(Button)findViewById(R.id.send);
	       /* Button  b2=(Button)findViewById(R.id.lreturn);*/

	        b1.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	
	            	txtemail = (EditText)findViewById(R.id.txtemail);
	            	String Email = txtemail.getText().toString().trim();
	            	 String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
	            	  if(Email.toString().equalsIgnoreCase(""))
	            	  { new AlertMessage(forgotpass.this)
						.showAToast(R.string.error_email_empty);
				      return; 

	            	  
	            	  }
	            	  else if (!Email.matches(emailPattern) && Email.length() > 0)
	                 { 
	            		 new AlertMessage(forgotpass.this)
							.showAToast(R.string.error_email_not_valid);
					      return; 
					   }
	            	 
	            	  else
	            	  {
	            		checkemail();
	            		 // if(exist)
	            		//forgotpwd();
	            		 
	            		 
	            	  }
	            }
	        });
	      /*  b2.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	 Intent intent = new Intent(forgotpass.this, MainActivity.class);
	                  startActivity(intent);
	            }
	            });
*/	            
	        
	 
	        }
	public void forgotpwd()
	{
		 JsonObject jsonObject = new JsonObject();
		  
			jsonObject.addProperty("email", txtemail.getText().toString().trim());
			Log.d("Data==>", jsonObject.toString());	            			
				new RestWebservices(forgotpass.this) {
					public void onSuccess(String data,
							com.restservice.HttpResponse response) {
						try {
							ResponseData obj = new Gson().fromJson(data,
									ResponseData.class);
							if (obj.getStatus().equalsIgnoreCase("ok")) {
								 new AlertMessage(forgotpass.this)
									.showAToast(R.string.sent_password);
							    
								Intent intent = new Intent(forgotpass.this,
										MainActivity.class);
								startActivity(intent);
								finish();
							} else {
								Toast.makeText(forgotpass.this, obj.getError(),
										Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.serviceCall(Constants.API_FORGOT_PASSWORD, "", jsonObject, true);
			
	}
	
private void checkemail() {

			JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("email", txtemail.getText().toString()
		.trim());



Log.d("Data==>", jsonObject.toString());
new RestWebservices(forgotpass.this) {
	public void onSuccess(String data,
			com.restservice.HttpResponse response) {
		try {
			ResponseData obj = new Gson().fromJson(data,
					ResponseData.class);
			if (obj.getStatus().equalsIgnoreCase("1")) {
				exist = true;
				forgotpwd();
			      //return; 
				return;
			} else {
				exist = false;
				 new AlertMessage(forgotpass.this)
					.showAToast(R.string.error_email_notfound);
				 txtemail.setText(" ");
				 txtemail.requestFocus();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	};
}.serviceCall(Constants.API_EMAIL_DUPLICATE, "", jsonObject, true);

}

	
			
		
	 
}
