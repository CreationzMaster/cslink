package com.kids.apps;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	LinearLayout menuBar, lot;
	private DrawerLayout mDrawerLayout;
	ListView list;
	String[] Drawerlist_item;
	int fragment;
	Integer[] Drawerlist_image = { R.drawable.my_profile_ic,
			R.drawable.newmessage_ic,R.drawable.message_history,
			R.drawable.register_ic, R.drawable.slectkid_ic,
			R.drawable.setting_ic, R.drawable.fraver, R.drawable.statistics_ic,
			R.drawable.contact_ic, R.drawable.logout_ic };
	public static TextView title;
	private ActionBarDrawerToggle mDrawerToggle;

	String _child_name, _child_id, _child_array, _school_id, language;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homeactivity);

		Intent intent = getIntent();

		fragment = intent.getIntExtra("fragment", 0);

		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);

		_child_array = sharedpref.getString("child_array", "");
		_child_id = sharedpref.getString("childid", "");
		language = sharedpref.getString("language", "");
		_child_name = sharedpref.getString("childname", "");
		_school_id = sharedpref.getString("school_id", "");

		if (savedInstanceState == null) {

			Fragment fragment = new  FragmentNewMessage();
			android.app.FragmentManager fragmentManager = getFragmentManager();
			
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			
			
		}

		list = (ListView) findViewById(R.id.list_slidermenu);

		LayoutInflater inflater = getLayoutInflater();
		View header = inflater.inflate(R.layout.header, list, false);
		list.addHeaderView(header, null, false);

		setActionBArStyle();
		menuBar = (LinearLayout) findViewById(R.id.btnbck);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		int width1 = getResources().getDisplayMetrics().widthPixels / 2;
		int width2 = getResources().getDisplayMetrics().widthPixels / 3;
		int width = width1 - width2;
		DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) list
				.getLayoutParams();
		params.width = width1;
		list.setLayoutParams(params);

		// .................get string array from sring.xml..........//

		loaddrawer();

		menuBar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
					mDrawerLayout.closeDrawers();
				} else if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

					SharedPreferences sharedpref = getSharedPreferences(
							"absentapp", 0);

					language = sharedpref.getString("language", "");

					loaddrawer();

					mDrawerLayout.openDrawer(list);

				}

			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {
			public View row;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				final int pos = position;

				if (row != null) {

					// row.setBackgroundColor(Color.GRAY);
				}
				row = view;
				// view.setBackgroundColor(Color.GRAY);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						callAction(pos); // your fragment transactions go here
					}
				}, 200);
				// callAction(position);
				mDrawerLayout.closeDrawers();
			}
		});
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (fragment == 1) {

			callAction(1);

		}

	}
	
	
	public void setActionBarTitle(String tit){
		title.setText(tit);
	}

	private void loaddrawer() {
		// TODO Auto-generated method stub

		if (language.equalsIgnoreCase("english")) {
			Drawerlist_item = getResources()
					.getStringArray(R.array.drawer_item);
		}

		else if (language.equalsIgnoreCase("nowrgian")) {

			Drawerlist_item = getResources().getStringArray(
					R.array.drawer_item_nowge);
		}

		AdapterHomeScreen adap = new AdapterHomeScreen(getApplicationContext(),
				Drawerlist_item, Drawerlist_image);
		list.setAdapter(adap);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_launcher, R.string.app_name, R.string.app_name) {
			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle("TAG PHOTO");
				invalidateOptionsMenu();

			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("TAG PHOTO");
				invalidateOptionsMenu();
			}

		};
	}

	public void setActionBArStyle() {

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		View cView = getLayoutInflater().inflate(R.layout.title, null);

		title = (TextView) cView.findViewById(R.id.textView1);
		if (language.equalsIgnoreCase("english")) {
			title.setText("Message");
		}

		else if (language.equalsIgnoreCase("nowrgian")) {

			title.setText("Samtaler");
		}
		
		actionBar.setCustomView(cView);
		actionBar.setDisplayShowHomeEnabled(false);

	}

	public void callAction(int pos) {
		Fragment fragment = null;
		Bundle bundle = new Bundle();
		switch (pos) {

		case 0:

			break;
		case 1:

			fragment = new FramentProfile();
			
			if (language.equalsIgnoreCase("english")) {
				title.setText("Profile");
			}

			else if (language.equalsIgnoreCase("nowrgian")) {

				title.setText("Profil");
			}
			
			
			bundle.putString("childArray", _child_array);
			bundle.putString("childid", _child_id);
			bundle.putString("childname", _child_name);
			fragment.setArguments(bundle);
			break;

		case 2:

			fragment = new FragmentNewMessage();
			if (language.equalsIgnoreCase("english")) {
				title.setText("Message");
			}

			else if (language.equalsIgnoreCase("nowrgian")) {
				title.setText("Samtaler");
			}
			break;

		case 3:

			fragment = new FragmentMessage();
			if (language.equalsIgnoreCase("english")) {
				title.setText("Alert Messages");
			}

			else if (language.equalsIgnoreCase("nowrgian")) {

				title.setText("Varselmeldinger");
			}
			break;

		case 4:
			fragment = new FragmentRegister();
			
			if (language.equalsIgnoreCase("english")) {
				title.setText("New Student ");
			}

			else if (language.equalsIgnoreCase("nowrgian")) {

				title.setText("Ny elev");
			}

			bundle.putString("school_id", _school_id);
			fragment.setArguments(bundle);
			break;

		case 5:
			fragment = new FragmentSelectKid();
			
			if (language.equalsIgnoreCase("english")) {
				title.setText("Select Child");
			}

			else if (language.equalsIgnoreCase("nowrgian")) {

				title.setText("Velg student");
			}
			
			break;
		case 6:

			fragment = new FragmentSetting();
			
			
			if (language.equalsIgnoreCase("english")) {
				title.setText("Settings");
			}

			else if (language.equalsIgnoreCase("nowrgian")) {

				title.setText("Innstillinger");
			}
			break;

		case 7:

			fragment = new FragmentAbsent();
			
			
			if (language.equalsIgnoreCase("english")) {
				title.setText("Absent");
			}

			else if (language.equalsIgnoreCase("nowrgian")) {

				title.setText("Fravær");
			}
			break;

		case 8:

			fragment = new FragmentStatistics();
			title.setText("Statistics");
			
			if (language.equalsIgnoreCase("english")) {
				title.setText("Statistic");
			}

			else if (language.equalsIgnoreCase("nowrgian")) {

				title.setText("Statistikk");
			}
			break;

		case 9:

			fragment = new FragmentContactUs();
			
			

			if (language.equalsIgnoreCase("english")) {
				title.setText("Your feedback!");
			}

			else if (language.equalsIgnoreCase("nowrgian")) {

				title.setText("Tilbakemelding");
			}
			break;

		case 10:

			// logout();
			break;

		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			list.setItemChecked(pos, true);
			list.setSelection(pos);
			setTitle("Naveen Sir");
			mDrawerLayout.closeDrawer(list);
		} else {

			Dialoglogout myDiag = new Dialoglogout();
			myDiag.show(getFragmentManager(), "Diag");
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

}
