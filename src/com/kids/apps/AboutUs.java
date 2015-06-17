package com.kids.apps;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class AboutUs extends Activity {

	TextView version, copyright, right;
	String language;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.aboutus);

		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);

		version = (TextView) findViewById(R.id.textView2);
		copyright = (TextView) findViewById(R.id.textView3);
		right = (TextView) findViewById(R.id.textView4);

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {
			version.setText("Version:2.12.5");
			copyright.setText("Copyright 2015 appsbusinesstore CSlink");
			right.setText(" All right reserved worldwide");

		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			version.setText("Versjon: 2.125");
			copyright.setText("Copyright 2015 appsbusinesstore CSlink");
			right.setText(" All right reserved worldwide");
		}
	}

	public void back(View v) {

		finish();

	}
}
