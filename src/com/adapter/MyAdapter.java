package com.adapter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kids.apps.ChatActivity;
import com.kids.apps.R;

public class MyAdapter extends BaseAdapter {
	// public ImageLoader imageLoader;

	private Activity activity;

	private LayoutInflater inflater = null;

	public ArrayList<Childbeans> _items;
	static int SECOND_MILLIS = 1000;
	static int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	static int HOUR_MILLIS = 60 * MINUTE_MILLIS;
	static int DAY_MILLIS = 24 * HOUR_MILLIS;

	public MyAdapter(ChatActivity chatActivity,
			ArrayList<Childbeans> InvitationList) {
		// TODO Auto-generated constructor stub
		this.activity = chatActivity;
		this._items = InvitationList;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// imageLoader = new ImageLoader(activity);
		// notifyDataSetChanged();

	}

	public int getCount() {

		return _items.size();
	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {

		return position;
	}

	// .............View holder use to hold the view comes in the form of
	// array list............//
	// ........................ Here we link the all the objects with the
	// xml class ...............//
	class ViewHolder {
		public TextView send_msg, receive_msg, date_send, date_receive;
		public LinearLayout layout_receive_msg, layout_send_msg;

	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder _holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.custom_list, null);
			_holder = new ViewHolder();
			_holder.send_msg = (TextView) convertView
					.findViewById(R.id.showingsend_msg);
			_holder.receive_msg = (TextView) convertView
					.findViewById(R.id.showingreceive_msg);

			_holder.date_receive = (TextView) convertView
					.findViewById(R.id.showingrecive_msg_date);

			_holder.date_send = (TextView) convertView
					.findViewById(R.id.showingsend_msg_date);

			_holder.layout_receive_msg = (LinearLayout) convertView
					.findViewById(R.id.show_recivemsg);
			_holder.layout_send_msg = (LinearLayout) convertView
					.findViewById(R.id.show_sendmsg);
			convertView.setTag(_holder);
		}

		else {
			_holder = (ViewHolder) convertView.getTag();
		}

		if (_items.get(position).sender.equals("me")) {
			_holder.layout_receive_msg.setVisibility(View.GONE);
			_holder.layout_send_msg.setVisibility(View.VISIBLE);

			String sm = _items.get(position).message_body;

			String time;
			if (_items.get(position).created_at.equalsIgnoreCase("")) {

				 time = "";
			} else {
				 time = getTimeAgo(gettime(_items.get(position).created_at));
			}

			
			_holder.send_msg.setText(sm);
			_holder.date_send.setText(time);

		}

		else if (_items.get(position).sender.equals("from")) {

			_holder.layout_receive_msg.setVisibility(View.VISIBLE);
			_holder.layout_send_msg.setVisibility(View.GONE);

			String sm = _items.get(position).message_body;
			
			String time;
			if (_items.get(position).created_at.equalsIgnoreCase("")) {

				 time = "";
			} else {
				 time = getTimeAgo(gettime(_items.get(position).created_at));
			}

			
			_holder.receive_msg.setText(sm);
			_holder.date_receive.setText(time);

		}
		

		return convertView;
	}

	private long gettime(String created_at) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		// SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm");
		long millis = 0;
		try {
			Date date;
			try {
				date = dateFormat.parse(created_at); // You will need try/catch
														// around this
				millis = date.getTime();
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ParseException e) {
		}

		return millis;
		// TODO Auto-generated method stub

	}

	public static String getTimeAgo(long time) {
		if (time < 1000000000000L) {

			time *= 1000;
		}

		long now = System.currentTimeMillis();
		if (time > now || time <= 0) {
			return null;
		}

		// TODO: localize
		final long diff = now - time;
		if (diff < MINUTE_MILLIS) {
			Date date = new Date();
			date.setTime(time);
			String formattedDate = new SimpleDateFormat("HH:mm").format(date);

			return formattedDate;

		} else if (diff < 2 * MINUTE_MILLIS) {
			Date date = new Date();
			date.setTime(time);
			String formattedDate = new SimpleDateFormat("HH:mm").format(date);

			return formattedDate;
		} else if (diff < 50 * MINUTE_MILLIS) {
			Date date = new Date();
			date.setTime(time);
			String formattedDate = new SimpleDateFormat("HH:mm").format(date);

			return formattedDate;
		} else if (diff < 90 * MINUTE_MILLIS) {
			Date date = new Date();
			date.setTime(time);
			String formattedDate = new SimpleDateFormat("HH:mm").format(date);

			return formattedDate;
		} else if (diff < 24 * HOUR_MILLIS) {
			Date date = new Date();
			date.setTime(time);
			String formattedDate = new SimpleDateFormat("HH:mm").format(date);

			return formattedDate;
		}
//		else if (diff < 48 * HOUR_MILLIS) {
//			//return "yesterday";
//			
//			Date date = new Date();
//			date.setTime(time);
//			String formattedDate = new SimpleDateFormat("HH:mm").format(date);
//
//			return formattedDate;
//		}

//		else if (diff / DAY_MILLIS < 8) {
//
//			return diff / DAY_MILLIS + " day ago";
//		}

		else {

			String pattern = "dd-MMM-yyyy";
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

			Date date = new Date(time);

			return dateFormat.format(date);
		}
	}

}