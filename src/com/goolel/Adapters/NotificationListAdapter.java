package com.goolel.Adapters;

import java.util.ArrayList;

import com.goolel.R;
import com.goolel.Entities.NotifyList;
import com.goolel.Entities.VideoList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotificationListAdapter extends BaseAdapter{

	ArrayList<NotifyList> arraylist;
	LayoutInflater inflater;
	Context context;
	
	public NotificationListAdapter(Context context,  ArrayList<NotifyList> arraylist) {
		this.context = context;
		this.arraylist = arraylist;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		if (arraylist != null) {
			return arraylist.size();
		}
		return 0;
	}

	@Override
	public NotifyList getItem(int position) {
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.notification_item_list_layout,
					parent, false);
			holder.textView = (TextView) convertView
					.findViewById(R.id.textview_notificationItem_message);
			convertView.setTag(holder);
		}else {
			holder = (Holder) convertView.getTag();
		}
		NotifyList rowItem =  getItem(position);
	    holder.textView.setText(rowItem.getNotify_msg());
		return convertView;
	}
	public static class Holder {
		public TextView textView;
	}
}
