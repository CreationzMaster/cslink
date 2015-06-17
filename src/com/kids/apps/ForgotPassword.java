package com.kids.apps;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class ForgotPassword extends Activity {

	private EditText email_id;
	private String login_url;
	ProgressDialog pDialog;
	private String msg_network, msg_dialog, msg_response, msg_validemail,
	msg_sucess, msg_fail,language,msg_email;
	private TextView header,enteremail,send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forgot_passsword);

		email_id = (EditText) findViewById(R.id.editText_email);
		
		header=(TextView)findViewById(R.id.textView1);
		enteremail=(TextView)findViewById(R.id.textViewemail);
		send=(TextView)findViewById(R.id.textView3);
		
		
		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);
		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {

			enteremail.setText("Enter Email id");
			send.setText("Send");
			header.setText("Forgot Password");
			msg_network = "Network error";
			msg_dialog = "Wait for server Response!";
			msg_response = "Please fill valid login credentials";
			msg_validemail = "Invalid email address";
			msg_email = "Please enter email id";
			msg_fail = "Email address does not exist";
			msg_sucess="New password has been sent to your mail id";

		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			enteremail.setText("Skriv inn email-ID");
			send.setText("Sende");
			header.setText("Glemt passord");
			msg_network = " nettverksfeil";
			msg_dialog = "Vent til server respons!";
			msg_response = "Vennligst fyll gyldig påloggingsinformasjon";
			msg_validemail = "Ugyldig e-post adresse";
			msg_email = "Skriv inn email-id";
			msg_fail = "E-postadressen finnes ikke";	
			msg_sucess="Nytt passord har blitt sendt til din e-id";
		}

		else {
			enteremail.setText("Enter Email id");
			send.setText("Send");
			header.setText("Forgot Password");
			msg_network = "Network error";
			msg_dialog = "Wait for sever Response!";
			msg_response = "Please fill valid login credentials";
			msg_validemail = "Invalid email address";
			msg_email = "Please enter email id";
			msg_fail = "Email address does not exist";
			msg_sucess="New password has been sent to your mail id";
		}
	}
	

	public void send(View v) {

		if (email_id.getText().length() > 0) {
			
			
			if (isValidEmail(email_id.getText().toString())) {
				
				login_url = ApplicationData.main_url
						+ "forgot_password.php?email="
						+ email_id.getText().toString();
				forgotpassword(login_url);
			}

			else {
				Toast.makeText(this,msg_validemail,
						Toast.LENGTH_LONG).show();

			
			
			
			}
			
			
			

			
		}

		else {

			Toast.makeText(this,msg_email, Toast.LENGTH_SHORT)
					.show();
		}

	}

	public void back(View v) {

		Intent in = new Intent(ForgotPassword.this, LoginActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);

		startActivity(in);
		finish();

	}

	private void forgotpassword(String login_url2) {
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
							String msg;
							if (Integer.parseInt(flag) == 1) {
								pDialog.dismiss();
								;
								alertdialog(msg_sucess);

							}

							else {
								pDialog.dismiss();
								
								alertdialog(msg_fail);

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

						Toast.makeText(ForgotPassword.this,msg_network,
								Toast.LENGTH_SHORT).show();
					}
				});

		queue.add(jsObjRequest);
	}

	protected void alertdialog(String msg) {
		// TODO Auto-generated method stub
		AlertDialog alertbox = new AlertDialog.Builder(this).setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {

					// do something when the button is clicked
					public void onClick(DialogInterface arg0, int arg1) {

						Intent in = new Intent(ForgotPassword.this,
								LoginActivity.class);
						in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);

						startActivity(in);
						finish();
					}
				})

				.show();
	}
	
	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(target)
						.matches();
	}

}
