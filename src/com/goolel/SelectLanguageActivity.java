package com.goolel;

import java.util.ArrayList;
import java.util.List;

import com.goolel.utils.AlertMessage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;



public class SelectLanguageActivity extends Activity {
	
	  private Spinner language_select_spinner;
	  private String[] languages;
	  Button button_language_ok;	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_language);	
		languages = getResources().getStringArray(R.array.language_list);	
		language_select_spinner = (Spinner) findViewById(R.id.select_language);
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, languages);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		language_select_spinner.setAdapter(dataAdapter);
		
		button_language_ok = (Button) findViewById(R.id.button_select_language); 

		button_language_ok.setOnClickListener(new View.OnClickListener() {
		        @Override 
		        public void onClick(View v) {
		            // handle click 
		        	String Text = language_select_spinner.getSelectedItem().toString();
		        	new AlertMessage(SelectLanguageActivity.this)
					.showAToast(Text);
		        		return;
		        } 
		    });
		
		
		
	}
	
	
	
	
}
