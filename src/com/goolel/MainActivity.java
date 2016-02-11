package com.goolel;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.goolel.Entities.ResponseData;
import com.goolel.utils.AlertMessage;
import com.goolel.utils.ConnectionDetector;
import com.goolel.utils.Constants;
import com.goolel.utils.ServerUtilities;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import com.goolel.utils.DevicePreferences;
import com.goolel.webservices.RestWebservices;
import static com.goolel.utils.CommonUtilities.SERVER_URL;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.NameValueTable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MainActivity extends AppCompatActivity {
	EditText usernameEditText, passwordEditText;
	String regId = "";
	// Connection detector
	ConnectionDetector cd;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().hide();

		progressDialog = new ProgressDialog(MainActivity.this);
		progressDialog.setCancelable(false);

		usernameEditText = (EditText) findViewById(R.id.usernameET);
		passwordEditText = (EditText) findViewById(R.id.passwordET);

		Button b1 = (Button) findViewById(R.id.loginBtn);
		Button b2 = (Button) findViewById(R.id.SignUpBtn);
		TextView b3 = (TextView) findViewById(R.id.forpass);

		usernameEditText.setText("goolel@gmail.com");
		passwordEditText.setText("password");

		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				new DevicePreferences().addKey(MainActivity.this,
						Constants.PREF_USER_ID, "9");
				Intent intent = new Intent(MainActivity.this,
						uploadscreen.class);
				startActivity(intent);
				finish();
				
//				if (usernameEditText.getText().toString().equalsIgnoreCase("")) {
//					new AlertMessage(MainActivity.this)
//							.showAToast(R.string.error_email_empty);
//					return;
//				} else if (validateEmail(usernameEditText)) {
//					new AlertMessage(MainActivity.this)
//							.showAToast(R.string.error_email_not_valid);
//					return;
//				} else if (passwordEditText.getText().toString().equals("")) {
//					new AlertMessage(MainActivity.this)
//							.showAToast(R.string.error_password_empty);
//					return;
//				} else {
//					// checkemail();
//
//				}
//
//				cd = new ConnectionDetector(MainActivity.this);

				// Check if Internet present
//				if (!cd.isConnectingToInternet()) {
//					// Internet Connection is not present
//					new AlertMessage(MainActivity.this)
//							.showAToast("Internet Connection Error");
//					// stop executing code by return
//					return;
//				}
//
//				GCMRegistrar.checkDevice(MainActivity.this);
//				// GCMRegistrar.checkManifest(MainActivity.this);
//
//				// lblMessage = (TextView) findViewById(R.id.lblMessage);
//				registerReceiver(mHandleMessageReceiver, new IntentFilter(
//						Constants.DISPLAY_MESSAGE_ACTION));
//
//				// Get GCM registration id
//				regId = GCMRegistrar.getRegistrationId(MainActivity.this);
//				if (regId.equals("")) {
//					// Registration is not present, register now with GCM
//					progressDialog.show();
//					GCMRegistrar.register(MainActivity.this,
//							Constants.SENDER_ID);
//					// RegId = GCMRegistrar
//					// .getRegistrationId(MainActivity.this);
//					Toast.makeText(MainActivity.this, "Registered",
//							Toast.LENGTH_LONG).show();
//				} else {
//					Log.d("RegId=====>", regId);
//					regId = GCMRegistrar.getRegistrationId(MainActivity.this);
//					doLogin(true);
//				}
			}

			// Intent intent = new Intent(MainActivity.this,
			// uploadscreen.class);
			// startActivity(intent);
			// finish();

		});

		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, signup.class);
				startActivity(intent);
				usernameEditText.setText("");
				passwordEditText.setText("");
			}
		});
		b3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, forgotpass.class);
				startActivity(intent);
				// usernameEditText.setText("");
				passwordEditText.setText("");
				passwordEditText.requestFocus();
			}
		});

	}

	/**
	 * Receiving push messages
	 * */
	public final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(
					Constants.EXTRA_MESSAGE);
			Toast.makeText(MainActivity.this, "Reg_Id: " + newMessage,
					Toast.LENGTH_LONG).show();
			regId = newMessage;
			doLogin(false);
		}
	};

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	public Boolean validateEmail(TextView emailTextView) {
		String email1 = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+\\.+[a-z]+";
		String email2 = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.+[a-z]+";
		String emailId = emailTextView.getText().toString().trim();

		if (!((emailId.matches(email1)) || (emailId.matches(email2)))) {
			return true;
		}
		return false;
	}

	private void doLogin(boolean showLoading) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("email", usernameEditText.getText().toString()
				.trim());
		jsonObject.addProperty("pwd", passwordEditText.getText().toString()
				.trim());
		jsonObject.addProperty("reg_id", regId);
		Toast.makeText(MainActivity.this, "Reg_Id confirm: " + jsonObject,
				Toast.LENGTH_LONG).show();
		Log.d("Data==>", jsonObject.toString());
		new RestWebservices(MainActivity.this) {
			public void onSuccess(String data,
					com.restservice.HttpResponse response) {
				try {
					new AlertMessage(MainActivity.this).showAToast(data);
					ResponseData obj = new Gson().fromJson(data,
							ResponseData.class);

					if (obj.getStatus().equalsIgnoreCase("ok")) {
						new DevicePreferences().addKey(MainActivity.this,
								Constants.PREF_USER_ID, obj.getId());
						Intent intent = new Intent(MainActivity.this,
								uploadscreen.class);
						startActivity(intent);
						finish();
					} else {
						// Toast.makeText(MainActivity.this, obj.getError(),
						// Toast.LENGTH_LONG).show();
						if (obj.getError().contains("match")) {
							new AlertMessage(MainActivity.this)
									.showAToast(R.string.error_emailpass_incorrect);
						} else {
							new AlertMessage(MainActivity.this).showAToast(obj
									.getError());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

			public void onComplete() {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			};
		}.serviceCall(Constants.API_LOGIN, "", jsonObject, showLoading);

	}

	private void checkemail() {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("email", usernameEditText.getText().toString()
				.trim());

		Log.d("Data==>", jsonObject.toString());
		new RestWebservices(MainActivity.this) {
			public void onSuccess(String data,
					com.restservice.HttpResponse response) {
				try {
					ResponseData obj = new Gson().fromJson(data,
							ResponseData.class);
					if (obj.getStatus().equalsIgnoreCase("1")) {
						// exist = true;
						doLogin(true);
						// return;
						return;
					} else {
						// exist = false;
						new AlertMessage(MainActivity.this)
								.showAToast(R.string.error_email_notfound);
						usernameEditText.setText(" ");
						usernameEditText.requestFocus();
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.serviceCall(Constants.API_EMAIL_DUPLICATE, "", jsonObject, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
