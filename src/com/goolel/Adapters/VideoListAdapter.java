package com.goolel.Adapters;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import java.net.URLConnection;
import javax.crypto.spec.PSource;
import android.app.Dialog;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.google.gson.Gson;
import com.goolel.AddVideoActivity;
import com.goolel.R;
import com.goolel.uploadscreen;
import com.goolel.Entities.ListData;
import com.goolel.Entities.UploadDataview;
import com.goolel.Entities.VideoList;
import com.goolel.R.id;
import com.goolel.R.layout;
import com.goolel.utils.AlertMessage;
import com.goolel.utils.AndroidMultiPartEntity;
import com.goolel.utils.Constants;
import com.goolel.utils.DevicePreferences;
import com.goolel.utils.AndroidMultiPartEntity.ProgressListener;
import com.goolel.webservices.RestWebservices;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VideoListAdapter extends BaseAdapter {

	Context context;
	String userid,video;
	List<VideoList> arrayList;
	LayoutInflater inflater;
	long totalSize = 0;
	private ProgressDialog pDialog;
	ImageView my_image;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0;
	private String name = "";
	// File url to download
	private static String file_url = "http://khadikhazana.com/goolel/uploads/video/";

	public interface ProgressUpdate {
		public void setProgress();

		public void onProgressupdate(int progress);

		public void onDone();
	}

	public VideoListAdapter(Context context, List<VideoList> items) {
		this.context = context;
		this.arrayList = items;
		inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}

	public void addItem(VideoList item) {
		arrayList.add(item);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (arrayList != null) {
			return arrayList.size();
		}
		return 0;
	}

	@Override
	public VideoList getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.video_list_item_layout,
					parent, false);
			holder.titleTextView = (TextView) convertView
					.findViewById(R.id.textview_videoListItem_title);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.imageView_videoListItem);
			holder.dateTextView = (TextView) convertView
					.findViewById(R.id.textview_videoListItem_date);
			holder.sizeTextView = (TextView) convertView
					.findViewById(R.id.textview_videoListItem_size);
			holder.percentageTextView = (TextView) convertView
					.findViewById(R.id.textview_videoListItem_percentage);
			holder.downloadButton = (ImageButton) convertView
					.findViewById(R.id.button_videoListItem_download);
			holder.uploadButton = (ImageButton) convertView
					.findViewById(R.id.button_videoListItem_upload);
			holder.deleteButton = (ImageButton) convertView
					.findViewById(R.id.button_videoListItem_delete);
			holder.progressBar = (ProgressBar) convertView
					.findViewById(R.id.progressBar_videoListItem);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		VideoList rowItem = getItem(position);

		if (rowItem.getImage() != null) {
			holder.imageView.setImageBitmap(rowItem.getImage());
		} else {

		}

		holder.titleTextView.setText(rowItem.getVideo_title());
		if (rowItem.getVideo_uploaddate() != null) {
			String dateformatString = context.getResources().getString(
					R.string.video_adapter_upload_date);
			
			holder.dateTextView.setText(
					rowItem.getVideo_uploaddate());
		}

		if (rowItem.getVideo_Size() != null) {
			holder.sizeTextView.setText(rowItem.getVideo_Size());
		} else {
			holder.sizeTextView.setText("");
		}

		if (rowItem.getVideo_id() != null) {
			holder.uploadButton.setEnabled(false);
			holder.deleteButton.setEnabled(true);
		} else {
			holder.uploadButton.setEnabled(true);
			holder.deleteButton.setEnabled(false);
		}

		holder.uploadButton.setTag(new UploadDataview(
				holder.percentageTextView, position, holder.progressBar,
				holder.uploadButton, holder.deleteButton));
		holder.downloadButton.setTag(position);
		holder.deleteButton.setTag(position);

		holder.uploadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UploadDataview obj = (UploadDataview) v.getTag();
				VideoList rowItem = getItem(obj.getPosition());
				obj.getUploadButton().setEnabled(false);
				// new UploadFileToServer(obj.getPercentage(), obj
				// .getProgressBar(), obj.getUploadButton(), obj
				// .getPosition(), obj.getDeleteButton()).execute(rowItem
				// .getImage_path());
			}
		});

		holder.downloadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int index = (Integer) v.getTag();
				VideoList rowItem = getItem(index);
				name = rowItem.getVideo_filename();
				String path = Environment.getExternalStorageDirectory() + "/"
						+ "goolel";
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				}

				if (new File(path + "/" + name).exists()) {
					new AlertMessage(context)
					.showAToast(R.string.adapter_video_exists);
					return ;
				}
				pDialog = new ProgressDialog(context);
				pDialog.setMessage("Downloading video..");
				pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pDialog.setCancelable(false);
				pDialog.show();
				// return pDialog;
				new DownloadFileFromURL(rowItem.getImage_path())
						.execute(file_url + name);
			}
		});

		holder.deleteButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int index = (Integer) v.getTag();
				VideoList rowItem = getItem(index);
				deleteVideo(rowItem.getUser_id(), rowItem.getVideo_id(), index);
			}
		});

		return convertView;
	}

	double roundTo2Decimals(double val) {
		DecimalFormat df2 = new DecimalFormat("###.##");
		return Double.valueOf(df2.format(val));
	}

	public static class ViewHolder {
		public TextView titleTextView, dateTextView, sizeTextView,
				percentageTextView;
		public ImageView imageView;
		public ImageButton uploadButton, downloadButton, deleteButton;
		public ProgressBar progressBar;
	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		String imagePath;

		public DownloadFileFromURL(String imagePath) {
			this.imagePath = imagePath;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			String path = Environment.getExternalStorageDirectory() + "/"
					+ "goolel";
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}

			if (new File(path + "/" + name).exists()) {
				return;
			}
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				String path = Environment.getExternalStorageDirectory() + "/"
						+ "goolel";
				File file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				}

				if (new File(path + "/" + name).exists()) {
					return "";
				}

				URL u = new URL(f_url[0]);
				URLConnection conection = u.openConnection();
				conection.connect();

				int lenghtOfFile = conection.getContentLength();
				// InputStream is = u.openStream();
				InputStream input = new BufferedInputStream(u.openStream(),
						8192);
				DataInputStream dis = new DataInputStream(input);

				byte[] buffer = new byte[1024];
				long total = 0;
				FileOutputStream fos = new FileOutputStream(new File(path + "/"
						+ name));
				while ((count = dis.read(buffer)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));

					// writing data to file
					fos.write(buffer, 0, count);
				}

				fos.flush();
				fos.close();
				input.close();
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		@Override
		protected void onPostExecute(String unused) {
			if (pDialog.isShowing()) {
				pDialog.dismiss();
			}
			if (unused != null) {
				if (unused.equalsIgnoreCase("")) {
					new AlertMessage(context)
							.showAToast(R.string.adapter_video_exists);
					return;
				}
			} else {
				String path = Environment.getExternalStorageDirectory() + "/"
						+ "goolel";
				new AlertMessage(context)
				.showAToast(R.string.confirm_file_download);
				new AlertMessage(context)
				.showAToast("Downloaded file path is" + path);

				notifyDataSetChanged();
			}
		}

	}
	private void deleteVideo(String userId, String videoId, final int position) {
		
		
        userid = userId;
        video = videoId;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		//builder.setTitle("Do this action");
		builder.setMessage("Do you want to delete this video?");
		
		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int which) {
		        // Do do my action here

		        dialog.dismiss();
		        String extraParameters = userid + Constants.API_GET_DELETE_VIDEO_PARAM1
						+ video;

				new RestWebservices(context) {
					public void onSuccess(String data,
							com.restservice.HttpResponse response) {
						try {
							arrayList.remove(position);
							notifyDataSetChanged();
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.serviceCall(Constants.API_GET_DELETE_VIDEO, extraParameters, true);
			
		    }

		});
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // I do not need any action here you might
		        dialog.dismiss();
		    }
		});
		

		AlertDialog alert = builder.create();
		alert.show();
	}


}
