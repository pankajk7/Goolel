package com.goolel.utils;

import android.content.Context;
import android.widget.Toast;

public class AlertMessage {

	Context context;
	Toast toast;

	public AlertMessage(Context context) {
		this.context = context;
		toast = new Toast(context);
	}

	public void showAToast(int resourceId) {
		String message = context.getResources().getString(resourceId);
		if (toast.getView() != null) {
			toast.setText(message);
		} else {
			toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
		}
		toast.show();
	}
	
	public void showAToast(String string) {
		if (toast.getView() != null) {
			toast.setText(string);
		} else {
			toast = Toast.makeText(context, string, Toast.LENGTH_LONG);
		}
		toast.show();
	}
}
