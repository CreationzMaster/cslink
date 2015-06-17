package com.kids.apps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class RegisterKid extends Activity {
	
	private ImageView back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register_kid);
		
		
		back=(ImageView)findViewById(R.id.imgback);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i=new Intent(RegisterKid.this, SelectChildActivity.class);
				startActivity(i);
				finish();
				
			}
		});
		
	}

}
