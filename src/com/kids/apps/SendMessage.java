package com.kids.apps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SendMessage extends Activity {

	private EditText message;

	String url, email_id, childid, sender_id, sendername, language;

	private String msg_network, msg_dialog, msg_response, msg_validemail,
			msg_sucess, msg_fail, msg_message;

	TextView headername;

	ImageView back;
	private LinearLayout send;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sendmessage);

		Intent intent = getIntent();

		sender_id = intent.getStringExtra("sender_id");
		sendername = intent.getStringExtra("sendername");

		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);
		childid = sharedpref.getString("childid", "");

		email_id = sharedpref.getString("parent_emailid", "");

		message = (EditText) findViewById(R.id.editText1);
		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {
			message.setHint("Message");
			msg_network = "Network error";
			msg_dialog = "Wait for server Response!";

			msg_message = "Please Enter the message.";
			msg_fail = "Message not sent ";
			msg_sucess = "Message sent sucessfully";

		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			message.setHint("Meldinger");

			msg_network = "nettverksfeil";
			msg_dialog = "Vent til server respons!";

			msg_message = "Skriv inn meldingen.";
			msg_fail = "Melding ikke sendt";
			msg_sucess = "Melding sendt sucessfully";
		}

		send = (LinearLayout) findViewById(R.id.send);
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

				

					url = ApplicationData.main_url
							+ "send_messages.php?userfromid=" + childid
							+ "&usertoids=" + sender_id + "&messagebody="
							+ mess_age;

					

					send_message(url);
				}

				else {

					Toast.makeText(SendMessage.this,
							msg_message, Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		headername = (TextView) findViewById(R.id.textView1);
		headername.setText(sendername);

		back = (ImageView) findViewById(R.id.imgback);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(SendMessage.this, MainActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	private void send_message(String login_url2) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		pDialog.setMessage(msg_dialog);
		pDialog.show();

		RequestQueue queue = Volley.newRequestQueue(this);

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, login_url2, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {

							String flag = response.getString("flag");

							if (Integer.parseInt(flag) == 1) {

								Toast.makeText(SendMessage.this,
										msg_sucess,
										Toast.LENGTH_SHORT).show();
								message.setText("");
								pDialog.dismiss();

							}

							else {

								Toast.makeText(SendMessage.this,
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

						Toast.makeText(SendMessage.this, msg_network,
								Toast.LENGTH_SHORT).show();
					}
				});

		queue.add(jsObjRequest);

	}

}
