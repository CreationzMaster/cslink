package com.kids.apps;

import static com.kids.apps.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.kids.apps.CommonUtilities.SENDER_ID;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends Activity {

	private EditText et_emailid, et_password;
	private String login_url;
	ProgressDialog pDialog;
	int register = 0;
	String _parent_id, child_array, email_id;
	String regid, language;
	private RelativeLayout forgot_password;
	private String msg_network, msg_dialog, msg_response, msg_validemail,
			msg_email, msg_password,parent_name;
	private TextView textlogin, textforgot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		
		
		
		
		

		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);
		//................................//

		_parent_id = sharedpref.getString("parent_id", "");

		if (_parent_id.equalsIgnoreCase("")) {

			findview();

			// ........languagechnage............//
			language = sharedpref.getString("language", "");
			if (language.equalsIgnoreCase("english")) {

				textlogin.setText("LOGIN");
				textforgot.setText("Forgot Password");
				msg_network = "Network error";
				msg_dialog = "Wait for server Response!";
				msg_response = "Please fill valid login credentials";
				msg_validemail = "Invalid email address";
				msg_email = "Please enter email id";
				msg_password = "Please enter password";

				et_password.setHint("Password");

			}

			else if (language.equalsIgnoreCase("nowrgian")) {
				textlogin.setText("Logg inn");
				textforgot.setText("Glemt passord");
				msg_network = " nettverksfeil";
				msg_dialog = "Vent til server respons!";
				msg_response = "Vennligst fyll gyldig påloggingsinformasjon";
				msg_validemail = "Ugyldig e-post adresse";
				msg_email = "Skriv inn email-id";
				msg_password = "Skriv inn passord";

				et_password.setHint("passord");
			}

			else {
				textlogin.setText("LOGIN");
				textforgot.setText("Forgot Password");
				msg_network = "Network error";
				msg_dialog = "Wait for server Response!";
				msg_response = "Please fill valid login credentials";
				msg_validemail = "Invalid email address";
				msg_email = "Please enter email id";
				msg_password = "Please enter password";

				et_password.setHint("Password");
			}
		}

		else {

			register = 1;

			registerdevice();

			child_array = sharedpref.getString("child_array", "");
			Intent in = new Intent(LoginActivity.this,
					SelectChildActivity.class);
			in.putExtra("childArray", child_array);
			startActivity(in);
			finish();

		}

	}

	private void findview() {
		// TODO Auto-generated method stub

		textlogin = (TextView) findViewById(R.id.textView2);
		textforgot = (TextView) findViewById(R.id.textView1);

		forgot_password = (RelativeLayout) findViewById(R.id.forgot_password);
		forgot_password.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent in = new Intent(LoginActivity.this, ForgotPassword.class);
				startActivity(in);
				finish();
			}
		});

		et_emailid = (EditText) findViewById(R.id.et_emailid);
		// et_emailid.setText("Singh@appsbusinesstore.com");
		et_password = (EditText) findViewById(R.id.et_password);
		// et_password.setText("wPGpHyrna6");
		// harinder.skywaltzlabs@gmail.com
		// uviNzhT5L1
		// Singh@appsbusinesstore.com
		// wPGpHyrna6
		// S9oPN1JmtX
	}

	// ....on click on login button..//

	public void login(View v) {

		if (et_emailid.getText().toString().length() > 0) {

			if (et_password.getText().toString().length() > 0) {

			String	email_idd = et_emailid.getText().toString();

				if (isValidEmail(email_idd)) {
					login_url = ApplicationData.main_url
							+ "get_parent_schools.php?email="
							+ et_emailid.getText().toString() + "&password="
							+ et_password.getText().toString();

					loginapp(login_url);

				}

				else {
					Toast.makeText(this, msg_validemail, Toast.LENGTH_LONG)
							.show();

				}

			}

			else {

				Toast.makeText(this, msg_password, Toast.LENGTH_LONG).show();
			}
		}

		else {

			Toast.makeText(this, msg_email, Toast.LENGTH_LONG).show();
		}

	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(target)
						.matches();
	}

	private void loginapp(String login_url2) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);
		pDialog.setMessage(msg_dialog);
		pDialog.show();

		RequestQueue queue = Volley.newRequestQueue(this);

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

								if (response.isNull("All Childs")) {

									String parent_id = response
											.getString("parentid");
									
									

									child_array = "";

									addprefrences(parent_id, child_array);

									register = 1;

									registerdevice();

								}

								else {

									String parent_id = response
											.getString("parentid");
									
									email_id=response
											.getString("parent email");
									parent_name=response
											.getString("parent name");

									JSONObject allchilds = response
											.getJSONObject("All Childs");
									JSONArray childs = allchilds
											.getJSONArray("childs");

									child_array = childs.toString();

									addprefrences(parent_id, child_array);

									register = 1;

									registerdevice();

								}

							}

							else {

								Toast.makeText(LoginActivity.this,
										msg_response, Toast.LENGTH_SHORT)
										.show();

								et_emailid.setText("");
								et_password.setText("");

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

						Toast.makeText(LoginActivity.this, msg_network,
								Toast.LENGTH_LONG).show();
					}
				});

		queue.add(jsObjRequest);
	}

	protected void registerdevice() {
		// TODO Auto-generated method stub

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);

		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));

		// Get GCM registration id
		String regid = GCMRegistrar.getRegistrationId(this);
		// Log.d("", "regIdregIdregIdregId" + regid);

		if (regid.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, SENDER_ID);
			// GCMRegistrar.unregister(this);

		} else {

			// Log.d("", "regIdregIdregIdregId" + regid);
			// ........................sharedpreferences.......................//
			SharedPreferences myPrefs = getSharedPreferences("absentapp",
					Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = myPrefs.edit();

			// editor.putString("logintype", "user");

			editor.putString("registrationId", regid);

			editor.commit();

			if (pDialog != null) {

				if (pDialog.isShowing()) {

					pDialog.dismiss();

					Intent in = new Intent(LoginActivity.this,
							SelectChildActivity.class);
					in.putExtra("childArray", child_array);
					startActivity(in);
					finish();
				}

			}

			else {
				// do nothing

			}

			if (GCMRegistrar.isRegisteredOnServer(this)) {

			} else {

				// GCMRegistrar.unregister(this);

			}

		}
	}

	protected void addprefrences(String parent_id, String child_array2) {
		// TODO Auto-generated method stub
		// ........................sharedpreferences.......................//
		SharedPreferences myPrefs = getSharedPreferences("absentapp",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();

		editor.putString("parent_id", parent_id);
		editor.putString("parent_emailid", email_id);
		editor.putString("child_array", child_array2);
		editor.putString("parent_name", parent_name);
		if (language.equalsIgnoreCase("")) {

			editor.putString("language", "english");
		}

		editor.putString("notification", "0");
		editor.commit();
	}

	// ....on click on forgot password..//
	public void forgotpassword(View v) {

		Intent in = new Intent(LoginActivity.this, ForgotPassword.class);

		startActivity(in);
		finish();

	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping

			WakeLocker.acquire(getApplicationContext());

			WakeLocker.release();
		}
	};

	@Override
	protected void onDestroy() {

		super.onDestroy();

		if (register == 1) {

			unregisterReceiver(mHandleMessageReceiver);

		}

	}

}
