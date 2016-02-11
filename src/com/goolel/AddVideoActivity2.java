package com.goolel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.goolel.Entities.ResponseData;
import com.goolel.Entities.UploadDataview;
import com.goolel.Entities.VideoList;
import com.goolel.utils.AlertMessage;
import com.goolel.utils.AndroidMultiPartEntity;
import com.goolel.utils.Constants;
import com.goolel.utils.DevicePreferences;
import com.goolel.utils.ImagePath;
import com.goolel.utils.AndroidMultiPartEntity.ProgressListener;
import com.goolel.webservices.RestWebservices;

import android.R.string;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class AddVideoActivity2 extends AppCompatActivity {

	private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	// directory name to store captured images and videos
	final static String IMAGE_DIRECTORY_NAME = "goolel";
	private static final int VIDEO_PICKER_SELECT = 100;
	private ProgressDialog pDialog;
	Button selectVideoButton, captureVideoButton, uploadVideoButton;
	TextView videopathtext, percentageTextView;
	EditText titletext, descriptionEditText;
	ProgressBar progressBar;
	Boolean exist;
	private Uri fileUri; // file url to store image/video
	VideoList objVideoList = new VideoList();
	long totalSize = 0;
	Bitmap bitmap;
	Toolbar toolbar;
	UploadFileToServer uploadFileToServer;
	long sizeInMb = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_video_layout);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.drawable.ic_video);
		getSupportActionBar().setTitle("Add Video");

		captureVideoButton = (Button) findViewById(R.id.button_addVideo_capture);
		selectVideoButton = (Button) findViewById(R.id.button_addVideo_select);
		videopathtext = (TextView) findViewById(R.id.video_path);
		titletext = (EditText) findViewById(R.id.video_title);
		descriptionEditText = (EditText) findViewById(R.id.video_description);
		uploadVideoButton = (Button) findViewById(R.id.button_addVideo_upload);
		percentageTextView = (TextView) findViewById(R.id.textview_addVideo_percentage);
		progressBar = (ProgressBar) findViewById(R.id.progressBar_addVideo);

		captureVideoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// checktitle("recordvideo");
				recordVideo();
			}
		});
		selectVideoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// checktitle("pickvideo");
				pickVideo();
			}
		});
		uploadVideoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (titletext.getText().toString().equals("")) {
					new AlertMessage(AddVideoActivity2.this).showAToast(R.string.error_title_empty);
					return;
				} else if (titletext.getText().toString().length() > 15) {
					new AlertMessage(AddVideoActivity2.this).showAToast(R.string.error_title_limit);
					return;

				} else if (videopathtext.getText().toString().trim().equals("")) {
					new AlertMessage(AddVideoActivity2.this).showAToast(R.string.error_path_empty);
					return;
				} else {
					if (sizeInMb > 20) {
						new AlertMessage(AddVideoActivity2.this).showAToast("Please select file size less than 20mb.");
					} else {
						checktitle();
					}
				}
			}
		});
	}

	public void recordVideo() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

		// set video quality
		// 1- for high quality video
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		// start the video capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);

	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	/*
	 * c Pick Video
	 */
	private void pickVideo() {
		Intent photoPickerIntent;
		if (Build.VERSION.SDK_INT < 19) {
			photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("video/*");
		} else {
			photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
			photoPickerIntent.setType("video/*");
		}
		startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Video"), VIDEO_PICKER_SELECT);

	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * Receiving activity result method will be called after closing the camera
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
		// generateListView();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if the result is capturing Image
		// fileUri = data.getData();

		if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				File sourceFile = new File(fileUri.getPath());
				long length = sourceFile.length();
				sizeInMb = length / (1024 * 1024);
				if (sizeInMb > 20) {
					new AlertMessage(AddVideoActivity2.this).showAToast("Please select file size less than 20mb.");
					return;
				}
				videopathtext.setText(fileUri.getPath());
				generateListView(true); // true bcoz capturing video
				// previewVideo();
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled recording
				Toast.makeText(getApplicationContext(), "Recording cancelled.", Toast.LENGTH_SHORT).show();
			} else {
				// failed to record video
				Toast.makeText(getApplicationContext(), "Sorry! Failed to record video", Toast.LENGTH_SHORT).show();
			}
		}
		if (requestCode == VIDEO_PICKER_SELECT) {
			if (resultCode == RESULT_OK) {
				fileUri = data.getData();
				File sourceFile = new File(fileUri.getPath());
				long length = sourceFile.length();
				sizeInMb = length / (1024 * 1024);
				if (sizeInMb > 20) {
					new AlertMessage(AddVideoActivity2.this).showAToast("Please select file size less than 20mb.");
					return;
				}
				videopathtext.setText(fileUri.getPath());
				generateListView(false); // true bcoz selecting video
				// previewVideo();
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled recording
				Toast.makeText(getApplicationContext(), "No video selected.", Toast.LENGTH_SHORT).show();
			} else {
				// failed to record video
				Toast.makeText(getApplicationContext(), "Sorry! Failed to pick video", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void generateListView(boolean isCapture) {
		String imagePath;
		if (isCapture) {
			imagePath = fileUri.getPath();
		} else {
			if (fileUri != null) {
				imagePath = ImagePath.getPath(AddVideoActivity2.this, fileUri);
			} else {
				new AlertMessage(AddVideoActivity2.this).showAToast("Can't get  path");
				return;
			}
		}

		Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Images.Thumbnails.MINI_KIND);
		bitmap = thumbnail;
		objVideoList = new VideoList();
		// objVideoList.setImage(thumbnail);
		objVideoList.setImage_path(imagePath);
		objVideoList.setVideo_title(titletext.getText().toString());
	}

	/**
	 * Uploading the file to server
	 */
	private class UploadFileToServer extends AsyncTask<String, Integer, String> {

		ProgressDialog progressDialog;
		HttpClient httpclient;
		HttpPost httppost;

		// boolean isCancelled = false;

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(AddVideoActivity2.this);
			progressDialog.setMessage("Uploading video...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCancelable(false);
			progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					httppost.abort();
					uploadFileToServer.cancel(true);
				}
			});
			progressDialog.show();

			super.onPreExecute();

		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// progressBar.setProgress(progress[0]);
			// percentageTextView.setText(String.valueOf(progress[0]) + "%");
			progressDialog.setProgress(progress[0]);

			if (progress[0] == 100) {
				progressDialog.setMessage("Validating video from server...");
				// progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			}
		}

		@Override
		protected String doInBackground(String... params) {
			String result = uploadFile(params[0]);
			if (isCancelled()) {
				return null;
			} else {
				return result;
			}
		}

		@SuppressWarnings("deprecation")
		private String uploadFile(String filePath) {
			String responseString = null;

			httpclient = new DefaultHttpClient();
			httppost = new HttpPost(Constants.FILE_UPLOAD_URL);

			try {
				AndroidMultiPartEntity entity = new AndroidMultiPartEntity(new ProgressListener() {

					@Override
					public void transferred(long num) {
						publishProgress((int) ((num * 100) / totalSize));
						// int value = (int) ((num * 100) / totalSize);

						// publishProgress((int) ((num / (float) totalSize) *
						// 100));
					}
				});

				File sourceFile = new File(filePath);

				// Adding file data to http body
				entity.addPart("video_file", new FileBody(sourceFile));

				String userId = new DevicePreferences().getString(AddVideoActivity2.this, Constants.PREF_USER_ID, "");
				entity.addPart("user_id", new StringBody(userId));
				entity.addPart("detail", new StringBody(descriptionEditText.getText().toString()));
				entity.addPart("title", new StringBody(titletext.getText().toString()));
				entity.addPart("image_path", new StringBody(filePath));

				totalSize = entity.getContentLength();
				entity.addPart("video_size", new StringBody(Long.toString(totalSize)));
				httppost.setEntity(entity);

				// Making server call
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity r_entity = response.getEntity();

				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					// Server response
					responseString = EntityUtils.toString(r_entity);
				} else {
					responseString = "Error occurred! Http Status Code: " + statusCode;
				}

			} catch (ClientProtocolException e) {
				responseString = e.toString();
			} catch (IOException e) {
				responseString = e.toString();
			}

			return responseString;

		}

		@Override
		protected void onCancelled(String result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
			showAlert("Cancelled uploading or Network error occur.", percentageTextView, progressBar, false);
		}

		@Override
		protected void onPostExecute(String result) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			// new AlertMessage(AddVideoActivity.this)
			// .showAToast(result);
			if (result != null) {
				Object json;
				try {
					json = new JSONTokener(result).nextValue();
					if (json instanceof JSONObject) {
						JSONObject jsonObject = (JSONObject) json;
						if (jsonObject.has("status")) {
							if (jsonObject.getString("status").equalsIgnoreCase("ok")) {
								objVideoList.setVideo_id(jsonObject.getString("id"));
								showAlert("File is uploaded.", percentageTextView, progressBar, true);
							} else {
								showAlert("Network error.", percentageTextView, progressBar, false);
							}
						} else {
							showAlert("Network error.", percentageTextView, progressBar, false);
						}
					} else {
						showAlert("Network error occur.", percentageTextView, progressBar, false);
					}
				} catch (JSONException e) {
					showAlert("Network error occur.", percentageTextView, progressBar, false);
				} finally {
					uploadVideoButton.setEnabled(true);
				}
			} else {
				showAlert("Cancelled uploading.", percentageTextView, progressBar, false);
			}

			super.onPostExecute(result);
		}

	}

	private void showAlert(String message, final TextView percentageTextView, final ProgressBar progressBar,
			final boolean isSuccess) {
		AlertDialog.Builder builder = new AlertDialog.Builder(AddVideoActivity2.this);
		builder.setMessage(message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				uploadVideoButton.setEnabled(true);
				if (isSuccess) {
					Intent intent = getIntent();
					objVideoList.setVideo_title(titletext.getText().toString().trim());
					objVideoList.setVideo_details(descriptionEditText.getText().toString().trim());
					intent.putExtra(Constants.PARAMS_INTENT_VIDEO_LIST, objVideoList);
					intent.putExtra("image", bitmap);
					setResult(RESULT_OK, intent);
					finish();
				}
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void checktitle() {
		String extraParameters = new DevicePreferences().getString(AddVideoActivity2.this, Constants.PREF_USER_ID, "");
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("user_id", extraParameters);

		jsonObject.addProperty("title", titletext.getText().toString().trim());

		Log.d("Data==>", jsonObject.toString());
		new RestWebservices(AddVideoActivity2.this) {
			public void onSuccess(String data, com.restservice.HttpResponse response) {
				try {
					ResponseData obj = new Gson().fromJson(data, ResponseData.class);
					if (obj.getStatus().equalsIgnoreCase("1")) {
						exist = true;
						Log.d("Title exist: ", "true");
						new AlertMessage(AddVideoActivity2.this).showAToast(R.string.error_title_exists);
						titletext.setText(" ");
						titletext.requestFocus();
						// return;
					} else {
						exist = false;
						Log.d("Title exist: ", "true");
						uploadVideoButton.setEnabled(false);

						uploadFileToServer = new UploadFileToServer();
						uploadFileToServer.execute(objVideoList.getImage_path());
					}
				} catch (Exception e) {
					Log.d("Title: ", "got exception - " + e.getMessage());
					e.printStackTrace();
				}
			};
		}.serviceCall(Constants.API_VIDEO_TITLE_DUPLICATE, "", jsonObject, true);

	}

}
