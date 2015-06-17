package com.kids.apps;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.adapter.Childbeans;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class FragmentSelectKid extends Fragment {

	ArrayList<Childbeans> arrayList = null;

	String[] videoNames;

	private Spinner childdropdown;

	String child_name, child_id, child_array, school_id, school_class_id,
			language;
	private RelativeLayout _done;

	TextView text, selectchild, _continue, _done_button;
	ImageView drop;
	private AlertDialog myalertDialog = null;

	private String msg_selectchild, msg_isselected, msg_child;

	public FragmentSelectKid() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);

		child_array = sharedpref.getString("child_array", "");

		// setActionBArStyle();
		View rootView = inflater.inflate(R.layout.selectkid_fragment,
				container, false);

		arrayList = new ArrayList<Childbeans>();

		try {
			JSONArray jsonObj = new JSONArray(child_array);

			videoNames = new String[jsonObj.length()];
			for (int i = 0; i < jsonObj.length(); i++) {
				JSONObject c = jsonObj.getJSONObject(i);
				Childbeans childbeans = new Childbeans();
				childbeans.child_image = c.getString("child_image");
				childbeans.child_name = c.getString("child_name");
				videoNames[i] = new String(c.getString("child_name"));
				childbeans.school_name = c.getString("school_name");
				childbeans.child_moblie = c.getString("child_moblie");
				childbeans.school_class_id = c.getString("school_class_id");
				childbeans.user_id = c.getString("user_id");
				childbeans.child_gender = c.getString("child_gender");
				childbeans.child_age = c.getString("child_age");
				childbeans.school_id = c.getString("school_id");

				arrayList.add(childbeans);
			}
			// looping through All Contacts

		} catch (JSONException e) {
			System.out.println("Andy Error " + e);
			e.printStackTrace();
		}

		drop = (ImageView) rootView.findViewById(R.id.imageView1);
		drop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (videoNames == null) {

					Toast.makeText(getActivity(), msg_child, Toast.LENGTH_SHORT)
							.show();
				}

				else {

					if (videoNames.length > 0) {
						alertdialog();

					} else {

						Toast.makeText(getActivity(), msg_child,
								Toast.LENGTH_SHORT).show();

					}

				}
			}
		});

		// text = (TextView) rootView.findViewById(R.id.textView2);

		selectchild = (TextView) rootView.findViewById(R.id.textView1);
		text = (TextView) rootView.findViewById(R.id.textView2);
		_done_button = (TextView) rootView.findViewById(R.id.textView3);

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {
			selectchild.setText("Select Student");
			text.setText("Select Student");
			msg_child = "No value for Child";
			msg_selectchild = "Please select children from the list and continue.";
			msg_isselected = "is selected";
			_done_button.setText("Done");

		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			selectchild.setText("Velg elev ");
			text.setText("Velg elev ");
			msg_child = "Ingen verdi for barn";
			msg_selectchild = "Vennligst velg barn fra listen og fortsette.";
			msg_isselected = "er valgt";
			_done_button.setText("Hent");
		}

		_done = (RelativeLayout) rootView.findViewById(R.id.done);
		_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String kidname = text.getText().toString();
				if (kidname.equalsIgnoreCase("Select Student")) {
					Toast.makeText(getActivity(), msg_selectchild,
							Toast.LENGTH_SHORT).show();
				}

				else if (kidname.equalsIgnoreCase("Velg student")) {

					Toast.makeText(getActivity(), msg_selectchild,
							Toast.LENGTH_SHORT).show();
				} else {
					// ........................sharedpreferences.......................//
					SharedPreferences myPrefs = getActivity()
							.getSharedPreferences("absentapp",
									Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = myPrefs.edit();

					editor.putString("childname", child_name);
					editor.putString("childid", child_id);
					editor.putString("school_id", school_id);
					editor.putString("school_class_id", school_class_id);

					editor.commit();

					Toast.makeText(getActivity(),
							child_name + " " + msg_isselected,
							Toast.LENGTH_SHORT).show();

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Intent i = new Intent(getActivity(),
									MainActivity.class);
							startActivity(i);
							getActivity().finish();
						}
					}, 500);
				}
			}
		});

		childdropdown = (Spinner) rootView.findViewById(R.id.spinner1);
		childdropdown.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				child_name = arrayList.get(position).child_name;
				child_id = arrayList.get(position).user_id;
				school_id = arrayList.get(position).school_id;
				school_class_id = arrayList.get(position).school_class_id;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.dropdown_childlist_item, videoNames);

		childdropdown.setAdapter(adapter);

		return rootView;
	}

	private void alertdialog() {
		// TODO Auto-generated method st

		final AlertDialog.Builder myDialog = new AlertDialog.Builder(
				getActivity());

		final ListView listview = new ListView(getActivity());

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				child_name = arrayList.get(position).child_name;
				child_id = arrayList.get(position).user_id;
				school_id = arrayList.get(position).school_id;
				school_class_id = arrayList.get(position).school_class_id;
				text.setText(child_name);

				myalertDialog.dismiss();
			}
		});
		;

		LinearLayout layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.addView(listview);
		myDialog.setView(layout);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.dropdown_childlist_item, videoNames);
		listview.setAdapter(adapter);

		myalertDialog = myDialog.show();
	}

}
