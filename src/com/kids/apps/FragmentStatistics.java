package com.kids.apps;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import com.adapter.Childbeans;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentStatistics extends Fragment {

	public FragmentStatistics() {
	}

	private String getsubject_url, send_data_url, send_subject, _parent_id,
			school_id, child_id, language;
	private ProgressDialog pDialog;

	ArrayList<Childbeans> arrayList_subject = null;

	ArrayList<Childbeans> arrayList_templete = null;
	int _date, pp, create_pdf = 0;

	private TextView fromdate, todate, _day, _hours, lang_day, lang_hour,
			lang_absent,get_report;

	private LinearLayout _senddata, _create_pdf, from_date, to_date;

	String pdf_schoolname, pdf_email, pdf_phone, pdf_studentname, pdf_teacher,
			pdf_parentname, pdf_fromdate, pdf_todate, pdf_totaldays,
			pdf_totalhours, pdf_totalabsent, pdf_title, pdf_reason,
			_schoolname, _email, _phone, _studentname, _teacher, _parentname,
			_fromdate, _todate, _totaldays, _totalhours, _totalabsent, _reason;
	private String msg_network, msg_dialog, msg_pdf, msg_from, msg_to,
			msg_send_server, msg_no_info, msg_no_absent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);// childid

		_parent_id = sharedpref.getString("parent_id", "");
		school_id = sharedpref.getString("school_id", "");
		child_id = sharedpref.getString("childid", "");
		language = sharedpref.getString("language", "");

		// setActionBArStyle();
		View rootView = inflater.inflate(R.layout.statistics_fragment,
				container, false);

		// ....change text according to language..//

		// ..............TextView......//

		change_language();

		_senddata = (LinearLayout) rootView.findViewById(R.id.getdata);
		_senddata.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// ...............get subject URL.................//

				send_data();

				// if
				// (fromdate.getText().toString().equalsIgnoreCase("From Date"))
				// {
				//
				// Toast.makeText(getActivity(), msg_from, Toast.LENGTH_LONG)
				// .show();
				//
				// }
				//
				// else {
				//
				// if (todate.getText().toString().equalsIgnoreCase("To Date"))
				// {
				//
				// Toast.makeText(getActivity(), msg_to, Toast.LENGTH_LONG)
				// .show();
				//
				// }
				//
				// else {
				//
				// _fromdate = fromdate.getText().toString();
				// _todate = todate.getText().toString();
				//
				// getsubject_url = ApplicationData.main_url
				// + "statistics.php?child_id=" + child_id
				// + "&from_date=" + fromdate.getText().toString()
				// + "&to_date=" + todate.getText().toString();
				//
				// Log.d("xxxxxx", "xxxx  "+getsubject_url);
				//
				// get_subject(getsubject_url);
				//
				// }
				//
				// }

			}
		});

		from_date = (LinearLayout) rootView.findViewById(R.id.fromdate);
		from_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				_date = 1;

				datepicker(_date);

			}
		});
		to_date = (LinearLayout) rootView.findViewById(R.id.todate);
		to_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				_date = 2;

				datepicker(_date);
			}
		});

		_create_pdf = (LinearLayout) rootView.findViewById(R.id.pdf);
		_create_pdf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (create_pdf == 1) {

					String pdf_name = child_id + _fromdate + language;
					createPDF(pdf_name);

				}

				else {
					Toast.makeText(getActivity(), msg_send_server,
							Toast.LENGTH_LONG).show();

				}
			}
		});

		// ..............................TextView......//

		fromdate = (TextView) rootView.findViewById(R.id.textView_fromdate);
		todate = (TextView) rootView.findViewById(R.id.textView_todate);
		lang_day = (TextView) rootView.findViewById(R.id.textView_fromday);
		lang_hour = (TextView) rootView.findViewById(R.id.textView_tohour);
		lang_absent = (TextView) rootView.findViewById(R.id.textView1);
		get_report=(TextView) rootView.findViewById(R.id.textView2);
		_day = (TextView) rootView.findViewById(R.id.textView_day);
		_hours = (TextView) rootView.findViewById(R.id.textView_hours);

		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		if (language.equalsIgnoreCase("english")) {
			fromdate.setText("From date");
			todate.setText("To date");
			lang_day.setText("Day");
			lang_hour.setText("Hour");
			lang_absent.setText("Absent");
			get_report.setText("Get Report");
			msg_network = "Network error";
			msg_dialog = "Wait for server Response!";

			msg_pdf = "Pdf created sucessfully";
			msg_from = "Please select from date";
			msg_to = "Please select to date";
			msg_send_server = "Please send data first to server";
			msg_no_info = "There is no info for child";
			msg_no_absent = "No absent record";
		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			fromdate.setText("Fra dato");
			todate.setText("Til dato");
			lang_day.setText("Dag");
			lang_hour.setText("Time");
			lang_absent.setText("Fravær");
			get_report.setText("Hent rapport");
			msg_network = " nettverksfeil";
			msg_dialog = "Vent til server respons!";

			msg_pdf = "Pdf opprettet vellykket";
			msg_from = "Vennligst velg fra dato";
			msg_to = "Vennligst velg oppdatert";
			msg_send_server = "Vennligst send data først til serveren";
			msg_no_info = "Det er ingen info for barn";
			msg_no_absent = "Ingen fravær registrert";
		}

		return rootView;
	}

	protected void send_data() {
		// TODO Auto-generated method stub
		if (fromdate.getText().toString().equalsIgnoreCase("From Date")
				|| fromdate.getText().toString().equalsIgnoreCase("Fra Dato")) {

			Toast.makeText(getActivity(), msg_from, Toast.LENGTH_LONG).show();

		}

		else {

			if (todate.getText().toString().equalsIgnoreCase("To Date")
					|| todate.getText().toString()
							.equalsIgnoreCase("Til Dato")) {

				Toast.makeText(getActivity(), msg_to, Toast.LENGTH_LONG).show();

			}

			else {

				_fromdate = fromdate.getText().toString();
				_todate = todate.getText().toString();

				getsubject_url = ApplicationData.main_url
						+ "statistics.php?child_id=" + child_id + "&from_date="
						+ fromdate.getText().toString() + "&to_date="
						+ todate.getText().toString();

				

				get_subject(getsubject_url);

			}

		}
	}

	private void change_language() {
		// TODO Auto-generated method stub
		if (language.equalsIgnoreCase("english")) {
			pdf_title = "Absent report";
			pdf_schoolname = "School name = ";
			pdf_email = "eMail = ";
			pdf_phone = "Telephone = ";
			pdf_studentname = "Student name = ";
			pdf_teacher = "Teacher inCharge = ";
			pdf_parentname = "Parent name = ";
			pdf_fromdate = "From date = ";
			pdf_todate = "To date = ";
			pdf_totaldays = "Total days = ";
			pdf_totalhours = "Total hours = ";
			pdf_totalabsent = "Total absent = ";
			pdf_reason = "Reason = ";
		}

		else if (language.equalsIgnoreCase("nowrgian")) {
			pdf_title = "Fravær report";
			pdf_schoolname = "Skolenavn = ";
			pdf_email = "eMail = ";
			pdf_phone = "Telefon = ";
			pdf_studentname = "Elev navn = ";
			pdf_teacher = "Kontakt lærer = ";
			pdf_parentname = "Foresatte 1 = ";
			pdf_fromdate = "Fra dato = ";
			pdf_todate = "Tildato = ";
			pdf_totaldays = "Antalldager = ";
			pdf_totalhours = "Antall timer = ";
			pdf_totalabsent = "Total fravær = ";
			pdf_reason = "Årsak = ";
		}
	}

	protected void createPDF(String pdf_name) {
		// TODO Auto-generated method stub

		try {
			File file2 = null;
			File sdCard = Environment.getExternalStorageDirectory();
			// String fpath
			// =Environment.getExternalStorageDirectory()+"/CSlinkFolder/pdf"+pp+
			// ".pdf";
			File file = new File(sdCard.getAbsolutePath() + "/CSlinkFolder/pdf");

			file.mkdirs();
			// If file does not exists, then create it

			file2 = new File(file, pdf_name + ".pdf");
			if (!file2.exists()) {

				file2.createNewFile();

				// file.createNewFile();
			}

			Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 20,
					Font.UNDERLINE);
			Font grayFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
					Font.NORMAL, BaseColor.DARK_GRAY);

			Font blackFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
					Font.NORMAL);

			// step 1
			Document document = new Document();
			// step 2
			PdfWriter.getInstance(document,
					new FileOutputStream(file2.getAbsoluteFile()));
			// step 3
			document.open();
			// step 4
			Paragraph preface = new Paragraph();
			// We add one empty line
			addEmptyLine(preface, 1);
			// Lets write a big header

			// Will create: Report generated by: _name, _date
			preface.add(new Paragraph(pdf_schoolname + _schoolname, grayFont));
			preface.add(new Paragraph(pdf_email + _email, grayFont));
			preface.add(new Paragraph(pdf_phone + _phone, grayFont));

			addEmptyLine(preface, 1);

			preface.add(new Paragraph(pdf_studentname + _studentname, blackFont));
			preface.add(new Paragraph(pdf_teacher + _teacher, blackFont));
			preface.add(new Paragraph(pdf_parentname + _parentname, blackFont));

			addEmptyLine(preface, 2);

			preface.add(new Paragraph(pdf_title, catFont));
			preface.setAlignment(Element.ALIGN_MIDDLE);

			addEmptyLine(preface, 1);

			preface.add(new Paragraph(_fromdate
					+ "              to              " + _todate, blackFont));

			addEmptyLine(preface, 1);
			preface.add(new Paragraph(pdf_totaldays + _totaldays, blackFont));
			addEmptyLine(preface, 1);

			preface.add(new Paragraph(pdf_totalhours + _totalhours, blackFont));
			addEmptyLine(preface, 1);

			preface.add(new Paragraph(pdf_reason + _reason, blackFont));
			addEmptyLine(preface, 1);

			addEmptyLine(preface, 2);
			preface.add(new Paragraph(pdf_totalabsent + _totalabsent, blackFont));

			document.add(preface);
			// step 5
			document.close();

			Toast.makeText(getActivity(), msg_pdf, Toast.LENGTH_SHORT).show();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	protected void datepicker(int _date) {
		// TODO Auto-generated method stub
		DatePickerFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "DatePicker");
	}

	private void get_subject(String login_url2) {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(getActivity());
		pDialog.setCancelable(false);
		pDialog.setMessage(msg_dialog);
		pDialog.show();

		RequestQueue queue = Volley.newRequestQueue(getActivity());

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

								create_pdf = 1;

								_day.setText(response.getString("days"));
								_hours.setText(response.getString("hours"));

								if (response.isNull("absent_student_info")) {

									Toast.makeText(getActivity(), msg_no_info,
											Toast.LENGTH_SHORT).show();

								}

								else {

									JSONObject absent_student_info = response
											.getJSONObject("absent_student_info");

									_schoolname = absent_student_info
											.getString("school_name");
									_email = absent_student_info
											.getString("school_email");
									_phone = absent_student_info
											.getString("school_phone");
									_studentname = absent_student_info
											.getString("student_name");
									_teacher = absent_student_info
											.getString("teacher_name");
									_parentname = absent_student_info
											.getString("parent_name");
									_reason = absent_student_info
											.getString("reason");

									_totaldays = response.getString("days");
									_totalhours = response.getString("hours");
									_totalabsent = response
											.getString("total_absent_students");

								}

								pDialog.dismiss();

							}

							else {

								Toast.makeText(getActivity(), msg_no_absent,
										Toast.LENGTH_SHORT).show();

								_day.setText("");
								_hours.setText("");
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

	public class DatePickerFragment extends DialogFragment implements
			OnDateSetListener {
		private DatePickerDialog datepic;

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			datepic = new DatePickerDialog(getActivity(), this, year, month,
					day);

			datepic.getDatePicker().setMaxDate(c.getTimeInMillis());

			return datepic;
		}

		public void onDateChanged(DatePicker view, int year, int month, int day) {

		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {
			Activity a = getActivity();

			// String datedummy = String.valueOf(year) + "-"
			// + String.valueOf(month + 1) + "-" + String.valueOf(day);
			String finalDate = null;
			String datedummyy = String.valueOf(day) + "-"
					+ String.valueOf(month + 1) + "-" + String.valueOf(year);

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date myDate = null;
			try {
				myDate = dateFormat.parse(datedummyy);

				finalDate = dateFormat.format(myDate);

			} catch (ParseException e) {
				e.printStackTrace();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// ............set date to text when check in is select...//

			if (_date == 1) {
				fromdate.setText(finalDate);
			}

			else if (_date == 2) {

				todate.setText(finalDate);

				//send_data();
			}

		}

	}
}
