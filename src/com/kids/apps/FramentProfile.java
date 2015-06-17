package com.kids.apps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.adapter.Childbeans;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FramentProfile extends Fragment {

	private ImageView profile_pic;

	private LinearLayout update;

	private RelativeLayout edit_profile;

	boolean editprofile = false;

	ProgressDialog pDialog;
	private String login_url, child_name, child_id, schoolid, mobile,
			schoolname, language;

	private TextView _childname, child_parent1, child_parent2, child_email1,
			child_email2, child_class, child_classincharge, lang_header,
			lang_edit, lang_class, lang_classing, lang_parent1, lang_parent2,
			lang_email1, lang_email2;

	private String msg_network, msg_dialog, msg_response, msg_validemail,
			msg_sucess, msg_fail, msg_email, image;

	public FramentProfile() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.profile, container, false);

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);

		child_id = sharedpref.getString("childid", "");

		child_name = sharedpref.getString("childname", "");

		edit_profile = (RelativeLayout) rootView
				.findViewById(R.id.edit_profile);
		edit_profile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent in = new Intent(getActivity(), UpdateProfile.class);

				in.putExtra("childname", _childname.getText().toString());
				in.putExtra("child_parent1", child_parent1.getText().toString());
				in.putExtra("child_parent2", child_parent2.getText().toString());
				in.putExtra("child_email1", child_email1.getText().toString());
				in.putExtra("child_email2", child_email2.getText().toString());
				in.putExtra("schoolid", schoolid);
				in.putExtra("mobile", mobile);
				in.putExtra("child_id", child_id);
				in.putExtra("schoolname", schoolname);// image
				in.putExtra("image", image);
				startActivity(in);

				getActivity().finish();

			}
		});

		profile_pic = (ImageView) rootView.findViewById(R.id.imageView1);
		profile_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// selectImage();
			}
		});
		// .......TextView.........//

		_childname = (TextView) rootView.findViewById(R.id.textView_childname);
		child_parent1 = (TextView) rootView.findViewById(R.id.editText_parent1);
		child_parent2 = (TextView) rootView.findViewById(R.id.editText_parent2);
		child_email1 = (TextView) rootView.findViewById(R.id.editText_email);
		child_email1.setMovementMethod(new ScrollingMovementMethod());
		child_email2 = (TextView) rootView.findViewById(R.id.editText_email2);
		child_class = (TextView) rootView.findViewById(R.id.editText_class);
		child_classincharge = (TextView) rootView
				.findViewById(R.id.editText_classincharge);

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {
			lang_header = (TextView) rootView
					.findViewById(R.id.textView_childname);
			lang_edit = (TextView) rootView.findViewById(R.id.textView2);
			lang_edit.setText("Edit Profile");
			lang_class = (TextView) rootView.findViewById(R.id.textView_class);
			lang_class.setText("Class");
			lang_classing = (TextView) rootView
					.findViewById(R.id.textView_classincharge);
			lang_classing.setText("Class Incharge");

			lang_parent1 = (TextView) rootView
					.findViewById(R.id.textView_parent1);
			lang_parent1.setText("Parent 1");
			lang_parent2 = (TextView) rootView
					.findViewById(R.id.textView_parent2);
			lang_parent2.setText("Parent 2");
			lang_email1 = (TextView) rootView.findViewById(R.id.textView_email);
			lang_email1.setText("e-Mail");
			lang_email2 = (TextView) rootView
					.findViewById(R.id.textView_email2);
			lang_email2.setText("e-Mail");

			msg_network = "Network error";
			msg_dialog = "Wait for server Response!";
			msg_response = "Please fill valid login credentials";
			msg_validemail = "Invalid email address";
			msg_email = "Please enter email id";
			msg_fail = "Email address does not exist";
			msg_sucess = "New password has been sent to your mail id";
		}

		else if (language.equalsIgnoreCase("nowrgian")) {

			lang_header = (TextView) rootView
					.findViewById(R.id.textView_childname);
			lang_edit = (TextView) rootView.findViewById(R.id.textView2);
			lang_edit.setText("Rediger profil");
			lang_class = (TextView) rootView.findViewById(R.id.textView_class);
			lang_class.setText("Klasse");
			lang_classing = (TextView) rootView
					.findViewById(R.id.textView_classincharge);
			lang_classing.setText("Kontakt lærer");

			lang_parent1 = (TextView) rootView
					.findViewById(R.id.textView_parent1);
			lang_parent1.setText("Foresatte 1");
			lang_parent2 = (TextView) rootView
					.findViewById(R.id.textView_parent2);
			lang_parent2.setText("Foresatte 2");
			lang_email1 = (TextView) rootView.findViewById(R.id.textView_email);
			lang_email1.setText("Epost");
			lang_email2 = (TextView) rootView
					.findViewById(R.id.textView_email2);
			lang_email2.setText("Epost");

			msg_network = " nettverksfeil";
			msg_dialog = "Vent til server respons!";
			msg_response = "Vennligst fyll gyldig påloggingsinformasjon";
			msg_validemail = "Ugyldig e-post adresse";
			msg_email = "Skriv inn email-id";
			msg_fail = "E-postadressen finnes ikke";
			msg_sucess = "Nytt passord har blitt sendt til din e-id .";
		}

		// ................................//

		int foo = Integer.parseInt(child_id);

		login_url = ApplicationData.main_url
				+ "get_child_details_id.php?userid=" + foo;

		loginapp(login_url);

		return rootView;
	}

	private void selectImage() {

		final CharSequence[] options = { "Take Photo", "Choose from Gallery",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Photo!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File f = new File(android.os.Environment
							.getExternalStorageDirectory(), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, 1);
				} else if (options[item].equals("Choose from Gallery")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, 2);

				} else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == -1) {
			if (requestCode == 1) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				try {
					Bitmap bitmap;
					BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

					bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
							bitmapOptions);

					bitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, true);

					profile_pic.setImageBitmap(bitmap);

					String path = android.os.Environment
							.getExternalStorageDirectory()
							+ File.separator
							+ "Phoenix" + File.separator + "default";
					f.delete();
					OutputStream outFile = null;
					File file = new File(path, String.valueOf(System
							.currentTimeMillis()) + ".jpg");
					try {
						outFile = new FileOutputStream(file);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
						outFile.flush();
						outFile.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == 2) {

				Uri selectedImage = data.getData();
				String[] filePath = { MediaStore.Images.Media.DATA };
				Cursor c = getActivity().getContentResolver().query(
						selectedImage, filePath, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePath[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
				thumbnail = Bitmap
						.createScaledBitmap(thumbnail, 150, 150, true);

				profile_pic.setImageBitmap(thumbnail);
			}
		}

	}

	private void loginapp(String login_url2) {
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

								JSONObject allchilds = response
										.getJSONObject("Child");

								JSONArray childs = allchilds
										.getJSONArray("childs");

								for (int i = 0; i < childs.length(); i++) {
									JSONObject c = childs.getJSONObject(i);
									_childname.setText(c.getString("username"));
									child_parent1.setText(c
											.getString("parent1name"));
									child_parent2.setText(c
											.getString("parent2name"));
									child_email1.setText(c
											.getString("parent1email"));
									child_email2.setText(c
											.getString("parent2email"));
									child_class.setText(c
											.getString("class_name"));
									child_classincharge.setText(c
											.getString("teachername"));
									schoolid = c.getString("school_id");

									schoolname = c.getString("school_name");
									mobile = c.getString("mobile");

									image = c.getString("image");

									loadimage(c.getString("image"));

									//

								}

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

	protected void loadimage(String string) {
		// TODO Auto-generated method stub
		Picasso.with(getActivity())
				.load("http://skywaltzlabs.in/abscentappCI/uploads/" + string)
				// optional
				// optional
				// .resize(200, 200) // optional
				// optional
				.error(R.drawable.download).into(profile_pic);
	}
}
