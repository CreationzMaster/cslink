package com.kids.apps;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import com.adapter.Childbeans;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class FragmentAbsent extends Fragment {

	private Spinner templetedropdown;
	private ListView subject_list;
	private String getsubject_url, send_data_url, send_subject, _parent_id,
			school_id, child_id, class_id, template;
	ProgressDialog pDialog;
	private CheckBox check_all;
	private LinearLayout single_selection, select_all, _send;

	ArrayList<Childbeans> arrayList_subject = null;

	ArrayList<Childbeans> arrayList_templete = null;

	String[] subject_list_arr;

	String[] templete_list_arr;
	ArrayList arrList;
	String strText = "";
	TextView text, lang_select, lang_selectall, lang_absent,_send_text;
	ImageView drop;
	private AlertDialog myalertDialog = null;

	private RelativeLayout _done;

	private String msg_network, msg_dialog, msg_dialog_sending, language,
			msg_template, msg_subject, msg_template_select, msg_childnot,
			msg_record;

	public FragmentAbsent() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		hideKeyboardForFocusedView(getActivity());
		

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);// childid

		_parent_id = sharedpref.getString("parent_id", "");
		school_id = sharedpref.getString("school_id", "");
		child_id = sharedpref.getString("childid", "");//school_class_id
		class_id= sharedpref.getString("school_class_id", "");
		View rootView = inflater.inflate(R.layout.absent_fragment, container,
				false);

		arrList = new ArrayList();

		// ...............get subject URL.................//

		getsubject_url = ApplicationData.main_url
				+ "getallschildsubjects.php?childid=" + child_id + "&schoolid="
				+ school_id;
		
		

		get_subject(getsubject_url);

		// ...................Linear layout..............//

		check_all = (CheckBox) rootView.findViewById(R.id.checkBox1);
		check_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub

				if (subject_list_arr != null) {

					if (subject_list_arr.length > 0) {

						if (isChecked) {

							if (arrList.size() > 0) {
								arrList.clear();

							}

							for (int i = 0; i < subject_list.getAdapter()
									.getCount(); i++) {
								subject_list.setItemChecked(i, true);
								arrList.add(i);

							}

							for (int i = 0; i < arrList.size(); i++) {
								strText += arrayList_subject.get(i).child_period_id
										+ ",";

							}

						}

						else {

							if (arrList.size() > 0) {
								arrList.clear();

							}
							for (int i = 0; i < subject_list.getAdapter()
									.getCount(); i++) {
								subject_list.setItemChecked(i, false);
							}

							strText = "";

						}

					}
				}
			}
		});

		single_selection = (LinearLayout) rootView
				.findViewById(R.id.select_subject);
		single_selection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (arrList.size() > 0) {

					if (check_all.isChecked()) {

						check_all.setChecked(false);
					}

					arrList.clear();
					for (int i = 0; i < subject_list.getAdapter().getCount(); i++) {
						subject_list.setItemChecked(i, false);
					}

					strText = "";
					

				}

			}
		});

		select_all = (LinearLayout) rootView.findViewById(R.id.all_subject);
		select_all.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		drop = (ImageView) rootView.findViewById(R.id.imageView1);
		drop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (templete_list_arr == null) {

					Toast.makeText(getActivity(), msg_template,
							Toast.LENGTH_SHORT).show();
				}

				else {

					if (templete_list_arr.length > 0) {
						alertdialog();

					}

					else {

						Toast.makeText(getActivity(), msg_template,
								Toast.LENGTH_SHORT).show();

					}

				}

			}
		});

		text = (TextView) rootView.findViewById(R.id.textView2);
		lang_select = (TextView) rootView
				.findViewById(R.id.textView_select_subject);
		lang_selectall = (TextView) rootView
				.findViewById(R.id.textView_all_subject);
		lang_absent = (TextView) rootView.findViewById(R.id.textView1);
		_send_text= (TextView) rootView.findViewById(R.id.textView22);

		// .......send data to server...............//

		_send = (LinearLayout) rootView.findViewById(R.id.send);
		_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (strText.equalsIgnoreCase("")) {

					Toast.makeText(getActivity(), msg_subject,
							Toast.LENGTH_SHORT).show();
				}

				else {

					String temp_lete = text.getText().toString();

					if (temp_lete.equalsIgnoreCase("Reason")) {

						Toast.makeText(getActivity(), msg_template_select,
								Toast.LENGTH_SHORT).show();
					}

					else if (temp_lete.equalsIgnoreCase("Årsak")) {
						Toast.makeText(getActivity(), msg_template_select,
								Toast.LENGTH_SHORT).show();
					}

					else {

						Calendar c = Calendar.getInstance();
						

						SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");	
						String formattedDate = df.format(c.getTime());
						

						send_data_url = ApplicationData.main_url
								+ "insertstudentattendance.php?classid="+class_id+"&childid="
								+ child_id + "&periodids=" + strText
								+ "&reason=" + template + "&date="
								+ formattedDate;
						
						Log.d("xxxx", "send_data_url  "+send_data_url);

						send_data_url = send_data_url.replaceAll(" ", "%20");

						System.out.println(send_data_url
								+ " milliseconds since midnight");

						 send_data(send_data_url);

					}

				}
			}
		});

		// .............ListView................//
		subject_list = (ListView) rootView.findViewById(R.id.listView1);
		subject_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		subject_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView arg0, View arg1, int arg2,
					long arg3) {

				if (arrList.contains(arg2)) {
					arrList.remove((Integer) arg2);

				} else {
					arrList.add(arg2);

				}

				Collections.sort(arrList);
				strText = "";

				for (int i = 0; i < arrList.size(); i++) {

					strText += arrayList_subject.get(i).child_period_id + ",";

				}

			}

		});

		// ..........dropdown spinner...........//

		templetedropdown = (Spinner) rootView.findViewById(R.id.spinner1);
		templetedropdown
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						template = arrayList_templete.get(position).child_template_title;
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {

			text.setText("Reason");
			lang_select.setText("Select Period");
			lang_selectall.setText("Select All");
			lang_absent.setText("Periods");
			msg_network = "Network error";
			msg_dialog = "Wait for server Response!";

			msg_dialog_sending = "Sending data to server";
			msg_template = "No values for templates";
			msg_subject = "Kindly select period!";
			msg_childnot = "Child does not exist";
			msg_template_select = "Please select reason and try again!";
			msg_record = "Report Submitted";
			_send_text.setText("SEND");

		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			text.setText("Årsak");
			lang_select.setText("Velg time");
			lang_selectall.setText("Velg alle");
			lang_absent.setText("periode");
			msg_network = " nettverksfeil";
			msg_dialog = "Vent til server respons!";

			msg_dialog_sending = "Sender fraværet til skolen!";
			msg_template = "Ingen verdier for maler";
			msg_subject = "Velg time!";
			msg_childnot = "Barn eksisterer ikke";
			msg_template_select = "Vennligst velg årsak til fravær!";
			msg_record = "Rapporter Sendt inn";
			_send_text.setText("Meld fravær");
		}

		return rootView;
	}

	protected void send_data(String send_data_url2) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(getActivity());
		pDialog.setCancelable(false);
		pDialog.setMessage(msg_dialog_sending);
		pDialog.show();

		RequestQueue queue = Volley.newRequestQueue(getActivity());

		 Log.e("URl", "" + send_data_url2);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, send_data_url2, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {

							String flag = response.getString("flag");

							if (Integer.parseInt(flag) == 1) {

								Toast.makeText(getActivity(), msg_record,
										Toast.LENGTH_SHORT).show();
								pDialog.dismiss();
								
								uncheck();

							}

							else {

								pDialog.dismiss();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						pDialog.dismiss();

						Toast.makeText(getActivity(), msg_network,
								Toast.LENGTH_SHORT).show();
					}
				});

		queue.add(jsObjRequest);
	}

	protected void uncheck() {
		// TODO Auto-generated method stub
		if (arrList.size() > 0) {

			if (check_all.isChecked()) {

				check_all.setChecked(false);
			}

			arrList.clear();
			for (int i = 0; i < subject_list.getAdapter().getCount(); i++) {
				subject_list.setItemChecked(i, false);
			}

			strText = "";
			

		}
	}

	private void get_subject(String login_url2) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(getActivity());
		pDialog.setCancelable(false);
		pDialog.setMessage(msg_dialog);
		pDialog.show();

		RequestQueue queue = Volley.newRequestQueue(getActivity());

		 Log.e("URl", "" + login_url2);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, login_url2, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {

							String flag = response.getString("flag");

							if (Integer.parseInt(flag) == 1) {

								JSONObject records = response
										.getJSONObject("Records");
								JSONArray childsubjects = records
										.getJSONArray("childsubjects");

								arrayList_subject = new ArrayList<Childbeans>();
								arrayList_templete = new ArrayList<Childbeans>();

								subject_list_arr = new String[childsubjects
										.length()];

								for (int i = 0; childsubjects.length() > i; i++) {

									JSONObject c = childsubjects
											.getJSONObject(i);
									Childbeans childbeans = new Childbeans();
									childbeans.child_subject_id = c
											.getString("subject_id");
									childbeans.child_teacher_id = c
											.getString("teacher_id");
									childbeans.child_class_id = c
											.getString("class_id");
									childbeans.child_subject_name = c
											.getString("subject_name");
									subject_list_arr[i] = new String(c
											.getString("subject_name"));
									childbeans.child_period_id = c
											.getString("period_id");
									arrayList_subject.add(childbeans);

								}

								JSONObject school_templates = response
										.getJSONObject("school templates");

								JSONArray schooltemp = school_templates
										.getJSONArray("schooltemp");

								templete_list_arr = new String[schooltemp
										.length()];

								for (int j = 0; schooltemp.length() > j; j++) {

									JSONObject c = schooltemp.getJSONObject(j);

									Childbeans childbeans = new Childbeans();
									childbeans.child_temlate_id = c
											.getString("template_id");
									childbeans.child_school_id = c
											.getString("school_id");
									childbeans.child_template_title = c
											.getString("template_title");

									templete_list_arr[j] = new String(c
											.getString("template_title"));

									arrayList_templete.add(childbeans);

								}

								setadapter();

								pDialog.dismiss();

							}

							else {

								pDialog.dismiss();
								Toast.makeText(getActivity(), msg_childnot,
										Toast.LENGTH_SHORT).show();
								// msg_childnot

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						pDialog.dismiss();

						Toast.makeText(getActivity(), msg_network,
								Toast.LENGTH_SHORT).show();
					}
				});

		queue.add(jsObjRequest);
	}

	protected void setadapter() {
		// TODO Auto-generated method stub

		subject_list.setAdapter(new ArrayAdapter(getActivity(),
				R.layout.multiple_selection, subject_list_arr));

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

				template = arrayList_templete.get(position).child_template_title;

				text.setText(template);

				myalertDialog.dismiss();
			}
		});
		;

		LinearLayout layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.addView(listview);
		myDialog.setView(layout);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.dropdown_childlist_item, templete_list_arr);
		listview.setAdapter(adapter);

		myalertDialog = myDialog.show();
	}

	// ....on click on forgot password..//
	public void conti(View v) {

	}
	
	public static void hideKeyboardForFocusedView(Activity activity) {
	    InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	    View view = activity.getCurrentFocus();
	    if (view != null) {
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	    }
	}
}
