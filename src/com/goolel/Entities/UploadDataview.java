package com.goolel.Entities;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UploadDataview {
	TextView percentageTextView;
	int position;
	ProgressBar progressBar;
	ImageButton uploadButton;
	ImageButton deleteButton;

	public UploadDataview(TextView percentageTextView, int position,
			ProgressBar progressBar, ImageButton uploadButton, ImageButton deleteButton) {
		this.percentageTextView = percentageTextView;
		this.position = position;
		this.progressBar = progressBar;
		this.uploadButton = uploadButton;
		this.deleteButton = deleteButton;
	}
	

	public TextView getPercentage() {
		return percentageTextView;
	}

	public void setPercentage(TextView percentage) {
		this.percentageTextView = percentage;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	public TextView getPercentageTextView() {
		return percentageTextView;
	}

	public void setPercentageTextView(TextView percentageTextView) {
		this.percentageTextView = percentageTextView;
	}

	public ImageButton getUploadButton() {
		return uploadButton;
	}

	public void setUploadButton(ImageButton uploadButton) {
		this.uploadButton = uploadButton;
	}
	
	public ImageButton getDeleteButton() {
		return deleteButton;
	}

	public void setDeleteButton(ImageButton deleteButton) {
		this.deleteButton = deleteButton;
	}
}