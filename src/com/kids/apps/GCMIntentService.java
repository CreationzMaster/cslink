package com.kids.apps;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import static com.kids.apps.CommonUtilities.SENDER_ID;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	String devicetoken;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	static Intent mainintent;

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		
//		Log.i(TAG, "Device registered: regId = " + registrationId);

		// ........................sharedpreferences.......................//
		SharedPreferences myPrefs = getSharedPreferences("absentapp",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();

		// editor.putString("logintype", "user");

		editor.putString("registrationId", registrationId);

		editor.commit();

		Intent in = new Intent(context, SelectChildActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(in);

		// pDialog.dismiss();

	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");

	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");

		mainintent = intent;

		String message = intent.getExtras().getString("message");

		SharedPreferences sharedpref = getSharedPreferences("absentapp", 0);

		String _notification = sharedpref.getString("notification", "");

		if (_notification.equalsIgnoreCase("0")) {

			generateNotification(context, message);

		}

		else {

		}

	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i("checked", "Received deleted messages notification");

		// generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i("checked", "Received error: " + errorId);
		// displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i("checked", "Received recoverable error: " + errorId);
		// displayMessage(context,
		// getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressLint("NewApi")
	private static void generateNotification(Context context, String message) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent notificationIntent = new Intent(context, LoginActivity.class);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder notification = new Notification.Builder(context)

		.setContentTitle("CSlink Message").setSmallIcon(R.drawable.logo)
				.setContentText(message).setContentIntent(intent)
				.setAutoCancel(true);

		Notification notificationn = notification.getNotification();

		notificationn.defaults |= Notification.DEFAULT_SOUND;

		notificationn.defaults |= Notification.DEFAULT_VIBRATE;
		notificationManager.notify(0, notificationn);

	}

}