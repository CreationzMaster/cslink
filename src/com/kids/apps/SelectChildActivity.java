package com.kids.apps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.Childbeans;
import com.adapter.SpinnerItem;

public class SelectChildActivity extends Activity {

	private Spinner childdropdown;

	ArrayList<Childbeans> arrayList = null;

	String[] videoNames;

	String child_name, child_id, child_array, school_id, Response = "",
			school_class_id;

	String login_url, parent_id, registrationid, language;

	ImageView drop;
	private AlertDialog myalertDialog = null;

	private RelativeLayout _done, _registerkid;

	ArrayList<SpinnerItem> items;
	ArrayAdapter<String> adapter;

	TextView selectchild, selectchild_list, _continue, logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.selectchild_activity);

		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);

		child_array = sharedpref.getString("child_array", "");
		parent_id = sharedpref.getString("parent_id", "");
		
		

		registrationid = sharedpref.getString("registrationId", "");

//		login_url = ApplicationData.main_url
//				+ "get_school_classes.php?schoolid=8";

		arrayList = new ArrayList<Childbeans>();

		items = new ArrayList<SpinnerItem>();

		if (child_array.equalsIgnoreCase("")) {

		}

		else {

			try {
				JSONArray jsonObj = new JSONArray(child_array);

				int adtuallength = jsonObj.length();

				videoNames = new String[adtuallength];
				for (int i = 0; i < adtuallength; i++) {

					Childbeans childbeans = new Childbeans();

					JSONObject c = jsonObj.getJSONObject(i);

					childbeans.child_image = c.getString("child_image");
					childbeans.child_name = c.getString("child_name");

					items.add(new SpinnerItem(c.getString("child_name"), false));
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

		}

		drop = (ImageView) findViewById(R.id.imageView1);
		drop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (videoNames == null) {

					// Toast.makeText(SelectChildActivity.this,
					// "No child register,please register the child",
					// Toast.LENGTH_SHORT).show();
				}

				else {

					if (videoNames.length > 0) {
						alertdialog();

					}

					else {

//						Toast.makeText(SelectChildActivity.this,
//								"No child register.",
//								Toast.LENGTH_SHORT).show();

					}

				}

			}
		});

		//text = (TextView) findViewById(R.id.textView2);

		selectchild = (TextView) findViewById(R.id.textView1);
		selectchild_list = (TextView) findViewById(R.id.textView2);
		_continue = (TextView) findViewById(R.id.textView3);
		logout = (TextView) findViewById(R.id.textView4);

		_registerkid = (RelativeLayout) findViewById(R.id.register_kid);
		_registerkid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialoglogout myDiag = new Dialoglogout();
				myDiag.show(getFragmentManager(), "Diag");
			}
		});

		_done = (RelativeLayout) findViewById(R.id.done);
		_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String child_name = selectchild_list.getText().toString();
				// ........................sharedpreferences.......................//

				if (child_name.equalsIgnoreCase("Select Student")) {

					Toast.makeText(SelectChildActivity.this,
							"Please select children from the list and continue.", Toast.LENGTH_SHORT)
							.show();
				}
				
				else if(child_name.equalsIgnoreCase("Velg student")){
					
					Toast.makeText(SelectChildActivity.this,
							"Vennligst velg barn fra listen og fortsette.", Toast.LENGTH_SHORT)
							.show();
				}

				else {

					SharedPreferences myPrefs = getSharedPreferences(
							"absentapp", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = myPrefs.edit();

					editor.putString("childname", child_name);
					editor.putString("childid", child_id);
					editor.putString("childArray", child_array);
					editor.putString("school_id", school_id);
					editor.putString("school_class_id", school_class_id);
					editor.commit();

					Intent in = new Intent(SelectChildActivity.this,
							MainActivity.class);

					in.putExtra("fragment", 0);

					startActivity(in);
					finish();
				}

			}
		});

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {
			selectchild.setText("Select Student");
			selectchild_list.setText("Select Student");
			_continue.setText("CONTINUE");
			logout.setText("LOGOUT");

		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			selectchild.setText("Velg elev");
			selectchild_list.setText("Velg elev");
			_continue.setText("Fortsette");
			logout.setText("Logg ut");
		}

		new SendRegistrationId().execute();

	}

	private void alertdialog() {
		// TODO Auto-generated method st

		final AlertDialog.Builder myDialog = new AlertDialog.Builder(
				SelectChildActivity.this);

		final ListView listview = new ListView(SelectChildActivity.this);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				child_name = arrayList.get(position).child_name;
				child_id = arrayList.get(position).user_id;
				school_id = arrayList.get(position).school_id;
				school_class_id = arrayList.get(position).school_class_id;
				selectchild_list.setText(child_name);

				myalertDialog.dismiss();
			}
		});
		;

		LinearLayout layout = new LinearLayout(SelectChildActivity.this);
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.addView(listview);
		myDialog.setView(layout);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.dropdown_childlist_item, videoNames);
		listview.setAdapter(adapter);

		myalertDialog = myDialog.show();
	}

	// ....on click on forgot password..//
	public void conti(View v) {

	}

	class SendRegistrationId extends AsyncTask<String, String, String> {

		boolean failure = false;
		String userttype;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... args) {

			try {

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(
						"http://skywaltzlabs.in/abscentapp/get_device_registr_id.php");

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				reqEntity.addPart("user_id", new StringBody(parent_id));
				reqEntity.addPart("device_regis_id", new StringBody(
						registrationid));

				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(
								response.getEntity().getContent(), "UTF-8"));
				String sResponse;
				StringBuilder s = new StringBuilder();

				while ((sResponse = reader.readLine()) != null) {
					s = s.append(sResponse);
				}

				Response = s.toString();
				System.out.println("Response: " + s);
			} catch (Exception e) {
				// handle exception here
				Log.e(e.getClass().getName(), e.getMessage());
			}

			return null;
		}

		// After completing background task Dismiss the progress dialog

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once product deleted

			JSONObject json = null;

			try {
				json = new JSONObject(Response);

				String flag = json.getString("flag");

				if (Integer.parseInt(flag) == 1) {

				}
			}

			catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

}
