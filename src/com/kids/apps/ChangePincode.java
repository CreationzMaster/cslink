package com.kids.apps;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class ChangePincode extends Activity {

	EditText _current_pincode, _new_pincode, _confirm_pincode;

	ImageView _back;

	LinearLayout send;

	private String login_url;

	ProgressDialog pDialog;

	TextView lang_header;

	String _parent_id, language, msg_fill, msg_confirm, msg_network,
			msg_dialog,msg_fail,msg_sucess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.changepin_code);

		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);

		_parent_id = sharedpref.getString("parent_id", "");

		findview();

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {
			lang_header.setText("Change Pincode");
			_current_pincode.setHint("Current Pincode");
			_new_pincode.setHint("New Pincode");
			_confirm_pincode.setHint("Confirm Pincode");
			msg_network = "Network error";
			msg_dialog = "Wait for server Response!";
			msg_fill = "Please fill out the required fields";
			msg_confirm = "New password and confirm pasword is not equal";
			msg_fail = "Password not changed";
			msg_sucess="Password has been changed";

		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			lang_header.setText("Endre PIN-kode");
			_current_pincode.setHint("Gammel PIN-kode");
			_new_pincode.setHint("Ny PIN-kode");
			_confirm_pincode.setHint("Bekreft PIN-kode");
			msg_network = " nettverksfeil";
			msg_dialog = "Vent til server respons!";
			msg_fill = "Vennligst fyll ut de nødvendige feltene";
			msg_confirm = "Nytt passord og bekreft pasword er ikke lik";
			msg_fail = "Passord ikke endret";
			msg_sucess="Passord har blitt endret";
		}

	}

	private void findview() {
		// TODO Auto-generated method stub

		// ........TextView......//
		lang_header = (TextView) findViewById(R.id.textView1);

		_current_pincode = (EditText) findViewById(R.id.editText_currentpincode);
		_new_pincode = (EditText) findViewById(R.id.editText_newpincode);
		_confirm_pincode = (EditText) findViewById(R.id.editText_confirmpincode);

		_back = (ImageView) findViewById(R.id.imgback);
		_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		send = (LinearLayout) findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				send_password();

			}
		});
	}

	protected void send_password() {
		// TODO Auto-generated method stub

		if ((_current_pincode.getText().length() > 0)
				&& (_new_pincode.getText().length() > 0)
				&& (_confirm_pincode.getText().length() > 0)) {

			if (_new_pincode.getText().toString()
					.equalsIgnoreCase(_confirm_pincode.getText().toString())) {

				login_url = ApplicationData.main_url
						+ "change_password.php?userid=" + _parent_id
						+ "&oldpassword="
						+ _current_pincode.getText().toString()
						+ "&newpassword=" + _new_pincode.getText().toString();
				changepassword(login_url);
			}

			else {

				Toast.makeText(this,
						msg_confirm,
						Toast.LENGTH_SHORT).show();
			}

		}

		else {

			Toast.makeText(this,msg_fill,
					Toast.LENGTH_SHORT).show();
		}
	}

	private void changepassword(String login_url2) {
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
								pDialog.dismiss();
								
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

						Toast.makeText(ChangePincode.this, msg_network,
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

					}
				})

				.show();
	}

}
