package com.bookstore.app.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstore.app.activity.R;
import com.bookstore.app.adapters.AgentJobListAdapter.ViewHolder;
import com.bookstore.app.entities.AgentJobList;

public class AgentPendingJobListAdapter extends ArrayAdapter<AgentJobList>{
	
	Context context;
	ArrayList<AgentJobList> agentJobList;
	AgentJobList agentJobListObject;

	public AgentPendingJobListAdapter(Context context, int resource,
			ArrayList<AgentJobList> objects) {
		super(context, resource, objects);
		this.context = context;
		agentJobList = objects;
	}

	@Override
	public int getCount() {
		return agentJobList.size();
	}

	@Override
	public AgentJobList getItem(int position) {
		return agentJobList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return agentJobList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View jobListView = convertView;
		final ViewHolder holder;
		try{
			agentJobListObject = agentJobList.get(position);
			if(convertView == null){
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				jobListView = inflater
						.inflate(R.layout.agent_job_list_item, null);
				holder = new ViewHolder();
				holder.bookPic = (ImageView) jobListView.findViewById(R.id.bookPic);
				holder.bookName = (TextView) jobListView.findViewById(R.id.bookName);
				holder.bookQuantity = (TextView) jobListView.findViewById(R.id.bookQuantity);
				holder.status = (TextView) jobListView.findViewById(R.id.status);
				jobListView.setTag(holder);
			}else{
				holder = (ViewHolder) jobListView.getTag();
			}
			holder.bookName.setText(agentJobListObject.BookName);
			holder.bookQuantity.setText("Quantity :"+agentJobListObject.Quantity);
			holder.status.setText("Pending");
		}catch(Exception ex){
			Log.e("BS", ex.getMessage());
		}
		return jobListView;
	}
	
	class ViewHolder{
		public ImageView bookPic;
		public TextView bookName;
		public TextView bookQuantity;
		public TextView status;
	}

}