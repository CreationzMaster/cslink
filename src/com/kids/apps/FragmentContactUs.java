package com.kids.apps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.adapter.Childbeans;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FragmentContactUs extends Fragment {

	private EditText message;

	String url, email_id,language;

	private LinearLayout send;
	ProgressDialog pDialog;
	private String msg_network, msg_dialog, msg_response, msg_validemail,
	msg_sucess, msg_fail, msg_message,parent_name,parent_email;

	public FragmentContactUs() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// setActionBArStyle();
		View rootView = inflater.inflate(R.layout.contactus_fragment,
				container, false);

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);

		email_id = sharedpref.getString("parent_emailid", "");
		parent_name= sharedpref.getString("parent_name", "");
		
		

		message = (EditText) rootView.findViewById(R.id.editText1);
		
		// ........languagechnage............//
				language = sharedpref.getString("language", "");
				if (language.equalsIgnoreCase("english")) {
					message.setHint("Message");
					
					msg_network = "Network error";
					msg_dialog = "Wait for server Response!";

					msg_message = "Please Enter the message.";
					msg_fail = "Message not sent ";
					msg_sucess = "Thanks for your feedback!";
					

				}

				else if (language.equalsIgnoreCase("nowrgian")) {
					message.setHint("Meldinger");
					
					msg_network = "nettverksfeil";
					msg_dialog = "Vent til server respons!";

					msg_message = "Skriv inn meldingen.";
					msg_fail = "Melding ikke sendt";
					msg_sucess = "Takker for din tilbakemeldling ";
				}

		send = (LinearLayout) rootView.findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (message.getText().length() > 0) {

					String mess_age = null;

					try {
						mess_age = URLEncoder.encode(message.getText()
								.toString(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					

					url = ApplicationData.main_url + "contact_us.php?email="
							+ email_id + "&name=" +parent_name+ "&message="
							+ mess_age;
				

					send_message(url);
				}

				else {

					Toast.makeText(getActivity(),msg_message,
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		return rootView;
	}

	private void send_message(String login_url2) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(getActivity());
		pDialog.setCancelable(false);
		pDialog.setMessage(msg_dialog);
		pDialog.show();

		RequestQueue queue = Volley.newRequestQueue(getActivity());

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, login_url2, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {

							String flag = response.getString("flag");

							if (Integer.parseInt(flag) == 1) {

								Toast.makeText(getActivity(),
										msg_sucess,
										Toast.LENGTH_SHORT).show();
								message.setText("");
								pDialog.dismiss();

							}

							else {

								Toast.makeText(getActivity(),
										msg_fail, Toast.LENGTH_SHORT)
										.show();

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
}
