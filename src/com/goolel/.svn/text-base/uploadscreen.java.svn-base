package com.goolel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.goolel.Adapters.VideoListAdapter;
import com.goolel.Adapters.VideoListAdapter.ProgressUpdate;
import com.goolel.Entities.ResponseData;
import com.goolel.Entities.VideoList;
import com.goolel.utils.AlertMessage;
import com.goolel.utils.Constants;
import com.goolel.utils.DevicePreferences;
import com.goolel.utils.ImagePath;
import com.goolel.webservices.RestWebservices;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.ThumbnailUtils;
import android.net.Uri;

public class uploadscreen extends AppCompatActivity {
	public static final int REQUEST_CODE = 3;
	public ArrayList<VideoList> CustomListViewValues;

	Toolbar toolbar;
	TextView txtnotfound;
	ListView listView;
	ArrayList<String> arrayList;
	VideoListAdapter adapter;
	long totalSize = 0;
	EditText txtsearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uploadscreen);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_logo);
		getSupportActionBar().setTitle("Goolel News");			
		listView = (ListView) findViewById(R.id.listview);
		txtnotfound = (TextView) findViewById(R.id.txt_record_nofound);
		txtnotfound.setVisibility(View.GONE);
		init();
		getVideoList();

	}

	private void init() {
		CustomListViewValues = new ArrayList<VideoList>();
		adapter = new VideoListAdapter(uploadscreen.this, CustomListViewValues);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				VideoList objVideoList = (VideoList) data
						.getSerializableExtra(Constants.PARAMS_INTENT_VIDEO_LIST);
				Bitmap bitmap = (Bitmap) data.getParcelableExtra("image");
				objVideoList.setImage(bitmap);
				CustomListViewValues.add(objVideoList);
				adapter.notifyDataSetChanged();
			}
		}
	}

	private void getVideoList() {
		String extraParameters = new DevicePreferences().getString(
				uploadscreen.this, Constants.PREF_USER_ID, "");
		new RestWebservices(uploadscreen.this) {
			public void onSuccess(String data,
					com.restservice.HttpResponse response) {
				try {
					VideoList.Video obj = new Gson().fromJson(data,
							VideoList.Video.class);
					VideoList[] array = obj.getList();
					if (array.length != 0) {
						for (VideoList videoList : array) {
							Bitmap thumbnail = ThumbnailUtils
									.createVideoThumbnail(
											videoList.getImage_path(),
											MediaStore.Images.Thumbnails.MINI_KIND);
							videoList.setImage(thumbnail);
							CustomListViewValues.add(videoList);
							txtnotfound.setVisibility(View.GONE);

						}
						adapter.notifyDataSetChanged();
					} else {
						txtnotfound.setVisibility(View.VISIBLE);
						return;
					}
				} catch (Exception e) {
					e.printStackTrace();
					new AlertMessage(uploadscreen.this)
							.showAToast(R.string.error_video_list);
				} finally {
					super.onComplete();
				}
			};

			public void onComplete() {

			};
		}.serviceCall(Constants.API_GET_VIDEO_LIST, extraParameters, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (new DevicePreferences().getBoolean(uploadscreen.this,
				Constants.PREF_NOTIFICATION, true)) {			
			getMenuInflater().inflate(R.menu.main, menu);
			getMenuInflater().inflate(R.menu.help, menu);
			getMenuInflater().inflate(R.menu.notification, menu);
		} else {
			getMenuInflater().inflate(R.menu.main, menu);
		}
		return true;
	}

	@Override
	protected void onResume() {
		invalidateOptionsMenu();
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_addVideo) {
			onAddVideoClick();
			return true;
		} else if (id == R.id.action_settings) {
			onSettingsClick();
			return true;
		}else if(id==R.id.action_help){
			onHelpClick();
			return true;
		}
		else if (id == R.id.action_logout) {
			finish();
			return true;
		} else if (id == R.id.action_searchVideo) {
			onSearchClick();
			return true;
		} else if (id == R.id.notification) {
			Intent intent = new Intent(uploadscreen.this,
					NotificationActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onAddVideoClick() {
		Intent intent = new Intent(uploadscreen.this, AddVideoActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}

	private void onSettingsClick() {
		Intent intent = new Intent(uploadscreen.this, SettingActivity.class);
		startActivity(intent);
	}
	private void  onHelpClick() {
		Intent intent = new Intent(uploadscreen.this, HelpActivity.class);
		startActivity(intent);
	}

	private void onSearchClick() {
		Intent intent = new Intent(uploadscreen.this, SearchTitleActivity.class);
		startActivityForResult(intent, REQUEST_CODE);
	}
}
