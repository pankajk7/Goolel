package com.goolel;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.goolel.Adapters.NotificationListAdapter;
import com.goolel.Adapters.VideoListAdapter;
import com.goolel.Entities.NotifyList;
import com.goolel.Entities.VideoList;
import com.goolel.utils.Constants;
import com.goolel.utils.DevicePreferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

public class NotificationActivity extends AppCompatActivity {
	Toolbar toolbar;
	ArrayList<NotifyList> notifyArrayList;
	NotificationListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_notification);
		getSupportActionBar().setTitle("Notification");
		String extraParameters = new DevicePreferences().getString(
				NotificationActivity.this, Constants.PREF_NOTIFICATION, "");

		ListView listView = (ListView) findViewById(R.id.list_notification);
		
		String messagesJsonArray = new DevicePreferences().getString(NotificationActivity.this, Constants.PREF_NOTIFICATION_MESSAGE, "");
		NotifyList[] array = new Gson().fromJson(messagesJsonArray, NotifyList[].class);
		notifyArrayList = new ArrayList<NotifyList>(Arrays.asList(array));
		// notification.setList(list)
		// arraylist.add("http://www.androidhive.info/2012/10/android-push-notifications-using-google-cloud-messaging-gcm-php-and-mysql/");
		// arraylist.add(extraParameters);
		// arraylist.add("Notification text");
		// arraylist.add("Notification text");
		adapter = new NotificationListAdapter(NotificationActivity.this,
				notifyArrayList);
		listView.setAdapter(adapter);
	}
}