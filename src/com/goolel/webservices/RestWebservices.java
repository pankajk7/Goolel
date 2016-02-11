package com.goolel.webservices;

import com.goolel.utils.Constants;
import com.goolel.utils.DevicePreferences;
import com.restservice.Http;
import com.restservice.HttpFactory;
import com.restservice.HttpResponse;
import com.restservice.NetworkError;
import com.restservice.ResponseHandler;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class RestWebservices {

	Context context;
	String baseURL;
	String urlSuffix;
	String resourceName;
	String cookieString;
	String statusString;
	ProgressDialog progressDialog;

	// String authorizationString;

	public RestWebservices(Context context) {
		baseURL = Constants.BASE_URL;
		urlSuffix = Constants.SUFFIX_URL;
		this.context = context;
		progressDialog = new ProgressDialog(context);
		statusString = "401";
	}

	private String getServiceURL(String resourceName, String extraParameters) {
		return baseURL + urlSuffix + resourceName + extraParameters;
	}

	public void onComplete() {
		try {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onFailure(NetworkError error) {
		Toast.makeText(context, Constants.ERROR_NETWORK_ERROR,
				Toast.LENGTH_LONG).show();
	}

	public void onError(String message, HttpResponse response) {

	}

	public String checkStatus(String message, HttpResponse response) {
		// Map<String, List<String>> map = response.getHeaders();
		// String status = "";
		int code = 0;
		try {
			code = response.getCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// try {
		// status = map.get("Status").toString();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// if (status.contains(statusString)) {
		if (code == 401) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			Toast.makeText(context, "User Unauthorized, Please Login again",
					Toast.LENGTH_LONG).show();
			return statusString;
		}

		if (message == null || message.trim().equalsIgnoreCase("")) {
			return "Some error occured!";
		}
		return message;
	}

	public void onSuccess(String message, String success) {

	}

	public void onSuccess(Object object, HttpResponse response) {
	}

	public void onSuccess(Object[] object, HttpResponse response) {
	}

	public void onSuccess(String data, HttpResponse response) {

	}

	public void serviceCall(String resourceName, String extraParameters,
			boolean showLoading) {
		String url = getServiceURL(resourceName, extraParameters);

		if (resourceName.equalsIgnoreCase(Constants.API_GET_VIDEO_LIST)) {
			getCall(url, showLoading);
		} else if (resourceName
				.equalsIgnoreCase(Constants.API_GET_DELETE_VIDEO)) {
			getCall(url, showLoading);
		} else if (resourceName.equalsIgnoreCase(Constants.API_GET_PROFILE)) {
			getCall(url, showLoading);
		}
	 

	}

	public void serviceCall(String resourceName, String extraParameters,
			Object object, boolean showLoading) {
		String url = getServiceURL(resourceName, extraParameters);

		if (resourceName.equalsIgnoreCase(Constants.API_LOGIN)) {
			postCall(url, object, showLoading);
		} else if (resourceName.equalsIgnoreCase(Constants.API_REGISTER)) {
			postCall(url, object, showLoading);
		} else if (resourceName.equalsIgnoreCase(Constants.API_FORGOT_PASSWORD)) {
			postCall(url, object, showLoading);
		} else if (resourceName.equalsIgnoreCase(Constants.API_CHANGE_PASSWORD)) {
			postCall(url, object, showLoading);
		} else if (resourceName.equalsIgnoreCase(Constants.API_EDIT)) {
			postCall(url, object, showLoading);
		} else if (resourceName
				.equalsIgnoreCase(Constants.API_POST_VIDEO_LIST_SEARCH)) {
			postCall(url, object, showLoading);
		}
	 else if (resourceName
			.equalsIgnoreCase(Constants.API_EMAIL_DUPLICATE)) {
		postCall(url, object, showLoading);
	}
	 else if (resourceName
				.equalsIgnoreCase(Constants.API_VIDEO_TITLE_DUPLICATE)) {
			postCall(url, object, showLoading);
		}

	}

	private void getCall(String url, boolean showLoading) {
		if (showLoading) {
			if (progressDialog.isShowing() == false) {
				progressDialog.show();
			}
		}

		Http http = HttpFactory.create(this.context);
		http.get(url).timeout(Constants.TIMEOUT)
				.handler(new ResponseHandler<String>() {
					@Override
					public void success(String data, HttpResponse response) {
						onSuccess(data, response);
						super.success(data, response);
					}

					@Override
					public void complete() {
						onComplete();
						super.complete();
					}

					@Override
					public void failure(NetworkError error) {
						onComplete();
						onFailure(error);
						// new
						// AlertDialogMessage(context).showAToast("Network failure occur");
						super.failure(error);
					}

					@Override
					public void error(String message, HttpResponse response) {
						message = checkStatus(message, response);
						if (!message.equalsIgnoreCase(statusString)) {
							onError(message, response);
						}
						super.error(message, response);
					}
				}).send();
	}

	private void postCall(String url, Object object, boolean showLoading) {
		if (showLoading) {
			if (progressDialog.isShowing() == false) {
				progressDialog.show();
			}
		}
		Http http = HttpFactory.create(this.context);
		
		http.post(url).data(object).timeout(Constants.TIMEOUT)	
		
		.handler(new ResponseHandler<String>() {
					@Override
					public void success(String data, HttpResponse response) {
						onSuccess(data, response);
						super.success(data, response);
					}

					@Override
					public void complete() {
						onComplete();
						super.complete();
					}

					@Override
					public void failure(NetworkError error) {
						onFailure(error);
						super.failure(error);
					}

					@Override
					public void error(String message, HttpResponse response) {
						message = checkStatus(message, response);
						if (!message.equalsIgnoreCase(statusString)) {
							onError(message, response);
						}
						super.error(message, response);
					}
				}).send();
	}
}