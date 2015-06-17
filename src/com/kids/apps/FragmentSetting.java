package com.kids.apps;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FragmentSetting extends Fragment {

	LinearLayout _change_pincode, _english, nowrgian, aboutus;
	String _language, _notification;

	ImageView notification;

	TextView lang_english, _lang_aboutus, _changepin, lang_noti;

	public FragmentSetting() {
	}

	int language = 1;

	int sdk = android.os.Build.VERSION.SDK_INT;

	private TextView _tv_english, _tv_nowrgian;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// setActionBArStyle();
		View rootView = inflater.inflate(R.layout.fragment_setting, container,
				false);

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);

		_language = sharedpref.getString("language", "");
		_notification = sharedpref.getString("notification", "");

		findview(rootView);

		return rootView;
	}

	private void findview(View rootView) {
		// TODO Auto-generated method stub
		// ......................All Text Views........//

		notification = (ImageView) rootView.findViewById(R.id.imageView3);

		setnotification();

		notification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				SharedPreferences sharedpref = getActivity()
						.getSharedPreferences("absentapp", 0);

				_notification = sharedpref.getString("notification", "");

				// ........................sharedpreferences.......................//
				SharedPreferences myPrefs = getActivity().getSharedPreferences(
						"absentapp", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = myPrefs.edit();

				if (_notification.equalsIgnoreCase("0")) {

					notification.setBackgroundResource(R.drawable.offtoggle);// offnotification
					editor.putString("notification", "1");
				}

				else if (_notification.equalsIgnoreCase("1")) {

					notification.setBackgroundResource(R.drawable.ontoggle);// on
																			// notification
					editor.putString("notification", "0");

				}

				editor.commit();
			}
		});

		_tv_english = (TextView) rootView.findViewById(R.id.textView_english);
		_tv_nowrgian = (TextView) rootView.findViewById(R.id.textView_nowrgian);
		_lang_aboutus = (TextView) rootView.findViewById(R.id.textView4);
		_changepin = (TextView) rootView.findViewById(R.id.textView5);
		lang_noti = (TextView) rootView.findViewById(R.id.textView3);

		// ///..............All Linear Layouts.......//
		_change_pincode = (LinearLayout) rootView
				.findViewById(R.id.change_pincode);
		_change_pincode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getActivity(), ChangePincode.class);
				startActivity(in);
			}
		});

		_english = (LinearLayout) rootView.findViewById(R.id.english);
		_english.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// ........................sharedpreferences.......................//
				SharedPreferences myPrefs = getActivity().getSharedPreferences(
						"absentapp", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = myPrefs.edit();

				editor.putString("language", "english");

				editor.commit();

				((MainActivity) getActivity()).setActionBarTitle("Settings");

				_tv_english.setText("English");

				lang_noti.setText("Notification");

				_lang_aboutus.setText("About us");

				_tv_nowrgian.setText("Norwegian");

				_changepin.setText("Change Pincode");

				_tv_english.setTextColor(getResources().getColor(
						R.color.color_white));
				_tv_nowrgian.setTextColor(getResources().getColor(
						R.color.app_theam_color));

				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					_english.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.leftround_blue));
					nowrgian.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.rightround_white));
				} else {
					_english.setBackground(getResources().getDrawable(
							R.drawable.leftround_blue));
					nowrgian.setBackground(getResources().getDrawable(
							R.drawable.rightround_white));
				}

			}
		});
		nowrgian = (LinearLayout) rootView.findViewById(R.id.norwgian);
		nowrgian.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// ........................sharedpreferences.......................//
				SharedPreferences myPrefs = getActivity().getSharedPreferences(
						"absentapp", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = myPrefs.edit();

				editor.putString("language", "nowrgian");

				editor.commit();

				_tv_english.setText("Engelsk");

				((MainActivity) getActivity())
						.setActionBarTitle("Innstillinger");

				lang_noti.setText("Varsling");

				_tv_nowrgian.setText("Norsk");

				_lang_aboutus.setText("Om appsbusinesstore");

				_changepin.setText("Endre PIN-kode");

				_tv_english.setTextColor(getResources().getColor(
						R.color.app_theam_color));
				_tv_nowrgian.setTextColor(getResources().getColor(
						R.color.color_white));
				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					_english.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.leftround_white));
					nowrgian.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.rightround_blue));
				} else {
					_english.setBackground(getResources().getDrawable(
							R.drawable.leftround_white));
					nowrgian.setBackground(getResources().getDrawable(
							R.drawable.rightround_blue));
				}
			}
		});

		aboutus = (LinearLayout) rootView.findViewById(R.id.aboutus);
		aboutus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getActivity(), AboutUs.class);
				startActivity(in);
			}
		});

		if (_language.equalsIgnoreCase("english")) {

			_tv_english.setText("English");
			
			lang_noti.setText("Notification");

			_lang_aboutus.setText("About us");

			_changepin.setText("Change Pincode");

			_tv_english.setTextColor(getResources().getColor(
					R.color.color_white));
			_tv_nowrgian.setTextColor(getResources().getColor(
					R.color.app_theam_color));

			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				_english.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.leftround_blue));
				nowrgian.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.rightround_white));
			} else {
				_english.setBackground(getResources().getDrawable(
						R.drawable.leftround_blue));
				nowrgian.setBackground(getResources().getDrawable(
						R.drawable.rightround_white));
			}
		}

		else if (_language.equalsIgnoreCase("nowrgian")) {

			_tv_english.setText("Engelsk");

			lang_noti.setText("Varsling");

			_tv_nowrgian.setText("Norsk");

			_lang_aboutus.setText("Om appsbusinesstore");

			_changepin.setText("Endre PIN-kode");

			_tv_english.setTextColor(getResources().getColor(
					R.color.app_theam_color));
			_tv_nowrgian.setTextColor(getResources().getColor(
					R.color.color_white));
			if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
				_english.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.leftround_white));
				nowrgian.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.rightround_blue));
			} else {
				_english.setBackground(getResources().getDrawable(
						R.drawable.leftround_white));
				nowrgian.setBackground(getResources().getDrawable(
						R.drawable.rightround_blue));
			}
		}
	}

	private void setnotification() {
		// TODO Auto-generated method stub
		if (_notification.equalsIgnoreCase("0")) {

			notification.setBackgroundResource(R.drawable.ontoggle);
		}

		else if (_notification.equalsIgnoreCase("1")) {

			notification.setBackgroundResource(R.drawable.offtoggle);

		}
	}

	public void setActionBArStyle() {
		// im=(ImageView)findViewById(R.id.imageView1);
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		View cView = getActivity().getLayoutInflater().inflate(R.layout.title,
				null);
		actionBar.setCustomView(cView);
		actionBar.setDisplayShowHomeEnabled(false);

	}

}
