package com.kids.apps;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.adapter.Childbeans;
import com.adapter.TeacherListAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class FragmentNewMessage extends Fragment {

	private ListView _teacherlist;
	private ProgressDialog pDialog;
	TeacherListAdapter adapter;
	private ArrayList<String> s_message;

	ArrayList<Childbeans> messageList = null;
	String getmessage_url, _parent_id, class_id;
	private String msg_network, msg_dialog, language;

	public FragmentNewMessage() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.message_fragment, container,
				false);

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);

		_parent_id = sharedpref.getString("parent_id", "");
		class_id = sharedpref.getString("school_class_id", "");
		
		

		_teacherlist = (ListView) rootView.findViewById(R.id.listView_message);
		_teacherlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent in = new Intent(getActivity(), ChatActivity.class);

				in.putExtra("sender_id", messageList.get(position).user_id);
				in.putExtra("sendername", messageList.get(position).sendername);
				in.putExtra("sender_image",
						messageList.get(position).senderimage);
				in.putExtra("sender_subject",
						messageList.get(position).subject_name);

				 in.putExtra("sender_school",
				 messageList.get(position).senderschool);

				startActivity(in);
				getActivity().finish();
			}
		});

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {

			msg_network = "Network error";
			msg_dialog = "Wait for server Response!";

		}

		else if (language.equalsIgnoreCase("nowrgian")) {

			msg_network = " nettverksfeil";
			msg_dialog = "Vent til server respons!";

		}

		// http://skywaltzlabs.in/abscentapp/get_child_teachers.php?classid=1
		getmessage_url = ApplicationData.main_url
				+ "get_child_teachers.php?classid=" + class_id;
		
		

		getmessage(getmessage_url);

		return rootView;
	}

	private void getmessage(String login_url2) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(getActivity());
		pDialog.setCancelable(false);
		pDialog.setMessage(msg_dialog);
		pDialog.show();

		RequestQueue queue = Volley.newRequestQueue(getActivity());

		// Log.e("URl", "" + login_url2);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, login_url2, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {

							String flag = response.getString("flag");

							if (Integer.parseInt(flag) == 1) {

								JSONObject All_Teachers = response
										.getJSONObject("All Teachers");

								JSONArray result = All_Teachers
										.getJSONArray("teachers");

								messageList = new ArrayList<Childbeans>();

								for (int i = 0; result.length() > i; i++) {

									JSONObject c = result.getJSONObject(i);

									// int checkdate = cheking(c
									// .getString("created_at"));

									// if (checkdate == 1) {

									Childbeans childbeans = new Childbeans();
									childbeans.user_id = c.getString("user_id");
									childbeans.sendername = c.getString("name");
									childbeans.senderimage = c
											.getString("image");
									childbeans.class_id = c
											.getString("class_id");
//									childbeans.subject_id = c
//											.getString("subject_id");
									childbeans.subject_name = c
											.getString("subject_name");
									childbeans.emailaddress = c
											.getString("emailaddress");
									childbeans.senderschool = c
											.getString("school_name");

									messageList.add(childbeans);

									// }
								}

								setlist_toadapter();

								pDialog.dismiss();

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

	protected int cheking(String string) {
		// TODO Auto-generated method stub
		int i = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();

			String time = sdf.format(c.getTime());

			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date date, changedate;

			date = sdf2.parse(string);

			String datechange = sdf.format(date);
			changedate = sdf.parse(datechange);

			// .............................//
			Date date2;
			date2 = sdf.parse(time);

			if (changedate.equals(date2)) {
				i = 1;

			}

			else {

				i = 0;
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return i;
	}

	protected void setlist_toadapter() {
		// TODO Auto-generated method stub

		if (messageList.size() > 0) {

			adapter = new TeacherListAdapter(getActivity(), messageList);
			_teacherlist.setAdapter(adapter);

		}
	}

}
