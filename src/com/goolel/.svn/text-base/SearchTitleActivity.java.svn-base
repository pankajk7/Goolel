package com.goolel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.goolel.Adapters.VideoListAdapter;
import com.goolel.Entities.VideoList;
import com.goolel.utils.AlertMessage;
import com.goolel.utils.Constants;
import com.goolel.utils.DevicePreferences;
import com.goolel.webservices.RestWebservices;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;



public class SearchTitleActivity extends AppCompatActivity {
	public static final int REQUEST_CODE = 3;
	RadioGroup radiogroupSearch;
	Toolbar toolbar;
	EditText searchEditText,datefrom,dateto;
	Button searchButton,advancesearch;
	ListView listView;
	TextView txtnotfound;
	Calendar myCalendar;
	ArrayList<VideoList> videoArrayList;
	VideoListAdapter adapter;
	DatePickerDialog.OnDateSetListener dateFromDialog, dateToDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_title);	
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_search);
		getSupportActionBar().setTitle("Search");
		init();
		
		findViews();
		listeners();
		setAdapter();
		//getVideoList();

	}

				
		
				
	
	

	
	private void init() {
		videoArrayList = new ArrayList<VideoList>();
		//adapter = new VideoListAdapter(SearchTitleActivity.this, videoArrayList);
		//listView.setAdapter(adapter);
	}
	private void findViews() {
		// titleRadioButton = (RadioButton)
		// findViewById(R.id.radioButton_search_title);
		// dateRadioButton = (RadioButton)
		// findViewById(R.id.radioButton_search_date);
		searchEditText = (EditText) findViewById(R.id.editText_search1);
		searchButton = (Button) findViewById(R.id.button_search1);
		radiogroupSearch = (RadioGroup)findViewById(R.id.radiogroup_search);
		datefrom = (EditText) findViewById(R.id.text_from_date1);
		dateto = (EditText) findViewById(R.id.text_to_date1);
		advancesearch = (Button) findViewById(R.id.button_search_date1);
		listView = (ListView) findViewById(R.id.list_video);
txtnotfound =(TextView) findViewById(R.id.txt_record_nofound);
	}

	private void setAdapter() {
		adapter = new VideoListAdapter(SearchTitleActivity.this, videoArrayList);
		listView.setAdapter(adapter);
	}

	private void listeners() {
		radiogroupSearch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton searchTitleRadioButton =(RadioButton)radiogroupSearch.findViewById(R.id.radiobutton_search_title);
				RadioButton searchDateRadioButton =(RadioButton)radiogroupSearch.findViewById(R.id.radiobutton_search_date);
				 LinearLayout advance_search_title_block = (LinearLayout)findViewById(R.id.title_search_block);
			 LinearLayout advance_search_input_block = (LinearLayout)findViewById(R.id.advance_search_block);
			 LinearLayout advance_search_button_block = (LinearLayout)findViewById(R.id.advance_search_button_block);
				
				 boolean isChecked = searchTitleRadioButton.isChecked();
				 if (isChecked)
				    { 
					 advance_search_title_block.setVisibility(View.VISIBLE);
				    	advance_search_input_block.setVisibility(View.GONE);
				    	advance_search_button_block.setVisibility(View.GONE);
				    	txtnotfound.setVisibility(View.GONE);
				    	datefrom.setText("");
				    	dateto.setText("");
				    	init();
						setAdapter();
				    	//advance_search_input_block.setText("");
				    	//advance_search_button_block
					// searchTitleRadioButton.setChecked(false);
				    } 
				    else {
				    	advance_search_title_block.setVisibility(View.GONE);
				    	advance_search_input_block.setVisibility(View.VISIBLE);
				    	advance_search_button_block.setVisibility(View.VISIBLE);
				    	txtnotfound.setVisibility(View.GONE);
				    	searchEditText.setText("");
				    	searchDateRadioButton.setChecked(true);
				    	init();
						setAdapter();
				    }
				 
			}
		});
		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (searchEditText.getText().toString().trim()
						.equalsIgnoreCase("")) {
					new AlertMessage(SearchTitleActivity.this)
							.showAToast(R.string.error_search_empty);
					return;
				}
				

				init();
				setAdapter();
				getVideoListtitle();
			}
		});
		myCalendar = Calendar.getInstance();
		dateFromDialog = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateFromDate();
			}

		};
		dateToDialog = new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				// TODO Auto-generated method stub
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateToDate();
			}

		};
		datefrom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(SearchTitleActivity.this, dateFromDialog,
						myCalendar.get(Calendar.YEAR), myCalendar
								.get(Calendar.MONTH), myCalendar
								.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		dateto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new DatePickerDialog(SearchTitleActivity.this, dateToDialog, myCalendar
						.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
						myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		advancesearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (datefrom.getText().toString().trim().equalsIgnoreCase("")
						|| dateto.getText().toString().trim()
								.equalsIgnoreCase("")) {
					new AlertMessage(SearchTitleActivity.this)
							.showAToast(R.string.error_search_empty_Date);
					return;
				}
				
				init();
				setAdapter();
				getVideoListdate();

							}

		});
	}
	
	private void updateFromDate() {

		String myFormat = "dd-MM-yyyy"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		datefrom.setText(sdf.format(myCalendar.getTime()));
	}

	private void updateToDate() {

		String myFormat = "dd-MM-yyyy"; // In which you need put here
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

		dateto.setText(sdf.format(myCalendar.getTime()));
	}

	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				VideoList objVideoList = (VideoList) data
						.getSerializableExtra(Constants.PARAMS_INTENT_VIDEO_LIST);
				Bitmap bitmap = (Bitmap) data.getParcelableExtra("image");
				objVideoList.setImage(bitmap);
				videoArrayList.add(objVideoList);
				adapter.notifyDataSetChanged();
			}
		}
	}

	private void getVideoList() {
		String extraParameters = new DevicePreferences().getString(
				SearchTitleActivity.this, Constants.PREF_USER_ID, "");
		new RestWebservices(SearchTitleActivity.this) {
			public void onSuccess(String data,
					com.restservice.HttpResponse response) {
				try {
					VideoList.Video obj = new Gson().fromJson(data,
							VideoList.Video.class);
					VideoList[] array = obj.getList();
					for (VideoList videoList : array) {
						Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(
								videoList.getImage_path(),
								MediaStore.Images.Thumbnails.MINI_KIND);
						videoList.setImage(thumbnail);
						videoArrayList.add(videoList);
					}
					adapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
					new AlertMessage(SearchTitleActivity.this)
							.showAToast(R.string.error_video_list);
				} finally {
					super.onComplete();
				}
			};

			public void onComplete() {

			};
		}.serviceCall(Constants.API_GET_VIDEO_LIST, extraParameters, true);
	}
	private void getVideoListtitle() {
		String extraParameters = new DevicePreferences().getString(
				SearchTitleActivity.this, Constants.PREF_USER_ID, "");

		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty(Constants.PARAMS_API_TITLE, searchEditText.getText().toString().trim()
				);

		new RestWebservices(SearchTitleActivity.this) {
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
							videoArrayList.add(videoList);
						}
						adapter.notifyDataSetChanged();
						txtnotfound.setVisibility(View.GONE);

					} else {
						txtnotfound.setText("Video not found.");
						txtnotfound.setVisibility(View.VISIBLE);
						
						return;
					}
				} catch (Exception e) {
					new AlertMessage(SearchTitleActivity.this)
							.showAToast(R.string.error_video_list);
				}
			};
		}.serviceCall(Constants.API_POST_VIDEO_LIST_SEARCH, extraParameters,
				jsonObject, true);
	}
	private void getVideoListdate() {
		String extraParameters = new DevicePreferences().getString(
				SearchTitleActivity.this, Constants.PREF_USER_ID, "");

		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty(Constants.PARAMS_API_FROM_DATE, datefrom.getText().toString().trim());
				
		jsonObject.addProperty(Constants.PARAMS_API_TO_DATE, dateto.getText().toString().trim());
				

		new RestWebservices(SearchTitleActivity.this) {
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
							videoArrayList.add(videoList);
						}
						adapter.notifyDataSetChanged();
						txtnotfound.setVisibility(View.GONE);

					} else {
						txtnotfound.setText("No videos found for the selected period.");
						txtnotfound.setVisibility(View.VISIBLE);
						return;
					}
				} catch (Exception e) {
					new AlertMessage(SearchTitleActivity.this)
							.showAToast(R.string.error_video_list);
				}
			};
		}.serviceCall(Constants.API_POST_VIDEO_LIST_SEARCH, extraParameters,
				jsonObject, true);
	}

	
}