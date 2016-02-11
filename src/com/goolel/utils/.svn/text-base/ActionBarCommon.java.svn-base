package com.goolel.utils;
import com.goolel.R;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ActionBarCommon {

	Button backButton;
    TextView titleTextView;
	LinearLayout backButtonLayout;

	ActionBar actionBar;
	Activity activity;
	
//	public ActionBarCommon(ActionBar actionBar) {
//		this.actionBar = actionBar;
//	}
	
	public ActionBarCommon(ActionBar actionBar, Activity activity) {
		this.actionBar = actionBar;
		this.activity = activity;
	}

	public ActionBarCommon setCustomActionBar() {
		actionBar.setTitle("");
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.action_bar_layout);
		actionBar.getCustomView().setBackgroundResource(R.color.primary_color);
		titleTextView = (TextView) actionBar.getCustomView().findViewById(
				R.id.actionbar_textview_title);
		titleTextView.setBackgroundResource(R.color.primary_color);
		LinearLayout menuLayout = (LinearLayout) actionBar.getCustomView()
				.findViewById(R.id.actionBar_layout_menu);
		menuLayout.setVisibility(View.GONE);

		backButtonLayout = (LinearLayout) actionBar.getCustomView()
				.findViewById(R.id.actionbar_layout_back);
		backButtonLayout.setVisibility(View.VISIBLE);
		backButton = (Button) actionBar.getCustomView().findViewById(
				R.id.actionbar_button_back);
		return this;
	}

	public void setTitleForActionBar(String titleName) {
		titleTextView.setText(titleName);
	}

	public LinearLayout getBackButtonLayout() {
		return backButtonLayout;
	}

	public Button getBackButton() {
		return backButton;
	}
}