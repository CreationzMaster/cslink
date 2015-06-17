package com.kids.apps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AdapterHomeScreen extends BaseAdapter {
    String[] tag;
    Integer[] image;
    
    ViewHolder holder;
    
  
Context myc;
     public AdapterHomeScreen(Context c,String[] date, Integer[] drawerlist_image) {
     	
    	 tag=date;
    	 image=drawerlist_image;
    	
        myc=c;
           
     }

    
     public int getCount() {
       
         return tag.length;
     }

     public Object getItem(int arg0) {
       
         return null;
     }

     public long getItemId(int position) {
    
         return position;
     }

     static class ViewHolder{
     
     	TextView date;
     ImageView img;
     	
     
     }
     	@Override
     	public View getView(final int pos, View convertview, ViewGroup arg2) {
     		
     		LayoutInflater li=(LayoutInflater)myc.getSystemService(myc.LAYOUT_INFLATER_SERVICE);
     		
     		if(convertview==null){
     			convertview=li.inflate(R.layout.homescreenlisttile, null);
     			
     			
     			holder=new ViewHolder();
     			holder.date=(TextView)convertview.findViewById(R.id.textView1);
     			holder.img=(ImageView)convertview.findViewById(R.id.imageView1);
     		
     			convertview.setTag(holder);
     		
     		}
     		else
     			
     			
     		holder=(ViewHolder)convertview.getTag();
     		holder.date.setText(tag[pos]);
     		
     		int sdk = android.os.Build.VERSION.SDK_INT;

				if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					holder.img.setBackgroundDrawable(myc.getResources()
							.getDrawable(image[pos]));
				} else {
					holder.img.setBackground(myc.getResources().getDrawable(
							image[pos]));
				}
		
 			
     		return convertview;
     		
     		
     	}
     	
   
}