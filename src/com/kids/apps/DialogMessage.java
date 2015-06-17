package com.kids.apps;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

public class DialogMessage extends DialogFragment {

	String language, msg_title, msg_message,_cancel;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		SharedPreferences sharedpref = getActivity().getSharedPreferences(
				"absentapp", 0);
		// ........languagechnage............//
		language = sharedpref.getString("language", "");
		msg_message = sharedpref.getString("msg_des", "");
		if (language.equalsIgnoreCase("english")) {

			msg_title = "Important notice!";
			
			_cancel="OK";
		}

		else if (language.equalsIgnoreCase("nowrgian")) {

			msg_title = "Viktig info!";
			_cancel="Ok";
			
		}
		return new AlertDialog.Builder(getActivity())
		.setIcon(R.drawable.aboutus_icon)
		// Set Dialog Title
		.setTitle(msg_title)
		// Set Dialog Message
		.setMessage(msg_message)

				// Negative Button
				.setNegativeButton(_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Do something else
							}
						}).create();
	}
}
