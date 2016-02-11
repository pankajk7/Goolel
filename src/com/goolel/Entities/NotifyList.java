package com.goolel.Entities;

import java.io.Serializable;

import android.graphics.Bitmap;

public class NotifyList implements Serializable {

	String notify_msg;

	public NotifyList(String notify_msg) {
		this.notify_msg = notify_msg;
	}

	public String getNotify_msg() {
		return notify_msg;
	}

	public void setNotify_msg(String notify_msg) {
		this.notify_msg = notify_msg;
	}
}
