package com.kids.apps;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
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

public class FragmentMessage extends Fragment {

	private ListView _teacherlist;
	private ProgressDialog pDialog;
	TeacherListAdapter adapter;
	private ArrayList<String> s_message;

	public FragmentMessage() {
	}

	ArrayList<Childbeans> messageList = null;
	String getmessage_url, _parent_id,child_id;
	private String msg_network, msg_dialog, language;
	private TextView School;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.message_fragment, container,
				false);

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);

		_parent_id = sharedpref.getString("parent_id", "");
		
		
		
		child_id= sharedpref.getString("childid", "");

		_teacherlist = (ListView) rootView.findViewById(R.id.listView_message);
		_teacherlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				SharedPreferences myPrefs = getActivity().getSharedPreferences("absentapp",
						Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = myPrefs.edit();

				editor.putString("msg_des", messageList.get(position).message_desc);
				editor.commit();
				
				DialogMessage myDiag = new DialogMessage();
				myDiag.show(getFragmentManager(), "Diag");
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

		getmessage_url = ApplicationData.main_url
				+ "get_all_message_onid.php?userid=" + child_id;

		Log.d("xxxx", "getmessage_url  " + getmessage_url);

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

								JSONObject All_Messages = response
										.getJSONObject("All Messages");

								JSONArray received = All_Messages
										.getJSONArray("received");

								messageList = new ArrayList<Childbeans>();

								for (int i = 0; received.length() > i; i++) {

									JSONObject c = received.getJSONObject(i);
									Childbeans childbeans = new Childbeans();
									childbeans.message_id = c
											.getString("message_id");
									childbeans.sendername = c
											.getString("fromname");
									childbeans.subject_name = c
											.getString("message_subject");
									
									childbeans.message_desc = c
											.getString("mm.message_desc");

									childbeans.senderimage = c
											.getString("image");

									messageList.add(childbeans);
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

	protected void setlist_toadapter() {
		// TODO Auto-generated method stub
		adapter = new TeacherListAdapter(getActivity(), messageList);
		_teacherlist.setAdapter(adapter);
	}

}
