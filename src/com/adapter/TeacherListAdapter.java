package com.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kids.apps.R;
import com.squareup.picasso.Picasso;

public class TeacherListAdapter extends BaseAdapter {
	ArrayList<Childbeans> Date;

	ViewHolder holder;

	Context myc;

	public TeacherListAdapter(Context c, ArrayList<Childbeans> messageList) {

		myc = c;
		this.Date = messageList;
	}

	public int getCount() {

		return Date.size();
	}

	public Object getItem(int arg0) {

		return null;
	}

	public long getItemId(int position) {

		return position;
	}

	static class ViewHolder {

		TextView tachername, subject;

		ImageView profile_pic;

	}

	@Override
	public View getView(final int pos, View convertview, ViewGroup arg2) {

		LayoutInflater li = (LayoutInflater) myc
				.getSystemService(myc.LAYOUT_INFLATER_SERVICE);

		if (convertview == null) {
			convertview = li.inflate(R.layout.teacher_list_item, null);

			holder = new ViewHolder();
			holder.tachername = (TextView) convertview
					.findViewById(R.id.textView_teachername);
			holder.subject = (TextView) convertview
					.findViewById(R.id.textView_subject);

			holder.profile_pic = (ImageView) convertview
					.findViewById(R.id.imageView1);

			convertview.setTag(holder);

		} else {
			holder = (ViewHolder) convertview.getTag();
		}

		holder.tachername.setText(Date.get(pos).sendername);
		holder.subject.setText(Date.get(pos).subject_name);

//		Picasso.with(myc)
//				.load("http://skywaltzlabs.in/abscentappCI/uploads/"
//						+ Date.get(pos).senderimage)
//				// optional
//				// optional
//				.resize(100, 100) // optional
//				// optional
//				.into(holder.profile_pic);
		
		  
		 Glide.with(myc).load("http://skywaltzlabs.in/abscentappCI/uploads/"
					+ Date.get(pos).senderimage)
					.override(100, 100)
		            .fitCenter()
		            .error(R.drawable.download)
					.into(holder.profile_pic);
					

		return convertview;

	}

}