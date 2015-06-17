package com.kids.apps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.Childbeans;
import com.adapter.MyAdapter;
import com.adapter.TeacherListAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class ChatActivity extends Activity {

	String message_id, sender_id, sendername, sender_image, sender_subject,
			getmessage_url, _parent_id, sendmessage_url, sender_school,
			window_id, child_id;

	ImageView profileimage;
	TextView name, subject;
	ListView messagelistview;
	private Handler myHandler;
	// Runnable runnable;
	private ProgressDialog pDialog;
	MyAdapter adapter;
	EditText message;
	RelativeLayout message_send;
	int first_time = 0;
	private TextView school;
	int array_size, firsttime;
	private String msg_network, msg_dialog, language, msg_message;

	public final int updateFrequency = 7000; // In milli seconds
	ArrayList<Childbeans> recmsg = new ArrayList<Childbeans>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chatactivity);

		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);

		_parent_id = sharedpref.getString("parent_id", "");
		child_id = sharedpref.getString("childid", "");

		Intent intent = getIntent();
		// message_id = intent.getStringExtra("message_id");
		sender_id = intent.getStringExtra("sender_id");
		sendername = intent.getStringExtra("sendername");
		sender_image = intent.getStringExtra("sender_image");
		sender_subject = intent.getStringExtra("sender_subject");
		sender_school = intent.getStringExtra("sender_school");

		firsttime = 1;

		school = (TextView) findViewById(R.id.textView1);
		school.setText(sender_school);

		// .....................ListView................//

		messagelistview = (ListView) findViewById(R.id.lstMessages);
		adapter = new MyAdapter(ChatActivity.this, recmsg);

		message = (EditText) findViewById(R.id.editText1);
		message.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

				if (count == 0) {

					myHandler.removeCallbacksAndMessages(null);
				}

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		message_send = (RelativeLayout) findViewById(R.id.sendmessage);

		message_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String newString = message.getText().toString().trim();

				if (newString.length() > 0) {

					String mess_age = newString;

					addtoist(mess_age);
					// myHandler.removeCallbacksAndMessages(null);

					try {
						mess_age = URLEncoder.encode(message.getText()
								.toString(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// teacher_id

					// sendmessage_url = ApplicationData.main_url
					// + "message_reply.php?pri_messageid=" + message_id
					// + "&userid=" + _parent_id + "&message_body="
					// + mess_age;

					Log.d("xxxx", "xxxx" + window_id);

					sendmessage_url = ApplicationData.main_url
							+ "chat_reply.php?window_id=" + window_id
							+ "&userid=" + _parent_id + "&message_body="
							+ mess_age;

					message.setText("");

					Log.d("xxxx", "sendmessage_url" + sendmessage_url);

					send_message(sendmessage_url);
				}

				else {
					Toast.makeText(ChatActivity.this, msg_message,
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		profileimage = (ImageView) findViewById(R.id.imageView2);
		// Picasso.with(ChatActivity.this)
		// .load("http://skywaltzlabs.in/abscentappCI/uploads/"
		// + sender_image)
		// // optional
		// // optional
		// .resize(100, 100) // optional
		// // optional
		// .into(profileimage);

		Glide.with(ChatActivity.this)
				.load("http://skywaltzlabs.in/abscentappCI/uploads/"
						+ sender_image).override(100, 100).fitCenter()
						.error(R.drawable.download)
				.into(profileimage);
		name = (TextView) findViewById(R.id.textView2);
		name.setText(sendername);
		subject = (TextView) findViewById(R.id.textView3);
		subject.setText(sender_subject);

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {

			msg_network = "Network error";
			msg_dialog = "Wait for server Response!";
			msg_message = "Please Enter the message.";

		}

		else if (language.equalsIgnoreCase("nowrgian")) {

			msg_network = " nettverksfeil";
			msg_dialog = "Vent til server respons!";
			msg_message = "Skriv inn meldingen.";

		}

		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		pDialog.setMessage(msg_dialog);
		pDialog.show();

		myHandler = new Handler();
		scheduleSendLocation();
		// myHandler.postDelayed(delayedUpdateLooper, updateFrequency);
	}

	protected void addtoist(String mess_age) {
		// TODO Auto-generated method stub

		Calendar c = Calendar.getInstance();

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		// SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
		String formattedDate = dateFormat.format(c.getTime());

		Childbeans childbeans = new Childbeans();
		childbeans.message_id = "0";
		childbeans.user_id = "0";
		childbeans.name = "0";
		childbeans.image = "0";
		childbeans.message_body = mess_age;
		childbeans.created_at = "";
		childbeans.sender = "me";
		recmsg.add(childbeans);
		adapter.notifyDataSetChanged();

		array_size = recmsg.size();
		firsttime = 0;
	}

	public void scheduleSendLocation() {
		myHandler.postDelayed(new Runnable() {
			public void run() {

				first_time = first_time + 1;
				// getmessage_url = ApplicationData.main_url
				// + "message_view.php?parent_id=" + _parent_id
				// + "&message_id=" + message_id;

				

				getmessage_url = ApplicationData.main_url
						+ "chat_view.php?parent_id=" + _parent_id
						+ "&teacher_id=" + sender_id + "&kid_id="+child_id;

				getmessage(getmessage_url); // this method will contain your
											// almost-finished HTTP calls
				myHandler.postDelayed(this, updateFrequency);
			}
		}, updateFrequency);

	}

	// ....on click on forgot password..//
	public void back(View v) {

		Intent in = new Intent(this, MainActivity.class);
		startActivity(in);
		finish();

		myHandler.removeCallbacksAndMessages(null);

	}

	private void getmessage(String login_url2) {
		// TODO Auto-generated method stub

		RequestQueue queue = Volley.newRequestQueue(this);

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

								JSONObject result = response
										.getJSONObject("result");

								recmsg = new ArrayList<Childbeans>();

								window_id = result.getString("window_id");

								JSONArray reply = result.getJSONArray("reply");

								for (int i = 0; reply.length() > i; i++) {

									JSONObject c = reply.getJSONObject(i);
									Childbeans childbeans = new Childbeans();
									// childbeans.message_id = c
									// .getString("message_reply_id");
									childbeans.user_id = c.getString("from_id");
									childbeans.name = c.getString("name");
									// childbeans.image = c.getString("image");
									childbeans.message_body = c
											.getString("msg");
									childbeans.created_at = c.getString("time");
									childbeans.sender = c.getString("sender");
									recmsg.add(childbeans);
								}

								if (firsttime == 1) {
									adapter = new MyAdapter(ChatActivity.this,
											recmsg);
									messagelistview.setAdapter(adapter);

								}

								else if (firsttime == 0) {
									if (recmsg.size() >= array_size) {

										adapter = new MyAdapter(
												ChatActivity.this, recmsg);
										messagelistview.setAdapter(adapter);
										firsttime = 1;

									}

								}

								checkdialog();

							}

							else {

								JSONObject result = response
										.getJSONObject("result");

								window_id = result.getString("window_id");

								checkdialog();

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						checkdialog();

						Toast.makeText(ChatActivity.this, msg_network,
								Toast.LENGTH_SHORT).show();
					}
				});

		queue.add(jsObjRequest);
	}

	protected void checkdialog() {
		// TODO Auto-generated method stub
		if (pDialog != null && pDialog.isShowing()) {
			// is running
			pDialog.dismiss();
		}

	}

	protected void send_message(String sendmessage_url2) {
		// TODO Auto-generated method stub
		RequestQueue queue = Volley.newRequestQueue(this);
		// Log.e("URl", "" + sendmessage_url2);
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, sendmessage_url2, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {

							String flag = response.getString("flag");

							if (Integer.parseInt(flag) == 1) {

								scheduleSendLocation();

							}

							else {

								// pDialog.dismiss();

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						// pDialog.dismiss();

						Toast.makeText(ChatActivity.this, msg_network,
								Toast.LENGTH_SHORT).show();
					}
				});

		queue.add(jsObjRequest);
	}

	public void pause() {

	}

	@Override
	protected void onPause() {
		super.onPause();

		myHandler.removeCallbacksAndMessages(null);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	@Override
	protected void onStop() {
		super.onStop();

		myHandler.removeCallbacksAndMessages(null);

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	public void onBackPressed() {

		myHandler.removeCallbacksAndMessages(null);
		Intent in = new Intent(this, MainActivity.class);
		startActivity(in);
		finish();

	}

}
