package com.kids.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateProfile extends Activity {

	protected static final int REQUEST_CAMERA = 1;
	private ProgressDialog dialog;
	protected static final int SELECT_FILE = 2;
	int imageclick = 0;
	Integer angle = 0;
	ProgressDialog pDialog;
	String Response;
	private String login_url, child_name, child_id, image;
	private TextView lang_header, lang_school, lang_mobile, lang_parent1,
			lang_parent2, lang_email1, lang_email2;

	private EditText _childname, child_parent1, child_parent2, child_email1,
			child_email2, child_schoolid, child_mobile;

	private String msg_network, msg_dialog, msg_response, msg_validemail1,
			msg_validemail2, msg_sucess, msg_fail, msg_email, msg_empty;

	private ImageView profile_pic;

	private RelativeLayout relative1;

	private LinearLayout update;
	String path = Environment.getExternalStorageDirectory().toString()
			+ File.separator + "yay.jpg";

	String path2 = Environment.getExternalStorageDirectory().toString()
			+ File.separator + "temp.jpg";

	String extStorageDirectory = Environment.getExternalStorageDirectory()
			.toString();
	private Bitmap bitmap;

	String parent1name, parent1email, parent2name, parent2email, childname,
			userid, schoolid, mobile, child_array, emailparent1, emailparent2,
			child_school_id, language;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.updateprofile);

		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);

		findView();

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {
			lang_header = (TextView) findViewById(R.id.textView1);
			lang_header.setText("Update Profile");
			lang_school = (TextView) findViewById(R.id.textView_class);
			lang_school.setText("School Name");

			lang_parent1 = (TextView) findViewById(R.id.textView_parent1);
			lang_parent1.setText("Parent 1");
			lang_parent2 = (TextView) findViewById(R.id.textView_parent2);
			lang_parent2.setText("Parent 2");
			lang_email1 = (TextView) findViewById(R.id.textView_email);
			lang_email1.setText("e-Mail");
			lang_email2 = (TextView) findViewById(R.id.textView_email2);
			lang_email2.setText("e-Mail");

			msg_dialog = "Wait for server Response!";
			msg_response = "Please fill valid login credentials";
			msg_validemail1 = "Parent1 invalid email address";
			msg_validemail2 = "Parent2 invalid email address";
			msg_email = "Please enter email id";
			msg_fail = "The profile is not updated";
			msg_sucess = "Update successfully";
			msg_empty = "Please enter your full details";
		}

		else if (language.equalsIgnoreCase("nowrgian")) {

			lang_header = (TextView) findViewById(R.id.textView1);
			lang_header.setText("Oppdater profil");
			lang_school = (TextView) findViewById(R.id.textView_class);
			lang_school.setText("skole Name");

			lang_parent1 = (TextView) findViewById(R.id.textView_parent1);
			lang_parent1.setText("Foresatte 1");
			lang_parent2 = (TextView) findViewById(R.id.textView_parent2);
			lang_parent2.setText("Foresatte 2");
			lang_email1 = (TextView) findViewById(R.id.textView_email);
			lang_email1.setText("Epost");
			lang_email2 = (TextView) findViewById(R.id.textView_email2);
			lang_email2.setText("Epost");

			msg_network = "nettverksfeil";
			msg_dialog = "Vent til server respons!";
			msg_response = "Vennligst fyll gyldig påloggingsinformasjon";
			msg_validemail1 = "Parent1 ugyldig e-postadresse";
			msg_validemail2 = "Parent2 ugyldig e-postadresse";
			msg_email = "Skriv inn email-id";
			msg_fail = "Profilen er ikke oppdatert";
			msg_sucess = "Info oppdatert";
			msg_empty = "Opplysninger mangler!";

		}

		// if(imageclick==0){
		//
		// loadimage(image);
		// }

	}

	private void findView() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();

		child_id = intent.getStringExtra("child_id");
		// ............Edit Text............//
		_childname = (EditText) findViewById(R.id.textView_childname);
		_childname.setText(intent.getStringExtra("childname"));
		_childname.setSelection(_childname.getText().length());

		child_parent1 = (EditText) findViewById(R.id.editText_parent1);
		child_parent1.setText(intent.getStringExtra("child_parent1"));
		child_parent1.setSelection(child_parent1.getText().length());

		child_parent2 = (EditText) findViewById(R.id.editText_parent2);
		child_parent2.setText(intent.getStringExtra("child_parent2"));
		child_parent2.setSelection(child_parent2.getText().length());

		child_email1 = (EditText) findViewById(R.id.editText_email);
		child_email1.setText(intent.getStringExtra("child_email1"));
		child_email1.setSelection(child_email1.getText().length());

		child_email2 = (EditText) findViewById(R.id.editText_email2);
		child_email2.setText(intent.getStringExtra("child_email2"));
		child_email2.setSelection(child_email2.getText().length());

		child_schoolid = (EditText) findViewById(R.id.editText_schoolid);
		child_schoolid.setText(intent.getStringExtra("schoolname"));
		child_schoolid.setSelection(child_schoolid.getText().length());

		child_mobile = (EditText) findViewById(R.id.editText_mobile);
		child_mobile.setText(intent.getStringExtra("mobile"));
		child_mobile.setSelection(child_mobile.getText().length());

		child_school_id = (intent.getStringExtra("schoolid"));

		image = (intent.getStringExtra("image"));

		update = (LinearLayout) findViewById(R.id.update);
		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					if (child_email1.getText().length() > 0
							&& child_email2.getText().length() > 0
							&& child_parent1.getText().length() > 0
							&& child_parent2.getText().length() > 0
							&& _childname.getText().length() > 0
							&& child_mobile.getText().length() > 0) {

						parent1name = URLEncoder.encode(child_parent1.getText()
								.toString(), "UTF-8");

						parent1email = URLEncoder.encode(child_email1.getText()
								.toString(), "UTF-8");
						parent2name = URLEncoder.encode(child_parent2.getText()
								.toString(), "UTF-8");

						parent2email = URLEncoder.encode(child_email2.getText()
								.toString(), "UTF-8");

						childname = URLEncoder.encode(_childname.getText()
								.toString(), "UTF-8");

						schoolid = URLEncoder.encode(child_school_id, "UTF-8");

						mobile = URLEncoder.encode(child_mobile.getText()
								.toString(), "UTF-8");
						userid = child_id;

						emailparent1 = child_email1.getText().toString();

						emailparent2 = child_email2.getText().toString();

						if (isValidEmail(emailparent1)) {

							if (isValidEmail(emailparent2)) {

								login_url = "http://skywaltzlabs.in/abscentapp/update_child_details.php";

								new register_user_runnner().execute();
							}

							else {
								Toast.makeText(UpdateProfile.this,
										msg_validemail2, Toast.LENGTH_SHORT)
										.show();

							}

						}

						else {
							Toast.makeText(UpdateProfile.this, msg_validemail1,
									Toast.LENGTH_SHORT).show();

						}

					}

					else {

						Toast.makeText(UpdateProfile.this, msg_empty,
								Toast.LENGTH_SHORT).show();
					}

				}

				catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		relative1 = (RelativeLayout) findViewById(R.id.relative1);
		relative1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImage();
			}
		});

		profile_pic = (ImageView) findViewById(R.id.imageView1);
		// profile_pic.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// selectImage();
		// }
		// });

	}

	public void back(View v) {

		Intent in = new Intent(UpdateProfile.this, MainActivity.class);

		in.putExtra("fragment", 1);
		// in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
		// Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(in);
		finish();

	}

	private void selectImage() {

		final CharSequence[] items = { "Take Photo", "Choose from Library",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(
				UpdateProfile.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file = new File(Environment
							.getExternalStorageDirectory()
							+ File.separator
							+ "yay.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
					// ******** code for crop image
					// intent.putExtra("crop", "true");
					// intent.putExtra("aspectX", 0);
					// intent.putExtra("aspectY", 0);
					// intent.putExtra("outputX", 500);
					// intent.putExtra("outputY", 450);

					intent.putExtra("return-data", true);
					startActivityForResult(intent, REQUEST_CAMERA);
				} else if (items[item].equals("Choose from Library")) {

					Intent gintent = new Intent();
					gintent.setType("image/*");
					gintent.setAction(Intent.ACTION_GET_CONTENT);

					File file = new File(Environment
							.getExternalStorageDirectory()
							+ File.separator
							+ "yay.jpg");
					gintent.putExtra("return-data", true);
					gintent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(file));
					startActivityForResult(
							Intent.createChooser(gintent, "Select File"),
							SELECT_FILE);
				} else if (items[item].equals("Cancel")) {
					imageclick = 0;
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				File f = new File(Environment.getExternalStorageDirectory()
						.toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("yay.jpg")) {
						f = temp;
						break;
					}
				}
				try {

					setimage(f.getAbsolutePath());

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (requestCode == SELECT_FILE) {
				Uri selectedImageUri = data.getData();

				String tempPath = getPath(selectedImageUri, UpdateProfile.this);

				setimage(tempPath);

			}
		}
	}

	private void setimage(String absolutePath) {
		// TODO Auto-generated method stub

		if (absolutePath != null) {

			ExifInterface ei = null;
			try {

				ei = new ExifInterface(absolutePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			switch (orientation) {

			case ExifInterface.ORIENTATION_ROTATE_90:

				angle = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:

				angle = 180;
				break;

			case ExifInterface.ORIENTATION_ROTATE_270:

				angle = 270;
				break;

			case 0:

				angle = 0;
				break;
			}

			try {

				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				options.inPurgeable = true;
				options.inInputShareable = true;

				Bitmap myBitmap = BitmapFactory.decodeFile(absolutePath,
						options);

				Matrix mat = new Matrix();
				mat.postRotate(angle);

				Bitmap captureBmp = Bitmap.createBitmap(myBitmap, 0, 0,
						myBitmap.getWidth(), myBitmap.getHeight(), mat, true);

				File file = new File(path);
				file.createNewFile();
				FileOutputStream ostream = new FileOutputStream(file);
				captureBmp.compress(CompressFormat.JPEG, 60, ostream);
				ostream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			decodeFile(path);
		} else {
			// bitmap = null;
		}

	}

	@SuppressLint("NewApi")
	public static String getPath(final Uri uri, Activity activity) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(activity, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(activity, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(activity, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(activity, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	public void decodeFile(String filePath) {
		// Decode image size

		Log.d("xxxxxxxx", "vcvvvv" + filePath);
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 512;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		bitmap = BitmapFactory.decodeFile(filePath, o2);

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int g;

		// if (w > h) {
		//
		// g = w;
		//
		// }
		//
		// else if (w < h){
		// g = h;
		// }
		// else {
		//
		// g = w;
		// }

		Bitmap sbmp;

		sbmp = Bitmap.createScaledBitmap(bitmap, w, h, false);

		// Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(sbmp, 100, 100);

		imageclick = 1;
		profile_pic.setImageBitmap(sbmp);

	}

	// FOR Firm pic upload class
	public class register_user_runnner extends AsyncTask<String, Void, Void> {

		DefaultHttpClient httpClient = null;
		InputStream inputStream = null;
		String registerResponse;
		String registerUrl;
		private String response_message;
		private String response_title;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			dialog = new ProgressDialog(UpdateProfile.this);
			dialog.setMessage(msg_dialog);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {

			try {

				String url = "http://skywaltzlabs.in/abscentapp/update_child_details.php?parent1name="
						+ parent1name
						+ "&parent1email="
						+ parent1email
						+ "&parent2name="
						+ parent2name
						+ "&parent2email="
						+ parent2email
						+ "&childname="
						+ childname
						+ "&userid="
						+ userid
						+ "&schoolid="
						+ schoolid
						+ "&mobile="
						+ mobile;

				Log.d("xxxxxx", "url" + url);

				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(url);
				// ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");

				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);

				if (imageclick == 0) {

					// File file1 = new File("");
					// FileBody bin1 = new FileBody(file1);
					// reqEntity.addPart("image",bin1);
				} else if (imageclick == 1) {

					File file = new File(path);
					FileBody bin = new FileBody(file);
					reqEntity.addPart("image", bin);
					postRequest.setEntity(reqEntity);
				}

				// reqEntity.addPart("parent1name", new
				// StringBody(parent1name));
				// reqEntity.addPart("parent1email", new
				// StringBody(parent1email));
				// reqEntity.addPart("parent2name", new
				// StringBody(parent2name));
				// reqEntity.addPart("parent2email", new
				// StringBody(parent2email));// emi_no
				// reqEntity.addPart("childname", new StringBody(childname));
				// reqEntity.addPart("userid", new StringBody(userid));
				// reqEntity.addPart("schoolid", new StringBody(schoolid));
				// reqEntity.addPart("mobile", new StringBody(mobile));

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

				// Log.d("xxxx", "Response  " + Response);

			} catch (Exception e) {
				// handle exception here
				Log.e(e.getClass().getName(), e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (Response != null) {

				try {
					JSONObject response = new JSONObject(Response);

					String flag = response.getString("flag");

					if (Integer.parseInt(flag) == 1) {

						JSONObject All_childs = response
								.getJSONObject("All childs");

						JSONArray childs = All_childs.getJSONArray("allchilds");

						child_array = childs.toString();

						addprefrences(child_array);

						Toast.makeText(UpdateProfile.this, msg_sucess,
								Toast.LENGTH_LONG).show();

						dialog.dismiss();

						Intent in = new Intent(UpdateProfile.this,
								MainActivity.class);

						in.putExtra("fragment", 1);
						// in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
						// Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(in);

						finish();

					}

					else {

						Toast.makeText(UpdateProfile.this, msg_fail,
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			else {

				Toast.makeText(UpdateProfile.this, msg_fail, Toast.LENGTH_SHORT)
						.show();
				dialog.dismiss();
			}

		}
	}

	protected void addprefrences(String parent_id) {
		// TODO Auto-generated method stub
		// ........................sharedpreferences.......................//
		SharedPreferences myPrefs = getSharedPreferences("absentapp",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();

		editor.putString("child_array", parent_id);
		editor.commit();
	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(target)
						.matches();
	}

	protected void loadimage(String string) {
		// TODO Auto-generated method stub
		Picasso.with(UpdateProfile.this)
				.load("http://skywaltzlabs.in/abscentappCI/uploads/" + string)
				// optional
				// optional
				.resize(200, 200) // optional
				// optional
				.error(R.drawable.download).into(profile_pic);
	}
}
