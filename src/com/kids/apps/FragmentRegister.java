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

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class FragmentRegister extends Fragment {

	String school_id, child_array, language;

	private EditText child_name;
	private RelativeLayout register, school_class;
	private String login_url, classid, registerurl, _parent_id;
	ProgressDialog pDialog;
	ArrayList<Childbeans> Listclasses = null;
	private String msg_network, msg_dialog,
			msg_sucess, msg_fail,msg_child, msg_class,
			msg_selectclass, msg_childname;
	TextView lang_register, lang_class;
	ImageView drop;
	private AlertDialog myalertDialog = null;

	public FragmentRegister() {
	}

	String[] class_id;
	private Spinner classes;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);

		_parent_id = sharedpref.getString("parent_id", "");
		school_id = sharedpref.getString("school_id", "");

		View rootView = inflater.inflate(R.layout.register_fragment, container,
				false);
		lang_class = (TextView) rootView.findViewById(R.id.textView2);
		lang_register = (TextView) rootView.findViewById(R.id.textView3);
		child_name = (EditText) rootView.findViewById(R.id.editText1);
		//child_name.setFilters(new InputFilter[]{filter}); 
		//child_name.setError(getErrorMsg("Only letters and numbers are allowed!"));
//		child_name.setFilters(new InputFilter[] {
//			    new InputFilter() {
//			       
//
//					@Override
//					public CharSequence filter(CharSequence source, int start,
//							int end, Spanned dest, int dstart, int dend) {
//						// TODO Auto-generated method stub
//						if(source.equals("")){ // for backspace
//							
//							childset();
//			                return source;
//			               
//			            }
//						else if(source.toString().matches("[a-zA-Z ]+")){
//			                return source;
//			            }
//			            
//			            else{
//			            	Toast.makeText(getActivity(),"Add only characters",
//									Toast.LENGTH_SHORT).show();
//			            	
//			            }
//			            return "";
//					}
//			    }
//			});

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {
			lang_class.setText("Class");
			lang_register.setText("Register");
			
			child_name.setHint("Please enter student name");

			msg_network = "Network error";
			msg_dialog = "Wait for server Response!";
			
			msg_fail = "Student already registered";
			msg_sucess = "New student has been registered!";
			msg_child = "No value for Child";
			msg_class = "No value for class";
			msg_selectclass = "Please select the class from the list.";
			msg_childname = "Please enter student name";

		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			lang_class.setText("Klasse");
			lang_register.setText("Registrer");
			
			child_name.setHint("Skriv elev navn");

			msg_network = " nettverksfeil";
			msg_dialog = "Vent til server respons!";
			
			msg_fail = "Eleven er registrert fra før!";
			msg_sucess = "Ny elev er registrert!";
			msg_child = "Ingen verdi for barn";
			msg_class = "Ingen verdi for klassen";
			msg_selectclass = "Vennligst velg klasse fra listen.";
			msg_childname = "Skriv elev navn";

		}

		drop = (ImageView) rootView.findViewById(R.id.imageView1);
		drop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (class_id == null) {

					Toast.makeText(getActivity(), msg_child, Toast.LENGTH_SHORT)
							.show();
				}

				else {

					if (class_id.length > 0) {
						alertdialog();
					} else {

						Toast.makeText(getActivity(), msg_class,
								Toast.LENGTH_SHORT).show();
					}

				}
			}
		});

		register = (RelativeLayout) rootView.findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (child_name.getText().length() > 0) {

					String class_name = lang_class.getText().toString();

					if (class_name.equalsIgnoreCase("Class")) {
						Toast.makeText(getActivity(), msg_selectclass,
								Toast.LENGTH_LONG).show();
					}

					else if (class_name.equalsIgnoreCase("Klasse")) {

						Toast.makeText(getActivity(), msg_selectclass,
								Toast.LENGTH_SHORT).show();
					} else {

						String kid_name = null;
						String class_id = null;
						try {
							kid_name = URLEncoder.encode(child_name.getText()
									.toString(), "UTF-8");
							class_id = URLEncoder.encode(classid, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						registerurl = ApplicationData.main_url
								+ "register_kid.php?parentid=" + _parent_id
								+ "&school_id=" + school_id + "&name="
								+ kid_name + "&class_id=" + class_id;

						register_kid(registerurl);

					}

				}

				else {

					Toast.makeText(getActivity(), msg_childname,
							Toast.LENGTH_SHORT).show();

				}
			}
		});

		school_class = (RelativeLayout) rootView
				.findViewById(R.id.school_class);
		school_class.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		classes = (Spinner) rootView.findViewById(R.id.spinner1);
		classes.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				classid = Listclasses.get(position).class_id;

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		login_url = ApplicationData.main_url
				+ "get_school_classes.php?schoolid=" + school_id;

		getschool_classes(login_url);

		return rootView;
	}

//	protected void childset() {
//		// TODO Auto-generated method stub
//		child_name.setText("");
//	}

	protected void register_kid(String registerurl2) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(getActivity());
		pDialog.setCancelable(false);
		pDialog.setMessage(msg_dialog);
		pDialog.show();

		RequestQueue queue = Volley.newRequestQueue(getActivity());

		JsonObjectRequest jsObjRequest = new JsonObjectRequest(
				Request.Method.GET, registerurl2, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub

						try {

							String flag = response.getString("flag");

							if (Integer.parseInt(flag) == 1) {

								JSONArray childs = response
										.getJSONArray("childs");

								child_array = childs.toString();

								addprefrences(child_array);

								pDialog.dismiss();

								Toast.makeText(getActivity(), msg_sucess,
										Toast.LENGTH_SHORT).show();

							}

							else {

								Toast.makeText(getActivity(), msg_fail,
										Toast.LENGTH_SHORT).show();

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

	private void getschool_classes(String login_url2) {
		// TODO Auto-generated method stub
		Listclasses = new ArrayList<Childbeans>();
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

								JSONObject allclasses = response
										.getJSONObject("All classes");

								JSONArray classes = allclasses
										.getJSONArray("classes");

								class_id = new String[classes.length()];
								for (int i = 0; i < classes.length(); i++) {
									JSONObject c = classes.getJSONObject(i);
									Childbeans childbeans = new Childbeans();
									childbeans.class_id = c
											.getString("class_id");
									childbeans.class_name = c
											.getString("class_name");
									class_id[i] = new String(c
											.getString("class_name"));

									Listclasses.add(childbeans);
									//

								}

								// addclasses_to_array();

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

	protected void addprefrences(String parent_id) {
		// TODO Auto-generated method stub
		// ........................sharedpreferences.......................//
		SharedPreferences myPrefs = getActivity().getSharedPreferences(
				"absentapp", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();

		editor.putString("child_array", parent_id);
		editor.commit();
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

				classid = Listclasses.get(position).class_id;

				String classname = Listclasses.get(position).class_name;

				lang_class.setText(classname);

				myalertDialog.dismiss();
			}
		});
		;

		LinearLayout layout = new LinearLayout(getActivity());
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.addView(listview);
		myDialog.setView(layout);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.dropdown_childlist_item, class_id);
		listview.setAdapter(adapter);

		myalertDialog = myDialog.show();
	}
	InputFilter filter = new InputFilter() { 
	    public CharSequence filter(CharSequence source, int start, int end, 
	Spanned dest, int dstart, int dend) { 
	            for (int i = start; i < end; i++) { 
	                    if ( !Character.isLetter(source.charAt(i)) || !Character.toString(source.charAt(i)) .equals("_") || !Character.toString(source.charAt(i)) .equals("-")) { 
	                            return ""; 
	                    } 
	            } 
	            return null; 
	    } 
	}; 
	
	
}
